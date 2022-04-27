package com.snetsrac.issuetracker.user.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.User;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Provides mapping functions between {@code User} objects and user dtos.
 */
@Component
public class UserMapper {

    /**
     * Maps a {@link User} to a {@link UserDto}.
     * 
     * @param user the user object
     * @return a user dto
     * @throws IllegalArgumentException if user is null
     */
    public static UserDto toDto(User user) {
        
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setPicture(user.getPicture());

        return dto;
    }
    
    /**
     * Maps a {@link UsersPage} to a {@link PageDto}<{@link UserDto}>.
     * 
     * @param page a page of users
     * @return a page dto
     * @throws IllegalArgumentException if page is null
     */
    public static PageDto<UserDto> toPageDto(Page<User> page) {
        if (page == null) {
            throw new IllegalArgumentException("page must not be null");
        }

        PageDto<UserDto> pageDto = new PageDto<>();
        List<UserDto> userDtos = page.getContent().stream()
                .map(user -> toDto(user))
                .collect(Collectors.toList());

        pageDto.setContent(userDtos);
        pageDto.setPageMetadata(new PageMetadata(page));

        return pageDto;
    }

}
