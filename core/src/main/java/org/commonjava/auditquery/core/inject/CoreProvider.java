package org.commonjava.auditquery.core.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.commonjava.auditquery.tracking.io.AuditQueryObjectMapper;

import javax.enterprise.inject.Produces;

public class CoreProvider
{

    @Produces
    public ObjectMapper getAuditQueryObjectMapper()
    {
        return new AuditQueryObjectMapper();
    }

}
