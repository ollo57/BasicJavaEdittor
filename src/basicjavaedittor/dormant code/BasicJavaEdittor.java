package basicjavaedittor;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * BasicJavaEditor.java
 * A simple graphical text editor built using Java Swing.
 * Includes methods for open, save, save as, copy, and paste.
 */
public class BasicJavaEdittor extends JFrame implements ActionListener {

    // --- GUI Components ---
    private JTextArea textArea;
    private JFileChooser fileChooser;

    // --- State Variables ---
    // Stores the path of the currently open/saved file. Null if new file.
    private File currentFile = null;
    private final String DEFAULT_TITLE = "Basic Java Editor - Untitled";


    /**
     * Constructor sets up the main window and menu.
     */
    public BasicJavaEdittor() {
        // Set up the main frame
        setTitle(DEFAULT_TITLE);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        textArea = new JTextArea();
        // Add text area to a scroll pane so text can scroll if it exceeds bounds
        add(new JScrollPane(textArea));
        
        // Initialize the file chooser
        fileChooser = new JFileChooser();

        // Set up the menu bar
        setupMenuBar();

        // Make the window visible
        setVisible(true);
    }

    /**
     * Sets up the JMenuBar with File and Edit menus.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // --- File Menu ---
        JMenu fileMenu = new JMenu("File");
        
        // Open (Added for complete editor functionality)
        JMenuItem openItem = new JMenuItem("Open...");
        openItem.addActionListener(this);
        openItem.setActionCommand("Open");
        fileMenu.add(openItem);

        // Save (Required)
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(this);
        saveItem.setActionCommand("Save");
        fileMenu.add(saveItem);

        // Save As (Required)
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.addActionListener(this);
        saveAsItem.setActionCommand("Save As");
        fileMenu.add(saveAsItem);
        
        fileMenu.addSeparator();
        
        // Exit
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        exitItem.setActionCommand("Exit");
        fileMenu.add(exitItem);


        // --- Edit Menu ---
        JMenu editMenu = new JMenu("Edit");

        // Copy (Required)
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(this);
        copyItem.setActionCommand("Copy");
        editMenu.add(copyItem);

        // Paste (Required)
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(this);
        pasteItem.setActionCommand("Paste");
        editMenu.add(pasteItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Set the menu bar to the frame
        setJMenuBar(menuBar);
    }

    /**
     * Handles all menu item actions.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "Save As":
                saveFileAs();
                break;
            case "Copy":
                textArea.copy(); // Standard JTextArea method handles clipboard copy
                break;
            case "Paste":
                textArea.paste(); // Standard JTextArea method handles clipboard paste
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    /**
     * Opens a file selected via JFileChooser and loads its content into the JTextArea.
     */
    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.setText(null); // Clear existing text
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                setTitle(DEFAULT_TITLE + " - " + currentFile.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not open file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Saves the current content to the currently tracked file path.
     * If no file is tracked, it calls saveFileAs(). (Required method)
     */
    private void saveFile() {
        if (currentFile != null) {
            writeFile(currentFile);
        } else {
            // If it's a new file, prompt for location (Save As functionality)
            saveFileAs();
        }
    }

    /**
     * Prompts the user for a new file location and saves the content there. (Required method)
     */
    private void saveFileAs() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Optionally add .txt extension if missing
            if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            currentFile = selectedFile; // Update the current file path
            writeFile(currentFile);
        }
    }

    /**
     * Helper method to write the JTextArea content to a specified file.
     * @param file The target File object to write to.
     */
    private void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Use textArea.getText() to get all content
            writer.write(textArea.getText());
            writer.flush();
            setTitle(DEFAULT_TITLE + " - " + file.getName());
            JOptionPane.showMessageDialog(this,
                    "File saved successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Main method to run the application.
     */
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (Swing standard practice)
        SwingUtilities.invokeLater(() -> new BasicJavaEdittor());
    }
}
