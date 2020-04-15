# TextSummary
In this project I used Java to develop a **command-line** tool called textsummary to help anaylize a text file.
## Synopsis
textsummary option \<filename>
where option can be **zero or more** of
- -c \<string> [integer]
- -d [integer]
- (-l | -s) [integer]
- -u
## Descriptions for Options
- -c \<string> [integer] : If specified, the textsummary tool will add the number of times the provided \<string> appears 
in the line to the start of each line in the file

- -d [integer] : By default, it will output the single most common word. If a positive integer is provided in the optional 
parameter [integer], the tool will output that number of the most common words, starting from the most common. 
**This option will always be executed first.**

- (-l | -s) [integer] : if specified, it will keep only the longest(-l) or shortest(-s) [integer](a required positive integer) 
lines in the file. **Options -l, -s, and -u are mutually exclusive. This option is executed last.**

- -u : if specified, it will keep only the first instance of any unique word (where a word is any sequence of alphanumeric 
characters) in the file. All other characters remain. **Options -l, -s, and -u are mutually exclusive. 
This option is executed last.**

**If none of the option flags are provided, textsummary will simply output the longest line in the file.**
## Notes
While the last command-line parameter provided is always treated as the filename, option flags can be provided in any order; 
though no matter the order of the parameters, if provided, -d will be applied first. 
## Examples
- ### Example 1
  - Command line : 
  ```
  textsummary file1.txt 
  ```
  - File content :
  ```
  3 dogs
  a cat 
  ```
  - Outputs : 
  ```
  3 dogs
  ```

- ### Example 2
  - Command line :
  ```
  textsummary -l 1 file1.txt 
  ```
  - File content :
  ```
  dog bird cat cat 
  cat dog fish
  ```
  - Result file :
  ```
  dog bird cat cat
  ```
  
 - ### Example 3
   - Command line :
   ```
   textsummary -d 2 -c “d” 2 file1.txt
   ```
   - File content :
   ```
   dog bird cat cat 
   cat dog fish
   ```
   - Result file:
   ```
   2 dog bird cat cat
   ```
   - Output :
   ```
   cat 3 dog 2
   ```
 
 - ### Example 4
   - Command line :
   ```
   textsummary -d -s 2 file1.txt
   ```
   - File content:
   ```
   dog bird cat cat 
   cat dog fish cat 
   dog
   bird fish
   ```
   - Result file :
   ```
   dog
   bird fish
   ```
   - Output :
   ```
   cat 4
   ```
 
 - ### Example 5
   - Command line :
   ```
   textsummary -u file1.txt
   ```
   - File content :
   ```
   dog bird cat cat
   cat dog fish cat
   ```
   - Result file :
   ```
   dag bird cat
   fish
   ```
    
  
