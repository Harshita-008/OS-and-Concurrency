import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Sorter implements Callable<ArrayList<Integer>> {
    ArrayList<Integer> listToSort;

    public Sorter(ArrayList<Integer> listToSort) {
        this.listToSort = listToSort;
    }

    @Override
    public ArrayList<Integer> call() throws InterruptedException, ExecutionException {
        if(listToSort.size() <= 1) return listToSort;
        int mid = listToSort.size()/2;
        ArrayList<Integer> leftHalfToSort = getSubList(listToSort, 0, mid-1);
        ArrayList<Integer> rightHalfToSort = getSubList(listToSort, mid, listToSort.size()-1);

        // Executor Service to sort the two halves in parallel
        ExecutorService es = Executors.newCachedThreadPool();
        // ExecutorService es = Executors.newFixedThreadPool(5);

        // Create subtask with the two sublists to assign them to different threads
        Sorter leftSorter = new Sorter(leftHalfToSort);
        Sorter rightSorter = new Sorter(rightHalfToSort);

        // Execute this in multithreaded way
        Future<ArrayList<Integer>> leftSortedFuture = es.submit(leftSorter);
        Future<ArrayList<Integer>> rightSortedFuture = es.submit(rightSorter);

        ArrayList<Integer> leftSortedList = leftSortedFuture.get();
        ArrayList<Integer> rightSortedList = rightSortedFuture.get();

        return merge(leftSortedList, rightSortedList);
    }

    private ArrayList<Integer> merge(ArrayList<Integer> A, ArrayList<Integer> B) {
        ArrayList<Integer> mergedList = new ArrayList<>();
        int i=0, j=0;
        while(i<A.size() && j<B.size()) {
            if(A.get(i) < B.get(j)) {
                mergedList.add(A.get(i));
                i++;
            }
            else {
                mergedList.add(B.get(j));
                j++;
            }
        }

        while(i < A.size()) {
            mergedList.add(A.get(i));
            i++;
        }
        while(j < B.size()) {
            mergedList.add(B.get(j));
            j++;
        }

        return mergedList;
    }

    private ArrayList<Integer> getSubList(ArrayList<Integer> list, int s, int e) {
        ArrayList<Integer> sublist = new ArrayList<>();
        for(int i=s; i<=e; i++) {
            sublist.add(list.get(i));
        }
        return sublist;
    }
}

public class MergeSort {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();  // takes care of the number of threads created
        // ExecutorService es = Executors.newFixedThreadPool(5);

        ArrayList<Integer> list = new ArrayList<>();
        for(int i=10; i>=0; i--) {
            list.add(i);
        }

        Sorter sortingTask = new Sorter(list);
        Future<ArrayList<Integer>> sortedListFuture = es.submit(sortingTask);

        // Retrieve and print the result
        ArrayList<Integer> sortedList = sortedListFuture.get();
        System.out.println(sortedList);

        es.shutdown(); // Ensure the executor service is properly shut down
    }
}






/*
 Executor Service has: Thread Pool, Task Queue
 
 If we ask the ES to create 10 threads, it does not create all the threads all at once. Tt creates threads according to the required tasks it has. 

 MULTIPLE - ES:
 eg: We created an ES with 3 fixed threads. Initially, 2 task of sorting the 2 halves was assigned to 2 threads. Now, the sub halves of the 2 parts will also require 2 more threads 2 get sorted. So, for every sub-task we have, starting from the origianl array till the last element with subarray of size 1, we are creating a different ES that is creating a new thread.

 SINGLE - ES:
 Now, if we have a single ES to use throughout the code, we can optimize these number of threads. If one thread has completed its job, it can be reused to complete another task. Threads can be reued inside a single ES. 
 eg: We are craeting the ES of size 3. Now 2 threads are used to sort the 2 halves. Now, one of the half needs 2 more threads to sort its sub-halves. So, the sorting of these has been added to the task queue of the ES. But we have only 1 thread to do the process. So, only one half can be processed. 
 Now, T1 will only get free when its sub halves are sorted. But, we only have one thread to do the task but we reuire 2 threads. So, T1 will never get free. Similarly, T2 require 2 threads but does not have any thread, so T2 also never gets freed. So, the proceeses will not continue and get stuck in that state. This condition is known as a DEADLOCK!

 DEADLOCK: T1 is depended on P2 to get completed. But P1 cannot get completed as it has shortage of resources and it depends on T1 to get completed as it needs a thread. So, there is an interdependency on tasks to get completed and none of them gets completed. This is a DEADLOCK!

 This can be resolves it we use multiple ES to create threads, but since so many threads will be created we may run out of memory. 
 For merge sort, we have 2^N SUBTASKS!!
 Also, we will be switching between threads, so there will be a lot of context switches.
 Hence, multithreaded merge sort will be way slower than a normal sequential merge sort, because so many threads are being created [WORST CASE: 2^N threads], and so many context switches are happening.
*/


/*
 LOCALITY OF REFERENCE:
 Merege Sort and Quick Sort are one of the fastest sorting algorithms. 
 The memory in RAM or Disk are not read in lines, it is read in chunks. So, since array is stored in a contiguous manner in the meory, if we pick an element at index 0, it is possible that the OS has already picked the elements that are surrounding it. So, reading those elements is going to be a veryfast operation. Hence, sequential Merge Sort or Quick Sort are even more faster.
 But, the same thing does not happen in a multithreaded Merge Sort as well. No, because, we have different threads trying to read the same array at the same time. So, it is possible they are fetching the nearby elements as well, but those elements are not required in their operation. So, to EFFECTIVELY USE THE LOCALITY OF REFERENCE IT IS BETTER TO WRITE SEQUENTIAL MERGE SORT.
*/


/*
 USE CASES OF MULTITHREADED MERGE SORT:
 (1) When we have to merge the sorted data from 2 different devices, we can sort them parallely in the 2 devices and finally merge them. Merging will be a faster operation than sorting. 
*/