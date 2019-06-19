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

import org.commonjava.auditquery.tracking.dto.TrackedContentEntryDTO;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TrackedContentEntryDTOTest extends AuditQueryModelTest
{

    @Test
    public void test() throws Exception
    {
        TrackedContentEntryDTO output = new TrackedContentEntryDTO(  );
        output.setStoreKey( "maven:remote:mrrc-ga-rh" );
        output.setPath( "/org/jboss/test/test-01.pom" );
        output.setAccessChannel( "MAVEN_REPO" );
        output.setSha1( "2a3b0cad0e022381d757d1913e7d2f5a8758ff32" );
        output.setMd5( "fcb4b9cad26db29d986f99534309f87a" );
        output.setSha256( "f8e5dacc1982c045a48b93e09e9b0beb6c820981dfaf5cf1578ac8806ea0e036" );
        output.setLocalUrl( "http://local.com/org/jboss/test/test-01.pom" );
        output.setOriginUrl( "http://remote.com/org/jboss/test/test-01.pom" );

        TrackedContentEntryDTO input = read( output );

        assertEquals(input.getAccessChannel(), output.getAccessChannel());
        assertEquals(input.getLocalUrl(), output.getLocalUrl());
        assertEquals(input.getMd5(), output.getMd5());
        assertEquals(input.getOriginUrl(), output.getOriginUrl());
        assertEquals(input.getPath(), output.getPath());
        assertEquals(input.getSha1(), output.getSha1());
        assertEquals(input.getSha256(), output.getSha256());
        assertEquals(input.getStoreKey(), output.getStoreKey());

    }

}
