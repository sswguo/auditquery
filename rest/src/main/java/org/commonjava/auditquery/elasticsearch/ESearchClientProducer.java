package org.commonjava.auditquery.elasticsearch;

import org.commonjava.auditquery.core.conf.ESearchConfiguration;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.net.InetAddress;

@ApplicationScoped
public class ESearchClientProducer
{

    @Inject
    ESearchConfiguration eSearchConfig;

    @Produces
    public Client createPreBuiltTransportClient()
    {
        Client client = null;
        try
        {
            Settings settings = Settings.builder().put( "cluster.name", eSearchConfig.getClusterName() ).build();
            client = new PreBuiltTransportClient( settings ).addTransportAddress(
                            new TransportAddress( InetAddress.getByName( eSearchConfig.getHost() ), eSearchConfig.getPort() ) );
        }
        catch ( Exception e )
        {
            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.error( "Start ES client error:", e );
        }
        return client;
    }
}
