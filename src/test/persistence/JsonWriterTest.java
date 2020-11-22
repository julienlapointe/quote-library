package persistence;

import exceptions.DuplicateException;
import exceptions.EmptyException;
import model.Quote;
import model.Library;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testJsonWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testJsonWriterNoQuotes() {
        try {
            Library library = new Library();
            JsonWriter writer = new JsonWriter("./data/testJsonWriterNoQuotes.json");
            writer.open();
            writer.write(library);
            writer.close();

            JsonReader reader = new JsonReader("./data/testJsonWriterNoQuotes.json");
            library = reader.read();
            assertEquals(0, library.numberOfQuotes());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testJsonWriterSomeQuotes() {
        try {
            Library library = new Library();
            try {
                library.addQuote(new Quote("Quote 1", "Author 1"));
                library.addQuote(new Quote("Quote 2", "Author 2"));
            } catch (EmptyException e) {
                System.out.println("Empty exception caught in JsonWriterTest.java!");
            } catch (DuplicateException e) {
                System.out.println("Duplicate exception caught in JsonWriterTest.java!");
            }
            JsonWriter writer = new JsonWriter("./data/testJsonWriterSomeQuotes.json");
            writer.open();
            writer.write(library);
            writer.close();

            JsonReader reader = new JsonReader("./data/testJsonWriterSomeQuotes.json");
            library = reader.read();
            List<Quote> quotes = library.getAllQuotes();
            assertEquals(2, quotes.size());
            checkQuote("Quote 1", "Author 1", quotes.get(0));
            checkQuote("Quote 2", "Author 2", quotes.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}