package cs601.blkqueue;

/** Demo msg passing with blocking queue.
 *
 * N = 8_000_000 MAX_BUFFER_SIZE = 1024
 * from terminal
 6724.910ms 1,189,607 events / second
 Producer: (4710 blocked + 24 waiting + 0 sleeping) / 10260 samples = 46.14% wasted
 Consumer: (3007 blocked + 1682 waiting + 0 sleeping) / 10237 samples = 45.80% wasted
 */
public class TestSynchronizedBlockingQueue {
	public static final int N = 8_000_000;

	public static void main(String[] args) throws Exception {
		SynchronizedBlockingQueue<Integer> queue = new SynchronizedBlockingQueue<Integer>(1024);
		MessageSequence<Integer> sequence = new IntegerSequence(1,N);
		TestRig.test(queue, sequence, N);
	}
}
