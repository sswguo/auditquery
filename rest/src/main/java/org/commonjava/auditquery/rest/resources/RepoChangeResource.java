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

import org.commonjava.auditquery.ctl.RepoChangeEventController;
import org.commonjava.auditquery.history.ChangeEvent;
import org.commonjava.auditquery.history.ChangeSummary;
import org.commonjava.auditquery.history.dto.ChangeEventDTO;
import org.commonjava.auditquery.history.dto.ChangeSummaryDTO;
import org.commonjava.auditquery.history.dto.ChangeSummaryStats;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.commonjava.propulsor.deploy.undertow.util.StandardApplicationContent.application_json;

@Path( "/api/rest/history/stores" )
@ApplicationScoped
public class RepoChangeResource
        implements RestResources
{
    @Inject
    private RepoChangeEventController ctl;

    private final int DEFAULT_PAGE = 0;
    private final int DEFAULT_PAGE_SIZE = 25;

    @GET
    @Path( "/summary/stats" )
    @Produces( application_json )
    public Response getSummariesByPattern( final @QueryParam( "lastUpdate" ) String lastUpdate,
                                           final @QueryParam( "page" ) Integer page,
                                           final @QueryParam( "pageSize" ) Integer pageSize,
                                           final @QueryParam( "pattern" ) String pattern,
                                           @Context final UriInfo uriInfo )
    {
        List<ChangeSummary> summaries = null;
        int startIndex = startIndex( page, pageSize );
        int curPageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        //TODO lastUpdate support not implement yet
        List<ChangeEvent> events = ctl.getEventsByPattern( pattern, curPageSize, startIndex );
        return Response.status( 200 )
                       .entity( collectSummaryStats( events ) )
                       .build();
    }

    @GET
    @Path( "/summary/by-store/{packageType}/{type: (hosted|group|remote)}/{name}" )
    @Produces( application_json )
    public Response getSummariesByStoreKey( final @PathParam( "packageType" ) String packageType,
                                            final @PathParam( "type" ) String type,
                                            final @PathParam( "name" ) String name,
                                            final @QueryParam( "lastUpdate" ) String lastUpdate,
                                            final @QueryParam( "pageSize" ) Integer pageSize,
                                            final @QueryParam( "page" ) Integer page, @Context final UriInfo uriInfo )
    {
        String key = String.format( "%s:%s:%s", packageType, type, name );
        int startIndex = startIndex( page, pageSize );
        int curPageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        final Integer total = ctl.sizeOfEventsByStoreKey( key );
        //TODO lastUpdate support not implement yet
        return Response.status( 200 )
                       .entity( toSummaryDTO( total, page, pageSize,
                                              ctl.getEventsByStoreKey( key, curPageSize, startIndex ) ) )
                       .build();
    }

    @GET
    @Path( "/change/{eventId}" )
    @Produces( { application_json, "application/x-patch" } )
    public Response getEvent( final @PathParam( "eventId" ) String eventId, final @QueryParam( "format" ) String format,
                              @Context final UriInfo uriInfo )
    {
        if ( "patch".equals( format ) )
        {
            //TODO patch format support not implement yet
            return null;
        }

        return Response.status( 200 ).entity( ctl.getEventById( eventId ) ).build();
    }

    private ChangeEventDTO toEventDTO( Integer total, Integer curPage, Integer pageSize, List<ChangeEvent> items )
    {
        return new ChangeEventDTO().total( total ).curPage( curPage ).pageSize( pageSize ).items( items );
    }

    private ChangeSummaryDTO toSummaryDTO( Integer total, Integer curPage, Integer pageSize, List<ChangeEvent> items )
    {
        List<ChangeSummary> summaries = items.stream()
                                             .map( c -> new ChangeSummary().eventId( c.getEventId() )
                                                                           .changeTime( c.getChangeTime() )
                                                                           .user( c.getUser() )
                                                                           .summary( c.getSummary() ) )
                                             .collect( Collectors.toList() );
        return new ChangeSummaryDTO().total( total ).curPage( curPage ).pageSize( pageSize ).items( summaries );
    }

    private List<ChangeSummaryStats> collectSummaryStats( List<ChangeEvent> events )
    {
        final Map<String, ChangeSummaryStats> stats = new HashMap<>();

        events.forEach( e -> {
            ChangeSummaryStats stat = stats.computeIfAbsent( e.getStoreKey(), k -> {
                ChangeSummaryStats newStat = new ChangeSummaryStats();
                newStat.setStoreKey( k );
                newStat.setLastUpdate( e.getChangeTime() );
                return newStat;
            } );

            switch ( e.getChangeType() )
            {
                case CREATE:
                    stat.setCreates( stat.getCreates() + 1 );
                    break;
                case UPDATE:
                    stat.setUpdates( stat.getUpdates() + 1 );
                    break;
                case DELETE:
                    stat.setDeletes( stat.getDeletes() + 1 );
                    break;
                default:
                    break;
            }
        } );

        return new ArrayList<>( stats.values() );
    }

    private int startIndex( final Integer page, final Integer pageSize )
    {
        int curPage = page == null ? DEFAULT_PAGE : page;
        int curPageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        return curPage * curPageSize;
    }
}
