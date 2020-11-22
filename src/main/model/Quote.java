package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

// import java.util.TreeSet;

// Represents a quote having an ID, phrase, author (optional), and tag(s)
public class Quote implements Writable {
    protected static int nextId = 0;    // tracks id of next quote created
    protected int id = 0;               // quote id
    public String phrase = "";          // phrase of quote
    public String author = "Anonymous"; // author of quote

    // Constructor
    // REQUIRES: phrase has a non-zero length
    // EFFECTS:  Quote is initialized with quote ID as a unique non-negative integer, phrase set to phrase,
    //           author set to author, and tags set to tag
    public Quote(String phrase, String author) {
        this.id = nextId++;
        this.phrase = phrase;
        this.author = author;
    }

    // =======
    // Getters
    // =======

    public int getId() {
        return this.id;
    }

    public String getPhrase() {
        return this.phrase;
    }

    public String getAuthor() {
        return this.author;
    }

    // =======
    // Setters
    // =======

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // EFFECTS: puts a Quote's phrase and author into a JSON Object and returns it
    // NOTE:    JSON uses key-value pairs
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("phrase", this.phrase);
        json.put("author", this.author);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return phrase.equals(quote.phrase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phrase);
    }
}
