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
package org.commonjava.auditquery.olap.handler;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.commonjava.auditquery.tracking.io.AuditQueryObjectMapper;
import org.commonjava.util.jhttpc.HttpFactory;
import org.commonjava.util.jhttpc.auth.ClientAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.commonjava.auditquery.olap.handler.CallbackTarget.CallbackMethod.POST;
import static org.commonjava.auditquery.olap.handler.CallbackTarget.CallbackMethod.PUT;

@ApplicationScoped
public class ContentTrackingOLAPCallback
                implements Consumer<CallbackResult>
{

    private HttpFactory httpFactory;

    private final int TIMEOUT_IN_MILLIS = 30000;

    @Inject
    AuditQueryObjectMapper objectMapper;

    @Inject
    RetriableProcessor retriableProcessor;

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
        retriableProcessor.commit( new CallbackJob( callbackResult, callback ) );
    }

    Function<CallbackJob, Boolean> callback = ( CallbackJob job ) -> {
        CallbackResult result = job.getResult();
        Boolean isCallbackOK = Boolean.FALSE;
        try (CloseableHttpClient client = httpFactory.createClient())
        {

            CallbackTarget target = result.getRequest().getCallbackTarget();
            if ( target.getMethod().equals( POST ) )
            {
                HttpPost post = new HttpPost( target.getUrl() );
                isCallbackOK = execute( client, post, target, result );
            }
            else if ( target.getMethod().equals( PUT ) )
            {
                HttpPut put = new HttpPut( target.getUrl() );
                isCallbackOK = execute( client, put, target, result );
            }
            else
            {
                throw new IllegalArgumentException( target.getMethod() + " not supported" );
            }

        }
        catch ( Exception e )
        {
            Logger logger = LoggerFactory.getLogger( getClass() );
            logger.error( "Callback request error.", e );

            isCallbackOK = false;
        }
        return isCallbackOK;
    };

    private Boolean execute( CloseableHttpClient client,
                             HttpEntityEnclosingRequestBase request,
                             CallbackTarget target,
                             CallbackResult result ) throws  Exception
    {
        RequestConfig rc = RequestConfig.custom()
                                        .setSocketTimeout( TIMEOUT_IN_MILLIS )
                                        .setConnectTimeout( TIMEOUT_IN_MILLIS )
                                        .setConnectionRequestTimeout( TIMEOUT_IN_MILLIS )
                                        .build();
        request.setConfig( rc );

        Map<String, String> headers = target.getHeaders();
        if ( headers != null )
        {
            for ( String key : headers.keySet() )
            {
                request.setHeader( key, headers.get( key ) );
            }
        }

        request.addHeader( "Content-Type", "application/json" );
        request.setEntity( new StringEntity( objectMapper.writeValueAsString( result.getResultObj() ) ) );
        CloseableHttpResponse response = client.execute( request );
        return isCallbackOK( response.getStatusLine().getStatusCode() );
    }

    private boolean isCallbackOK( int statusCode )
    {
        return statusCode == 200;
    }
}
