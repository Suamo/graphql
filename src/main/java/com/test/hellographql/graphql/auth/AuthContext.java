package com.test.hellographql.graphql.auth;

import graphql.GraphQLException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpSession;

public class AuthContext {

    private static final String INCORRECT_CREDENTIALS_MSG = "User or password is invalid!";

    private HttpSession session;
    private AuthenticationTrustResolver trustResolver;
    private UserDetailsService userDetailsService;

    public AuthContext(HttpSession session,
                       AuthenticationTrustResolver trustResolver,
                       UserDetailsService userDetailsService) {
        this.session = session;
        this.trustResolver = trustResolver;
        this.userDetailsService = userDetailsService;
    }

    public boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated() &&
                !trustResolver.isAnonymous(authentication);
    }

    public boolean login(String username, String password) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new GraphQLException(INCORRECT_CREDENTIALS_MSG);
        }
        //fixme: check credentials

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.getId();

        return true;
    }

    public boolean logout() {
        session.invalidate();
        return true;
    }
}
