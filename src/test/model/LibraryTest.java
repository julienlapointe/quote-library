package model;

import exceptions.DuplicateException;
import exceptions.EmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

class LibraryTest {

    private Library testLibrary;
    private Quote quote1;
    private Quote quote2;
    private Quote quote3;
    private String phrase1 = "Phrase 1";
    private String author1 = "Author 1";
    private String phrase2 = "Phrase 2";
    private String author2 = "Author 2";
    private String phrase3 = "";
    private String author3 = "Empty Quote";

    @BeforeEach
    void setup() {
        testLibrary = new Library();
        quote1 = new Quote(phrase1, author1);
        quote2 = new Quote(phrase2, author2);
        quote3 = new Quote(phrase3, author3);
    }

    // Source: IntegerSet example
    @Test
    void testAddQuote() {
        checkLibraryEmptyDoesNotContain(quote1);
        addQuoteNonExceptional(quote1);
        checkLibraryContainsOnce(quote1);
    }

    // Test adding duplicate quotes
    @Test
    public void testAddQuoteDuplicate() {
        checkLibraryEmptyDoesNotContain(quote1);
        addQuoteNonExceptional(quote1);
        checkLibraryContainsOnce(quote1);
        addQuoteExceptional(quote1);
        checkLibraryContainsOnce(quote1);
    }

    // Test adding empty quote
    @Test
    public void testAddQuoteEmpty() {
        checkLibraryEmptyDoesNotContain(quote3);
        addQuoteExceptional(quote3);
        checkLibraryEmptyDoesNotContain(quote3);
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
            addQuoteNonExceptional(newQuote);
            assertTrue(testLibrary.getAllQuotes().contains(newQuote));
            assertEquals(i+1, testLibrary.getNumberOfQuotes());
        }
    }

    @Test
    public void testRemoveQuote() {
        addQuoteNonExceptional(quote1);
        checkLibraryContainsOnce(quote1);
        testLibrary.removeQuote(quote1);
        checkLibraryEmptyDoesNotContain(quote1);
    }

    @Test
    public void testRemoveAllQuotes() {
        addQuoteNonExceptional(quote1);
        checkLibraryContainsOnce(quote1);
        addQuoteNonExceptional(quote2);
        assertEquals(2, testLibrary.getNumberOfQuotes());
        assertTrue(testLibrary.getAllQuotes().contains(quote2));
        testLibrary.removeAllQuotes();
        checkLibraryEmptyDoesNotContain(quote1);
        checkLibraryEmptyDoesNotContain(quote2);
    }

    @Test
    public void testEditQuote() {
        addQuoteNonExceptional(quote1);
        try {
            testLibrary.editQuote(quote2, 0);
        } catch (EmptyException exception) {
            System.out.println("EmptyException caught in LibraryTest.java!");
            fail("I was not expecting EmptyException!");
        } catch (DuplicateException exception) {
            System.out.println("DuplicateException caught in LibraryTest.java!");
            fail("I was not expecting DuplicateException!");
        }
        assertEquals("Phrase 2", testLibrary.getAllQuotes().get(0).getPhrase());
        assertEquals("Author 2", testLibrary.getAllQuotes().get(0).getAuthor());
    }

    // ==============
    // Helper methods
    // ==============

    private void checkLibraryEmptyDoesNotContain(Quote quote) {
        assertEquals(0, testLibrary.getNumberOfQuotes());
        assertFalse(testLibrary.getAllQuotes().contains(quote));
    }

    private void checkLibraryContainsOnce(Quote quote) {
        assertEquals(1, testLibrary.getNumberOfQuotes());
        assertTrue(testLibrary.getAllQuotes().contains(quote));
    }

    private void addQuoteNonExceptional(Quote quote) {
        try {
            testLibrary.addQuote(quote);
        } catch (EmptyException exception) {
            System.out.println("EmptyException caught in LibraryTest.java!");
            fail("I was not expecting EmptyException!");
        } catch (DuplicateException exception) {
            System.out.println("DuplicateException caught in LibraryTest.java!");
            fail("I was not expecting DuplicateException!");
        }
    }

    private void addQuoteExceptional(Quote quote) {
        try {
            testLibrary.addQuote(quote);
            fail("I was not expecting to reach this line!");
        } catch (EmptyException exception) {
            System.out.println("EmptyException in LibraryTest.java!");
        } catch (DuplicateException exception) {
            System.out.println("DuplicateException caught in LibraryTest.java!");
        }
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