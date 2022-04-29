package com.snetsrac.issuetracker;

import java.util.Optional;

import com.snetsrac.issuetracker.user.User;
import com.snetsrac.issuetracker.user.UserService;
import com.snetsrac.issuetracker.user.dto.UserDto;
import com.snetsrac.issuetracker.user.dto.UserMapper;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles GET requests to the user endpoint, which returns the authenticated
     * {@code User} (or 401 if unauthorized).
     * 
     * @return the authenticated user
     */
    @GetMapping("/user")
    public UserDto getAuthUser(Authentication auth) {

        // Get the user from the user service
        Optional<User> user = userService.findById(auth.getName());

        // Unauthenticated users would receive a 401 before reaching this function.
        // If an authenticated user doesn't actually exist, that would be weird.
        assert user.isPresent();

        // Build a dto and return 200
        return UserMapper.toDto(user.get());
    }
    
}
