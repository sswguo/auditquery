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
package org.commonjava.auditquery.cache;

import org.apache.commons.io.FileUtils;
import org.commonjava.auditquery.core.conf.DefaultAuditQueryConfig;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * This is a temporary implementation and it will be replaced by the one in propulsor
 */
@ApplicationScoped
public class CacheProducer
{

    @Inject
    DefaultAuditQueryConfig config;

    private static final String ISPN_XML = "infinispan.xml";
    private static final String ISPN_CLUSTER_XML = "infinispan-cluster.xml";

    EmbeddedCacheManager cacheManager;
    EmbeddedCacheManager clusterCacheManager;

    Logger logger = LoggerFactory.getLogger( getClass() );

    public CacheProducer()
    {

    }

    @PostConstruct
    public void start()
    {
        String configDir = config.getConfigDir();
        File ispnConf = new File( configDir, ISPN_XML );
        File ispnClusterConf = new File( configDir, ISPN_CLUSTER_XML );

        if ( ispnConf.exists() )
        {
            logger.info( " Loading ISPN config file {} from dir {} . ", ISPN_XML, configDir );
            try ( FileInputStream ispnStream = FileUtils.openInputStream( ispnConf ) )
            {
                cacheManager = new DefaultCacheManager( ispnStream );
            }
            catch ( IOException e )
            {
                logger.error( "Read ISPN configuration error, {}", e.getMessage(), e );
            }

            if ( ispnClusterConf.exists() )
            {
                logger.info( " Loading ISPN cluster config file {} from dir {} . ", ISPN_CLUSTER_XML, configDir );
                try ( FileInputStream ispnClusterStream = FileUtils.openInputStream( ispnClusterConf ) )
                {
                    clusterCacheManager = new DefaultCacheManager( ispnClusterStream );

                    String[] cacheNames = clusterCacheManager.getCacheNames().toArray( new String[]{} );
                    logger.info( "Starting cluster caches to make sure they existed: {}", Arrays.toString( cacheNames ) );
                    clusterCacheManager.startCaches( cacheNames );
                }
                catch ( IOException e )
                {
                    logger.error( "Read ISPN cluster configuration error, {}", e.getMessage(), e );
                }
            }
        }
        else
        {
            logger.info( " Loading ISPN config files from CLASSPATH. " );
            try
            {
                cacheManager = new DefaultCacheManager( ISPN_XML );
                clusterCacheManager = new DefaultCacheManager( ISPN_CLUSTER_XML );

                String[] cacheNames = clusterCacheManager.getCacheNames().toArray( new String[]{} );
                logger.info( "Starting cluster caches to make sure they existed: {}", Arrays.toString( cacheNames ) );
                clusterCacheManager.startCaches( cacheNames );
            }
            catch ( IOException e )
            {
                logger.error( "Read ISPN configuration error, {}", e.getMessage(), e );
            }
        }
    }

    public <K, V> Cache<K, V> getCache(String name)
    {
        Cache<K, V> cache;
        if ( clusterCacheManager.cacheExists( name ) )
        {
            logger.info( "Cluster cache retrieved. {}", name );
            cache = clusterCacheManager.getCache( name );
        }
        else
        {
            logger.info( "Cluster cache missing, try to get local cache: {}", name  );
            cache = cacheManager.getCache( name );
        }
        return cache;
    }

}
