package org.commonjava.auditquery.olap.handler;

public class CallbackResult<T>
{

    private CallbackRequest request;

    private T resultObj;

    public CallbackResult( CallbackRequest request, T resultObj )
    {
        this.request = request;
        this.resultObj = resultObj;
    }

    public CallbackRequest getRequest()
    {
        return request;
    }

    public void setRequest( CallbackRequest request )
    {
        this.request = request;
    }

    public T getResultObj()
    {
        return resultObj;
    }

    public void setT( T resultObj )
    {
        this.resultObj = resultObj;
    }

    @Override
    public String toString()
    {
        return "CallbackResult{" + "request=" + request + ", resultObj=" + resultObj + '}';
    }
}
