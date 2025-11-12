package gpwgui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.muquit.gpw.Gpw;
import com.muquit.gpw.GpwPasswordModifier;

public class GpwGui extends JFrame
{
    private JSpinner countSpinner;
    private JSpinner lengthSpinner;
    private JCheckBox capitalizeCheckBox;
    private JCheckBox digitsCheckBox;
    private JCheckBox symbolsCheckBox;
    private JCheckBox spellCheckBox;
    private JTextArea resultTextArea;
    private JButton generateButton;
    private JButton copyButton;
    private JButton clearButton;
    
    private static final Map<Character, String> NATO_ALPHABET = new HashMap<>();
    
    static
    {
        // NATO phonetic alphabet for lowercase letters
        NATO_ALPHABET.put('a', "Alpha");
        NATO_ALPHABET.put('b', "Bravo");
        NATO_ALPHABET.put('c', "Charlie");
        NATO_ALPHABET.put('d', "Delta");
        NATO_ALPHABET.put('e', "Echo");
        NATO_ALPHABET.put('f', "Foxtrot");
        NATO_ALPHABET.put('g', "Golf");
        NATO_ALPHABET.put('h', "Hotel");
        NATO_ALPHABET.put('i', "India");
        NATO_ALPHABET.put('j', "Juliet");
        NATO_ALPHABET.put('k', "Kilo");
        NATO_ALPHABET.put('l', "Lima");
        NATO_ALPHABET.put('m', "Mike");
        NATO_ALPHABET.put('n', "November");
        NATO_ALPHABET.put('o', "Oscar");
        NATO_ALPHABET.put('p', "Papa");
        NATO_ALPHABET.put('q', "Quebec");
        NATO_ALPHABET.put('r', "Romeo");
        NATO_ALPHABET.put('s', "Sierra");
        NATO_ALPHABET.put('t', "Tango");
        NATO_ALPHABET.put('u', "Uniform");
        NATO_ALPHABET.put('v', "Victor");
        NATO_ALPHABET.put('w', "Whiskey");
        NATO_ALPHABET.put('x', "Xray");
        NATO_ALPHABET.put('y', "Yankee");
        NATO_ALPHABET.put('z', "Zulu");
        
        // Uppercase letters (same as lowercase)
        NATO_ALPHABET.put('A', "Alpha");
        NATO_ALPHABET.put('B', "Bravo");
        NATO_ALPHABET.put('C', "Charlie");
        NATO_ALPHABET.put('D', "Delta");
        NATO_ALPHABET.put('E', "Echo");
        NATO_ALPHABET.put('F', "Foxtrot");
        NATO_ALPHABET.put('G', "Golf");
        NATO_ALPHABET.put('H', "Hotel");
        NATO_ALPHABET.put('I', "India");
        NATO_ALPHABET.put('J', "Juliet");
        NATO_ALPHABET.put('K', "Kilo");
        NATO_ALPHABET.put('L', "Lima");
        NATO_ALPHABET.put('M', "Mike");
        NATO_ALPHABET.put('N', "November");
        NATO_ALPHABET.put('O', "Oscar");
        NATO_ALPHABET.put('P', "Papa");
        NATO_ALPHABET.put('Q', "Quebec");
        NATO_ALPHABET.put('R', "Romeo");
        NATO_ALPHABET.put('S', "Sierra");
        NATO_ALPHABET.put('T', "Tango");
        NATO_ALPHABET.put('U', "Uniform");
        NATO_ALPHABET.put('V', "Victor");
        NATO_ALPHABET.put('W', "Whiskey");
        NATO_ALPHABET.put('X', "Xray");
        NATO_ALPHABET.put('Y', "Yankee");
        NATO_ALPHABET.put('Z', "Zulu");
        
        // Numbers
        NATO_ALPHABET.put('0', "Zero");
        NATO_ALPHABET.put('1', "One");
        NATO_ALPHABET.put('2', "Two");
        NATO_ALPHABET.put('3', "Three");
        NATO_ALPHABET.put('4', "Four");
        NATO_ALPHABET.put('5', "Five");
        NATO_ALPHABET.put('6', "Six");
        NATO_ALPHABET.put('7', "Seven");
        NATO_ALPHABET.put('8', "Eight");
        NATO_ALPHABET.put('9', "Nine");
    }
    
