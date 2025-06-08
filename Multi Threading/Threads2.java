import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// CALLABLE: When we want to return an object.

// (1) write a code to return a list having 2x the elements of original list and return the new list.
//Ex : <1,2,3> --> <2,4,6>
class ListModifier implements Callable<ArrayList<Integer>> {
    ArrayList<Integer> list;

    public ListModifier(ArrayList<Integer> list) {
        this.list = list;
    }

    @Override
    public ArrayList<Integer> call() throws Exception {
        ArrayList<Integer> doubledList = new ArrayList<>();
        for(int i=0; i<list.size(); i++) {
            doubledList.add(2 * list.get(i));
        }
        return doubledList;
    }
}
    
public class Threads2 {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);  // at max it will create 10 threads
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            list.add(i);
        }
        ListModifier task = new ListModifier(list);
        Future<ArrayList<Integer>> doubledList = es.submit(task);  //this line is done by second thread
        
        try {
            System.out.println(doubledList.get());
        } catch (Exception e) {
            System.out.println("Exception occured");
        }

        // es, list<>, task => runs in the main thread
        // es.submit() => runs in a different thread (Thread-0) which is craeted by the es
        // So, when this arraylist is being created in another thread, the main thread still runs in parallel and the execution moves to the next line. The main thraed never gets the arraylist that is returned. So, we need a promise from the thread that is created, that whatever is returned later on, we will get it in the main thraed. So, for the time being we get a token Future<>. As soon ans something is returned it gets filled inside the variable that is inside the Future<> token.
        // When we print, whenever the print line executes, its not guaranteed that the arraylist is returned. So we night not get the correct answer printed. So, we use the .get() function, which waits to execute until the output is returned from the ExecutorService.


        // es.submit() -> returns an outpur
        // es.execute() -> when we don't want to return anything, maybe just simply print something
    }
}
