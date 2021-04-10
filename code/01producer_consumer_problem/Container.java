
import java.util.*;

public class Container {

    List<String> product = new ArrayList<>();

    public void put(String pro) throws Exception {
        synchronized (this) {
            System.out.println(String.format("receive pro form %s", pro));
            product.add(pro);
            if (product.size() >= 5) {
                wait();
            } else {
                notifyAll();
            }
        }
    }

    public List<String> get() throws Exception {
        synchronized (this) {
            List<String> productCopy = new ArrayList<>();
            productCopy.addAll(product);
            product.clear();
            notifyAll();
            return productCopy;
        }
    }

    public static void main(String[] args) throws Exception {
        Container container = new Container();
        Consumer consumer = new Consumer("c0", container);
        Consumer consumer1 = new Consumer("c1", container);
        Consumer consumer2 = new Consumer("c2", container);
        Producer producer = new Producer("p0", container);
        Producer producer1 = new Producer("p1", container);
        Producer producer2 = new Producer("p2", container);

        new Thread(producer).start();
        new Thread(producer1).start();
        new Thread(producer2).start();
        new Thread(consumer).start();
        new Thread(consumer1).start();
        new Thread(consumer2).start();

    }

}
