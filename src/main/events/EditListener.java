package events;

import ui.Panels;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

// Represents an event listener for the "Edit" button
public class EditListener extends Panels
                          implements ActionListener {

    // MODIFIES: index, string, splitStrings, phrase, author, listModel, size, editButton, list,
    //           editPanel, phraseField, authorField,
    //           url, audioIn, clip
    // EFFECTS: Value of the selected list item is captured, parsed, and inserted into phraseField and authorField
    //          of JOptionPane pop-up dialog; edited values are captured from phraseField and authorField;
    //          editedQuote is assembled and validated (not empty and not a duplicate);
    //          editedQuote is inserted into the listModel at the same index
    public void actionPerformed(ActionEvent event) {

        // Play sound for initiation of task
        playEditSound();

        // Get the index of the selected list item
        int index = list.getSelectedIndex();
        // Get value of the selected list item
        String string = (String) list.getModel().getElementAt(index);
        // Split string into phrase and author
        String[] splitStrings = string.split(" ~ ");
        phrase = splitStrings[0];
        author = splitStrings[1];

        // Get JOptionPane pop-up dialog to capture edited values phrase and author (editedQuote)
        editedQuote = getEditPanel();
        // Replace list item at same index with editedQuote
        listModel.setElementAt(editedQuote, index);

        // Get number of list items in listModel
        int size = listModel.getSize();

        // If there are no list items to select, disable the "Edit" button
        if (size == 0) {
            editButton.setEnabled(false);
        }
    }

    // MODIFIES: editPanel, phraseField, authorField
    // EFFECTS: JPanel is created with two JTextFields; JPanel is added to a JOptionPane;
    //          phraseField and authorField are captured; values are assembled into editedQuote;
    //          editedQuote is returned
    private String getEditPanel() {
        String editedQuote = "";

        JPanel editPanel = new JPanel();
        JTextField phraseField = new JTextField(phrase,20);
        JTextField authorField = new JTextField(author,20);
        editPanel.add(phraseField);
        editPanel.add(authorField);
        JOptionPane.showMessageDialog(null, editPanel);

        editedQuote = phraseField.getText() + " ~ " + authorField.getText();

        return editedQuote;
    }

    // MODIFIES: url, audioIn, clip
    // EFFECTS: File location is retrieved; audio input stream is opened; audio clip is set and opened;
    //          audio is loaded and played / started from the audio input stream
    private void playEditSound() {
        try {
            URL url = this.getClass().getClassLoader().getResource("Edit.wav");
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
