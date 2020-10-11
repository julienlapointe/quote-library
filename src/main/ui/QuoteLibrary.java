package ui;

// Imports
import model.Library;
import model.Quote;
import java.util.Scanner;

// Represents the UI for the Quote and Library classes in the "model" package
public class QuoteLibrary {
    // Fields
    Library library;
    Scanner in = new Scanner(System.in);

    // Constructor
    // EFFECTS: Library is initialized and the app is run
    public QuoteLibrary() {
        library = new Library();
        runApp();
    }

    // EFFECTS: User menu is printed, user input is captured, and corresponding method is called
    void runApp() {
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
        int userInput;

        do {
            printUserMenu(s0, s1, s2, s3, s4, s5, s6, s7);
            userInput = in.nextInt();
            // Issue w/ Scanner.nextInt method not reading newline character when user hits "ENTER"
            // Source: https://bit.ly/3iR4p4C
            in.nextLine();
            returnIfInvalidInput(userInput);

            if (userInput == 1) {
                System.out.println("ENTERED 1");
                System.out.println(library.viewAllQuotes());
            } else if (userInput == 2) {
                System.out.println("ENTERED 2");
                addQuote();
            } else if (userInput == 3) {
                System.out.println("ENTERED 3");
                removeQuote();
            } else if (userInput == 4) {
                System.out.println("ENTERED 4");
                editQuote();
            } else if (userInput == 5) {
                System.out.println("ENTERED 5");
            } else if (userInput == 6) {
                System.out.println("ENTERED 6");
            }
        } while (userInput != 7);
    }

    // EFFECTS: an array of strings is printed
    private void printUserMenu(String... menuItems) {
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }

    // EFFECTS: user input is captured for selecting a quote to edit / remove
    private int selectQuoteFromMenu() {
        int userInput;
        library.viewAllQuotes();
        System.out.println("Select a quote:");
        userInput = in.nextInt();
        in.nextLine();
        returnIfInvalidInput(userInput);
        return userInput;
    }

    // EFFECTS:
    private void returnIfInvalidInput(int userInput) {
        if (userInput < 0 || userInput >= library.getQuotes().size()) {
            return;
        }
    }

    private void returnIfNoQuotes() {
        if (this.library.getQuotes().size() == 0) {
            return;
        }
    }

//    void out(Object text) {
//        System.out.println(text);
//    }

    private void addQuote() {
        System.out.println("Enter the quote:");
        String phrase = in.nextLine();
        System.out.println("Enter the author:");
        String author = in.nextLine();
        library.addQuote(new Quote(phrase, author));
    }

    private void removeQuote() {
        returnIfNoQuotes();
        int userInput = selectQuoteFromMenu();
        library.removeQuote(library.getQuotes().get(userInput));
    }

    private void editQuote() {
        String s0 = "What would you like to edit?";
        String s1 = "1. Phrase";
        String s2 = "2. Author";

        returnIfNoQuotes();
        int userInput = selectQuoteFromMenu();
        Quote quote = library.getQuotes().get(userInput);

        printUserMenu(s0, s1, s2);
        userInput = in.nextInt();
        in.nextLine();
        System.out.println("What would you like to change it to?");
        String newText = in.nextLine();
        if (userInput == 1) {
            quote.setPhrase(newText);
        } else if (userInput == 2) {
            quote.setAuthor(newText);
        } else {
            // Invalid entry
            return;
        }
        // library.viewAllQuotes();
    }

}
