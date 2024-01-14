package org.example;

import org.apache.hc.core5.http.ParseException;
import org.example.forms.Menu.Menu;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        Menu menu = new Menu();
        menu.run();

    }
}