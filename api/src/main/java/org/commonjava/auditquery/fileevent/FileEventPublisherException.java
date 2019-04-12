package org.commonjava.auditquery.fileevent;

public class FileEventPublisherException
                extends RuntimeException
{
    public FileEventPublisherException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public FileEventPublisherException( String message )
    {
        super( message );
    }

    public FileEventPublisherException( Throwable cause )
    {
        super( cause );
    }

    public FileEventPublisherException()
    {
        super();
    }
}
