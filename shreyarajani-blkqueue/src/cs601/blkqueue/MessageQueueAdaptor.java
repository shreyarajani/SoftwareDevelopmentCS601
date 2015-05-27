package cs601.blkqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class MessageQueueAdaptor<T> implements MessageQueue<T> {
    protected BlockingQueue<T> blockingQueue;

	public MessageQueueAdaptor(int size) {
        blockingQueue = new ArrayBlockingQueue<T>(size);
    }

	@Override
	public void put(T o) throws InterruptedException {
        blockingQueue.put(o);
	}

	@Override
	public T take() throws InterruptedException {
		return blockingQueue.take();
	}
}
