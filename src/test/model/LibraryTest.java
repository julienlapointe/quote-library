package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private Library testLibrary;
    private Quote quote1;
    private Quote quote2;
    private String phrase1 = "Phrase 1";
    private String author1 = "Author 1";
    private String phrase2 = "Phrase 2";
    private String author2 = "Author 2";

    @BeforeEach
    void setup() {
        testLibrary = new Library();
        quote1 = new Quote(phrase1, author1);
        quote2 = new Quote(phrase2, author2);
    }

    // Source: IntegerSet example
    @Test
    void testAddQuoteNotThere() {
        checkLibraryEmptyDoesntContain(quote1);
        testLibrary.addQuote(quote1);
        checkLibraryContainsOnce(quote1);
    }

    @Test
    public void testAddQuoteAlreadyThere() {
        checkLibraryEmptyDoesntContain(quote1);
        testLibrary.addQuote(quote1);
        checkLibraryContainsOnce(quote1);
        testLibrary.addQuote(quote1);
        checkLibraryContainsOnce(quote1);
    }

    @Test
    public void testAddQuoteHighVolume() {
        String randomPhrase;
        String randomAuthor;
        Quote newQuote;
        for (int i=0; i<5000; i++) {
            randomPhrase = randomString();
            randomAuthor = randomString();
            newQuote = new Quote(randomPhrase, randomAuthor);
            testLibrary.addQuote(newQuote);
            assertTrue(testLibrary.getAllQuotes().contains(newQuote));
            assertEquals(testLibrary.getAllQuotes().size(), i+1);
        }
    }

    @Test
    public void testRemoveQuote() {
        testLibrary.addQuote(quote1);
        testLibrary.removeQuote(quote1);
        checkLibraryEmptyDoesntContain(quote1);
    }

    @Test
    public void testEditQuote() {
        testLibrary.addQuote(quote1);
        quote1.setPhrase("Test");
        quote1.setAuthor("Test");
//        testLibrary.editQuote(quote1);
        assertEquals(quote1.getPhrase(), "Test");
        assertEquals(quote1.getAuthor(), "Test");
    }

    // ==============
    // Helper methods
    // ==============

    private void checkLibraryEmptyDoesntContain(Quote quote) {
        assertEquals(testLibrary.getAllQuotes().size(), 0);
        assertFalse(testLibrary.getAllQuotes().contains(quote));
    }

    private void checkLibraryContainsOnce(Quote quote) {
        assertEquals(testLibrary.getAllQuotes().size(), 1);
        assertTrue(testLibrary.getAllQuotes().contains(quote));
    }

    private String randomString() {
        // String of all possible characters
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        // Create a StringBuilder object
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int length = 10;
        for (int i = 0; i < length; i++) {
            // Generate random index number
            int index = random.nextInt(alphabet.length());
            // Get character specified by index from the string
            char randomChar = alphabet.charAt(index);
            // append the character to string builder
            stringBuilder.append(randomChar);
        }
        String randomString = stringBuilder.toString();
        return randomString;
    }

}