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

public class TrackedContentEntryDTO implements Comparable<TrackedContentEntryDTO>, Externalizable
{

    private static final int VERSION = 1;

    /* current artifact repository key info */
    private String storeKey;

    /* calculated from package type of storeKey */
    private String accessChannel;

    /* current path */
    private String path;

    /* calculated from original store URL and path from first STORE event */
    private String originUrl;

    /* calculated from current storeKey + path + a configured Indy baseURL */
    private String localUrl;

    private String md5;

    private String sha256;

    private String sha1;

    private Long size;

    public TrackedContentEntryDTO()
    {

    }

    public TrackedContentEntryDTO( final String storeKey, final String accessChannel, final String path )
    {
        this.storeKey = storeKey;
        this.accessChannel = accessChannel;
        this.path = path;
    }

    public String getStoreKey() { return storeKey; }

    public void setStoreKey( String storeKey ) { this.storeKey = storeKey; }

    public String getAccessChannel() { return accessChannel; }

    public void setAccessChannel( String accessChannel ) { this.accessChannel = accessChannel; }

    public String getPath() { return path; }

    public void setPath( String path ) { this.path = path; }

    public String getOriginUrl() { return originUrl; }

    public void setOriginUrl( String originUrl ) { this.originUrl = originUrl; }

    public String getLocalUrl() { return localUrl; }

    public void setLocalUrl( String localUrl ) { this.localUrl = localUrl; }

    public String getMd5() { return md5; }

    public void setMd5( String md5 ) { this.md5 = md5; }

    public String getSha256() { return sha256; }

    public void setSha256( String sha256 ) { this.sha256 = sha256; }

    public String getSha1() { return sha1; }

    public void setSha1( String sha1 ) { this.sha1 = sha1; }

    public Long getSize() { return size; }

    public void setSize( Long size ) { this.size = size; }

    @Override
    public int compareTo( TrackedContentEntryDTO other )
    {
        int comp = storeKey.compareTo( other.getStoreKey() );
        if ( comp == 0 )
        {
            comp = accessChannel.compareTo( other.getAccessChannel() );
        }
        if ( comp == 0 )
        {
            comp = path.compareTo( other.getPath() );
        }

        return comp;
    }

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        out.writeObject( Integer.toString( VERSION ) );
        out.writeObject( storeKey );
        out.writeObject( accessChannel );
        out.writeObject( path );
        out.writeObject( originUrl );
        out.writeObject( localUrl );
        out.writeObject( md5 );
        out.writeObject( sha256 );
        out.writeObject( sha1 );
        out.writeObject( size );
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
        storeKey = (String)in.readObject();
        accessChannel = (String)in.readObject();
        path = (String)in.readObject();
        originUrl = (String)in.readObject();
        localUrl = (String)in.readObject();
        md5 = (String)in.readObject();
        sha256 = (String)in.readObject();
        sha1 = (String)in.readObject();
        size = (Long)in.readObject();
    }
}
