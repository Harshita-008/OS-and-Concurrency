import java.lang.*;
import java.util.*;

// FIRST COME FIRST SERVE
public class FCFS {
    public static class Process {
        int id, AT, BT, CT, TAT, WT;

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

        int currTime = 0, totalTAT = 0, totalWT = 0;
        for(int i=0; i<N; i++) {
            Process current = p[i];

            if(currTime < current.AT) {
                currTime = current.AT;
            }

            current.CT = currTime + current.BT;
            current.TAT = current.CT - current.AT;
            current.WT = current.TAT - current.BT;

            totalTAT += current.TAT;
            totalWT += current.WT;
            currTime += current.BT;
        }

        System.out.println(totalWT/N);
        System.out.println(totalTAT/N);
    }
}