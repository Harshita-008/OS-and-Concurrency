/*
 QUESTION:

 5 places for product storage
 3 producers -> they can keep any number of products just ensuring that there is empty space
 any number of consumers -> if a consumer comes, it is guaranteed they will consume the product

 Ensure:
 - no more then 5 products go inisde the block
 - consumer only comes if there is atleast one product inside the block

 AT MAX:
 - 5 producers
 - 5 consumers

 Any number of producers and consumers can enter the store.

 Synschronization issue can arise when both producer and consumer are present inside the block at the same time.
 Producer knows that there are 2 empty spaces, but if a consumer comes at the same time and takes a product, the number of empty spaces may increase, and similar issues vice versa.
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class Store {
    int maxSize;
    int currSize;

    public Store(int maxSize) {
        this.maxSize = maxSize;
        this.currSize = 0;
    }
}

class Producer implements Runnable {
    Store store;
    int id;
    Semaphore pSemaphore;
    Semaphore cSemaphore;

    public Producer(Store store, int id, Semaphore pSemaphore, Semaphore cSemaphore) {
        this.store = store;
        this.id = id;
        this.pSemaphore = pSemaphore;
        this.cSemaphore = cSemaphore;
    }

    @Override
    public void run() {
        while(true) {
            // producer's semaphore--
            try {
                pSemaphore.acquire();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }

            if(this.store.currSize < this.store.maxSize) {
                System.out.println("Producing an item by producer #: " + this.id);
                this.store.currSize++;
                System.out.println("Current itemCount in Store after addition "+store.currSize);
            }

            // consumer's semaphore++
            cSemaphore.release();
        }
    }
}

class Consumer implements Runnable {
    Store store;
    int id;
    Semaphore pSemaphore;
    Semaphore cSemaphore;

    public Consumer(Store store, int id, Semaphore pSemaphore, Semaphore cSemaphore) {
        this.store = store;
        this.id = id;
        this.pSemaphore = pSemaphore;
        this.cSemaphore = cSemaphore;
    }

    @Override
    public void run() {
        while(true) {
            // consumer's semaphore--
            try {
                cSemaphore.acquire();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }

            if(this.store.currSize > 0){
                System.out.println("Consuming by Consumer #: " + this.id);
                this.store.currSize--;
                System.out.println("Current itemCount in Store after poll "+store.currSize);
            }

            // producer's semaphore++
            pSemaphore.release();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Semaphore pSemaphore = new Semaphore(3);
        Semaphore cSemaphore = new Semaphore(0);

        Store store = new Store(3);
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i=0; i<100; i++){
            Producer p = new Producer(store, i, pSemaphore, cSemaphore);
            es.execute(p);
        }
        for(int i=0; i<3; i++){
            Consumer c = new Consumer(store, i, pSemaphore, cSemaphore);
            es.execute(c);
        }
        es.shutdown();
    }
}
