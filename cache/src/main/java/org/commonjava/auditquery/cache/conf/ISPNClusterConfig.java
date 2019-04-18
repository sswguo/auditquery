package org.commonjava.auditquery.cache.conf;

import org.apache.commons.lang3.StringUtils;
import org.commonjava.auditquery.core.conf.AuditQueryConfigInfo;
import org.commonjava.auditquery.core.conf.SystemPropertyProvider;
import org.commonjava.propulsor.config.annotation.ConfigName;
import org.commonjava.propulsor.config.annotation.SectionName;

import javax.enterprise.context.ApplicationScoped;
import java.util.Properties;

@ApplicationScoped
@SectionName("infinispan-cluster")
public class ISPNClusterConfig implements AuditQueryConfigInfo, SystemPropertyProvider
{

    private static final String AQ_JGROUPS_GOSSIP_ROUTER_HOSTS = "jgroups.gossip_router_hosts";

    private static final String AQ_JGROUP_TCP_BIND_PORT = "jgroups.tcp.bind_port";

    private static final String DEFAULT_AQ_JGROUP_TCP_BIND_PORT = "7800";

    private String gossipRouterHosts;

    private String tcpBindPort;

    public String getGossipRouterHosts() { return gossipRouterHosts; }

    @ConfigName( AQ_JGROUPS_GOSSIP_ROUTER_HOSTS )
    public void setGossipRouterHosts( String gossipRouterHosts ) { this.gossipRouterHosts = gossipRouterHosts; }

    public String getTcpBindPort() { return tcpBindPort == null ? DEFAULT_AQ_JGROUP_TCP_BIND_PORT : tcpBindPort; }

    @ConfigName( AQ_JGROUP_TCP_BIND_PORT )
    public void setTcpBindPort( String tcpBindPort ) { this.tcpBindPort = tcpBindPort; }

    @Override
    public Properties getSystemProperties()
    {
        Properties properties = new Properties();
        preparePropertyInSysEnv( properties, AQ_JGROUPS_GOSSIP_ROUTER_HOSTS, getGossipRouterHosts() );
        preparePropertyInSysEnv( properties, AQ_JGROUP_TCP_BIND_PORT, getTcpBindPort() );

        return properties;
    }

    private void preparePropertyInSysEnv( Properties props, String propName, String ifNotInSysEnv )
    {
        String propVal = System.getenv( propName );
        if ( StringUtils.isBlank( propVal ) )
        {
            propVal = System.getProperty( propName );
        }
        if ( StringUtils.isBlank( propVal ) )
        {
            propVal = ifNotInSysEnv;
        }
        if ( StringUtils.isNotBlank( propVal ) )
        {
            propVal = propVal.replace( "\"", "" );

            props.setProperty( propName, propVal );
        }
    }
}
