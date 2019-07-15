package com.test.hellographql.graphql.auth.directives;

import com.test.hellographql.graphql.auth.AuthContext;
import graphql.schema.DataFetchingEnvironment;

public class AuthenticationDirective extends AnstractDirective {

    @Override
    boolean runDirectiveLogic(DataFetchingEnvironment env, AuthContext context) {
        return context.isUserAuthenticated();
    }

    @Override
    String getExceptionMsg() {
        return "Requested data is available only for authorized users.";
    }
}