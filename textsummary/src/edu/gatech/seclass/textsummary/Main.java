package edu.gatech.seclass.textsummary;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.io.BufferedWriter;

public class Main {

/*
Empty Main class for compiling Assignment 6.
DO NOT ALTER THIS CLASS or implement it.
 */

    public static void main(String[] args) {

        if (args.length == 0){
            usage();
        }
        else {
            final Map<String, List<String>> params = new HashMap<>();
            List<String> options = null;

            for (int i = 0; i < args.length - 1; i++) {
                final String a = args[i];
                if (a.equals("-c") || a.equals("-d") || a.equals("-l") || a.equals("-s") || a.equals("-u")) {
                    options = new ArrayList<>();
                    params.put(a.substring(1), options);
                } else if (options != null) {
                    options.add(a);
                } else {
                    usage();
                    return;
                }
            }

            //System.out.println(args[args.length - 1]);

            File file = new File(args[args.length - 1]);

            //System.out.println(file.getPath());

            if (!file.exists()) {
                System.err.println("File Not Found");
                return;
            }
            else if (file.length() == 0){
                System.err.println("File is empty.");
                return;
            }

            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                if (br.readLine() == null) {
                    System.err.println("File is empty.");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            boolean lsuOption = false;
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                if (entry.getKey().equals("c")) {
                    if (entry.getValue().size() == 0 || entry.getValue().size() > 2) {
                        System.err.println("-c option must have a string value provided.");
                        return;
                    }
                    /*
                    if (entry.getValue().get(0).matches("^.*[^a-zA-Z0-9 ].*$")){
                        System.err.println("-c value has to be alphanumeric.");
                        return;
                    }
                     */
                    if (entry.getValue().size() == 2){
                        if (!entry.getValue().get(1).chars().allMatch(Character::isDigit) ||
                                Integer.parseInt(entry.getValue().get(1)) <= 0){
                            usage();
                            return;
                        }
                    }
                } else if (entry.getKey().equals("d")) {
                    if (entry.getValue().size() > 1){
                        usage();
                        return;
                    }
                    else if (entry.getValue().size() == 0) {
                        entry.getValue().add("1");
                        //System.out.println(entry.getValue());
                    }
                    if (!entry.getValue().get(0).chars().allMatch( Character::isDigit )){
                        usage();
                        return;
                    }
                    else if (Integer.parseInt(entry.getValue().get(0)) <= 0) {
                        System.err.println("-d option must have a positive integer value provided.");
                        return;
                    }
                } else if (entry.getKey().equals("l")) {
                    if (lsuOption) {
                        usage();
                        return;
                    }
                    if (entry.getValue().size() > 1) {
                        usage();
                        return;
                    } else if(entry.getValue().size() == 0) {
                        System.err.println("A positive integer value must be provided with -l option.");
                        return;
                    }
                    if (!entry.getValue().get(0).chars().allMatch( Character::isDigit )){
                        usage();
                        return;
                    }
                    if (Integer.parseInt(entry.getValue().get(0)) < 0) {
                        System.err.println("-l option must have a positive integer value provided.");
                        return;
                    }
                    lsuOption = true;
                } else if (entry.getKey().equals("s")) {
                    if (lsuOption) {
                        usage();
                        return;
                    }
                    if (entry.getValue().size() > 1) {
                        usage();
                        return;
                    } else if (entry.getValue().size() == 0) {
                            System.err.println("A positive integer value must be provided with -s option.");
                            return;
                    }
                    if (!entry.getValue().get(0).chars().allMatch( Character::isDigit )){
                        usage();
                        return;
                    }
                    if (Integer.parseInt(entry.getValue().get(0)) <= 0) {
                            System.err.println("-s option must have a positive integer value provided.");
                            return;
                    }
                    lsuOption = true;
                } else if (entry.getKey().equals("u")) {
                    if (lsuOption) {
                        usage();
                        return;
                    }
                    if (entry.getValue().size() != 0) {
                        usage();
                        return;
                    }
                    lsuOption = true;
                } else {
                    usage();
                    return;
                }
            }

            if (params.containsKey("d")) {
                int arg = Integer.parseInt(params.get("d").get(0));
                dOption(file, arg);
            }
            if (params.containsKey("l")) {
                int arg = Integer.parseInt(params.get("l").get(0));
                lOption(file.getPath(), arg);
            }
            if (params.containsKey("s")) {
                int arg = Integer.parseInt(params.get("s").get(0));
                sOption(file.getPath(), arg);
            }
            if (params.containsKey("c")) {
                List<String> arg = params.get("c");
                cOption(file.getPath(), arg);
            }
            if (params.containsKey("u")) {
                uOption(file.getPath());
            }
            if (params.isEmpty()) {
                noOption(file.getPath());
            }
        }
    }

