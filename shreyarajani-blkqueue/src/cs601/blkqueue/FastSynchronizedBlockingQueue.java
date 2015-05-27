package cs601.blkqueue;

import java.util.ArrayList;
import java.util.List;

public class FastSynchronizedBlockingQueue<T> implements MessageQueue<T> {
    protected List<T> bufferQueue;
    protected int size;

    public FastSynchronizedBlockingQueue(int size) {
        this.size = size;
        bufferQueue = new ArrayList<T>(size);
	}

	@Override
	public synchronized void put(T o) throws InterruptedException {
        try {
            while (bufferQueue.size() == size) {
                wait();
            }
        }catch (InterruptedException e) {
                System.err.println("Put method interrupted " + e);
        }
        int previousSize = bufferQueue.size();
        bufferQueue.add(o);
        if(previousSize == 0) {
            notifyAll();
        }
	}

	@Override
	public synchronized T take() throws InterruptedException {
        try {
            while (bufferQueue.isEmpty()) {
                wait();
            }
        }catch (InterruptedException e) {
                System.err.println("Take method interrupted " + e);
        }

        int previousSize = bufferQueue.size();
        T takenElement = bufferQueue.get(0);
        bufferQueue.remove(0);
        if (previousSize == size) {
            notifyAll();
        }

        return takenElement;
	}
}
