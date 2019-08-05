package edu.gatech.seclass.textsummary;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/*
This is a Georgia Tech provided code example for use in assigned private GT repositories. Students and other users of this template
code are advised not to share it with other students or to make it available on publicly viewable websites including
repositories such as github and gitlab.  Such sharing may be investigated as a GT honor code violation. Created for CS6300.
 */

/*
DO NOT ALTER THIS CLASS.  Use it as an example for MyMainTest.java
*/


public class MainTest {

    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private Charset charset = StandardCharsets.UTF_8;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    /*
    *  TEST UTILITIES
    */

    // Create File Utility
    private File createTmpFile() throws Exception {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Write File Utility
    private File createInputFile(String input) throws Exception {
        File file =  createTmpFile();

        OutputStreamWriter fileWriter =
                     new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        fileWriter.write(input);

        fileWriter.close();
        return file;
    }


    //Read File Utility
    private String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
	
	/*
	* TEST FILE CONTENT
	*/
	private static final String FILE1 = "3 dogs" + System.lineSeparator() + "a cat";
    private static final String FILE2 = "dog bird cat cat" + System.lineSeparator() + "cat dog fish";
	private static final String FILE3 = "dog bird cat cat" + System.lineSeparator() + "cat dog fish cat" 
			+ System.lineSeparator() + "dog" + System.lineSeparator() + "bird fish";
	private static final String FILE4 = "Log: 123 abc" + System.lineSeparator() + 
	"Error: 456123123 123 xyz" + System.lineSeparator() + "Log: 456 cde" + System.lineSeparator() + 
	"Log: 1231 cde" + System.lineSeparator() + "Error: 123123 ab c";
    private static final String FILE5 = "Log: 123 abc" + System.lineSeparator() +
            "Error: 456123123 123 xyz" + System.lineSeparator() + "Log: 456 cde" + System.lineSeparator() +
            "Log: 1231 cde" + System.lineSeparator() + "Error: 123123 ab c"+ System.lineSeparator() +
            "Error: 456123 123 xyz"+ System.lineSeparator() + "Log: 123 cde"+ System.lineSeparator() +
            "Error: 456 123 qrz"+ System.lineSeparator() +
            "Error: 123 123 xyz"+ System.lineSeparator() + "Log: 456 cde"+ System.lineSeparator() + "Log: 456 cde"+
            System.lineSeparator() + "Log: 123 cde"+ System.lineSeparator() + "Log: 456 jkl";
    private static final String FILE6 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String FILE7 = "Let's try some **special**  %!(characters)!% ###\n" +
            "and line breaks^$@ \r" +
            "of \\different// types; \n" +
            "in 1 file\r"+
            ":-)";
    private static final String FILE8 = "Up with the white and gold\r" +
            "Down with the red and black\r" +
            "Georgia Tech is out for a victory\r" +
            "We'll drop a battle axe on georgia's head\r" +
            "When we meet her our team is sure to beat her\r" +
            "Down on the old farm there will be no sound\r" +
            "'Till our bow wows rips through the air\r" +
            "When the battle is over georgia's team will be found\r" +
            "With the Yellow Jacket's swarming 'round! Hey!";
    private static final String FILE9 = ".*";
    private static final String FILE10 = "Howdy Billy," + System.lineSeparator() +
            "I am going to take cs6300 and cs6400 next semester."  + System.lineSeparator() +
            "Did you take cs 6300 last semester? I want to"  + System.lineSeparator() +
            "take 2 courses so that I will graduate Asap!";
    private static final String FILE11 = "\n";
    private static final String FILE12 = "Howdy Billy," + System.lineSeparator() + System.lineSeparator()+
            "I am going to take cs6300 and cs6400 next semester."  + System.lineSeparator() +
            "Did you take cs 6300 last semester? I want to"  + System.lineSeparator() + System.lineSeparator()+
            "take 2 courses so that I will graduate Asap!" + System.lineSeparator();
    private static final String FILE13 = "ABCDEF\nghijkl\rMNOPQR";



    

    /*
    *   TEST CASES
    */

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 1 from assignment directions
    @Test
    public void mainTest1() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {inputFile.getPath()};
        Main.main(args);

        String expected = FILE1;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("3 dogs", outStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 2 from assignment directions
    @Test
    public void mainTest2() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-d", "2", "-c", "d", inputFile.getPath()};
        Main.main(args);

        String expected = "2 dog bird cat cat" + System.lineSeparator() 
			+ "1 cat dog fish";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("cat 3 dog 2", outStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 3 from assignment directions
    @Test
    public void mainTest3() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-l", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "dog bird cat cat";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 4 from assignment directions
    @Test
    public void mainTest4() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-d", "-s", "2", inputFile.getPath()};
        Main.main(args);

        String expected = "dog" + System.lineSeparator() 
			+ "bird fish";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("cat 4", outStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Extra example
    @Test
    public void mainTest5() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-s", "1", "-d", "4", inputFile.getPath()};
        Main.main(args);

        String expected = "Log: 123 abc";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("Log 3 123 2 Error 2 cde 2", outStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Extra example
    @Test
    public void mainTest6() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-c", "123", "-d", inputFile.getPath()};
        Main.main(args);

        String expected = "1 Log: 123 abc" + System.lineSeparator() + 
			"3 Error: 456123123 123 xyz" + System.lineSeparator() + "0 Log: 456 cde" + System.lineSeparator() + 
			"1 Log: 1231 cde" + System.lineSeparator() + "2 Error: 123123 ab c";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
		assertEquals("Log 3", outStream.toString().trim());
    }


    // Purpose: To provide an example of a test case format (no arguments passed)
    // Frame #: Instructor error example
    @Test
    public void mainTest7() {
        //if no arguments are entered on the command line it will pass an array of length 0 to the application, not null.
        String args[]  = new String[0];
        Main.main(args);
        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>", errStream.toString().trim());
    }


    // Purpose: To provide an example of a test case format
    // Frame #: Extra example
    @Test
    public void mainTest8() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-c", "o", "-s", "2", inputFile.getPath()};
        Main.main(args);

        String expected = "1 Log: 123 abc" + System.lineSeparator() + "1 Log: 456 cde";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }


    // Purpose: To provide an example of a test case format
    // Frame #: Extra example
    @Test
    public void mainTest9() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-c", "*", "-d", "2", "-l", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "0 Error: 456123123 123 xyz";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("Log 3 123 2", outStream.toString().trim());
    }


    // Purpose: To provide an example of a test case format (error in options)
    // Frame #: Extra example
    @Test
    public void mainTest10() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-c", "log", "-u", "-s", "1", inputFile.getPath()};
        Main.main(args);
        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>", errStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest11() {

        String args[] = {"nosuchfile.txt"};
        Main.main(args);
        assertEquals("File Not Found", errStream.toString().trim());

    }
    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest12() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-d", "1", "-c", " ", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "2 Log: 123 abc" + System.lineSeparator() +
                "3 Error: 456123123 123 xyz" + System.lineSeparator() + "2 Log: 456 cde" + System.lineSeparator() +
                "2 Log: 1231 cde" + System.lineSeparator() + "3 Error: 123123 ab c"+ System.lineSeparator() +
                "3 Error: 456123 123 xyz"+ System.lineSeparator() + "2 Log: 123 cde"+ System.lineSeparator() +
                "3 Error: 456 123 qrz"+ System.lineSeparator() +
                "3 Error: 123 123 xyz"+ System.lineSeparator() + "2 Log: 456 cde"+ System.lineSeparator() + "2 Log: 456 cde"+
                System.lineSeparator() + "2 Log: 123 cde"+ System.lineSeparator() + "2 Log: 456 jkl";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("Log 8", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest13() throws Exception {
        File inputFile = createInputFile(FILE6);

        String args[] = {inputFile.getPath()};
        Main.main(args);

        String expected = FILE6;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest14() throws Exception {
        File inputFile = createInputFile(FILE7);

        String args[] = {"-l", "4", "-d", "2", inputFile.getPath()};
        Main.main(args);

        String expected = "Let's try some **special**  %!(characters)!% ###\n" +
                "and line breaks^$@ \r" +
                "of \\different// types; \n" +
                "in 1 file";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("Let 1 s 1", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest15() throws Exception {
        File inputFile = createInputFile(FILE8);

        String args[] = {"-d", "4", "-c", " with the ", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "1 Up with the white and gold\r" +
                "1 Down with the red and black";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("the 6 is 3 s 3 with 2", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest16() throws Exception {
        File inputFile = createInputFile(FILE9);

        String args[] = {"-s", "3", inputFile.getPath()};
        Main.main(args);

        String expected = FILE9;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest17() throws Exception {
        File inputFile = createInputFile(FILE10);

        String args[] = {"-u", inputFile.getPath()};
        Main.main(args);

        String expected = "Howdy Billy," + System.lineSeparator() +
                "I am going to take cs6300 and cs6400 next semester." + System.lineSeparator() +
                "Did you  cs 6300 last ?  want " + System.lineSeparator() +
                " 2 courses so that  will graduate Asap!";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest18() throws Exception {
        File inputFile = createInputFile(FILE11);

        String args[] = {"-u", inputFile.getPath()};
        Main.main(args);

        String expected = FILE11;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest19() throws Exception {
        File inputFile = createInputFile(FILE12);

        String args[] = {"-d", "7", "-c", "a", "3", inputFile.getPath()};
        Main.main(args);

        String expected = "3 I am going to take cs6300 and cs6400 next semester."  + System.lineSeparator() +
                "3 Did you take cs 6300 last semester? I want to"  + System.lineSeparator() +
                "5 take 2 courses so that I will graduate Asap!";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("I 3 take 3 to 2 semester 2 Howdy 1 Billy 1 am 1", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest20() throws Exception {
        File inputFile = createInputFile(FILE13);

        String args[] = {"-d", "3", "-c", "X", inputFile.getPath()};
        Main.main(args);

        String expected = "0 ABCDEF\n0 ghijkl\r0 MNOPQR";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("ABCDEF 1 ghijkl 1 MNOPQR 1", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest21() throws Exception {
        File inputFile = createInputFile(FILE13);

        String args[] = {"-d", "1", "-u", inputFile.getPath()};
        Main.main(args);

        String expected = FILE13;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("ABCDEF 1", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest22() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "Error", "1", "-u", inputFile.getPath()};
        Main.main(args);

        String expected = "1 Error: 456123123 123 xyz"  + System.lineSeparator() +
                " : 123123 ab c"  + System.lineSeparator() +
                " : 456123  "  + System.lineSeparator() +
                " : 456  qrz"  + System.lineSeparator() +
                " :   ";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest23() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "123", "-l", "4", "-d", "5", inputFile.getPath()};
        Main.main(args);

        String expected =  "3 Error: 456123123 123 xyz"   + System.lineSeparator() +
                "2 Error: 123123 ab c"   + System.lineSeparator() +
                "2 Error: 456123 123 xyz"   + System.lineSeparator() +
                "1 Error: 456 123 qrz";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("Log 8 123 8 cde 6 Error 5 456 5", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest24() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "r", inputFile.getPath()};
        Main.main(args);

        String expected = "0 Log: 123 abc" + System.lineSeparator() +
                "3 Error: 456123123 123 xyz" + System.lineSeparator() + "0 Log: 456 cde" + System.lineSeparator() +
                "0 Log: 1231 cde" + System.lineSeparator() + "3 Error: 123123 ab c"+ System.lineSeparator() +
                "3 Error: 456123 123 xyz"+ System.lineSeparator() + "0 Log: 123 cde"+ System.lineSeparator() +
                "4 Error: 456 123 qrz"+ System.lineSeparator() +
                "3 Error: 123 123 xyz"+ System.lineSeparator() + "0 Log: 456 cde"+ System.lineSeparator() + "0 Log: 456 cde"+
                System.lineSeparator() + "0 Log: 123 cde"+ System.lineSeparator() + "0 Log: 456 jkl";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("", outStream.toString().trim());
    }

    // Purpose: Example of Updated Specifications Test
    @Test
    public void mainTest25() throws Exception {
        File inputFile = createInputFile(FILE8);

        String args[] = {"-c", "-x", "-d", "5", inputFile.getPath()};
        Main.main(args);

        String expected = "0 Up with the white and gold\r" +
                "0 Down with the red and black\r" +
                "0 Georgia Tech is out for a victory\r" +
                "0 We'll drop a battle axe on georgia's head\r" +
                "0 When we meet her our team is sure to beat her\r" +
                "0 Down on the old farm there will be no sound\r" +
                "0 'Till our bow wows rips through the air\r" +
                "0 When the battle is over georgia's team will be found\r" +
                "0 With the Yellow Jacket's swarming 'round! Hey!";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("the 6 is 3 s 3 with 2 and 2", outStream.toString().trim());
    }



}