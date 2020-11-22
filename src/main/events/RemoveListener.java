package events;

import ui.Panels;

import javax.sound.sampled.*;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

// Represents an event listener for the "Remove" button
public class RemoveListener extends Panels
                            implements ActionListener {
    public void actionPerformed(ActionEvent event) {

        // Get the index of the selected list item
        int index = list.getSelectedIndex();
        // Remove selected list item
        listModel.removeElementAt(index);

        // Get number of list items in listModel
        int size = listModel.getSize();

        // If there are no list items to select, disable the "Remove" button
        if (size == 0) {
            removeButton.setEnabled(false);
            // Otherwise, select the previous list item
        } else {
            index--;
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
        // Play sound for successful completion of task
        playRemoveSound();
    }

    // MODIFIES: url, audioIn, clip
    // EFFECTS: File location is retrieved; audio input stream is opened; audio clip is set and opened;
    //          audio is loaded and played / started from the audio input stream
    private void playRemoveSound() {
        try {
            URL url = this.getClass().getClassLoader().getResource("Remove.wav");
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
