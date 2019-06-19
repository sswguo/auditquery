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
package org.commonjava.auditquery.core.inject;

import org.commonjava.auditquery.core.conf.AuditQueryConfigInfo;
import org.commonjava.propulsor.config.ConfigurationException;
import org.commonjava.propulsor.config.ConfigurationRegistry;
import org.commonjava.propulsor.config.DefaultConfigurationListener;
import org.commonjava.propulsor.config.DefaultConfigurationRegistry;
import org.commonjava.propulsor.deploy.undertow.ui.UIConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class ConfigRegistryProducer
{

    @Inject
    UIConfiguration config;

    @Inject
    Instance<AuditQueryConfigInfo> configs;

    @Produces
    public ConfigurationRegistry getConfigurationRegistry() throws ConfigurationException
    {
        Logger logger = LoggerFactory.getLogger( getClass() );
        logger.info( "AuditQuery service configuration registry producer." );

        DefaultConfigurationListener configListener =
                        new DefaultConfigurationListener();

        configListener.with( config );

        configs.forEach( (conf) -> {
            try
            {
                configListener.with( conf );
            }
            catch ( ConfigurationException e )
            {
                logger.error( "Load configuration error, config:{}, error: {}", conf.getClass(), e.getMessage(), e );
            }
        });

        return new DefaultConfigurationRegistry( configListener );
    }

}
