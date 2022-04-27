package com.snetsrac.issuetracker.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

/**
 * Provides read and update functionality for users managed by Auth0.
 */
@Service
public class UserServiceAuth0Impl implements UserService {

    static final String USER_FIELDS = "user_id,email,username,name,picture,app_metadata";

    private final ManagementAPI managementAPI;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public UserServiceAuth0Impl(ManagementAPI managementAPI) {
        this.managementAPI = managementAPI;
    }

    /**
     * Returns a page of users from the Auth0 {@link ManagementAPI}. If the API call
     * throws {@link Auth0Exception}, logs the reason and returns an empty page.
     * 
     * @param pageNumber    the 0-indexed page number to return
     * @param amountPerPage the number of users per page
     * @param order         the field to sort by
     * @return a page of users
     * @throws IllegalArgumentException if order is null
     */
    @Override
    public Page<User> findAll(Pageable pageable) {

        if (pageable == null) {
            throw new IllegalArgumentException("pageable must not be null");
        }

        // Get the first sort option (UserFilter only accepts a single property to sort
        // by)
        Optional<Order> order = pageable.getSort().get().findFirst();

        // Build user filter
        UserFilter userFilter;

        if (order.isPresent()) {
            userFilter = userFilterWithFields()
                    .withPage(pageable.getPageNumber(), pageable.getPageSize())
                    .withSort(order.get().getProperty() + ":" + (order.get().isAscending() ? "1" : "-1"))
                    .withTotals(true);
        } else {
            userFilter = userFilterWithFields()
                    .withPage(pageable.getPageNumber(), pageable.getPageSize())
                    .withTotals(true);
        }

        // Build request
        Request<UsersPage> request = managementAPI.users().list(userFilter);

        // Execute the request
        try {
            return fromAuth0UsersPage(request.execute());
        } catch (Auth0Exception e) {
            log.error(e.getMessage(), e);
            return new PageImpl<>(List.of());
        }
    }

    /**
     * Returns a set of users with the given user ids from the Auth0
     * {@link ManagementAPI}. If the API call throws {@link Auth0Exception}, logs
     * the reason and returns an empty page.
     * 
     * @param ids the user ids to fetch
     * @return a set of users with the given ids
     * @throws IllegalArgumentException if ids is null
     */
    @Override
    public Set<User> findByIds(Collection<String> ids) {

        if (ids == null) {
            throw new IllegalArgumentException("ids must not be null");
        }

        // Build the user filter
        UserFilter userFilter = userFilterWithFields()
                .withFields(USER_FIELDS, true)
                .withQuery(collectionToQuery(ids));

        // Build the request
        Request<UsersPage> request = managementAPI.users().list(userFilter);

        // Execute the request
        try {
            return fromAuth0UsersPageToSet(request.execute());
        } catch (Auth0Exception e) {
            log.error(e.getMessage(), e);
            return Set.of();
        }
    }

    /**
     * Returns the user with the given id from the Auth0 {@link ManagementAPI}
     * if it exists. If the API call throws {@link Auth0Exception}, logs the reason
     * and returns an empty {@code Optional}.
     * 
     * @param id the user id to fetch
     * @return the user with the given id, or empty {@code Optional} if not found
     * @throws IllegalArgumentException if id is null or empty
     */
    @Override
    public Optional<User> findById(String id) {

        if (id == null || id.length() == 0) {
            throw new IllegalArgumentException("id must be a non-empty string");
        }

        // Build the request
        Request<com.auth0.json.mgmt.users.User> request = managementAPI.users().get(id, userFilterWithFields());

        // Execute the request
        try {
            User user = fromAuth0User(request.execute());
            return Optional.ofNullable(user);
        } catch (Auth0Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Returns the user with the given username from the Auth0 {@link ManagementAPI}
     * if it exists. If the API call throws {@link Auth0Exception}, logs the reason
     * and returns an empty {@code Optional}.
     * 
     * @param username the user username to fetch
     * @return the user with the given username, or empty {@code Optional} if not
     *         found
     * @throws IllegalArgumentException if username is null or empty
     */
    @Override
    public Optional<User> findByUsername(String username) {

        if (username == null || username.length() == 0) {
            throw new IllegalArgumentException("id must be a non-empty string");
        }

        // Build the user filter
        UserFilter userFilter = userFilterWithFields()
                .withQuery("app_metadata.username:" + username);

        // Build the request
        Request<UsersPage> request = managementAPI.users().list(userFilter);

        // Execute the request
        try {
            UsersPage usersPage = request.execute();

            // We are enforcing unique usernames as a business rule, so if this assertion
            // fails then we need to look into that
            assert (usersPage.getItems().size() <= 1);

            if (usersPage.getItems().size() == 1) {
                User user = fromAuth0User(usersPage.getItems().get(0));
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (Auth0Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private UserFilter userFilterWithFields() {
        return new UserFilter().withFields(USER_FIELDS, true);
    }

    private String collectionToQuery(Collection<String> ids) {
        return String.join(" or ",
                ids.stream().filter(id -> id != null).map(id -> "user_id:" + id).collect(Collectors.toList()));
    }

    // private com.auth0.json.mgmt.users.User toAuth0User(User user) {
    // com.auth0.json.mgmt.users.User auth0User = new
    // com.auth0.json.mgmt.users.User();

    // auth0User.setId(user.getId());
    // auth0User.setEmail(user.getEmail());
    // auth0User.setName(user.getName());
    // auth0User.getAppMetadata().put("username", user.getUsername());
    // auth0User.setPicture(user.getPicture());

    // return auth0User;
    // }

    private User fromAuth0User(com.auth0.json.mgmt.users.User auth0User) {
        if (auth0User == null) {
            return null;
        }

        User user = new User();

        user.setId(auth0User.getId());
        user.setEmail(auth0User.getEmail());
        user.setName(auth0User.getName());
        user.setUsername((String) auth0User.getAppMetadata().get("username"));
        user.setPicture(auth0User.getPicture());

        return user;
    }

    private Page<User> fromAuth0UsersPage(UsersPage usersPage) {
        if (usersPage == null) {
            return new PageImpl<>(List.of());
        }

        List<User> users = usersPage.getItems()
                .stream()
                .map(auth0User -> fromAuth0User(auth0User))
                .collect(Collectors.toList());
        return new PageImpl<>(users);
    }

    private Set<User> fromAuth0UsersPageToSet(UsersPage usersPage) {
        if (usersPage == null) {
            return Set.of();
        }

        Set<User> users = new HashSet<>();

        for (com.auth0.json.mgmt.users.User user : usersPage.getItems()) {
            users.add(fromAuth0User(user));
        }

        return users;
    }
}
