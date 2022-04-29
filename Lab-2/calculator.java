/*
@name Danielle Gonzalez-Wu
@date 4/30/2021
@course CSC201
 */

package com.company;
import java.io.IOException;
import java.util.Scanner;

public class calculator
{
  //Will implement linked list for stacks so it can dynamically adjust space
  //Class for Node for Linked Lists
  class node
  {
    public String data;
    public node next;

    node() //creating node
    {
      next = null; //this will act as the sentinel value that points to the end of the list
    }

    node(String value) //creating node
    {
      data = value;
      next = null;
    }
  }

  node first_numeric = new node();
  node first_operator = new node();
  String pop_numeric;
  String pop_operator;
  String peek_numeric;
  String peek_operator;

  //Make one stack for numeric and another for operator
  public class NumericStack
  {
    //push: inserting an item into a stack, equivalent to addFirst stack function
    //pop: removes and returns stack top entry, very similar to delete_first stack function
    //peek: returns the contents of the top of the stack. Does not remove entry, similar to get_first stack function.
    //isEmpty: detects if the stack is empty. item (clear) resets the stack to empty. probably requires loop delete_first, and confirm the first node next is pointing to null again.
    void push(String value)
    {
      if (first_numeric.next == null) //case: list is empty
      {
        first_numeric.next = new node(value);
      }
      else //case: list is not empty at least one node on the list
      {
        node newnode = new node(value); //allocating memory for the new node
        newnode.next = first_numeric.next; //changing new node pointer from null to what the first.next is pointing to
        first_numeric.next = newnode; //changing what the first node pointer is pointing to previously to where the new node information is pointing to
      }
    }

    public String pop()
    {
      try
      {
        if (first_numeric.next == null) //case: list is empty
        {
          //Making exception here to let the user know no accounts exist according to file
          throw new NullPointerException();

        }
        else //case: list is not empty at least one node on the list
        {
          pop_numeric = first_numeric.next.data;
          node temp = first_numeric.next; //creating a temporary node that points to where the first node is also pointing to
          first_numeric.next = temp.next; //moving the first node's pointer to the next node pointer effectively removing the original head from the list
        }
      }
      catch(NullPointerException exception)
      {
        System.out.println(exception + ":" + " The list is empty, so there is nothing to remove. If a final result is present, it may be incorrect. Please check result and equation syntax and try again.");
      }
      return pop_numeric;
    }


    public String peek() //custom exception
    {
      try
      {
        if (first_numeric.next == null) //case: list is empty
        {
          //Making exception here to let the user know no accounts exist according to file
          throw new NullPointerException();

        }
        else //case: list is not empty at least one node on the list
        {
          peek_numeric = first_numeric.next.data; //data for the element in the first node (accompanying train car on list)
        }
      }
      catch(NullPointerException exception)
      {
        //Commenting out as it is not an error, this is acceptable for our calculator. We want to ignore this exception in this case.
        //System.out.println(exception + ":" + " The list is empty, so there is nothing to return.");
      }
      return peek_numeric;
    }

    boolean isEmpty()
    {
      if (first_numeric.next == null)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }

  public class OperatorStack
  {
    //push: inserting an item into a stack, equivalent to add_last stack function
    //pop: removes and returns stack top entry, very similar to delete_last stack function
    //peek: returns the contents of the top of the stack. Does not remove entry, similar to get_first stack function.
    //isEmpty: detects if the stack is empty. item (clear) resets the stack to empty. probably requires loop delete_first, and confirm the first node next is pointing to null again.

    void push(String value)
    {
      if (first_operator.next == null) //case: list is empty
      {
        first_operator.next = new node(value);
      }
      else //case: list is not empty at least one node on the list
      {
        node newnode = new node(value); //allocating memory for the new node
        newnode.next = first_operator.next; //changing new node pointer from null to what the first.next is pointing to
        first_operator.next = newnode; //changing what the first node pointer is pointing to previously to where the new node information is pointing to
      }
    }

