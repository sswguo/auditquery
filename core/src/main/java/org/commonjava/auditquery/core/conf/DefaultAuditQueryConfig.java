package org.commonjava.auditquery.core.conf;

import org.commonjava.propulsor.config.annotation.ConfigName;
import org.commonjava.propulsor.config.annotation.SectionName;

import javax.enterprise.context.ApplicationScoped;

@SectionName
@ApplicationScoped
public class DefaultAuditQueryConfig implements AuditQueryConfigInfo
{

    private String configDir;

    public String getConfigDir()
    {
        return configDir;
    }

    @ConfigName( "config.dir" )
    public void setConfigDir( String configDir )
    {
        this.configDir = configDir;
    }
}
