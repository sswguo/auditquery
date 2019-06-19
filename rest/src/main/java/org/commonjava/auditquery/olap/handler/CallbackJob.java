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

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Function;

public class CallbackJob implements Serializable
{

    private UUID jobId;

    private CallbackResult result;

    private int retryCount = 0;

    private Function<CallbackJob, Boolean> exec;

    public CallbackJob( CallbackResult result, Function exec )
    {
        this.jobId = UUID.randomUUID();
        this.result = result;
        this.exec = exec;
    }

    public UUID getJobId()
    {
        return jobId;
    }

    public void setJobId( UUID jobId )
    {
        this.jobId = jobId;
    }

    public CallbackResult getResult()
    {
        return result;
    }

    public void setResult( CallbackResult result )
    {
        this.result = result;
    }

    public Boolean start()
    {
        return exec.apply( this );
    }

    public int getRetryCount()
    {
        return retryCount;
    }

    public void setRetryCount( int retryCount )
    {
        this.retryCount = retryCount;
    }

    public void increaseRetryCount()
    {
        retryCount++;
    }

    @Override
    public String toString()
    {
        return "CallbackJob{" + "jobId=" + jobId + ", result=" + result + '}';
    }
}
