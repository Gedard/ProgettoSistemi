package utility;

import java.io.Serializable;

public class CounterThread extends Thread implements Serializable {

    private int count;

    public CounterThread(int count) {
        this.count = count;
        this.start();
    }

    public void run() {
        try {
            while (count > 0) {
                Thread.sleep(1000);
                count--;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return count;
    }

    public boolean timeIsUp() {
        return count == 0;
    }
}
