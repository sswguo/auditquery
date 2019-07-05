package org.commonjava.auditquery.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class PaginatedResult<T>
{

    @JsonProperty
    private Integer pageSize;

    @JsonProperty
    private Integer total;

    @JsonProperty
    private Integer curPage;

    @JsonProperty
    private Collection<T> items;

    public Integer getPageSize()
    {
        return pageSize;
    }

    public PaginatedResult pageSize( Integer pageSize )
    {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getTotal()
    {
        return total;
    }

    public PaginatedResult total( Integer total )
    {
        this.total = total;
        return this;
    }

    public Integer getCurPage()
    {
        return curPage;
    }

    public PaginatedResult curPage( Integer curPage )
    {
        this.curPage = curPage;
        return this;
    }

    public Collection<T> getItems()
    {
        return items;
    }

    public PaginatedResult items( Collection<T> items )
    {
        this.items = items;
        return this;
    }
}
