package events;

import model.Library;
import model.Quote;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.Panels;

import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static java.lang.System.out;

// Represents an event listener for the "Save" button
public class SaveListener extends Panels
                          implements ActionListener {

    // Model Fields
    Library library;

    // Persistence Fields
    private static final String JSON_FILE_LOCATION = "./data/quotes.json";
    private JsonWriter jsonWriter;

    public SaveListener() {
        //Initialize persistence object (read)
        jsonWriter = new JsonWriter(JSON_FILE_LOCATION);
    }

    public void actionPerformed(ActionEvent event) {
        library.removeAllQuotes();
        for (int i = 0; i < list.getModel().getSize(); i++) {
            String string = (String) list.getModel().getElementAt(i);
            String[] splitStrings = string.split(" ~ ");
            phrase = splitStrings[0];
            author = splitStrings[1];
            library.addQuote(new Quote(phrase, author));
        }
        saveLibrary();

        // Play sound for successful completion of task
        playSaveSound();
    }

    // EFFECTS: saves the Library to file
    private void saveLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();
            out.println("Saved your quotes to " + JSON_FILE_LOCATION);
        } catch (FileNotFoundException e) {
            out.println("Unable to write to file: " + JSON_FILE_LOCATION);
        }
    }

    // MODIFIES: url, audioIn, clip
    // EFFECTS: File location is retrieved; audio input stream is opened; audio clip is set and opened;
    //          audio is loaded and played / started from the audio input stream
    private void playSaveSound() {
        try {
            URL url = this.getClass().getClassLoader().getResource("Save.wav");
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
