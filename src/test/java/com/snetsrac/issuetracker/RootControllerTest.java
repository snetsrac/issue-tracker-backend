package com.snetsrac.issuetracker;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snetsrac.issuetracker.user.UserService;
import com.snetsrac.issuetracker.user.UserTestData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = RootController.class)
public class RootControllerTest {

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
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test_user")
    void withAuth_GetAuthUser_Returns200() throws Exception {
        when(userService.findById(anyString())).thenReturn(Optional.of(UserTestData.USER));

        MvcResult mvcResult = mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService).findById("test_user");
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(
                        objectMapper.writeValueAsString(UserTestData.USER_DTO));
    }

}
