/*
@name Danielle Gonzalez-Wu
@date 4/30/2021
@course CSC201
 */

package com.company;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.HashMap;


public class huffmanencoding
{
  //we need to sort the frequency from highest to lowest and use a priority queue for this and use our own comparator
  // tree node class that will be used in our priority queue as we make trees
  public class tree_node
  {
    Character character;
    Integer frequency;
    tree_node left;
    tree_node right;

    public tree_node()
    {
      left = null;
      right = null;
    }

    public tree_node(Character chara, Integer freq)
    {
      left = null;
      right = null;
      character = chara;
      frequency = freq;
    }
  }

  class HuffmanComparator implements Comparator<tree_node>
  {
    //sorting from lowest to highest priority queue order
      /*
      a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
      we will tell it what to return based on these scenarios!
       */

    @Override
    public int compare(tree_node n1, tree_node n2)
    {
      if (n1.frequency < n2.frequency)
        return -1; //-1 puts it in back of what is already in the queue that was compared

      else if (n1.frequency > n2.frequency)
        return 1; //puts it in front of what is already in the queue that was compared

      return 0; //error if this is reached
    }
  }

  //now making a priority queue to sort
  PriorityQueue<tree_node> pq = new PriorityQueue<tree_node>(5, new HuffmanComparator());
  //making a Hashmap to store generated Huffman Codes from created Huffman Tree
  HashMap<Character, String> huffman_codes = new HashMap<Character, String>();
  //making the text variable a global variable as we use the same input for two functions (get frequency and sort function and the huffman encoding final function)
  String text = "";


  public void get_frequency_and_sort()
  {
    //we will not force it to change to lower case as this actually messes up the huffman encoding tree
    //use text length for for loop
    int string_length = text.length();

    //for character ascii value 32 - 126, traverse through string and increment if it finds a match
    for (int f = 32; f < 127 ; f++)
    {
      char ascii_character = (char) f;
      int frequency = 0;
      for(int f1 = 0; f1 < string_length; f1++)
      {
        char string_character = text.charAt(f1);
        if(string_character == ascii_character)
        {
          frequency++;
        }
      }
      //adding it to the priority queue
      if (frequency != 0)
      {
        //add to priority queue
        tree_node aq = new tree_node(ascii_character, frequency);
        pq.add(aq);
      }
    }
  }

  public void build_huffman_tree()
  {
    //want to do this until one node is remaining on the priority queue which should be all frequency the stuff merged in correct order in one tree
    while(pq.size() > 1)
    {
      //1. remove two minimum elements nodes (this should be the front of then queue if its organized lowest to highest frequency)
      tree_node minimum_node_1 = pq.poll();
      tree_node minimum_node_2 = pq.poll();
      //2. Add the two frequencies together and let that value be the root node
      Integer root_value = minimum_node_1.frequency + minimum_node_2.frequency;
      tree_node huffman_root = new tree_node(null, root_value);
      //3. Merge the two nodes removed with that tree
      //using first extracted minimum node as left child as this should be the smaller value of the extracted minimum nodes as pre-sorted
      huffman_root.left = minimum_node_1;
      //using second extracted minimum node as right child as this should be the equal or larger value to the extracted minimum nodes as pre-sorted
      huffman_root.right = minimum_node_2;
      //4. Reinsert this new node into the priority queue
      pq.add(huffman_root);
    }
  }

