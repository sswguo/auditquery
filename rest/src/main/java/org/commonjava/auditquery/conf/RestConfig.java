package org.commonjava.auditquery.conf;

import org.commonjava.auditquery.core.conf.AuditQueryConfigInfo;
import org.commonjava.propulsor.config.annotation.ConfigName;
import org.commonjava.propulsor.config.annotation.SectionName;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@SectionName( "rest" )
public class RestConfig implements AuditQueryConfigInfo
{

    private String indyUrl;

    public String getIndyUrl()
    {
        return indyUrl;
    }

    @ConfigName( "indy.url")
    public void setIndyUrl( String indyUrl )
    {
        this.indyUrl = indyUrl;
    }

}
