package events;

import model.Library;
import model.Quote;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.Panels;

import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import static java.lang.System.out;

// Represents an event listener for the "Load" button
public class LoadListener extends Panels
                          implements ActionListener {

    // Model Fields
    Library library;

    // Persistence Fields
    private static final String JSON_FILE_LOCATION = "./data/quotes.json";
    private JsonReader jsonReader;

    public LoadListener() {
        //Initialize persistence object (read)
        jsonReader = new JsonReader(JSON_FILE_LOCATION);
    }

    public void actionPerformed(ActionEvent event) {
        loadLibrary();
        listModel.removeAllElements();
        for (Quote quote : library.getAllQuotes()) {
            listModel.addElement(quote.getPhrase() + " ~ " + quote.getAuthor());
        }

        // Play sound for successful completion of task
        playLoadSound();
    }

    // MODIFIES: this
    // EFFECTS: loads Library from file
    private void loadLibrary() {
        try {
            library = jsonReader.read();
            out.println("Loaded your quotes from " + JSON_FILE_LOCATION);
        } catch (IOException e) {
            out.println("Unable to read from file: " + JSON_FILE_LOCATION);
        }
    }

    // MODIFIES: url, audioIn, clip
    // EFFECTS: File location is retrieved; audio input stream is opened; audio clip is set and opened;
    //          audio is loaded and played / started from the audio input stream
    private void playLoadSound() {
        try {
            URL url = this.getClass().getClassLoader().getResource("Load.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (LineUnavailableException exception) {
            exception.printStackTrace();
        }
    }
}
