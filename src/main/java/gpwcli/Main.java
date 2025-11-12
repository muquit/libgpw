package gpwcli;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.muquit.gpw.Gpw;
import com.muquit.gpw.GpwPasswordModifier;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "gpw-cli", 
         mixinStandardHelpOptions = true, 
         version = "gpw-cli 1.0",
         description = "Generate pronounceable passwords")
public class Main implements Callable<Integer>
{
    @Option(names = {"-n", "--number"}, 
            description = "Number of passwords to generate (default: ${DEFAULT-VALUE})",
            defaultValue = "5")
    private int numberOfPasswords;

    @Option(names = {"-l", "--length"}, 
            description = "Password length (default: ${DEFAULT-VALUE})",
            defaultValue = "8")
    private int passwordLength;

    @Option(names = {"-s", "--spell"}, 
            description = "Spell out password using NATO phonetic alphabet")
    private boolean spellOut;

    @Option(names = {"-c", "--capitalize"}, 
            description = "Add uppercase letters")
    private boolean capitalize;

    @Option(names = {"-d", "--digits"}, 
            description = "Add numerals")
    private boolean addDigits;

    @Option(names = {"-y", "--symbols"}, 
            description = "Add symbols")
    private boolean addSymbols;

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

    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception
    {
        Gpw gpw = new Gpw();
        List<String> passwords = gpw.generatePasswords(numberOfPasswords, passwordLength);
        
        for (String password : passwords)
        {
            // Apply modifications if any flags are set
            if (capitalize || addDigits || addSymbols)
            {
                password = GpwPasswordModifier.modifyPassword(password, capitalize, addDigits, addSymbols);
            }
            
            if (spellOut)
            {
                String spelling = spellPassword(password);
                System.out.println(password + " " + spelling);
            }
            else
            {
                System.out.println(password);
            }
        }
        
        return 0;
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
                // Fallback for any character not in the map
                spelling.append(c);
            }
        }
        
        return spelling.toString();
    }
}