package org.commonjava.auditquery.ctl;

import org.commonjava.auditquery.cache.FileEventsCache;
import org.commonjava.auditquery.cache.TrackingSummaryCache;
import org.commonjava.auditquery.tracking.TrackingSummary;
import org.commonjava.auditquery.tracking.dto.TrackedContentDTO;
import org.commonjava.auditquery.tracking.dto.TrackedContentEntryDTO;
import org.commonjava.auditquery.tracking.dto.TrackingSummaryDTO;
import org.commonjava.cdi.util.weft.ExecutorConfig;
import org.commonjava.cdi.util.weft.WeftManaged;
import org.commonjava.propulsor.content.audit.model.FileEvent;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContentTrackingController
{

    @Inject
    @WeftManaged
    @ExecutorConfig( named = "OLAP-executor", threads = 3 )
    ExecutorService executor;

    @Inject
    @TrackingSummaryCache
    Cache<String, TrackingSummary> trackingSummaryCache;

    @Inject
    @FileEventsCache
    Cache<String, FileEvent> fileEventCache;

    Logger logger = LoggerFactory.getLogger( getClass() );

    public void getTrackedContent( String trackingID, String call )
    {

        logger.info( "Thread off the OLAP process, trackingID {}", trackingID );

        Callable<String> callableTask = () -> {
            try
            {

                TrackingSummary trackingSummary = getTrackingSummary( trackingID );

                if ( trackingSummary == null )
                {
                    throw new Exception( "No tracking summary existing. TrackingID: " + trackingID );
                }

                logger.info( "Retrieve the corresponding storage events which contains the checksums." );
                List<FileEvent> fileStorageEvents =
                                queryFileEventsByChecksums( trackingSummary.getDownloads(), "STORAGE" );

                logger.info( "Grouping the storage events according to the checksum." );
                Map<String, List<FileEvent>> storageEventsMap = fileStorageEvents.stream()
                                                                                 .collect( Collectors.groupingBy(
                                                                                                 event -> event.getChecksum() ) );

                logger.info( "Query all the file access events of the tracking id: {}", trackingID );
                List<FileEvent> fileAccessEventList = queryFileEvents( trackingID, "ACCESS" );

                Set<TrackedContentEntryDTO> uploads = new TreeSet<>();
                Set<TrackedContentEntryDTO> downloads = new TreeSet<>();
                fileAccessEventList.forEach( accessEvent -> {
                    String checksum =  accessEvent.getChecksum();
                    TrackedContentEntryDTO entryDTO = new TrackedContentEntryDTO();

                    List<FileEvent> storageEventList = storageEventsMap.get( checksum );
                    FileEvent first = storageEventList.stream()
                                                      .min( Comparator.comparing( i -> i.getTimestamp() ) )
                                                      .get();
                    FileEvent last = storageEventList.stream()
                                                     .max( Comparator.comparing( i -> i.getTimestamp() ) )
                                                     .get();

                    entryDTO.setOriginUrl( first.getTargetLocation() );
                    entryDTO.setLocalUrl( last.getTargetLocation() );
                    entryDTO.setPath( accessEvent.getTargetPath() );
                    entryDTO.setStoreKey( accessEvent.getExtra().get( "storeKey" ) );
                    entryDTO.setAccessChannel( accessEvent.getExtra().get( "accessChannel" ) );
                    entryDTO.setSha256( checksum );
                    downloads.add( entryDTO );
                } );

                fileStorageEvents.forEach( storageEvent -> {
                    TrackedContentEntryDTO entryDTO = new TrackedContentEntryDTO();
                    entryDTO.setLocalUrl( storageEvent.getTargetLocation() );
                    entryDTO.setStoreKey( storageEvent.getExtra().get( "storeKey" ) );
                    entryDTO.setAccessChannel( storageEvent.getExtra().get( "accessChannel" ) );
                    entryDTO.setPath( storageEvent.getTargetPath() );
                    entryDTO.setOriginUrl( "" );
                    entryDTO.setSha256( storageEvent.getChecksum() );
                    uploads.add( entryDTO );
                } );

                TrackedContentDTO trackedContentDTO = new TrackedContentDTO( trackingID, uploads, downloads );

                // TODO invoke callback
            }
            catch ( Exception e )
            {
                logger.error( "OLAP process error,", e );
            }
            return "OLAP process done.";
        };

        executor.submit( callableTask );

    }

    private TrackingSummary getTrackingSummary( String trackingID ) throws Exception
    {
        TrackingSummary trackingSummary = null;

        QueryFactory queryFactory = Search.getQueryFactory( trackingSummaryCache );
        org.infinispan.query.dsl.QueryBuilder qb =
                        queryFactory.from( TrackingSummary.class ).having( "trackingID" ).eq( trackingID ).toBuilder();

        List<TrackingSummary> trackingSummaryList = qb.build().list();

        if ( trackingSummaryList.isEmpty() )
        {
            logger.info( "Missing summary with trackingId {} in the cache, try to construct it.", trackingID );

            trackingSummary = constructTrackingSummary( trackingID );
            if ( trackingSummary != null )
            {
                logger.info( "Put tracking summary into the cache, trackingId {}.", trackingID );
                trackingSummaryCache.putIfAbsent( trackingID, trackingSummary );
            }
        }
        else
        {
            trackingSummary = trackingSummaryList.get( 0 );
        }

        return trackingSummary;
    }

    private TrackingSummary constructTrackingSummary( String trackingID )
    {
        List<FileEvent> fileEventList = queryFileEvents( trackingID );

        if ( fileEventList.isEmpty() )
        {
            return null;
        }

        Set<String> uploads = new TreeSet<>();
        Set<String> downloads = new TreeSet<>();

        fileEventList.forEach( fileEvent -> {
            switch ( fileEvent.getEventType() )
            {
                case "STORAGE":
                    uploads.add( fileEvent.getChecksum() );
                    break;
                case "ACCESS":
                    downloads.add( fileEvent.getChecksum() );
                    break;
                default:
                    break;
            }
        } );

        return new TrackingSummary( trackingID, uploads, downloads );
    }

    private List<FileEvent> queryFileEvents( String trackingID )
    {
        return queryFileEvents( trackingID, null );
    }

    private List<FileEvent> queryFileEvents( String trackingID, String eventType )
    {
        QueryFactory queryFactory = Search.getQueryFactory( fileEventCache );
        org.infinispan.query.dsl.QueryBuilder qb = queryFactory.from( FileEvent.class );
        if ( eventType != null )
        {
            qb.having( "sessionId" ).eq( trackingID ).and().having( "eventType" ).eq( eventType ).toBuilder();
        }
        else
        {
            qb.having( "sessionId" ).eq( trackingID ).toBuilder();
        }
        return qb.build().list();
    }

    private List<FileEvent> queryFileEventsByChecksums( Set<String> downloads, String eventType )
    {
        QueryFactory queryFactory = Search.getQueryFactory( fileEventCache );
        org.infinispan.query.dsl.QueryBuilder qb = queryFactory.from( FileEvent.class )
                                                               .having( "eventType" )
                                                               .eq( eventType )
                                                               .and()
                                                               .having( "checksum" )
                                                               .in( downloads )
                                                               .toBuilder();
        return qb.build().list();
    }

}
