package mjpegslicer.util;

/**
 * Provides static sleep methods.
 */
public class Sleep {

	/**
	 * Waits for a certain amount of time.
	 * 
	 * @param millis
	 *            The delay in milliseconds.
	 */
	public static void sleepMillis(long millis) {
		Validator.checkArgument("sleepMillis", "millis", millis, millis >= 0,
				"interval must not be negative.");
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
		}
	}

	/**
	 * Waits for a certain amount of time.
	 * 
	 * @param seconds
	 *            The delay in seconds.
	 */
	public static void sleepSeconds(long seconds) {
		Validator.checkArgument("sleepSeconds", "seconds", seconds,
				seconds >= 0, "interval must not be negative.");
		sleepMillis(1000L * seconds);
	}
}
