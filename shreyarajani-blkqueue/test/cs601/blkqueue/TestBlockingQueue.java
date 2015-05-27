package cs601.blkqueue;

/** Demo Java's BlockingQueue.
 * from terminal
 * jdk 1.8 N = 20,000,000
 5460.474ms 3,662,685.75 events / second
 Producer: (0 blocked + 953 waiting + 0 sleeping) / 7744 samples = 12.31% wasted
 Consumer: (1 blocked + 1741 waiting + 0 sleeping) / 7718 samples = 22.57% wasted
 */
public class TestBlockingQueue {
	public static final int N = 20_000_000;

	public static void main(String[] args) throws Exception {
		MessageQueueAdaptor<Integer> queue = new MessageQueueAdaptor<Integer>(1024);
		MessageSequence<Integer> sequence = new IntegerSequence(1,N);
		TestRig.test(queue, sequence, N);
	}
}
