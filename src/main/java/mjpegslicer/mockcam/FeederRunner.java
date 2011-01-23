package mjpegslicer.mockcam;

import mjpegslicer.util.LoggableObject;

public class FeederRunner extends LoggableObject implements Runnable {
	private FeederTemplate feeder;
	private boolean running;

	/**
	 * Creates a runner instance.
	 * 
	 * @param feeder
	 *            The feeder that owns this runner.
	 */
	public FeederRunner(FeederTemplate feeder) {
		debugEntering(MN_INIT);
		this.feeder = feeder;
		debugLeaving(MN_INIT);
	}

	/**
	 * Ends the feeder run.
	 */
	public void shutdown() {
		String mn = debugEntering("shutdown");
		running = false;
		debugLeaving(mn);
	}

	/**
	 * The runner implementation.
	 */
	@Override
	public void run() {
		String mn = debugEntering("run");
		long now = System.currentTimeMillis();
		long next = now + feeder.getMsecPerFrame();
		running = true;
		try {
			while (running) {
				feeder.incrFrameSequenceNumber();
				feeder.prepareFrame();
				now = System.currentTimeMillis();
				long delay = next - now;
				if (delay > 0L) {
					Thread.sleep(delay);
				}
				feeder.flushFrame();
				next += feeder.getMsecPerFrame();
			}
		} catch (InterruptedException ie) {
			debug(mn, "interrupted: ", ie);
			shutdown();
		}
		debugLeaving(mn);
	}
}