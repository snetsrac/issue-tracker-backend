package com.snetsrac.issuetracker.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.auth0.json.mgmt.users.User;
import com.snetsrac.issuetracker.user.dto.UserDto;

public class UserTestData {

    public static final User USER;
    public static final UserDto USER_DTO;
    public static final List<User> USER_LIST;
    public static final List<UserDto> USER_DTO_LIST;

    static {
        USER = user();
        USER_DTO = userDto();
        USER_LIST = userList();
        USER_DTO_LIST = userDtoList();
    }

    private UserTestData() {
    }

    private static User user() {
        User user = new User();
        
        user.setId("123");
        user.setEmail("test@user.com");
        user.setName("Test User");
        user.setAppMetadata(Map.of("username", "test_user"));
        user.setPicture("test_user.picture.com");

        return user;
    }

    private static UserDto userDto() {
        UserDto userDto = new UserDto();

        userDto.setId("123");
        userDto.setEmail("test@user.com");
        userDto.setName("Test User");
        userDto.setUsername("test_user");
        userDto.setPicture("test_user.picture.com");

        return userDto;
    }

    private static List<User> userList() {

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

    private static List<UserDto> userDtoList() {

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
