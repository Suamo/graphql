package com.test.hellographql;

import com.test.hellographql.graphql.auth.AuthContext;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@RestController
public class Controller {

    @Autowired
    private GraphQL graphql;

    @Autowired
    private AuthenticationTrustResolver trustResolver;

    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping(value = "/graphql")
    public ExecutionResult doGraphql(HttpServletRequest request, @RequestBody String requestBody) {
        String[] blocks = requestBody.split(",");
        String query = "";
        Map<String, Object> variables = new HashMap<>();
        for (String block : blocks) {
            String[] elements = block.split(":");
            if (elements[0].contains("query")) {
                query = elements[1].replaceAll("\"", "").replaceAll("\\\\n", "");
            } else if (elements[0].contains("variables")){
                System.out.println(elements[1]);
            }
        }
        AuthContext authCtx = new AuthContext(request.getSession(), trustResolver, userDetailsService);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(query)
                .variables(variables)
                .context(authCtx)
                .build();
        return graphql.execute(executionInput);
    }

}
