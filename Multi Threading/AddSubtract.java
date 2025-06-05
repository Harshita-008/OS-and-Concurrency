import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// QUESTION: We have a global variable 0. We add 1-100 to it and subtract 1-100 to it. The final output must be 0.

class Value {
    int value;
    public Value(int value) {
        this.value = value;
    }
}

// ADDER

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

class AdderWithLock implements Callable<Void> {
    Value val;
    Lock lock;

    public AdderWithLock(Value val, Lock lock) {
        this.val = val;
        this.lock = lock;
    }

    @Override
    public Void call() {
        for(int i=1; i<=10000; i++) {
            lock.lock();
            this.val.value += i;  // Critical Section => modifies the shared data
            lock.unlock();
        }
        return null;
    }
}

class AdderWithSynchronization implements Callable<Void> {
    Value val;

    public AdderWithSynchronization(Value val) {
        this.val = val;
    }

    @Override
    public Void call() {
        for(int i=1; i<=10000; i++) {
            synchronized(val) {
                this.val.value += i;
            }
        }
        return null;
    }
}


// SUBTRACTOR

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

class SubtractorWithLock implements Callable<Void> {
    Value val;
    Lock lock;

    public SubtractorWithLock(Value val, Lock lock) {
        this.val = val;
        this.lock = lock;
    }

    @Override
    public Void call() {
        for(int i=1; i<=10000; i++) {
            lock.lock();
            try {
                val.value -= i;
            } finally {
                lock.unlock();
            }
        }
        return null;
    }
}

class SubtractorWithSynchronization implements Callable<Void> {
    Value val;

    public SubtractorWithSynchronization(Value val) {
        this.val = val;
    }

    @Override
    public Void call() {
        for(int i=1; i<=10000; i++) {
            synchronized(val) {
                this.val.value -=i;
            }
        }
        return null;
    }
}

public class AddSubtract {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // WITHOUT LOCK:
        Value val = new Value(0);
        Adder adderTask = new Adder(val);
        Subtractor subtractorTask = new Subtractor(val);

        ExecutorService es = Executors.newFixedThreadPool(5);
        Future<Void> adderFuture = es.submit(adderTask);
        Future<Void> subtractorFuture = es.submit(subtractorTask);

        adderFuture.get();
        subtractorFuture.get();
        System.out.println("Without Lock: " + val.value);
        
        
        // WITH LOCK:
        // PROBLEM: Even though lock gives a lot of flexibility, everytime to get rid of synchronization problem, with this solution we need to modify the class and the constructor for any code.
        val.value = 0;
        Lock lock = new ReentrantLock();  // shared variable lock for both the classes => we want to keep the 2 threads in the same queue of the same lock
        AdderWithLock adderTaskWithLock = new AdderWithLock(val, lock);
        SubtractorWithLock subtractorTaskWithLock = new SubtractorWithLock(val, lock);
        
        Future<Void> adderFutureWithLock = es.submit(adderTaskWithLock);
        Future<Void> subtractorFutureWithLock = es.submit(subtractorTaskWithLock);
        
        adderFutureWithLock.get();
        subtractorFutureWithLock.get();
        System.out.println("With Lock: " + val.value);


        // WITH SYNCHRONIZATION:
        val.value = 0;  
        AdderWithSynchronization adderWithSynchronized = new AdderWithSynchronization(val);
        SubtractorWithSynchronization subtracterWithSynchronized = new SubtractorWithSynchronization(val);

        Future<Void> addedWithSynchronizedFuture = es.submit(adderWithSynchronized);
        Future<Void> subtractedWithSynchronizedFuture = es.submit(subtracterWithSynchronized);

        addedWithSynchronizedFuture.get();
        subtractedWithSynchronizedFuture.get();
        System.out.println("With Synchronization: " + val.value);
        

        es.shutdown();
    }
}

/*
 JAVA IS ONE OF THE BEST PROGRAMMING LANGUAGE FOR MULTITHREADING:
 In the object of java, it provides us a way to make it threat safe. Every class in java has a superclass called Object. The Object class that is by deafult the superclass of all the objects that we create in java has it's own implicit lock in-build which can avoid this creation of lock. There are ways by which whatever is implemented using the Lock object can be implemented by the java object.
 => SYNCHRONIZED KEYWORD
*/

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
    1. CRITICAL SECTION - The part of the code where the shared resource is accessed for reading or writing.
    2. RACE CONDITION - When two or more threads try to access the shared resource (critical section) at the same time.
    3. OS IS PREEMPTIVE - It can stop a thread at any time and start another thread. (single core system) (presence of race condition ensures this). Multicore processors can run different threads even if the OS is non-preemptive

 How to remove synchronization issues:
    1. MUTUAL EXCLUSION - only one thread => can access the shared resource at a time / executes the critical section => no race condition.
    2. PROGRESS - Overall progress of the system should not be stopped.
    3. BOUNDED WAIT - Every thread should be allowed to access the shared resource (critical section). Waiting for the shared resource to be free. We don't want any starvation or deadlock. In case of priority some thread might never get to access the critical section at any point of time. So, to access the critical section at some point of time, easiest way is FCFS queue.
    4. NO BUSY WAITING - No thread should be checking the shared resource continuously for its availability.

 Solutions to solve synchronization issues:
    1. Mutex (Mutual Exclusion) - lock (explicit lock - lock interface - lock() and unlock() methods) => locks the critical section and only one key is present to open the lock.
    Lock lock = new ReentrantLock();
    lock.lock();
    {Critical Section Code}
    lock.unlock();
    2. using implicit lock of object class - synchronized keyword - to avoid passing a common lock object to the threads everytime.
*/