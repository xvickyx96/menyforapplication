package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.models.FruitItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.example.service.UtilService.*;


public class FruitItemServices {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static List<FruitItem> getAllFruitsItems(String jwt) throws IOException, ParseException {
        HttpGet request = new HttpGet("http://localhost:8080/allproducts");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred");
            return null;
        }

        HttpEntity entity = response.getEntity();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<FruitItem> fruitItems = mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<FruitItem>>() {});

        for (FruitItem item : fruitItems) {
            System.out.println(String.format("ID: %d, Fruit Item: %s, Cost: %f, Category: %s, Color: %s, Quantity : %s ",
                    item.getProductId(),
                    item.getProductName(),
                    item.getPrice(),
                    item.getColor(),

                    item.getColor(),
                    item.getQuantity()
            ));
        }

        return fruitItems;
    }

    // Implement other methods for getting one clothing item, adding, updating, and deleting

    private static FruitItem createFruitItem() {
        FruitItem newFruitItem = new FruitItem();

        newFruitItem.setProductName(getStringInput("Enter the name of the fruit: "));
        newFruitItem.setPrice(getDoubleInput("Enter the cost of the fruit: "));
        newFruitItem.setColor(getStringInput("Enter the color of the fruit: "));
        newFruitItem.setQuantity(getIntegerInput("Enter the quantity of the fruits: "));


        return newFruitItem;
    }

    public static void addFruitItem(String jwt) throws IOException, ParseException {
        FruitItem newFruitItem = createFruitItem();

        HttpPost request = new HttpPost("http://localhost:8080/addnewproducts");
        ObjectMapper mapper = new ObjectMapper();
        StringEntity payload = new StringEntity(mapper.writeValueAsString(newFruitItem), ContentType.APPLICATION_JSON);

        request.setEntity(payload);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred");
            return;
        }

        HttpEntity entity = response.getEntity();

        FruitItem responseItem = mapper.readValue(EntityUtils.toString(entity), new TypeReference<FruitItem>() {});

        if (responseItem.getProductName().equals(newFruitItem.getProductName()) &&
                responseItem.getPrice() == newFruitItem.getPrice() &&
                responseItem.getColor().equals(newFruitItem.getColor())) {
            System.out.println("Success for fruit item!");
        } else {
            System.out.println("Something went wrong!");
        }
    }

    public static FruitItem getFruitItemById(String jwt, int itemId) throws IOException, ParseException {
        String url = String.format("http://localhost:8080/product/%d", itemId);
        HttpGet request = new HttpGet(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred");
            return null;
        }

        HttpEntity entity = response.getEntity();
        ObjectMapper mapper = new ObjectMapper();
        FruitItem clothingItem = mapper.readValue(EntityUtils.toString(entity), FruitItem.class);

        System.out.println(String.format("Clothing Item: %s, Cost: %f, Category: %s, Color: %s, Quantity : %s ",
                clothingItem.getProductName(),
                clothingItem.getPrice(),
                clothingItem.getColor(),
                clothingItem.getColor(),
                clothingItem.getQuantity()
        ));

        return clothingItem;
    }

    public static void deleteFruitItem(String jwt, int itemId) throws IOException, ParseException {
        String url = String.format("http://localhost:8080/deleteproduct/%d", itemId);
        HttpDelete request = new HttpDelete(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() == 200) {
            System.out.println("Clothing item deleted successfully.");
        } else {
            System.out.println("Error occurred while deleting the clothing item.");
        }
    }


    private static FruitItem updateFruitItem() {
        FruitItem newFruitItem = new FruitItem();

        newFruitItem.setProductName(getStringInput("Enter the name of the fruit: "));
        newFruitItem.setPrice(getDoubleInput("Enter the cost of the fruit: "));
        newFruitItem.setColor(getStringInput("Enter the color of the fruit: "));
        newFruitItem.setQuantity(getIntegerInput("Enter the quantity of the fruits: "));


        return newFruitItem;
    }
/*
    public static void updateFruitItem(String jwt, int itemId, int updatedItem) throws IOException, ParseException {
        String url = "http://localhost:8080/updateproducts";
        HttpPut request = new HttpPut(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        // Konvertera ClothingItem-objektet till JSON-sträng
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(updatedItem);
        StringEntity entity = new StringEntity(jsonBody);
        request.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() == 200) {
            System.out.println("Clothing item updated successfully.");
        } else {
            System.out.println("Error occurred while updating the clothing item.");
        }
    }
*/

    public static void updateFruitItem(String jwt) throws IOException, ParseException {

        int itemIdToUpdate = getIntegerInput("Enter the ID of the fruit to update: ");
        FruitItem existingFruitItem = getFruitItemById(jwt, itemIdToUpdate);

        if (existingFruitItem != null) {
            System.out.println("Existing Fruit Item:");
            System.out.println(existingFruitItem);

            FruitItem updatedFruitItem = updateFruitItem();

            existingFruitItem.setProductName(updatedFruitItem.getProductName());
            existingFruitItem.setPrice(updatedFruitItem.getPrice());
            existingFruitItem.setColor(updatedFruitItem.getColor());
            existingFruitItem.setQuantity(updatedFruitItem.getQuantity());


            sendUpdateRequest(jwt, existingFruitItem);
        } else {
            System.out.println("Fruit item with ID " + itemIdToUpdate + " not found.");
        }
    }

    private static void sendUpdateRequest(String jwt, FruitItem updatedFruitItem) throws IOException, ParseException {
        String url = "http://localhost:8080/updateproducts";
        HttpPut request = new HttpPut(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        // Konvertera FruitItem-objektet till JSON-sträng
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(updatedFruitItem);

        StringEntity entity = new StringEntity(jsonBody);
        request.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(request);

        System.out.println("Wrapper " + Arrays.toString(response.getHeaders()));
        System.out.println(response.getCode());

        // Skriv ut hela HTTP-svaret för att få mer information
        System.out.println(EntityUtils.toString(response.getEntity()));

        if (response.getCode() == 200) {
            System.out.println("Fruit item updated successfully.");
        } else {
            System.out.println("Error occurred while updating the fruit item.");
        }
    }



}