    public String pop()
    {
      try
      {
        if (first_operator.next == null) //case: list is empty
        {
          //Making exception here to let the user know no accounts exist according to file
          throw new NullPointerException();

        }
        else //case: list is not empty at least one node on the list
        {
          pop_operator = first_operator.next.data;
          node temp = first_operator.next; //creating a temporary node that points to where the first node is also pointing to
          first_operator.next = temp.next; //moving the first node's pointer to the next node pointer effectively removing the original head from the list
        }
      }
      catch(NullPointerException exception)
      {
        System.out.println(exception + ":" + " The list is empty, so there is nothing to remove. If a final result is present, it may be incorrect. Please check result and equation syntax and try again.");
      }
      return pop_operator;
    }


    public String peek()
    {
      try
      {
        if (first_operator.next == null) //case: list is empty
        {
          //Making exception here to let the user know no accounts exist according to file
          throw new NullPointerException();

        }
        else //case: list is not empty at least one node on the list
        {
          peek_operator = first_operator.next.data; //data for the element in the first node (accompanying train car on list)
        }
      }
      catch(NullPointerException exception)
      {
        //Commenting out as it is not an error, this is acceptable for our calculator. We want to ignore this exception in this case.
        //System.out.println(exception + ":" + " The list is empty, so there is nothing to return.");
      }
      return peek_operator;
    }

    boolean isEmpty()
    {
      if (first_operator.next == null)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }

  //Make an input so the person can input the equation and makes sure it reads the whole line this is a global equation
  String input;
  void inputequation()
  {
    Scanner function = new Scanner(System.in);
    System.out.println("Please input the function to be solved.");
    //Reads in whole string line
    input = function.nextLine();
  }

