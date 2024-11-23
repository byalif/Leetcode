package com.byalif.coding.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.byalif.coding.demo.service.RedditPostProcessor;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired
    private RedditPostProcessor redditPostProcessor;
    


    /**
     * Manually fetch and process Reddit posts.
     *
     * @return Status message.
     */
    @GetMapping("/fetch")
    public List<Map<String, Object>> fetchAndProcessPosts() {
        redditPostProcessor.processRedditPosts();
        return redditPostProcessor.getMatchedCategoriesDataFromDatabase(); // Return the processed posts as JSON
    }
    /**
     * Get the latest processed Reddit posts with matched categories.
     *
     * @return List of processed posts.
     */
    @GetMapping("/matched")
    public List<Map<String, Object>> getMatchedPosts() {
        return redditPostProcessor.getMatchedCategoriesDataFromDatabase();
    }

    


}
