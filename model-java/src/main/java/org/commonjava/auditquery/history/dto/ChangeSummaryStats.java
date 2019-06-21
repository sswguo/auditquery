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
package org.commonjava.auditquery.history.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.util.Date;

@ApiModel
public class ChangeSummaryStats
{
    @JsonProperty
    private String storeKey;

    @JsonProperty
    private Integer creates = 0;

    @JsonProperty
    private Integer updates = 0;

    @JsonProperty
    private Integer deletes = 0;

    @JsonProperty
    private Date lastUpdate;

    public String getStoreKey()
    {
        return storeKey;
    }

    public void setStoreKey( String storeKey )
    {
        this.storeKey = storeKey;
    }

    @JsonProperty
    public Integer getTotal()
    {
        return creates + updates + deletes;
    }

    public Integer getCreates()
    {
        return creates;
    }

    public void setCreates( Integer creates )
    {
        this.creates = creates;
    }

    public Integer getUpdates()
    {
        return updates;
    }

    public void setUpdates( Integer updates )
    {
        this.updates = updates;
    }

    public Integer getDeletes()
    {
        return deletes;
    }

    public void setDeletes( Integer deletes )
    {
        this.deletes = deletes;
    }

    public Date getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate( Date lastUpdate )
    {
        this.lastUpdate = lastUpdate;
    }

}
