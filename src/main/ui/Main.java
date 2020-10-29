// QUESTIONS:
// - How to implement tagging? Separate ArrayList (but not duplicates) that
//   keeps track of tag name and array of quote IDs?
// - How to test editQuote() method? Not possible due to UI?
// - How to deal with invalid user inputs? Ex. char when expecting int
//   Use a try-catch statement?

package ui;

import java.io.FileNotFoundException;

// Represents an instance of the QuoteLibrary app
public class Main {
    public static void main(String[] args) {
        try {
            new QuoteLibrary();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
