package org.commonjava.auditquery.olap.handler;

public class CallbackRequest
{

    private String trackingID;

    private CallbackTarget callbackTarget;

    public String getTrackingID()
    {
        return trackingID;
    }

    public void setTrackingID( String trackingID )
    {
        this.trackingID = trackingID;
    }

    public CallbackTarget getCallbackTarget()
    {
        return callbackTarget;
    }

    public void setCallbackTarget( CallbackTarget callbackTarget )
    {
        this.callbackTarget = callbackTarget;
    }

    @Override
    public String toString()
    {
        return "CallbackRequest{" + "trackingID='" + trackingID + '\'' + ", callbackTarget=" + callbackTarget + '}';
    }
}
