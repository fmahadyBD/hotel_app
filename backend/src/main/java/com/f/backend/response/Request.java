package com.f.backend.response;

import jakarta.persistence.Column;

public class Request {

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String Password;

    public Request() {
    }

    public Request(String username, String password) {
        this.username = username;
        Password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}