    public GpwGui()
    {
        setTitle("GPW - Pronounceable Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null); // Center on screen
        
        // Set system look and feel
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // Setup About handler for macOS and other platforms
        setupAboutHandler();
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showAboutDialog();
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        
        // Spinner panel
        JPanel spinnerPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        
        // Number of passwords
        spinnerPanel.add(new JLabel("Number of passwords:"));
        countSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
        spinnerPanel.add(countSpinner);
        
        // Password length
        spinnerPanel.add(new JLabel("Password length:"));
        lengthSpinner = new JSpinner(new SpinnerNumberModel(8, 6, 32, 1));
        spinnerPanel.add(lengthSpinner);
        
        controlPanel.add(spinnerPanel);
        controlPanel.add(Box.createVerticalStrut(10));
        
        // Checkbox panel
        JPanel checkBoxPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        capitalizeCheckBox = new JCheckBox("Capitalize");
        digitsCheckBox = new JCheckBox("Digits");
        symbolsCheckBox = new JCheckBox("Symbols");
        spellCheckBox = new JCheckBox("Spell (NATO phonetic)");
        
        checkBoxPanel.add(capitalizeCheckBox);
        checkBoxPanel.add(digitsCheckBox);
        checkBoxPanel.add(symbolsCheckBox);
        checkBoxPanel.add(spellCheckBox);
        
        controlPanel.add(checkBoxPanel);
        
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        
        // Center panel for results
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Generated Passwords"));
        
        resultTextArea = new JTextArea();
        resultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(resultPanel, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        generateButton = new JButton("Generate");
        copyButton = new JButton("Copy All");
        clearButton = new JButton("Clear");
        
        generateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                generatePasswords();
            }
        });
        
        copyButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                copyToClipboard();
            }
        });
        
        clearButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                resultTextArea.setText("");
            }
        });
        
        buttonPanel.add(generateButton);
        buttonPanel.add(copyButton);
        buttonPanel.add(clearButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void generatePasswords()
    {
        int count = (Integer) countSpinner.getValue();
        int length = (Integer) lengthSpinner.getValue();
        boolean capitalize = capitalizeCheckBox.isSelected();
        boolean digits = digitsCheckBox.isSelected();
        boolean symbols = symbolsCheckBox.isSelected();
        boolean spell = spellCheckBox.isSelected();
        
        Gpw gpw = new Gpw();
        List<String> passwords = gpw.generatePasswords(count, length);
        
        StringBuilder result = new StringBuilder();
        
        for (String password : passwords)
        {
            // Apply modifications if any flags are set
            if (capitalize || digits || symbols)
            {
                password = GpwPasswordModifier.modifyPassword(password, capitalize, digits, symbols);
            }
            
            if (spell)
            {
                String spelling = spellPassword(password);
                result.append(password).append(" ").append(spelling).append("\n");
            }
            else
            {
                result.append(password).append("\n");
            }
        }
        
        resultTextArea.setText(result.toString());
    }
    
    private String spellPassword(String password)
    {
        StringBuilder spelling = new StringBuilder();
        
        for (char c : password.toCharArray())
        {
            String natoWord = NATO_ALPHABET.get(c);
            if (natoWord != null)
            {
                spelling.append(natoWord);
            }
            else
            {
                // Fallback for any character not in the map (e.g., symbols)
                spelling.append(c);
            }
        }
        
        return spelling.toString();
    }
    
    private void copyToClipboard()
    {
        String text = resultTextArea.getText();
        if (text != null && !text.isEmpty())
        {
            StringSelection stringSelection = new StringSelection(text);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }
    
    private void setupAboutHandler()
    {
        // Setup About handler for macOS (and other platforms that support it)
        if (Desktop.isDesktopSupported())
        {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.APP_ABOUT))
            {
                desktop.setAboutHandler(e -> showAboutDialog());
            }
        }
    }
    
    private void showAboutDialog()
    {
        String message = "GPW - Pronounceable Password Generator\n" +
                         "Version 1.0.2\n\n" +
                         "https://github.com/muquit/gpw";
        
        JOptionPane.showMessageDialog(this,
                                     message,
                                     "About GPW",
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args)
    {
        // Set macOS-specific properties for app menu
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "GPW");
        System.setProperty("apple.awt.application.name", "GPW");
        
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                GpwGui gui = new GpwGui();
                gui.setVisible(true);
            }
        });
    }
}