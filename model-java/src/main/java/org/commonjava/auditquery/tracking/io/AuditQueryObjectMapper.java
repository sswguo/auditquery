package org.commonjava.auditquery.tracking.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class AuditQueryObjectMapper extends ObjectMapper
{
    @PostConstruct
    public void init()
    {

        setSerializationInclusion( JsonInclude.Include.NON_EMPTY );
        configure( JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, true );

        enable( SerializationFeature.INDENT_OUTPUT, SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID );

        enable( MapperFeature.AUTO_DETECT_FIELDS );

        disable( SerializationFeature.WRITE_NULL_MAP_VALUES, SerializationFeature.WRITE_EMPTY_JSON_ARRAYS );

        disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );

    }
}