  /*
  now we want a function to print out the tree in BFS to ensure it was built correctly
  Print out data in BFS mode
  will always print starting at root which in our case we can see this by peeking at our priority queue
  BFS proceeds 'level by level' visiting all nodes on one level before moving on to the next!
   */
  public void bfs(tree_node root)
  {
    /*
    Being an interface the queue needs a concrete class for the declaration and the most common classes are the PriorityQueue and LinkedList
    in Java.It is to be noted that both the implementations are not thread safe.
    We use LinkedList for efficiency
     */
    Queue<tree_node> queue = new LinkedList<tree_node>(); //FIFO structure

    if (root == null) //case: nothing on the tree
    {
      //return to delete error as nothing to delete
      return;
    }
    else //case: something is on the tree
    {
      //adding root node to queue because we know there is something there and it satisfies the while loop to print at least once
      queue.add(root);
      while (!queue.isEmpty()) //while the queue is not empty
      {
        //this cast may be redundant as casted twice for the same thing, as each node is removed we traverse further by adding to queue until we reach end of list
        tree_node n = (tree_node) queue.remove(); //casting to node so we can extract the data from the node added to the queue later on to print
        System.out.println(n.character + ", " + n.frequency);

        if(n.left != null) //if it has a left child then add it to the queue
        {
          queue.add(n.left); //adding the node to the queue
        }

        if(n.right != null) //if it has a right child then add it to the queue
        {
          queue.add(n.right); //adding the node to the queue
        }
      }
    }
  }

  public void print_breadthfirst()
  {
    tree_node root = pq.peek();
    bfs(root);
  }

  //translating the huffman tree into tbe respective huffman code
  public void huffman_encoding_traversal(tree_node n, String huffman_code)
  {
    //we know an encoded letter is present when the left and right pointers are null.
    //all parents will have is the total frequency represented with the leaf's. Leaf's will store the binary encoding and the letter.
    /*
    We need to use DFS tree printout mode to reconstruct the tree
    DFS follows a path from the starting node to an ending node, then another path from start to end, and so on, until all the nodes are visited.
     DFS is a preorder traversal algorithm that uses recursion
     */

    //base case: when the left and right branches are null this indicates we have reached a character the reason why we don't check if its a character is because space doesn't count as character so it leads to error
    if(n.left == null && n.right == null)
    {
      //we have reached the point where our string should be fully formed so print it out and return the function
      System.out.println(n.character + ": " + huffman_code);
      //Storing the huffman code in Hashmap
      huffman_codes.put(n.character, huffman_code);
      //should be returning one level up and tries to recurse again
      return;
    }


    //recursively calling the function so we can traverse through all the left and right sub trees and assemble huffman code as we do
    //if it is recursing to the left branches we will add 0 to the huffman code
    huffman_encoding_traversal(n.left, huffman_code + "0");
    //if it is recursing to the right branches we will add 1 to the huffman code
    huffman_encoding_traversal(n.right, huffman_code + "1");
  }

  public void huffman_encoding()
  {
    //we will start the recursion at the root of the tree
    tree_node root = pq.peek();
    //Empty string as we will add to this string as we recursively traverse and generate code as we go
    String huffman_code = "";
    huffman_encoding_traversal(root, huffman_code);
  }

  public void print_huffman_encoded_string()
  {
    int string_length = text.length();
    for(int g1 = 0; g1 < string_length; g1++)
    {
      char string_character = text.charAt(g1);
      System.out.println(huffman_codes.get(string_character));
    }
  }




  public static void main(String[] args)
  {
    huffmanencoding test1 = new huffmanencoding();
    huffmanencoding test2 = new huffmanencoding();

    System.out.println("Text: data structures project");
    System.out.println("");
    test1.text = "data structures project";
    test1.get_frequency_and_sort();
    test1.build_huffman_tree();
    System.out.println("PRINTING HUFFMAN BINARY TREE IN BFS MODE: ");
    test1.print_breadthfirst();
    System.out.println("");
    System.out.println("HUFFMAN CODE:");
    test1.huffman_encoding();
    System.out.println("");
    System.out.println("ENCODED HUFFMAN DATA: ");
    test1.print_huffman_encoded_string();

    System.out.println("________________________________________________________________________________");
    System.out.println("Text: apples and oranges");
    System.out.println("");
    test2.text = "apples and oranges";
    test2.get_frequency_and_sort();
    test2.build_huffman_tree();
    System.out.println("PRINTING HUFFMAN BINARY TREE IN BFS MODE: ");
    test2.print_breadthfirst();
    System.out.println("");
    System.out.println("HUFFMAN CODE:");
    test2.huffman_encoding();
    System.out.println("");
    System.out.println("ENCODED HUFFMAN DATA: ");
    test2.print_huffman_encoded_string();
  }
}
