package cs601.blkqueue;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class RingBuffer<T> implements MessageQueue<T> {
	private final AtomicLong w = new AtomicLong(-1);	// just wrote location
	private final AtomicLong r = new AtomicLong(0);		// about to read location
    private int n;
    private volatile T[] ringBuffer;

	public RingBuffer(int n) {
        this.n=n;
        ringBuffer = (T[]) new Object[n];
        if(!isPowerOfTwo(n)){
            throw new IllegalArgumentException("N not in power of 2!");
        }
	}

	// http://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetParallel
	static boolean isPowerOfTwo(int v) {
		if (v<0) return false;
		v = v - ((v >> 1) & 0x55555555);                    // reuse input as temporary
		v = (v & 0x33333333) + ((v >> 2) & 0x33333333);     // temp
		int onbits = ((v + (v >> 4) & 0xF0F0F0F) * 0x1010101) >> 24; // count
		// if number of on bits is 1, it's power of two, except for sign bit
		return onbits==1;
	}

	@Override
	public void put(T v) throws InterruptedException {
        //Spin loop
        while(w.get()+1 - r.get() == n){
           LockSupport.parkNanos(1);
        }
        int y = (int) ((w.get()+1) & (n-1));
        ringBuffer[y] = v;
        w.getAndIncrement();
	}

	@Override
	public T take() throws InterruptedException {
//        orgR = r.get(); // I don't see why you need a field here
        //Spin loop
        while (w.get()<r.get()){
            LockSupport.parkNanos(1);
        }
        int y = (int) (r.get() & (n-1));
        T data = ringBuffer[y];
        r.getAndIncrement();

		return data;
	}
}
