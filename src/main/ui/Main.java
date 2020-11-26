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
// PHASE 3 / 4
// - Are event listener and handler the same thing in Swing?
// - What is the purpose of mirroring UI list items in the model then converting to JSON
//   when I could just grab the values from UI and convert them to JSON?

// TODO:
// - Make rest of GUI event listeners / handlers update the model (addListener is done)
// - Check that DuplicateException is NOT thrown when an item is added, removed, then re-added
// - Check that DuplicateException is thrown when duplicate quote with empty author ("Anonymous")
//   is added to list and phrase already exists in list
// - Add empty author ("Anonymous") feature to Edit dialog
// - Fix textField focus issue after a DuplicateException / EmptyException
// - Pop-up dialog with error message for DuplicateException / EmptyException
// - Add drag-and-drop functionality to list (must update the model; see "SwingJListDragAndDrop" projects)
// - Add "Quote Show" carousel visual display of quotes (random order)
// - Add tags to Quotes using HashMap (HashSet so no duplicates; key = tag (String); value = array of quoteIds;
//   in the Quotes class, tags = array of Strings
// - Filter quotes by tag
// - Search
// - Move "Add" button to bottom panel and "Save / Load" buttons to top panel with Search / Filter?
// - Refactor GUI.java to (1) have a sound() helper method and (2) put each of the five
//   event listeners / handlers into their own files
// - Comments / Credits

package ui;

import static ui.GUI.getGUI;

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
