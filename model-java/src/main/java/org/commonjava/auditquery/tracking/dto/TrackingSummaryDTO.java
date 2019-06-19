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
package org.commonjava.auditquery.tracking.dto;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

public class TrackingSummaryDTO implements Externalizable
{

    private static final int VERSION = 1;

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

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        out.writeObject( Integer.toString( VERSION ) );
        out.writeObject( trackingID );
        out.writeObject( Integer.toString( uploadCount ) );
        out.writeObject( Integer.toString( downloadCount ) );
        out.writeObject( startTime );
        out.writeObject( endTime );
    }

    @Override
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        int version = Integer.parseInt( ( String )in.readObject() );
        if ( version != VERSION )
        {
            throw new IOException( "Cannot deserialize. Unmatched version, class version: " + VERSION
                                                   + " vs. the version read from the data stream: " + version);
        }
        trackingID = (String)in.readObject();
        uploadCount = Integer.parseInt( (String)in.readObject() );
        downloadCount = Integer.parseInt( (String)in.readObject() );
        startTime = (Date)in.readObject();
        endTime = (Date)in.readObject();
    }
}
