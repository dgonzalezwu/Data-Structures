/*
@name Danielle Gonzalez-Wu
@date 4/30/2021
@course CSC201
 */

package com.company;

import java.util.Queue; //for printing in BFS mode
import java.util.LinkedList; //for printing in BFS mode

public class binarysearchtree
{
  public class tree_node
  {
    Integer data;
    tree_node left;
    tree_node right;

    public tree_node()
    {
      left = null;
      right = null;
    }

    public tree_node(Integer value)
    {
      left = null;
      right = null;
      data = value;
    }
  }

  //Making node initially null bc nothing is on tree yet
  tree_node root = null;

  //Be able to add data
  private int insert(tree_node n, Integer newdata)
  {
    if (newdata <= n.data) //case: if new data is less than or equal data at node then insert to the left
    {
      if (n.left != null) //if the left node of the root is already filled recursively call to insert and traverse tree until we find appropriate node with no child to insert this node into
      {
        insert(n.left, newdata);
      }
      else //insert into left child node of that tree node as it is empty and available
      {
        n.left = new tree_node(newdata);
        //Returning 1 to indicate true that insertion was success
        return 1;
      }
    }
    else //case: if new data is greater than the node data than insert to the right
    {
      if(n.right != null)
      {
        insert(n.right, newdata);
      }
      else
      {
        n.right = new tree_node(newdata);
        return 1;
      }
    }
    //If insertion has failed for some reason we return 0 to let the user know something went wrong.
    return 0;
  }

  public void insert(Integer newdata)
  {
    //error checking
    if (newdata == null)
    {
      throw new NullPointerException("first argument to insert() is null");
    }

    if (root == null) //case: nothing on binary search tree yet
    {
      root = new tree_node(newdata);
    }
    else
    {
      insert(root, newdata);
    }
  }

  //Delete data
  public tree_node getSuccessor(tree_node delete_node)
  {
    tree_node successor = null;
    tree_node successor_parent = null;
    //we always go to the right first to access the right sub tree to get the successor
    tree_node current = delete_node.right;
    //while there is a right child
    while (current != null)
    {
      successor_parent = successor; //keep track of the parent of successor as we traverse to find the successor
      successor = current;
      //we then always go to the left until we hit no more left child on the right sub tree which is null
      current = current.left;
    }

    //broke out of while loop so we now have a successor and know it has no left child
    //checking if successor has a right child this can only be done if it traversed to the left after going one to right as may need to swap parent
    if(successor != delete_node.right)
    {
      //now that potential right child is addressed no matter what we can change pointer and delete previous right child and have successor assume right subtree for when it goes up a level
      successor_parent.left = successor.right; //adding successor right child (whether null or not) to left so it is the left child of successor parent
      successor.right = delete_node.right;
    }
    return successor;
  }


