package com.snetsrac.issuetracker.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // GET /user

    @Test
    void withoutAuth_GetAuthUser_Returns401() throws Exception {
        mockMvc.perform(get(UserController.ROOT + "/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test_user")
    void withAuth_GetAuthUser_Returns200() throws Exception {
        when(userService.findById(anyString())).thenReturn(Optional.of(UserTestData.USER));

        MvcResult mvcResult = mockMvc.perform(get(UserController.ROOT + "/user"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService).findById("test_user");
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(
                        objectMapper.writeValueAsString(UserTestData.USER_DTO));
    }

    // GET /users

    @Test
    void withoutAuth_GetUsers_Returns401() throws Exception {
        mockMvc.perform(get(UserController.ROOT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void withoutRole_GetUsers_Returns403() throws Exception {
        mockMvc.perform(get(UserController.ROOT))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "read:users")
    void withInvalidInput_GetUsers_Returns200() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("aoeuiao"));
        when(userService.findByIds(anyCollection())).thenReturn(Set.copyOf(UserTestData.USER_LIST));
        when(userService.findAll(any(Pageable.class))).thenReturn(UserTestData.USER_PAGE);

        MvcResult mvcResult = mockMvc.perform(get(UserController.ROOT)
                .param("page", "three")
                .param("size", "eighteen")
                .param("sort", "aoeuiao"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService).findAll(pageable);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(
                        objectMapper.writeValueAsString(UserTestData.USER_DTO_PAGE_DTO));
    }

    @Test
    @WithMockUser(authorities = "read:users")
    void withValidInput_GetUsers_Returns200() throws Exception {
        Pageable pageable = PageRequest.of(3, 18, Sort.by(Order.desc("id")));
        when(userService.findAll(any(Pageable.class))).thenReturn(UserTestData.USER_PAGE);

        MvcResult mvcResult = mockMvc.perform(get(UserController.ROOT)
                .param("page", "3")
                .param("size", "18")
                .param("sort", "id,desc"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService).findAll(pageable);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(
                        objectMapper.writeValueAsString(UserTestData.USER_DTO_PAGE_DTO));
    }

    // GET /users/:username

    @Test
    void withoutAuth_GetUserByUsername_Returns401() throws Exception {
        mockMvc.perform(get(UserController.ROOT + "/" + UserTestData.USER.getUsername()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void withoutRole_GetUserByUsername_Returns403() throws Exception {
        mockMvc.perform(get(UserController.ROOT + "/" + UserTestData.USER.getUsername()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "read:users")
    void withNonexistentUser_GetUserByUsername_Returns404() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get(UserController.ROOT + "/" + UserTestData.USER.getUsername()))
                .andExpect(status().isNotFound());

        verify(userService).findByUsername(UserTestData.USER.getUsername());
    }

    @Test
    @WithMockUser(authorities = "read:users")
    void withValidInput_GetUserByUsername_Returns200() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(UserTestData.USER));

        MvcResult mvcResult = mockMvc.perform(get(UserController.ROOT + "/" + UserTestData.USER.getUsername()))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService).findByUsername(UserTestData.USER.getUsername());
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(
                        objectMapper.writeValueAsString(UserTestData.USER_DTO));
    }

}
