package org.example.auth.models;


public class User {
    // Attributes
    private int id;
    private String name;
    private String email;
    private String password;
    private String cv;

    // Constructors
    public User() {
        // Default constructor
    }

    public User(String name, String email, String password, String cv) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cv = cv;
    }

    public User(int id, String name, String email, String password, String cv) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.cv = cv;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    // Override toString() for debugging purposes
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", cv='" + cv + '\'' +
                '}';
    }
}