  void assess(String theinput)
  {
    //Creating the stacks
    OperatorStack operator_stack = new OperatorStack();
    NumericStack numeric_stack = new NumericStack();


    //Removing all excess spaces from the equation
    theinput = theinput.replaceAll(" ","");
    System.out.println("Input with spaces removed: " + theinput);
    //Getting string length, which starts at 0 and counts up index wise
    int length = theinput.length();
    //These two variables will be used to separate the parts of the string into substrings to be pushed into the stacks accordingly;
    int b = 0;
    int c = 1;
    int depth = 0;

    //Validate that this equation syntax is correct before processing it into stacks
    //Check to see if parentheses is balanced throughout equation (on StackOverflow it seems regex is not the most efficient way to analyze this part better to scan manually)
    try
    {
      for (int a = 0; a < length; a++)
      {
        String substring_input = theinput.substring(b, c);
        if(substring_input.equals("("))
        {
          depth++;
        }
        if(substring_input.equals(")"))
        {
          depth--;
        }
        b++;
        c++;
      }
      //If parentheses are balanced the number should not have changed at all
      if (depth != 0)
      {
        throw new IOException();
      }
      //Resetting integer b and c so it can be used again later
      b = 0;
      c = 1;
      //Checks to see if there are only the correct operators and integer numbers or double numbers only
      //regex for decimals: \.?\d ADD IN IF HE WANTS IT]

      if(theinput.substring(0,1).matches("^[+)*/^-]+$"))
      {
        throw new NoSuchFieldException();
      }

      //This checks to see that only the appropriate digits and operators are used. Will throw an exception if it detects anything is wrong.
      if (!theinput.matches("^[0-9+-/\\*\\(\\)^ ]*$"))
      {
        throw new ArithmeticException();
      }

      //Checks to see if all the operators have correct syntax.
      int correct_operator_syntax = 0;
      int num_operators = 0;

      for (int d = 0; d < length; d++)
      {
        String substring_input = theinput.substring(b,c);
        if (substring_input.matches("[+*/^-]*$"))
        {
          if (theinput.substring(b-1,c-1).matches("^[\\d]*$") || theinput.substring(b-1,c-1).equals(")") )
          {
            if(theinput.substring(b+1,c+1).matches("^[\\d]*$") || theinput.substring(b+1,c+1).equals("("))
            {
              correct_operator_syntax++;
            }
          }
        }
        b++;
        c++;
      }
      //Resetting integer b and c so it can be used again later
      b = 0;
      c = 1;

      for (int e = 0; e < length; e++)
      {
        String substring_input = theinput.substring(b,c);
        if (substring_input.matches("[+*/^-]*$"))
        {
          num_operators++;
        }
        b++;
        c++;
      }

      //If num_operators and correct operator syntax is equal that means everything is okay. If not, it means something in the function has incorrect syntax
      if (num_operators != correct_operator_syntax)
      {
        throw new NumberFormatException();
      }
    }
    catch(IOException exception1)
    {
      System.out.println(exception1 + ":" + " The parentheses are not balanced. There are missing open/closing parentheses. Please correct the syntax and re-input the equation.");
    }
    catch(NoSuchFieldException exception2)
    {
      System.out.println(exception2 + ":" + " The beginning of the equation contains incorrect syntax. Only a digit or a ( is acceptable to begin an equation. Please correct the syntax and re-input the equation.");
    }
    catch (ArithmeticException exception3)
    {
      System.out.println(exception3 + ":" + " There are invalid characters present that is causing the equation syntax to be wrong. Please correct the syntax and re-input the equation.");
    }
    catch(NumberFormatException exception4)
    {
      System.out.println(exception4 + ":" + " There is incorrect operation syntax causing the equation syntax to be wrong. Please correct the syntax and re-input the equation.");
    }

    //Resetting integer b and c so it can be used again later
    b = 0;
    c = 1;

    //Now here, we will be utilizing regular expression, stacks and precedence (PEMDAS)
    for (int f = 0; f < length; f++)
    {
      String substring_input = theinput.substring(b,c);
      //Check if the character being assessed is a digit if the character after that is also a digit so it can be grouped together
      if (substring_input.matches("^[\\d]*$"))
      {
        int g = b;
        int h = c;
        String number = substring_input;
        while (theinput.substring(g,h).matches("^[\\d]*$") && h<length)
        {
          if (theinput.substring(g+1, h+1).matches("^[\\d]*$"))
          {
            String number_cont = theinput.substring(g+1, h+1);
            number = number.concat(number_cont);
            b = g + 1;
            c = h + 1;
          }
          if (!theinput.substring(g+1, h+1).matches("^[\\d]*$"))
          {
            break;
          }
          g++;
          h++;
        }
        //adding number to the number stack
        numeric_stack.push(number);

      }

      if (substring_input.equals("^"))
      {
        //Taking a peek at the stack to see what is on top
        operator_stack.peek();
        //If the stack is empty
        if(peek_operator == null)
        {
          operator_stack.push(substring_input);
        }

        //If there is something on the stack
        if (peek_operator != null)
        {
          //If the input operator is higher precedence than operator currently on top of the stack so just add it to stack
          //For ^: this includes (, *, /, +, -
          if(peek_operator.matches("^[+(*/-]*$"))
          {
            //adding it to the operator stack
            operator_stack.push(substring_input);
          }

          //For exponents if it is still same precedence just push onto stack this is an exception from the usual
          //Exponent can't be less than anything on the top of the stack as it is the highest precedence
          if (peek_operator.equals("^"))
          {
            operator_stack.push(substring_input);
          }
        }
      }

      if (substring_input.equals("("))
      {
        operator_stack.peek();
        //If the stack is empty
        if (peek_operator == null)
        {
          operator_stack.push(substring_input);
        }
        //If there is something on the stack
        if(peek_operator != null)
        {
          //Parentheses is a unique case it will act like this until a ) appears and it is removed from the stack
          //Higher precedence than operator currently on top of the stack so just add it to stack or the same precedence as parentheses is a special case
          //Parentheses will evaluate once it finds the )
          if(peek_operator.matches("^[+(*/^-]*$"))
          {
            operator_stack.push(substring_input);
          }
        }
      }

      //This will never be pushed into the stack. This indicates to the stack to keep solving until it reaches the (. Then it pops off the (.
      if (substring_input.equals(")"))
      {
        while (operator_stack.isEmpty() == false)
        {
          //will take a look at the top of the stack with each pass
          operator_stack.peek();
          if(peek_operator.equals("^"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = Math.pow(number1, number2);
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if(peek_operator.equals("*"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = number1 * number2;
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if(peek_operator.equals("/"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = number1/number2;
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if(peek_operator.equals("+"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = number1 + number2;
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if(peek_operator.equals("-"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = number1 - number2;
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if (peek_operator.equals("("))
          {
            operator_stack.pop();
            break;
          }
        }
      }

      if(substring_input.equals("*"))
      {
        //Taking a peek at the stack to see what is on top
        operator_stack.peek();
        //If the stack is empty
        if (peek_operator == null)
        {
          //just add it to the stack
          operator_stack.push(substring_input);
        }

        //If the stack is not empty
        if(peek_operator != null)
        {
          //If the top of the stack is ( as this is an exception for the usual precedence rule
          if(peek_operator.equals("("))
          {
            operator_stack.push(substring_input);
          }

          //If the input operator is of higher precedence than what is on the top of the stack
          if(peek_operator.matches("^[+-]*$"))
          {
            operator_stack.push(substring_input);
          }

          //If the input operator is of lower precedence or equal precedence that what is on the stack
          //For * this includes: ^, *, and /
          if(peek_operator.matches("^[*/^]*$"))
          {
            while(operator_stack.isEmpty() == false)
            {
              //will take a look at the top of the stack with each pass
              operator_stack.peek();

              if(peek_operator.equals("^"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = Math.pow(number1, number2);
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if(peek_operator.equals("*"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1 * number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("/"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1/number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.matches("[+(-]*$"))
              {
                operator_stack.push(substring_input);
                break;
              }
            }
            //if it had to evaluate everything and become empty we push value onto the empty operator stack
            if(operator_stack.isEmpty() == true)
            {
              operator_stack.push(substring_input);
            }
          }
        }
      }

      if(substring_input.equals("/"))
      {
        //Taking a peek at the stack to see what is on top
        operator_stack.peek();
        //If the stack is empty
        if (peek_operator == null)
        {
          //just add it to the stack
          operator_stack.push(substring_input);
        }

        //If the stack is not empty
        if(peek_operator != null)
        {
          //If the top of the stack is ( as this is an exception for the usual precedence rule
          if(peek_operator.equals("("))
          {
            operator_stack.push(substring_input);
          }

          //If the input operator is of higher precedence than what is on the top of the stack
          if(peek_operator.matches("^[+-]*$"))
          {
            operator_stack.push(substring_input);
          }

          //If the input operator is of lower precedence or equal precedence that what is on the stack
          //For * this includes: ^, *, and /
          if(peek_operator.matches("^[*/^]*$"))
          {
            while(operator_stack.isEmpty() == false)
            {
              //will take a look at the top of the stack with each pass
              operator_stack.peek();

              if(peek_operator.equals("^"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = Math.pow(number1, number2);
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if(peek_operator.equals("*"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1 * number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("/"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1/number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.matches("^[+(-]*$"))
              {
                operator_stack.push(substring_input);
                break;
              }
            }
            //if it had to evaluate everything and become empty we push value onto the empty operator stack
            if(operator_stack.isEmpty() == true)
            {
              operator_stack.push(substring_input);
            }
          }
        }
      }

      if(substring_input.equals("+"))
      {
        //Taking a peek at the stack to see what is on top
        operator_stack.peek();
        //If the stack is empty
        if (peek_operator == null)
        {
          //just add it to the stack
          operator_stack.push(substring_input);
        }

        //If the stack is not empty
        if(peek_operator != null)
        {
          //If the top of the stack is ( as this is an exception for the usual precedence rule
          if(peek_operator.equals("("))
          {
            operator_stack.push(substring_input);
          }

          //If the input operator is of higher precedence than what is on the stack not possible for + and - as those are assigned the lowest precedence for operators!

          //If the input operator is of lower precedence or equal precedence that what is on the stack
          //For + this includes: +, -, /,*, ^
          if(peek_operator.matches("^[+*/^-]*$"))
          {
            while(operator_stack.isEmpty() == false)
            {
              //will take a look at the top of the stack with each pass
              operator_stack.peek();
              if(peek_operator.equals("^"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = Math.pow(number1, number2);
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if(peek_operator.equals("*"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1 * number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("/"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1/number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("+"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1 + number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("-"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1 - number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("("))
              {
                operator_stack.push(substring_input);
                break;
              }
            }
            //if it had to evaluate everything and become empty we push value onto the empty operator stack
            if(operator_stack.isEmpty() == true)
            {
              operator_stack.push(substring_input);
            }
          }
        }
      }

      if(substring_input.equals("-"))
      {
        //Taking a peek at the stack to see what is on top
        operator_stack.peek();
        //If the stack is empty
        if (peek_operator == null)
        {
          //just add it to the stack
          operator_stack.push(substring_input);
        }

        //If the stack is not empty
        if(peek_operator != null)
        {
          //If the top of the stack is ( as this is an exception for the usual precedence rule
          if(peek_operator.equals("("))
          {
            operator_stack.push(substring_input);
          }

          //If the input operator is of higher precedence than what is on the stack not possible for + and - as those are assigned the lowest precedence for operators!

          //If the input operator is of lower precedence or equal precedence that what is on the stack
          //For + this includes: +, -, /,*, ^
          if(peek_operator.matches("^[+*/^-]*$"))
          {
            while(operator_stack.isEmpty() == false)
            {
              //will take a look at the top of the stack with each pass
              operator_stack.peek();
              if(peek_operator.equals("^"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = Math.pow(number1, number2);
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if(peek_operator.equals("*"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1 * number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("/"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1/number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("+"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1 + number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("-"))
              {
                numeric_stack.pop();
                double number2 = Double.valueOf(pop_numeric);
                numeric_stack.pop();
                double number1 = Double.valueOf(pop_numeric);
                operator_stack.pop();
                double evaluate = number1 - number2;
                String result = Double.toString(evaluate);
                numeric_stack.push(result);
              }

              if (peek_operator.equals("("))
              {
                operator_stack.push(substring_input);
                break;
              }
            }
            //if it had to evaluate everything and become empty we push value onto the empty operator stack
            if(operator_stack.isEmpty() == true)
            {
              operator_stack.push(substring_input);
            }
          }
        }
      }

      //When it reaches the end of the input string, pop off any operators and operands and solve
      if (c==length)
      {
        while(operator_stack.isEmpty() == false)
        {
          //will take a look at the top of the stack with each pass
          operator_stack.peek();
          if(peek_operator.equals("^"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = Math.pow(number1, number2);
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if(peek_operator.equals("*"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = number1 * number2;
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if(peek_operator.equals("/"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = number1/number2;
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if(peek_operator.equals("+"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = number1 + number2;
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if(peek_operator.equals("-"))
          {
            numeric_stack.pop();
            double number2 = Double.valueOf(pop_numeric);
            numeric_stack.pop();
            double number1 = Double.valueOf(pop_numeric);
            operator_stack.pop();
            double evaluate = number1 - number2;
            String result = Double.toString(evaluate);
            numeric_stack.push(result);
          }

          if (peek_operator.equals("("))
          {
            operator_stack.pop();
          }
        }

        //Popping the final result off the stack.
        numeric_stack.pop();

        //If there is still something remaining after the result should have been popped off and the stack is empty, throw an exception. If not, just print result.
        try
        {
          if (numeric_stack.isEmpty() == true)
          {
            System.out.println("Result: " + pop_numeric);
          }
          else
          {
            throw new ArithmeticException();
          }
        }
        catch(ArithmeticException exception)
        {
          System.out.println(exception + ":" + " There has been a calculation error indicating that the equation syntax is wrong. Please check the equation syntax, correct it, and try again.");
        }

        break;
      }
      else
      {
        b++;
        c++;
      }
    }
  }
  public static void main(String[] args)
  {
    //Will use this before making an input to ensure stack and order of precedence are working correctly
    String testequation = "( ( 3 + 2 * 4) * 4)/ ( 5 - 1 ) ";

    calculator mycalculator = new calculator();
    mycalculator.inputequation();
    mycalculator.assess(mycalculator.input);
  }
}
