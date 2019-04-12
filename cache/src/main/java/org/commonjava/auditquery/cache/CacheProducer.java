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

    public CacheProducer()
    {

    }

    @PostConstruct
    public void start()
    {
        try
        {
            cacheManager = new DefaultCacheManager("infinispan.xml");
        }
        catch ( IOException e )
        {
            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.error( "Read ISPN configuration error, {}", e.getMessage(), e );
        }
    }

    public <K, V> Cache<K, V> getCache(String name)
    {
        return cacheManager.getCache( name );
    }

}
