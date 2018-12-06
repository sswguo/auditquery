package org.commonjava.auditquery.tracking.dto;

import java.util.Set;

public class TrackedContentDTO
{
    private String trackingID;

    private Set<TrackedContentEntryDTO> uploads;

    private Set<TrackedContentEntryDTO> downloads;

    public TrackedContentDTO()
    {

    }

    public TrackedContentDTO( final String trackingID, final Set<TrackedContentEntryDTO> uploads,
                              final Set<TrackedContentEntryDTO> downloads )
    {
        this.trackingID = trackingID;
        this.uploads = uploads;
        this.downloads = downloads;
    }

    public String getTrackingID() { return trackingID; }

    public void setTrackingID( String trackingID ) { this.trackingID = trackingID; }

    public Set<TrackedContentEntryDTO> getUploads() { return uploads; }

    public void setUploads( Set<TrackedContentEntryDTO> uploads ) { this.uploads = uploads; }

    public Set<TrackedContentEntryDTO> getDownloads() { return downloads; }

    public void setDownloads( Set<TrackedContentEntryDTO> downloads ) { this.downloads = downloads; }
}
