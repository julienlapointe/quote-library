package ui;

// Imports
import exceptions.DuplicateException;
import java.io.IOException;

import exceptions.EmptyException;
import model.Library;
import model.Quote;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.util.Scanner;
import static java.lang.System.out;

// Represents the UI for the Quote and Library classes in the "model" package
public class QuoteLibrary {

    // Fields
    Library library;
    Scanner in = new Scanner(System.in);
    private static final String JSON_FILE_LOCATION = "./data/quotes.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Constructor
    // EFFECTS: Library is initialized and the app is run
    public QuoteLibrary() throws FileNotFoundException {
        library = new Library();
        jsonWriter = new JsonWriter(JSON_FILE_LOCATION);
        jsonReader = new JsonReader(JSON_FILE_LOCATION);
        runApp();
    }

    // EFFECTS: User menu is printed, user input is captured, and corresponding method is called
    void runApp() {
        loadLibrary();

        out.println("WELCOME TO QUOTE LIBRARY");
        out.println("========================");
        out.println("PLEASE SELECT AN OPTION:");
        String[] options = {"1. View all quotes in library", "2. Add a quote", "3. Remove a quote", "4. Edit a quote",
                            "5. Exit"};
        int userInput;

        do {
            do {
                printUserMenu(options);
                userInput = in.nextInt();
                // Issue w/ Scanner.nextInt method not reading newline character when user hits "ENTER"
                // Source: https://bit.ly/3iR4p4C
                in.nextLine();
            } while (isInvalidInteger(userInput, options.length));

            if (userInput == 1) {
                out.println(printAllQuotes());
            } else if (userInput == 2) {
                addQuote();
            } else if (userInput == 3) {
                removeQuote();
            } else if (userInput == 4) {
                editQuote();
            }
        } while (userInput != 5);
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
        Quote newQuote = new Quote(phrase, author);
        try {
            // checkDuplicate() throws DuplicateException()
            checkDuplicate(newQuote);
            library.addQuote(newQuote);
            saveLibrary();
        } catch (EmptyException e) {
            out.println("Sorry! The \"Quote\" field cannot be empty.");
        } catch (DuplicateException e) {
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
        saveLibrary();
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
            String[] options = {"1. Phrase", "2. Author"};

            libraryHasQuotes();
            int userInput = selectQuoteFromMenu();
            Quote quote = library.getAllQuotes().get(userInput);

            int selectedOption;
            do {
                out.println("What would you like to edit?");
                printUserMenu(options);
                selectedOption = in.nextInt();
                in.nextLine();
            } while (isInvalidInteger(selectedOption, options.length));

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
        saveLibrary();
    }

    // ===========
    // Persistence
    // ===========

    // EFFECTS: saves the Library to file
    private void saveLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();
//            System.out.println("Saved your quotes to " + JSON_FILE_LOCATION);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_FILE_LOCATION);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads Library from file
    private void loadLibrary() {
        try {
            library = jsonReader.read();
//            System.out.println("Loaded your quotes from " + JSON_FILE_LOCATION);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_FILE_LOCATION);
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
        } while (isInvalidInteger(userInput, library.getAllQuotes().size()));
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

    // EFFECTS: newQuote phrase is checked for duplicates; if unique, then do nothing; if duplicate, then throw
    //          DuplicateException
    private void checkDuplicate(Quote newQuote) throws DuplicateException {
        for (Quote quote : library.getAllQuotes()) {
            if (quote.getPhrase().contains(newQuote.phrase)) {
                throw new DuplicateException();
            }
        }
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
        if (author.length() == 0) {
            author = "Anonymous";
        }
        return author;
    }

    // EFFECTS: all quotes in library are printed in a numbered list starting at 0
    public String printAllQuotes() {
        String quotes = "";
        if (libraryHasQuotes()) {
            quotes = quotes.concat("Here is a list of your quotes: \n");
            int index = 1;
            for (Quote quote : library.getAllQuotes()) {
                quotes = quotes.concat(index + ". " + "\"" + quote.getPhrase() + "\" ~ " +  quote.getAuthor() + "\n");
                index++;
            }
        }
        return quotes;
    }
}
