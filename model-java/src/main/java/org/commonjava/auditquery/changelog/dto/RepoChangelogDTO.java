/**
 * Copyright (C) 2013~2019 Red Hat, Inc.
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
package org.commonjava.auditquery.changelog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.commonjava.auditquery.changelog.RepositoryChangeLog;

import java.util.List;

public class RepoChangelogDTO
{
    @JsonProperty
    private Integer pageSize;

    @JsonProperty
    private Integer total;

    @JsonProperty
    private Integer curPage;

    @JsonProperty
    private List<RepositoryChangeLog> items;

    public RepoChangelogDTO()
    {
    }

    public RepoChangelogDTO( Integer pageSize, Integer total, Integer curPage, List<RepositoryChangeLog> items )
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

    public RepoChangelogDTO pageSize( Integer pageSize )
    {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getTotal()
    {
        return total;
    }

    public RepoChangelogDTO total( Integer total )
    {
        this.total = total;
        return this;
    }

    public Integer getCurPage()
    {
        return curPage;
    }

    public RepoChangelogDTO curPage( Integer curPage )
    {
        this.curPage = curPage;
        return this;
    }

    public List<RepositoryChangeLog> getItems()
    {
        return items;
    }

    public RepoChangelogDTO items( List<RepositoryChangeLog> items )
    {
        this.items = items;
        return this;
    }
}
