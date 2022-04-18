package com.snetsrac.issuetracker.user;

import java.util.stream.Collectors;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.snetsrac.issuetracker.error.NotFoundException;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
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
    public PageDto<UserDto> getUsers(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        UsersPage page = userService.findAll(pageable);
        PageDto<UserDto> pageDto = new PageDto<>();
        
        pageDto.setContent(page.getItems().stream().map(userMapper::toDto).collect(Collectors.toList()));
        pageDto.setPageMetadata(new PageMetadata(page));

        return pageDto;
    }

    // Single item
    
    @GetMapping("/byId/{id}")
    @PreAuthorize("hasAuthority('read:users')")
    public UserDto getUserById(@PathVariable String id) {
        User user = userService.findById(id);

        if (user == null) {
            throw new NotFoundException("user.not-found");
        }

        return userMapper.toDto(user);
    }
    
    @GetMapping("/byUsername/{username}")
    @PreAuthorize("hasAuthority('read:users')")
    public UserDto getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new NotFoundException("user.not-found");
        }

        return userMapper.toDto(user);
    }

}
