package com.snetsrac.issuetracker.user.dto;

import com.auth0.json.mgmt.users.User;
import com.snetsrac.issuetracker.model.Mapper;

import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    @Override
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setNickname(user.getNickname());
        dto.setUsername(user.getUsername());
        dto.setPicture(user.getPicture());

        return dto;
    }
    
}
