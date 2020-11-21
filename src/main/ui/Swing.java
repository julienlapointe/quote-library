package ui;

// Imports
import exceptions.DuplicateException;
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
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static java.lang.System.out;

// Represents the Swing UI
public class Swing extends JPanel
                   implements ListSelectionListener {

    // Model Fields
    Library library;

    // Persistence Fields
    private static final String JSON_FILE_LOCATION = "./data/quotes.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // List, TextField, and Button Fields
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
    private String newQuote = "";
    private String editedQuote = "";

    // Constructor
    // EFFECTS: Library, JsonWriter, JsonReader, and DefaultListModel are initialized;
    //          button panels are added to Swing UI using BorderLayout
    public Swing() {
        super(new BorderLayout());

        // Initialize model object (a Library of Quotes)
        library = new Library();
        //Initialize persistence objects (write and read)
        jsonWriter = new JsonWriter(JSON_FILE_LOCATION);
        jsonReader = new JsonReader(JSON_FILE_LOCATION);
        // Initialize Swing list object
        listModel = new DefaultListModel();

        // Add dummy quotes until quotes are loaded from file
        addDummyQuotes();

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
    private void addDummyQuotes() {
        library.addQuote(new Quote("First dummy quote.", "Anonymous"));
        library.addQuote(new Quote("Second dummy quote.", "Anonymous"));
        library.addQuote(new Quote("Third dummy quote.", "Anonymous"));
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

        // MODIFIES: phrase, author, newQuote, listModel, phraseField, authorField, list
        // EFFECTS: phrase and author text are captured from phraseField and authorField;
        //          newQuote is assembled and validated (not empty and not a duplicate);
        //
        public void actionPerformed(ActionEvent event) {

            // Get text from phraseField and authorField
            phrase = phraseField.getText();
            author = authorField.getText();

            // If authorField is empty, then set author to "Anonymous"
            authorFieldValidation();

            // Assemble newQuote
            newQuote = phrase  + " ~ " + author;

            // If phrase is empty or newQuote is a duplicate, then play a *beep* sound and return / exit
            if (phrase.equals("") || alreadyInList(newQuote)) {
                playBeepSound();
                out.println("BEEP!");
                return;
            }

            // Get index of selected item in list
            int index = list.getSelectedIndex();
            // If no item is selected, then set index for insertion to 0 (the start of the list)
            if (index == -1) {
                index = 0;
            // Otherwise, set index for insertion to be right after the selected item
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

            // Select the newQuote item and make it visible
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);

            // Play sound for successful completion of task
            playAddSound();
        }

        // --------------
        // HELPER METHODS
        // --------------

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

        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    // Represents an event listener for the "Edit" button
    class EditListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            // Play sound for initiation of task
            playEditSound();

            int index = list.getSelectedIndex();
            String string = (String) list.getModel().getElementAt(index);
            String[] splitStrings = string.split(" ~ ");
            phrase = splitStrings[0];
            author = splitStrings[1];

            editedQuote = getEditPanel();
            listModel.setElementAt(editedQuote, index);

            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                editButton.setEnabled(false);
            } else { //Select an index.

                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }

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
                // Open an audio input stream.
                URL url = this.getClass().getClassLoader().getResource("Edit.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
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
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.removeElementAt(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);

                // REQUIRES: 1 <= userInput <= total number of Quotes in Library
                // MODIFIES: Quote and Library
                // EFFECTS: userInput is captured;
                //          userInput is used to find Quote to delete;
                //          Quote is deleted from Library
//                library.removeQuote(library.getAllQuotes().get(index));
//                saveLibrary();
            }
            // Play sound for successful completion of task
            playRemoveSound();
        }

        // MODIFIES: url, audioIn, clip
        // EFFECTS: File location is retrieved; audio input stream is opened; audio clip is set and opened;
        //          audio is loaded and played / started from the audio input stream
        private void playRemoveSound() {
            try {
                // Open an audio input stream.
                URL url = this.getClass().getClassLoader().getResource("Remove.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
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
                library.addQuote(new Quote(phrase, author));
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
                // Open an audio input stream.
                URL url = this.getClass().getClassLoader().getResource("Save.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
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
                // Open an audio input stream.
                URL url = this.getClass().getClassLoader().getResource("Load.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
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

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {

                removeButton.setEnabled(false);
                editButton.setEnabled(false);

            } else {

                removeButton.setEnabled(true);
                editButton.setEnabled(true);
            }
        }
    }

    protected static void getGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Quote Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = null;
        newContentPane = new Swing();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
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
