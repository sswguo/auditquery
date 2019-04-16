package org.commonjava.auditquery.cache;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;


/**
 * This is a temporary implementation and it will be replaced by the one in propulsor
 */
@ApplicationScoped
public class CacheProducer
{

    EmbeddedCacheManager cacheManager;
    EmbeddedCacheManager clusterCacheManager;

    public CacheProducer()
    {

    }

    @PostConstruct
    public void start()
    {
        try
        {
            cacheManager = new DefaultCacheManager("infinispan.xml");
            clusterCacheManager = new DefaultCacheManager( "infinispan-cluster.xml" );

            clusterCacheManager.startCaches( String.join( ",", clusterCacheManager.getCacheNames() ) );
        }
        catch ( IOException e )
        {
            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.error( "Read ISPN configuration error, {}", e.getMessage(), e );
        }
    }

    public <K, V> Cache<K, V> getCache(String name)
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
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
