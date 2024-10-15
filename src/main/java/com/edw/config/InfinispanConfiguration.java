package com.edw.config;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;
import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *  com.edw.config.InfinispanConfiguration
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 15 Oct 2024 19:47
 */
@Configuration
public class InfinispanConfiguration {
    @Bean
    public RemoteCacheManager remoteCacheManager() {
        return new RemoteCacheManager(
                new org.infinispan.client.hotrod.configuration.ConfigurationBuilder()
                        .addServers("localhost:11222")
                        .security().authentication().username("admin2").password("password")
                        .clientIntelligence(ClientIntelligence.HASH_DISTRIBUTION_AWARE)
                        .marshaller(ProtoStreamMarshaller.class)
                        .build());
    }
}
