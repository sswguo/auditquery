package org.commonjava.auditquery.cache;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

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
        cacheManager = new DefaultCacheManager();
    }

    public <K, V> Cache<K, V> getCache(String name)
    {
        return cacheManager.getCache( name );
    }

}
