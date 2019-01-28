package org.commonjava.auditquery.olap.resources;

import org.commonjava.auditquery.ctl.ContentTrackingController;
import org.commonjava.propulsor.deploy.resteasy.RestResources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path( "/api/olap" )
@ApplicationScoped
public class ContentTrackingOLAPResource
                implements RestResources
{

    @Inject
    ContentTrackingController trackingController;

    @GET
    @Path( "/history/content/tracking/{tracking-id}" )
    public Response getTrackedContent( @PathParam( "tracking-id" ) String trackingID,
                                       @QueryParam( "call" ) String call )
    {

        trackingController.getTrackedContent( trackingID, call );

        return Response.ok().build();
    }
}