  public boolean delete(int delete_data)
  {
    tree_node parent = root;
    tree_node current = root; //this will be one of the children of the parent node
    boolean isLeftChild = false;
    //traversing through binary tree searching for that value
    while(current.data != delete_data)
    {
      parent = current;
      if(current.data > delete_data)
      {
        //if the parent is bigger than the data to be deleted we go left as thats where anything less than the parent node will be added
        isLeftChild = true;
        //setting the current to the parent's left children node
        current = current.left;
      }
      else
      {
        //if the parent is smaller than the data to be deleted we go right as thats where anything greater than the parent node will be added
        isLeftChild = false;
        //setting the current to the parent's right children node
        current = current.right;
      }

      if (current == null) //case: after setting check if its null which indicates we are at the end of the list and have not found data to be deleted
      {
        return false;
      }
    }

    //if we reach here, that means we have found a match and broke out of the while loop. At this point the current will be at the node for the data to be deleted
    //Case 1: if node to be deleted has no children
    //This means it is a leaf, so make parent (left or right) node pointer point to null to effectively delete it
    if (current.left == null && current.right == null)
    {
      if (current == root) //Case: if node is the root
      {
        root = null;
      }
      if(isLeftChild == true) //Case: if node is a left child of a parent node
      {
        //remove pointer of parent to that node essentially deleting it
        parent.left = null;
      }
      else //Case: if node is a right child of a parent node
      {
        parent.right = null;
      }
    }
    //Case 2: if node to be deleted has only one child
    //Move the child to parent level which effectively deletes node to be deleted while maintaining order
    else if(current.right == null) //current parent node to be deleted has one child on the left
    {
      if(current == root) //if node to be deleted parent node is right at the root
      {
        root = current.left; //moving left child up to parent level
      }
      else if(isLeftChild)
      {
        //parent is the level above the current node that is the child so it is pointing to it
        parent.left = current.left; //moving left child up to parent level
      }
      else
      {
        parent.right = current.left; //moving left child up to parent level
      }
    }
    else if(current.left == null) //current parent node to be deleted has one child on the right
    {
      if(current == root)
      {
        root = current.right;
      }
      else if(isLeftChild)
      {
        parent.left = current.right;
      }
      else
      {
        parent.right = current.right;
      }
    }
    //Case 3: if node to be delete has two children
    //Must find the successor node and promote, after promotion we delete the old successor node. This maintains order.
    //Successor is the node which will replace the deleted node. It is the smaller node in the right sub tree of the node to be deleted. It
    //will be a leaf or at most have a right child.
    else if(current.left != null && current.right != null)
    {
      //find successor node
      tree_node successor = getSuccessor(current);
      if (current == root)
      {
        root = successor; //delete node and promote successor to that level
      }
      else if(isLeftChild) //if the node to be deleted is a left child
      {
        parent.left = successor; //promote the successor up to the node to be deleted
      }
      else //if the node to be deleted is a right child
      {
        parent.right = successor; //promote the successor up to the node to be deleted
      }
      //successor doesn't have left child always so this pointer will now put to the left subtree left by the node to be deleted
      successor.left = current.left;
    }
    //if we got here that means everything was successful so return true to let user know success
    return true;
  }

  //Search for data
  public boolean search(int find_data)
  {
    tree_node parent = root;
    tree_node current = root; //this will be one of the children of the parent node
    boolean isLeftChild = false;
    //traversing through binary tree searching for that value
    while(current.data != find_data)
    {
      parent = current;
      if(current.data > find_data)
      {
        //if the parent is bigger than the data to be deleted we go left as thats where anything less than the parent node will be added
        isLeftChild = true;
        //setting the current to the parent's left children node
        current = current.left;
      }
      else
      {
        //if the parent is smaller than the data to be deleted we go right as thats where anything greater than the parent node will be added
        isLeftChild = false;
        //setting the current to the parent's right children node
        current = current.right;
      }

      if (current == null) //case: after setting check if its null which indicates we are at the end of the list and have not found data to be deleted
      {
        return false;
      }
    }
    //We break out of loop because condition was found to be true if we reach here
    return true;
  }

