package mjpegslicer.mockcam;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * This interface defines a nestable content provider that feeds content into a
 * {@link BufferedImage} instance.
 */
public class RainbowBackgroundContentProvider extends BackgroundContentProvider {

	private float saturation = 1.0f;
	private float brightness = 1.0f;
	private float hue = 0.0f;
	private float dhue = 0.01f;

	/**
	 * Returns the next background color.
	 * 
	 * @return The next color.
	 */
	@Override
	public Color nextBackground() {
		int irgb = Color.HSBtoRGB(hue, saturation, brightness);
		hue += dhue;
		if (hue >= 1f) {
			hue = 0f;
		}
		return new Color(irgb);
	}
}
