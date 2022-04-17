package com.snetsrac.issuetracker.user;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.snetsrac.issuetracker.error.NotFoundException;
import com.snetsrac.issuetracker.model.PagedDto;
import com.snetsrac.issuetracker.user.dto.UserDto;
import com.snetsrac.issuetracker.user.dto.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    // Aggregate root
    @GetMapping("")
    @PreAuthorize("hasAuthority('read:users')")
    public PagedDto<User, UserDto> getUsers(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        UsersPage page = userService.findAll(pageable);
        PagedDto<User, UserDto> dto = PagedDto.from(page, userMapper);
        return dto;
    }

    // Single item
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read:users')")
    public UserDto getUserById(@PathVariable String id) {
        User user = userService.findById(id);
        
        if (user == null) {
            throw new NotFoundException("user.not-found");
        }

        return userMapper.toDto(user);
    }

}