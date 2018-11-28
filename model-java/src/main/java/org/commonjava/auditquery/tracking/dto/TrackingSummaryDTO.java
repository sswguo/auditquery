package org.commonjava.auditquery.tracking.dto;

import org.commonjava.auditquery.tracking.TrackingSummary;

import java.util.Date;

public class TrackingSummaryDTO
{
    private String trackingID;

    private int uploadCount;

    private int downloadCount;

    private Date startTime;

    private Date endTime;

    public TrackingSummaryDTO()
    {

    }

    public TrackingSummaryDTO( final String trackingID, final int uploadCount, final int downloadCount )
    {
        this.trackingID = trackingID;
        this.uploadCount = uploadCount;
        this.downloadCount = downloadCount;
    }

    public String getTrackingID() { return trackingID; }

    public void setTrackingID( String trackingID ) { this.trackingID = trackingID; }

    public int getUploadCount() { return uploadCount; }

    public void setUploadCount( int uploadCount ) { this.uploadCount = uploadCount; }

    public int getDownloadCount() { return downloadCount; }

    public void setDownloadCount( int downloadCount ) { this.downloadCount = downloadCount; }

    public Date getStartTime() { return startTime; }

    public void setStartTime( Date startTime ) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }

    public void setEndTime( Date endTime ) { this.endTime = endTime; }
}
