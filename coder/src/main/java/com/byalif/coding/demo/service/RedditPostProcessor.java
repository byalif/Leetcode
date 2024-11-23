package com.byalif.coding.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.byalif.coding.demo.model.KeywordDictionary;
import com.byalif.coding.demo.model.PostData;
import com.byalif.coding.demo.repository.PostDataRepository;

@Service
public class RedditPostProcessor {

    @Autowired
    private RedditScraperService redditScraperService;

    @Autowired
    private ProblemMatcherService problemMatcherService;

    @Autowired
    private PostDataRepository postDataRepository;
    // In-memory map to store matched categories and related problems
    private final ConcurrentHashMap<String, List<Map<String, Object>>> matchedCategoriesData = new ConcurrentHashMap<>();

    /**
     * Fetch Reddit posts, process each post through matchCategories, and accumulate category data.
     */
    @Scheduled(cron = "0 0 0/12 * * *")
    public void processRedditPosts() {
        try {
            // Fetch Reddit posts
            Map<String, Object> redditResponse = redditScraperService.fetchTopPosts("leetcode", 30, "week");

            // Extract posts
            List<Map<String, Object>> posts = (List<Map<String, Object>>) ((Map) redditResponse.get("data")).get("children");

            // Initialize list to store matched categories for all posts
            List<Map<String, Object>> matchedResults = new ArrayList<>();

            // Process each post
            for (Map<String, Object> post : posts) {
                Map<String, Object> postData = (Map<String, Object>) post.get("data");

                // Extract title and description
                String title = (String) postData.get("title");
                String description = (String) postData.getOrDefault("selftext", "");

                // Match categories for this post
                Map<String, Object> matched = problemMatcherService.matchCategories(title, description);

                // Add metadata for the post
                matched.put("postMetadata", Map.of(
                        "title", title,
                        "description", description,
                        "url", postData.get("url"),
                        "score", postData.get("score")
                ));

                // Add matched data to results
                matchedResults.add(matched);
            }
            
            Map<String, Integer> topicScores = new HashMap<>();

            // Process each object in the JSON array
            for (Map<String, Object> post : matchedResults) {
                // Extract relevanceScores
                Map<String, Integer> relevanceScores = (Map<String, Integer>) post.get("relevanceScores");

                // Aggregate scores
                for (Map.Entry<String, Integer> entry : relevanceScores.entrySet()) {
                    topicScores.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }

            // Persist aggregated data to the database
            for (Map.Entry<String, Integer> entry : topicScores.entrySet()) {
                PostData postData = new PostData();
                postData.setTopic(entry.getKey());
                postData.setRelevanceScore(entry.getValue());
                postDataRepository.save(postData);
            }

            // Store the processed matched data in memory
            matchedCategoriesData.put("latest", matchedResults);

        } catch (Exception e) {
            throw new RuntimeException("Error processing Reddit posts for matching categories: " + e.getMessage(), e);
        }
    }
    
    @Scheduled(cron = "0 0 0 */2 * *") // Every 2 days at midnight
    public void removeOldData() {
        try {
            // Assuming PostData has a `createdAt` field of type `LocalDateTime`
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(2);
            postDataRepository.deleteByCreatedAtBefore(cutoffDate);
        } catch (Exception e) {
            throw new RuntimeException("Error removing old data: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getMatchedCategoriesDataFromDatabase() {
    	Set<String> topicz = new HashSet();
        // Fetch 20 most recent unique topics from the database
        List<PostData> recentTopics = postDataRepository.findTop20ByOrderByIdDesc();

        // Prepare response
        List<Map<String, Object>> response = new ArrayList<>();

        for (PostData topicData : recentTopics) {
            String topic = topicData.getTopic();
        	if(topicz.contains(topic)) continue;
            topicz.add(topic);
            Integer relevanceScore = topicData.getRelevanceScore();

            // Fetch related questions for this topic from the dictionary
            List<Map<String, String>> questions = KeywordDictionary.PROBLEM_LINKS.getOrDefault(topic, Collections.emptyList())
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());

            // Format the result
            Map<String, Object> topicResult = new HashMap<>();
            topicResult.put("topic", topic);
            topicResult.put("relevanceScore", relevanceScore);
            topicResult.put("questions", questions);
            topicResult.put("keywords", KeywordDictionary.CATEGORY_KEYWORDS.get(topic));

            response.add(topicResult);
        }
        // Sort the response list by relevanceScore in descending order
        response.sort((a, b) -> {
            Integer scoreA = (Integer) a.get("relevanceScore");
            Integer scoreB = (Integer) b.get("relevanceScore");
            return scoreB.compareTo(scoreA); // Descending order
        });

        return response;
    }

}
