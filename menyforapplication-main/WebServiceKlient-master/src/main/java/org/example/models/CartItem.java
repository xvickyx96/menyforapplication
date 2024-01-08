package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {


    private int cartId;
    private Long productId;
    private String productName;
    private double price;
    private String color;
    private int customerId;
    private String dimension;
    private String specification;
    private String manufacturer;
    private int quantity;
    private Long createdBy;
    private String category;
    private ClothingItem clothingItem;

}
