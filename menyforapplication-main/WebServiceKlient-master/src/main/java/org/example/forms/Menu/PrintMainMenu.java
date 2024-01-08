package org.example.forms.Menu;

import org.example.service.UtilService;

public class PrintMainMenu {
    public static int printMainMenu(int choice) {
        System.out.println("\nMain Menu:");
        System.out.println("1. Admin");
        System.out.println("2. User");
        System.out.println("3. Exit");
        choice = UtilService.getIntegerInput("\nEnter your choice: ");
        return choice;
    }
}
