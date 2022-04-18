package com.snetsrac.issuetracker.user.dto;

import com.auth0.json.mgmt.users.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setUsername((String) user.getAppMetadata().get("username"));
        dto.setPicture(user.getPicture());

        return dto;
    }
    
}
