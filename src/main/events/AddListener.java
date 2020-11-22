package events;

import ui.Panels;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import static java.lang.System.out;

// Represents an event listener for the "Add" button
public class AddListener extends Panels
                         implements ActionListener, DocumentListener {
    private boolean alreadyEnabled = false;
    private JButton button;

    // Constructor
    // EFFECTS: JButton is initialized
    public AddListener(JButton button) {
        this.button = button;
    }

    // MODIFIES: phrase, author, newQuote, listModel, phraseField, authorField, list,
    //           url, audioIn, clip,
    //           button, alreadyEnabled
    // EFFECTS: phrase and author text are captured from phraseField and authorField;
    //          newQuote is assembled and validated (not empty and not a duplicate);
    //          newQuote is inserted into the listModel at position index;
    //          phraseField and authorField are reset
    public void actionPerformed(ActionEvent event) {

        // Get text from phraseField and authorField
        String phrase = phraseField.getText();
        String author = authorField.getText();

        // If authorField is empty, then set author to "Anonymous"
        authorFieldValidation();

        // Assemble newQuote
        String newQuote = phrase  + " ~ " + author;

        // If phrase is empty or newQuote is a duplicate, then play a *beep* sound and return / exit
        if (phrase.equals("") || alreadyInList(newQuote)) {
            playBeepSound();
            out.println("BEEP!");
            return;
        }

        // Get index of selected list item
        int index = list.getSelectedIndex();
        // If no list item is selected, then set index for insertion to 0 (the start of the list)
        if (index == -1) {
            index = 0;
            // Otherwise, set index for insertion to be right after the selected list item
        } else {
            index++;
        }

        // Insert newQuote into the listModel at position index
        listModel.insertElementAt(newQuote, index);

        // Reset the text field
        phraseField.requestFocusInWindow();
        phraseField.setText("");
        authorField.requestFocusInWindow();
        authorField.setText("");

        // Select the newQuote list item and make it visible
        list.setSelectedIndex(index);
        list.ensureIndexIsVisible(index);

        // Play sound for successful completion of task
        playAddSound();
    }

    // -----------------------------------------------------
    // HELPER METHODS FOR actionPerformed(ActionEvent event)
    // -----------------------------------------------------

    // MODIFIES: author
    // EFFECTS: If authorField is empty, then author is set to "Anonymous"
    private void authorFieldValidation() {
        if (author.equals("")) {
            author = "Anonymous";
        }
    }

    // MODIFIES: url, audioIn, clip
    // EFFECTS: File location is retrieved; audio input stream is opened; audio clip is set and opened;
    //          audio is loaded and played / started from the audio input stream
    private void playAddSound() {
        try {
            // Retrieve location of audio file
            URL url = this.getClass().getClassLoader().getResource("Add.wav");
            // Open audio input stream
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource
            Clip clip = AudioSystem.getClip();
            // Open audio clip from the audio input stream
            clip.open(audioIn);
            // Start playing the audio clip
            clip.start();
        } catch (UnsupportedAudioFileException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (LineUnavailableException exception) {
            exception.printStackTrace();
        }
    }

    // MODIFIES: url, audioIn, clip
    // EFFECTS: File location is retrieved; audio input stream is opened; audio clip is set and opened;
    //          audio is loaded and played / started from the audio input stream
    private void playBeepSound() {
        try {
            URL url = this.getClass().getClassLoader().getResource("Beep.wav");
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

    // EFFECTS: If listModel already contains the quote, then return true; otherwise, return false
    protected boolean alreadyInList(String quote) {
        return listModel.contains(quote);
    }

    // Required by DocumentListener
    public void insertUpdate(DocumentEvent event) {
        enableButton();
    }

    // Required by DocumentListener
    public void removeUpdate(DocumentEvent event) {
        handleEmptyTextField(event);
    }

    // Required by DocumentListener
    public void changedUpdate(DocumentEvent event) {
        if (!handleEmptyTextField(event)) {
            enableButton();
        }
    }

    // Required by DocumentListener
    // MODIFIES: button
    private void enableButton() {
        if (!alreadyEnabled) {
            button.setEnabled(true);
        }
    }

    // Required by DocumentListener
    // MODIFIES: button, alreadyEnabled
    private boolean handleEmptyTextField(DocumentEvent event) {
        if (event.getDocument().getLength() <= 0) {
            button.setEnabled(false);
            alreadyEnabled = false;
            return true;
        }
        return false;
    }
}
