package org.commonjava.auditquery.rest.conf;

import org.commonjava.propulsor.deploy.resteasy.ResteasyAppConfig;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import static java.util.Arrays.asList;

@ApplicationScoped
public class AuditQueryResteasyAppConfig implements ResteasyAppConfig
{

    @Override
    public List<String> getJaxRsMappings()
    {
        return asList( "/api*", "/api/*" );
    }
}
