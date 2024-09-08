package com.muquit.gpw;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Add uppercase, numbers, symbols to a word
 * @author muquit@muquit.com - Sep 8, 2024
 */
public class GpwPasswordModifier
{
	private final static Logger logger = LoggerFactory.getLogger(GpwPasswordModifier.class);
	// use a mostly used subset
	private static final String SYMBOLS = "!@#$%^&*()+";

	private static final SecureRandom random = new SecureRandom();
	private static final int BASE_LENGTH = 8;
	private static final double MAX_ELEMENT_PERCENTAGE = 0.25;
	private static final int MAX_ELEMENT_COUNT = 5;

	/**
	 * Modify a password to capitalize, add numbers, symbols
	 * 
	 * @param password
	 * @param capitalize 
	 * @param numerals
	 * @param symbols
	 * @return The modifield password
	 * <p>
	 * @author muquit@muquit.com - Sep 8, 2024
	 */
	public static String modifyPassword(final String password, boolean capitalize, boolean numerals, boolean symbols)
	{
		StringBuilder modifiedPassword = new StringBuilder(password);
		int passwordLength = password.length();

		int requiredCapitals = calculateRequiredElements(passwordLength, capitalize);
		int requiredNumerals = calculateRequiredElements(passwordLength, numerals);
		int requiredSymbols = calculateRequiredElements(passwordLength, symbols);

		addRequiredCapitals(modifiedPassword, requiredCapitals);
		addRequiredNumerals(modifiedPassword, requiredNumerals);
		addRequiredSymbols(modifiedPassword, requiredSymbols);

		ensureRequirements(modifiedPassword, capitalize, numerals, symbols);

		return modifiedPassword.toString();
	}

	private static int calculateRequiredElements(int passwordLength, boolean optionEnabled)
	{
		if (!optionEnabled)
			return 0;

		int baseCount = 1;
		if (passwordLength > BASE_LENGTH)
		{
			double complexityFactor = (double) (passwordLength - BASE_LENGTH) / BASE_LENGTH;
			int additionalCount = (int) Math.floor(complexityFactor);
			int totalCount = baseCount + additionalCount;

			int maxCount = (int) Math.min(passwordLength * MAX_ELEMENT_PERCENTAGE, MAX_ELEMENT_COUNT);
			return Math.min(totalCount, maxCount);
		}
		return baseCount;
	}

	private static boolean containsUppercase(String str)
	{
		return !str.equals(str.toLowerCase());
	}

	private static boolean containsNumeral(String str)
	{
		return str.matches(".*\\d.*");
	}

	private static boolean containsSymbol(String str)
	{
		return Pattern.compile(".*[" + Pattern.quote(SYMBOLS) + "].*").matcher(str).matches();
	}

	private static void addRequiredCapitals(StringBuilder password, int count)
	{
		if (count > 0 && !containsUppercase(password.toString()))
		{
			int position = findAvailablePosition(password, true);
			if (position != -1)
			{
				password.setCharAt(position, Character.toUpperCase(password.charAt(position)));
			} else
			{
				// if no lowercase letter is found, capitalize the first letter
				for (int i = 0; i < password.length(); i++)
				{
					if (Character.isLetter(password.charAt(i)))
					{
						password.setCharAt(i, Character.toUpperCase(password.charAt(i)));
						break;
					}
				}
			}
		}
	}

	private static void addRequiredNumerals(StringBuilder password, int count)
	{
		for (int i = 0; i < count; i++)
		{
			if (!containsNumeral(password.toString()))
			{
				int position = findAvailablePosition(password, false);
				if (position != -1)
				{
					char numeral = (char) ('0' + random.nextInt(10));
					password.setCharAt(position, numeral);
				} else
				{
					System.out.println("Debug: No available position for adding numeral");
					break;
				}
			}
		}
	}

	private static void addRequiredSymbols(StringBuilder password, int count)
	{
		for (int i = 0; i < count; i++)
		{
			if (!containsSymbol(password.toString()))
			{
				int position = findAvailablePosition(password, false);
				if (position != -1)
				{
					char symbol = SYMBOLS.charAt(random.nextInt(SYMBOLS.length()));
					password.setCharAt(position, symbol);
				} else
				{
					System.out.println("Debug: No available position for adding symbol");
					break;
				}
			}
		}
	}

	private static int findAvailablePosition(StringBuilder password, boolean forCapitalization)
	{
		int attempts = 0;
		int maxAttempts = password.length() * 2; // Arbitrary limit to prevent
													// infinite loop
		while (attempts < maxAttempts)
		{
			int position = random.nextInt(password.length());
			if (isPositionAvailable(password, position, forCapitalization))
			{
				return position;
			}
			attempts++;
		}
		return -1; // Couldn't find an available position
	}

	private static boolean isPositionAvailable(StringBuilder password, int position, boolean forCapitalization)
	{
		char c = password.charAt(position);
		if (forCapitalization)
		{
			return Character.isLowerCase(c);
		} else
		{
			return Character.isLetter(c); // Allow replacing any letter with a
											// number or symbol
		}
	}

	private static void ensureRequirements(StringBuilder password, boolean capitalize, boolean numerals,
			boolean symbols)
	{
		if (capitalize && !containsUppercase(password.toString()))
		{
			forceCapitalization(password);
		}
		if (numerals && !containsNumeral(password.toString()))
		{
			forceNumeral(password);
		}
		if (symbols && !containsSymbol(password.toString()))
		{
			forceSymbol(password);
		}
	}

	private static void forceCapitalization(StringBuilder password)
	{
		for (int i = 0; i < password.length(); i++)
		{
			if (Character.isLowerCase(password.charAt(i)))
			{
				password.setCharAt(i, Character.toUpperCase(password.charAt(i)));
				return;
			}
		}
	}

	private static void forceNumeral(StringBuilder password)
	{
		int position = random.nextInt(password.length());
		password.setCharAt(position, (char) ('0' + random.nextInt(10)));
	}

	private static void forceSymbol(StringBuilder password)
	{
		int position = random.nextInt(password.length());
		password.setCharAt(position, SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
	}
}