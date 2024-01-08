package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.example.models.ClothingItem;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.example.service.UtilService.*;


public class ClothingItemService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static List<ClothingItem> getAllClothingItems(String jwt) throws IOException, ParseException {
        HttpGet request = new HttpGet("http://localhost:8080/allproducts");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred");
            return null;
        }

        HttpEntity entity = response.getEntity();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<ClothingItem> clothingItems = mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<ClothingItem>>() {});

        for (ClothingItem item : clothingItems) {
            System.out.println(String.format("Clothing Item: %s, Cost: %f, Category: %s, Color: %s, Quantity : %s ",
                    item.getProductName(),
                    item.getPrice(),
                    item.getCategory(),
                    item.getColor(),
                    item.getQuantity()
            ));
        }

        return clothingItems;
    }

    // Implement other methods for getting one clothing item, adding, updating, and deleting

    private static ClothingItem createClothingItem() {
        ClothingItem newClothingItem = new ClothingItem();

        newClothingItem.setProductName(getStringInput("Enter the name of the clothing item: "));
        newClothingItem.setPrice(getDoubleInput("Enter the cost of the clothing item: "));
        newClothingItem.setColor(getStringInput("Enter the color of the clothing item: "));
        newClothingItem.setQuantity(getIntegerInput("Enter the Quantity of the clothing item: "));
        newClothingItem.setCategory(getStringInput("Enter the category of the clothing item: "));

        return newClothingItem;
    }

    public static void addClothingItem(String jwt) throws IOException, ParseException {
        ClothingItem newClothingItem = createClothingItem();

        HttpPost request = new HttpPost("http://localhost:8080/addnewproducts");
        ObjectMapper mapper = new ObjectMapper();
        StringEntity payload = new StringEntity(mapper.writeValueAsString(newClothingItem), ContentType.APPLICATION_JSON);

        request.setEntity(payload);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred");
            return;
        }

        HttpEntity entity = response.getEntity();

        ClothingItem responseItem = mapper.readValue(EntityUtils.toString(entity), new TypeReference<ClothingItem>() {});

        if (responseItem.getProductName().equals(newClothingItem.getProductName()) &&
                responseItem.getPrice() == newClothingItem.getPrice() &&
                responseItem.getColor().equals(newClothingItem.getColor())) {
            System.out.println("Success for new clothing item!");
        } else {
            System.out.println("Something went wrong!");
        }
    }

    public static ClothingItem getClothingItemById(String jwt, int itemId) throws IOException, ParseException {
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
        ClothingItem clothingItem = mapper.readValue(EntityUtils.toString(entity), ClothingItem.class);

        System.out.println(String.format("Clothing Item: %s, Cost: %f, Category: %s, Color: %s, Quantity : %s ",
                clothingItem.getProductName(),
                clothingItem.getPrice(),
                clothingItem.getCategory(),
                clothingItem.getColor(),
                clothingItem.getQuantity()
        ));

        return clothingItem;
    }

    public static void deleteClothingItem(String jwt, int itemId) throws IOException, ParseException {
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

    public static void updateClothingItem(String jwt, int itemId, int updatedItem) throws IOException, ParseException {
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

}
