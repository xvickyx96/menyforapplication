package org.example.forms.Menu;
import org.example.models.FruitItem;
import org.example.models.LoginResponse;
import org.example.service.CartService;
import org.example.service.FruitItemServices;
import org.example.service.UserService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import static org.example.service.UserService.adminRegister;
import static org.example.service.UserService.userRegister;


public class Menu {



        private static final Scanner scanner = new Scanner(System.in);
        private static LoginResponse currentUser;



    public void run() {
        while (true) {
            printMainMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    handleAdminMenu();
                    break;
                case 2:
                    handleUserMenu();
                    break;
                case 3:
                    handleCreateAccountMenu();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

        private static void printMainMenu() {
            System.out.println("\nMain Menu:");
            System.out.println("1. Changing cart menu (Admin)");
            System.out.println("2. Buy fruits (Admin and user)");
            System.out.println("3. Create new user");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
        }

        private static int getChoice() {
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // Ignore non-integer input
            }
            return choice;
        }

        private static void handleAdminMenu() {
            try {
                currentUser = UserService.login();
                if (currentUser == null) {
                    System.out.println("Login failed. Please try again.");
                    return;
                }

                while (true) {
                    printAdminMenu();
                    int choice = getChoice();

                    switch (choice) {
                        case 1: // Klar
                            FruitItemServices.addFruitItem(currentUser.getAccess_token());
                            break;
                        case 2:
                            // Klar
                            FruitItemServices.getAllFruitsItems(currentUser.getAccess_token());
                            FruitItemServices.updateFruitItem(currentUser.getAccess_token());
                            break;
                        case 3:
                            // Fixa med id
                            FruitItemServices.getAllFruitsItems(currentUser.getAccess_token());
                            FruitItemServices.deleteFruitItem(currentUser.getAccess_token(), 2);
                            break;
                        case 4:
                            // Fixa med id
                            FruitItemServices.getFruitItemById(currentUser.getAccess_token(), 1);
                            break;
                            case 5:
                                FruitItemServices.getAllFruitsItems(currentUser.getAccess_token());
                                break;
                            case 6:
                            System.out.println("Logging out...");
                            currentUser = null;
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } catch (IOException | org.apache.hc.core5.http.ParseException e) {
                e.printStackTrace();
            }
        }

        private static void handleUserMenu() {
            try {
                currentUser = UserService.login();
                if (currentUser == null) {
                    System.out.println("Login failed. Please try again.");
                    return;
                }

                while (true) {
                    printUserMenu();
                    int choice = getChoice();

                    switch (choice) {
                        case 1: // Bra
                            FruitItemServices.getAllFruitsItems(currentUser.getAccess_token());
                            break;
                        case 2:
                            // Fixa med ID så användaren väljer
                            FruitItemServices.getAllFruitsItems(currentUser.getAccess_token());
                            CartService.addToCart(currentUser.getAccess_token(),3,1,3);
                            break;
                        case 3:
                            // Fixa med ID, hur många quantity
                            CartService.removeCartItemById(currentUser.getAccess_token(),2);
                            break;
                        case 4: // Klar
                            CartService.clearCart(currentUser.getAccess_token());
                        case 5: // Klar
                            CartService.getAllCartItems(currentUser.getAccess_token());
                            break;
                        case 6:
                            // Klar
                            CartService.getAllCartItems(currentUser.getAccess_token());
                            System.out.println("Din betalning är gjord");
                            CartService.clearCart(currentUser.getAccess_token());
                            break;
                        case 7: // Klar
                            System.out.println("Logging out...");
                            currentUser = null;
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } catch (IOException | org.apache.hc.core5.http.ParseException e) {
                e.printStackTrace();
            }
        }

        private static void printAdminMenu() {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add New fruits");
            System.out.println("2. Update fruits");
            System.out.println("3. Delete fruits");
            System.out.println("4. Show fruits with ID");
            System.out.println("5. Show all fruits");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
        }

        private static void printUserMenu() {
            System.out.println("\nUser Menu:");
            System.out.println("1. See all fruits");
            System.out.println("2. Add to Cart");
            System.out.println("3. Delete from Cart");
            System.out.println("4. Delete all from Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Checkout");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
        }


    private void printCreateAccountMenu() {
        System.out.println("\nCreate Account Menu:");
        System.out.println("1. Create Admin Account");
        System.out.println("2. Create User Account");
        System.out.println("3. Go Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private void handleCreateAccountMenu() {
        while (true) {
            printCreateAccountMenu();
            int choice = getChoice();
            switch (choice) {
                case 1:
                    createAdminAccount();
                    break;
                case 2:
                    createUserAccount();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private void createAdminAccount() {
        try {
            adminRegister();
        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            e.printStackTrace();
        }
    }

    private void createUserAccount() {
        try {
            userRegister();
        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            e.printStackTrace();
        }
    }
}

