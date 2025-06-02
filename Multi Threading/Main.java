// MULTI THREADING
// Create a class of the task you want to perform and assign that task to a thread.

// (1) Print Hello World.
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

// ADVANTAGE OF THEREADS: If there is a problem in implementing one of the threads, it won't affect the implementation of the rest of the threads. It might throw an exception where it catches the error, but the rest of the processes in the other threads continue as normal.


// (4) Create a student class with run method.
class Student extends Thread {
    @Override
    public void run() {

    }
}

public class Main {
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
        // implements Runnable
        for(int i=1; i<=100; i++) {
            // Thread th = new Thread(new SingleNumberPrinterV2(i));

            SingleNumberPrinterV2 task = new SingleNumberPrinterV2(i);
            Thread th = new Thread(task);
            th.start();
        }
    }
}