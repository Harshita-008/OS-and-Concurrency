public class client {
    public static void main(String[] args) {
        for(int i=1; i<=100; i++) {
            NumberPrinter task = new NumberPrinter(i);
            Thread thread = new Thread(task);
            thread.start();
        }
    }
}
