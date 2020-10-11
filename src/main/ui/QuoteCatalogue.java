package ui;

import model.Library;

import java.util.Scanner;

public class QuoteCatalogue {
    Library library;
    Scanner scanner = new Scanner(System.in);

    public QuoteCatalogue() {
        library = new Library();
        run();
    }

    void run() {
        System.out.println("Welcome to Quoterrific");
        System.out.println("======================");

        String s0 = "Please select an option:";
        String s1 = "1. View all quotes";
        String s2 = "2. Add a quote";
        String s3 = "3. Remove a quote";
        String s4 = "4. Edit a quote";
        String s5 = "5. Sort quotes";
        String s6 = "6. Search for a quote";
        String s7 = "7. Exit";
        String userInput;
        
        do {
            viewMenu(s0, s1, s2, s3, s4, s5, s6, s7);
            userInput = scanner.nextLine();

            if (userInput.equals("1")) {
                System.out.println("ENTERED 1");
            } else if (userInput.equals("2")) {
                System.out.println("ENTERED 2");
            } else if (userInput.equals("3")) {
                System.out.println("ENTERED 3");
            } else if (userInput.equals("4")) {
                System.out.println("ENTERED 4");
            } else if (userInput.equals("5")) {
                System.out.println("ENTERED 5");
            } else if (userInput.equals("6")) {
                System.out.println("ENTERED 6");
            }

        } while (!userInput.equals("7"));
    }

    private void viewMenu(String... menuItems) {
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }

    public String
}
