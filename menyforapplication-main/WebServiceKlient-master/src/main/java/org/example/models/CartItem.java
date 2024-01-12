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
    private int quantity;
    private Long createdBy;
    private FruitItem fruitItem;

}
