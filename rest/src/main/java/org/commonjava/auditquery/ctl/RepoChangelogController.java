/**
 * Copyright (C) 2013~2019 Red Hat, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.auditquery.ctl;

import org.commonjava.auditquery.cache.RepoChangelogCache;
import org.commonjava.auditquery.changelog.RepositoryChangeLog;
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
public class RepoChangelogController
{
    private final Logger logger = LoggerFactory.getLogger( this.getClass() );

    @Inject
    @RepoChangelogCache
    private Cache<String, RepositoryChangeLog> changeLogCache;

    private QueryFactory queryFactory;

    @PostConstruct
    public void init()
    {
        queryFactory = Search.getQueryFactory( changeLogCache );
    }

    public List<RepositoryChangeLog> getLogsByStoreKey( String storeKey, int max, int offset )
    {
        return queryFactory.from( RepositoryChangeLog.class )
                           .having( "storeKey" )
                           .eq( storeKey )
                           .maxResults( max )
                           .startOffset( offset )
                           .build()
                           .list();

    }

    public Integer sizeOfLogsByStoreKey( String storeKey )
    {
        return queryFactory.from( RepositoryChangeLog.class )
                           .having( "storeKey" )
                           .eq( storeKey )
                           .build()
                           .getResultSize();
    }

    public List<RepositoryChangeLog> getAllLogs( int max, int offset )
    {
        return queryFactory.from( RepositoryChangeLog.class ).maxResults( max ).startOffset( offset ).build().list();
    }

    public Integer sizeOfAllLogs(){
        return queryFactory.from( RepositoryChangeLog.class ).build().getResultSize();
    }

}
