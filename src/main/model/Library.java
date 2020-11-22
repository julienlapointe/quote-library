package model;

import exceptions.DuplicateException;
import exceptions.EmptyException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a library that stores a collection of quotes
public class Library implements Writable {
    private ArrayList<Quote> quotes;

    // Constructor
    // EFFECTS: Library is initialized to be an empty ArrayList of Quotes
    public Library() {
        quotes = new ArrayList<Quote>();
    }

    // REQUIRES: quote contains a phrase String of non-zero length
    // MODIFIES: this
    // EFFECTS: newQuote is added to the Library of Quotes
    public void addQuote(Quote newQuote) throws DuplicateException {
        if (quotes.contains(newQuote)) {
            throw new DuplicateException();
        }
        quotes.add(newQuote);
    }

    // MODIFIES: this
    // EFFECTS: Quote is removed from Library of Quotes
    public void removeQuote(Quote quote) {
        quotes.remove(quote);
    }

    // MODIFIES: this
    // EFFECTS: All Quotes are removed from Library of Quotes
    public void removeAllQuotes() {
        quotes.clear();
    }

    // EFFECTS: returns an ArrayList of all Quotes in Library
    public ArrayList<Quote> getAllQuotes() {
        return quotes;
    }

    // EFFECTS: returns the number of Quotes in Library
    public int numberOfQuotes() {
        return quotes.size();
    }

    // EFFECTS: puts the JSON Array of all Quotes in Library into a JSON Object at key "quotes" and returns it
    // NOTE:    JSON uses key-value pairs
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("quotes", quotesToJson());
        return json;
    }

    // EFFECTS: puts each Quote from Library into the JSON array and returns it
    private JSONArray quotesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Quote quote : quotes) {
            jsonArray.put(quote.toJson());
        }
        return jsonArray;
    }
}
