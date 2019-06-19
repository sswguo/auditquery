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
package org.commonjava.auditquery.ctl;

import org.apache.commons.lang3.StringUtils;
import org.commonjava.auditquery.cache.RepoChangeCache;
import org.commonjava.auditquery.history.ChangeEvent;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class RepoChangeEventController
{
    private final Logger logger = LoggerFactory.getLogger( this.getClass() );

    @Inject
    @RepoChangeCache
    private Cache<String, ChangeEvent> changeCache;

    private QueryFactory queryFactory;

    @PostConstruct
    public void init()
    {
        queryFactory = Search.getQueryFactory( changeCache );
    }

    public List<ChangeEvent> getEventsByStoreKey( String storeKey, int max, int offset )
    {
        return queryFactory.from( ChangeEvent.class )
                           .having( "storeKey" )
                           .eq( storeKey )
                           .maxResults( max )
                           .startOffset( offset )
                           .build()
                           .list();

    }

    public Integer sizeOfEventsByStoreKey( String storeKey )
    {
        return queryFactory.from( ChangeEvent.class ).having( "storeKey" ).eq( storeKey ).build().getResultSize();
    }

    public List<ChangeEvent> getEventsByPattern( String pattern, int max, int offset )
    {
        List<ChangeEvent> events;
        if ( StringUtils.isBlank( pattern ) )
        {
            events = queryFactory.from( ChangeEvent.class ).startOffset( offset ).maxResults( max ).build().list();
        }
        else
        {
            events = queryFactory.from( ChangeEvent.class )
                                 .having( "storeKey" )
                                 .like( pattern )
                                 .startOffset( offset )
                                 .maxResults( max )
                                 .build()
                                 .list();
        }

        logger.debug( "Get events size: {}", events.size() );
        return events;
    }

    public Integer sizeOfEventsByPattern( String pattern )
    {
        if ( StringUtils.isBlank( pattern ) )
        {
            return queryFactory.from( ChangeEvent.class ).build().getResultSize();
        }
        return queryFactory.from( ChangeEvent.class ).having( "storeKey" ).like( pattern ).build().getResultSize();
    }

    public List<ChangeEvent> getAllEvents( int max, int offset )
    {
        return queryFactory.from( ChangeEvent.class ).maxResults( max ).startOffset( offset ).build().list();
    }

    public Integer sizeOfAllEvents()
    {
        return queryFactory.from( ChangeEvent.class ).build().getResultSize();
    }

    public ChangeEvent getEventById( final String eventId )
    {
        return (ChangeEvent) queryFactory.from( ChangeEvent.class )
                                         .having( "eventId" )
                                         .eq( eventId )
                                         .build()
                                         .list()
                                         .get( 0 );
    }

}
