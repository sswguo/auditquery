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
