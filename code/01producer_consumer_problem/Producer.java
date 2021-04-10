
public class Producer implements Runnable {

    private String name;

    private Container container;

    public Producer(String name, Container container) {
        this.name = name;
        this.container = container;
    }

    public void run() {
        while (true) {
            int index = 1;
            try {
                container.put(name + "_" + index++);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}