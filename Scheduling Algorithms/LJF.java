import java.lang.*;
import java.util.*;

// LONGEST JOB FIRST
public class LJF {
    public static class Process {
        int id, AT, BT;

        Process(int id, int AT, int BT) {
            this.id = id;
            this.AT = AT;
            this.BT = BT;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] arrivalInput = sc.nextLine().split(" ");
        String[] burstInput = sc.nextLine().split(" ");

        int N = arrivalInput.length;
        Process[] p = new Process[N];
        for(int i=0; i<N; i++) {
            p[i] = new Process(i, Integer.parseInt(arrivalInput[i]), Integer.parseInt(burstInput[i]));
        }

        Arrays.sort(p, Comparator.comparingInt(process -> process.AT));
        PriorityQueue<Process> pq = new PriorityQueue<>(Comparator.comparingInt((Process process) -> process.BT).reversed());

        int idx = 0, currTime = 0, totalTAT = 0, totalWT = 0;
        while(idx < N || pq.size() > 0) {
            while(idx < N && p[idx].AT <= currTime) {
                pq.add(p[idx]);
                idx++;
            }

            if(pq.isEmpty()) {
                currTime++;
                continue;
            }

            Process current = pq.poll();

            int CT = currTime + current.BT;
            int TAT = CT - current.AT;
            int WT = TAT - current.BT;

            totalTAT += TAT;
            totalWT += WT;
            currTime = CT;
        }

        System.out.println(totalWT/N);
        System.out.println(totalTAT/N);
    }
}