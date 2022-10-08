package com.accumed.Admini.jwt;

public class UserNameAndPasswordAuthenticationRequest {
    String username;
    String password;

    public UserNameAndPasswordAuthenticationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
