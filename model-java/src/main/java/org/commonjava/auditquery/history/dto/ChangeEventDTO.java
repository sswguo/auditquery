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
import org.commonjava.auditquery.history.ChangeEvent;

import java.util.List;

@ApiModel
public class ChangeEventDTO
{
    @JsonProperty
    private Integer pageSize;

    @JsonProperty
    private Integer total;

    @JsonProperty
    private Integer curPage;

    @JsonProperty
    private List<ChangeEvent> items;

    public ChangeEventDTO()
    {
    }

    public ChangeEventDTO( Integer pageSize, Integer total, Integer curPage, List<ChangeEvent> items )
    {
        this.pageSize = pageSize;
        this.total = total;
        this.curPage = curPage;
        this.items = items;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public ChangeEventDTO pageSize( Integer pageSize )
    {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getTotal()
    {
        return total;
    }

    public ChangeEventDTO total( Integer total )
    {
        this.total = total;
        return this;
    }

    public Integer getCurPage()
    {
        return curPage;
    }

    public ChangeEventDTO curPage( Integer curPage )
    {
        this.curPage = curPage;
        return this;
    }

    public List<ChangeEvent> getItems()
    {
        return items;
    }

    public ChangeEventDTO items( List<ChangeEvent> items )
    {
        this.items = items;
        return this;
    }


}
