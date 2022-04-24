package com.snetsrac.issuetracker.user.dto;

import java.util.Objects;

/**
 * Represents the {@code User} object that will be provided by the server in API
 * responses.
 */
public class UserDto {

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

        if (!(obj instanceof UserDto))
            return false;

        UserDto userDto = (UserDto) obj;

        return Objects.equals(userDto.id, this.id) &&
                Objects.equals(userDto.name, this.name) &&
                Objects.equals(userDto.email, this.email) &&
                Objects.equals(userDto.username, this.username) &&
                Objects.equals(userDto.picture, this.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, username, picture);
    }

    @Override
    public String toString() {
        return "UserDto [id=" + id + ", name=" + name + ", email=" + email + ", username=" + username + ", picture="
                + picture + "]";
    }
}
