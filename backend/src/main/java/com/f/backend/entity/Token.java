package com.f.backend.entity;


import jakarta.persistence.*;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @Column(name = "is_log_out")
    private boolean logout;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;


    public Token() {
    }

    public Token(long id, String token, boolean logout, User user) {
        this.id = id;
        this.token = token;
        this.logout = logout;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogout() {
        return logout;
    }

    public void setLogout(boolean logout) {
        this.logout = logout;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

