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
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Instances of the receiver represent a selectable user interface object
 * that allows the user to drag a rubber banded outline of the sash within
 * the parent control.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Sash extends Control {
//  boolean dragging;
  int lastX, lastY;
//  final static int INCREMENT = 1;
//  final static int PAGE_INCREMENT = 9;

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
  public Sash(Composite parent, int style) {
    super(parent, checkStyle(style));
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the control is selected, by sending
   * it one of the messages defined in the <code>SelectionListener</code>
   * interface.
   * <p>
   * When <code>widgetSelected</code> is called, the x, y, width, and height fields of the event object are valid.
   * If the reciever is being dragged, the event object detail field contains the value <code>SWT.DRAG</code>.
   * <code>widgetDefaultSelected</code> is not called.
   * </p>
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
   * @see SelectionEvent
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

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the control is selected.
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

  java.awt.Point startPoint = null;
  
  Container getNewHandle() {
    JSplitPane splitPane = new JSplitPane((style & SWT.HORIZONTAL) != 0? JSplitPane.VERTICAL_SPLIT: JSplitPane.HORIZONTAL_SPLIT);
    final BasicSplitPaneDivider divider = ((BasicSplitPaneUI)splitPane.getUI()).getDivider();
    if((style & SWT.BORDER) != 0) {
      divider.setBorder(javax.swing.UIManager.getBorder("TextField.border"));
    }
    MouseAdapter mouseAdapter = new MouseAdapter() {
      public void mousePressed(java.awt.event.MouseEvent e) {
        startPoint = e.getPoint();
        lastX = -1;
        lastY = -1;
      }
    };
    MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
      public void mouseDragged(java.awt.event.MouseEvent e) {
        Event event = new Event();
        Rectangle bounds = getBounds();
        event.x = bounds.x;
        event.y = bounds.y;
        if((style & SWT.VERTICAL) != 0) {
          event.x += e.getX() - startPoint.x;
        } else {
          event.y += e.getY() - startPoint.y;
        }
        event.width = bounds.width;
        event.height = bounds.height;
        event.detail = SWT.DRAG;
        Dimension parentSize = parent.getContentPane().getSize();
        if(event.x < 0) {
          event.x = 0;
        } else if(event.x + event.width > parentSize.width) {
          event.x = parentSize.width - event.width;
        }
        if(event.y < 0) {
          event.y = 0;
        } else if(event.y + event.height > parentSize.height) {
          event.y = parentSize.height - event.height;
        }
        int x = event.x;
        int y = event.y;
        if(lastX != x || lastY != y) {
          sendEvent(SWT.Selection, event);
        }
        // TODO: is it the way I should handle the event.doit?
        if(event.doit) {
          lastX = x;
          lastY = y;
        }
      }
    };
    divider.addMouseListener(mouseAdapter);    
    divider.addMouseMotionListener(mouseMotionAdapter);    
    return divider;

  }

  boolean overrideLayout() {
    return false;
  }

}
