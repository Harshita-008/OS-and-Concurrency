import java.lang.*;
import java.util.*;

// ROUND ROBIN
public class RR {
    public static class Process {
        int id, AT, BT, remaining;
        
         Process(int id, int AT, int BT) {
            this.id = id;
            this.AT = AT;
            this.BT = BT;
            this.remaining = BT;
         }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] arrivalInput = sc.nextLine().split(" ");
        String[] burstInput = sc.nextLine().split(" ");
        int time = sc.nextInt();

        int N = arrivalInput.length;
        Process[] p = new Process[N];
        for(int i=0; i<N; i++) {
            p[i] = new Process(i, Integer.parseInt(arrivalInput[i]), Integer.parseInt(burstInput[i]));
        }

        Arrays.sort(p, Comparator.comparingInt(process -> process.AT));
        Queue<Process> q = new LinkedList<>();

        int idx = 0, currTime = 0, totalTAT = 0, totalWT = 0;
        boolean[] isInQueue = new boolean[N];
        while(idx < N || q.size() > 0) {
            while(idx < N && p[idx].AT <= currTime) {
                q.add(p[idx]);
                isInQueue[p[idx].id] = true;
                idx++; 
            }

            if(q.isEmpty()) {
                currTime++;
                continue;
            }

            Process current = q.poll();
            
            int execTime = Math.min(time, current.remaining);
            current.remaining -= execTime;
            currTime += execTime;

            while(idx < N && p[idx].AT <= currTime) {
                if(!isInQueue[p[idx].id]) {
                    q.add(p[idx]);
                    isInQueue[p[idx].id] = true;
                    idx++;
                }               
            }

            if(current.remaining == 0) {
                int CT = currTime;
                int TAT = CT - current.AT;
                int WT = TAT - current.BT;

                totalTAT += TAT;
                totalWT += WT;
            }
            else {
                q.add(current);
            }
        }

        System.out.println(totalWT/N);
        System.out.println(totalTAT/N);
    }
}