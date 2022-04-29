/*
@name Danielle Gonzalez-Wu
@date 4/30/2021
@course CSC201
 */

package com.company;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class spellchecker
{
  public class hashlink<Key, Value>
  {
    //inner class of nodes
    public class node
    {
      public Key key;
      public Value val;
      public node next; //pointer to link it together

      node()
      {
        next = null;
      }

      node(Key key, Value val)
      {
        this.key = key;
        this.val = val;
        next = null;
      }
    }

    node first = new node(); //linked list header node
    int N = 0; //number of items on the linked list

    public int size()
    {
      return N;
    }

    public boolean isEmpty()
    {
      return size() == 0;
    }

    public boolean contains(Key key)
    {
      //If get key did find key returns value of key, if it does not it returns null
      return get(key) != null;
    }

    public Value get(Key key)
    {
      //traverse through list to see if the key is in the list
      //essentially the same as a while loop that goes to next node until it reaches the null pointer also known as the end of the list
      for (node x = first.next; x != null; x = x.next)
      {
        if(key.equals(x.key))
        {
          //this is if it finds a matching key
          return x.val;
        }
      }
      //this is if it does not find a matching key
      return null;
    }

    public void put(Key key, Value val)
    {
      for (node x = first.next; x != null; x = x.next)
      {
        if (key.equals(x.key))
        {
          //if key is already present in hashmap we just replace the value with the new value being assigned to it
          x.val = val;
          //returning to stop after we are done as there should only be one key of a certain value in a hash map
          return;
        }
      }
      //if key was not present in list we make and insert it into our list
      //we will be adding first in this case
      if (first.next == null)
      {
        first.next = new node(key, val);
      }
      else
      {
        node newnode = new node(key, val);
        newnode.next = first.next;
        first.next = newnode;
      }
    }

    public int delete(Key key)
    {
      node curr = first.next;
      node prev = null;
      while (curr != null)
      {
        if (key.equals(curr.key))
        {
          //we now delete this node to get rid of the key on the hashmap
          if (prev == null) //case: it is the first node
          {
            //move pointer to the next node after the first one
            first.next = first.next.next;
          }
          else
          {
            //moving pointer to skip over that node essentially taking it out of the list
            prev.next = curr.next;
          }
          //incrementing list count down by 1
          N--;
          return 1;
        }
        //if we do not find a match that equals to the key we want to delete, we continue to traverse the list
        else
        {
          prev = curr;
          curr = curr.next;
        }
      }
      //Nothing for us to delete so if we reach this exception to tell the user that key did not exist in the list in the first place
      throw new NoSuchElementException();
    }
  }

  public class hashtable<Key, Value>
  {
    private int N; // number of key-value pairs in whole table
    private int M;  //hash table size
    private hashlink<Key, Value>[] table;

    //Making our hash table of desired size
    public void hashtable_amount(int M)
    {
      this.M = M;
      //Creating the table with the amount of hashes spaces we want.
      table = (hashlink<Key, Value>[]) new hashlink[M];
      //Creating a linked list to make an array of linked list with each table entry
      for (int i = 0; i < M; i++)
      {
        table[i] = new hashlink<Key,Value>();
      }
    }

    //Hash value function to create unique hash values between M-1 so each has a unique number that differentiates itself
    private int hash(Key key)
    {
      //Using build in hashcode generator first to generate initial hashcode
      int h = key.hashCode();
      //Bit shifting to create a unique hashcode
      h ^= (h >>> 20) ^ (h >>> 12);
      h = h ^ (h >>> 7) ^ (h >>> 4);
      //dividing by M to get hashcodes in the range of 0 and M-1
      return (h & 0x7fffffff) % M;
    }

    public Value get(Key key)
    {
      if (key == null)
      {
        throw new NullPointerException("argument to get the key value: () is null.");
      }
      int i = hash(key);
      return table[i].get(key);
    }

    public boolean contains (Key key)
    {
      if (key == null)
      {
        throw new NullPointerException("argument to get the key value: () is null.");
      }
      //return true if it finds the key
      return get(key) != null;
    }

    public void delete(Key key)
    {
      if (key == null)
      {
        throw new NullPointerException("argument to delete key:() is null");
      }
      int i = hash(key);
      if (table[i].contains(key))
      {
        N--; //decrement total amount of key value pairs in table
        table[i].delete(key);
      }
    }

    public void put(Key key, Value val)
    {
      if (key == null)
      {
        throw new NullPointerException("first argument to put (key: ()) is null");
      }
      if (val == null)
      {
        delete(key);
        return;
      }
      int i  = hash(key);
      //If this key does not already exist in the table
      if (!table[i].contains(key))
      {
        //add it in
        N++; //incrementing amount of key-value pairs in whole table
        table[i].put(key,val);
      }
    }

    //Checking hashtables key-values amount
    public int size()
    {
      return N;
    }
    //Checking if there is no key-values populated on the hashtable
    public boolean isEmpty()
    {
      return size() == 0;
    }
  }

  //Making two array lists one to store in the words of each line after splitting them. One for the test file and another for the file used to generate dictionary
  ArrayList<String> dictionarywords = new ArrayList<String>();
  ArrayList<String> testfilewords = new ArrayList<String>();
  public void generatedictionarywords() throws java.io.FileNotFoundException
  {
    //1. Reading in the file
    Scanner generator_file = new  Scanner(new File("odyssey_part1_original.txt"));
    //2. Creating a for or while loop to parse the lines and process them appropriately to generate dictionary words
    while (generator_file.hasNextLine())
    {
      String line = generator_file.nextLine();
      //3. Return a copy of the line with the trailing spaces from the right side taken away
      //Regular expression method:
      line = line.replaceAll("\\s++$", "");
      //4. Now return a copy of the line with any characters not related to words deleted. So basically anything that is not a letter on the keyboard.
      //Must do square bracket [ and ] separately as regular expression confuses this
      //- character must be first on the list and ^ must not be the first on the list
      line = line.replaceAll("[`~!@#$%^&*()_=+{}|;:,.?>/<\"]", "");
      line = line.replaceAll("\\[", "");
      line = line.replaceAll("]","");
      //Addressing 's part as it does not leave a correct word because grammar and -- as it leaves gaps that should not be there based on writing
      line = line.replaceAll("'s","");
      line = line.replaceAll("'S","");
      line = line.replaceAll("--", " \n");
      line = line.replaceAll("'", "");
      //5. Now, split the line into individual words and store it into an Array
      //Splitting each value that has a space between them. THIS PORTION WORKS WE ARE JUST REFINING OUR REGULAR EXPRESSION
      String words[] = line.split(" ");
      for (String aa : words)
      {
        //Storing into array list
        dictionarywords.add(aa);
      }
    }
  }

  public void generatetestfilewords() throws java.io.FileNotFoundException
  {
    //Now prepare file we want to spell check so that the words can be checked so we must generate words from this file and compare with our dictionary
    //1. Reading in the file
    Scanner test_file = new Scanner(new File("odyssey_part1_with_mistakes.txt"));
    //2. Creating a for or while loop to parse the lines and process them appropriately to generate dictionary words
    while (test_file.hasNextLine())
    {
      String line = test_file.nextLine();
      //3. Return a copy of the line with the trailing spaces from the right side taken away
      //Regular expression method:
      line = line.replaceAll("\\s++$", "");
      //4. Now return a copy of the line with any characters not related to words deleted. So basically anything that is not a letter on the keyboard.
      //Must do square bracket [ and ] separately as regular expression confuses this
      //- character must be first on the list and ^ must not be the first on the list
      line = line.replaceAll("[`~!@#$%^&*()_=+{}|;:,.?>/<\"]", "");
      line = line.replaceAll("\\[", "");
      line = line.replaceAll("]","");
      //Addressing 's part as it does not leave a correct word because grammar and -- as it leaves gaps that should not be there based on writing
      line = line.replaceAll("'s","");
      line = line.replaceAll("'S","");
      line = line.replaceAll("--", " \n");
      line = line.replaceAll("'", "");
      String test_file_words[] = line.split(" ");
      for (String bb : test_file_words)
      {
        //Storing into array list
        testfilewords.add(bb);
      }
    }
  }

  public void spellcheck() throws java.io.FileNotFoundException
  {
    generatedictionarywords();
    //Populating dictionary with words we generated
    //Getting size of the array holding all the words we generated
    int table_size = dictionarywords.size();
    hashtable<String, String> dictionary = new hashtable<String, String>();
    //Generating hashtable of that amount of words size
    dictionary.hashtable_amount(table_size);
    //Putting the words into the hashtable and making key and value the same value in this case
    for (int i = 0; i < dictionarywords.size(); i++)
    {
      String key = dictionarywords.get(i);
      String value = dictionarywords.get(i);
      dictionary.put(key,value);
    }
    //Now that hashtable is populated, compare it to array list generated from test file and print out words that are not in common with eachother
    generatetestfilewords();
    System.out.println("BADLY SPELLED WORDS:");
    for (int i = 0; i < testfilewords.size(); i++)
    {
      String key = testfilewords.get(i);
      if (dictionary.contains(key) == false)
      {
        System.out.println(key);
      }
    }
  }

  public static void main(String[] args) throws FileNotFoundException
  {
    spellchecker test = new spellchecker();
    test.spellcheck();
  }
}
