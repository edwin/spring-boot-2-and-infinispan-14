package com.edw.config;

import com.edw.model.GenMdAccountEntity;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.marshall.MarshallerUtil;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *  com.edw.config.InfinispanInitializer
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 15 Oct 2024 19:47
 */
@Component
public class InfinispanInitializer implements CommandLineRunner {

    private RemoteCacheManager cacheManager;

    @Autowired
    public InfinispanInitializer(RemoteCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void run(String...args) throws Exception {
        SerializationContext ctx = MarshallerUtil.getSerializationContext(cacheManager);
        RemoteCache<String, String> protoMetadataCache = cacheManager.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);

        String msgSchemaFile = null;
        try {
            ProtoSchemaBuilder protoSchemaBuilder = new ProtoSchemaBuilder();
            msgSchemaFile = protoSchemaBuilder.fileName("GenMdAccountEntity.proto").packageName("default").addClass(GenMdAccountEntity.class).build(ctx);
            protoMetadataCache.put("GenMdAccountEntity.proto", msgSchemaFile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build protobuf definition from 'GenMdAccountEntity class'", e);
        }

        String errors = protoMetadataCache.get(ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX);
        if (errors != null) {
            throw new IllegalStateException("Some Protobuf schema files contain errors: " + errors + "\nSchema :\n" + msgSchemaFile);
        }
    }

}
