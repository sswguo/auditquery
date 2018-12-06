package org.commonjava.auditquery.tracking.dto;

public class TrackedContentEntryDTO
{
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
}
