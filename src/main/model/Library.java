package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// Represents a library of all quotes
public class Library {
    private ArrayList<Quote> quotes;

    // Constructor
    // EFFECTS: Library is initialized to be an empty ArrayList of Quotes
    public Library() {
        quotes = new ArrayList<Quote>();
    }

    // REQUIRES: quote contains a phrase String of non-zero length
    // MODIFIES: this
    // EFFECTS: newQuote is validated as unique, then added to Library of Quotes
    public boolean addQuote(Quote newQuote) {
        for (Quote quote : quotes) {
            if (quote.getPhrase().contains(newQuote.phrase)) {
                return false;
            }
        }
        quotes.add(newQuote);
        return true;
//        if (getAllQuotes().contains(newQuote.phrase)) {
//            return false;
//        }
//        quotes.add(newQuote);
//        return true;
    }

    // REQUIRES: quote contains a phrase String of non-zero length
    // MODIFIES: this
    // EFFECTS: New quote is removed from Library of Quotes
    public void removeQuote(Quote quote) {
        quotes.remove(quote);
    }

    // REQUIRES: quote contains a phrase String of non-zero length
    // MODIFIES: this
    // EFFECTS: New quote replaces old quote in Library of Quotes
    public void editQuote(Quote editedQuote) {
        for (Quote quote : quotes) {
           if (Objects.equals(quote.getId(), editedQuote.getId())) {
               quote = editedQuote;
           }
        }
    }

    // EFFECTS: returns an ArrayList of all Quotes in Library
    public ArrayList<Quote> getAllQuotes() {
        return quotes;
    }

    // REQUIRES: 1 <= index <= total number of Quotes in Library
    // EFFECTS: returns the Quote where ID matches index - 1
    public Quote getQuote(int index) {
        Quote quote;
        quote = quotes.get(index - 1);
        return quote;
    }

}
