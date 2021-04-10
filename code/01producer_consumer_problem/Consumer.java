
import java.util.*;

public class Consumer implements Runnable {

    private String name;
    private Container container;

    public Consumer(String name, Container container) {
        this.name = name;
        this.container = container;
    }

    public void run() {
        int index = 1;
        while (true) {
            try {
                List<String> pro = container.get();
                for (String proItem : pro) {
                    System.out.println(String.format("%s consume %s", name, proItem));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}