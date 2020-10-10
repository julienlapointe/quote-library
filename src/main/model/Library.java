package model;

import java.util.HashSet;
import java.util.Set;

// Represents a library of all quotes
public class Library {
    private HashSet<Quote> quotes;

    // Constructor
    // EFFECTS: library is initialized to have no quotes
    public Library() {
        quotes = new HashSet<Quote>();
    }

    public addQuote(Quote quote) {
        quotes.add(quote);
    }

    public removeQuote(Quote quote) {
        quotes.remove(quote);
    }

    public editQuote(Quote editedQuote) {
        if (quotes.contains(editedQuote.id))
        quotes.remove(editedQuote);
    }
}
