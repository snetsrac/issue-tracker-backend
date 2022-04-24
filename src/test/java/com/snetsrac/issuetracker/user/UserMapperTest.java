package com.snetsrac.issuetracker.user;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.dto.UserDto;
import com.snetsrac.issuetracker.user.dto.UserMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

    private static List<User> users;
    private static List<UserDto> userDtos;

    @BeforeAll
    static void initData() {
        users = UserData.userList();
        userDtos = UserData.userDtoList();
    }

    @Test
    void withNullInput_toDto_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> UserMapper.toDto(null));
    }

    @Test
    void withValidInput_toDto_ReturnsUserDto() {
        assertThat(UserMapper.toDto(users.get(0))).isEqualTo(userDtos.get(0));
    }

    @Test
    void withNullInput_toPageDto_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> UserMapper.toPageDto(null));
    }
    
    @Test
    void withValidInput_toPageDto_ReturnsPageDto() {
        UsersPage usersPage = new UsersPage(users);
        PageDto<UserDto> pageDto = new PageDto<>();

        pageDto.setContent(userDtos);
        pageDto.setPageMetadata(new PageMetadata(usersPage));

        assertThat(UserMapper.toPageDto(usersPage)).isEqualTo(pageDto);
    }
    
    @Test
    void withEmptyInput_toPageDto_ReturnsEmptyPageDto() {
        UsersPage usersPage = new UsersPage(List.of());
        PageDto<UserDto> pageDto = new PageDto<>();

        pageDto.setContent(List.of());
        pageDto.setPageMetadata(new PageMetadata(usersPage));

        assertThat(UserMapper.toPageDto(usersPage)).isEqualTo(pageDto);
    }
}
