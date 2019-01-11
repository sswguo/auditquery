package org.commonjava.auditquery.elasticsearch;

import org.apache.http.HttpHost;
import org.commonjava.auditquery.core.conf.ESearchConfiguration;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Closeable;
import java.io.IOException;

@ApplicationScoped
public class ESearchClientFactory
                implements Closeable
{

    private RestHighLevelClient restClient;

    @Inject
    ESearchConfiguration eSearchConfig;

    public ESearchClientFactory()
    {

    }

    public RestHighLevelClient getRestClient() throws Exception
    {

        restClient = new RestHighLevelClient( RestClient.builder(
                        new HttpHost( eSearchConfig.getHost(), eSearchConfig.getPort(), "http" ) ) );
        return restClient;
    }

    @Override
    public void close() throws IOException
    {
        restClient.close();
    }
}
