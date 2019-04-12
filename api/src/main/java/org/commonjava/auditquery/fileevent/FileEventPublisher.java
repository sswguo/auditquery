package org.commonjava.auditquery.fileevent;

/**
 * This interface provides methods for producing fileevent audit events
 */
public interface FileEventPublisher
{

    void publishFileEvent( FileEvent fileEvent ) throws FileEventPublisherException;

    void publishFileGroupingEvent( FileGroupingEvent fileGroupingEvent ) throws FileEventPublisherException;

}
