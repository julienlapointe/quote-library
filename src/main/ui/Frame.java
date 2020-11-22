package ui;

// Imports
import javax.swing.*;

// Represents the Swing UI
public class Frame {

    // MODIFIES: frame, contentPane
    // EFFECTS: JFrame is created and configured; JComponent is created and added to JFrame;
    //          JFrame is displayed
    protected static void getGUI() {

        // Create and set up the window
        JFrame frame = new JFrame("Quote Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane
        JComponent contentPane = new Panels();
        contentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(contentPane);

        // Display the window
        frame.pack();
        frame.setVisible(true);
    }
}
