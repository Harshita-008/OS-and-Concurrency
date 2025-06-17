import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsingExecutorService {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        for(int i=1; i<=100; i++) {
            if(i==5 || i==10 || 1==50 || i==98) {
                System.out.println("DEBUG");
            }
            NumberPrinter np = new NumberPrinter(i);
            es.execute(np);
        }
    }
}