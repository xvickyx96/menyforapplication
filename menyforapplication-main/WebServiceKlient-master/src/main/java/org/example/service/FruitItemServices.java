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
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.models.FruitItem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
            System.out.println(String.format("ID: %d, Fruit Item: %s, Cost: %f, Color: %s, Quantity : %s ",
                    item.getProductId(),
                    item.getProductName(),
                    item.getPrice(),
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

        System.out.println(String.format("Clothing Item: %s, Cost: %f, Color: %s, Quantity : %s ",
                clothingItem.getProductName(),
                clothingItem.getPrice(),
                clothingItem.getColor(),
                clothingItem.getQuantity()
        ));

        return clothingItem;
    }
    public static void deleteFruitItem(String jwt) throws IOException, ParseException {
        int itemIdToDelete = getIntegerInput("Enter the ID of the fruit to delete: ");
        FruitItem existingFruitItem = getFruitItemById(jwt, itemIdToDelete);

        if (existingFruitItem != null) {
            System.out.println("Existing Fruit Item:");
            System.out.println(existingFruitItem);

            sendDeleteRequest(jwt, itemIdToDelete);
        } else {
            System.out.println("Fruit item with ID " + itemIdToDelete + " not found.");
        }
    }

    private static void sendDeleteRequest(String jwt, int itemId) throws IOException {
        String url = String.format("http://localhost:8080/deleteproduct/%d", itemId);
        HttpDelete request = new HttpDelete(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() == 200) {
            System.out.println("Fruit item deleted successfully.");
        } else {
            System.out.println("Error occurred while deleting the fruit item.");
        }
    }



    private static FruitItem updateFruitItem(FruitItem existingFruitItem) {
        // Use the existing fruit item and modify its properties
        existingFruitItem.setProductName(getStringInput("Enter the name of the fruit: "));
        existingFruitItem.setPrice(getDoubleInput("Enter the cost of the fruit: "));
        existingFruitItem.setColor(getStringInput("Enter the color of the fruit: "));
        existingFruitItem.setQuantity(getIntegerInput("Enter the quantity of the fruits: "));

        return existingFruitItem;
    }

    public static void updateFruitItem(String jwt) throws IOException, ParseException {
        int itemIdToUpdate = getIntegerInput("Enter the ID of the fruit to update: ");
        FruitItem existingFruitItem = getFruitItemById(jwt, itemIdToUpdate);

        if (existingFruitItem != null) {
            System.out.println("Existing Fruit Item:");
            System.out.println(existingFruitItem);

            // Update the existing fruit item with new values
            FruitItem updatedFruitItem = updateFruitItem(existingFruitItem);

            // Call the method to send the update request
            sendUpdateRequest(jwt, updatedFruitItem);
        } else {
            System.out.println("Fruit item with ID " + itemIdToUpdate + " not found.");
        }
    }

    private static void sendUpdateRequest(String jwt, FruitItem updatedFruitItem) throws IOException, ParseException {
        String url = "http://localhost:8080/updateproducts";
        HttpPut request = new HttpPut(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        // Convert FruitItem object to JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(updatedFruitItem);

        // Use StringEntity with UTF-8 encoding
        StringEntity entity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
        request.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(request);

        System.out.println("Wrapper " + Arrays.toString(response.getHeaders()));
        System.out.println(response.getCode());

        // Print the entire HTTP response for more information
        System.out.println(EntityUtils.toString(response.getEntity()));

        if (response.getCode() == 200) {
            System.out.println("Fruit item updated successfully.");
        } else {
            System.out.println("Error occurred while updating the fruit item.");
        }
    }




}


