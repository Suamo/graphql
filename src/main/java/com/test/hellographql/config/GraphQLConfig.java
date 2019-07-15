package com.test.hellographql.config;

import com.test.hellographql.graphql.datafetchers.CustomWiringFactory;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@Configuration
public class GraphQLConfig {

    @Value("${graphql.schema.path}")
    private String graphqlSchema;

    @Bean
    public CustomWiringFactory customWiringFactory() {
        return new CustomWiringFactory();
    }

    @Bean
    public GraphQLSchema graphQLSchema(CustomWiringFactory customWiringFactory) {
        TypeDefinitionRegistry registry = new SchemaParser().parse(getSchemaFile());
        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .wiringFactory(customWiringFactory)
                .build();
        return new SchemaGenerator()
                .makeExecutableSchema(registry, runtimeWiring);
    }

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private File getSchemaFile() {
        try {
            URL url = getClass().getResource(graphqlSchema);
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
