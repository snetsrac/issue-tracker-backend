package com.snetsrac.issuetracker.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.UsersEntity;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort.Order;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private ManagementAPI managementAPI;

    @Mock
    private UsersEntity usersEntity;

    @Mock
    private Request<User> userRequest;

    @Mock
    private Request<UsersPage> usersPageRequest;

    @InjectMocks
    private UserServiceImpl userService;

    private static Collection<String> userIds;
    private static List<User> userList;

    @BeforeAll
    static void initData() {
        userIds = List.of("123", "456", "789");

        userList = List.of(new User(), new User(), new User());
        userList.get(0).setId("123");
        userList.get(0).setAppMetadata(Map.of("username", "user_0")); 
        userList.get(1).setId("456");
        userList.get(1).setAppMetadata(Map.of("username", "user_1"));
        userList.get(2).setId("789");
        userList.get(2).setAppMetadata(Map.of("username", "user_2"));
    }

    @Test
    void findAllThrowsIfOrderIsNull() throws Auth0Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userService.findAll(0, 20, null));
    }

    @Test
    void findAllReturnsEmptyPageIfRequestThrows() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withPage(0, 20)
                .withSort("user_id:1")
                .withTotals(true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenThrow(Auth0Exception.class);

        assertThat(userService.findAll(0, 20, Order.asc("user_id")).getItems()).isEqualTo(List.of());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findAllReturnsUsersPage() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withPage(0, 20)
                .withSort("user_id:1")
                .withTotals(true);
        UsersPage usersPage = new UsersPage(userList);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(usersPage);

        assertThat(userService.findAll(0, 20, Order.asc("user_id"))).isSameAs(usersPage);
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
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withQuery("user_id:123 or user_id:456 or user_id:789");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenThrow(Auth0Exception.class);

        assertThat(userService.findByIds(userIds)).isEqualTo(Set.of());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdsReturnsUserSet() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withQuery("user_id:123 or user_id:456 or user_id:789");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(userList));

        assertThat(userService.findByIds(userIds)).isEqualTo(Set.copyOf(userList));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdsReturnsEmptyUserSet() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withQuery("user_id:123 or user_id:456 or user_id:789");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(List.of()));

        assertThat(userService.findByIds(userIds)).isEqualTo(Set.of());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdsReturnsUserSetIfIdsContainsNull() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withQuery("user_id:123 or user_id:456 or user_id:789");
        List<String> userIdsWithNull = new ArrayList<>(userIds);
        userIdsWithNull.add(null);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(userList));

        assertThat(userService.findByIds(userIdsWithNull)).isEqualTo(Set.copyOf(userList));
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
                .withFields(UserServiceImpl.USER_FIELDS, true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.get(anyString(), argument.capture())).thenReturn(userRequest);
        when(userRequest.execute()).thenThrow(Auth0Exception.class);

        assertThat(userService.findById(userList.get(0).getId())).isEqualTo(Optional.empty());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdReturnsUser() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.get(anyString(), argument.capture())).thenReturn(userRequest);
        when(userRequest.execute()).thenReturn(userList.get(0));

        assertThat(userService.findById(userList.get(0).getId())).isEqualTo(Optional.of(userList.get(0)));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByIdReturnsEmptyOptional() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true);

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.get(anyString(), argument.capture())).thenReturn(userRequest);
        when(userRequest.execute()).thenReturn(null);

        assertThat(userService.findById(userList.get(0).getId())).isEqualTo(Optional.empty());
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
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withQuery("app_metadata.username:user_0");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(userList));

        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> userService.findByUsername((String) userList.get(0).getAppMetadata().get("username")));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByUsernameReturnsEmptyOptionalIfRequestThrows() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withQuery("app_metadata.username:user_0");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenThrow(Auth0Exception.class);

        assertThat(userService.findByUsername((String) userList.get(0).getAppMetadata().get("username"))).isEqualTo(Optional.empty());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByUsernameReturnsUser() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withQuery("app_metadata.username:user_0");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(userList.subList(0, 1)));

        assertThat(userService.findByUsername((String) userList.get(0).getAppMetadata().get("username"))).isEqualTo(Optional.of(userList.get(0)));
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }

    @Test
    void findByUsernameReturnsEmptyOptional() throws Auth0Exception {
        ArgumentCaptor<UserFilter> argument = ArgumentCaptor.forClass(UserFilter.class);
        UserFilter userFilter = new UserFilter()
                .withFields(UserServiceImpl.USER_FIELDS, true)
                .withQuery("app_metadata.username:user_0");

        when(managementAPI.users()).thenReturn(usersEntity);
        when(usersEntity.list(argument.capture())).thenReturn(usersPageRequest);
        when(usersPageRequest.execute()).thenReturn(new UsersPage(List.of()));

        assertThat(userService.findByUsername((String) userList.get(0).getAppMetadata().get("username"))).isEqualTo(Optional.empty());
        assertThat(argument.getValue().getAsMap()).containsExactlyInAnyOrderEntriesOf(userFilter.getAsMap());
    }
}
