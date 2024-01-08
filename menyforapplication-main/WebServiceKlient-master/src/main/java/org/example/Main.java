package org.example;

import org.apache.hc.core5.http.ParseException;
import org.example.forms.Menu.Menu;
import org.example.models.LoginResponse;

import java.io.IOException;

import static org.example.forms.Menu.HandleAdminMenu.handleAdminMenu;
import static org.example.forms.Menu.HandleUserMenu.handleUserMenu;
import static org.example.forms.Menu.PrintMainMenu.printMainMenu;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        Menu menu = new Menu();
        menu.run();

    }
}