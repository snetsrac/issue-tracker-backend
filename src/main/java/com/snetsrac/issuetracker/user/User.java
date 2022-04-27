package com.snetsrac.issuetracker.user;

import java.util.Objects;

/**
 * Represents a generic, implementation-independent {@code User} backend object.
 */
public class User {

    private String id;
    private String email;
    private String name;
    private String username;
    private String picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof User))
            return false;

        User user = (User) obj;

        return Objects.equals(user.id, this.id) &&
                Objects.equals(user.name, this.name) &&
                Objects.equals(user.email, this.email) &&
                Objects.equals(user.username, this.username) &&
                Objects.equals(user.picture, this.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, username, picture);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", username=" + username + ", picture="
                + picture + "]";
    }
}
