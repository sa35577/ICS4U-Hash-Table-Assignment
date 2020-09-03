/*
HashTable.java
Sat Arora
This is the simulation of a HashTable. It stores any type of value using its hashCode as the key, and
items that turn out to be in the same index in the ArrayList are stored in a LinkedList. Besides the LinkedList and ArrayList in java.util,
no other built-in data structures were used.
 */
//importing necessary packages
import java.util.*;
class HashTable<T> {
    private int numElements; //holds number of elements in hashtable
    private ArrayList<LinkedList<T>> table; //this is the table itself, just an arraylist of linkedlists
    private double maxLoad; //holds the maximum load to size ratio (expressed as decimal)
    //constructor
    public HashTable() {
        numElements = 0;
        emptyList(10);
        maxLoad = 0.75;
    }
    //method returns the size of the table
    public int getSize() {return table.size();}
    //method returns the number of elements
    public int getNumElements() {return numElements;}
    //method that creates an empty list of size n
    public void emptyList(int n) {
        table = new ArrayList<LinkedList<T>>();
        for (int i = 0; i < n; i++) {
            table.add(null);
        }
    }
    //method that adds a given value to a hashtable
    public void add(T val) {
        int spot = Math.abs(val.hashCode()) % table.size(); //getting the spot in the hashtable that prevents out of bound exception
        if (table.get(spot) == null) {
            table.set(spot,new LinkedList<T>()); //setting the spot to have an empty LinkedList to be able to add to it
        }
        table.get(spot).add(val); //adding to the LinkedList
        numElements++; //one more element in hashTable
        //resize if the load is not enough
        if (getLoad() >= maxLoad) {
            resize();
        }
    }
    //method that removes a value in the hashTable if it exists
    public void remove(T val) {
        if (!this.contains(val)) return; //checking if the table actually contains the value, and will immediately leave the function if it doesn't as there will be nothing to remove
        int spot = Math.abs(val.hashCode()) % table.size(); //getting the spot in the table where the value would be located
        if (table.get(spot).size() == 1) { //checks if that is the only value in the LinkedList
            table.set(spot,null); //setting the LinkedList at the spot to be null if it will be empty after deletion
        }
        else table.get(spot).remove(spot); //otherwise we simply delete the value
        numElements--;//one less element in the LinkedList
    }
    //method that checks if the value is inside the HashTable
    public boolean contains(T val) {
        LinkedList<T> list = table.get(Math.abs(val.hashCode()) % table.size()); //getting the LinkedList which would hold the value by using the value's hashCode
        return list != null && list.contains(val); //returns true if the list exists, and it contains the desired value
    }
    //method that gets the value given the hashCode of the object
    public T get(int code) {
        int modCode = Math.abs(code) % table.size(); //getting the code used to index in the ArrayList
        if (table.get(modCode) != null) { //checks if there is a valid LinkedList at the spot
            for (T val : table.get(modCode)) { //looping thru every element in the LinkedList
                if (val.hashCode() == code) return val; //returns the value if its hashCode is the same as the desired hashCode
            }
        }
        return null; //at this point, there is no object in the HashTable that has the same hashCode as the one passed in, so it reutrns null
    }
    //method that gets the load of the hashTable
    public double getLoad() {
        return (double)numElements/table.size();
    }
    //method that sets the maxLoad of the hashTable
    public void setMaxLoad(int percent) {
        if (percent >= 0.1 && percent <= 0.8) maxLoad = percent; //setting the maxLoad to the percent if it is valid (btw 0.1 and 0.8)
        if (this.getLoad() >= maxLoad) { //resizes if the load is greater than the maxLoad
            resize();
        }
    }
    //setting a load to a specific value, if it is valid
    public void setLoad(double percent){
        if(percent >= 0.1 && percent <= 0.8){ //checks if percent is valid
            if(percent < maxLoad){ //if the current maxLoad was larger than the percent, then it must be resized to match
                double size = (double) numElements/percent; //getting the exact value to resize to
                resize((int)size); //resizing to the floor of that value
            }
        }
    }
    //method that returns an ArrayList of all the contents
    public ArrayList<T> toArray() {
        ArrayList<T> arr = new ArrayList<T>(); //ArrayList that will hold all contents
        for (LinkedList<T> lst : table) { //looping thru each LinkedList
            if (lst != null) { //checks if the list exists
                for (T val : lst) { //adding every value to the ArrayList
                    arr.add(val);
                }
            }
        }
        return arr;
    }
    //default resize method will increasing the table's size by a factor of 10
    public void resize() {
        resize(this.getSize()*10);
    }
    //resize method that takes in a specific value to resize to
    public void resize(int sz) {
        ArrayList<LinkedList<T>> tmp = table; //temporary variable that holds the same contents as the current table
        emptyList(sz); //creating empty list with specified size
        numElements = 0; //setting the number of elements to 0 as there is nothing in the table
        for (LinkedList<T> lst : tmp) { //looping thru each LinkedList, adding all of its contents if the LinkedList exists
            if (lst != null) {
                for (T val : lst) {
                    add(val);
                }
            }
        }
    }
    //method that returns a string with all the values in the HashTable
    @Override
    public String toString() {
        String ans = ""; //holds the string that will contain all values
        for (LinkedList<T> lst : table) { //adding each of the vales in each LinkedList if the LinkedList exists to the String with a comma & space for style
            if (lst != null) {
                for (T val : lst) {
                    ans += val + ", ";
                }
            }
        }
        if (ans != "") {
            ans = ans.substring(0,ans.length()-2); //taking away last comma and space
        }
        return "{" + ans + "}";
    }

}