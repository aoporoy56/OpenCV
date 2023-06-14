package com.example.OpenCV.Entity;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Search {
    private String collection_id;
    private double min_score;
    private String search_mode;
}
