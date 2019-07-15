package com.test.hellographql.graphql.auth.directives;

import com.test.hellographql.graphql.auth.AuthContext;
import graphql.schema.DataFetchingEnvironment;

public class LogoutDirective extends AnstractDirective {

    @Override
    boolean runDirectiveLogic(DataFetchingEnvironment env, AuthContext context) {
        return context.logout();
    }

    @Override
    String getExceptionMsg() {
        return "Cannot log out.";
    }
}
