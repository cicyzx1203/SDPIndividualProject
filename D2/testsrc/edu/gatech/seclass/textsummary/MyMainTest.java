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

public class MyMainTest {
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
    private static final String FILE1 =  "";
    private static final String FILE2 =  "It's a test file2." + System.lineSeparator() + "second line.";
    private static final String FILE3 =  "short1" + System.lineSeparator() + "short2"
            + System.lineSeparator() + "longest line1" + System.lineSeparator() + "longest line2"
            + System.lineSeparator() + "longest line3" + System.lineSeparator() +"middle one";
    private static final String FILE4 = "it is file 4" + System.lineSeparator() + "no duplicate word"
            + System.lineSeparator() + "period.";
    private static final String FILE5 = "the file5" + System.lineSeparator() + "6300 the 6300"
            + System.lineSeparator() + "another line" + System.lineSeparator() + "6300 is the course";
    /*
     *   TEST CASES
     */

    // Purpose: To test the error scenario when the file is not presented
    // Frame #: Test case 1
    @Test
    public void textsummaryTest1() throws Exception {
        String args[] = {"notExist.txt"};
        Main.main(args);

        assertEquals("File Not Found", errStream.toString().trim());
    }

    // Purpose: To test the scenario when the file is empty
    // Frame #: Test case 2
    // Failure Type: Corner Case
    @Test
    public void textsummaryTest2() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {inputFile.getPath()};
        Main.main(args);

