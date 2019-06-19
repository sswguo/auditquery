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
package org.commonjava.auditquery.tracking;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Indexed
public class TrackingSummary implements Externalizable
{

    private static final int VERSION = 1;

    @Field
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

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        out.writeObject( Integer.toString( VERSION ) );
        out.writeObject( trackingID );
        out.writeObject( uploads );
        out.writeObject( downloads );
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
        trackingID = ( String )in.readObject();
        Set<String> ups = (Set<String>)in.readObject();
        uploads = ups == null ? new HashSet<>() : new HashSet<>( ups );
        Set<String> downs = (Set<String>)in.readObject();
        downloads = downs == null ? new HashSet<>() : new HashSet<>( downs );
        startTime = (Date)in.readObject();
        endTime = (Date)in.readObject();

    }
}
