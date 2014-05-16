/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ladder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 * @author Kurt Haffner
 */
public class Ladder {

    //Hashset for the wordlst.
    public static HashSet<String> wordlst = new HashSet<>();

    //String that holds the first word.
    public static String first;

    //String that holds the last word.
    public static String last;

    //Set up the graph.
    public static HashMap<String, Set<String>> graph = new HashMap<>();

    //Main class for main things.
    public static void main(String[] args) throws FileNotFoundException {

        //Set the first word from the command line.
        first = args[1];

        //Set last word from the command line.
        last = args[2];

        //If the two words entered are not the same length, 
        //error out and exit.
        if (!equal_Len(first, last)) {
            
            System.out.println("\n\nThe two words must be the same length.\n\n");
            System.exit(0);
        }

        //Fill dictionary with the words given.
        fillWords(args[0]);

        //Find the shortest path.
        pathFinder(); //find the shortest path
    }

    //Method that will check if the two words are of equal length.
    public static boolean equal_Len(String first, String second) {

        //If equal, return true. Else, return false.
        if (first.length() == second.length()) {
            return true;
        } else {
            return false;
        }
    }

    //Method to fill dictionary with the given file.
    public static void fillWords(String wordFile) throws FileNotFoundException {

        //Set up scanner for the file that is input.
        Scanner file = new Scanner(new File(wordFile));

        //While there is another line, add iterate to the dictionary.
        while (file.hasNext()) {

            String s = file.next();
            wordlst.add(s);
        }

        //If either first or last is not in the list, print an error and exit the program.
        if (!wordlst.contains(first) || !wordlst.contains(last)) {

            System.out.println("\n\nOne of these words is not in the dictionary.\n\n");
            System.exit(0);
        }
    }

    //Method that will find the shortest path.
    public static void pathFinder() {
        
        //Set of strings that have been visit.
        Set<String> visit = new HashSet<>();
        
        //Set up a new queue.
        Queue<String> q = new LinkedList<>();
        
        //Hashmap that follows children and parents.
        HashMap<String, String> child_Par = new HashMap<>();
        
        //Set up int for depth and string array.
        int depth = 1;
        String[] words_Array;
        
        //String for the current word, set to first.
        String current = first;
        
        //Add current to the visited set.
        visit.add(current);
        
        //While current != last...
        while (!current.equals(last)) {
            
            //Call adjacent with current.
            adjacent(current);
            
            //Make an iterator to go through the graph.
            Iterator<String> iterate = graph.get(current).iterator();
            
            //While the iterator has another token...
            while (iterate.hasNext()) {
                
                //String temp for the next token.
                String temp = iterate.next();
                
                //If visit doesn't hold temp, add to the queue and add temp and 
                //current to child_Par.
                if (!visit.contains(temp)) {
                    
                    q.add(temp);
                    child_Par.put(temp, current);
                }
                //Add temp to the visited set.
                visit.add(temp);
            }
            
            //Set current to the poll from the queue.
            current = q.poll();
            
            //If queue is empty, print error and exit the program.
            if (q.isEmpty()) {
                
                System.out.println("\n\nNo ladder was found from " + first + " to " + last + ".\n\n");
                System.exit(0);
            }
        }
        
        //While current is not first...
        while (!current.equals(first)) { 
            
            //Set current to the child_Par temp current. Increment depth after.
            current = child_Par.get(current);
            depth++;
        }
        
        //Set current to the second word.
        current = last;
        
        //Make the array have size depth.
        words_Array = new String[depth];
        
        //For loop that follows the depth.
        for (int i = depth - 1; i >= 0; i--) {
            
            //Set words_Array[i] to current.
            words_Array[i] = current;
            
            //If current is not the first word, current is set to
            //child_Par of current.
            if (!current.equals(first)) {
                current = child_Par.get(current);
            }
        }
        
        //Print out for formatting in command line.
        System.out.println("\n\n");
        
        //For loop to go through the depth size.
        for (int i = 0; i < depth; i++) {

            //If i < depth - 1, print the ladder, else print the final word.
            //Used for formatting, so no dangling arrow at the end.
            if (i != depth - 1) {
                
                System.out.print(words_Array[i] + " -> ");
            } else {
                
                System.out.print(words_Array[i] + "\n\n\n");
            }
        }
        
        //Print out the depth of the word ladder, for fun and profit.
        System.out.println("The depth of the ladder is " + depth + ".\n\n");
    }
    
    //Method used to find adjacent words.
    public static void adjacent(String word) {
        
        //String for the set.
        String set;
        
        //Set used to check for adjacent words.
        Set<String> adj_Set = new HashSet<>();
        
        //For loop that runs the word.
        for (int i = 0; i < word.length(); i++) {
            
            //For loop that goes through the alphabet.
            for (char j = 'z'; j >= 'a'; j--) {
                
                //Array that the chars go into.
                char[] temp = word.toCharArray();
                
                //If not the same, set temp to j and set to null.
                if (temp[i] != j) {
                    
                    temp[i] = j;
                    set = "";
                    
                    //For loop that iterates the string.
                    for (int c = 0; c < word.length(); c++) {
                        
                        //Put the temp[c] into the string by adding iterate to set.
                        set += temp[c];
                    }
                    
                    //If the dictionary has the created word, put the word into
                    //the set.
                    if (wordlst.contains(set)) {
                        
                        adj_Set.add(set); 
                    }
                }
            }
        }
        //Put the set into the graph when done.
        graph.put(word, adj_Set);
    }
}
