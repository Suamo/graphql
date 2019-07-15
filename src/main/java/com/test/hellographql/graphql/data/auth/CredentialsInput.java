package com.test.hellographql.graphql.data.auth;

public class CredentialsInput {
    private String username;
    private String password;

    public CredentialsInput() {
    }

    public CredentialsInput(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
