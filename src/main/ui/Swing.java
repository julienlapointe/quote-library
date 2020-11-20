package ui;

import exceptions.DuplicateException;
import model.Library;
import model.Quote;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static java.lang.System.out;

public class Swing extends JPanel
                   implements ListSelectionListener {

    private JList list;
    private DefaultListModel listModel;

    Library library;
    private static final String JSON_FILE_LOCATION = "./data/quotes.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

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

    Icon addIcon = new ImageIcon(getClass().getResource("/Add24.gif"));
    Icon removeIcon = new ImageIcon(getClass().getResource("/Remove24.gif"));
    Icon editIcon = new ImageIcon(getClass().getResource("/Edit24.gif"));
    Icon saveIcon = new ImageIcon(getClass().getResource("/Save24.gif"));
    Icon loadIcon = new ImageIcon(getClass().getResource("/Load24.gif"));
    Icon logoIcon = new ImageIcon(getClass().getResource("/Logo24.gif"));

    JLabel saveIconLabel = new JLabel(saveIcon);

    private JTextField phraseField;
    private JTextField authorField;

    public Swing() throws FileNotFoundException {
        super(new BorderLayout());

        library = new Library();
        jsonWriter = new JsonWriter(JSON_FILE_LOCATION);
        jsonReader = new JsonReader(JSON_FILE_LOCATION);

        library.addQuote(new Quote("First dummy quote.", "Anonymous"));
        library.addQuote(new Quote("Second dummy quote.", "Anonymous"));
        library.addQuote(new Quote("Third dummy quote.", "Anonymous"));

        out.println(Swing.class.getResource("/Add16.gif"));
        out.println(getClass().getResource("/Add16.gif"));

        loadLibrary();

        listModel = new DefaultListModel();
        for (Quote quote : library.getAllQuotes()) {
            listModel.addElement(quote.getPhrase() + " ~ " + quote.getAuthor());
        }

        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        addButton = new JButton(addString, addIcon);
//        addButton.setSize(50, 30);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        removeButton = new JButton(removeString, removeIcon);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());

        editButton = new JButton(editString, editIcon);
        editButton.setActionCommand(editString);
        editButton.addActionListener(new EditListener());

        saveButton = new JButton(saveString, saveIcon);
        saveButton.setActionCommand(saveString);
        saveButton.addActionListener(new SaveListener());

        loadButton = new JButton(loadString, loadIcon);
        loadButton.setActionCommand(loadString);
        loadButton.addActionListener(new LoadListener());

        JLabel appLogoLabel = new JLabel(logoIcon);
        JLabel appNameLabel = new JLabel("Quote Library");
        JLabel phraseLabel = new JLabel("Quote:");
        JLabel authorLabel = new JLabel("Author:");



        phraseField = new JTextField(20);
//        phraseField = new JTextField("Quote...", 20);
        phraseField.addActionListener(addListener);
        phraseField.getDocument().addDocumentListener(addListener);
        String name = listModel.getElementAt(
                list.getSelectedIndex()).toString();

        authorField = new JTextField(20);
//        authorField = new JTextField("Author...", 20);
        authorField.addActionListener(addListener);
        authorField.getDocument().addDocumentListener(addListener);
        String author = listModel.getElementAt(
                list.getSelectedIndex()).toString();

//        phraseField.addFocusListener(new FocusListener() {
//            public void focusGained(FocusEvent e) {
//                phraseField.setText("");
//            }
//
//            public void focusLost(FocusEvent e) {
//                phraseField.setText("Quote...");
//            }
//        });
//
//        authorField.addFocusListener(new FocusListener() {
//            public void focusGained(FocusEvent e) {
//                authorField.setText("");
//            }
//
//            public void focusLost(FocusEvent e) {
//                authorField.setText("Author...");
//            }
//        });



        //Create a panel that uses BoxLayout.
        JPanel logoPane = new JPanel();
        logoPane.setLayout(new BoxLayout(logoPane, BoxLayout.LINE_AXIS));
        logoPane.add(appLogoLabel);
        logoPane.add(appNameLabel);

        //Create a panel that uses BoxLayout.
        JPanel addButtonPane = new JPanel();
        addButtonPane.setLayout(new BoxLayout(addButtonPane, BoxLayout.LINE_AXIS));
        addButtonPane.add(phraseLabel);
        addButtonPane.add(phraseField);
        addButtonPane.add(Box.createHorizontalStrut(10));
        addButtonPane.add(authorLabel);
        addButtonPane.add(authorField);
        addButtonPane.add(Box.createHorizontalStrut(10));
        addButtonPane.add(addButton);

        //Create a panel that uses BoxLayout.
        JPanel editAndRemoveButtonPane = new JPanel();
//        editAndRemoveButtonPane.setLayout(new BoxLayout(editAndRemoveButtonPane, BoxLayout.LINE_AXIS));
        editAndRemoveButtonPane.add(editButton);
        editAndRemoveButtonPane.add(Box.createHorizontalStrut(5));
        editAndRemoveButtonPane.add(new JSeparator(SwingConstants.VERTICAL));
        editAndRemoveButtonPane.add(Box.createHorizontalStrut(5));
        editAndRemoveButtonPane.add(removeButton);

        //Create a panel that uses BoxLayout.
        JPanel loadAndSaveButtonPane = new JPanel();
//        loadAndSaveButtonPane.setLayout(new BoxLayout(loadAndSaveButtonPane, BoxLayout.LINE_AXIS));
        loadAndSaveButtonPane.add(saveButton);
        loadAndSaveButtonPane.add(Box.createHorizontalStrut(5));
        loadAndSaveButtonPane.add(new JSeparator(SwingConstants.VERTICAL));
        loadAndSaveButtonPane.add(Box.createHorizontalStrut(5));
        loadAndSaveButtonPane.add(loadButton);

        //Create a panel that uses BoxLayout.
        JPanel topButtonPane = new JPanel();
        topButtonPane.setLayout(new BoxLayout(topButtonPane, BoxLayout.LINE_AXIS));
        topButtonPane.add(logoPane);
        topButtonPane.add(Box.createHorizontalStrut(60));
        topButtonPane.add(addButtonPane);
        topButtonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        //Create a panel that uses BoxLayout.
        JPanel bottomButtonPane = new JPanel();
        bottomButtonPane.setLayout(new BoxLayout(bottomButtonPane, BoxLayout.LINE_AXIS));
        bottomButtonPane.add(editAndRemoveButtonPane);
        bottomButtonPane.add(loadAndSaveButtonPane);
        bottomButtonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(topButtonPane, BorderLayout.PAGE_START);
        add(listScrollPane, BorderLayout.CENTER);
        add(bottomButtonPane, BorderLayout.PAGE_END);
    }

    // ===============
    // EVENT LISTENERS
    // ===============

    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

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
                library.removeQuote(library.getAllQuotes().get(index));
                saveLibrary();
            }
        }
    }

    // COPIED REMOVELISTENER IMPLEMENTATION
    class EditListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

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
    }

    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                saveButton.setEnabled(false);
            } else { //Select an index.

                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                loadButton.setEnabled(false);
            } else { //Select an index.

                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    //This listener is shared by the text field and the hire button.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String phrase = phraseField.getText();
            String author = authorField.getText();

            //User didn't type in a unique name...
            if (phrase.equals("") || alreadyInList(phrase)) {
                // USE EXTERNAL SOUND FILE (.MP3?)
                Toolkit.getDefaultToolkit().beep();
                out.println("BEEP!");
                phraseField.requestFocusInWindow();
                phraseField.selectAll();
                authorField.requestFocusInWindow();
                authorField.selectAll();
                return;
            }

            // REQUIRES: phrase has a non-zero length
            // MODIFIES: Quote and Library
            // EFFECTS: User input is captured for phrase and author;
            //          phrase and author are used to populate a new Quote;
            //          the new Quote is added to the Library
            Quote newQuote = new Quote(phrase, author);
            try {
                // checkDuplicate() throws DuplicateException()
                checkDuplicate(newQuote);
                library.addQuote(newQuote);
                saveLibrary();
            } catch (DuplicateException exception) {
                out.println("Sorry! That quote already exists.");
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            String phraseAuthorString = phraseField.getText() + " ~ " + authorField.getText();
            listModel.insertElementAt(phraseAuthorString, index);

            //Reset the text field.
            phraseField.requestFocusInWindow();
            phraseField.setText("");
            authorField.requestFocusInWindow();
            authorField.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
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

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {

                removeButton.setEnabled(false);
                editButton.setEnabled(false);

            } else {

                removeButton.setEnabled(true);
                editButton.setEnabled(true);
            }
        }
    }

    protected static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Quote Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = null;
        try {
            newContentPane = new Swing();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            System.out.println("Saved your quotes to " + JSON_FILE_LOCATION);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_FILE_LOCATION);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads Library from file
    private void loadLibrary() {
        try {
            library = jsonReader.read();
            System.out.println("Loaded your quotes from " + JSON_FILE_LOCATION);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_FILE_LOCATION);
        }
    }

}
