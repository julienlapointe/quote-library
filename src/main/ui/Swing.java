package ui;

import exceptions.DuplicateException;
import model.Library;
import model.Quote;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
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

    private static final String addString = "Add";
    private static final String removeString = "Remove";
    private static final String editString = "Edit";
    private static final String saveString = "Save";
    private static final String loadString = "Load";

    private JButton addButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton saveButton;
    private JButton loadButton;

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

        addButton = new JButton(addString);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());

        editButton = new JButton(editString);
        editButton.setActionCommand(editString);
        editButton.addActionListener(new EditListener());

        phraseField = new JTextField(20);
        phraseField.addActionListener(addListener);
        phraseField.getDocument().addDocumentListener(addListener);
        String name = listModel.getElementAt(
                list.getSelectedIndex()).toString();

        authorField = new JTextField(20);
        authorField.addActionListener(addListener);
        authorField.getDocument().addDocumentListener(addListener);
        String author = listModel.getElementAt(
                list.getSelectedIndex()).toString();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(editButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(phraseField);
        buttonPane.add(authorField);
        buttonPane.add(addButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

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

            } else {

                removeButton.setEnabled(true);
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
