/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * 
 * Swing port: Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com/swtswing
*******************************************************************************/
package org.eclipse.swt.widgets;

import java.awt.Container;

import javax.swing.JProgressBar;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of the receiver represent is an unselectable
 * user interface object that is used to display progress,
 * typically in the form of a bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SMOOTH, HORIZONTAL, VERTICAL, INDETERMINATE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class ProgressBar extends Control {
  static final int DELAY = 100;
  static final int TIMER_ID = 100;

  /**
   * Constructs a new instance of this class given its parent
   * and a style value describing its behavior and appearance.
   * <p>
   * The style value is either one of the style constants defined in
   * class <code>SWT</code> which is applicable to instances of this
   * class, or must be built by <em>bitwise OR</em>'ing together
   * (that is, using the <code>int</code> "|" operator) two or more
   * of those <code>SWT</code> style constants. The class description
   * lists the style constants that are applicable to the class.
   * Style bits are also inherited from superclasses.
   * </p>
   *
   * @param parent a composite control which will be the parent of the new instance (cannot be null)
   * @param style the style of control to construct
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
       *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
   * </ul>
   *
   * @see SWT#SMOOTH
   * @see SWT#HORIZONTAL
   * @see SWT#VERTICAL
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public ProgressBar(Composite parent, int style) {
    super(parent, checkStyle(style));
  }

  JProgressBar progressBar;

  Container getNewHandle() {
    // TODO: investigate for style SMOOTH, and find about the unit in use
    progressBar = new JProgressBar((style & SWT.HORIZONTAL) != 0? JProgressBar.HORIZONTAL: JProgressBar.VERTICAL);
    progressBar.setIndeterminate((style & SWT.INDETERMINATE) != 0);
    return progressBar;
  }

  static int checkStyle(int style) {
    return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
  }

//  int defaultForeground() {
//    return OS.GetSysColor(OS.COLOR_HIGHLIGHT);
//  }

  /**
   * Returns the maximum value which the receiver will allow.
   *
   * @return the maximum
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getMaximum() {
    checkWidget();
    return progressBar.getMaximum();
  }

  /**
   * Returns the minimum value which the receiver will allow.
   *
   * @return the minimum
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getMinimum() {
    checkWidget();
    return progressBar.getMinimum();
  }

  /**
   * Returns the single <em>selection</em> that is the receiver's position.
   *
   * @return the selection
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getSelection() {
    checkWidget();
    return progressBar.getValue();
  }

//  void releaseWidget() {
//    super.releaseWidget();
//    if((style & SWT.INDETERMINATE) != 0) {
//      OS.KillTimer(handle, TIMER_ID);
//    }
//  }

//  void setBackgroundPixel(int pixel) {
//    if(background == pixel) {
//      return;
//    }
//    background = pixel;
//    if(pixel == -1) {
//      pixel = OS.CLR_DEFAULT;
//    }
//    OS.SendMessage(handle, OS.PBM_SETBKCOLOR, 0, pixel);
//  }

  public boolean setFocus() {
    checkWidget();
    return false;
  }

//  void setForegroundPixel(int pixel) {
//    if(foreground == pixel) {
//      return;
//    }
//    foreground = pixel;
//    if(pixel == -1) {
//      pixel = OS.CLR_DEFAULT;
//    }
//    OS.SendMessage(handle, OS.PBM_SETBARCOLOR, 0, pixel);
//  }

  /**
   * Sets the maximum value which the receiver will allow
   * to be the argument which must be greater than or
   * equal to zero.
   *
   * @param value the new maximum (must be zero or greater)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setMaximum(int value) {
    checkWidget();
    int minimum = progressBar.getMinimum();
    if(0 <= minimum && minimum < value) {
      progressBar.setMaximum(value);
    }
  }

  /**
   * Sets the minimum value which the receiver will allow
   * to be the argument which must be greater than or
   * equal to zero.
   *
   * @param value the new minimum (must be zero or greater)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setMinimum(int value) {
    checkWidget();
    int maximum = progressBar.getMaximum();
    if(0 <= value && value < maximum) {
      progressBar.setMinimum(value);
    }
  }

  /**
   * Sets the single <em>selection</em> that is the receiver's
   * position to the argument which must be greater than or equal
   * to zero.
   *
   * @param value the new selection (must be zero or greater)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSelection(int value) {
    checkWidget();
    progressBar.setValue(value);
  }

  int widgetStyle() {
    int bits = super.widgetStyle();
    if((style & SWT.SMOOTH) != 0) {
      bits |= OS.PBS_SMOOTH;
    }
    if((style & SWT.VERTICAL) != 0) {
      bits |= OS.PBS_VERTICAL;
    }
    return bits;
  }

  LRESULT WM_GETDLGCODE(int wParam, int lParam) {
    LRESULT result = super.WM_GETDLGCODE(wParam, lParam);
    if(result != null) {
      return result;
    }
    /*
     * Feature in Windows.  The progress bar does
     * not implement WM_GETDLGCODE.  As a result,
     * a progress bar takes focus and takes part
     * in tab traversal.  This behavior, while
     * unspecified, is unwanted.  The fix is to
     * implement WM_GETDLGCODE to behave like a
     * STATIC control.
     */
    return new LRESULT(OS.DLGC_STATIC);
  }

  LRESULT WM_TIMER(int wParam, int lParam) {
    LRESULT result = super.WM_TIMER(wParam, lParam);
    if(result != null) {
      return result;
    }
    if(wParam == TIMER_ID) {
      OS.SendMessage(handle, OS.PBM_STEPIT, 0, 0);
    }
    return null;
  }

}
