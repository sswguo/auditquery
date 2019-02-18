package org.commonjava.auditquery.olap.handler;

public class CallbackRequest
{

    private String TrackingID;

    private CallbackTarget callbackTarget;

    public String getTrackingID()
    {
        return TrackingID;
    }

    public void setTrackingID( String trackingID )
    {
        TrackingID = trackingID;
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
        return "CallbackRequest{" + "TrackingID='" + TrackingID + '\'' + ", callbackTarget=" + callbackTarget + '}';
    }
}
