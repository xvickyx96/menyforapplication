package org.example.forms.Menu;

import org.apache.hc.core5.http.ParseException;
import org.example.models.LoginResponse;
import org.example.service.CartService;
import org.example.service.UserService;
import org.example.service.UtilService;

import java.io.IOException;

public class HandleUserMenu {
    static LoginResponse currentUser;
    public static void handleUserMenu() {
        try {
            currentUser = UserService.login();
            if (currentUser == null) {
                System.out.println("Login failed. Please try again.");
                return;
            }

            while (true) {
                printUserMenu();
                int choice = 0;

                switch (choice) {
                    case 1:
                        CartService.addToCart(currentUser.getAccess_token(),1,1,1);
                        break;
                    case 2:
                        // Implement other user functionalities like delete, view, checkout, etc.
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
    private static int printUserMenu() {
        System.out.println("\nUser Menu:");
        System.out.println("1. Add to Cart");
        System.out.println("2. View Cart/Checkout");
        System.out.println("3. Logout");
        int choice = UtilService.getIntegerInput("\nEnter your choice: ");
        return choice;
    }
}
