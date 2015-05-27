package cs601.blkqueue;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedBlockingQueue<T> implements MessageQueue<T> {
    protected List<T> bufferQueue;
    protected int size;

    public SynchronizedBlockingQueue(int size) {
        this.size = size;
        bufferQueue = new ArrayList<>(size);
	}

	@Override
	public synchronized void put(T o) throws InterruptedException{
		while (bufferQueue.size() == size) {
			wait();
		}

		bufferQueue.add(o);
		notifyAll();
	}

	@Override
	public synchronized T take() throws InterruptedException{
		while (bufferQueue.isEmpty()) {
			wait();
		}

		T takenElement = bufferQueue.get(0);
		bufferQueue.remove(0);
		notifyAll();

        return takenElement;
	}
}
