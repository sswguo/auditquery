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

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.Set;

@Indexed
public class TrackedContentDTO implements Externalizable
{

    private static final int VERSION = 1;

    @Field
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

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        out.writeObject( Integer.toString( VERSION ) );
        out.writeObject( trackingID );
        out.writeObject( uploads );
        out.writeObject( downloads );
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
        trackingID = (String) in.readObject();
        Set<TrackedContentEntryDTO> ups = (Set<TrackedContentEntryDTO>) in.readObject();
        uploads = ups == null ? new HashSet<>() : new HashSet<>( ups );
        Set<TrackedContentEntryDTO> downs = (Set<TrackedContentEntryDTO>) in.readObject();
        downloads = downs == null ? new HashSet<>() : new HashSet<>( downs );
    }
}
