package org.example.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.models.CartItem;

import java.io.IOException;
import java.util.List;

public class CartService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void addToCart(String jwt, int productId, int customerId, int quantity) throws IOException {
        String url = String.format("http://localhost:8080/Cart/addtocart/%d/%d", productId, customerId);
        HttpPost request = new HttpPost(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        // Skapa JSON-body för begäran
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();

        // Lägg till "quantity" i JSON-body
        requestBody.put("quantity", quantity);

        // Skapa cartItem och customerlist i JSON-body
        ObjectNode cartItemNode = mapper.createObjectNode();
        cartItemNode.put("productId", productId);

        ObjectNode customerlistNode = mapper.createObjectNode();
        customerlistNode.put("userId", customerId);

        requestBody.set("cartItem", cartItemNode);
        requestBody.set("customerlist", customerlistNode);

        StringEntity entity = new StringEntity(requestBody.toString());
        request.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println("Response Code: " + response.getCode());

        if (response.getCode() == 201 || response.getCode() == 200) {
            System.out.println("Product added to cart successfully.");
        } else {
            System.out.println("Error occurred while adding the product to the cart.");
        }
    }


    public static void clearCart(String jwt) throws IOException, ParseException {
        String url = "http://localhost:8080/Cart/cart/clear";
        HttpDelete request = new HttpDelete(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println("Response Code: " + response.getCode());

        if (response.getCode() == 200) {
            System.out.println("Cart cleared successfully.");
        } else {
            System.out.println("Error occurred while clearing the cart.");
        }
    }


    public static List<CartItem> getAllCartItems(String jwt) throws IOException, ParseException {
        String url = "http://localhost:8080/Cart/cart/viewAllCart";
        HttpGet request = new HttpGet(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println("Response Code: " + response.getCode());

        if (response.getCode() != 200) {
            System.out.println("Error occurred while fetching cart items.");
            return null;
        }

        HttpEntity entity = response.getEntity();
        ObjectMapper mapper = new ObjectMapper();
        List<CartItem> cartItems = mapper.readValue(EntityUtils.toString(entity), new TypeReference<List<CartItem>>() {});

        for (CartItem cartItem : cartItems) {
            System.out.printf("Product Name: %s, Price: %f, Color: %s%n",
                    cartItem.getProductName(),
                    cartItem.getPrice(),
                    cartItem.getColor());
        }

        double totalPrice = 0.0;

        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getPrice() * cartItem.getQuantity();
        }
        System.out.println("Total Price: " + totalPrice);


        return cartItems;
    }

    public static void removeCartItemById(String jwt, int cartItemId) throws IOException, ParseException {
        String url = String.format("http://localhost:8080/Cart/cart/removeCartItem/%d", cartItemId);
        HttpDelete request = new HttpDelete(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() == 200) {
            System.out.println("Cart item removed successfully.");
        } else {
            System.out.println("Error occurred while removing the cart item.");
        }
    }



}
