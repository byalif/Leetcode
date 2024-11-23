package com.byalif.coding.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RedditAuthService {

    @Value("${reddit.client.id}")
    private String clientId;

    @Value("${reddit.client.secret}")
    private String clientSecret;

    @Value("${reddit.username}")
    private String username;

    @Value("${reddit.password}")
    private String password;

    @Value("${reddit.token.url}")
    private String tokenUrl;

    private String accessToken;

    /**
     * Authenticate with Reddit API and fetch access token.
     *
     * @return Access token for Reddit API.
     */
    public String authenticate() {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret); // Basic Auth with client ID and secret
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Prepare body
        String body = "grant_type=password&username=" + username + "&password=" + password;

        // Make the request
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

        // Extract access token
        accessToken = (String) response.getBody().get("access_token");
        return accessToken;
    }

    /**
     * Get the current access token, or reauthenticate if necessary.
     *
     * @return Access token for Reddit API.
     */
    public String getAccessToken() {
        if (accessToken == null) {
            authenticate();
        }
        return accessToken;
    }
}
