package com.test.hellographql.config;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${mongo.database}")
    private String mongoDbName;


    @Bean
    public Datastore datastore() {
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.test.hellographql.graphql.data");
        return morphia.createDatastore(new MongoClient(), mongoDbName);
    }

}
