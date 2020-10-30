// QUESTIONS:
// PHASE 1
// - How to implement tagging? Separate ArrayList (but not duplicates) that
//   keeps track of tag name and array of quote IDs?
// - How to test editQuote() method? Not possible due to UI?
// - How to deal with invalid user inputs? Ex. char when expecting int
//   Use a try-catch statement?
// PHASE 2
// - How to test a try-catch that is in my UI package? See DuplicateException
// - Where do cite / credit other code? Ex. Persistence code
// - As I add try-catch statements or validation for user input, I can remove REQUIRES clauses?
// - Should "robust" code ideally have no REQUIRES clauses?

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
