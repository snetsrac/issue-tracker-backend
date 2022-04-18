package com.snetsrac.issuetracker.user;

import java.util.Collection;
import java.util.Map;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;

import org.springframework.data.domain.Pageable;

public interface UserService {
    public UsersPage findAll(Pageable pageable);

    public User findById(String id);

    public Map<String, User> findByIds(Collection<String> ids);

    public User findByUsername(String username);
}
