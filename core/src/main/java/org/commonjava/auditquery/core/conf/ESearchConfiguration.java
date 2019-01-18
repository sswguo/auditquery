package org.commonjava.auditquery.core.conf;

import org.commonjava.propulsor.config.annotation.ConfigName;
import org.commonjava.propulsor.config.annotation.SectionName;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@SectionName("elasticsearch")
public class ESearchConfiguration
{

    public ESearchConfiguration()
    {

    }

    private String host;

    private Integer port;

    private String trackingSummaryIndex;

    private String fileEventIndex;

    public String getHost()
    {
        return host;
    }

    @ConfigName( "host" )
    public void setHost( String host )
    {
        this.host = host;
    }

    public Integer getPort() { return port; }

    @ConfigName( "port" )
    public void setPort( Integer port )
    {
        this.port = port;
    }

    public String getTrackingSummaryIndex() { return trackingSummaryIndex; }

    @ConfigName( "index.tracking_summary" )
    public void setTrackingSummaryIndex( String trackingSummaryIndex ) { this.trackingSummaryIndex = trackingSummaryIndex; }

    public String getFileEventIndex() { return fileEventIndex; }

    @ConfigName( "index.file_event" )
    public void setFileEventIndex( String fileEventIndex ) { this.fileEventIndex = fileEventIndex; }
}
