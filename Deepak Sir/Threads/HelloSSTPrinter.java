/* 
 We cannnot craete an object of INTERFACE, because interfaces are abstract and the functions inside them are not implemented. Since, Runnable is an interface, we cannot  create its object, but we can create a class that implements the Runnable Interface. 
 When we implement an Interface, we have to run all the absract methods of that interface.
*/

public class HelloSSTPrinter implements Runnable {

    @Override
    public void run() {
        // The code that we want to execute via a different thread.sout
        System.out.println("Hello SST :) , Printed by: " + Thread.currentThread().getName());
    }
    
}
