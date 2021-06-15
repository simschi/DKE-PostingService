package com.postingservice.demo.model;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    private Long id;
    private String username;

    public User() {

    }

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
