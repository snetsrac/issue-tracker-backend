package com.snetsrac.issuetracker;

import static org.assertj.core.api.Assertions.*;

import com.auth0.client.mgmt.ManagementAPI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class IssueTrackerApplicationTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private ManagementAPI auth0ManagementApi;
    
    @Test
    void applicationContextLoads() {
        assertThat(context).isNotNull();
    }

}
