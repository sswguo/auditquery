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
package org.commonjava.auditquery.rest.resources;

import org.commonjava.auditquery.common.PaginatedResult;
import org.commonjava.auditquery.ctl.ContentTrackingController;
import org.commonjava.auditquery.rest.exception.AuditQueryWebException;
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
import javax.ws.rs.core.Response;
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
                    throws AuditQueryWebException
    {
        TrackingSummaryDTO trackingSummaryDTO;
        try
        {
            trackingSummaryDTO = trackingController.getTrackingSummaryByID( trackingID );
        }
        catch ( Exception e )
        {
            throw new AuditQueryWebException( e );
        }

        return trackingSummaryDTO;
    }

    @GET
    @Path( "/history/content/tracking/{tracking-id}/entries" )
    public Response listEntries( @PathParam( "tracking-id" ) String trackingID,
                                 @QueryParam( "type" ) String type,
                                 @QueryParam( "skip" ) int skip,
                                 @QueryParam( "count" ) int count )
                    throws AuditQueryWebException
    {
        Collection<TrackedContentEntryDTO> trackedContentEntryDTOS;
        try
        {
            trackedContentEntryDTOS = trackingController.queryContentEntries( trackingID.trim(), type, skip, count );
        }
        catch ( Exception e )
        {
            throw new AuditQueryWebException( e );
        }
        TrackingSummaryDTO trackingSummaryDTO;
        try
        {
            trackingSummaryDTO = trackingController.getTrackingSummaryByID( trackingID );
        }
        catch ( Exception e )
        {
            throw new AuditQueryWebException( e );
        }
        PaginatedResult<TrackedContentEntryDTO> result =
                        new PaginatedResult<TrackedContentEntryDTO>()
                                        .items( trackedContentEntryDTOS )
                                        .total( type.equals( "download" ) ? trackingSummaryDTO.getDownloadCount() : trackingSummaryDTO.getUploadCount() );
        return Response.status( Response.Status.OK ).entity( result ).build();
    }
}
