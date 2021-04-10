import java.util.concurrent.locks.*;

/**
 * 仓库满了才能消费
 */
public class ContainerBatchAwait {
    private Lock lock = new ReentrantLock();
    /**
     * 仓库存储已到达上限
     */
    private Condition notFull = lock.newCondition();

    private Condition notEmpty = lock.newCondition();

    int maxCap = 2;
    int curCap = 0;

    public void put(String pro) throws Exception {
        lock.lock();
        if (curCap >= maxCap) {
            System.out.println(pro + " is full , take a rest and try again");
            // 阻塞其他生产者，也就是其他生产者不会在拿到lock
            notFull.await();
        } else {
            curCap++;
            System.out.println(String.format("receive %s, current size: %d", pro, curCap));
        }
        lock.unlock();
    }

    public void get(String name) throws Exception {
        lock.lock();
        if (curCap < maxCap) {
            System.out.println(name + " not full , take a rest and try again");
        } else {
            curCap = 0;
            System.out.println(String.format("%s consume all, current size: %d", name, curCap));
            notFull.signal();
        }
        lock.unlock();
    }

    static class ProducerAwait implements Runnable {

        private String name;
        private ContainerAwait container;

        public ProducerAwait(String name, ContainerAwait container) {
            this.name = name;
            this.container = container;
        }

        public void run() {
            while (true) {
                int index = 0;
                try {
                    container.put(name + "_" + (index++));
                } catch (Exception e) {

                }
            }
        }
    }

    static class ConsumerAwait implements Runnable {

        String name;
        ContainerAwait container;

        public ConsumerAwait(String name, ContainerAwait container) {
            this.name = name;
            this.container = container;
        }

        public void run() {
            int index = 0;
            while (true) {
                try {
                    container.get(name);
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
            }
        }
    }

    public static void main(String[] args) {
        ContainerAwait container = new ContainerAwait();
        ProducerAwait p1 = new ProducerAwait("p1", container);
        ProducerAwait p2 = new ProducerAwait("p2", container);
        ProducerAwait p3 = new ProducerAwait("p3", container);
        ConsumerAwait c1 = new ConsumerAwait("c1", container);
        ConsumerAwait c2 = new ConsumerAwait("c2", container);
        ConsumerAwait c3 = new ConsumerAwait("c3", container);

        new Thread(p1).start();
        new Thread(p2).start();
        new Thread(p3).start();
        new Thread(c1).start();
        new Thread(c2).start();
        new Thread(c3).start();
    }
}