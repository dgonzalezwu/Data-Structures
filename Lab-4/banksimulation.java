/*
@name Danielle Gonzalez-Wu
@date 4/30/2021
@course CSC201
 */

package com.company;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class banksimulation
{
  //We are creating an array of linked lists
  public class queue_ll
  {
    //inner class of nodes
    public class node
    {
      public Integer customer;
      public Integer arrival_time;
      public Integer duration_of_services;
      public Integer duration_of_services_timer;
      public node next; //pointer to link it together
      public node last; //pointer to point to last node from the first node

      node()
      {
        next = null;
        last = null;
      }

      node(Integer customer, Integer arrival_time, Integer duration_of_services, Integer duration_of_services_timer)
      {
        this.customer = customer;
        this.arrival_time = arrival_time;
        this.duration_of_services = duration_of_services;
        this.duration_of_services_timer = duration_of_services_timer;
        next = null;
      }
    }

    //Same as first = null except that I am making the first.next = null because I usually do that so I understand it a bit better that way
    node first = new node(); //linked list header node
    int N = 0; //number of items on list
    //Decided to not include the data for the duration of services timer, as this is just to make sure customer finishes task and is equivalent to duration_of_services information already inserted
    Integer timer;
    String item_customer, item_arrival_time, item_duration_of_services, item;
    String peek, peek_customer, peek_arrival_time, peek_duration_of_services;

    public int size()
    {
      return N;
    }

    public boolean isEmpty()
    {
      return size() == 0;
    }

    //Helper method for debugging
    public void getall()
    {
      for (node x = first.next; x != null; x = x.next)
      {
        System.out.println("["+ x.customer + "," +  x.arrival_time + "," + x.duration_of_services + "]");
      }
    }

    //Enqueue (add_last)
    public void enqueue(Integer customer, Integer arrival_time, Integer duration_of_services, Integer duration_of_services_timer)
    {
      if (first.next == null)
      {
        first.next = new node(customer, arrival_time, duration_of_services, duration_of_services_timer);
        first.last = first.next; //pointing last pointer to the new node
        N++; //incrementing size of customers in the line
      }
      else
      {
        //trying to make it easier with the last node so we do not need a while loop anymore to save time
        node temp = first.last;
        //Last node should always be pointing to null so this is how we know our last node is pointing to the right thing
        if (first.last.next == null)
        {
          node newnode = new node(customer, arrival_time, duration_of_services, duration_of_services_timer);
          newnode.next = temp.next;
          temp.next = newnode;
          //Pointing last to new last node.
          first.last = newnode;
          N++; //incrementing number of customers in the line
        }
      }
    }

    //Dequeue (delete_first)
    public String dequeue()
    {
      try
      {
        if (first.next == null) //case: list is empty
        {
          //Making exception here to let the user know no accounts exist according to file
          throw new NullPointerException();
        }
        else //case: list is not empty at least one node on the list
        {
          item_customer = Integer.toString(first.next.customer);
          item_arrival_time = Integer.toString(first.next.arrival_time);
          item_duration_of_services = Integer.toString(first.next.duration_of_services);
          item = item_customer + "," + item_arrival_time + "," + item_duration_of_services;
          node temp = first.next; //creating a temporary node that points to where the first node is also pointing to
          first.next = temp.next; //moving the first node's pointer to the next node pointer effectively removing the original head from the list
          N--; //decrement amount of customers in line
        }
      }
      catch(NullPointerException exception)
      {
        System.out.println(exception + ":" + " The list is empty, so there is nothing to remove.");
      }
      return item;
    }

    public String peek() //custom exception
    {
      try
      {
        if (first.next == null) //case: list is empty
        {
          //Making exception here to let the user know no accounts exist according to file
          throw new NullPointerException();

        }
        else //case: list is not empty at least one node on the list
        {
          peek_customer = Integer.toString(first.next.customer); //data for the element in the first node
          peek_arrival_time = Integer.toString(first.next.arrival_time); //data for the element in the first node
          peek_duration_of_services = Integer.toString(first.next.duration_of_services); //data for the element in the first node
          peek = peek_customer + "," + peek_arrival_time + "," + peek_duration_of_services;
        }
      }
      catch(NullPointerException exception)
      {
        //Commenting out as it is not an error, this is acceptable for our simulation. We want to ignore this exception printing in this case.
        //System.out.println(exception + ":" + " The list is empty, so there is nothing to return.");
      }
      return peek;
    }

    //search function to find if the appropriate times to take them off list is present
    public boolean search_arrival_time(Integer appropiate_time)
    {
      if (first.next == null)
      {
        throw new NullPointerException("The list is empty, so there is nothing to search for.");
      }
      else
      {
        node temp = first.next;
        while (temp.next != null)
        {
          if (temp.arrival_time == appropiate_time)
          {
            return true;
          }
          temp = temp.next;
        }
        return false;
      }
    }

    public Integer check_timer()
    {
      if (first.next == null)
      {
        throw new NullPointerException("The list is empty, so there are no customers to check on to see how much time they have left on their transaction.");
      }
      else
      {
        //This will be like again peeking at the first node on the list to see where the duration of services is at
        timer = first.next.duration_of_services_timer;
      }
      return timer;
    }

    public void decrement_timer()
    {
      //In our case, the timer will only change for the customer currently being helped which will be the first node on the list, so we should not need to traverse the list.
      //Only decrement when it is still greater than 0 because by 0 the customer is done and should be processed out
      if (first.next == null)
      {
        throw new NullPointerException("The list is empty, so there are no customers on the line to decrement the transaction time remaining.");
      }
      else
      {
        if (first.next.duration_of_services_timer > 0)
        {
          first.next.duration_of_services_timer = ((first.next.duration_of_services_timer) - 1);
        }
      }
    }
  }

  public class teller_array
  {
    private int M; //the size of the table (amount of tellers)
    private queue_ll[] table;
    //Create an instance of the queue class from above to use here

    //Creating a table size based on the amount of tellers we want to have
    public void teller_amount(int M)
    {
      this.M = M;
      //Creating the array with the amount of space for the amount of tellers we want
      table = (queue_ll[]) new queue_ll[M];
      //Creating a corresponding queue for each teller space which will act as a line for the customers
      for (int i = 0; i < M; i++)
      {
        table[i] = new queue_ll();
      }
    }

    //Size of the working tellers
    public int size()
    {
      return table.length;
    }

    public int queue_size(int i)
    {
      return table[i].size();
    }

    public void queue_getall(int i)
    {
      table[i].getall();
    }

    public boolean queue_isEmpty(int i)
    {
      return table[i].isEmpty();
    }

    public void enqueue(int i, Integer customer, Integer arrival_time, Integer duration_of_services, Integer duration_of_services_timer)
    {
      table[i].enqueue(customer, arrival_time, duration_of_services, duration_of_services_timer);
    }

    public String dequeue(int i)
    {
      return table[i].dequeue();
    }

    public String peek(int i)
    {
      return table[i].peek();
    }

    public Integer queue_check_timer(int i)
    {
      return table[i].check_timer();
    }

    public void queue_decrement_timer(int i)
    {
      table[i].decrement_timer();
    }

    //Probably will not use this wrapper function as its meant for the central line but including it anyways
    public boolean queue_search_arrival_time(int i, int appropriate_time)
    {
      return table[i].search_arrival_time(appropriate_time);
    }
  }

  //Function to run the simulation
  public void simulating(int number_of_tellers) throws java.io.FileNotFoundException
  {
    double process_time;
    double running_total = 0;
    double average_process_time;
    teller_array test= new teller_array();
    //Making the array of tellers based of the number we want to simulate
    test.teller_amount(number_of_tellers);
    //Reading in the list of customers, arrival_times, and processing times that we will simulate and storing this into a separate linked list
    queue_ll simulation_data = new queue_ll();
    Scanner data_file = new Scanner(new File("banksimulation.txt"));
    //Creating a central line of customers we will take them in when the time is appropriate
    while(data_file.hasNextLine())
    {
      String line = data_file.nextLine();
      String data_entries[] = line.split("\t\t");
      Integer customer = Integer.valueOf(data_entries[0]);
      Integer arrival_time = Integer.valueOf(data_entries[1]);
      Integer duration_of_service = Integer.valueOf(data_entries[2]);
      Integer duration_of_service_timer = Integer.valueOf(data_entries[2]);
      simulation_data.enqueue(customer, arrival_time, duration_of_service, duration_of_service_timer);
    }
    //Getting the total amount of customers on central line to be processed by the tellers
    int number_of_customers_processed = simulation_data.size();

    //Simulating time as real time would lead to much more complications so we make our own time in this case
    int time = 0;
    int total_customers_on_lines = 0;

    do {
      {
        //While there are still customers on the central line with a wait time that is ready to take them off the line
        while (simulation_data.search_arrival_time(time) == true)
        {
          //The customers arrive in time order so peeking at top and see if it matches appropriate time to take off line
          String get_customer_data = simulation_data.peek();
          String customer_data[] = get_customer_data.split(",");
          Integer customer_customer = Integer.valueOf(customer_data[0]);
          Integer customer_arrival_time = Integer.valueOf(customer_data[1]);
          Integer customer_duration_of_service = Integer.valueOf(customer_data[2]);
          Integer customer_duration_of_service_timer = Integer.valueOf(customer_data[2]);
          if(customer_arrival_time == time)
          {
            //Remove customer data off central line
            simulation_data.dequeue();
            //Find line with the lowest amount of customers through a loop. We start by assigning it to the first line and assigning the corresponding number to that line.
            int shortest_line = test.queue_size(0);
            int shortest_line_number = 0;
            for (int a = 1; a < number_of_tellers; a++)
            {
              //If current shortest line is greater than the next line we make the next line the new shortest line
              if(shortest_line > test.queue_size(a))
              {
                shortest_line = test.queue_size(a);
                shortest_line_number = a;
              }
            }
            //Put customer in shortest line we found
            test.enqueue(shortest_line_number, customer_customer, customer_arrival_time, customer_duration_of_service, customer_duration_of_service_timer);
          }
        }
        for (int b = 0; b < number_of_tellers; b++)
        {
          //Checking to see if each teller has a customer, this means they should have a queue size greater than 0.
          if(test.queue_size(b) > 0)
          {
            //Decrement the amount of time the customer has for the transaction
            test.queue_decrement_timer(b);
            //If it the timer is now at 0, the customers will be done and leave so process them out
            if(test.queue_check_timer(b) == 0)
            {
              //take them off the queue as they are leaving
              String get_leaving_customer_data = test.dequeue(b);
              String leaving_customer_data[] = get_leaving_customer_data.split(",");
              Integer leaving_customer_customer = Integer.valueOf(leaving_customer_data[0]);
              Integer leaving_customer_arrival_time = Integer.valueOf(leaving_customer_data[1]);
              Integer leaving_customer_duration_of_service = Integer.valueOf(leaving_customer_data[2]);

              //calculate the process time using leaving customer data (current_time – arrival_time) + transaction_time
              process_time = (time - leaving_customer_arrival_time) + leaving_customer_duration_of_service;

              //accumulating all the process time into the running total
              running_total += process_time;

            }
          }
        }

        //At end, calculate total customers on line and see if condition still holds and increment it by 1
        //Resetting total_customers_on_lines variables before calculating because it is accumulating
        total_customers_on_lines = 0;
        for (int c = 0; c < number_of_tellers; c++)
        {
          total_customers_on_lines += test.queue_size(c);
        }
        //Increment simulation time by 1 now that all tasks to be done within that time period is done
        time++;
      }
    }
    //Condition: repeat until no customers are present on any teller lines
    while(total_customers_on_lines != 0);

    //Now that we have broken out of loop calculate and print average processing time based on that number of tellers simulated. The equation running total / # of customers processed
    average_process_time = running_total/number_of_customers_processed;
    System.out.println(number_of_tellers + " teller average process time: " + average_process_time);
  }

  public static void main(String[] args)  throws FileNotFoundException
  {
    banksimulation test = new banksimulation();
    //Print out the average process time for 2 tellers all the way up to 10 tellers.
    test.simulating(2);
    test.simulating(3);
    test.simulating(4);
    test.simulating(5);
    test.simulating(6);
    test.simulating(7);
    test.simulating(8);
    test.simulating(9);
    test.simulating(10);
  }
}