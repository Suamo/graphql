package com.test.hellographql.graphql.auth.directives;

import com.test.hellographql.graphql.auth.AuthContext;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

public abstract class AnstractDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
        GraphQLFieldDefinition field = env.getElement();

        DataFetcher authDataFetcher = dataFetchingEnvironment -> {
            AuthContext authContext = dataFetchingEnvironment.getContext();

            if (runDirectiveLogic(dataFetchingEnvironment, authContext)) {
                return field.getDataFetcher().get(dataFetchingEnvironment);
            } else {
                throw new GraphQLException(getExceptionMsg());
            }
        };
        return field.transform(builder -> builder.dataFetcher(authDataFetcher));
    }

    abstract boolean runDirectiveLogic(DataFetchingEnvironment env, AuthContext context);

    abstract String getExceptionMsg();
}
