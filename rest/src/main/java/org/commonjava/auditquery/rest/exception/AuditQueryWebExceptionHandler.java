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
package org.commonjava.auditquery.rest.exception;

import org.commonjava.propulsor.deploy.resteasy.RestResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AuditQueryWebExceptionHandler
                implements RestResources, ExceptionMapper<AuditQueryWebException>
{

    public Response toResponse( AuditQueryWebException e )
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.error( "WebException:", e );

        String error = "{\"error\":" + "\"" + e.getMessage() + "\"}";
        Response response = Response.status( Response.Status.INTERNAL_SERVER_ERROR )
                                    .entity( error )
                                    .type( MediaType.APPLICATION_JSON_TYPE )
                                    .build();
        return response;
    }
}
