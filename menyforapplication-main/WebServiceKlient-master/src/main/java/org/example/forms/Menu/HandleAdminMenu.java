package org.example.forms.Menu;

import org.apache.hc.core5.http.ParseException;
import org.example.models.LoginResponse;
import org.example.service.ClothingItemService;
import org.example.service.UserService;
import org.example.service.UtilService;

import java.io.IOException;

public class HandleAdminMenu {
    static LoginResponse currentUser;
    public static void handleAdminMenu() {
        try {
            currentUser = UserService.login();
            if (currentUser == null) {
                System.out.println("Login failed. Please try again.");
                return;
            }

            while (true) {
                printAdminMenu();
                int choice = 0;

                switch (choice) {
                    case 1:
                        ClothingItemService.addClothingItem(currentUser.getAccess_token());
                        break;
                    case 2:
                        // Implement other admin functionalities like update, delete, and view
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        currentUser = null;
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static int printAdminMenu() {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. Add New Product");
        System.out.println("2. Update/Delete Products");
        System.out.println("3. Logout");
        int choice = UtilService.getIntegerInput("\nEnter your choice: ");
        return choice;
    }
}
