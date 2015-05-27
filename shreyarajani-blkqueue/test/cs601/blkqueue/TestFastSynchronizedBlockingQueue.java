package cs601.blkqueue;

/** Demo msg passing with blocking queue.
 *
 * N = 40_000_000 MAX_BUFFER_SIZE = 1024
 * from terminal
 8133.152ms 4,918,142.5 events / second
 Producer: (1308 blocked + 41 waiting + 0 sleeping) / 11636 samples = 11.59% wasted
 Consumer: (2959 blocked + 401 waiting + 0 sleeping) / 11638 samples = 28.87% wasted
 Producer:
 */
public class TestFastSynchronizedBlockingQueue {
	public static final int N = 40_000_000;

	public static void main(String[] args) throws Exception {
		FastSynchronizedBlockingQueue<Integer> queue = new FastSynchronizedBlockingQueue<Integer>(1024);
		MessageSequence<Integer> sequence = new IntegerSequence(1,N);
		TestRig.test(queue, sequence, N);
	}
}
