package persistence;

import model.Quote;
import model.Library;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testJsonReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Library library = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testJsonReaderEmptyNoQuotes() {
        JsonReader reader = new JsonReader("./data/testJsonReaderNoQuotes.json");
        try {
            Library library = reader.read();
            assertEquals(0, library.getNumberOfQuotes());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testJsonReaderGeneralSomeQuotes() {
        JsonReader reader = new JsonReader("./data/testJsonReaderSomeQuotes.json");
        try {
            Library library = reader.read();
            List<Quote> quotes = library.getAllQuotes();
            assertEquals(2, quotes.size());
            checkQuote("Quote 1", "Author 1", quotes.get(0));
            checkQuote("Quote 2", "Author 2", quotes.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}