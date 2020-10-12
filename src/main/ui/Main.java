// QUESTIONS:
// - How to implement tagging? Separate ArrayList (but not duplicates) that
//   keeps track of tag name and array of quote IDs?
// - How to test editQuote() method? Not possible due to UI?
// - How to deal with invalid user inputs? Ex. char when expecting int

package ui;

// Represents an instance of the QuoteLibrary app
public class Main {
    public static void main(String[] args) {
        new QuoteLibrary();
    }
}
