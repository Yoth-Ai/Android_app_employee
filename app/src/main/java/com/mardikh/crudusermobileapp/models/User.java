package com.mardikh.crudusermobileapp.models;

public class User {

    private int id;

    private String name;

    private String email;

    private String gender;

    private Role Role;

    public User() {
    }

    public User(int id, String name, String email, String gender, com.mardikh.crudusermobileapp.models.Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        Role = role;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return Role;
    }

    public void setRole(Role role) {
        Role = role;
    }
}
