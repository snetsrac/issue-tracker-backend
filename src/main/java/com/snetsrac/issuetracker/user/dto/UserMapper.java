package com.snetsrac.issuetracker.user.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;

import org.springframework.stereotype.Component;

/**
 * Provides mapping functions between Auth0 {@code User} objects and user dtos.
 */
@Component
public class UserMapper {

    /**
     * Maps an Auth0 {@link User} to a {@link UserDto}.
     * 
     * @param user the Auth0 user object
     * @return a user dto
     * @throws IllegalArgumentException if user is null
     */
    public UserDto toDto(User user) {
        
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setUsername((String) user.getAppMetadata().get("username"));
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
    public PageDto<UserDto> toPageDto(UsersPage page) {
        if (page == null) {
            throw new IllegalArgumentException("page must not be null");
        }

        PageDto<UserDto> pageDto = new PageDto<>();
        List<UserDto> userDtos = page.getItems().stream()
                .map(user -> toDto(user))
                .collect(Collectors.toList());

        pageDto.setContent(userDtos);
        pageDto.setPageMetadata(new PageMetadata(page));

        return pageDto;
    }

}
