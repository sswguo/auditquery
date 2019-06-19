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
package org.commonjava.auditquery.fileevent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileGroupingEvent implements Comparable<FileGroupingEvent>, Externalizable
{

    private static final int VERSION = 1;

    private UUID eventId;

    private String sessionId;

    private List<UUID> fileEvents;

    private String nodeId;

    private FileGroupingEventType eventType;

    private String requestId;

    private Integer eventVersion;

    private Map<String, String> extra;

    @JsonFormat( shape = JsonFormat.Shape.STRING )
    private Date timestamp;

    public FileGroupingEvent()
    {
        this.eventId = UUID.randomUUID();
        this.eventVersion = 1;
    }

    public FileGroupingEvent( @JsonProperty( "eventType" ) FileGroupingEventType eventType )
    {
        this.eventId = UUID.randomUUID();
        this.eventVersion = 1;
        this.eventType = eventType;
    }

    public UUID getEventId() { return eventId; }

    public String getSessionId() { return sessionId; }

    public void setSessionId( String sessionId ) { this.sessionId = sessionId; }

    public List<UUID> getFileEvents() { return fileEvents; }

    public void setFileEvents( List<UUID> fileEvents ) { this.fileEvents = fileEvents; }

    public String getNodeId() { return nodeId; }

    public void setNodeId( String nodeId ) { this.nodeId = nodeId; }

    public FileGroupingEventType getEventType() { return eventType; }

    public void setEventType( FileGroupingEventType eventType ) { this.eventType = eventType; }

    public String getRequestId() { return requestId; }

    public void setRequestId( String requestId ) { this.requestId = requestId; }

    public Integer getEventVersion() { return eventVersion; }

    public void setEventVersion( Integer eventVersion ) { this.eventVersion = eventVersion; }

    public Map<String, String> getExtra() { return extra; }

    public void setExtra( Map<String, String> extra ) { this.extra = extra; }

    public Date getTimestamp() { return timestamp; }

    public void setTimestamp( Date timestamp ) { this.timestamp = timestamp; }

    @Override
    public int compareTo( FileGroupingEvent o )
    {
        return getTimestamp().compareTo( o.getTimestamp() );
    }

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        out.writeObject( Integer.toString( VERSION ) );
        out.writeObject( eventId );
        out.writeObject( sessionId );
        out.writeObject( nodeId );
        out.writeObject( fileEvents );
        out.writeObject( eventType );
        out.writeObject( requestId );
        out.writeObject( eventVersion );
        out.writeObject( extra );
    }

    @Override
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        int version = Integer.parseInt( ( String )in.readObject() );
        if ( version > VERSION )
        {
            throw new IOException(
                            "Cannot deserialize. This class is of an older version: " + VERSION +
                                            " vs. the version read from the data stream: " + version + "." );
        }
        eventId = ( UUID )in.readObject();
        sessionId = ( String )in.readObject();
        nodeId = ( String )in.readObject();
        List<UUID> events = (List<UUID>) in.readObject();
        fileEvents = events == null ? new ArrayList<>(  ) : new ArrayList<>( events );
        eventType = ( FileGroupingEventType )in.readObject();
        requestId = ( String )in.readObject();
        eventVersion = ( Integer )in.readObject();
        Map<String,String> extraMap = (Map<String,String>)in.readObject();
        extra = extraMap == null ? new HashMap<>(  ) : new HashMap<>( extraMap );
    }

}
