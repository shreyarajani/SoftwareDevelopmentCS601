package cs601.blkqueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

/** A runnable class that attaches to another thread and wakes up
 *  at regular intervals to determine that thread's state. The goal
 *  is to figure out how much time that thread is blocked, waiting,
 *  or sleeping.
 */
class ThreadObserver implements Runnable {
	protected final Map<String, Long> histogram = new HashMap<String, Long>();
	protected int numEvents = 0;
	protected int blocked = 0;
	protected int waiting = 0;
	protected int sleeping = 0;
    protected boolean done = false;
    protected final Thread threadToMonitor;
    public static final long MONITORING_PERIOD = 500_000L;
    public long periodInNanoSeconds;
    private StackTraceElement[] traceElements;

	public ThreadObserver(Thread threadToMonitor, long periodInNanoSeconds) {
        this.threadToMonitor = threadToMonitor;
        this.periodInNanoSeconds = periodInNanoSeconds;
	}

	@Override
	public void run() {
        while (!done) {
            traceElements = threadToMonitor.getStackTrace();
            int lengthOfMap = traceElements.length;

            addToMap(lengthOfMap);

            //https://github.com/parrt/cs601/blob/master/lectures%2Fcode%2Fthreads%2FThreadObserver.java
            numEvents++;
            switch ( threadToMonitor.getState() ) {
                case BLOCKED: blocked++; break;
                case WAITING: waiting++; break;
                case TIMED_WAITING: sleeping++; break;
            }
            LockSupport.parkNanos(MONITORING_PERIOD);
        }
	}

    private void addToMap(int lengthOfMap) {
        if(lengthOfMap != 0) {
            String threadName = traceElements[0].getMethodName();
            if (!histogram.containsKey(threadName)) {
                histogram.put(threadName, 1L);
            } else {
                histogram.put(threadName, histogram.get(threadName) + 1);
            }
        }
    }

    public Map<String, Long> getMethodSamples() {
        return histogram;
    }

	public void terminate() {
        done = true;
    }

	public String toString() {
		return String.format("(%d blocked + %d waiting + %d sleeping) / %d samples = %1.2f%% wasted",
							 blocked,
							 waiting,
							 sleeping,
							 numEvents,
							 100.0*(blocked + waiting + sleeping)/numEvents);
	}
}
