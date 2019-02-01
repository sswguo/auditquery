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
