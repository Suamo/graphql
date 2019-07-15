package com.test.hellographql.graphql.datafetchers;

import com.test.hellographql.graphql.auth.directives.AuthenticationDirective;
import com.test.hellographql.graphql.auth.directives.LogInDirective;
import com.test.hellographql.graphql.auth.directives.LogoutDirective;
import com.test.hellographql.graphql.operation.Mutation;
import com.test.hellographql.graphql.operation.Query;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import graphql.schema.PropertyDataFetcher;
import graphql.schema.TypeResolver;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomWiringFactory implements WiringFactory {

    @Autowired
    private Query query;
    @Autowired
    private Mutation mutation;

    @Override
    public boolean providesDataFetcher(FieldWiringEnvironment environment) {
        String parentFieldName = environment.getParentType().getName();
        return "Query".equals(parentFieldName) || "Mutation".equals(parentFieldName);
    }

    @Override
    public DataFetcher getDataFetcher(FieldWiringEnvironment environment) {
        String fieldName = environment.getParentType().getName();
        if ("Query".equals(fieldName)) {
            return new RootDataFetcher(query);
        } else if ("Mutation".equals(fieldName)) {
            return new RootDataFetcher(mutation);
        }
        throw new GraphQLException("Cannot fetch type. Please check if 'providesDataFetcher' method properly defines allowed types.");
    }

    @Override
    public DataFetcher getDefaultDataFetcher(FieldWiringEnvironment environment) {
        String fieldName = environment.getFieldDefinition().getName();
        return PropertyDataFetcher.fetching(fieldName);
    }

    @Override
    public boolean providesSchemaDirectiveWiring(SchemaDirectiveWiringEnvironment environment) {
        return true;
    }

    @Override
    public SchemaDirectiveWiring getSchemaDirectiveWiring(SchemaDirectiveWiringEnvironment environment) {
        if ("auth".equals(environment.getDirective().getName())) {
            return new AuthenticationDirective();
        } if ("login".equals(environment.getDirective().getName())) {
            return new LogInDirective();
        }
        return new LogoutDirective();
    }

    @Override
    public boolean providesTypeResolver(InterfaceWiringEnvironment environment) {
        return true;
    }

    @Override
    public TypeResolver getTypeResolver(InterfaceWiringEnvironment environment) {
        return env -> {
            String className = env.getObject().getClass().getSimpleName();
            return env.getSchema().getObjectType(className);
        };
    }

    @Override
    public boolean providesTypeResolver(UnionWiringEnvironment environment) {
        return true;
    }

    @Override
    public TypeResolver getTypeResolver(UnionWiringEnvironment environment) {
        return env -> {
            String className = env.getObject().getClass().getSimpleName();
            return env.getSchema().getObjectType(className);
        };
    }
}
