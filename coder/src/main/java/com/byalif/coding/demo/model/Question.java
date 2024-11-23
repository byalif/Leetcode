package com.byalif.coding.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String company;
    private String topic;
    private String sourceUrl;
    private String difficulty;
    private LocalDateTime timestamp;

    // Getters, setters, constructors (use Lombok @Data if added)
}
