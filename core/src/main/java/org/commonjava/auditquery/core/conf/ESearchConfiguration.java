package org.commonjava.auditquery.core.conf;

import org.commonjava.propulsor.config.annotation.ConfigName;
import org.commonjava.propulsor.config.annotation.SectionName;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@SectionName("elasticsearch")
public class ESearchConfiguration
{

    public ESearchConfiguration()
    {

    }

    private String clusterName;

    private String host;

    private Integer port;

    public String getClusterName()
    {
        return clusterName;
    }

    @ConfigName( "cluster.name" )
    public void setClusterName( String clusterName )
    {
        this.clusterName = clusterName;
    }

    public String getHost()
    {
        return host;
    }

    @ConfigName( "host" )
    public void setHost( String host )
    {
        this.host = host;
    }

    public Integer getPort()
    {
        return port;
    }

    @ConfigName( "port" )
    public void setPort( Integer port )
    {
        this.port = port;
    }
}
