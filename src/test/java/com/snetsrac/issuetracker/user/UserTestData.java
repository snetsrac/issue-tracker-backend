package com.snetsrac.issuetracker.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.dto.UserDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class UserTestData {

    public static final Collection<String> USER_IDS;
    public static final User USER;
    public static final UserDto USER_DTO;
    public static final List<User> USER_LIST;
    public static final List<UserDto> USER_DTO_LIST;
    public static final Page<User> USER_PAGE;
    public static final PageDto<UserDto> USER_DTO_PAGE_DTO;
    public static final com.auth0.json.mgmt.users.User AUTH0_USER;
    public static final List<com.auth0.json.mgmt.users.User> AUTH0_USER_LIST;

    static {
        USER_IDS = List.of("123", "456", "789");
        USER = user();
        USER_DTO = userDto();
        USER_LIST = userList();
        USER_DTO_LIST = userDtoList();
        USER_PAGE = userPage();
        USER_DTO_PAGE_DTO = userDtoPageDto();
        AUTH0_USER = auth0User();
        AUTH0_USER_LIST = auth0UserList();
    }

    private UserTestData() {
    }

    private static User user() {
        User user = new User();

        user.setId("123");
        user.setEmail("test@user.com");
        user.setName("Test User");
        user.setUsername("test_user");
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

        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());

        userList.get(0).setId("123");
        userList.get(0).setEmail("test1@user.com");
        userList.get(0).setName("Test User 1");
        userList.get(0).setUsername("test_user_1");
        userList.get(0).setPicture("test_user_1.picture.com");

        userList.get(1).setId("456");
        userList.get(1).setEmail("test2@user.com");
        userList.get(1).setName("Test User 2");
        userList.get(1).setUsername("test_user_2");
        userList.get(1).setPicture("test_user_2.picture.com");

        userList.get(2).setId("789");
        userList.get(2).setEmail("test3@user.com");
        userList.get(2).setName("Test User 3");
        userList.get(2).setUsername("test_user_3");
        userList.get(2).setPicture("test_user_3.picture.com");

        return userList;
    }

    private static List<UserDto> userDtoList() {

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto());
        userDtoList.add(new UserDto());
        userDtoList.add(new UserDto());

        userDtoList.get(0).setId("123");
        userDtoList.get(0).setEmail("test1@user.com");
        userDtoList.get(0).setName("Test User 1");
        userDtoList.get(0).setUsername("test_user_1");
        userDtoList.get(0).setPicture("test_user_1.picture.com");

        userDtoList.get(1).setId("456");
        userDtoList.get(1).setEmail("test2@user.com");
        userDtoList.get(1).setName("Test User 2");
        userDtoList.get(1).setUsername("test_user_2");
        userDtoList.get(1).setPicture("test_user_2.picture.com");

        userDtoList.get(2).setId("789");
        userDtoList.get(2).setEmail("test3@user.com");
        userDtoList.get(2).setName("Test User 3");
        userDtoList.get(2).setUsername("test_user_3");
        userDtoList.get(2).setPicture("test_user_3.picture.com");

        return userDtoList;
    }

    private static Page<User> userPage() {
        return new PageImpl<>(userList());
    }

    private static PageDto<UserDto> userDtoPageDto() {
        PageDto<UserDto> pageDto = new PageDto<>();

        pageDto.setContent(userDtoList());
        pageDto.setPageMetadata(new PageMetadata(userPage()));

        return pageDto;
    }

    private static com.auth0.json.mgmt.users.User auth0User() {
        com.auth0.json.mgmt.users.User auth0User = new com.auth0.json.mgmt.users.User();
        
        auth0User.setId("123");
        auth0User.setEmail("test@user.com");
        auth0User.setName("Test User");
        auth0User.setAppMetadata(Map.of("username", "test_user"));
        auth0User.setPicture("test_user.picture.com");

        return auth0User;
    }

    private static List<com.auth0.json.mgmt.users.User> auth0UserList() {

        List<com.auth0.json.mgmt.users.User> auth0UserList = new ArrayList<>();
        auth0UserList.add(new com.auth0.json.mgmt.users.User());
        auth0UserList.add(new com.auth0.json.mgmt.users.User());
        auth0UserList.add(new com.auth0.json.mgmt.users.User());

        auth0UserList.get(0).setId("123");
        auth0UserList.get(0).setEmail("test1@user.com");
        auth0UserList.get(0).setName("Test User 1");
        auth0UserList.get(0).setAppMetadata(Map.of("username", "test_user_1"));
        auth0UserList.get(0).setPicture("test_user_1.picture.com");

        auth0UserList.get(1).setId("456");
        auth0UserList.get(1).setEmail("test2@user.com");
        auth0UserList.get(1).setName("Test User 2");
        auth0UserList.get(1).setAppMetadata(Map.of("username", "test_user_2"));
        auth0UserList.get(1).setPicture("test_user_2.picture.com");

        auth0UserList.get(2).setId("789");
        auth0UserList.get(2).setEmail("test3@user.com");
        auth0UserList.get(2).setName("Test User 3");
        auth0UserList.get(2).setAppMetadata(Map.of("username", "test_user_3"));
        auth0UserList.get(2).setPicture("test_user_3.picture.com");

        return auth0UserList;
    }

}
