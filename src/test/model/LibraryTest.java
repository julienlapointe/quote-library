package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        quote1 = new Quote(phrase2, author2);
    }

    @Test
    void testAddQuoteNotThere() {

    }

}