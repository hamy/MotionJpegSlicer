package mjpegslicer.mockcam;

import mjpegslicer.util.LoggableObject;

public class DefaultFeederFactory extends LoggableObject implements
		FeederFactory {

	/**
	 * Creates a new feeder.
	 * 
	 * @return The created feeder
	 */
	@Override
	public Feeder newFeeder() {
		String mn = debugEntering("newFeeder");
		Feeder result = new FeederTemplate();
		result.setContentProvider(new BackgroundContentProvider());
		debugLeaving(mn, "result: ", result);
		return result;
	}
}