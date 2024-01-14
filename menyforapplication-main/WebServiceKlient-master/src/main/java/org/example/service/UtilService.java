package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.Scanner;

public class UtilService {

    public static String getStringInput(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt);
        String input = scan.nextLine();

        //Kontroll
        if (input.equals("")) {
            System.out.println("Felaktig input. Försök igen.");
            return getStringInput(prompt);
        }
        return input;
    }

    public static int getIntegerInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        int result;
        while (true) {
            try {
                System.out.print(prompt);
                result = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return result;
    }


    public static double getDoubleInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        double result;
        while (true) {
            try {
                System.out.print(prompt);
                result = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return result;
    }

    public static StringEntity createPayload(Object object) throws JsonProcessingException {
        //Skapa och inkludera en Payload till request
        ObjectMapper mapper = new ObjectMapper();
        StringEntity payload = new StringEntity(mapper.writeValueAsString(object), ContentType.APPLICATION_JSON);

        return payload;
    }
}
