package model;

import java.util.ArrayList;
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
    // EFFECTS: new quote is added to Library of Quotes
    public void addQuote(Quote quote) {
        quotes.add(quote);
    }

    // REQUIRES: quote contains a phrase String of non-zero length
    // MODIFIES: this
    // EFFECTS: new quote is removed from Library of Quotes
    public void removeQuote(Quote quote) {
        quotes.remove(quote);
    }

    // REQUIRES: quote contains a phrase String of non-zero length
    // MODIFIES: this
    // EFFECTS: new quote replaces old quote in Library of Quotes
    public void editQuote(Quote editedQuote) {
        for (Quote quote : quotes) {
           if (Objects.equals(quote.getId(), editedQuote.getId())) {
               quote = editedQuote;
           }
        }
    }

    public String printAllQuotes() {
        String listOfQuotes = "Here is a list of your quotes:";
        int index = 0;
        for (Quote quote : quotes) {
            listOfQuotes = listOfQuotes.concat("\n" + index + ". " + "\"" + quote.phrase + "\" ~ " +  quote.author);
            index++;
        }
        return listOfQuotes;
    }

    public Quote getQuote(int index) {
        Quote quote;
        quote = quotes.get(index - 1);
        return quote;
    }

    public ArrayList<Quote> getAllQuotes() {
        return quotes;
    }

}
