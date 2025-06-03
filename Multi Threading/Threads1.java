import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// MULTI THREADING
// Create a class of the task you want to perform and assign that task to a thread.

// (1) Print Hello World.

import java.util.ArrayList;

class HelloWorldPrinter extends Thread {
    @Override
    public void run() {
        System.out.println("Hello World :) " + Thread.currentThread().getName());
    }
}


// (2) Print numbers from 1 - 10 using a different thread.
class NumberPrinter extends Thread {
    @Override
    public void run() {
        for(int i=1; i<=10; i++) {
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex) {}
            System.out.println(i + " " + Thread.currentThread().getName());
        }
    }
}
// Output is obtained in a random order. Since the work happens in parallel, it depends upon the CPU scheduler, how the processes in the queue get scheduled.




// (3) Print numbers from 1 - 100 each with a different thread.

// APPROACH -1:
// We cannot rely on this method to print all the numbers. Some numbers might be missing or some might be printed more than once. This is because since all the threads are running in parallel, it might happen that 2 threads print the same value and increment at the same time.
class SingleNumberPrinter extends Thread {
    // Making the variable static means same variable will be shared amongst all the objects of this class.
    static int num = 0;
    @Override
    public void run() {
        num++;
        System.out.println(num + " " + Thread.currentThread().getName());
        // num++;
    }
}

// CORRECT APPROACH-1:
// extends Thread:
// Thread class alread has a run method. We make another run method that overrides the actual run method to perform tasks that we want to perform.
class SingleNumberPrinterV1 extends Thread {
    int num;

    public SingleNumberPrinterV1(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        System.out.println(num + " " + Thread.currentThread().getName());
        // num++;
    }
}


// CORRECT APPROACH-2:
// Runnable is an Interface. We have to implement all the methods present inside the interface or else we will keep getting compilation error. These methods that we have to implement are Abstract methods in the interface.
// Runnable is implemented in a class, and we pass the object of this class as a parameter to the object of the Thread class.
class SingleNumberPrinterV2 implements Runnable {
    int num;

    public SingleNumberPrinterV2(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        System.out.println(num + " " + Thread.currentThread().getName());
    }
}

// ADVANTAGE OF THREADS: If there is a problem in implementing one of the threads, it won't affect the implementation of the rest of the threads. It might throw an exception where it catches the error, but the rest of the processes in the other threads continue as normal.



// // (4) Create a student class with run method.
// class Student extends Thread {
//     @Override
//     public void run() {

//     }
// }



// (5) Write a code to return a list having 2x the elements of original list and return the new list.
//Ex : <1,2,3> --> <2,4,6>
class ListModifier extends Thread{
    ArrayList<Integer> nums;

    public ListModifier(ArrayList<Integer> nums){
        this.nums = nums;
    }

    @Override
    public void run(){
        for(int i=0 ; i<nums.size() ; i++){
            this.nums.set(i , nums.get(i)*2);
        }
        System.out.println(Thread.currentThread().getName());
    }
}
/* 
Here we can get three kinds of outputs:
CASE 1: main prints before thread modifies list
[1, 2, 3]
[1, 2, 3]
Thread-0

CASE 2: thread finishes before main prints again
[1, 2, 3]
[2, 4, 6]
Thread-0

CASE 3: interleaved execution
[1, 2, 3]
Thread-0
[2, 4, 6]
*/


public class Threads1 {
    public static void main(String[] args) {
        // // (1)
        // System.out.println("Hello World " + Thread.currentThread().getName());

        // HelloWorldPrinter hwp = new HelloWorldPrinter();
        // hwp.run();     // Runs on the main thread
        // hwp.start();   // Creates a new thread and runs on that thread

        // HelloWorldPrinter hwp2 = new HelloWorldPrinter();
        // hwp2.start();


        // // (2)       
        // NumberPrinter np1 = new NumberPrinter();
        // np1.start();
        // NumberPrinter np2 = new NumberPrinter();
        // np2.start();


        // (3)
        // // extends Thread
        // for(int i=1; i<=100; i++) {
        //     SingleNumberPrinterV1 snp = new SingleNumberPrinterV1(i);
        //     snp.start();
        // }

        // // implements Runnable
        // for(int i=1; i<=100; i++) {
        //     // Thread th = new Thread(new SingleNumberPrinterV2(i));

        //     SingleNumberPrinterV2 task = new SingleNumberPrinterV2(i);
        //     Thread th = new Thread(task);
        //     th.start();
        // }

        // // ExecutorService
        /*
        Executors.newFixedThreadPool(int n): Creates a fixed number of threads (n) and the process takes place in these number of threads. Threads are reused. If all threads are busy, new tasks wait in a queue.

        Executors.newCachedThreadPool(): Creates new threads as needed (unbounded pool). Reuses idle threads if available. Removes threads that are idle for 60 seconds.

        Executors.newSingleThreadExecutor(): Only one thread executes tasks. Tasks are queued if a task is running. Ensures sequential execution.

        Executors.newScheduledThreadPool(int corePoolSize): Creates a pool of threads that can schedule tasks. Can run task, after a fixed delay, at a fixed rate.
        */
        // ExecutorService es = Executors.newFixedThreadPool(10);
        // for(int i=0 ; i<=100 ; i++){
        //     es.submit(new SingleNumberPrinterV2(i));
        // }


        // (5)
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list);
        ListModifier lm = new ListModifier(list);
        lm.start();
        System.out.println(list);
    }
}