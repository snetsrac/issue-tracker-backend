package com.snetsrac.issuetracker.user;

import com.auth0.client.HttpOptions;
import com.auth0.client.LoggingOptions;
import com.auth0.client.LoggingOptions.LogLevel;
import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Auth0ManagementApi {

    @Value("${AUTH0_DOMAIN:}")
    private String domain;

    @Value("${AUTH0_CLIENT_ID:}")
    private String clientId;

    @Value("${AUTH0_CLIENT_SECRET:}")
    private String clientSecret;

    @Bean
    public ManagementAPI auth0ManagementApiBean() throws Auth0Exception {
        // Enable logging
        HttpOptions options = new HttpOptions();
        options.setLoggingOptions(new LoggingOptions(LogLevel.BASIC));

        // Before creating the ManagementAPI, we need to get an access token
        AuthAPI authAPI = new AuthAPI(domain, clientId, clientSecret, options);
        AuthRequest authRequest = authAPI.requestToken("https://" + domain + "/api/v2/");
        TokenHolder holder = authRequest.execute();

        return new ManagementAPI(domain, holder.getAccessToken(), options);
    }

}
