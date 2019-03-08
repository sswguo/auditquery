package org.commonjava.auditquery.boot;

import org.commonjava.propulsor.boot.BootOptions;
import org.commonjava.propulsor.boot.Booter;

public class Main
{
    public static void main( String[] args ) throws Exception
    {
        BootOptions options = Booter.loadFromSysProps( "auditquery", "auditquery.boot.defaults", "auditquery.home" );
        Booter booter = new Booter();
        booter.runAndWait( options );
    }
}
