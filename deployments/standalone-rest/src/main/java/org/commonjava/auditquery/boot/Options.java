package org.commonjava.auditquery.boot;

import org.commonjava.propulsor.boot.BootOptions;
import org.commonjava.propulsor.deploy.undertow.UndertowBootOptions;

public class Options extends BootOptions implements UndertowBootOptions
{
    @Override
    public String getApplicationName()
    {
        return "AuditQuery";
    }

    @Override
    public String getHomeSystemProperty()
    {
        return "aq.home";
    }

    @Override
    public String getConfigSystemProperty()
    {
        return "aq.conf";
    }

    @Override
    public String getHomeEnvar()
    {
        return "AQ_HOME";
    }

    @Override
    public String getContextPath()
    {
        return "/";
    }

    @Override
    public String getDeploymentName()
    {
        return "Audit Query Service REST";
    }

    @Override
    public int getPort()
    {
        return 8082;
    }

    @Override
    public String getBind()
    {
        return "0.0.0.0";
    }

    @Override
    public void setPort( int i )
    {

    }

    @Override
    protected String getDefaultConfigFile()
    {
        return "etc/auditquery/main.conf";
    }
}
