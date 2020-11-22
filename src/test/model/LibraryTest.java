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
        checkLibraryEmptyDoesntContain(quote1);
        try {
            testLibrary.addQuote(quote1);
        } catch (EmptyException e) {
            System.out.println("Empty exception caught in LibraryTest.java!");
            fail("I was not expecting EmptyException!");
        } catch (DuplicateException e) {
            System.out.println("Duplicate exception caught in LibraryTest.java!");
            fail("I was not expecting DuplicateException!");
        }
        checkLibraryContainsOnce(quote1);
    }

    // Test adding duplicate quotes
    @Test
    public void testAddQuoteDuplicate() {
        checkLibraryEmptyDoesntContain(quote1);
        try {
            testLibrary.addQuote(quote1);
        } catch (EmptyException e) {
            System.out.println("Empty exception caught in LibraryTest.java!");
            fail("I was not expecting EmptyException!");
        } catch (DuplicateException e) {
            System.out.println("Duplicate exception caught in LibraryTest.java!");
            fail("I was not expecting DuplicateException!");
        }
        checkLibraryContainsOnce(quote1);
        try {
            testLibrary.addQuote(quote1);
            fail("I was not expecting to reach this line!");
        } catch (EmptyException e) {
            System.out.println("Empty exception caught in LibraryTest.java!");
        } catch (DuplicateException e) {
            System.out.println("Duplicate exception caught in LibraryTest.java!");
        }
        checkLibraryContainsOnce(quote1);
    }

    // Test adding empty quote
    @Test
    public void testAddQuoteEmpty() {
        checkLibraryEmptyDoesntContain(quote3);
        try {
            testLibrary.addQuote(quote3);
            fail("I was not expecting to reach this line!");
        } catch (EmptyException e) {
            System.out.println("Empty exception caught in LibraryTest.java!");
        } catch (DuplicateException e) {
            System.out.println("Duplicate exception caught in LibraryTest.java!");
        }
        checkLibraryEmptyDoesntContain(quote3);
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
            try {
                testLibrary.addQuote(newQuote);
            } catch (EmptyException e) {
                System.out.println("Empty exception caught in LibraryTest.java!");
            } catch (DuplicateException e) {
                System.out.println("Duplicate exception caught in LibraryTest.java!");
            }
            assertTrue(testLibrary.getAllQuotes().contains(newQuote));
            assertEquals(testLibrary.getAllQuotes().size(), i+1);
        }
    }

    @Test
    public void testRemoveQuote() {
        try {
            testLibrary.addQuote(quote1);
        } catch (EmptyException e) {
            System.out.println("Empty exception caught in LibraryTest.java!");
        } catch (DuplicateException e) {
            System.out.println("Duplicate exception caught in LibraryTest.java!");
        }
        checkLibraryContainsOnce(quote1);
        testLibrary.removeQuote(quote1);
        checkLibraryEmptyDoesntContain(quote1);
    }

    @Test
    public void testRemoveAllQuotes() {
        try {
            testLibrary.addQuote(quote1);
        } catch (EmptyException e) {
            System.out.println("Empty exception caught in LibraryTest.java!");
        } catch (DuplicateException e) {
            System.out.println("Duplicate exception caught in LibraryTest.java!");
        }
        checkLibraryContainsOnce(quote1);
        try {
            testLibrary.addQuote(quote2);
        } catch (EmptyException e) {
            System.out.println("Empty exception caught in LibraryTest.java!");
        } catch (DuplicateException e) {
            System.out.println("Duplicate exception caught in LibraryTest.java!");
        }
        assertEquals(testLibrary.getAllQuotes().size(), 2);
        assertTrue(testLibrary.getAllQuotes().contains(quote2));
        testLibrary.removeAllQuotes();
        checkLibraryEmptyDoesntContain(quote1);
        checkLibraryEmptyDoesntContain(quote2);
    }

    @Test
    public void testEditQuote() {
        try {
            testLibrary.addQuote(quote1);
        } catch (EmptyException e) {
            System.out.println("Empty exception caught in LibraryTest.java!");
        } catch (DuplicateException e) {
            System.out.println("Duplicate exception caught in LibraryTest.java!");
        }
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