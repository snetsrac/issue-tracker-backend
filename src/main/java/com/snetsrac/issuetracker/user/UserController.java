package com.snetsrac.issuetracker.user;

import java.util.Optional;

import com.snetsrac.issuetracker.error.NotFoundException;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.user.dto.UserDto;
import com.snetsrac.issuetracker.user.dto.UserMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.ROOT)
public class UserController {

    public final static String ROOT = "/users";

    private final UserService userService;

    public UserController(UserService userService) {
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

    /**
     * Handles GET requests to the users endpoint.
     * 
     * @param pageable pagination options
     * @returns a page of users
     */
    @GetMapping("")
    @PreAuthorize("hasAuthority('read:users')")
    public PageDto<UserDto> getUsers(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        
        // Get a page of users from the user service
        Page<User> page = userService.findAll(pageable);
        
        // Build a page dto and return 200;
        return UserMapper.toPageDto(page);
    }

    /**
     * Handles GET requests to the users/:username endpoint.
     * 
     * @param id the user id of the user to retrieve
     * @return the user
     * @throws NotFoundException if the issue doesn't exist
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('read:users')")
    public UserDto getUserByUsername(@PathVariable String username) {

        // Get the user from the user service
        Optional<User> user = userService.findByUsername(username);

        // If no user was found, return 404
        if (!user.isPresent()) {
            throw new NotFoundException("user.not-found");
        }

        // Build a dto and return 200
        return UserMapper.toDto(user.get());
    }

}
