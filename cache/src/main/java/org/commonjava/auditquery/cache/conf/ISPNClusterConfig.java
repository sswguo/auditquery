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
