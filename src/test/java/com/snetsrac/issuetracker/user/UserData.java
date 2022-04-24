package com.snetsrac.issuetracker.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.auth0.json.mgmt.users.User;
import com.snetsrac.issuetracker.user.dto.UserDto;

public class UserData {
    
    public static List<User> userList() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        users.get(0).setId("123");
        users.get(0).setEmail("test1@user.com");
        users.get(0).setName("Test User 1");
        users.get(0).setAppMetadata(Map.of("username", "test_user_1"));
        users.get(0).setPicture("test_user_1.picture.com");

        users.get(1).setId("456");
        users.get(1).setEmail("test2@user.com");
        users.get(1).setName("Test User 2");
        users.get(1).setAppMetadata(Map.of("username", "test_user_2"));
        users.get(1).setPicture("test_user_2.picture.com");

        users.get(2).setId("789");
        users.get(2).setEmail("test3@user.com");
        users.get(2).setName("Test User 3");
        users.get(2).setAppMetadata(Map.of("username", "test_user_3"));
        users.get(2).setPicture("test_user_3.picture.com");

        return users;
    }
    
    public static List<UserDto> userDtoList() {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(new UserDto());
        userDtos.add(new UserDto());
        userDtos.add(new UserDto());

        userDtos.get(0).setId("123");
        userDtos.get(0).setEmail("test1@user.com");
        userDtos.get(0).setName("Test User 1");
        userDtos.get(0).setUsername("test_user_1");
        userDtos.get(0).setPicture("test_user_1.picture.com");

        userDtos.get(1).setId("456");
        userDtos.get(1).setEmail("test2@user.com");
        userDtos.get(1).setName("Test User 2");
        userDtos.get(1).setUsername("test_user_2");
        userDtos.get(1).setPicture("test_user_2.picture.com");

        userDtos.get(2).setId("789");
        userDtos.get(2).setEmail("test3@user.com");
        userDtos.get(2).setName("Test User 3");
        userDtos.get(2).setUsername("test_user_3");
        userDtos.get(2).setPicture("test_user_3.picture.com");

        return userDtos;
    }

}
