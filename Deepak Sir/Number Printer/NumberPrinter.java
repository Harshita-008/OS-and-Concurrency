public class NumberPrinter implements Runnable {
    private int number;

    public NumberPrinter(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        // Print the number
        System.out.println(number + " -> " + Thread.currentThread().getName());
    }
}
