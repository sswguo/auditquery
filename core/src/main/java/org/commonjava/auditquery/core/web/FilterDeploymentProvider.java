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
package org.commonjava.auditquery.core.web;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.commonjava.propulsor.deploy.undertow.UndertowDeploymentProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.DispatcherType;

@ApplicationScoped
public class FilterDeploymentProvider
                implements UndertowDeploymentProvider
{

    @Inject
    ResourcesFilter resourcesFilter;

    @Override
    public DeploymentInfo getDeploymentInfo()
    {
        final FilterInfo resourcesFilter =
                        Servlets.filter( "ResourceManagement", ResourcesFilter.class,
                                         new ImmediateInstanceFactory<ResourcesFilter>(
                                                         this.resourcesFilter ) );

        final DeploymentInfo di = new DeploymentInfo().addFilter( resourcesFilter )
                                                      .addFilterUrlMapping( resourcesFilter.getName(),
                                                                            "/browse/*", DispatcherType.REQUEST )
                                                      .setDeploymentName( "AuditQueryWeb" )
                                                      .setClassLoader( ClassLoader.getSystemClassLoader() );

        return di;
    }
}
