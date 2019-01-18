package org.commonjava.auditquery.elasticsearch;

import org.commonjava.auditquery.core.conf.ESearchConfiguration;
import org.commonjava.auditquery.tracking.TrackingSummary;
import org.commonjava.auditquery.tracking.dto.TrackedContentEntryDTO;
import org.commonjava.auditquery.tracking.dto.TrackingSummaryDTO;
import org.commonjava.auditquery.tracking.io.AuditQueryObjectMapper;
import org.commonjava.propulsor.content.audit.model.FileEvent;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sound.midi.Track;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@ApplicationScoped
public class ESearchQueryController
{

    @Inject
    ESearchClientFactory factory;

    @Inject
    AuditQueryObjectMapper objectMapper;

    @Inject
    ESearchConfiguration eSearchConfig;

    Logger logger = LoggerFactory.getLogger( getClass() );

    private Consumer<SearchHit> hitDebugConsumer = ( hit ) -> {
        logger.debug( hit.getSourceAsString() );
    };

    public TrackingSummary getTrackingSummaryByID( String trackingId ) throws Exception
    {
        try (RestHighLevelClient client = factory.getRestClient())
        {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(
                            QueryBuilders.constantScoreQuery( QueryBuilders.termQuery( "trackingID", trackingId ) ) );
            sourceBuilder.timeout( new TimeValue( 60, TimeUnit.SECONDS ) );

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices( eSearchConfig.getTrackingSummaryIndex() );
            searchRequest.source( sourceBuilder );

            SearchResponse searchResponse = client.search( searchRequest );
            SearchHits hits = searchResponse.getHits();
            hits.forEach( hitDebugConsumer );

            TrackingSummary trackingSummary = null;

            if ( hits.totalHits > 0 )
            {
                SearchHit hit = hits.getAt( 0 );
                trackingSummary =
                                objectMapper.readValue( hit.getSourceAsString(), TrackingSummary.class );

            }
            else
            {
                trackingSummary = constructTrackingSummary( trackingId );
            }

            return trackingSummary;
        }
    }

    public Collection<TrackedContentEntryDTO> listEntries( String trackingID, String type, int skip, int count )
                    throws Exception
    {

        Collection<TrackedContentEntryDTO> trackedContentEntryDTOS = new ArrayList<>();

        Collection<FileEvent> fileEvents = queryFileEvents( trackingID, type, skip, count );

        fileEvents.forEach( fileEvent -> {
            TrackedContentEntryDTO entryDTO = new TrackedContentEntryDTO();
            entryDTO.setPath( fileEvent.getTargetPath() );
            entryDTO.setSha256( fileEvent.getChecksum() );
            ( (ArrayList<TrackedContentEntryDTO>) trackedContentEntryDTOS ).add( entryDTO );
        } );

        return trackedContentEntryDTOS;

    }

    public TrackingSummary constructTrackingSummary ( String trackingId ) throws Exception
    {

        logger.info( "Constructing tracking summary, trackingID: {}", trackingId );

        long eventCount = queryDocCountByTrackingID( trackingId );

        logger.info( "Event count for trackingId {}, {}", trackingId, eventCount );

        Collection<FileEvent> fileEvents = queryFileEvents( trackingId, "ALL", 0, (int)eventCount );
        Set<String> uploads = new TreeSet<>();
        Set<String> downloads = new TreeSet<>();
        fileEvents.forEach( fileEvent -> {
            if ( "ACCESS".equals( fileEvent.getEventType() ) )
            {
                downloads.add( fileEvent.getChecksum() );
            }
            else if ( "STORAGE".equals( fileEvent.getEventType() ) )
            {
                uploads.add( fileEvent.getChecksum() );
            }
        } );

        TrackingSummary trackingSummary = new TrackingSummary( trackingId, uploads, downloads );

        return trackingSummary;
    }

    public long queryDocCountByTrackingID ( String trackingID ) throws Exception
    {
        try (RestHighLevelClient client = factory.getRestClient()) {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.termQuery("extra.trackingId", trackingID));
            sourceBuilder.size(0);
            TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_trackingID").field("extra.trackingId");
            sourceBuilder.aggregation(aggregation);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(eSearchConfig.getFileEventIndex());
            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);

            // Retrieving Aggregations
            Aggregations aggregations = searchResponse.getAggregations();
            Terms byTrackingIDAggregation = aggregations.get( "by_trackingID");

            Terms.Bucket bucket = null;
            if ( byTrackingIDAggregation != null && byTrackingIDAggregation.getBuckets().size() > 0 )
            {
                bucket = byTrackingIDAggregation.getBuckets().get( 0 );
            }
            if ( bucket != null )
            {
                return bucket.getDocCount();
            }
            return 0;
        }
    }

    public Collection<FileEvent> queryFileEvents ( String trackingID, String type, int skip, int count ) throws Exception
    {
        Collection<FileEvent> fileEvents = new ArrayList<>(  );
        try (RestHighLevelClient client = factory.getRestClient())
        {
            String eventType = "";
            switch ( type )
            {
                case "upload":
                    eventType = "STORAGE";
                    break;
                case "download":
                    eventType = "ACCESS";
                    break;
                default:
                    eventType = "ALL";
                    break;
            }
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery()
                                                          .must( QueryBuilders.matchQuery( "extra.trackingId",
                                                                                           trackingID ) );

            if ( !"ALL".equals( eventType ) )
            {
                queryBuilders = queryBuilders.must( QueryBuilders.matchQuery( "eventType", eventType ) );
            }


            // sorting
            sourceBuilder.sort( new FieldSortBuilder( "timestamp" ).order( SortOrder.DESC ) );

            sourceBuilder.query( queryBuilders );
            sourceBuilder.from( skip * count );
            sourceBuilder.size( count );
            sourceBuilder.timeout( new TimeValue( 60, TimeUnit.SECONDS ) );

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices( eSearchConfig.getFileEventIndex() );
            searchRequest.source( sourceBuilder );

            SearchResponse searchResponse = client.search( searchRequest );
            SearchHits hits = searchResponse.getHits();
            hits.forEach( hit -> {
                hitDebugConsumer.accept( hit );
                try
                {
                    FileEvent fileEvent = objectMapper.readValue( hit.getSourceAsString(), FileEvent.class );
                    ( (ArrayList<FileEvent>) fileEvents ).add( fileEvent );
                }
                catch ( IOException e )
                {
                    logger.error( "", e );
                }
            } );

            return fileEvents;
        }
    }

}
