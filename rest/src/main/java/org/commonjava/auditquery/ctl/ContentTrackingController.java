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


import org.commonjava.auditquery.cache.FileEventsCache;
import org.commonjava.auditquery.cache.TrackingSummaryCache;
import org.commonjava.auditquery.conf.RestConfig;
import org.commonjava.auditquery.fileevent.FileEvent;
import org.commonjava.auditquery.olap.handler.CallbackRequest;
import org.commonjava.auditquery.olap.handler.CallbackResult;
import org.commonjava.auditquery.tracking.TrackingSummary;
import org.commonjava.auditquery.tracking.dto.TrackedContentDTO;
import org.commonjava.auditquery.tracking.dto.TrackedContentEntryDTO;
import org.commonjava.auditquery.tracking.dto.TrackingSummaryDTO;
import org.commonjava.cdi.util.weft.ExecutorConfig;
import org.commonjava.cdi.util.weft.WeftExecutorService;
import org.commonjava.cdi.util.weft.WeftManaged;
import org.commonjava.util.jhttpc.util.UrlUtils;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContentTrackingController
{

    @Inject
    @WeftManaged
    @ExecutorConfig( named = "OLAP-Service", threads = 8, maxLoadFactor = 40)
    WeftExecutorService olapService;

    @Inject
    @TrackingSummaryCache
    Cache<String, TrackingSummary> trackingSummaryCache;

    @Inject
    @FileEventsCache
    Cache<String, FileEvent> fileEventCache;

    @Inject
    Consumer<CallbackResult> callbackReqConsumer;

    @Inject
    RestConfig config;

    Logger logger = LoggerFactory.getLogger( getClass() );

    public TrackingSummaryDTO getTrackingSummaryByID( String trackingID ) throws Exception
    {

        TrackingSummaryDTO trackingSummaryDTO;

        TrackingSummary trackingSummary = getTrackingSummary( trackingID );

        if ( trackingSummary == null )
        {
            throw new Exception( "No tracking summary exists, trackingId: " + trackingID );
        }

        trackingSummaryDTO =
                        new TrackingSummaryDTO( trackingSummary.getTrackingID(), trackingSummary.getUploads().size(),
                                                trackingSummary.getDownloads().size() );

        return trackingSummaryDTO;

    }

    public Collection<TrackedContentEntryDTO> queryContentEntries( String trackingID, String type, int skip, int count )
                    throws Exception
    {

        Collection<TrackedContentEntryDTO> trackedContentEntryDTOS;

        Collection<FileEvent> fileEvents = queryPaginationFileEvents( trackingID, type, skip, count );

        if ( "download".equals( type ) )
        {
            TrackingSummary trackingSummary = getTrackingSummary( trackingID );

            Map<String, List<FileEvent>> storageEventsMap = queryStorageEventsFunction.apply( trackingSummary );

            trackedContentEntryDTOS = fileEvents.stream()
                                                .map( fileEvent -> eventToBasicEntryFunction.apply( fileEvent ) )
                                                .map( entryDTO -> {
                                                    List<FileEvent> storageEventList =
                                                                    storageEventsMap.get( entryDTO.getSha256() );
                                                    if ( storageEventList != null && !storageEventList.isEmpty() )
                                                    {
                                                        FileEvent first = minEventFunction.apply( storageEventList );
                                                        FileEvent last = maxEventFunction.apply( storageEventList );
                                                        try
                                                        {
                                                            if ( config.getIndyUrl() != null )
                                                            {
                                                                entryDTO.setLocalUrl( UrlUtils.buildUrl( config.getIndyUrl(), first.getTargetPath() ) );
                                                            }
                                                            if ( last.getSourceLocation() != null )
                                                            {
                                                                entryDTO.setOriginUrl( UrlUtils.buildUrl( last.getSourceLocation(), last.getSourcePath() ) );
                                                            }
                                                        }
                                                        catch ( MalformedURLException e )
                                                        {
                                                            logger.error( "Cannot format URL. Reason: {}", e.getMessage(), e );
                                                        }

                                                    }
                                                    return entryDTO;
                                                } )
                                                .collect( Collectors.toSet() );
        }
        else
        {
            trackedContentEntryDTOS = fileEvents.stream()
                                                .map( fileEvent -> eventToBasicEntryFunction.apply( fileEvent ) )
                                                .collect( Collectors.toSet() );
        }

        return trackedContentEntryDTOS;

    }

    public void getTrackedContent( CallbackRequest request )
    {

        String trackingID = request.getTrackingID();
        logger.info( "Thread off the OLAP process, trackingID {}", trackingID );

        Callable<String> callableTask = () -> {
            try
            {

                TrackingSummary trackingSummary = getTrackingSummary( trackingID );

                if ( trackingSummary == null )
                {
                    throw new Exception( "No tracking summary existing. TrackingID: " + trackingID );
                }

                Map<String, List<FileEvent>> storageEventsMap = queryStorageEventsFunction.apply( trackingSummary );

                logger.info( "Query all the file access events of the tracking id: {}", trackingID );
                List<FileEvent> fileAccessEventList = queryFileEvents( trackingID, "ACCESS" );

                Set<TrackedContentEntryDTO> downloads = fileAccessEventList.stream()
                                                                           .map( accessEvent -> eventToBasicEntryFunction
                                                                                           .apply(
                                                                                           accessEvent ) )
                                                                           .map( entryDTO -> {
                                                                               String checksum = entryDTO.getSha256();

                                                                               List<FileEvent> storageEventList =
                                                                                               storageEventsMap.get(
                                                                                                               checksum );

                                                                               if ( storageEventList!= null && !storageEventList.isEmpty() )
                                                                               {
                                                                                   FileEvent first = minEventFunction.apply(
                                                                                                   storageEventList );
                                                                                   FileEvent last = maxEventFunction.apply(
                                                                                                   storageEventList );

                                                                                   try
                                                                                   {
                                                                                       if ( config.getIndyUrl() != null )
                                                                                       {
                                                                                           entryDTO.setLocalUrl( UrlUtils.buildUrl( config.getIndyUrl(), first.getTargetPath() ) );
                                                                                       }
                                                                                       if ( last.getSourceLocation() != null )
                                                                                       {
                                                                                           entryDTO.setOriginUrl( UrlUtils.buildUrl( last.getSourceLocation(), last.getSourcePath() ) );
                                                                                       }
                                                                                   }
                                                                                   catch ( MalformedURLException e )
                                                                                   {
                                                                                       logger.error( "Cannot format URL. Reason: {}", e.getMessage(), e );
                                                                                   }
                                                                               }
                                                                               return entryDTO;
                                                                           } )
                                                                           .collect( Collectors.toSet() );

                List<FileEvent> fileStorageEvents = new ArrayList<>();
                storageEventsMap.values().stream().forEach( subList -> fileStorageEvents.addAll( subList ) );
                Set<TrackedContentEntryDTO> uploads = fileStorageEvents.stream()
                                                                       .map( storageEvent -> eventToBasicEntryFunction.apply(
                                                                                       storageEvent ) )
                                                                       .collect( Collectors.toSet() );

                TrackedContentDTO trackedContentDTO = new TrackedContentDTO( trackingID, uploads, downloads );

                callbackReqConsumer.accept( new CallbackResult( request, trackedContentDTO ) );
            }
            catch ( Exception e )
            {
                logger.error( "OLAP process error,", e );
            }
            return "OLAP process done.";
        };

        olapService.submit( callableTask );

    }

    private TrackingSummary getTrackingSummary( String trackingID ) throws Exception
    {
        TrackingSummary trackingSummary = trackingSummaryCache.get( trackingID );

        if ( trackingSummary == null )
        {
            logger.info( "Missing summary with trackingId {} in the cache, try to construct it.", trackingID );
            trackingSummary = constructTrackingSummary( trackingID );
            if ( trackingSummary != null )
            {
                logger.info( "Put tracking summary into the cache, trackingId {}.", trackingID );
                trackingSummaryCache.putIfAbsent( trackingID, trackingSummary );
            }
        }

        return trackingSummary;
    }

    private TrackingSummary constructTrackingSummary( String trackingID )
    {
        List<FileEvent> fileEventList = queryFileEvents( trackingID );

        if ( fileEventList.isEmpty() )
        {
            logger.info( "No corresponding file events with the trackingId: {}", trackingID );
            return null;
        }

        Set<String> uploads = new TreeSet<>();
        Set<String> downloads = new TreeSet<>();

        fileEventList.forEach( fileEvent -> {
            switch ( fileEvent.getEventType() )
            {
                case STORAGE:
                    uploads.add( fileEvent.getChecksum() );
                    break;
                case ACCESS:
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

    private Collection<FileEvent> queryPaginationFileEvents( String trackingID, String type, int skip, int count )
                    throws Exception
    {
        String eventType;
        switch ( type )
        {
            case "upload":
                eventType = "STORAGE";
                break;
            case "download":
                eventType = "ACCESS";
                break;
            default:
                throw new IllegalArgumentException( "Invalid event type:" + type );
        }

        QueryFactory queryFactory = Search.getQueryFactory( fileEventCache );
        org.infinispan.query.dsl.QueryBuilder qb = queryFactory.from( FileEvent.class )
                                                               .orderBy( "timestamp", SortOrder.DESC )
                                                               .startOffset( skip * count )
                                                               .maxResults( count )
                                                               .having( "sessionId" )
                                                               .eq( trackingID )
                                                               .and()
                                                               .having( "eventType" )
                                                               .eq( eventType )
                                                               .toBuilder();
        List<FileEvent> fileEventList = qb.build().list();
        return fileEventList;
    }

    Function<TrackingSummary, Map<String, List<FileEvent>>> queryStorageEventsFunction = trackingSummary -> {

        logger.info( "Retrieve the corresponding storage events which contains the checksums." );
        QueryFactory queryFactory = Search.getQueryFactory( fileEventCache );
        org.infinispan.query.dsl.QueryBuilder qb = queryFactory.from( FileEvent.class )
                                                               .having( "eventType" )
                                                               .eq( "STORAGE" )
                                                               .and()
                                                               .having( "checksum" )
                                                               .in( trackingSummary.getDownloads() )
                                                               .toBuilder();

        List<FileEvent> fileStorageEvents = qb.build().list();
        logger.info( "Grouping the storage events according to the checksum." );
        Map<String, List<FileEvent>> storageEventsMap =
                        fileStorageEvents.stream().collect( Collectors.groupingBy( event -> event.getChecksum() ) );
        return storageEventsMap;
    };

    Function<FileEvent, TrackedContentEntryDTO> eventToBasicEntryFunction = fileEvent -> {
        TrackedContentEntryDTO entryDTO = new TrackedContentEntryDTO();
        String path = fileEvent.getTargetPath();
        entryDTO.setPath( path );
        entryDTO.setSha256( fileEvent.getChecksum() );
        entryDTO.setStoreKey( fileEvent.getStoreKey() );
        entryDTO.setMd5( fileEvent.getMd5() );
        entryDTO.setSha1( fileEvent.getSha1() );
        entryDTO.setSize( fileEvent.getSize() );
        try
        {
            if ( config.getIndyUrl() != null )
            {
                entryDTO.setLocalUrl( UrlUtils.buildUrl( config.getIndyUrl(), path ) );
            }
            if ( fileEvent.getSourceLocation() != null )
            {
                entryDTO.setOriginUrl( UrlUtils.buildUrl( fileEvent.getSourceLocation(), path ) );
            }
        }
        catch ( MalformedURLException e )
        {
            logger.error( "Cannot format URL. Reason: {}", e.getMessage(), e );
        }
        entryDTO.setAccessChannel( fileEvent.getExtra().get( "packageType" ) );
        return entryDTO;
    };

    Function<List<FileEvent>, FileEvent> minEventFunction =
                    list -> list.stream().min( Comparator.comparing( FileEvent::getTimestamp ) ).get();

    Function<List<FileEvent>, FileEvent> maxEventFunction =
                    list -> list.stream().max( Comparator.comparing( FileEvent::getTimestamp ) ).get();

}
