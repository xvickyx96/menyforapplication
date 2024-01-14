package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.example.models.LoginResponse;
import org.example.models.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.example.service.UtilService.createPayload;
import static org.example.service.UtilService.getStringInput;

public class UserService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private static Map<String, User> userSessions = new HashMap<>();

    public static LoginResponse adminRegister() throws IOException, ParseException {

        //Skapa ett username och password
        String firstname = getStringInput("Ange ditt förnamn:");
        String lastname = getStringInput("Ange ditt efternamn:");
        String username = getStringInput("Ange ditt användarnamn:");
        String password = getStringInput("Ange ditt lösenord:");

        //Skapa ett nytt User objekt
        User newUser = new User(0L,firstname,lastname, username, password);

        //Skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8080/api/v1/auth/adminRegister");

        //Skapa en Payload till Request
        request.setEntity(createPayload(newUser));

        //Send request
        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println("Response code = "+ response.getCode());

        if (response.getCode() != 200 ) {
            System.out.println("Något har gått fel vid registrering");
            return null;
        }

        //Hämta Payload från repsonse
        HttpEntity payload = response.getEntity();

        //Skapa User objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        User responseUser = mapper.readValue(EntityUtils.toString(payload), new TypeReference<User>() {});

        //output till User
        System.out.println("Användaren har skapats");
        return null;

    }

    public static LoginResponse userRegister() throws IOException, ParseException {

        //Skapa ett username och password
        String name = getStringInput("Ange ett nytt användarnamn:");
        String l = getStringInput("Ange ett nytt after namn:");
        String username = getStringInput("Ange ett nytt användarnamn:");
        String password = getStringInput("Ange ett nytt lösenord:");

        //Skapa ett nytt User objekt
        User newUser = new User(0L,name,l, username, password);

        //Skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8080/api/v1/auth/userRegister");

        //Skapa en Payload till Request
        request.setEntity(createPayload(newUser));

        //Send request
        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println("Response code = "+ response.getCode());

        if (response.getCode() != 200 ) {
            System.out.println("Något har gått fel vid registrering");
            return null;
        }

        //Hämta Payload från repsonse
        HttpEntity payload = response.getEntity();

        //Skapa User objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        User responseUser = mapper.readValue(EntityUtils.toString(payload), new TypeReference<User>() {});

        //output till User
        System.out.println("Användaren har skapats");
        return null;

    }

    public static LoginResponse login() throws IOException, ParseException {
        //Ange username och password
        String username = getStringInput("Ange användarnamn:");
        String password = getStringInput("Ange lösenord:");

        //Skapa ett nytt User objekt
        User loginUser = new User(0L, username, password);

        //Skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8080/api/v1/auth/authenticate");

        //Skapa en Payload till Request
        request.setEntity(createPayload(loginUser));

        //Send request
        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println("Response Code = "+ response.getCode());

        if (response.getCode() != 200 ) {
            System.out.println("Något har gått fel vid Inloggning");
            return null;
        }

        //Hämta Payload från repsonse
        HttpEntity payload = response.getEntity();

        //Skapa User objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        LoginResponse loginResponse = mapper.readValue(EntityUtils.toString(payload), new TypeReference<LoginResponse>() {});

        if (loginResponse.getAccess_token() == null) {
            System.out.println("Felaktigt användarnamn eller lösenord");
            return null;
        }

        System.out.println("Inloggad");
        System.out.println(String.format("Access token: %s", loginResponse.getAccess_token()));
        System.out.println(String.format("Refresh token: %s", loginResponse.getRefresh_token()));
        return loginResponse;
    }
}
