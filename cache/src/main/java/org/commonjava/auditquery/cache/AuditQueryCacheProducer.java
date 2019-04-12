package org.commonjava.auditquery.cache;

import org.commonjava.auditquery.tracking.TrackingSummary;
import org.commonjava.auditquery.tracking.dto.TrackedContentDTO;
import org.commonjava.propulsor.content.audit.model.FileEvent;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.cfg.SearchMapping;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.lang.annotation.ElementType;

@ApplicationScoped
public class AuditQueryCacheProducer
{

    private static final String TRACKING_SUMMARY_CACHE = "tracking_summary_cache";

    private static final String FILE_EVENT_CACHE = "file_event_cache";

    private static final String TRACKED_CONTENT_DTO_CACHE = "tracked_content_dto_cache";

    @Inject
    CacheProducer cacheProvider;

    @Factory
    public SearchMapping getSearchMapping()
    {
        final SearchMapping entryMapping = new SearchMapping();
        entryMapping.entity( FileEvent.class )
                    .indexed()
                    .property( "sessionId", ElementType.METHOD )
                    .field()
                    .analyze(Analyze.NO)
                    .property( "eventType", ElementType.METHOD )
                    .field()
                    .property( "checksum", ElementType.METHOD )
                    .field()
                    .property( "timestamp", ElementType.METHOD )
                    .dateBridge( Resolution.MILLISECOND )
                    .field()
                    .property( "extra", ElementType.METHOD )
                    .field();

        return entryMapping;
    }

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
