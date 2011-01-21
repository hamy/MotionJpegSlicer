package mjpegslicer.mockcam;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * This interface defines a nestable content provider that feeds content into a
 * {@link BufferedImage} instance.
 */
public class BackgroundContentProvider extends AbstractContentProvider {

	/**
	 * Returns the next background color. Sub-classes might want to override
	 * this method.
	 * 
	 * @return The next color.
	 */
	public Color nextBackground() {
		return Color.GRAY;
	}

	/**
	 * Provide content for the local node.
	 * 
	 * @param image
	 *            The {@link BufferedImage} instance that receives the content.
	 */
	@Override
	public void provideContent(BufferedImage image) {
		String mn = debugEntering("provideContent");
		Color background = nextBackground();
		debug(mn, "background color: ", background);
		fillImage(image, background);
		debugLeaving(mn);
	}

	private void fillImage(BufferedImage image, Color color) {
		Graphics2D g2d = image.createGraphics();
		int width = image.getWidth() + 1;
		int height = image.getHeight() + 1;
		g2d.fillRect(0, 0, width, height);
	}
}