  //Print out data in BFS mode
  //will always print starting at root
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
        System.out.print(n.data + ", ");

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
    bfs(root);
  }

  //DFS traversals added in for Final Exam

  private void inordertraversal(tree_node x)
  {
    if(x == null)
      return;

    inordertraversal(x.left);
    System.out.print(x.data + ", ");
    inordertraversal(x.right);
  }

  public void inorder()
  {
    inordertraversal(root);
  }

  private void preordertraversal(tree_node x)
  {
    if(x == null)
      return;

    System.out.print(x.data + ", ");
    preordertraversal(x.left);
    preordertraversal(x.right);
   }

   public void preorder()
   {
     preordertraversal(root);
   }

   private void postordertraversal(tree_node x) {
    if(x == null)
      return;

    postordertraversal(x.left);
    postordertraversal(x.right);
    System.out.print(x.data + ", ");
  }

  public void postorder()
  {
    postordertraversal(root);
  }

  //Have print_to_dot function to use graph wiz online to ensure it is all correct
  public int print_to_dot()
  {
    Queue <tree_node> queue = new LinkedList <tree_node>();
    //Queue <node <KEY , VALUE >> queue = new LinkedList <node <KEY ,VALUE >>();
    StringBuilder dot_file = new StringBuilder();
    dot_file.append("digraph G {\n" + " \n" + " node [shape=circle , width =.6\n" + " fontname=Helvetica , fontweight=bold , fontcolor=black ,\n" + " fontsize =24, fixedsize=true ];\n");
    Integer node_counter =0;
    String clr;
    tree_node root_temp = root;

    if(root != null)
    {
      queue.add(root_temp);
      dot_file.append("N"+ root_temp.data + "[color=black , label = " + root_temp.data.toString()+ "];\n");
    }
    else
    {
      dot_file.append("}");
      System.out.println(dot_file);
      return(1);
    }

    String temp;
    int flag =0;
    int ID = 1;
    while (!queue.isEmpty())
    {
      tree_node current_node = queue.remove();
      if(current_node.left != null)
      {
        queue.add(current_node.left);
        temp = "[fontcolor=black , label = ";
        dot_file.append("N" + current_node.left.data + temp + current_node.left.data.toString()+ "];\n");
      }
      if(current_node.right != null)
      {
        queue.add(current_node.right);
        temp = "[fontcolor=black , label = ";
        dot_file.append("N" + current_node.right.data + temp + current_node.right.data.toString()+ "];\n");
      }
    }
    tree_node temp2 = root;
    queue.add(temp2);
    while (!queue.isEmpty())
    {
      tree_node current_node = queue.remove();
      if(current_node.left != null)
      {
        queue.add(current_node.left);
        dot_file.append("N" + current_node.data + "->N"+ current_node.left.data+ ";\n");
      }
      if(current_node.right != null)
      {
        queue.add(current_node.right);
        dot_file.append("N" + current_node.data + "->N"+ current_node.right.data+ ";\n");
      }
    }
    dot_file.append("}");
    System.out.println(dot_file.toString());
    return(1);
  }

  public static void main(String[] args)
  {
    binarysearchtree test  = new binarysearchtree();
        //Insert test cases
    System.out.println("INSERTING 30, 100, 20, 28, 29, 27,26, 15, 16, and 14 INTO THE BINARY TREE: ");
    test.insert(30);
    test.insert(100);
    test.insert(20);
    test.insert(28);
    test.insert(29);
    test.insert(27);
    test.insert(26);
    test.insert(15);
    test.insert(16);
    test.insert(14);
    //Printing BFS to see if everything has been inserted
    System.out.println("Printing our binary tree in BFS mode: ");
    test.print_breadthfirst();
    //Printing to graphwiz to confirm it is correctly structures
    System.out.println("");
    System.out.println("Generating Graphwiz structure to ensure we are getting the correct Binary tree formation: ");
    test.print_to_dot();
    System.out.println("________________________________________________________________________________________________");

    System.out.println("DELETING FROM THE BINARY TREE: ");
    System.out.println("Deleting 20:");
    //Delete test cases (print inbetween in BFS to show that function is working)
    test.delete(20);
    //Printing BFS to see if everything has been inserted
    System.out.println("Printing our binary tree in BFS mode: ");
    test.print_breadthfirst();
    //Printing to graphwiz to confirm it is correctly structures
    System.out.println("");
    System.out.println("Generating Graphwiz structure to ensure we are getting the correct Binary tree formation: ");
    test.print_to_dot();
    System.out.println("");
    System.out.println("Deleting 30: ");
    test.delete(30);
    //Printing BFS to see if everything has been inserted
    System.out.println("Printing our binary tree in BFS mode: ");
    test.print_breadthfirst();
    //Printing to graphwiz to confirm it is correctly structures
    System.out.println("");
    System.out.println("Generating Graphwiz structure to ensure we are getting the correct Binary tree formation: ");
    test.print_to_dot();
    System.out.println("");
    System.out.println("Deleting 100: ");
    test.delete(100);
    //Printing BFS to see if everything has been inserted
    System.out.println("Printing our binary tree in BFS mode: ");
    test.print_breadthfirst();
    //Printing to graphwiz to confirm it is correctly structures
    System.out.println("");
    System.out.println("Generating Graphwiz structure to ensure we are getting the correct Binary tree formation: ");
    test.print_to_dot();
    System.out.println("________________________________________________________________________________________________");

    //Search test cases
    System.out.println("SEARCHING FOR DATA IN OUR BINARY TREE: ");
    System.out.println("Searching for 28 in our Binary Tree: " + test.search(28));
    System.out.println("Searching for 5 in our Binary Tree: " + test.search(5));
    System.out.println("Searching for 15 in our Binary Tree: " + test.search(15));
    System.out.println("Searching for 100 in our Binary Tree: " + test.search(100));
    System.out.println("________________________________________________________________________________________________");

    //Print out tree in BFS mode
    System.out.println("PRINTING OUT OUR FINAL TREE IN BFS MODE AFTER USING SOME FUNCTIONS TO ALTER TREE: ");
    test.print_breadthfirst();
  }
}
