package com.snetsrac.issuetracker.user;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Request;
import com.snetsrac.issuetracker.error.BadRequestException;
import com.snetsrac.issuetracker.error.InternalServerException;
import com.snetsrac.issuetracker.error.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(getClass());

    private static final String USER_FIELDS = "user_id,email,username,name,nickname,picture";

    @Autowired
    private ManagementAPI managementAPI;

    @Override
    public UsersPage findAll(Pageable pageable) {
        UserFilter filter = new UserFilter()
                .withPage(pageable.getPageNumber(), pageable.getPageSize())
                .withSort(pageableSortToAuth0SortString(pageable.getSort()))
                .withTotals(true);
        
        Request<UsersPage> request = managementAPI.users().list(filter);
        return executeRequest(request);
    }

    @Override
    public User findById(String id) {
        UserFilter filter = new UserFilter().withFields(USER_FIELDS, true);
        Request<User> request = managementAPI.users().get(id, filter);
        return executeRequest(request);
    }

    private String pageableSortToAuth0SortString(Sort sort) {
        return String.join(",", sort.map(order -> {
            String direction = order.getDirection() == Direction.DESC ? "-1" : "1";
            return order.getProperty() + ":" + direction;
        }));
    }

    private <T> T executeRequest(Request<T> request) {
        try {
            return request.execute();
        } catch (APIException e) {
            if (e.getError().equals("invalid_uri")) {
                throw new BadRequestException("user.invalid-uri");
            } else if (e.getError().equals("invalid_query_string")) {
                throw new BadRequestException("user.invalid-query-string");
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new NotFoundException("user.not-found");
            }
            
            log.error(e.getMessage(), e);
            throw new InternalServerException();
        } catch (Auth0Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerException();
        }
    }
}
