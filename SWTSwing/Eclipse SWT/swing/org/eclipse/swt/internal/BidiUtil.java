/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.awt.Container;
import java.text.Bidi;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;

/*
 * This class is supplied so that the StyledText code that supports bidi text (supported
 * for win platforms) is not platform dependent.  Bidi text is not implemented on 
 * emulated platforms.
 */
public class BidiUtil {
	// Keyboard language types
	public static final int KEYBOARD_NON_BIDI = 0;
	public static final int KEYBOARD_BIDI = 1;

	// bidi rendering input flag constants, not used
	// on emulated platforms
	public static final int CLASSIN = 1;
	public static final int LINKBEFORE = 2;
	public static final int LINKAFTER = 4;

	// bidi rendering/ordering constants, not used on 
	// emulated platforms
	public static final int CLASS_HEBREW = 2;
	public static final int CLASS_ARABIC = 2;
	public static final int CLASS_LOCALNUMBER = 4;
	public static final int CLASS_LATINNUMBER = 5;  
	public static final int REORDER = 0;        
	public static final int LIGATE = 0;
	public static final int GLYPHSHAPE = 0;

/*
 * Not implemented.
 */
public static void addLanguageListener(Container handle, Runnable runnable) {
}
/*
 * Not implemented.
 *
 */
public static void drawGlyphs(GC gc, char[] renderBuffer, int[] renderDx, int x, int y) {
}
/*
 * Bidi not supported on emulated platforms.
 *
 */
public static boolean isBidiPlatform() {
	return false;
}
/*
 * Not implemented.
 */
public static boolean isKeyboardBidi() {
	return false;
}
/*
 * Not implemented.
 */
public static int getFontBidiAttributes(GC gc) {
	return 0; 
}
/*
 *  Not implemented.
 *
 */
public static void getOrderInfo(GC gc, String text, int[] order, byte[] classBuffer, int flags, int [] offsets) {
}
/*
 *  Not implemented. Returns null.
 *
 */
public static char[] getRenderInfo(GC gc, String text, int[] order, byte[] classBuffer, int[] dx, int flags, int[] offsets) {
	return null;
}
/*
 * Not implemented. Returns 0.
 */
public static int getKeyboardLanguage() {
	return 0;
}
/*
 * Not implemented.
 */
public static void removeLanguageListener(Container handle) {
}
/**
 * Determine the base direction for the given text. The direction is derived
 * from that of the first strong bidirectional character. In case the text
 * doesn't contain any strong characters, the base direction is to be
 * derived from a higher-level protocol (e.g. the widget orientation).
 * <p>
 *
 * @param text
 *            Text base direction should be resolved for.
 * @return SWT#LEFT_RIGHT or SWT#RIGHT_TO_LEFT if the text contains strong
 *         characters and thus the direction can be resolved, SWT#NONE
 *         otherwise.
 * @since 3.105
 */
public static int resolveTextDirection (String text) {
	return new Bidi(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0? SWT.LEFT_TO_RIGHT: SWT.RIGHT_TO_LEFT;
}
/*
 * Not implemented.
 */
public static void setKeyboardLanguage(int language) {
}
/*
 * Not implemented.
 */
public static boolean setOrientation(Container handle, int orientation) {
	return false;
}
}