        //assertEquals("File is empty.", errStream.toString().trim());
        assertEquals("", errStream.toString().trim());
    }

    // Purpose: To test the scenario when the option c is chosen but no value provided
    // Frame #: Test case 4
    // Failure Type: BUG: textsummary fails when no argument provided for -c,
    // probably due to missing error check for option selected.
    @Test
    public void textsummaryTest3() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-c", inputFile.getPath()};
        Main.main(args);

        assertEquals("-c option must have a string value provided.", errStream.toString().trim());
    }

    // Purpose: To test the scenario when the option c is chosen,
    //          and the value provided is space
    // Frame #: Test case 5
    @Test
    public void textsummaryTest4() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-c", "\r", inputFile.getPath()};
        Main.main(args);

        String expected = "0 It's a test file2." + System.lineSeparator() + "0 second line.";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: To test the scenario when the option c is chosen,
    //          and the value provided is special character
    // Frame #: Test case 6
    @Test
    public void textsummaryTest5() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-c", " ", inputFile.getPath()};
        Main.main(args);

        String expected = "3 It's a test file2." + System.lineSeparator() + "1 second line.";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: To test the scenario when the option d is chosen but non-positive value provided
    // Frame #: Test case 7
    // Failure Type: BUG: textsummary fails when 0 provided for -d,
    // probably due to missing error check for the argument, which should be a positive integer.
    @Test
    public void textsummaryTest6() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-d", "0", inputFile.getPath()};
        Main.main(args);

        assertEquals("-d option must have a positive integer value provided.", errStream.toString().trim());
    }

    // Purpose: To test the scenario when the option l is chosen,
    //          but the value provided is larger than the number of lines in the file
    // Frame #: Test case 10
    // Failure Type: BUG: textsummary fails when the argument provided for -l is larger than the total line number of the file
    // probably due to missing check for the argument with the total line number of the file
    // if the argument is larger than the total line number of the file, we just keep the file unchanged.
    @Test
    public void textsummaryTest7() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-l", "100", inputFile.getPath()};
        Main.main(args);

        String expected = FILE3;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: To test the scenario when the option s is chosen,
    //          but the value provided is larger than the number of lines in the file
    // Frame #: Test case 13
    // Duplicates <testsummaryTest7>
    @Test
    public void textsummaryTest8() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-s", "100", inputFile.getPath()};
        Main.main(args);

        String expected = FILE3;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: To test the scenario when the option l is chosen,
    //         and the number of longest lines (with same length) is larger than the value provided
    // Frame #: Test case 11
    @Test
    public void textsummaryTest9() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-l", "2", inputFile.getPath()};
        Main.main(args);

        String expected = "longest line1" + System.lineSeparator() + "longest line2";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: To test the scenario when the option s is chosen,
    //         and the number of shortest lines (with same length) is larger than the value provided
    // Frame #: Test case 14
    @Test
    public void textsummaryTest10() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-s", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "short1";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: To test the scenario when the option d(value 1) & l(value >0) is chosen,
    //          and the most occurrence word(s) only occur once.
    // Frame #: Test case 20
    @Test
    public void textsummaryTest11() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-d", "1", "-l", "2", inputFile.getPath()};
        Main.main(args);

        String expected = "it is file 4" + System.lineSeparator() + "no duplicate word";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("it 1", outStream.toString().trim()); //it
    }

    // Purpose: To test the scenario when the option c(length >0) & s(value >0) is chosen,
    //          and the string provided by c occurs more than 1 in this file,
    //          and the string provided by c occurs 1 in one of the lines.
    // Frame #: Test case 36
    // Assumption: I assume the -s/l will be always executed before -c.
    @Test
    public void textsummaryTest12() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "the", "-s", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "1 the file5";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: To test the scenario when the option c(length >0) & l(value >0) is chosen,
    //          and the string provided by c occurs more than 1 in this file,
    //          and the string provided by c occurs more than 1 in one of the lines.
    // Frame #: Test case 47
    @Test
    public void textsummaryTest13() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "6300", "-l", "3", inputFile.getPath()};
        Main.main(args);

        String expected = "2 6300 the 6300" + System.lineSeparator() + "0 another line"
                + System.lineSeparator() + "1 6300 is the course";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: To test the scenario when the option c(length>0), d(value>0) and l(value>0) are chosen,
    //          and the string provided by c occurs more than 1 in this file,
    //          and the string provided by c occurs more than 1 in one of the lines.
    // Frame #: Test case 50
    @Test
    public void textsummaryTest14() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "6300", "-d", "3", "-l", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "1 6300 is the course";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("the 3 6300 3 file5 1", outStream.toString().trim());
    }

    // Purpose: To test the scenario when the option c(length>0), d(value>0) and s(value>0) are chosen,
    //          and the string provided by c occurs more than 1 in this file,
    //          and the string provided by c occurs more than 1 in one of the lines.
    // Frame #: Test case 51
    @Test
    public void textsummaryTest15() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "the", "-d", "2", "-s", "2", inputFile.getPath()};
        Main.main(args);

        String expected = "1 the file5" + System.lineSeparator() + "1 another line"; //another line

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("the 3 6300 3", outStream.toString().trim());
    }

    // Purpose: Test when the value for -s option is negative number.
    // Failure Type: BUG: textsummary fails when negative argument are provided for -s option
    // probably due to missing error check for the argument provided for -s option
    @Test
    public void textsummaryTest16() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-s", "-1", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>", errStream.toString().trim());
    }

    // Purpose: Test when -l and -s are selected both.
    @Test
    public void textsummaryTest17() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-l", "1", "-s", "2", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>", errStream.toString().trim());
    }

    // Purpose: Test when the value provided for -s is not an number.
    // Duplicates <testsummaryTest16>
    @Test
    public void textsummaryTest18() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-s", "abc", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>",
                errStream.toString().trim());
    }

    // Purpose: Test when the value provided for -l is not an number.
    // Duplicates <testsummaryTest16> & <testsummaryTest18>
    @Test
    public void textsummaryTest19() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-l", "abc", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>",
                errStream.toString().trim());
    }

    // Purpose: Test when the value provided for -d is not an number.
    @Test
    public void textsummaryTest20() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-d", "abc", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>",
                errStream.toString().trim());
    }

    // Purpose: Test when -d and -c are both selected.
    @Test
    public void textsummaryTest21() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-d", "2", "-c", "it is", inputFile.getPath()};
        Main.main(args);

        String expected = "1 it is file 4" + System.lineSeparator() + "0 no duplicate word" + System.lineSeparator() + "0 period.";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("it 1 is 1", outStream.toString().trim());
    }

    // Purpose: Test when there are more than one argument provided for -c
    // Purpose for D2: Test when the second value provided for -c is a negative number
    // Failure Type: BUG: textsummary fails when negative number provided for the 2nd value of -c option
    // probably due to missing validation check for the 2nd argument provided for -c option.
    @Test
    public void textsummaryTest22() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "the", "-2", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>",
                errStream.toString().trim());
    }

    // Purpose for D2: Test when the second value provided for -c is zero
    // Failure Type: BUG: textsummary fails when zero provided for the 2nd value of -c option
    // probably due to missing validation check for the 2nd argument provided for -c option,
    // which should be a positive integer.
    @Test
    public void textsummaryTest23() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "6300", "0", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>",
                errStream.toString().trim());
    }

    // Purpose: Test when -d and -s are both selected
    @Test
    public void textsummaryTest24() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-d", "3", "-s", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "the file5";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals("the 3 6300 3 file5 1", outStream.toString().trim());
    }

    // Purpose: Test when a hugh integer are provided as the argument for -d
    // Failure Type: BUG: textsummary fails when a hugh number provided for -d option
    // probably due to missing check for the number with the total word number of the file
    @Test
    public void textsummaryTest25() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-d", "1000", inputFile.getPath()};
        Main.main(args);

        assertEquals("The total number of words in the file is less than the argument.",
                errStream.toString().trim());
    }

    // Test when 0 provided as the arguement for -d option
    // Duplicates <testsummaryTest6>
    @Test
    public void textsummaryTest26() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-d", "0", inputFile.getPath()};
        Main.main(args);

        assertEquals("-d option must have a positive integer value provided.", errStream.toString().trim());
    }

    // Purpose: Test -l option with large argument
    // Duplicates <testsummaryTest7> & <testsummaryTest8>
    @Test
    public void textsummaryTest27() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-l", "56", inputFile.getPath()};
        Main.main(args);

        String expected = FILE3;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: Test -s option with large argument
    // // Duplicates <testsummaryTest7> & <testsummaryTest8> & <testsummaryTest27>
    @Test
    public void textsummaryTest28() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-s", "43", inputFile.getPath()};
        Main.main(args);

        String expected = FILE3;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: Test the -l option
    @Test
    public void textsummaryTest29() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-l", "3", inputFile.getPath()};
        Main.main(args);

        String expected = "6300 the 6300" + System.lineSeparator() + "another line"
                + System.lineSeparator() + "6300 is the course";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: Test the -s option
    @Test
    public void textsummaryTest30() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-s", "1", inputFile.getPath()};
        Main.main(args);

        String expected = "period.";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: Test the -u option
    @Test
    public void textsummaryTest31() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-u", inputFile.getPath()};
        Main.main(args);

        String expected = "short1" + System.lineSeparator() + "short2" + System.lineSeparator() + "longest line1"
                + System.lineSeparator() + " line2" + System.lineSeparator() + " line3"
                + System.lineSeparator() + "middle one";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: Test the -u option with extra argument provided
    @Test
    public void textsummaryTest32() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-u", "3", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>",
                errStream.toString().trim());
    }

    // Purpose: Test both -l and -u options are both selected
    @Test
    public void textsummaryTest33() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-u", "-l", "1", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>",
                errStream.toString().trim());
    }



}
