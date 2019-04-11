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
