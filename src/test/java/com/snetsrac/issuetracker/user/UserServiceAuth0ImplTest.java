package com.snetsrac.issuetracker.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.UsersEntity;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@ExtendWith(MockitoExtension.class)
public class UserServiceAuth0ImplTest {

    @Mock
    private ManagementAPI managementAPI;

    @Mock
    private UsersEntity usersEntity;

    @Mock
    private Request<User> userRequest;

    @Mock
    private Request<UsersPage> usersPageRequest;

    @InjectMocks
    private UserServiceAuth0Impl userService;

    @Test
    void findAllThrowsIfOrderIsNull() throws Auth0Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userService.findAll(null));
    }

    @Test
    void findAllReturnsEmptyPageIfRequestThrows() throws Auth0Exception {
        Pageable pageable = PageRequest.of(3, 18, Sort.by(Order.desc("user_id")));
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withPage(3, 18)
                .withSort("user_id:-1")
                .withTotals(true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenThrow(Auth0Exception.class);

        assertThat(userService.findAll(pageable).getContent()).isEqualTo(List.of());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findAllReturnsUsersPage() throws Auth0Exception {
        Pageable pageable = PageRequest.of(3, 18, Sort.by(Order.desc("user_id")));
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withPage(3, 18)
                .withSort("user_id:-1")
                .withTotals(true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(UserTestData.AUTH0_USER_LIST));

        assertThat(userService.findAll(pageable)).isEqualTo(new PageImpl<>(UserTestData.USER_LIST));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdsThrowsIfIdsIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userService.findByIds(null));
    }

    @Test
    void findByIdsReturnsEmptySetIfRequestThrows() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withQuery("user_id:123 or user_id:456 or user_id:789");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenThrow(Auth0Exception.class);

        assertThat(userService.findByIds(UserTestData.USER_IDS)).isEqualTo(Set.of());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdsReturnsUserSet() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withQuery("user_id:123 or user_id:456 or user_id:789");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(UserTestData.AUTH0_USER_LIST));

        assertThat(userService.findByIds(UserTestData.USER_IDS)).isEqualTo(Set.copyOf(UserTestData.USER_LIST));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdsReturnsEmptyUserSet() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withQuery("user_id:123 or user_id:456 or user_id:789");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(List.of()));

        assertThat(userService.findByIds(UserTestData.USER_IDS)).isEqualTo(Set.of());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdsReturnsUserSetIfIdsContainsNull() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withQuery("user_id:123 or user_id:456 or user_id:789");
        List<String> userIdsWithNull = new ArrayList<>(UserTestData.USER_IDS);
        userIdsWithNull.add(null);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(UserTestData.AUTH0_USER_LIST));

        assertThat(userService.findByIds(userIdsWithNull)).isEqualTo(Set.copyOf(UserTestData.USER_LIST));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdThrowsIfIdIsNullOrEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userService.findById(null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userService.findById(""));
    }

    @Test
    void findByIdReturnsEmptyOptionalIfRequestThrows() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.get(anyString(), argument.capture())).thenReturn(userRequest);
        when(userRequest.execute()).thenThrow(Auth0Exception.class);

        assertThat(userService.findById(UserTestData.USER.getId())).isEqualTo(Optional.empty());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdReturnsUser() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.get(anyString(), argument.capture())).thenReturn(userRequest);
        when(userRequest.execute()).thenReturn(UserTestData.AUTH0_USER);

        assertThat(userService.findById(UserTestData.USER.getId())).isEqualTo(Optional.of(UserTestData.USER));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdReturnsEmptyOptional() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.get(anyString(), argument.capture())).thenReturn(userRequest);
        when(userRequest.execute()).thenReturn(null);

        assertThat(userService.findById(UserTestData.USER.getId())).isEqualTo(Optional.empty());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByUsernameThrowsIfIdIsNullOrEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userService.findByUsername(null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userService.findByUsername(""));
    }

    @Test
    void findByUsernameThrowsIfMoreThanOneUserReturned() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withQuery("app_metadata.username:" + UserTestData.USER.getUsername());

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(UserTestData.AUTH0_USER_LIST));

        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> userService.findByUsername(UserTestData.USER.getUsername()));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByUsernameReturnsEmptyOptionalIfRequestThrows() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withQuery("app_metadata.username:" + UserTestData.USER.getUsername());

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenThrow(Auth0Exception.class);

        assertThat(userService.findByUsername(UserTestData.USER.getUsername())).isEqualTo(Optional.empty());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByUsernameReturnsUser() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withQuery("app_metadata.username:" + UserTestData.USER.getUsername());

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(List.of(UserTestData.AUTH0_USER)));

        assertThat(userService.findByUsername(UserTestData.USER.getUsername())).isEqualTo(Optional.of(UserTestData.USER));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByUsernameReturnsEmptyOptional() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceAuth0Impl.USER_FIELDS, true)
                .withQuery("app_metadata.username:" + UserTestData.USER.getUsername());

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(List.of()));

        assertThat(userService.findByUsername(UserTestData.USER.getUsername())).isEqualTo(Optional.empty());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }
}
