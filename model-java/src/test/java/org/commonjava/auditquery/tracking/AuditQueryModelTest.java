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

import org.junit.After;
import org.junit.Before;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AuditQueryModelTest
{

    File tempFile;

    @Before
    public void start() throws Exception
    {
        tempFile = File.createTempFile( "objectOutput", ".tmp" );
    }

    protected <T extends Externalizable> T read(T output) throws Exception
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream( tempFile );
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream( fileOutputStream ))
        {
            output.writeExternal( objectOutputStream );
        }

        T input = (T) output.getClass().getDeclaredConstructor().newInstance();
        try (FileInputStream fileInputStream = new FileInputStream( tempFile );
                        ObjectInputStream objectInputStream = new ObjectInputStream( fileInputStream ))
        {
            input.readExternal( objectInputStream );
        }
        return input;
    }

    @After
    public void end()
    {
        if ( tempFile.exists() )
        {
            tempFile.delete();
        }
    }

}
