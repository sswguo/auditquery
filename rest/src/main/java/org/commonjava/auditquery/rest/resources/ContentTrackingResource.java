package org.commonjava.auditquery.rest.resources;

import org.commonjava.auditquery.core.conf.AuditQueryConfig;
import org.commonjava.auditquery.tracking.TrackingSummary;
import org.commonjava.auditquery.tracking.dto.TrackedContentEntryDTO;
import org.commonjava.auditquery.tracking.dto.TrackingSummaryDTO;
import org.commonjava.propulsor.deploy.resteasy.RestResources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.commonjava.propulsor.deploy.undertow.util.StandardApplicationContent.application_json;

@Path( "/api/rest" )
@Consumes( { application_json } )
@Produces( { application_json } )
@ApplicationScoped
public class ContentTrackingResource
                implements RestResources
{

    @GET
    @Path( "/history/content/tracking/{tracking-id}/summary" )
    public TrackingSummaryDTO getTrackingSummary( @PathParam( "tracking-id" ) String trackingID )
    {
        //TODO
        TrackingSummaryDTO trackingSummaryDTO = new TrackingSummaryDTO( trackingID, 2, 10 );

        trackingSummaryDTO.setStartTime( new Date(  ) );
        trackingSummaryDTO.setEndTime( new Date(  ) );

        return trackingSummaryDTO;
    }

    @GET
    @Path( "/history/content/tracking/{tracking-id}/entries" )
    public Collection<TrackedContentEntryDTO> listEntries( @PathParam( "tracking-id" ) String trackingID,
                                                           @QueryParam( "type" ) String type,
                                                           @QueryParam( "skip" ) int skip,
                                                           @QueryParam( "count" ) int count )
    {
        //TODO
        Collection<TrackedContentEntryDTO> trackedContentEntryDTOS = new ArrayList<TrackedContentEntryDTO>();

        if ( "upload".equals( type ) )
        {
            for (int i =0; i<20; i++)
            {
                TrackedContentEntryDTO up = new TrackedContentEntryDTO( "hosted:foo" + i, "MAVEN", "/path/to/foo.pom" );
                trackedContentEntryDTOS.add( up );
            }
        }
        else if ( "download".equals( type ) )
        {
            for (int i =0; i<20; i++)
            {
                TrackedContentEntryDTO down = new TrackedContentEntryDTO( "remote:foo" + i, "MAVEN", "/path/to/foo1.pom" );
                trackedContentEntryDTOS.add( down );
            }
        }

        return trackedContentEntryDTOS;
    }
}
