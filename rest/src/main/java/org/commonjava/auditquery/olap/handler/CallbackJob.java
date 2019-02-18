package org.commonjava.auditquery.olap.handler;

import java.io.Serializable;
import java.util.UUID;

public class CallbackJob implements Serializable
{

    private UUID jobId;

    private CallbackResult result;

    public CallbackJob( CallbackResult result )
    {
        this.jobId = UUID.randomUUID();
        this.result = result;
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

    @Override
    public String toString()
    {
        return "CallbackJob{" + "jobId=" + jobId + ", result=" + result + '}';
    }
}
