package org.example.pi.models;

public class User {
    private Long id;
    private String username;
    private String role; // "employee" ou "RH"

    public User(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}