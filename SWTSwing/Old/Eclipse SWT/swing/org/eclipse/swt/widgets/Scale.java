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

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of the receiver represent a selectable user
 * interface object that present a range of continuous
 * numeric values.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public class Scale extends Control {

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
   * @see SWT#HORIZONTAL
   * @see SWT#VERTICAL
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public Scale(Composite parent, int style) {
    super(parent, checkStyle(style));
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the receiver's value changes, by sending
   * it one of the messages defined in the <code>SelectionListener</code>
   * interface.
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SelectionListener
   * @see #removeSelectionListener
   */
  public void addSelectionListener(SelectionListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Selection, typedListener);
    addListener(SWT.DefaultSelection, typedListener);
  }

  static int checkStyle(int style) {
    return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
  }

  JSlider slider;

  Container getNewHandle() {
    slider = new JSlider();
    slider.setMaximum(100);
    slider.setMajorTickSpacing(10);
    slider.setPaintTicks(true);
    slider.setValue(0);
    slider.setOrientation((style & SWT.HORIZONTAL) != 0? JSlider.HORIZONTAL: JSlider.VERTICAL);
    return slider;
  }

  void addSwingListeners() {
    super.addSwingListeners();
    addSwingScaleListeners();
    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        swingStateChanged(e);
      }
    });
  }
  
  void addSwingScaleListeners() {
    
  }
  
  void swingStateChanged(ChangeEvent e) {
    Event event = new Event();
    sendEvent(SWT.Selection, event);
  }
  
//  int defaultForeground() {
//    return OS.GetSysColor(OS.COLOR_BTNFACE);
//  }

  /**
   * Returns the amount that the receiver's value will be
   * modified by when the up/down (or right/left) arrows
   * are pressed.
   *
   * @return the increment
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getIncrement() {
    checkWidget();
    // TODO: find what to return for this method
    return 1;
//    return OS.SendMessage(handle, OS.TBM_GETLINESIZE, 0, 0);
  }

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
    return slider.getMaximum();
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
    return slider.getMinimum();
  }

  /**
   * Returns the amount that the receiver's value will be
   * modified by when the page increment/decrement areas
   * are selected.
   *
   * @return the page increment
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getPageIncrement() {
    checkWidget();
    return slider.getMajorTickSpacing();
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
    return slider.getValue();
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the receiver's value changes.
   *
   * @param listener the listener which should no longer be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SelectionListener
   * @see #addSelectionListener
   */
  public void removeSelectionListener(SelectionListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Selection, listener);
    eventTable.unhook(SWT.DefaultSelection, listener);
  }

//  void setBackgroundPixel(int pixel) {
//    if(background == pixel) {
//      return;
//    }
//    super.setBackgroundPixel(pixel);
//    /*
//     * Bug in Windows.  Changing the background color of the Scale
//     * widget and calling InvalidateRect still draws with the old color.
//     * The fix is to post a fake WM_SETFOCUS event to cause it to redraw
//     * with the new background color.
//     *
//     * Note.  This WM_SETFOCUS message causes recursion when
//     * setBackground is called from within the focus event listener.
//     */
//    OS.PostMessage(handle, OS.WM_SETFOCUS, 0, 0);
//  }

  /**
   * Sets the amount that the receiver's value will be
   * modified by when the up/down (or right/left) arrows
   * are pressed to the argument, which must be at least
   * one.
   *
   * @param value the new increment (must be greater than zero)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setIncrement(int increment) {
    checkWidget();
    if(increment < 1) {
      return;
    }
//    int minimum = OS.SendMessage(handle, OS.TBM_GETRANGEMIN, 0, 0);
//    int maximum = OS.SendMessage(handle, OS.TBM_GETRANGEMAX, 0, 0);
//    if(increment > maximum - minimum) {
//      return;
//    }
//    OS.SendMessage(handle, OS.TBM_SETLINESIZE, 0, increment);
  }

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
    int minimum = slider.getMinimum();
    if(0 <= minimum && minimum < value) {
      slider.setMaximum(value);
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
    int maximum = slider.getMaximum();
    if(0 <= value && value < maximum) {
      slider.setMinimum(value);
    }
  }

  /**
   * Sets the amount that the receiver's value will be
   * modified by when the page increment/decrement areas
   * are selected to the argument, which must be at least
   * one.
   *
   * @return the page increment (must be greater than zero)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setPageIncrement(int pageIncrement) {
    checkWidget();
    if(pageIncrement < 1) {
      return;
    }
    int minimum = slider.getMinimum();
    int maximum = slider.getMaximum();
    if(pageIncrement > maximum - minimum) {
      return;
    }
    slider.setMajorTickSpacing(pageIncrement);
    slider.setMinorTickSpacing(pageIncrement);
  }

  /**
   * Sets the single <em>selection</em> that is the receiver's
   * value to the argument which must be greater than or equal
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
    slider.setValue(value);
  }

//  int widgetStyle() {
//    int bits = super.widgetStyle() | OS.WS_TABSTOP | OS.TBS_BOTH |
//        OS.TBS_AUTOTICKS;
//    if((style & SWT.HORIZONTAL) != 0) {
//      return bits | OS.TBS_HORZ;
//    }
//    return bits | OS.TBS_VERT;
//  }

  LRESULT wmScrollChild(int wParam, int lParam) {

    /* Do nothing when scrolling is ending */
    int code = wParam & 0xFFFF;
    if(code == OS.TB_ENDTRACK) {
      return null;
    }

    /*
     * This code is intentionally commented.  The event
     * detail field is not currently supported on all
     * platforms.
     */
    Event event = new Event();
//	switch (code) {
//		/*
//		* This line is intentionally commented.  Do not set the detail
//		* field to DRAG to indicate that the dragging has ended when the
//		* scroll bar is finally positioned in TB_THUMBPOSITION.
//		*/
////		case OS.TB_THUMBPOSITION: 	break;
//		case OS.TB_THUMBTRACK:		event.detail = SWT.DRAG;  break;
//		case OS.TB_TOP: 			event.detail = SWT.HOME;  break;
//		case OS.TB_BOTTOM:		event.detail = SWT.END;  break;
//		case OS.TB_LINEDOWN:		event.detail = SWT.ARROW_DOWN;  break;
//		case OS.TB_LINEUP: 		event.detail = SWT.ARROW_UP;  break;
//		case OS.TB_PAGEDOWN: 		event.detail = SWT.PAGE_DOWN;  break;
//		case OS.TB_PAGEUP: 		event.detail = SWT.PAGE_UP;  break;
//	}

    /*
     * Send the event because WM_HSCROLL and WM_VSCROLL
     * are sent from a modal message loop in windows that
     * is active when the user is scrolling.
     */
    sendEvent(SWT.Selection, event);
    // widget could be disposed at this point
    return null;
  }

}
