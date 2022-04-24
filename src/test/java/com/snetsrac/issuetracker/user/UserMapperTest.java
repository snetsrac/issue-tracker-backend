package com.snetsrac.issuetracker.user;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.dto.UserDto;
import com.snetsrac.issuetracker.user.dto.UserMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

    private static User user;
    private static UserDto userDto;
    
    private UserMapper userMapper;

    @BeforeAll
    static void initData() {
        user = new User();
        user.setId("123");
        user.setEmail("test@user.com");
        user.setName("Test User");
        user.setAppMetadata(Map.of("username", "test_user"));
        user.setPicture("test_user.picture.com");

        userDto = new UserDto();
        userDto.setId("123");
        userDto.setEmail("test@user.com");
        userDto.setName("Test User");
        userDto.setUsername("test_user");
        userDto.setPicture("test_user.picture.com");
    }

    @BeforeEach
    void init() {
        userMapper = new UserMapper();
    }

    @Test
    void toDtoThrowsIfUserIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userMapper.toDto(null));
    }

    @Test
    void toDtoReturnsUserDto() {

        assertThat(userMapper.toDto(user)).isEqualTo(userDto);
    }

    @Test
    void toPageDtoThrowsIfPageIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userMapper.toPageDto(null));
    }
    
    @Test
    void toPageDtoReturnsPageDto() {
        UsersPage usersPage = new UsersPage(List.of(user));
        PageDto<UserDto> pageDto = new PageDto<>();

        pageDto.setContent(List.of(userDto));
        pageDto.setPageMetadata(new PageMetadata(usersPage));

        assertThat(userMapper.toPageDto(usersPage)).isEqualTo(pageDto);
    }
    
    @Test
    void toPageDtoReturnsEmptyPageDto() {
        UsersPage usersPage = new UsersPage(List.of());
        PageDto<UserDto> pageDto = new PageDto<>();

        pageDto.setContent(List.of());
        pageDto.setPageMetadata(new PageMetadata(usersPage));

        assertThat(userMapper.toPageDto(usersPage)).isEqualTo(pageDto);
    }
}
