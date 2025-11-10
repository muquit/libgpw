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
```    
```
  public List<String> generatePassphrases(final int numberOfPassphrases,
      final int numberOfWords, final int wordLength)
```
```
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

The jar file `gpw-1.0.2.jar` will be created in `./target` directory

# How to use

## Maven projects

The project is not in maven central yet. But it can be installed to your local
maven repository.  To install it to your local maven repo, do the following:

```bash
mvn install:install-file \
   -Dfile=./target/gpw-1.0.2.jar \
   -DgroupId=com.muquit.gpw \
   -DartifactId=gpw \
   -Dversion=1.0.2 \
   -Dpackaging=jar \
   -DgeneratePom=true
```
Then addd the following dependency in your project's pom.xml

```
   <dependency>
        <groupId>com.muquit.gpw</groupId>
        <artifactId>gpw</artifactId>
        <version>1.0.2</version>
   </dependency>
```
For non-maven projects, the `gpw-1.0.2.jar` jar file is available from the 
@RELEASES@ page.

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

```bash
MIT License

Copyright (c) 2025 Muhammad Muquit

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

This project incorporates work covered by the following copyright and 
permission notice:

Random Password Generator by Tom Van Vleck

As requested by the original author: 
1. Users of this software are encouraged to share their source freely.
2. Please inform the original author if you are using this software.
3. Credit should be given to the original author and other pioneers for 
the data and algorithms used.

For full attribution and usage requirements, please see the @NOTICE@
file in this repository.

