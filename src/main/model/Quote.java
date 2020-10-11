package model;

// import java.util.TreeSet;

// Represents a quote having an ID, phrase, author (optional), and tag(s)
public class Quote {
    protected static int nextId = 0;          // tracks id of next quote created
    protected int id = 0;                     // quote id
    protected String phrase = "";             // phrase of quote
    protected String author = "Anonymous";    // author of quote
//    private TreeSet<String> tags;           // tag(s) of quote

    // Constructor
    // REQUIRES: phrase has a non-zero length
    // EFFECTS:  Quote is initialized with quote ID as a unique non-negative integer, phrase set to phrase, author set to
    //           author, and tags set to tag
    public Quote (String phrase, String author) {
//    public Quote (String phrase, String author, TreeSet<String> tags) {
        this.id = nextId++;
        this.phrase = phrase;
        this.author = author;
//        this.tags = TreeSet<String>();
//        this.tags = tags;
    }

    // Getters
    public int getId() {
        return this.id;
    }
    public String getPhrase() {
        return this.phrase;
    }
    public String getAuthor() {
        return this.author;
    }
//    public TreeSet<String> getTags() {
//        return this.tags;
//    }

    // Setters
    public void setPhrase (String phrase) {
        this.phrase = phrase;
    }
    public void setAuthor (String author) {
        this.author = author;
    }

}
