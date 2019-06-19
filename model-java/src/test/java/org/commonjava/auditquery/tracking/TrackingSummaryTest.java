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

import org.commonjava.auditquery.tracking.dto.TrackingSummaryDTO;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

public class TrackingSummaryTest extends AuditQueryModelTest
{

    @Test
    public void testTrackingSummary() throws Exception
    {
        TrackingSummary output = new TrackingSummary();
        output.setTrackingID( "build-test_001" );
        Set<String> ups = new HashSet<>();
        ups.add( "a9e723099fcd884fe04535a2be1e0fc8" );
        ups.add( "4308110efa94007117a06ea8771b1e9c" );
        output.setUploads( new HashSet<>( ups ) );
        Set<String> downs = new HashSet<>();
        downs.add( "fcb4b9cad26db29d986f99534309f87a" );
        downs.add( "f8f1352c52a4c6a500b597596501fc64" );
        output.setDownloads( downs );
        output.setStartTime( new Date() );
        output.setEndTime( new Date() );

        TrackingSummary input = read( output );

        assertEquals( input.getTrackingID(),  output.getTrackingID() );
        assertEquals( input.getStartTime(), output.getStartTime() );
        assertEquals( input.getUploads(), output.getUploads() );
        assertEquals( input.getDownloads(),  output.getDownloads() );
        assertEquals( input.getEndTime(), output.getEndTime() );
    }

    @Test
    public void testTrackingSummaryDTO() throws Exception
    {
        TrackingSummaryDTO output = new TrackingSummaryDTO(  );
        output.setTrackingID( "build-test_001" );
        output.setUploadCount( 10 );
        output.setDownloadCount( 1000 );
        output.setStartTime( new Date(  ) );
        output.setEndTime( new Date(  ) );

        TrackingSummaryDTO input = read( output );

        assertEquals( input.getTrackingID(),  output.getTrackingID() );
        assertEquals( input.getStartTime(), output.getStartTime() );
        assertEquals( input.getDownloadCount(), output.getDownloadCount() );
        assertEquals( input.getUploadCount(),  output.getUploadCount() );
        assertEquals( input.getEndTime(), output.getEndTime() );
    }

}
