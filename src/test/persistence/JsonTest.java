package persistence;

import model.Quote;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkQuote(String phrase, String author, Quote quote) {
        assertEquals(phrase, quote.getPhrase());
        assertEquals(author, quote.getAuthor());
    }
}
