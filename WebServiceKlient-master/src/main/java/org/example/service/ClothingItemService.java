package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        HttpGet request = new HttpGet("http://localhost:8080/clothingitems");
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
            System.out.println(String.format("Clothing Item: %s, Cost: %f, Description: %s", item.getName(), item.getCost(), item.getDescription()));
        }

        return clothingItems;
    }

    // Implement other methods for getting one clothing item, adding, updating, and deleting

    private static ClothingItem createClothingItem() {
        ClothingItem newClothingItem = new ClothingItem();

        newClothingItem.setName(getStringInput("Enter the name of the clothing item: "));
        newClothingItem.setCost(getDoubleInput("Enter the cost of the clothing item: "));
        newClothingItem.setDescription(getStringInput("Enter the description of the clothing item: "));

        return newClothingItem;
    }

    public static void addClothingItem(String jwt) throws IOException, ParseException {
        ClothingItem newClothingItem = createClothingItem();

        HttpPost request = new HttpPost("http://localhost:8080/clothingitems");
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

        if (responseItem.getName().equals(newClothingItem.getName()) &&
                responseItem.getCost() == newClothingItem.getCost() &&
                responseItem.getDescription().equals(newClothingItem.getDescription())) {
            System.out.println("Success for new clothing item!");
        } else {
            System.out.println("Something went wrong!");
        }
    }

}
