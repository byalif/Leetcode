package com.byalif.coding.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RedditScraperService {

    private final ObjectMapper objectMapper; // Jackson ObjectMapper for JSON handling
    private final RedditAuthService redditAuthService;

    @Value("${reddit.api.url}")
    private String redditApiUrl;

    public RedditScraperService(RedditAuthService redditAuthService, ObjectMapper objectMapper) {
        this.redditAuthService = redditAuthService;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch top posts from a subreddit and return the raw data as JSON string.
     *
     * @param subreddit Subreddit name (e.g., "leetcode").
     * @param limit     Number of posts to fetch.
     * @param timeFrame Timeframe for top posts (e.g., "hour", "day").
     * @return JSON string containing Reddit data.
     */
    public String fetchTopPostsAsJson(String subreddit, int limit, String timeFrame) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + redditAuthService.getAccessToken());
        headers.set("User-Agent", "RedditScraper/1.0");

        // Construct API URL
        String url = String.format("%s/r/%s/top?t=%s&limit=%d", redditApiUrl, subreddit, timeFrame, limit);

        try {
            // Make the API request
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            // Convert the response body to a JSON string using ObjectMapper
            return objectMapper.writeValueAsString(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch and process Reddit data: " + e.getMessage(), e);
        }
    }

    /**
     * Fetch top posts from a subreddit and return as a Java Map object.
     *
     * @param subreddit Subreddit name (e.g., "leetcode").
     * @param limit     Number of posts to fetch.
     * @param timeFrame Timeframe for top posts (e.g., "hour", "day").
     * @return Map containing Reddit data.
     */
    public Map<String, Object> fetchTopPosts(String subreddit, int limit, String timeFrame) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + redditAuthService.getAccessToken());
        headers.set("User-Agent", "RedditScraper/1.0");

        // Construct API URL
        String url = String.format("%s/r/%s/top?t=%s&limit=%d", redditApiUrl, subreddit, timeFrame, limit);

        try {
            // Make the API request
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            // Return the response body directly as a Map
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch and process Reddit data: " + e.getMessage(), e);
        }
    }
}
