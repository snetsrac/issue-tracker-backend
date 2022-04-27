package com.snetsrac.issuetracker.user;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    public Page<User> findAll(Pageable pageable);

    public Set<User> findByIds(Collection<String> ids);

    public Optional<User> findById(String id);

    public Optional<User> findByUsername(String username);
}
