package org.commonjava.auditquery.rest.exception;

public class AuditQueryWebException
                extends Throwable
{

    public AuditQueryWebException( String message )
    {
        super( message );
    }

    public AuditQueryWebException( Throwable throwable )
    {
        super( throwable );
    }

    public AuditQueryWebException( String message, Throwable throwable )
    {
        super( message, throwable );
    }

}
