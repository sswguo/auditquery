package org.commonjava.auditquery.rest.resources;

import org.commonjava.auditquery.ctl.ContentTrackingController;
import org.commonjava.auditquery.tracking.dto.TrackedContentEntryDTO;
import org.commonjava.auditquery.tracking.dto.TrackingSummaryDTO;
import org.commonjava.propulsor.deploy.resteasy.RestResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Collection;

import static org.commonjava.propulsor.deploy.undertow.util.StandardApplicationContent.application_json;

@Path( "/api/rest" )
@Consumes( { application_json } )
@Produces( { application_json } )
@ApplicationScoped
public class ContentTrackingResource
                implements RestResources
{

    @Inject
    ContentTrackingController trackingController;

    @GET
    @Path( "/history/content/tracking/{tracking-id}/summary" )
    public TrackingSummaryDTO getTrackingSummary( @PathParam( "tracking-id" ) String trackingID )
    {
        TrackingSummaryDTO trackingSummaryDTO = null;
        try
        {
            trackingSummaryDTO = trackingController.getTrackingSummaryByID( trackingID );
        }
        catch ( Exception e )
        {
            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.error( "Get tracking summary error, {}", trackingID, e );
        }

        return trackingSummaryDTO;
    }

    @GET
    @Path( "/history/content/tracking/{tracking-id}/entries" )
    public Collection<TrackedContentEntryDTO> listEntries( @PathParam( "tracking-id" ) String trackingID,
                                                           @QueryParam( "type" ) String type,
                                                           @QueryParam( "skip" ) int skip,
                                                           @QueryParam( "count" ) int count )
    {
        Collection<TrackedContentEntryDTO> trackedContentEntryDTOS = null;
        try
        {
            trackedContentEntryDTOS = trackingController.queryContentEntries( trackingID.trim(), type, skip, count );
        }
        catch ( Exception e )
        {
            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.error( "Query content entries error, {}", trackingID, e );
        }

        return trackedContentEntryDTOS;
    }
}
