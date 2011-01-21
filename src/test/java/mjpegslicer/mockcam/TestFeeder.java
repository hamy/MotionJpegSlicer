package mjpegslicer.mockcam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

//import javax.swing.ImageIcon;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mjpegslicer.AbstractTestCase;

public class TestFeeder extends AbstractTestCase {

	private Feeder feeder;
	private FileOutputStream fos;

	@Before
	public void setUp() throws Exception {
		String mn = debugEntering("setUp");
		feeder = new FeederTemplate();
		File file = File.createTempFile("video-", ".mjpg");
		debug(mn, "video file: ", file);
		fos = new FileOutputStream(file);
		feeder.setOutputStream(fos);
		debugLeaving(mn);
	}

	@After
	public void tearDown() throws Exception {
		String mn = debugEntering("tearDown");
		if (feeder.isStarted()) {
			feeder.stop();
		}
		if (fos != null) {
			fos.close();
		}
		debugLeaving(mn);
	}

	@Test
	public void testSetUp() throws Exception {
		String mn = debugEntering("testSetUp");
		assertFalse(feeder.isStarted());
		assertEquals(0, feeder.getFrameSequenceNumber());
		assertEquals(Feeder.DEFAULT_WIDTH, feeder.getWidth());
		assertEquals(Feeder.DEFAULT_HEIGHT, feeder.getHeight());
		assertEquals(Feeder.DEFAULT_RATE, feeder.getRate());
		assertEquals(Feeder.DEFAULT_MAX_FRAME_SEQUENCE_NUMBER,
				feeder.getMaxFrameSequenceNumber());
		assertEquals(0, feeder.getFrameSequenceNumber());
		debugLeaving(mn);
	}

	@Test
	public void testStartAndStop() throws Exception {
		String mn = debugEntering("testStartAndStop");
		assertFalse(feeder.isStarted());
		debug(mn, "***** first run *****");
		assertEquals(0, feeder.getFrameSequenceNumber());
		feeder.start();
		assertTrue(feeder.isStarted());
		Thread.sleep(500);
		assertTrue(feeder.isStarted());
		feeder.stop();
		assertFalse(feeder.isStarted());
		debug(mn, "***** second run *****");
		feeder.start();
		assertTrue(feeder.isStarted());
		Thread.sleep(500);
		assertTrue(feeder.isStarted());
		feeder.stop();
		assertFalse(feeder.isStarted());
		debugLeaving(mn);
	}

	@Test
	public void testCreateImage() throws Exception {
		String mn = debugEntering("testCreateImage");
		BufferedImage image = feeder.createImage();
		debug(mn, "image created: ", image);
		assertNotNull(image);
		assertEquals(Feeder.DEFAULT_WIDTH, image.getWidth());
		assertEquals(Feeder.DEFAULT_HEIGHT, image.getHeight());
		// ImageIcon icon = new ImageIcon(image);
		// JLabel label = new JLabel(icon, JLabel.CENTER);
		// JOptionPane.showMessageDialog(null, label, "icon", -1);
		debugLeaving(mn);

	}
}
