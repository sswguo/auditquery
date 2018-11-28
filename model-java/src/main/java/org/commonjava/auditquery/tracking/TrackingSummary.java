package org.commonjava.auditquery.tracking;

import java.util.Date;
import java.util.Set;

public class TrackingSummary
{
    private String trackingID;

    /* checksums */
    private Set<String> uploads;

    /* checksums */
    private Set<String> downloads;

    /* first timestamp in corresponding events */
    private Date startTime;

    /* last timestamp in corresponding events */
    private Date endTime;

    public TrackingSummary()
    {

    }

    public TrackingSummary( final String trackingID, final Set<String> uploads, final Set<String> downloads )
    {
        this.trackingID = trackingID;
        this.uploads = uploads;
        this.downloads = downloads;
    }

    public String getTrackingID() { return trackingID; }

    public void setTrackingID( String trackingID ) { this.trackingID = trackingID; }

    public Set<String> getUploads() { return uploads; }

    public void setUploads( Set<String> uploads ) { this.uploads = uploads; }

    public Set<String> getDownloads() { return downloads; }

    public void setDownloads( Set<String> downloads ) { this.downloads = downloads; }

    public Date getStartTime() { return startTime; }

    public void setStartTime( Date startTime ) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }

    public void setEndTime( Date endTime ) { this.endTime = endTime; }
}
