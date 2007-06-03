package org.eclipse.swt.internal.swing;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * @author Dieter Krachtus
 * 
 * I put a few methods in here. We can refactor them out, but for the beginning
 * I thought I keep them separated and don't directly include them in Image.java
 * etc.
 * 
 */
public class GeneralUtils {

	public static boolean isEqualOrHigherVM(double javaversion) {
		try {
			if (javaversion <= new Double(System
					.getProperty("java.specification.version")).doubleValue())
				return true;
			else
				System.err.println("You need Java Version " + javaversion
						+ " to run method"); // + get yourself a stacktrace
												// and print calling method
		} catch (Exception e) {
		}

		return false;
	}

	/**
	 * Method consists of two parts, a tribute to handle either direct or
	 * indexed models used in images. This is somehow the most natural solution
	 * and future fixed shouldn't be workarounds for special cases anymore
	 * 
	 * @param data
	 * @return
	 */
	public static BufferedImage imageData2BufferedImage(ImageData data) {
		BufferedImage bi;
		if (data.palette.isDirect) 	bi = createBufferedImageDirectPalette(data, data.palette);
		else 						bi = createBufferedImageIndexPalette (data, data.palette);
		return bi;
	}
	
	

	private static BufferedImage createBufferedImageIndexPalette(ImageData data, PaletteData p) {
		ColorModel cM;
		RGB[] rgbs = p.getRGBs();
		byte[] red = new byte[rgbs.length];
		byte[] green = new byte[rgbs.length];
		byte[] blue = new byte[rgbs.length];
		for (int i = 0; i < rgbs.length; i++) {
			RGB rgb = rgbs[i];
			red[i] = (byte) rgb.red;
			green[i] = (byte) rgb.green;
			blue[i] = (byte) rgb.blue;
		}
		if (data.transparentPixel != -1) {
			cM = new IndexColorModel(data.depth, rgbs.length, red,
					green, blue, data.transparentPixel);
		} else {
			cM = new IndexColorModel(data.depth, rgbs.length, red,
					green, blue);
		}
		BufferedImage bi = new BufferedImage(cM,
				cM.createCompatibleWritableRaster(data.width,
						data.height), false, null);
		WritableRaster r = bi.getRaster();
		int[] pA = new int[1];
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				int pixel = data.getPixel(x, y);
				pA[0] = pixel;
				r.setPixel(x, y, pA);
			}
		}
		// System.out.println("data.transparentPixel: "
		// + data.transparentPixel);
		// System.out.println(colorModel);
		// System.out.println(bufferedImage);
		// System.out.println();
		return bi;
	}

	private static BufferedImage createBufferedImageDirectPalette(ImageData data, PaletteData p) {
		ColorModel cM;
		// Added ColorModel.TRANSLUCENT to create a ColorModel with
		// transparency and resulting in a ARGB BufferedImage
		// TODO: Check if true: Still Transparency-Gradients may be
		// impossible with standard JavaIO & BufferedImages since
		// ColorModel.TRANSLUCENT is either opaque or transparent.
		cM = new DirectColorModel(data.depth, p.redMask,
				p.greenMask, p.blueMask, ColorModel.TRANSLUCENT);
		BufferedImage bi = new BufferedImage(cM,
				cM.createCompatibleWritableRaster(data.width,
						data.height), false, null);
		WritableRaster r = bi.getRaster();
		int[] pA = new int[4];
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				int pixel = data.getPixel(x, y);
				RGB rgb = p.getRGB(pixel);
				pA[0] = rgb.red;
				pA[1] = rgb.green;
				pA[2] = rgb.blue;
				pA[3] = data.getAlpha(x, y);

				// Line is a bugfix which is actually for Problems with
				// detecting Transparency in BufferedImages. Also using the
				// normal ImageIO.read(...) method results in this bug and
				// can be tested using comment.png from Azureus
				if (data.transparentPixel != -1
						&& pixel == data.transparentPixel) {
					pA[3] = 0;
				}
				// System.out.println(pixelArray[3]);
				r.setPixels(x, y, 1, 1, pA);
			}
		}
		// System.out.println("data.transparentPixel: "
		// + data.transparentPixel);
		// System.out.println(colorModel);
		// System.out.println(bufferedImage);
		// System.out.println();
		return bi;
	}

}
