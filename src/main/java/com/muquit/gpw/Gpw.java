package com.muquit.gpw;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gpw
{
	private final static Logger logger = LoggerFactory.getLogger(Gpw.class);
	/**
	 * Generate a list of passphrases
	 * @param npp
	 * @param nWords
	 * @param wordLen
	 * @return list of passphrases
	 */
	public List<String> generatePassphrases(final int numberOfPassphrases,
			final int numberOfWords,
			final int wordLen)
	{
		List<String> passphrasesList = new ArrayList<String>();
		for (int i = 0; i < numberOfPassphrases; i++)
		{
			List<String> words = generatePasswords(numberOfWords, wordLen);
			StringBuilder sb = new StringBuilder();
			for (String word : words)
			{
				if (sb.length() > 0)
				{
					sb.append(" ");
				}
				sb.append(word);
			}
			String result = sb.toString();
			passphrasesList.add(result);
		}
		return passphrasesList;
	}

	public List<String> generatePasswords(final int npw, final int pwl)
	{
		final String alphabet = "abcdefghijklmnopqrstuvwxyz";
		int c1, c2, c3;
		long sum = 0;
		int nchar;
		long ranno;
		int pwnum;
		double pik;

		GpwData data = new GpwData();
		StringBuffer password;
		// Random ran = new Random(); // new random source seeded by clock
		SecureRandom sran = new SecureRandom();
		List<String> passwordList = new ArrayList<String>();

		// Pick a random starting point.
		for (pwnum = 0; pwnum < npw; pwnum++)
		{
			password = new StringBuffer(pwl);
			pik = sran.nextDouble(); // random number [0,1]
			ranno = (long) (pik * data.getSigma()); // weight by sum of
													// frequencies
			sum = 0;
			for (c1 = 0; c1 < 26; c1++)
			{
				for (c2 = 0; c2 < 26; c2++)
				{
					for (c3 = 0; c3 < 26; c3++)
					{
						sum += data.get(c1, c2, c3);
						if (sum > ranno)
						{
							password.append(alphabet.charAt(c1));
							password.append(alphabet.charAt(c2));
							password.append(alphabet.charAt(c3));
							c1 = 26; // Found start. Break all 3 loops.
							c2 = 26;
							c3 = 26;
						} // if sum
					} // for c3
				} // for c2
			} // for c1

			// Now do a random walk.
			nchar = 3;
			while (nchar < pwl)
			{
				c1 = alphabet.indexOf(password.charAt(nchar - 2));
				c2 = alphabet.indexOf(password.charAt(nchar - 1));
				sum = 0;
				for (c3 = 0; c3 < 26; c3++)
					sum += data.get(c1, c2, c3);
				if (sum == 0)
				{
					break; // exit while loop
				}
				pik = sran.nextDouble();
				ranno = (long) (pik * sum);
				sum = 0;
				for (c3 = 0; c3 < 26; c3++)
				{
					sum += data.get(c1, c2, c3);
					if (sum > ranno)
					{
						password.append(alphabet.charAt(c3));
						c3 = 26; // break for loop
					} // if sum
				} // for c3
				nchar++;
			} // while nchar
			passwordList.add(password.toString());
			// pan.add(new Label(password.toString())); // Password generated
		} // for pwnum
		return passwordList;
	}
	
}
