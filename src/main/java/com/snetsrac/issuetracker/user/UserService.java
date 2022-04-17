package com.snetsrac.issuetracker.user;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;

import org.springframework.data.domain.Pageable;

public interface UserService {
    public UsersPage findAll(Pageable pageable);

    public User findById(String id);
}
