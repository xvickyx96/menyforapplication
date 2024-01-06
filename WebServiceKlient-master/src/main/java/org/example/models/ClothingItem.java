package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClothingItem {
    // Primary key for the clothing item entity

    private Integer id;

    // Name of the clothing item
    private String name;

    // Cost of the clothing item
    private double cost;

    // Description of the clothing item
    private String description;

    // User ID associated with the clothing item
}
