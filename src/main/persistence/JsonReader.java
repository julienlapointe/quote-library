package persistence;

import model.Quote;
import model.Library;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads the Library of Quotes from JSON data stored in a file
public class JsonReader {
    private String jsonFileLocation;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String jsonFileLocation) {
        this.jsonFileLocation = jsonFileLocation;
    }

    // EFFECTS: reads Library of Quotes from file and returns it;
    //          throws IOException if an error occurs while attempting to read data from file
    public Library read() throws IOException {
        String jsonData = readFile(jsonFileLocation);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLibrary(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String jsonFileLocation) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        // Stream class: https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
            // forEach(Consumer<? super T> action)
            // performs an action for each element of this stream
        // Files class: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/Files.html
            // lines(Path path, Charset cs)
            // reads all lines from a file as a Stream; unlike readAllLines, this method does not read all lines into
            // a List, but instead populates lazily as the stream is consumed.
        // Paths (note the "s" at end) class: https://docs.oracle.com/javase/7/docs/api/java/nio/file/Paths.html
            // get(String)
            // converts a path string to a Path object
        try (Stream<String> stream = Files.lines(Paths.get(jsonFileLocation), StandardCharsets.UTF_8)) {
            // (Parameters) -> {Body}
            // https://stackoverflow.com/questions/15146052/what-does-the-arrow-operator-do-in-java
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses Library from JSON object and returns it
    private Library parseLibrary(JSONObject jsonObject) {
        Library library = new Library();
        addQuotes(library, jsonObject);
        return library;
    }

    // MODIFIES: library
    // EFFECTS: parses all Quotes from JSON object and adds them to Library
    private void addQuotes(Library library, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("quotes");
        for (Object json : jsonArray) {
            // WHAT IS GOING ON WITH (JSONObject)??? TYPECASTING???
            JSONObject nextQuote = (JSONObject) json;
            addQuote(library, nextQuote);
        }
    }

    // MODIFIES: library
    // EFFECTS: parses a single Quote from JSON object and adds it to Library
    private void addQuote(Library library, JSONObject jsonObject) {
        String phrase = jsonObject.getString("phrase");
        String author = jsonObject.getString("author");
        Quote quote = new Quote(phrase, author);
        library.addQuote(quote);
    }
}
