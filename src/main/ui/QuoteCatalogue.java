package ui;

import model.Library;
import model.Quote;

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
        String s1 = "1. View all quotes in library";
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
                System.out.println(library.viewAllQuotes());
            } else if (userInput.equals("2")) {
                System.out.println("ENTERED 2");
                addQuote();
            } else if (userInput.equals("3")) {
                System.out.println("ENTERED 3");
                removeQuote();
            } else if (userInput.equals("4")) {
                System.out.println("ENTERED 4");
                editQuote();
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

    private int selectQuote() {
        int userInput;
        library.viewAllQuotes();
        System.out.println("Select a quote:");
        userInput = scanner.nextInt();
        if (userInput < 0 || userInput >= library.getQuotes().size()) {
            return 0;
        }
        return userInput;
    }

    private void addQuote() {
        System.out.println("Enter the quote:");
        String phrase = scanner.nextLine();
        System.out.println("Enter the author:");
        String author = scanner.nextLine();
        library.addQuote(new Quote(phrase, author));
    }

    private void returnIfNoQuotes() {
        if (this.library.getQuotes().size() == 0) {
            return;
        }
    }

    private void removeQuote() {
        returnIfNoQuotes();
        int userInput = selectQuote();
        library.removeQuote(library.getQuotes().remove(userInput));
    }

    private void editQuote() {
        String s0 = "What would you like to edit?";
        String s1 = "1. Phrase";
        String s2 = "2. Author";

        returnIfNoQuotes();
        int userInput = selectQuote();
        Quote quote = library.getQuotes().get(userInput);

        viewMenu(s0, s1, s2);
        userInput = scanner.nextInt();
        scanner.nextLine();
        System.out.println("What would you like to change it to?");
        String s = scanner.nextLine();
        if (userInput == 1) {
            quote.setPhrase(s);
        } else if (userInput == 2) {
            quote.setAuthor(s);
        } else {
            // Invalid entry
            return;
        }
        // library.viewAllQuotes();
    }

}
