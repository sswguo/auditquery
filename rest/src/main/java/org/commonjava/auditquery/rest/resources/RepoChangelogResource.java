/**
 * Copyright (C) 2013~2019 Red Hat, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.auditquery.rest.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.commonjava.auditquery.ctl.RepoChangelogController;
import org.commonjava.propulsor.deploy.resteasy.RestResources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.commonjava.propulsor.deploy.undertow.util.StandardApplicationContent.application_json;

@Path( "/api/rest/repository/changelog" )
@ApplicationScoped
public class RepoChangelogResource
        implements RestResources
{
    @Inject
    private RepoChangelogController ctl;

    @GET
    @Path( "/{packageType}/{type: (hosted|group|remote)}/{name}" )
    @Produces( application_json )
    public Response getChangelogByStoreKey( final @PathParam( "packageType" ) String packageType,
                                            final @PathParam( "type" ) String type,
                                            final @PathParam( "name" ) String name,
                                            final @QueryParam( "pageMax" ) int pageMax,
                                            final @QueryParam( "pageStart" ) int pagetStart,
                                            @Context final UriInfo uriInfo )
    {
        String key = String.format( "%s:%s:%s", packageType, type, name );
        return Response.status( 200 ).entity( ctl.getLogsByStoreKey( key, pageMax, pagetStart ) ).build();
    }

    @GET
    @Path( "all" )
    @Produces( application_json )
    public Response getAllChangelogs( final @QueryParam( "pageMax" ) int pageMax,
                                      final @QueryParam( "pageStart" ) int pagetStart, @Context final UriInfo uriInfo )
    {
        return Response.status( 200 ).entity( ctl.getAllLogs( pageMax, pagetStart ) ).build();
    }

}