    private static void usage() {
        System.err.println("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>");
    }

    private static void cOption(String filename, List<String> args){
        HashMap<Integer, Integer> strCountMap = new HashMap<>();
        LinkedHashMap<Integer, List<Character>> lineSepMap = new LinkedHashMap<>();
        BufferedReader reader = null;
        File file = new File(filename);

        try (BufferedReader rd = new BufferedReader(new FileReader(file))){
            int lineNum = 0;
            boolean newLine = true;
            List<Character> line = null;
            int chaInt;
            while ((chaInt = rd.read()) != -1){
                //System.out.println("reader: " + reader.read());
                if (newLine) {
                    line = new ArrayList<>();
                    lineSepMap.put(lineNum, line);
                    newLine = false;
                }
                char ch = (char)chaInt;
                //String c = String.valueOf(ch);
                //System.out.println("char c: " + c);
                line.add(ch);
                if (ch == '\r' || ch == '\n') {
                    newLine = true;
                    lineNum++;
                }
            }
            //System.out.println("lineSepMap: " + lineSepMap);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        try {
            reader = new BufferedReader(new FileReader(file));

            String currentLine = reader.readLine();
            int lineNum = 0;

            while (currentLine != null) {
                int count = 0;
                int lastIndex = 0;

                while(lastIndex != -1){
                    lastIndex = currentLine.indexOf(args.get(0),lastIndex);
                    if(lastIndex != -1){
                        count ++;
                        lastIndex += args.get(0).length();
                    }
                }
                strCountMap.put(lineNum, count);
                lineNum++;
                currentLine = reader.readLine();
            }

            List<Character> line = new ArrayList<>();
            String output = "";
            for (Map.Entry<Integer, Integer> entry : strCountMap.entrySet()) {
                StringBuilder str = new StringBuilder();
                line = lineSepMap.get(entry.getKey());
                //System.out.println("char array line: " + line);
                for (Character ch : line) {
                    str.append(ch);
                }

                String line_orig = str.toString();
                //System.out.println("line_orig: " + line_orig);
                if (args.size() == 1) {
                    output += entry.getValue() + " " + line_orig;
                    //System.out.println("output: " + output);
                }
                else{
                    if (Integer.parseInt(args.get(1)) <= entry.getValue()){
                        output += entry.getValue() + " " + line_orig;
                    }
                }
            }

            //System.out.println("the last line: " + line);
            if (output.charAt(output.length()-1) == '\n' || output.charAt(output.length()-1) == '\r')
                output =  output.substring(0, output.length() - 1);
            //output.trim();
            reader.close();

            try {
                FileWriter writer = new FileWriter(file.getPath(), false);

                writer.write(output);
                writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void dOption(File file, int arg){
        LinkedHashMap<String, Integer> wordCountMap = new LinkedHashMap<>();

        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(file));

            String currentLine = reader.readLine();

            while (currentLine != null)
            {
                //String[] words = currentLine.split("\\s|\\?|:|,|.");
                String[] words = currentLine.split("\\W");
                for (String word : words)
                {
                    //System.out.println(word);
                    if(word.length() > 0 && wordCountMap.containsKey(word))
                    {
                        wordCountMap.put(word, wordCountMap.get(word)+1);
                    }
                    else
                    {
                        if (word.matches("[a-zA-Z0-9]+"))
                            wordCountMap.put(word, 1);
                    }
                }
                //System.out.println(wordCountMap);
                currentLine = reader.readLine();
            }
            //Getting the most repeated word and its occurrence
            //System.out.println(wordCountMap);
            if (wordCountMap.size() < arg){
                System.err.println("The total number of words in the file is less than the argument.");
                return;
            }
            LinkedHashMap<String, Integer> sortedMap;

            sortedMap = wordCountMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(arg)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            for (Map.Entry<String, Integer> entry: sortedMap.entrySet()){
                System.out.print(entry.getKey() + ' ' + entry.getValue() + ' ');
            }

            /*
            LinkedHashMap<Integer, List<String>> outputMap = new LinkedHashMap<>();
            List<String> words = null;
            int prev_count = 0;

            for (Map.Entry<String, Integer> entry : sortedMap.entrySet()){
                if (entry.getValue() != prev_count){
                    words = new ArrayList<>();
                    words.add(entry.getKey());
                    outputMap.put(entry.getValue(), words);
                    prev_count = entry.getValue();
                }
                else{
                    words.add(entry.getKey());
                    java.util.Collections.sort(words);
                }
            }

            int i = 0;
            for (Map.Entry<Integer, List<String>> entry : outputMap.entrySet()){
                if (i < arg) {

                    int count = entry.getKey();

                    for (String word : entry.getValue()) {
                        if (i < arg) {
                            System.out.print(word + ' ' + count + ' ');
                            i++;
                        }
                    }
                }
            }
             */
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

}

    private static void lOption(String filename, int arg){
        LinkedHashMap<Integer, Integer> lineCountMap = new LinkedHashMap<>();
        LinkedHashMap<Integer, List<Character>> lineSepMap = new LinkedHashMap<>();

        File file = new File(filename);

        try {
            Path path = Paths.get(filename);
            long lineCount = Files.lines(path).count();
            if (arg >= lineCount){
                return;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        //BufferedReader reader = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            int lineNum = 0;
            boolean newLine = true;
            List<Character> line = null;
            int chaInt;
            while ((chaInt = reader.read()) != -1){
                //System.out.println("reader: " + reader.read());
                if (newLine) {
                    line = new ArrayList<>();
                    lineSepMap.put(lineNum, line);
                    newLine = false;
                }
                char ch = (char)chaInt;
                //String c = String.valueOf(ch);
                //System.out.println("char c: " + c);
                line.add(ch);
                if (ch == '\r' || ch == '\n') {
                    newLine = true;
                    lineNum++;
                }
            }
            //System.out.println("lineSepMap: " + lineSepMap);
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){

            String currentLine = reader.readLine();
            int lineNum = 0;

            while (currentLine != null) {
                int lineLength = currentLine.length();
                lineCountMap.put(lineNum, lineLength);
                lineNum++;
                currentLine = reader.readLine();
            }

            List<Integer> maxLineNum = new ArrayList<>();

            for (int i = 0; i < arg; i++){
                Map.Entry<Integer, Integer> maxEntry = null;

                for (Map.Entry<Integer, Integer> entry : lineCountMap.entrySet()) {
                    if (!maxLineNum.contains(entry.getKey())) {
                        if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                            maxEntry = entry;
                        }
                    }
                }
                //lineCountMap.remove(maxEntry.getKey());
                maxLineNum.add(maxEntry.getKey());
            }
            //System.out.println("maxLineNum: " + maxLineNum);

            /*
            LinkedHashMap<Integer, Integer> sortedMap;
            sortedMap = lineCountMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(arg)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            //System.out.println(sortedMap);
            List<String> longest = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
                String line = Files.readAllLines(Paths.get(filename)).get(entry.getKey());
                //System.out.println(line);
                longest.add(line);
                //System.out.println(longest);
            }
            //System.out.println(longest);
             */
            StringBuilder str = new StringBuilder();
            for (Map.Entry<Integer, List<Character>> entry : lineSepMap.entrySet()){
                if (maxLineNum.contains(entry.getKey())){
                    List<Character> longest = lineSepMap.get(entry.getKey());
                    //System.out.println(longest);
                    for (Character ch : longest) {
                        str.append(ch);
                    }
                }
            }

            String output = str.toString().trim();

            //System.out.println("output: " + output);

            reader.close();

            try {
                FileWriter writer = new FileWriter(file.getPath(), false);
                /*
                for (int i = 0; i < maxLineNum.size(); i++) {
                    if (i >= 1)
                        writer.write(System.lineSeparator());
                    writer.write(maxLineNum.get(i));
                }
                */
                writer.write(output);

                writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void sOption(String filename, int arg){
        HashMap<Integer, Integer> lineCountMap = new HashMap<>();

        File file = new File(filename);

        try {
            Path path = Paths.get(filename);
            long lineCount = Files.lines(path).count();
            if (arg >= lineCount){
                return;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){

            String currentLine = reader.readLine();
            int lineNum = 0;

            while (currentLine != null) {
                int lineLength = currentLine.length();
                lineCountMap.put(lineNum, lineLength);
                lineNum++;
                currentLine = reader.readLine();
            }

            LinkedHashMap<Integer, Integer> sortedMap;
            sortedMap = lineCountMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(arg)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            //System.out.println(sortedMap);
            List<String> shortest = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
                String line = Files.readAllLines(Paths.get(filename)).get(entry.getKey());
                //System.out.println(line);
                shortest.add(line);
                //System.out.println(longest);
            }
            reader.close();

            try {
                FileWriter writer = new FileWriter(file.getPath(), false);
                for (int i = 0; i < shortest.size(); i++) {
                    if (i >= 1) {
                        writer.write(System.lineSeparator());
                    }
                    writer.write(shortest.get(i));
                }
                writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void noOption(String filename) {
        HashMap<Integer, Integer> lineCountMap = new HashMap<>();

        File file = new File(filename);

        //BufferedReader reader = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String currentLine = reader.readLine();
            int lineNum = 0;

            while (currentLine != null) {
                int lineLength = currentLine.length();
                lineCountMap.put(lineNum, lineLength);
                lineNum++;
                currentLine = reader.readLine();
            }
            /*
            LinkedHashMap<Integer, Integer> sortedMap;
            sortedMap = lineCountMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(1)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

             */
            int longestLineNum = lineCountMap.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            String longestline = Files.readAllLines(Paths.get(filename)).get(longestLineNum);
            System.out.println(longestline);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void uOption(String filename){
        List<String> wordHistory = new ArrayList<>();
        //List<String> output = new ArrayList<>();
        //LinkedHashMap<String, Integer> wordMap = new LinkedHashMap<>();
        //LinkedHashMap<Integer, List<Character>> lineSepMap = new LinkedHashMap<>();

        File file = new File(filename);

        //BufferedReader reader = null;

        String output = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int chaInt;
            String word = new String();
            while ((chaInt = reader.read()) != -1) {
                //String[] words = currentLine.split("\\s|\\?|:|,|.");

                //boolean newline = true;
                char ch = (char)chaInt;

                if (Character.isDigit(ch) || Character.isLetter(ch)){
                        word += ch;
                }
                else{
                    if (!wordHistory.contains(word)){
                            wordHistory.add(word);
                            output += word;
                    }
                        word = "";
                        output += ch;
                }
            }
            if (word != "" && !wordHistory.contains(word))
                output += word;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(file.getPath(), false);
            writer.write(output);

            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
