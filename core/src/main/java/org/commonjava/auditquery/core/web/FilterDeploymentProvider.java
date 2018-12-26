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
