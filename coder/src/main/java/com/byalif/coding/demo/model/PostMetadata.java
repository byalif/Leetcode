package com.byalif.coding.demo.model;

public class PostMetadata {

    private String title;       // Title of the post
    private String url;         // URL of the post
    private String description; // Description of the post (if available)
    private int score;          // Post score (upvotes, etc.)

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
