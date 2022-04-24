package com.snetsrac.issuetracker.user;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;

import org.springframework.data.domain.Sort.Order;

public interface UserService {
    public UsersPage findAll(int pageNumber, int amountPerPage, Order order);

    public Set<User> findByIds(Collection<String> ids);

    public Optional<User> findById(String id);

    public Optional<User> findByUsername(String username);
}
