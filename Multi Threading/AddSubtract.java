import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


// QUESTION: We have a global variable 0. We add 1-100 to it and subtract 1-100 to it. The final output must be 0.

class Value {
    int value;
    public Value(int value) {
        this.value = value;
    }
}

class Adder implements Callable<Void> {
    Value val;

    public Adder(Value val) {
        this.val = val;
    }

    @Override
    public Void call() {
        for(int i=1; i<=10000; i++) {
            this.val.value += i;
        }
        return null;
    }
}

class Subtractor implements Callable<Void> {
    Value val;

    public Subtractor(Value val) {
        this.val = val;
    }

    @Override
    public Void call() {
        for(int i=1; i<=10000; i++) {
            this.val.value -=i;
        }
        return null;
    }
}

public class AddSubtract {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Value val = new Value(0);
        Adder adderTask = new Adder(val);
        Subtractor subtractorTask = new Subtractor(val);

        ExecutorService es = Executors.newFixedThreadPool(5);
        Future<Void> adderFuture = es.submit(adderTask);
        Future<Void> subtractorFuture = es.submit(subtractorTask);

        adderFuture.get();
        subtractorFuture.get();
        System.out.println(val.value);
    }
}

/*
 The single statement of incrementing or decrementing implies 3 tasks:
 x = read(this.val.value)    => reading the value
 (Register) r = x+i or x-i   => storing it in the register
 x -> r                      => re-writing the value of x from what is stored in the register

 If we do it in a small loop the probability of getting a 0 as output increases and for a big loop the probability of getting a 0 decreases.
 This is because, if the task is smaller, the CPU will not get the time shared among different threads.
*/

/* 
 Here , we have the problem of SYNCHRONIZATION. Multiple threads are trying to access the same variable at the same time.
 Editing the data is not a single step process / ATOMIC PROCESS. It includes reading the data, modifying it and then writing it back.
 Beacuse of this, we have inconsistency in the data. And hence we get a random output.

 The value of value in above exapmle can range anywhere from -5050 to 5050.

 Conditions for synchronization issues to occur:
    1. critical section - the part of the code where the shared resource is accessed.
    2. race condition - when two or more threads try to access the shared resource at the same time.
    3. os is pre-emptive - it can stop a thread at any time and start another thread. (presence of race condition ensures this).

 How to remove synchronization issues:
    1. mutual exclusion - only one thread can access the shared resource at a time => no race condition.
    2. progress - overall progress of the system should not be stopped.
    3. bounded wait - every thread should be allowed to access the shawaiting for the shared resource to be free.red resource at some point of time. -- fcfs queue.
    4. no busy waiting - no thread should be checking the shared resource continuously for its availability.

 Solutions to solve synchronization issues:
    1. Mutex (mutual exclusion) - lock (explicit lock - lock interface - lock() and unlock() methods)
    2. using implicit lock of object class - synchronized keyword - to avoid passing a common lock object to the threads everytime.
*/