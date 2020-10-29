package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a library of all quotes
public class Library implements Writable {
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
    }

    // REQUIRES: quote contains a phrase String of non-zero length
    // MODIFIES: this
    // EFFECTS: Quote is removed from Library of Quotes
    public void removeQuote(Quote quote) {
        quotes.remove(quote);
    }

    // EFFECTS: returns an ArrayList of all Quotes in Library
    public ArrayList<Quote> getAllQuotes() {
        return quotes;
    }

    // EFFECTS: returns number of thingies in this workroom
    public int numberOfQuotes() {
        return quotes.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Quote Library", quotesToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray quotesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Quote quote : quotes) {
            jsonArray.put(quote.toJson());
        }

        return jsonArray;
    }
}
