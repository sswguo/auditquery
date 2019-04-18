package org.commonjava.auditquery.cache;

import org.commonjava.auditquery.fileevent.FileEvent;
import org.commonjava.auditquery.tracking.TrackingSummary;
import org.commonjava.auditquery.tracking.dto.TrackedContentDTO;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class AuditQueryCacheProducer
{

    private static final String TRACKING_SUMMARY_CACHE = "tracking_summary_cache";

    private static final String FILE_EVENT_CACHE = "event-audit";

    private static final String TRACKED_CONTENT_DTO_CACHE = "tracked_content_dto_cache";

    @Inject
    CacheProducer cacheProvider;

    @Produces
    @TrackingSummaryCache
    public Cache<String, TrackingSummary> createTrackingSummaryCache()
    {
        return cacheProvider.getCache( TRACKING_SUMMARY_CACHE );
    }

    @Produces
    @FileEventsCache
    public Cache<String, FileEvent> createFileEventCache()
    {
        return cacheProvider.getCache( FILE_EVENT_CACHE );
    }

    @Produces
    @TrackedContentDTOCache
    public Cache<String, TrackedContentDTO> createTrackedContentDTOCache()
    {
        return cacheProvider.getCache( TRACKED_CONTENT_DTO_CACHE );
    }
}
