// QUESTIONS:
// PHASE 1
// - How to implement tagging? Separate ArrayList (but not duplicates) that
//   keeps track of tag name and array of quote IDs?
// - How to test editQuote() method? Not possible due to UI?
// - How to deal with invalid user inputs? Ex. char when expecting int
//   Use a try-catch statement?
// PHASE 2
// - How to test a try-catch that is in my UI package? See DuplicateException
// - As I add try-catch statements or validation for user input, I can remove REQUIRES clauses?
// - Should "robust" code ideally have no REQUIRES clauses?
// - Where do cite / credit other code? Ex. Persistence code
// - Are we allowed to use a Swing library for UI?
// PHASE 3
// -

package ui;

import static ui.Frame.getGUI;

// Represents an instance of the QuoteLibrary app
public class Main {
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getGUI();
            }
        });
    }
}
