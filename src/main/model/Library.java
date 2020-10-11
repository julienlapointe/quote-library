package model;

import java.util.ArrayList;
import java.util.Objects;

// Represents a library of all quotes
public class Library {
    private ArrayList<Quote> quotes;

    // Constructor
    // EFFECTS: library is initialized to have no quotes
    public Library() {
        quotes = new ArrayList<Quote>();
    }

    public void addQuote(Quote quote) {
        quotes.add(quote);
    }

    public void removeQuote(Quote quote) {
        quotes.remove(quote);
    }

    public void editQuote(Quote editedQuote) {
//        (int i = 0; i < quotes.size(); i++)
        for (Quote quote : quotes) {
           if (Objects.equals(quote.getId(), editedQuote.getId())) {
               quote = editedQuote;
           }
        }
    }

    public String viewQuoteLibrary() {
//        Quote quote;
        String listOfQuotes = "Here is a list of your quotes: ";
        int index = 0;
        for (Quote quote : quotes) {
//        for (int i = 0; i < quotes.size(); i++) {
//            quote =
            listOfQuotes = listOfQuotes.concat("\n" + index + ". " + quote.phrase + " ~ " +  quote.author);
            index++;
        }
        return listOfQuotes;
    }

    public Quote getQuote(int index) {
        Quote quote;
        quote = quotes.get(index - 1);
        return quote;
    }

}
