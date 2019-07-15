package com.test.hellographql.graphql.auth.directives;

import com.test.hellographql.graphql.auth.AuthContext;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

public class LogInDirective extends AnstractDirective {

    @Override
    boolean runDirectiveLogic(DataFetchingEnvironment env, AuthContext context) {
        LinkedHashMap<String, String> credentials = env.getArgument("credentials");
        String username = credentials.get("username");
        String password = credentials.get("password");

        return context.login(username, password);
    }

    @Override
    String getExceptionMsg() {
        return "Cannot log in.";
    }
}
