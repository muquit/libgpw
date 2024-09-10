package test.com.muquit.gpw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.muquit.gpw.Gpw;
import com.muquit.gpw.GpwPasswordModifier;

public class TestGpw
{
	private final static Logger logger = LoggerFactory.getLogger(TestGpw.class);
	
	@Test
	public void testGeneratePassword()
	{
		Gpw gpw = new Gpw();
		logger.info("Test generating passwords");
		List<String> passwords = gpw.generatePasswords(4, 8);
		assertEquals(4, passwords.size());
		for (String password : passwords)
		{
			logger.info(password);
			assertEquals(8, password.length());
			assertTrue(! containsUppercase(password));
			assertTrue(! containsNumeral(password));
			assertTrue(! containsSymbol(password));
		}
	}

	@Test
	public void testGeneratePassPhrases()
	{
		Gpw gpw = new Gpw();
		logger.info("Test generating passphrases");
		List<String> pps = gpw.generatePassphrases(4, 5, 7);
		assertEquals(4, pps.size());
		for (String p : pps)
		{
			logger.info(p);
			assertTrue(! containsUppercase(p));
			assertTrue(! containsNumeral(p));
			assertTrue(! containsSymbol(p));
		}
	}

	@Test
	public void testModifyPassword_AllOptions()
	{
		String original = "password";
		for (int i=0; i < 100; i++)
		{
			String modified = GpwPasswordModifier.modifyPassword(original, true, true, true);
			logger.info("original=" + original + " Modified: " + modified);
			assertNotEquals(original, modified);
			assertTrue(containsUppercase(modified));
			assertTrue(containsNumeral(modified));
			assertTrue(containsSymbol(modified));
			assertEquals(original.length(), modified.length());
		}
	}
	
    @Test
    public void testModifyPassword_OnlyCapitalize()
    {
        String original = "password";
        String modified = GpwPasswordModifier.modifyPassword(original, true, false, false);

        assertNotEquals(original, modified);
        assertTrue(containsUppercase(modified));
        assertFalse(containsNumeral(modified));
        assertFalse(containsSymbol(modified));
        assertEquals(original.length(), modified.length());
    }	
    
    @Test
    public void testModifyPassword_NumeralsAndSymbols() {
        String original = "password";
        String modified = GpwPasswordModifier.modifyPassword(original, false, true, true);

        assertNotEquals(original, modified);
        assertFalse(containsUppercase(modified));
        assertTrue(containsNumeral(modified));
        assertTrue(containsSymbol(modified));
        assertEquals(original.length(), modified.length());
    }    

    @Test
    public void testModifyPassword_AllCaps()
    {
        String original = "ALLCAPS";
        String modified = GpwPasswordModifier.modifyPassword(original, true, true, true);

        assertNotEquals(original, modified);
        assertTrue(containsUppercase(modified));
        assertTrue(containsNumeral(modified));
        assertTrue(containsSymbol(modified));
        assertEquals(original.length(), modified.length());
    }    
    
    @Test
    public void testModifyPassword_AlreadyMeetsRequirements()
    {
        String original = "P@ssw0rd";
        String modified = GpwPasswordModifier.modifyPassword(original, true, true, true);
        
        logger.info("Original: " + original);
        logger.info("Modified: " + modified);

//        assertEquals(original, modified);
    }    
    
    @Test
    public void testCalculateRequiredElements()
    {
    	String p = "nkamentittatapelecomeldenolleddo";
    	int n = GpwPasswordModifier.calculateRequiredElements(p.length(), true);
    	logger.info("plan: " + p.length() +  " n: " + n);
		StringBuilder pass = new StringBuilder(p);
		GpwPasswordModifier.addRequiredCapitals(pass, n);
		logger.info("pass: " + pass.toString());

		pass = new StringBuilder(p);
	    GpwPasswordModifier.addRequiredNumerals(pass,n);
		logger.info("pass: " + pass.toString());

		pass = new StringBuilder(p);
	    GpwPasswordModifier.addRequiredSymbols(pass,n);
		logger.info("pass: " + pass.toString());
    }

	private boolean containsUppercase(String str)
	{
		return !str.equals(str.toLowerCase());
	}

	private boolean containsNumeral(String str)
	{
		return str.matches(".*\\d.*");
	}

	private boolean containsSymbol(String str)
	{
		return str.matches(".*[!@#$%^&*()+].*");
	}

}
