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
