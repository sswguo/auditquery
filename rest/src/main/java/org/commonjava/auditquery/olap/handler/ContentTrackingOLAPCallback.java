package org.commonjava.auditquery.olap.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.commonjava.util.jhttpc.HttpFactory;
import org.commonjava.util.jhttpc.auth.ClientAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.function.Consumer;

@ApplicationScoped
public class ContentTrackingOLAPCallback
                implements Consumer<CallbackResult>
{

    private HttpFactory httpFactory;

    private final int TIMEOUT_IN_MILLIS = 30000;

    @Inject
    ObjectMapper objectMapper;

    public ContentTrackingOLAPCallback()
    {

    }

    @PostConstruct
    public void init()
    {
        ClientAuthenticator authenticator = null;
        httpFactory = new HttpFactory( authenticator );
    }


    @Override
    public void accept( CallbackResult callbackResult )
    {
        callback( new CallbackJob( callbackResult ) );
    }

    public void callback( CallbackJob job )
    {
        CallbackResult result = job.getResult();
        Boolean isCallbackOK = false;
        try( CloseableHttpClient client = httpFactory.createClient( ) )
        {

            CallbackTarget target = result.getRequest().getCallbackTarget();
            HttpPost post = new HttpPost( target.getUrl() );

            RequestConfig rc = RequestConfig.custom()
                                            .setSocketTimeout( TIMEOUT_IN_MILLIS )
                                            .setConnectTimeout( TIMEOUT_IN_MILLIS )
                                            .setConnectionRequestTimeout( TIMEOUT_IN_MILLIS )
                                            .build();
            post.setConfig( rc );

            Map<String, String> headers = target.getHeaders();
            if ( headers != null )
            {
                for ( String key : headers.keySet() )
                {
                    post.setHeader( key, headers.get( key ) );
                }
            }

            post.addHeader( "Content-Type", "application/json" );
            post.setEntity( new StringEntity( objectMapper.writeValueAsString( result.getResultObj() ) ) );
            CloseableHttpResponse response = client.execute( post );

            isCallbackOK = isCallbackOK ( response.getStatusLine().getStatusCode() );

            //TODO fix to handle the job failure
        }
        catch ( Exception e )
        {
            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.error( "Callback request error.", e );
        }
    }

    private boolean isCallbackOK( int statusCode )
    {
        return statusCode == 200;
    }
}
