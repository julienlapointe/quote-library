package ui;

// Imports
import exceptions.DuplicateException;
import exceptions.EmptyException;
import model.Library;
import model.Quote;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static java.lang.System.out;

// Represents the Swing UI
public class GUI extends JPanel
                 implements ListSelectionListener {

    // Model Fields
    Library library;

    // Persistence Fields
    private static final String JSON_FILE_LOCATION = "./data/quotes.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Frame, List, TextField, and Button Fields
    private static JFrame frame;
    private JList list;
    private DefaultListModel listModel;

    private JTextField phraseField;
    private JTextField authorField;

    private JButton addButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton saveButton;
    private JButton loadButton;

    private static final String addString = "Add";
    private static final String removeString = "Remove";
    private static final String editString = "Edit";
    private static final String saveString = "Save";
    private static final String loadString = "Load";

    Icon logoIcon = new ImageIcon(getClass().getResource("/Logo.gif"));
    Icon addIcon = new ImageIcon(getClass().getResource("/Add.gif"));
    Icon removeIcon = new ImageIcon(getClass().getResource("/Remove.gif"));
    Icon editIcon = new ImageIcon(getClass().getResource("/Edit.gif"));
    Icon saveIcon = new ImageIcon(getClass().getResource("/Save.gif"));
    Icon loadIcon = new ImageIcon(getClass().getResource("/Load.gif"));

    // String Manipulation Fields
    private String phrase = "";
    private String author = "";
    private String newQuoteString = "";
    private String editedQuoteString = "";

    // Constructor
    // EFFECTS: Library, JsonWriter, JsonReader, and DefaultListModel are initialized;
    //          button panels are added to Swing UI using BorderLayout
    public GUI() {
        super(new BorderLayout());

        // Initialize model object (a Library of Quotes)
        library = new Library();
        //Initialize persistence objects (write and read)
        jsonWriter = new JsonWriter(JSON_FILE_LOCATION);
        jsonReader = new JsonReader(JSON_FILE_LOCATION);
        // Initialize Swing list object
        listModel = new DefaultListModel();

        // Add dummy quotes until quotes are loaded from file
        try {
            addDummyQuotes();
        } catch (DuplicateException e) {
            out.println("Duplicate exception caught!");
        }

        // Assemble components into panels
        JScrollPane listScrollPane = getListPane();
        JPanel logoPanel = getLogoPanel();
        JPanel addButtonPanel = getAddTextFieldsAndButtonPanel();
        JPanel loadAndSaveButtonPanel = getSaveAndLoadButtonPanel();
        JPanel editAndRemoveButtonPanel = getEditAndRemoveButtonPanel();

        // Assemble panels into "panel groups"
        JPanel topButtonPanel = getTopPanel(logoPanel, addButtonPanel);
        JPanel bottomButtonPanel = getBottomPanel(loadAndSaveButtonPanel, editAndRemoveButtonPanel);

        // Place "panel groups" onto BorderLayout
        add(topButtonPanel, BorderLayout.PAGE_START);
        add(listScrollPane, BorderLayout.CENTER);
        add(bottomButtonPanel, BorderLayout.PAGE_END);
    }

    // ==============
    // HELPER METHODS
    // ==============

    // MODIFIES: library, listModel
    // EFFECTS: Dummy quotes are added to library and listModel
    private void addDummyQuotes() throws DuplicateException {
        for (String phrase : Arrays.asList("First dummy quote.", "Second dummy quote.", "Third dummy quote.")) {
            try {
                library.addQuote(new Quote(phrase, "Anonymous"));
            } catch (EmptyException | DuplicateException e) {
                out.println("Exception caught in Swing.addDummyQuotes()!");
            }
        }
        for (Quote quote : library.getAllQuotes()) {
            listModel.addElement(quote.getPhrase() + " ~ " + quote.getAuthor());
        }
    }

    // =====================
    // COMPONENTS AND PANELS
    // =====================

    // MODIFIES: list, listScrollPane
    // EFFECTS: JList is created and configured; JScrollPane is created; JList is added to JScrollPane
    private JScrollPane getListPane() {
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(list);
        return listScrollPane;
    }

    // MODIFIES: appLogoLabel, appNameLabel, logoPane
    // EFFECTS: JPanel is created that uses BoxLayout; JLabels are created for app name and logo;
    //          JLabels are added to JPanel; JPanel is returned
    private JPanel getLogoPanel() {
        //Create a panel that uses BoxLayout.
        JLabel appLogoLabel = new JLabel(logoIcon);
        JLabel appNameLabel = new JLabel("Quote Library");
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.LINE_AXIS));
        logoPanel.add(appLogoLabel);
        logoPanel.add(appNameLabel);
        return logoPanel;
    }

    // MODIFIES: phraseLabel, authorLabel, addButton, addListener, phraseField, authorField, addButtonPane
    // EFFECTS: JLabels are created to indicate purpose of each JTextField; JButton is created;
    //          AddListener event listener is created; JTextFields are created to accept user input;
    //          JPanel is created and components are placed using BoxLayout; JPanel is returned
    private JPanel getAddTextFieldsAndButtonPanel() {
        JLabel phraseLabel = new JLabel("Quote:");
        JLabel authorLabel = new JLabel("Author:");

        addButton = new JButton(addString, addIcon);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        phraseField = new JTextField(20);
        phraseField.addActionListener(addListener);
        phraseField.getDocument().addDocumentListener(addListener);

        authorField = new JTextField(20);
        authorField.addActionListener(addListener);
        authorField.getDocument().addDocumentListener(addListener);

        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new BoxLayout(addButtonPanel, BoxLayout.LINE_AXIS));
        addButtonPanel.add(phraseLabel);
        addButtonPanel.add(phraseField);
        addButtonPanel.add(Box.createHorizontalStrut(10));
        addButtonPanel.add(authorLabel);
        addButtonPanel.add(authorField);
        addButtonPanel.add(Box.createHorizontalStrut(10));
        addButtonPanel.add(addButton);
        return addButtonPanel;
    }

    // MODIFIES: editButton, removeButton, editAndRemoveButtonPanel
    // EFFECTS: JButtons are created and configured for "Edit" and "Remove" features;
    //          JPanel is created and JButtons are added to it; JPanel is returned
    private JPanel getEditAndRemoveButtonPanel() {
        editButton = new JButton(editString, editIcon);
        editButton.setActionCommand(editString);
        editButton.addActionListener(new EditListener());

        removeButton = new JButton(removeString, removeIcon);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());

        JPanel editAndRemoveButtonPanel = new JPanel();
        editAndRemoveButtonPanel.add(editButton);
        editAndRemoveButtonPanel.add(Box.createHorizontalStrut(5));
        editAndRemoveButtonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        editAndRemoveButtonPanel.add(Box.createHorizontalStrut(5));
        editAndRemoveButtonPanel.add(removeButton);
        return editAndRemoveButtonPanel;
    }

    // MODIFIES: saveButton, loadButton, loadAndSaveButtonPanel
    // EFFECTS: JButtons are created and configured for "Save" and "Load" features;
    //          JPanel is created and JButtons are added to it; JPanel is returned
    private JPanel getSaveAndLoadButtonPanel() {
        saveButton = new JButton(saveString, saveIcon);
        saveButton.setActionCommand(saveString);
        saveButton.addActionListener(new SaveListener());

        loadButton = new JButton(loadString, loadIcon);
        loadButton.setActionCommand(loadString);
        loadButton.addActionListener(new LoadListener());

        JPanel loadAndSaveButtonPanel = new JPanel();
        loadAndSaveButtonPanel.add(saveButton);
        loadAndSaveButtonPanel.add(Box.createHorizontalStrut(5));
        loadAndSaveButtonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        loadAndSaveButtonPanel.add(Box.createHorizontalStrut(5));
        loadAndSaveButtonPanel.add(loadButton);
        return loadAndSaveButtonPanel;
    }

    // MODIFIES: topButtonPanel
    // EFFECTS: JPanel is created and JPanels passed in as parameters are placed on it using BoxLayout;
    //          JPanel is returned
    private JPanel getTopPanel(JPanel logoPanel, JPanel addButtonPanel) {
        JPanel topButtonPanel = new JPanel();
        topButtonPanel.setLayout(new BoxLayout(topButtonPanel, BoxLayout.LINE_AXIS));
        topButtonPanel.add(logoPanel);
        topButtonPanel.add(Box.createHorizontalStrut(60));
        topButtonPanel.add(addButtonPanel);
        topButtonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return topButtonPanel;
    }

    // MODIFIES: bottomButtonPanel
    // EFFECTS: JPanel is created and JPanels passed in as parameters are placed on it using BoxLayout;
    //          JPanel is returned
    private JPanel getBottomPanel(JPanel loadAndSaveButtonPanel, JPanel editAndRemoveButtonPanel) {
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setLayout(new BoxLayout(bottomButtonPanel, BoxLayout.LINE_AXIS));
        bottomButtonPanel.add(editAndRemoveButtonPanel);
        bottomButtonPanel.add(loadAndSaveButtonPanel);
        bottomButtonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return bottomButtonPanel;
    }

    // ============================
    // EVENT LISTENERS AND HANDLERS
    // ============================

    // Represents an event listener for the "Add" button
    class AddListener implements ActionListener, DocumentListener {
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

            // Get values from phraseField and authorField
            phrase = phraseField.getText();
            author = authorField.getText();

            // Create new Quote and add to Library model
            Quote newQuote = new Quote(phrase, author);
            try {
                library.addQuote(newQuote);
            } catch (EmptyException e) {
                playBeepSound();
                out.println("BEEP! Sorry, the \"Quote\" text field cannot be empty.");
                return;
            } catch (DuplicateException e) {
                playBeepSound();
                out.println("BEEP! Sorry, that quote is already in the library.");
                return;
            }

            // Assemble newQuoteString
            newQuoteString = newQuote.getPhrase() + " ~ " + newQuote.getAuthor();
            // Set index for insertion of newQuoteString
            int index = setListItemInsertionIndex();
            // Insert newQuoteString into the listModel at position index
            listModel.insertElementAt(newQuoteString, index);

            // Reset the text fields
            resetTextFields();

            // Select the newQuote list item and make it visible
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);

            // Play sound for successful completion of task
            playAddSound();
        }

        // -----------------------------------------------------
        // HELPER METHODS FOR actionPerformed(ActionEvent event)
        // -----------------------------------------------------

        private int setListItemInsertionIndex() {
            // Get index of selected list item
            int index = list.getSelectedIndex();
            // If no list item is selected, then set index for insertion to 0 (the start of the list)
            if (index == -1) {
                index = 0;
                // Otherwise, set index for insertion to be right after the selected list item
            } else {
                index++;
            }
            return index;
        }

        private void resetTextFields() {
            // Reset the text fields
            phraseField.requestFocusInWindow();
            phraseField.setText("");
            authorField.requestFocusInWindow();
            authorField.setText("");
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

    // Represents an event listener for the "Edit" button
    class EditListener implements ActionListener {

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
            editedQuoteString = getEditPanel();
            // Replace list item at same index with editedQuote
            listModel.setElementAt(editedQuoteString, index);

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
            String editedQuoteString = "";

            JPanel editPanel = new JPanel();
            JTextField phraseField = new JTextField(phrase,20);
            JTextField authorField = new JTextField(author,20);
            editPanel.add(phraseField);
            editPanel.add(authorField);
            JOptionPane.showMessageDialog(frame, editPanel);

            editedQuoteString = phraseField.getText() + " ~ " + authorField.getText();

            return editedQuoteString;
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

    // Represents an event listener for the "Remove" button
    class RemoveListener implements ActionListener {
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

    // Represents an event listener for the "Save" button
    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            library.removeAllQuotes();
            for (int i = 0; i < list.getModel().getSize(); i++) {
                String string = (String) list.getModel().getElementAt(i);
                String[] splitStrings = string.split(" ~ ");
                phrase = splitStrings[0];
                author = splitStrings[1];
                try {
                    library.addQuote(new Quote(phrase, author));
                } catch (EmptyException | DuplicateException e) {
                    out.println("Exception caught in Swing.SaveListener.actionPerformed()!");
                }
            }
            saveLibrary();

            // Play sound for successful completion of task
            playSaveSound();
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

    // Represents an event listener for the "Load" button
    class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            loadLibrary();
            listModel.removeAllElements();
            for (Quote quote : library.getAllQuotes()) {
                listModel.addElement(quote.getPhrase() + " ~ " + quote.getAuthor());
            }

            // Play sound for successful completion of task
            playLoadSound();
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

    // Required by ListSelectionListener
    // MODIFIES: removeButton, editButton
    public void valueChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting() == false) {
            if (list.getSelectedIndex() == -1) {
                editButton.setEnabled(false);
                removeButton.setEnabled(false);
            } else {
                editButton.setEnabled(true);
                removeButton.setEnabled(true);
            }
        }
    }

    // MODIFIES: frame, contentPane
    // EFFECTS: JFrame is created and configured; JComponent is created and added to JFrame;
    //          JFrame is displayed
    protected static void getGUI() {

        // Create and set up the window
        frame = new JFrame("Quote Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane
        JComponent contentPane = new GUI();
        contentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(contentPane);

        // Display the window
        frame.pack();
        frame.setVisible(true);
    }

    // ==============
    // Helper methods
    // ==============

    // EFFECTS: newQuote phrase is checked for duplicates; if unique, then do nothing; if duplicate, then throw
    //          DuplicateException
    private void checkDuplicate(Quote newQuote) throws DuplicateException {
        for (Quote quote : library.getAllQuotes()) {
            if (quote.getPhrase().contains(newQuote.phrase)) {
                throw new DuplicateException();
            }
        }
    }

    // ===========
    // Persistence
    // ===========

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

}
