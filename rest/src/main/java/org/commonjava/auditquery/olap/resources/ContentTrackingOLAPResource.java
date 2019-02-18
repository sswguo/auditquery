package org.commonjava.auditquery.olap.resources;

import org.commonjava.auditquery.ctl.ContentTrackingController;
import org.commonjava.auditquery.olap.handler.CallbackRequest;
import org.commonjava.propulsor.deploy.resteasy.RestResources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path( "/api/olap" )
@ApplicationScoped
public class ContentTrackingOLAPResource
                implements RestResources
{

    @Inject
    ContentTrackingController trackingController;

    @POST
    @Path( "/history/content/tracking" )
    public Response getTrackedContent( CallbackRequest request )
    {

        trackingController.getTrackedContent( request );

        return Response.ok().build();
    }
}
