/**
 * Copyright (C) 2018 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.auditquery.olap.handler;

import org.commonjava.auditquery.cache.CacheProducer;
import org.commonjava.cdi.util.weft.ExecutorConfig;
import org.commonjava.cdi.util.weft.WeftExecutorService;
import org.commonjava.cdi.util.weft.WeftManaged;
import org.infinispan.Cache;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryExpired;
import org.infinispan.notifications.cachelistener.event.CacheEntryExpiredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class RetriableProcessor
{

    @Inject
    CacheProducer cacheProducer;

    @WeftManaged
    @Inject
    @ExecutorConfig( named = "callback-job-exec", threads = 10, maxLoadFactor = 100 )
    WeftExecutorService retryService;

    Cache<String, CallbackJob> retryCache;

    Logger logger = LoggerFactory.getLogger( getClass() );

    private final int MAX_RETRIES = 4;

    @PostConstruct
    public void init()
    {
        retryCache = cacheProducer.getCache( "callback-job-retry" );
        retryCache.addListener( new ExpireListener() );
    }

    @Listener
    class ExpireListener
    {
        @CacheEntryExpired
        public void onExpired( CacheEntryExpiredEvent<String, CallbackJob> event )
        {
            if ( event.isPre() )
            {
                return;
            }
            CallbackJob job = event.getValue();
            logger.info( "Cache entry {} expired and retry.", job.getJobId() );
            commit( job );
        }

    }

    public void commit( CallbackJob job )
    {
        retryService.submit( () -> {
            logger.info( "Exec job: {}, thread: {}", job.toString(), Thread.currentThread().getName() );

            if ( job.getRetryCount() < MAX_RETRIES )
            {
                Boolean isCompleted = job.start();

                if ( !isCompleted )
                {
                    delayJob( job );
                }
                else
                {
                    skipJob( job );
                }
            }
            else
            {
                logger.warn( "Tried more than {}: {}", MAX_RETRIES, job.getJobId() );
                skipJob( job );
            }
        } );

    }

    private void skipJob( CallbackJob job )
    {
        logger.info( "Skip job: {}", job.getJobId() );
    }

    private void delayJob( CallbackJob job )
    {
        logger.info( "Delay job: {} ", job.getJobId() );
        job.increaseRetryCount();
        retryCache.put( job.getJobId().toString(), job, job.getRetryCount(), TimeUnit.MINUTES );
    }

}
