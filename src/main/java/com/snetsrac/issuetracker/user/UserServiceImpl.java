package com.snetsrac.issuetracker.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(getClass());

    private static final String USER_FIELDS = "user_id,email,username,name,picture,app_metadata";

    @Autowired
    private ManagementAPI managementAPI;

    @Override
    public UsersPage findAll(Pageable pageable) {
        Request<UsersPage> request = managementAPI.users().list(userFilter(pageable));
        return executeRequest(request);
    }

    @Override
    public User findById(String id) {
        Request<User> request = managementAPI.users().get(id, userFilter());
        return executeRequest(request);
    }

    @Override
    public User findByUsername(String username) {
        Request<UsersPage> request = managementAPI.users().list(userFilter(username));
        UsersPage usersPage = executeRequest(request);
        
        if (usersPage != null) {
            return usersPage.getItems().get(0);
        }

        return null;
    }

    private String pageableSortToAuth0SortString(Sort sort) {
        return String.join(",", sort.map(order -> {
            String direction = order.getDirection() == Direction.DESC ? "-1" : "1";
            return order.getProperty() + ":" + direction;
        }));
    }

    private UserFilter userFilter() {
        return new UserFilter().withFields(USER_FIELDS, true);
    }

    private UserFilter userFilter(Pageable pageable) {
        return new UserFilter()
                .withFields(USER_FIELDS, true)
                .withPage(pageable.getPageNumber(), pageable.getPageSize())
                .withSort(pageableSortToAuth0SortString(pageable.getSort()))
                .withTotals(true);
    }

    private UserFilter userFilter(Collection<String> ids) {
        String query = String.join(" or ", ids.stream().map(id -> "user_id:" + id).collect(Collectors.toList()));

        return new UserFilter()
                .withFields(USER_FIELDS, true)
                .withQuery(query);
    }

    private UserFilter userFilter(String username) {
        String query = "app_metadata.username:" + username;

        return new UserFilter()
                .withFields(USER_FIELDS, true)
                .withQuery(query);
    }

    private <T> T executeRequest(Request<T> request) {
            try {
                return request.execute();
            } catch (Auth0Exception e) {
                log.info(e.getMessage(), e);
                return null;
            }
    }
}
