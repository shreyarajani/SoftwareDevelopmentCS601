package cs601.blkqueue;

/*
from terminal not intellij seems faster, more stable.
jdk 8 N = 100_000_000
3164.566ms 31,599,910 events / second
Producer: (0 blocked + 0 waiting + 2077 sleeping) / 4719 samples = 44.01% wasted
Consumer: (0 blocked + 0 waiting + 2181 sleeping) / 4712 samples = 46.29% wasted

seen as low as:

6631.473ms 15,079,606 events / second
Producer: (0 blocked + 0 waiting + 405 sleeping) / 9490 samples = 4.27% wasted
Consumer: (0 blocked + 0 waiting + 2712 sleeping) / 9469 samples = 28.64% wasted
 */
public class TestRingBuffer {
	public static final int N = 100_000_000;

	public static void main(String[] args) throws Exception {
		RingBuffer<Integer> queue = new RingBuffer<Integer>(1024);
		MessageSequence<Integer> sequence = new IntegerSequence(1,N);
		TestRig.test(queue, sequence, N);
	}
}
