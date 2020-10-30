package persistence;

import model.Library;
import org.json.JSONObject;

import java.io.*;

// Represents a writer that writes a JSON representation of Library to a file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String jsonFileLocation;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String jsonFileLocation) {
        this.jsonFileLocation = jsonFileLocation;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot be opened
    public void open() throws FileNotFoundException {
        // PrintWriter object: http://stleary.github.io/JSON-java/index.html
        // prints formatted representations of objects to a text-output stream
        writer = new PrintWriter(new File(jsonFileLocation));
    }

    // MODIFIES: this
    // EFFECTS: writes a JSON representation of Library to a file
    public void write(Library library) {
        JSONObject json = library.toJson();
        // toString(int indentFactor); http://stleary.github.io/JSON-java/index.html
        // makes pretty JSON text of the JSON Object (json in this case)
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
