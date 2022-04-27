package com.snetsrac.issuetracker.user;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.dto.UserDto;
import com.snetsrac.issuetracker.user.dto.UserMapper;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class UserMapperTest {

    @Test
    void withNullInput_toDto_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> UserMapper.toDto(null));
    }

    @Test
    void withValidInput_toDto_ReturnsUserDto() {
        assertThat(UserMapper.toDto(UserTestData.USER)).isEqualTo(UserTestData.USER_DTO);
    }

    @Test
    void withNullInput_toPageDto_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> UserMapper.toPageDto(null));
    }
    
    @Test
    void withValidInput_toPageDto_ReturnsPageDto() {
        Page<User> page = new PageImpl<>(UserTestData.USER_LIST);
        PageDto<UserDto> pageDto = new PageDto<>();

        pageDto.setContent(UserTestData.USER_DTO_LIST);
        pageDto.setPageMetadata(new PageMetadata(page));

        assertThat(UserMapper.toPageDto(page)).isEqualTo(pageDto);
    }
    
    @Test
    void withEmptyInput_toPageDto_ReturnsEmptyPageDto() {
        Page<User> usersPage = new PageImpl<>(List.of());
        PageDto<UserDto> pageDto = new PageDto<>();

        pageDto.setContent(List.of());
        pageDto.setPageMetadata(new PageMetadata(usersPage));

        assertThat(UserMapper.toPageDto(usersPage)).isEqualTo(pageDto);
    }
}
