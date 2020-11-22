package ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import events.*;

public class Panels extends JPanel
                    implements ListSelectionListener {

    // List, TextField, and Button Fields
    protected JList list;
    protected DefaultListModel listModel;

    protected JTextField phraseField;
    protected JTextField authorField;

    protected JButton addButton;
    protected JButton removeButton;
    protected JButton editButton;
    protected JButton saveButton;
    protected JButton loadButton;

    protected static final String addString = "Add";
    protected static final String removeString = "Remove";
    protected static final String editString = "Edit";
    protected static final String saveString = "Save";
    protected static final String loadString = "Load";

//    Icon logoIcon = new ImageIcon(getClass().getResource("/Logo.gif"));
//    Icon addIcon = new ImageIcon(getClass().getResource("/Add.gif"));
//    Icon removeIcon = new ImageIcon(getClass().getResource("/Remove.gif"));
//    Icon editIcon = new ImageIcon(getClass().getResource("/Edit.gif"));
//    Icon saveIcon = new ImageIcon(getClass().getResource("/Save.gif"));
//    Icon loadIcon = new ImageIcon(getClass().getResource("/Load.gif"));

    // String Manipulation Fields
    protected String phrase = "";
    protected String author = "";
    protected String newQuote = "";
    protected String editedQuote = "";

    // Constructor
    // EFFECTS: Library, JsonWriter, JsonReader, and DefaultListModel are initialized;
    //          button panels are added to Swing UI using BorderLayout
    public Panels() {
        super(new BorderLayout());

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
        listModel.addElement("First dummy quote. ~ Anonymous");
        listModel.addElement("Second dummy quote. ~ Anonymous");
        listModel.addElement("Third dummy quote. ~ Anonymous");
    }

    // =====================
    // PANELS AND COMPONENTS
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
        JLabel appLogoLabel = new JLabel();
//        JLabel appLogoLabel = new JLabel(logoIcon);
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

        addButton = new JButton(addString);
//        addButton = new JButton(addString, addIcon);
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
        editButton = new JButton(editString);
//        editButton = new JButton(editString, editIcon);
        editButton.setActionCommand(editString);
        editButton.addActionListener(new EditListener());

        removeButton = new JButton(removeString);
//        removeButton = new JButton(removeString, removeIcon);
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
        saveButton = new JButton(saveString);
//        saveButton = new JButton(saveString, saveIcon);
        saveButton.setActionCommand(saveString);
        saveButton.addActionListener(new SaveListener());

        loadButton = new JButton(loadString);
//        loadButton = new JButton(loadString, loadIcon);
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

    // Required by ListSelectionListener
    // MODIFIES: removeButton, editButton
    @Override
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
}
