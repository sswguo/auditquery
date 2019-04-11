/**
 * Copyright (C) 2011-2018 Red Hat, Inc. (https://github.com/Commonjava/auditquery)
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

package org.commonjava.auditquery.core.conf;

import org.commonjava.propulsor.config.annotation.ConfigName;
import org.commonjava.propulsor.config.annotation.SectionName;
import org.commonjava.propulsor.config.section.ConfigurationSectionListener;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@SectionName( ConfigurationSectionListener.DEFAULT_SECTION )
public class AuditQueryConfig
{

    private String indyUrl;

    public String getIndyUrl()
    {
        return indyUrl;
    }

    @ConfigName( "indy.url")
    public void setIndyUrl( String indyUrl )
    {
        this.indyUrl = indyUrl;
    }
}
