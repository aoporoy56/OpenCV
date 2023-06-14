package com.example.OpenCV.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class Images {
    String[] gallery;
    String[] probe;
    String search_mode;
}
