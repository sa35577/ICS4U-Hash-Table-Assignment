/*
HashAssign1.java
Sat Arora
This code is for #1 on the HashTable assignment, which allows a user to enter
a series of characters. From a dictionary of over 80000 words,
the program prints all the strings that are possible permutations of that string.
 */
//importing necessary packages
import java.io.*;
import java.util.*;
public class HashAssign1 {
    static HashTable<String> h = new HashTable<String>(); //creating HashTable that holds Strings
    public static void main(String[] args) throws IOException {
        Scanner inFile = new Scanner(new BufferedReader(new FileReader("dictionary.txt"))); //scanner to read from "dictionary.txt" file
        while (inFile.hasNext()) {  //runs while the file has a word
            String word = inFile.next(); //getting next word
            h.add(word); //adding word to HashTable
        }
        inFile.close(); //file is done, closing
        Scanner kb = new Scanner(System.in); //scanner for input
        String input = ""; //string holding all characters inputted
        System.out.println("Enter up to 8 characters one at a time (enter -1 to terminate):");
        for (int i = 0; i < 8; i++) { //runs a max of 8 times
            String nxt = kb.next(); //getting next String
            if (nxt.equals("-1")) break; //if they want to terminate, break out of for loop
            input += nxt; //adding the String they inputted to the String of total characters
        }
        ArrayList<String> validWords = anagram(input); //ArrayList holding all valid permutations that occur in the dictionary
        for (String word : validWords) System.out.println(word);
    }
    //method that searches through all permutations
    public static ArrayList<String> anagram(String word) {return anagram("",word); }
    //overloaded recursive method that build up all permutations that occur in the dictionary, sofar is the permutation that has been added and left is the permutation that is remaining
    public static ArrayList<String> anagram(String sofar, String left) {
        ArrayList<String> perms = new ArrayList<>(); //creating new ArrayList for this recursive instance
        if (left.equals("")) { //checks if there are no characters that are left to be added
            if (h.contains(sofar) && !perms.contains(sofar)) //sofar is added to perms if it is not a duplicate and the permutation of sofar is a valid word
                perms.add(sofar);
        }
        else {
            for (int i = 0; i < left.length(); i++) { //looping thru each index
                String next = left.substring(0,i) + left.substring(i+1); //creating a string that will be used in the recursion
                for (String word : anagram(sofar + left.charAt(i),next)) { //adding each word from the recursive call if perms does not contain that permutation
                    if (!perms.contains(word)) perms.add(word);
                }

            }
        }
        return perms;
    }
}
