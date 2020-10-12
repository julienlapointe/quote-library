package ui;

// Imports
import model.Library;
import model.Quote;
import java.util.Scanner;
import static java.lang.System.out;

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
        out.println("WELCOME TO QUOTE LIBRARY");
        out.println("========================");
        String s0 = "PLEASE SELECT AN OPTION:";
        String s1 = "1. View all quotes in library";
        String s2 = "2. Add a quote";
        String s3 = "3. Remove a quote";
        String s4 = "4. Edit a quote";
        String s5 = "5. Sort quotes";
        String s6 = "6. Search for a quote";
        String s7 = "7. Exit";
        String[] sArray = {s0, s1, s2, s3, s4, s5, s6, s7};
        int userInput;

        do {
            do {
                printUserMenu(s0, s1, s2, s3, s4, s5, s6, s7);
                userInput = in.nextInt();
                // Issue w/ Scanner.nextInt method not reading newline character when user hits "ENTER"
                // Source: https://bit.ly/3iR4p4C
                in.nextLine();
            } while (isInvalidInteger(userInput, sArray.length-1));

            if (userInput == 1) {
                out.println("ENTERED 1");
                out.println(printAllQuotes());
            } else if (userInput == 2) {
                out.println("ENTERED 2");
                addQuote();
            } else if (userInput == 3) {
                out.println("ENTERED 3");
                removeQuote();
            } else if (userInput == 4) {
                out.println("ENTERED 4");
                editQuote();
            } else if (userInput == 5) {
                out.println("ENTERED 5");
            } else if (userInput == 6) {
                out.println("ENTERED 6");
            }
        } while (userInput != 7);
    }

    // REQUIRES: phrase has a non-zero length
    // MODIFIES: Quote and Library
    // EFFECTS: User input is captured for phrase and author;
    //          phrase and author are used to populate a new Quote;
    //          the new Quote is added to the Library
    private void addQuote() {
        out.println("Enter the quote:");
        String phrase = in.nextLine();
        isValidString(phrase);
        out.println("Enter the author:");
        String author = in.nextLine();
        author = anonymousAuthorIfEmpty(author);
        boolean uniqueQuote = library.addQuote(new Quote(phrase, author));
        if (!uniqueQuote) {
            out.println("Sorry! That quote already exists.");
        }
    }

    // REQUIRES: 1 <= userInput <= total number of Quotes in Library
    // MODIFIES: Quote and Library
    // EFFECTS: userInput is captured;
    //          userInput is used to find Quote to delete;
    //          Quote is deleted from Library
    private void removeQuote() {
        if (libraryHasQuotes()) {
            int userInput = selectQuoteFromMenu();
            library.removeQuote(library.getAllQuotes().get(userInput));
        }
    }

    // REQUIRES: 1 <= userInput <= total number of Quotes in Library; 1 <= selectedOption <= 2;
    //           newText is a String of non-zero length
    // MODIFIES: Quote
    // EFFECTS: userInput is captured;
    //          userInput is used to find Quote to edit;
    //          selectedOption is captured;
    //          selectedOption is used to identify which field of Quote to edit (phrase or author)
    //          newText is captured;
    //          newText is used to populate the selected field of Quote (phrase or author)
    private void editQuote() {
        if (libraryHasQuotes()) {
            String s0 = "What would you like to edit?";
            String s1 = "1. Phrase";
            String s2 = "2. Author";
            String[] sArray = {s0, s1, s2};

            libraryHasQuotes();
            int userInput = selectQuoteFromMenu();
            Quote quote = library.getAllQuotes().get(userInput);

            int selectedOption;
            do {
                printUserMenu(s0, s1, s2);
                selectedOption = in.nextInt();
                in.nextLine();
            } while (isInvalidInteger(selectedOption, sArray.length-1));

            out.println("What would you like to change it to?");
            String editedText = in.nextLine();

            if (selectedOption == 1) {
                isValidString(editedText);
                quote.setPhrase(editedText);
            } else {
                editedText = anonymousAuthorIfEmpty(editedText);
                quote.setAuthor(editedText);
            }
        }
    }

    // ==============
    // Helper methods
    // ==============

    // EFFECTS: An array of strings is printed
    private void printUserMenu(String... menuItems) {
        for (String menuItem : menuItems) {
            out.println(menuItem);
        }
    }

    // EFFECTS: User input is captured for selecting a quote to edit / remove
    private int selectQuoteFromMenu() {
        int userInput;
        printAllQuotes();
        do {
            out.println("Select a quote:");
            userInput = in.nextInt();
            in.nextLine();
        } while(isInvalidInteger(userInput, library.getAllQuotes().size()));
        // Subtract 1 because array index starts at 0
        return userInput - 1;
    }

    // EFFECTS: User input is validated; if invalid, then error message is printed and true is returned;
    //          otherwise return false
    private boolean isInvalidInteger(int userInput, int max) {
        if (userInput < 1 || userInput > max) {
            out.println("Invalid entry. Please enter a # between 1 and " + max + ".");
            return true;
        }
        return false;
    }

    // EFFECTS: User input is validated; if string length is 0, then error message is printed and false is returned;
    //          otherwise return true
    private boolean isValidString(String userInput) {
        if (userInput.length() == 0) {
            out.println("Invalid entry. Please enter at least 1 character.");
            return false;
        }
        return true;
    }

    // EFFECTS: Library is checked for quotes; if none, then error message is printed and false is returned;
    //          otherwise return true
    private boolean libraryHasQuotes() {
        if (library.getAllQuotes().size() == 0) {
            out.println("You have no quotes. Try adding some quotes first.");
            return false;
        }
        return true;
    }

    // EFFECTS: If author length is 0, then value is set to "Anonymous"
    private String anonymousAuthorIfEmpty(String author) {
        out.println(author.length());
        if (author.length() == 0) {
            author = "Anonymous";
        }
        return author;
    }

    // EFFECTS: all quotes in library are printed in a numbered list starting at 0
    public String printAllQuotes() {
        String allQuotes = "";
        if (libraryHasQuotes()) {
            allQuotes = allQuotes.concat("Here is a list of your quotes:");
            int index = 1;
            for (Quote quote : library.getAllQuotes()) {
                allQuotes = allQuotes.concat("\n" + index + ". " + "\"" + quote.getPhrase() + "\" ~ " +  quote.getAuthor());
                index++;
            }
        }
        return allQuotes;
    }
}
