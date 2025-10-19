# Introduction

`gpw` is java library to generate "pronounceable" passwords. The code is 
taken from the java implementation mentioned in @GPW_HTML@ page.
The original source for the java applet is @GPW_CODE@.
The code is extracted from the source and created this maven project.

The original author of the code describes gpw in the comment at the top of 
@GPW_CODE@ as follows:
```bash
    /* GPW - Generate pronounceable passwords
     This program uses statistics on the frequency of three-letter sequences
     in English to generate passwords.  The statistics are
     generated from your dictionary by the program loadtris.

    See www.multicians.org/thvv/gpw.html for history and info.
    Tom Van Vleck

    THVV 06/01/94 Coded
    THVV 04/14/96 converted to Java
    THVV 07/30/97 fixed for Netscape 4.0
    */
```
# Changes

The following changes and additions are made to the original java code:

- Converted to a maven project from java applet code
- Created unit tests
- Uses `SecureRandom()` instead of `Random()`
- The following APIs are added

  ```
  public List<String> generatePasswords(final int numberOfPasswords,
      final int passwordLength)

  public List<String> generatePassphrases(final int numberOfPassphrases,
      final int numberOfWords, final int wordLength)

  public static String modifyPassword(final String password,
    boolean capitalize,
    boolean numerals,
    boolean symbols)

  public static String modifyPassword(final String password,
    boolean capitalize,
    boolean numerals,
    boolean symbols)

  ```

# How to build

```
    mvn clean install
    mvn clean package
    mvn test
```

# How to use

## Maven projects

The project is not in maven central yet. But it can be installed to your local
maven repository. Please look at the script 
[install_as_local_maven.sh](install_as_local_maven.sh) Use
this script if you are on Linux or Mac. If you are on Windows, look at the
script and run `mvn install:install-file` manually.

Then addd the following dependency in your project's pom.xml

```
   <dependency>
        <groupId>com.muquit.gpw</groupId>
        <artifactId>gpw</artifactId>
        <version>1.0.2</version>
   </dependency>
```
For non-maven projects, the gpw-1.0.2.jar jar file is also available from the 
@RELEASES@ page

# Examples

Please look at 
@TEST_GPW@ unit test file for complete examples.

```
    import com.muquit.gpw;

    Gpw gpw = new Gpw();

    int npw = 4;
    int pwlen = 8;
    List<String> passwords = gpw.generatePasswords(npw, pwlen);
    for (String password: passwords)
    {
        logger.info(password);
        String modified = GpwPasswordModifier.modifyPassword(password, true, true, true);
        logger.info(" modified all: " + modified);

        modified = GpwPasswordModifier.modifyPassword(password, true, false, false);
        logger.info(" modified caps: " + modified);
    }
```

Add at lease one upper case letter. The number depends on the length of the password.

```
    final String original = "password";

    boolean capitalize = true;
    boolean numerals = false;
    boolean symbols = false;

    String modified = GpwPasswordModifier.modifyPassword(original, true, false, false);
    logger.info("Original: " + original + " Modified caps: " + modified);
```

Similarly numbers, symbols can be added to the password.

# License

Lice is mit please look at @LICENSE@ for details
