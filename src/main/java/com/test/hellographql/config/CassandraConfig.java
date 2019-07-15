package com.test.hellographql.config;

import com.datastax.driver.core.Cluster;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {

    @Value("${cassandra.host}")
    private String cassandraHost;

    @Value("${cassandra.cluster}")
    private String clusterName;


    @Bean
    public Cluster cluster() {
        return Cluster.builder()
                .addContactPoint(cassandraHost)
                .withClusterName(clusterName)
                .build();
    }
}
