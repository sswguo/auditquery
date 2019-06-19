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
