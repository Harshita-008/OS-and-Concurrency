import java.lang.*;
import java.util.*;

// HIGHEST RESPONSE RATIO NEXT
public class HRRN {
    public static class Process {
        int id, AT, BT;
        boolean completed;

        Process(int id, int AT, int BT) {
            this.id = id;
            this.AT = AT;
            this.BT = BT;
            this.completed = false;
        }

        private double getRR(int currTime) {
            int WT = currTime - AT;
            return 1 + (double) WT / BT;
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

        int completed = 0, currTime = 0, totalTAT = 0, totalWT = 0;
        while(completed < N) {
            Process current = null;
            double maxRR = -1;

            for(Process pr : p) {
                if(!pr.completed && pr.AT <= currTime) {
                    double RR = pr.getRR(currTime);
                    if(RR > maxRR) {
                        maxRR = RR;
                        current = pr;
                    }
                }
            }

            if(current == null) {
                currTime++;
                continue;
            }

            int CT = currTime + current.BT;
            int TAT = CT - current.AT;
            int WT = TAT - current.BT;

            totalTAT += TAT;
            totalWT += WT;

            currTime = CT;
            current.completed = true;
            completed++;
        }

        System.out.println(totalWT/N);
        System.out.println(totalTAT/N);
    }
}