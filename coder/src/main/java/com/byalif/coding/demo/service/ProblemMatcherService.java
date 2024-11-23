package com.byalif.coding.demo.service;

import org.springframework.stereotype.Service;

import com.byalif.coding.demo.model.KeywordDictionary;

import java.util.*;

@Service
public class ProblemMatcherService {

    /**
     * Match categories based on keywords found in the title and description.
     *
     * @param postTitle       The title of the Reddit post.
     * @param postDescription The description of the Reddit post.
     * @return Map of matched categories and their relevance scores.
     */
    public Map<String, Object> matchCategories(String postTitle, String postDescription) {
        // Normalize the title and description (lowercase, remove special characters)
        String normalizedTitle = normalizeText(postTitle);
        String normalizedDescription = normalizeText(postDescription);

        // Tokenize the title and description into words
        Set<String> titleWords = tokenize(normalizedTitle);
        Set<String> descriptionWords = tokenize(normalizedDescription);

        // Initialize relevance scores for each category
        Map<String, Integer> relevanceScores = new HashMap<>();

        // Match keywords and calculate relevance scores
        KeywordDictionary.CATEGORY_KEYWORDS.forEach((category, keywords) -> {
            int score = 0;
            for (String keyword : keywords) {
                if (titleWords.contains(keyword)) {
                    score += 3; // Higher weight for title matches
                }
                if (descriptionWords.contains(keyword)) {
                    score += 1; // Lower weight for description matches
                }
            }
            if (score > 0) {
                relevanceScores.put(category, score);
            }
        });

        // Find the top categories
        Map<String, Object> result = new HashMap<>();
        result.put("relevanceScores", relevanceScores);

        // Retrieve related problems for top categories
        List<Map<String, String>> relatedProblems = getRelatedProblems(relevanceScores);
        result.put("relatedProblems", relatedProblems);

        return result;
    }

    /**
     * Normalize text by converting to lowercase and removing special characters.
     */
    private String normalizeText(String text) {
        return text == null ? "" : text.toLowerCase().replaceAll("[^a-zA-Z\\s]", "");
    }

    /**
     * Tokenize text into a set of unique words.
     */
    private Set<String> tokenize(String text) {
        return new HashSet<>(Arrays.asList(text.split("\\s+")));
    }

    /**
     * Retrieve related problems for the top categories based on relevance scores.
     */
    private List<Map<String, String>> getRelatedProblems(Map<String, Integer> relevanceScores) {
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(relevanceScores.entrySet());
        sortedScores.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue())); // Sort by score descending

        // Collect related problems for top categories
        List<Map<String, String>> problems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedScores) {
            String category = entry.getKey();
            problems.addAll(KeywordDictionary.PROBLEM_LINKS.getOrDefault(category, Collections.emptyList()));
        }

        return problems;
    }
}
