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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.border.Border;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Container;

import org.eclipse.swt.internal.swing.Utils;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b>
 * <dd>FocusIn, FocusOut, Help, KeyDown, KeyUp, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, Move, Paint, Resize</dd>
 * </dl>
 * <p>
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * Note: Only one of LEFT_TO_RIGHT and RIGHT_TO_LEFT may be specified.
 */

public abstract class Control extends Widget implements Drawable2 {
  /**
   * the handle to the OS resource
   * (Warning: This field is platform dependent)
   */
  public int handle;

  /**
   * The handle to the OS resource
   * (Warning: This field is platform dependent)
   */
  public Container swingHandle;

  Composite parent;
  int drawCount, hCursor;
  int foreground, background;
  Menu menu;
  String toolTipText;
  Object layoutData;
  Accessible accessible;
  static final short[] ACCENTS = new short[] {
      '~', '`', '\'', '^', '"'};

  public int internal_new_GC(GCData data) {
    throw new IllegalStateException("Not supported by Swing implementation!");
  }

  /**
   * Prevents uninitialized instances from being created outside the package.
   */
  Control() {
  }

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
   * @see SWT#BORDER
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public Control(Composite parent, int style) {
    super(parent, style);
    this.parent = parent;
    createWidget();
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the control is moved or resized, by sending
   * it one of the messages defined in the <code>ControlListener</code>
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
   * @see ControlListener
   * @see #removeControlListener
   */
  public void addControlListener(ControlListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Resize, typedListener);
    addListener(SWT.Move, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the control gains or loses focus, by sending
   * it one of the messages defined in the <code>FocusListener</code>
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
   * @see FocusListener
   * @see #removeFocusListener
   */
  public void addFocusListener(FocusListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.FocusIn, typedListener);
    addListener(SWT.FocusOut, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when help events are generated for the control,
   * by sending it one of the messages defined in the
   * <code>HelpListener</code> interface.
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
   * @see HelpListener
   * @see #removeHelpListener
   */
  public void addHelpListener(HelpListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Help, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when keys are pressed and released on the system keyboard, by sending
   * it one of the messages defined in the <code>KeyListener</code>
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
   * @see KeyListener
   * @see #removeKeyListener
   */
  public void addKeyListener(KeyListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.KeyUp, typedListener);
    addListener(SWT.KeyDown, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when mouse buttons are pressed and released, by sending
   * it one of the messages defined in the <code>MouseListener</code>
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
   * @see MouseListener
   * @see #removeMouseListener
   */
  public void addMouseListener(MouseListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.MouseDown, typedListener);
    addListener(SWT.MouseUp, typedListener);
    addListener(SWT.MouseDoubleClick, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the mouse passes or hovers over controls, by sending
   * it one of the messages defined in the <code>MouseTrackListener</code>
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
   * @see MouseTrackListener
   * @see #removeMouseTrackListener
   */
  public void addMouseTrackListener(MouseTrackListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.MouseEnter, typedListener);
    addListener(SWT.MouseExit, typedListener);
    addListener(SWT.MouseHover, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the mouse moves, by sending it one of the
   * messages defined in the <code>MouseMoveListener</code>
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
   * @see MouseMoveListener
   * @see #removeMouseMoveListener
   */
  public void addMouseMoveListener(MouseMoveListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.MouseMove, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the receiver needs to be painted, by sending it
   * one of the messages defined in the <code>PaintListener</code>
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
   * @see PaintListener
   * @see #removePaintListener
   */
  public void addPaintListener(PaintListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Paint, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when traversal events occur, by sending it
   * one of the messages defined in the <code>TraverseListener</code>
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
   * @see TraverseListener
   * @see #removeTraverseListener
   */
  public void addTraverseListener(TraverseListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Traverse, typedListener);
  }

  int callWindowProc(int msg, int wParam, int lParam) {
    throw new IllegalStateException("Not supported");
  }

  void checkOrientation(Widget parent) {
    super.checkOrientation(parent);
    if((style & SWT.RIGHT_TO_LEFT) != 0) {
      style |= SWT.MIRRORED;
    }
  }

  /**
   * Returns the preferred size of the receiver.
   * <p>
   * The <em>preferred size</em> of a control is the size that it would
   * best be displayed at. The width hint and height hint arguments
   * allow the caller to ask a control questions such as "Given a particular
   * width, how high does the control need to be to show all of the contents?"
   * To indicate that the caller does not wish to constrain a particular
   * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
   * </p>
   *
   * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
   * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
   * @return the preferred size of the control
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Layout
   * @see #getBorderWidth
   * @see #getBounds
   * @see #getSize
   * @see #pack
   * @see "computeTrim, getClientArea for controls that implement them"
   */
  public Point computeSize(int wHint, int hHint) {
    return computeSize(wHint, hHint, true);
  }

  /**
   * Returns the preferred size of the receiver.
   * <p>
   * The <em>preferred size</em> of a control is the size that it would
   * best be displayed at. The width hint and height hint arguments
   * allow the caller to ask a control questions such as "Given a particular
   * width, how high does the control need to be to show all of the contents?"
   * To indicate that the caller does not wish to constrain a particular
   * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
   * </p><p>
   * If the changed flag is <code>true</code>, it indicates that the receiver's
   * <em>contents</em> have changed, therefore any caches that a layout manager
   * containing the control may have been keeping need to be flushed. When the
   * control is resized, the changed flag will be <code>false</code>, so layout
   * manager caches can be retained.
   * </p>
   *
   * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
   * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
   * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
   * @return the preferred size of the control.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Layout
   * @see #getBorderWidth
   * @see #getBounds
   * @see #getSize
   * @see #pack
   * @see "computeTrim, getClientArea for controls that implement them"
   */
  public Point computeSize(int wHint, int hHint, boolean changed) {
    checkWidget();
    // TODO: find a way to use the hint?
    Dimension size = getHandle().getPreferredSize();
    Dimension contentSize = getContentPane().getPreferredSize();
    int width = (int)size.getWidth();
    int height = (int)size.getHeight();
    if(wHint != SWT.DEFAULT) {
      width = wHint + width - (int)contentSize.getWidth();
    }
    if(hHint != SWT.DEFAULT) {
      height = hHint + height - (int)contentSize.getHeight();
    }
    return new Point(width, height);
  }

  Control computeTabGroup() {
    if(isTabGroup()) {
      return this;
    }
    return parent.computeTabGroup();
  }

  Control computeTabRoot() {
    Control[] tabList = parent._getTabList();
    if(tabList != null) {
      int index = 0;
      while(index < tabList.length) {
        if(tabList[index] == this) {
          break;
        }
        index++;
      }
      if(index == tabList.length) {
        if(isTabGroup()) {
          return this;
        }
      }
    }
    return parent.computeTabRoot();
  }

  Control[] computeTabList() {
    if(isTabGroup()) {
      if(getVisible() && getEnabled()) {
        return new Control[] {
            this};
      }
    }
    return new Control[0];
  }

  /**
   * Returns the receiver's monitor.
   * 
   * @return the receiver's monitor
   * 
   * @since 3.0
   */
  public Monitor getMonitor () {
    checkWidget ();
    // TODO: implement method content
    throw new IllegalStateException("Not yet implemented!");
//    if (OS.IsWinCE || (OS.WIN32_MAJOR << 16 | OS.WIN32_MINOR) < (4 << 16 | 10)) {
//      return display.getPrimaryMonitor ();
//    }
//    int hmonitor = OS.MonitorFromWindow (handle, OS.MONITOR_DEFAULTTONEAREST);
//    MONITORINFO lpmi = new MONITORINFO ();
//    lpmi.cbSize = MONITORINFO.sizeof;
//    OS.GetMonitorInfo (hmonitor, lpmi);
//    Monitor monitor = new Monitor ();
//    monitor.handle = hmonitor;
//    monitor.x = lpmi.rcMonitor_left;
//    monitor.y = lpmi.rcMonitor_top;
//    monitor.width = lpmi.rcMonitor_right - lpmi.rcMonitor_left;
//    monitor.height = lpmi.rcMonitor_bottom - lpmi.rcMonitor_top;
//    monitor.clientX = lpmi.rcWork_left;
//    monitor.clientY = lpmi.rcWork_top;
//    monitor.clientWidth = lpmi.rcWork_right - lpmi.rcWork_left;
//    monitor.clientHeight = lpmi.rcWork_bottom - lpmi.rcWork_top;
//    return monitor;
  }

  /**
   * Get the top container to add to the content pane of the parent.
   * @return The top container.
   */
  Container getNewHandle() {
    // TODO: set this method to be abstract when all the widgets will be completed.
    return null;
  }
  
  Container getHandle() {
    return swingHandle;
  }

  /**
   * Set the actual swing container that is manipulated. If not specified, this
   * considers the top container to be the component.
   * @param The actual swing container that is manipulated.
   */
  void setSwingContainer(Container container) {
    if(swingContainer != null) {
      throw new IllegalStateException("setSwingContainer() must be called from the getNewHandle() method");
    }
    swingContainer = container;
  }
  
  Container swingContainer;

  Container getSwingContainer() {
    return swingContainer;
  }

  /**
   * Set the content pane of the swing container. If not specified, this
   * considers the swing container to be the content pane.
   * @return The content pane of the swing component.
   */
  void setContentPane(Container container) {
    if(contentPane != null) {
      throw new IllegalStateException("setContentPane() must be called from the getNewHandle() method");
    }
    contentPane = container;
  }

  Container contentPane;

  Container getContentPane() {
    return contentPane;
  }

  void addSwingListeners() {
    addSwingControlListeners();
  }

  void sendEvent (int eventType, Event event, boolean send) {
    super.sendEvent(eventType, event, send);
    if(event != null && parent != null && !hooks(event.type)) {
      event.item = parent;
      java.awt.Point p = SwingUtilities.convertPoint(getContentPane(), 0, 0, parent.getContentPane());
      event.x = event.x + p.x;
      event.y = event.y + p.y;
      parent.sendEvent(eventType, event, send);
    }
  }

  java.awt.Point lastMousePosition = new java.awt.Point(0, 0);

  void addSwingControlListeners() {
    getSwingContainer().addMouseListener(new java.awt.event.MouseListener() {
      public void mousePressed(java.awt.event.MouseEvent e) {
        swingMousePressed(e);
      }
      public void mouseReleased(java.awt.event.MouseEvent e) {
        swingMouseReleased(e);
      }
      public void mouseClicked(java.awt.event.MouseEvent e) {
        swingMouseClicked(e);
      }
      public void mouseEntered(java.awt.event.MouseEvent e) {
        swingMouseEntered(e);
      }
      public void mouseExited(java.awt.event.MouseEvent e) {
        swingMouseExited(e);
      }
    });
    getSwingContainer().addMouseMotionListener(new java.awt.event.MouseMotionListener() {
      public void mouseDragged(java.awt.event.MouseEvent e) {
        swingMouseDragged(e);
      }
      public void mouseMoved(java.awt.event.MouseEvent e) {
        swingMouseMoved(e);
      }
    });
    getContentPane().addComponentListener(new ComponentListener() {
      public void componentHidden(ComponentEvent e) {
        swingComponentHidden(e);
      }
      public void componentMoved(ComponentEvent e) {
        swingComponentMoved(e);
      }
      public void componentResized(ComponentEvent e) {
        swingComponentResized(e);
      }
      public void componentShown(ComponentEvent e) {
        swingComponentShown(e);
      }
    });
  }

  void maybeShowPopup(java.awt.event.MouseEvent e) {
    if(menu != null && e.isPopupTrigger()) {
      java.awt.Point p = new java.awt.Point(e.getPoint());
      SwingUtilities.convertPointToScreen(p, e.getComponent());
      menu.setLocation(new Point(p.x, p.y));
      menu.setVisible(true);
    }
  }

  void swingMousePressed(java.awt.event.MouseEvent e) {
    if(!isDisposed()) {
      maybeShowPopup(e);
      Event event = new Event();
      Point point = convertPointToControl(e.getPoint());
      event.x = point.x;
      event.y = point.y;
      if((e.getModifiersEx() & java.awt.event.MouseEvent.BUTTON1_DOWN_MASK) != 0) {
        event.button = 1;
      } else if((e.getModifiersEx() & java.awt.event.MouseEvent.BUTTON2_DOWN_MASK) != 0) {
        event.button = 2;
      } else if((e.getModifiersEx() & java.awt.event.MouseEvent.BUTTON3_DOWN_MASK) != 0) {
        event.button = 3;
      }
      event.stateMask = getDisplay().getInputState();
      postEvent(SWT.MouseDown, event);
    }
  }

  void swingMouseReleased(java.awt.event.MouseEvent e) {
    if(!isDisposed()) {
      maybeShowPopup(e);
      Event event = new Event();
      Point point = convertPointToControl(e.getPoint());
      event.x = point.x;
      event.y = point.y;
      event.button = Display.getButtonNumber(e.getButton());
      event.stateMask = getDisplay().getInputState();
      postEvent(SWT.MouseUp, event);
    }
  }

  void swingMouseClicked(java.awt.event.MouseEvent e) {
    if(!isDisposed() && e.getClickCount() == 2) {
      Event event = new Event();
      Point point = convertPointToControl(e.getPoint());
      event.x = point.x;
      event.y = point.y;
      event.stateMask = getDisplay().getInputState();
      postEvent(SWT.MouseDoubleClick, event);
    }
  }

  boolean isInComponent = false; 
  java.awt.event.MouseEvent lastMouseMoveEvent = null; 
  java.util.Timer timer = null;

  void swingMouseEntered(java.awt.event.MouseEvent e) {
    if(!isDisposed()) {
      Event event = new Event();
      Point point = convertPointToControl(e.getPoint());
      event.x = point.x;
      event.y = point.y;
      event.stateMask = getDisplay().getInputState();
      postEvent(SWT.MouseEnter, event);
      isInComponent = true;
      if(hooks(SWT.MouseHover)) {
        timer = new java.util.Timer();
        timer.schedule(new java.util.TimerTask() {
          public void run() {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                if(isInComponent && !isDisposed()) {
                  swingMouseMoved(lastMouseMoveEvent);
                } else {
                  cancel();
                }
              }
            });
          }
        }, 250, 250);
      }
    }
  }

  void swingMouseExited(java.awt.event.MouseEvent e) {
    if(timer != null) {
      timer.cancel();
      timer = null;
    }
    if(!isDisposed()) {
      isInComponent = false;
      Event event = new Event();
      Point point = convertPointToControl(e.getPoint());
      event.x = point.x;
      event.y = point.y;
      event.stateMask = getDisplay().getInputState();
      postEvent(SWT.MouseExit, event);
    }
  }

  void swingMouseDragged(java.awt.event.MouseEvent e) {
    if(!isDisposed()) {
      Event event = new Event();
      event.detail = SWT.DRAG;
      Point point = convertPointToControl(e.getPoint());
      event.x = point.x;
      event.y = point.y;
      event.stateMask = getDisplay().getInputState();
      postEvent(SWT.MouseMove, event);
    }
  }

  void swingMouseMoved(java.awt.event.MouseEvent e) {
    if(!isDisposed()) {
      lastMouseMoveEvent = e;
      Event event = new Event();
      Point point = convertPointToControl(e.getPoint());
      event.x = point.x;
      event.y = point.y;
      event.stateMask = getDisplay().getInputState();
      java.awt.Point newMousePosition = e.getPoint();
      if(e.getPoint().equals(lastMousePosition)) {
        postEvent(SWT.MouseHover, event);
      } else {
        postEvent(SWT.MouseMove, event);
        lastMousePosition = e.getPoint();
      }
    }
  }

  void swingComponentHidden(ComponentEvent e) {
    sendEvent(SWT.Hide);
  }

  void swingComponentMoved(ComponentEvent e) {
    postEvent(SWT.Move);
//    Event event = new Event();
//    event.gc = GC.swing_new(Control.this);
//    postEvent(SWT.Paint, event);
  }

  void swingComponentResized(ComponentEvent e) {
    // To be sure size computations by SWT are correct, we have to wait for all Swing events to be processed
    SwingUtilities.invokeLater(new Runnable(){
      public void run() {
        sendEvent(SWT.Resize);
      }
    });
//    postEvent(SWT.Resize);
//    Event event = new Event();
//    event.gc = GC.swing_new(Control.this);
//    postEvent(SWT.Paint, event);
  }

  void swingComponentShown(ComponentEvent e) {
    sendEvent(SWT.Show);
  }
  
  void createHandle() {
    swingHandle = getNewHandle();
    if(swingContainer == null) {
      swingContainer = swingHandle;
    }
    if(contentPane == null) {
      contentPane = swingContainer;
    }
    if(overrideLayout()) {
      contentPane.setLayout(null);
    }
    addSwingListeners();
  }

  boolean overrideLayout() {
    return true;
  }

  void createWidget() {
    foreground = background = -1;
    checkOrientation(parent);
    createHandle();
    if(swingHandle instanceof JComponent) {
      JComponent comp = (JComponent)swingHandle;
      if(!comp.isOpaque()) {
        comp.setOpaque(true);
      }
    }
    if(parent != null) {
      Container handle = getHandle();
      if(handle instanceof Window) {
      } else {
        if(parent.isAcceptingChild(this)) {
          parent.getContentPane().add(handle);
        }
      }
    }
    fixInitSize();
    register();
//    subclass();
//    setDefaultFont();
  }

  boolean isInitSizeSet = false;

  /** Fixes the initialization size to not be {0, 0} */
  void fixInitSize() {
    if(!isInitSizeSet) {
      Container handle = getHandle();
      handle.setSize(handle.getPreferredSize());
      if(handle instanceof JComponent) {
        ((JComponent)handle).revalidate();
      } else {
        handle.validate();
      }
      if(parent != null) {
        parent.fixInitSize();
      }
    }
  }

  int defaultBackground() {
    if(OS.IsWinCE) {
      return OS.GetSysColor(OS.COLOR_WINDOW);
    }
    return OS.GetSysColor(OS.COLOR_BTNFACE);
  }

  int defaultFont() {
    Display display = getDisplay();
    return display.systemFont();
  }

  int defaultForeground() {
    return OS.GetSysColor(OS.COLOR_WINDOWTEXT);
  }

  void deregister() {
    WidgetTable.remove(swingHandle);
  }

  void destroyWidget() {
    int hwnd = handle;
    releaseHandle();
    if(hwnd != 0) {
      OS.DestroyWindow(hwnd);
    }
  }

  void drawBackground(int hDC) {
    RECT rect = new RECT();
    OS.GetClientRect(handle, rect);
    drawBackground(hDC, rect);
  }

  void drawBackground(int hDC, RECT rect) {
    Display display = getDisplay();
    int hPalette = display.hPalette;
    if(hPalette != 0) {
      OS.SelectPalette(hDC, hPalette, false);
      OS.RealizePalette(hDC);
    }
//    int pixel = getBackgroundPixel();
//    int hBrush = findBrush(pixel);
//    OS.FillRect(hDC, rect, hBrush);
  }

  int findBrush(int pixel) {
    return parent.findBrush(pixel);
  }

  int findCursor() {
    if(hCursor != 0) {
      return hCursor;
    }
    return parent.findCursor();
  }

//  char findMnemonic(String string) {
//    int index = 0;
//    int length = string.length();
//    do {
//      while(index < length && string.charAt(index) != Mnemonic) {
//        index++;
//      }
//      if(++index >= length) {
//        return '\0';
//      }
//      if(string.charAt(index) != Mnemonic) {
//        return string.charAt(index);
//      }
//      index++;
//    } while(index < length);
//    return '\0';
//  }

  void fixFocus() {
    Shell shell = getShell();
    Control control = this;
    while((control = control.parent) != null) {
      if(control.setFocus() || control == shell) {
        return;
      }
    }
    OS.SetFocus(0);
  }

  /**
   * Forces the receiver to have the <em>keyboard focus</em>, causing
   * all keyboard events to be delivered to it.
   *
   * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #setFocus
   */
  public boolean forceFocus() {
    checkWidget();
    Decorations shell = menuShell();
    shell.setSavedFocus(this);
    if(!isEnabled() || !isVisible() || !isActive()) {
      return false;
    }
    if(isFocusControl()) {
      return true;
    }
    shell.bringToTop();
    getHandle().requestFocus();
    return isFocusControl();
  }

  void forceResize() {
    if(parent == null) {
      return;
    }
    WINDOWPOS[] lpwp = parent.lpwp;
    if(lpwp == null) {
      return;
    }
    for(int i = 0; i < lpwp.length; i++) {
      WINDOWPOS wp = lpwp[i];
      if(wp != null && wp.hwnd == handle) {
        /*
         * This code is intentionally commented.  All widgets that
         * are created by SWT have WS_CLIPSIBLINGS to ensure that
         * application code does not draw outside of the control.
         */
//			int count = parent.getChildrenCount ();
//			if (count > 1) {
//				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//				if ((bits & OS.WS_CLIPSIBLINGS) == 0) wp.flags |= OS.SWP_NOCOPYBITS;
//			}
        OS.SetWindowPos(wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
        lpwp[i] = null;
        return;
      }
    }
  }

  /**
   * Returns the accessible object for the receiver.
   * If this is the first time this object is requested,
   * then the object is created and returned.
   *
   * @return the accessible object
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Accessible#addAccessibleListener
   * @see Accessible#addAccessibleControlListener
   *
   * @since 2.0
   */
  public Accessible getAccessible() {
    checkWidget();
    if(accessible == null) {
      accessible = new_Accessible(this);
    }
    return accessible;
  }

  /**
   * Returns the receiver's background color.
   *
   * @return the background color
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Color getBackground() {
    checkWidget();
    return Color.swing_new(getDisplay(), getHandle().getBackground());
  }

//  int getBackgroundPixel() {
//    if(background == -1) {
//      return defaultBackground();
//    }
//    return background;
//  }

  /**
   * Returns the receiver's border width.
   *
   * @return the border width
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getBorderWidth() {
    checkWidget();
    Container container = getHandle();
    if(container instanceof JComponent) {
      Border border = ((JComponent)container).getBorder();
      if(border != null) {
        Insets insets = border.getBorderInsets(container);
        int total = insets.left + insets.right;
        return total / 2 + total % 2;
      }
    } else {
      Insets insets = container.getInsets();
      int total = insets.left + insets.right;
      return total / 2 + total % 2;
    }
    return 0;
  }

  /**
   * Returns a rectangle describing the receiver's size and location
   * relative to its parent (or its display if its parent is null).
   *
   * @return the receiver's bounding rectangle
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Rectangle getBounds() {
    checkWidget();
    java.awt.Rectangle rectangle = getHandle().getBounds();
    Control parent = getParent();
    if(parent != null) {
      Container parentHandle = parent.getHandle();
      if(parentHandle instanceof javax.swing.RootPaneContainer) {
        rectangle = SwingUtilities.convertRectangle(parent.getContentPane(), rectangle, ((javax.swing.RootPaneContainer)parentHandle).getContentPane());
      } else {
        rectangle = SwingUtilities.convertRectangle(parent.getContentPane(), rectangle, parentHandle);
      }
    }
    return new Rectangle((int)rectangle.getX(), (int)rectangle.getY(), (int)rectangle.getWidth(), (int)rectangle.getHeight());
  }

  int getCodePage() {
    //TODO: implement content of method...
    throw new IllegalStateException("Not implemented...");
//    int hFont = OS.SendMessage(handle, OS.WM_GETFONT, 0, 0);
//    LOGFONT logFont = new LOGFONT();
//    OS.GetObject(hFont, LOGFONT.sizeof, logFont);
//    int cs = logFont.lfCharSet & 0xFF;
//    int[] lpCs = new int[8];
//    if(OS.TranslateCharsetInfo(cs, lpCs, OS.TCI_SRCCHARSET)) {
//      return lpCs[1];
//    }
//    return OS.GetACP();
  }

  /**
   * Returns the display that the receiver was created on.
   *
   * @return the receiver's display
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Display getDisplay() {
    Composite parent = this.parent;
    if(parent == null) {
      error(SWT.ERROR_WIDGET_DISPOSED);
    }
    return parent.getDisplay();
  }

  /**
   * Returns <code>true</code> if the receiver is enabled, and
   * <code>false</code> otherwise. A disabled control is typically
   * not selectable from the user interface and draws with an
   * inactive or "grayed" look.
   *
   * @return the receiver's enabled state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #isEnabled
   */
  public boolean getEnabled() {
    checkWidget();
    return getHandle().isEnabled();
  }

  /**
   * Returns the font that the receiver will use to paint textual information.
   *
   * @return the receiver's font
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Font getFont() {
    checkWidget();
//    int hFont = OS.SendMessage(handle, OS.WM_GETFONT, 0, 0);
//    if(hFont == 0) {
//      hFont = defaultFont();
//    }
    return Font.swing_new(getDisplay(), getHandle().getFont());
  }

  /**
   * Returns the foreground color that the receiver will use to draw.
   *
   * @return the receiver's foreground color
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Color getForeground() {
    checkWidget();
    return Color.swing_new(getDisplay(), getHandle().getForeground());
  }

//  int getForegroundPixel() {
//    if(foreground == -1) {
//      return defaultForeground();
//    }
//    return foreground;
//  }

  /**
   * Returns layout data which is associated with the receiver.
   *
   * @return the receiver's layout data
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Object getLayoutData() {
    checkWidget();
    return layoutData;
  }

  Point convertPointToControl(java.awt.Point point) {
    point = SwingUtilities.convertPoint(getSwingContainer(), point, getHandle());
    Control parent = getParent();
    if(parent != null) {
      Container parentHandle = parent.getHandle();
      if(parentHandle instanceof javax.swing.RootPaneContainer) {
        point = SwingUtilities.convertPoint(parent.getContentPane(), point, ((javax.swing.RootPaneContainer)parentHandle).getContentPane());
      } else {
        point = SwingUtilities.convertPoint(parent.getContentPane(), point, parentHandle);
      }
    } else {
      point = SwingUtilities.convertPoint(getHandle(), point, getContentPane());
    }
    return new Point(point.x, point.y);
  }

  /**
   * Returns a point describing the receiver's location relative
   * to its parent (or its display if its parent is null).
   *
   * @return the receiver's location
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Point getLocation() {
    checkWidget();
//    forceResize();
    java.awt.Point location = getHandle().getLocation();
    Control parent = getParent();
    if(parent != null) {
      Container parentHandle = parent.getHandle();
      if(parentHandle instanceof javax.swing.RootPaneContainer) {
        location = SwingUtilities.convertPoint(parent.getContentPane(), location, ((javax.swing.RootPaneContainer)parentHandle).getContentPane());
      } else {
        location = SwingUtilities.convertPoint(parent.getContentPane(), location, parentHandle);
      }
    }
    return new Point((int)location.getX(), (int)location.getY());
  }

  /**
   * Returns the receiver's pop up menu if it has one, or null
   * if it does not. All controls may optionally have a pop up
   * menu that is displayed when the user requests one for
   * the control. The sequence of key strokes, button presses
   * and/or button releases that are used to request a pop up
   * menu is platform specific.
   *
   * @return the receiver's menu
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Menu getMenu() {
    checkWidget();
    return menu;
  }

  /**
   * Returns the receiver's parent, which must be a <code>Composite</code>
   * or null when the receiver is a shell that was created with null or
   * a display for a parent.
   *
   * @return the receiver's parent
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Composite getParent() {
    checkWidget();
    return parent;
  }

  Control[] getPath() {
    int count = 0;
    Shell shell = getShell();
    Control control = this;
    while(control != shell) {
      count++;
      control = control.parent;
    }
    control = this;
    Control[] result = new Control[count];
    while(control != shell) {
      result[--count] = control;
      control = control.parent;
    }
    return result;
  }

  /**
   * Returns the receiver's shell. For all controls other than
   * shells, this simply returns the control's nearest ancestor
   * shell. Shells return themselves, even if they are children
   * of other shells.
   *
   * @return the receiver's shell
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #getParent
   */
  public Shell getShell() {
    checkWidget();
    return parent.getShell();
  }

  /**
   * Returns a point describing the receiver's size. The
   * x coordinate of the result is the width of the receiver.
   * The y coordinate of the result is the height of the
   * receiver.
   *
   * @return the receiver's size
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
//  public Point getSize() {
//    checkWidget();
//    forceResize();
//    RECT rect = new RECT();
//    OS.GetWindowRect(handle, rect);
//    int width = rect.right - rect.left;
//    int height = rect.bottom - rect.top;
//    return new Point(width, height);
//  }
  public Point getSize() {
    checkWidget();
    Dimension size = getHandle().getSize();
    return new Point((int)size.getWidth(), (int)size.getHeight());
  }

  /**
   * Returns the receiver's tool tip text, or null if it has
   * not been set.
   *
   * @return the receiver's tool tip text
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public String getToolTipText() {
    checkWidget();
    return toolTipText;
  }

  /**
   * Returns <code>true</code> if the receiver is visible, and
   * <code>false</code> otherwise.
   * <p>
   * If one of the receiver's ancestors is not visible or some
   * other condition makes the receiver not visible, this method
   * may still indicate that it is considered visible even though
   * it may not actually be showing.
   * </p>
   *
   * @return the receiver's visibility state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean getVisible() {
    checkWidget();
    return getHandle().isVisible();
  }

  boolean hasCursor() {
    RECT rect = new RECT();
    if(!OS.GetClientRect(handle, rect)) {
      return false;
    }
    if(OS.MapWindowPoints(handle, 0, rect, 2) == 0) {
      return false;
    }
    POINT pt = new POINT();
    return(OS.GetCursorPos(pt) && OS.PtInRect(rect, pt));
  }

  /**
   * Gets the device in use by this control.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Control</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @return the Device of this drawable object.
   */

  public Device internal_get_Device() {
    return getDisplay();
  }
  
  /**
   * Invokes platform specific functionality to allocate a new GC handle.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Control</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @return the platform specific GC handle
   */
  public Graphics internal_new_GC() {
    checkWidget();
    Container handle = getContentPane();
    Graphics graphics = handle.getGraphics();
//    // Realize the component to get the graphics
//    if(graphics == null) {
//      getShell().getHandle().addNotify();
//      graphics = handle.getGraphics();
//    }
    if(graphics == null) {
      SWT.error(SWT.ERROR_NO_HANDLES);
    }
    return graphics;
  }

  /**
   * Invokes platform specific functionality to dispose a GC handle.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Control</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @param handle the platform specific GC handle
   * @param data the platform specific GC data
   */
  public void internal_dispose_GC(int hDC, GCData data) {
    checkWidget();
    if(data == null || data.ps == null) {
      OS.ReleaseDC(handle, hDC);
    } else {
      OS.EndPaint(handle, data.ps);
    }
  }

  boolean isActive() {
    Display display = getDisplay();
    Shell modal = display.getModalShell();
    if(modal != null && modal != this) {
      if((modal.style & SWT.PRIMARY_MODAL) != 0) {
        Shell shell = getShell();
        if(modal.parent == shell) {
          return false;
        }
      }
      int bits = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
      if((modal.style & bits) != 0) {
        Control control = this;
        while(control != null) {
          if(control == modal) {
            break;
          }
          control = control.parent;
        }
        if(control != modal) {
          return false;
        }
      }
    }
    return getShell().getEnabled();
  }

  public boolean isDisposed() {
    return swingHandle == null;
  }

  /**
   * Returns <code>true</code> if the receiver is enabled and all
   * of the receiver's ancestors are enabled, and <code>false</code>
   * otherwise. A disabled control is typically not selectable from the
   * user interface and draws with an inactive or "grayed" look.
   *
   * @return the receiver's enabled state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #getEnabled
   */
  public boolean isEnabled() {
    checkWidget();
    return getEnabled() && parent.isEnabled();
  }

  /**
   * Returns <code>true</code> if the receiver has the user-interface
   * focus, and <code>false</code> otherwise.
   *
   * @return the receiver's focus state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean isFocusControl() {
    checkWidget();
    return getHandle().hasFocus();
  }

  boolean isFocusAncestor() {
    Display display = getDisplay();
    Control control = display.getFocusControl();
    while(control != null && control != this) {
      control = control.parent;
    }
    return control == this;
  }

  /**
   * Returns <code>true</code> if the underlying operating
   * system supports this reparenting, otherwise <code>false</code>
   *
   * @return <code>true</code> if the widget can be reparented, otherwise <code>false</code>
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean isReparentable() {
    checkWidget();
    return true;
  }

  boolean isShowing() {
    /*
     * This is not complete.  Need to check if the
     * widget is obscurred by a parent or sibling.
     */
    if(!isVisible()) {
      return false;
    }
    Control control = this;
    while(control != null) {
      Point size = control.getSize();
      if(size.x == 0 || size.y == 0) {
        return false;
      }
      control = control.parent;
    }
    return true;
    /*
     * Check to see if current damage is included.
     */
//	if (!OS.IsWindowVisible (handle)) return false;
//	int flags = OS.DCX_CACHE | OS.DCX_CLIPCHILDREN | OS.DCX_CLIPSIBLINGS;
//	int hDC = OS.GetDCEx (handle, 0, flags);
//	int result = OS.GetClipBox (hDC, new RECT ());
//	OS.ReleaseDC (handle, hDC);
//	return result != OS.NULLREGION;
  }

  boolean isTabGroup() {
    Control[] tabList = parent._getTabList();
    if(tabList != null) {
      for(int i = 0; i < tabList.length; i++) {
        if(tabList[i] == this) {
          return true;
        }
      }
    }
    int bits = OS.GetWindowLong(handle, OS.GWL_STYLE);
    return(bits & OS.WS_TABSTOP) != 0;
  }

  boolean isTabItem() {
    Control[] tabList = parent._getTabList();
    if(tabList != null) {
      for(int i = 0; i < tabList.length; i++) {
        if(tabList[i] == this) {
          return false;
        }
      }
    }
    int bits = OS.GetWindowLong(handle, OS.GWL_STYLE);
    if((bits & OS.WS_TABSTOP) != 0) {
      return false;
    }
    int code = OS.SendMessage(handle, OS.WM_GETDLGCODE, 0, 0);
    if((code & OS.DLGC_STATIC) != 0) {
      return false;
    }
    if((code & OS.DLGC_WANTALLKEYS) != 0) {
      return false;
    }
    if((code & OS.DLGC_WANTARROWS) != 0) {
      return false;
    }
    if((code & OS.DLGC_WANTTAB) != 0) {
      return false;
    }
    return true;
  }

  /**
   * Returns <code>true</code> if the receiver is visible and all
   * of the receiver's ancestors are visible and <code>false</code>
   * otherwise.
   *
   * @return the receiver's visibility state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #getVisible
   */
  public boolean isVisible() {
    checkWidget();
    return getHandle().isShowing();
  }

  Decorations menuShell() {
    return parent.menuShell();
  }

  boolean mnemonicHit(char key) {
    return false;
  }

  boolean mnemonicMatch(char key) {
    return false;
  }

  /**
   * Moves the receiver above the specified control in the
   * drawing order. If the argument is null, then the receiver
   * is moved to the top of the drawing order. The control at
   * the top of the drawing order will not be covered by other
   * controls even if they occupy intersecting areas.
   *
   * @param the sibling control (or null)
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void moveAbove(Control control) {
    checkWidget();
    // TODO: see how to support this
//    int hwndAbove = OS.HWND_TOP;
//    if(control != null) {
//      if(control.isDisposed()) {
//        error(SWT.ERROR_INVALID_ARGUMENT);
//      }
//      if(parent != control.parent) {
//        return;
//      }
//      int hwnd = control.handle;
//      if(hwnd == 0 || hwnd == handle) {
//        return;
//      }
//      hwndAbove = OS.GetWindow(hwnd, OS.GW_HWNDPREV);
//      /*
//       * Bug in Windows.  For some reason, when GetWindow ()
//       * with GW_HWNDPREV is used to query the previous window
//       * in the z-order with the first child, Windows returns
//       * the first child instead of NULL.  The fix is to detect
//       * this case and move the control to the top.
//       */
//      if(hwndAbove == 0 || hwndAbove == hwnd) {
//        hwndAbove = OS.HWND_TOP;
//      }
//    }
//    int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE;
//    OS.SetWindowPos(handle, hwndAbove, 0, 0, 0, 0, flags);
  }

  /**
   * Moves the receiver below the specified control in the
   * drawing order. If the argument is null, then the receiver
   * is moved to the bottom of the drawing order. The control at
   * the bottom of the drawing order will be covered by all other
   * controls which occupy intersecting areas.
   *
   * @param the sibling control (or null)
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void moveBelow(Control control) {
    checkWidget();
    int hwndAbove = OS.HWND_BOTTOM;
    if(control != null) {
      if(control.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
      }
      if(parent != control.parent) {
        return;
      }
      hwndAbove = control.handle;
    }
    if(hwndAbove == 0 || hwndAbove == handle) {
      return;
    }
    int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE;
    OS.SetWindowPos(handle, hwndAbove, 0, 0, 0, 0, flags);
  }

  Accessible new_Accessible(Control control) {
    return Accessible.internal_new_Accessible(this);
  }

  /**
   * Causes the receiver to be resized to its preferred size.
   * For a composite, this involves computing the preferred size
   * from its layout, if there is one.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #computeSize
   */
  public void pack() {
    checkWidget();
    pack(true);
  }

  /**
   * Causes the receiver to be resized to its preferred size.
   * For a composite, this involves computing the preferred size
   * from its layout, if there is one.
   * <p>
   * If the changed flag is <code>true</code>, it indicates that the receiver's
   * <em>contents</em> have changed, therefore any caches that a layout manager
   * containing the control may have been keeping need to be flushed. When the
   * control is resized, the changed flag will be <code>false</code>, so layout
   * manager caches can be retained.
   * </p>
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #computeSize
   */
  public void pack(boolean changed) {
    checkWidget();
    setSize(computeSize(SWT.DEFAULT, SWT.DEFAULT, changed));
  }

  /**
   * Causes the entire bounds of the receiver to be marked
   * as needing to be redrawn. The next time a paint request
   * is processed, the control will be completely painted.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #update
   */
  public void redraw() {
    checkWidget();
    getHandle().repaint();
//    if(!OS.IsWindowVisible(handle)) {
//      return;
//    }
//    if(OS.IsWinCE) {
//      OS.InvalidateRect(handle, null, true);
//    } else {
//      int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
//      OS.RedrawWindow(handle, null, 0, flags);
//    }
  }

  /**
   * Causes the rectangular area of the receiver specified by
   * the arguments to be marked as needing to be redrawn.
   * The next time a paint request is processed, that area of
   * the receiver will be painted. If the <code>all</code> flag
   * is <code>true</code>, any children of the receiver which
   * intersect with the specified area will also paint their
   * intersecting areas. If the <code>all</code> flag is
   * <code>false</code>, the children will not be painted.
   *
   * @param x the x coordinate of the area to draw
   * @param y the y coordinate of the area to draw
   * @param width the width of the area to draw
   * @param height the height of the area to draw
   * @param all <code>true</code> if children should redraw, and <code>false</code> otherwise
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #update
   */
  public void redraw(int x, int y, int width, int height, boolean all) {
    checkWidget();
    if(width <= 0 || height <= 0) {
      return;
    }
    if(!OS.IsWindowVisible(handle)) {
      return;
    }
    RECT rect = new RECT();
    OS.SetRect(rect, x, y, x + width, y + height);
    if(OS.IsWinCE) {
      OS.InvalidateRect(handle, rect, true);
    } else {
      int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
      if(all) {
        flags |= OS.RDW_ALLCHILDREN;
      }
      OS.RedrawWindow(handle, rect, 0, flags);
    }
  }

  void register() {
    WidgetTable.put(getHandle(), this);
  }

  void releaseHandle() {
    super.releaseHandle();
    Container parent = swingHandle.getParent();
    if(parent != null) {
      parent.remove(swingHandle);
      parent.repaint();
    }
    swingHandle = null;
  }

  void releaseWidget() {
    super.releaseWidget();
    setToolTipText(null);
//    if(toolTipText != null) {
//      Shell shell = getShell();
//      shell.setToolTipText(handle, null);
//    }
    toolTipText = null;
    if(menu != null && !menu.isDisposed()) {
      menu.dispose();
    }
    menu = null;
    deregister();
    parent = null;
    layoutData = null;
    if(accessible != null) {
      accessible.internal_dispose_Accessible();
    }
    accessible = null;
//    swingHandle = null;
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the control is moved or resized.
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
   * @see ControlListener
   * @see #addControlListener
   */
  public void removeControlListener(ControlListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Move, listener);
    eventTable.unhook(SWT.Resize, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the control gains or loses focus.
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
   * @see FocusListener
   * @see #addFocusListener
   */
  public void removeFocusListener(FocusListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.FocusIn, listener);
    eventTable.unhook(SWT.FocusOut, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the help events are generated for the control.
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
   * @see HelpListener
   * @see #addHelpListener
   */
  public void removeHelpListener(HelpListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Help, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when keys are pressed and released on the system keyboard.
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
   * @see KeyListener
   * @see #addKeyListener
   */
  public void removeKeyListener(KeyListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.KeyUp, listener);
    eventTable.unhook(SWT.KeyDown, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the mouse passes or hovers over controls.
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
   * @see MouseTrackListener
   * @see #addMouseTrackListener
   */
  public void removeMouseTrackListener(MouseTrackListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.MouseEnter, listener);
    eventTable.unhook(SWT.MouseExit, listener);
    eventTable.unhook(SWT.MouseHover, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when mouse buttons are pressed and released.
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
   * @see MouseListener
   * @see #addMouseListener
   */
  public void removeMouseListener(MouseListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.MouseDown, listener);
    eventTable.unhook(SWT.MouseUp, listener);
    eventTable.unhook(SWT.MouseDoubleClick, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the mouse moves.
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
   * @see MouseMoveListener
   * @see #addMouseMoveListener
   */
  public void removeMouseMoveListener(MouseMoveListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.MouseMove, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the receiver needs to be painted.
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
   * @see PaintListener
   * @see #addPaintListener
   */
  public void removePaintListener(PaintListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Paint, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when traversal events occur.
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
   * @see TraverseListener
   * @see #addTraverseListener
   */
  public void removeTraverseListener(TraverseListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Traverse, listener);
  }

  boolean sendKeyEvent(int type, int msg, int wParam, int lParam) {
    Event event = new Event();
    if(!setKeyState(event, type)) {
      return true;
    }
    return sendKeyEvent(type, msg, wParam, lParam, event);
  }

  boolean sendKeyEvent(int type, int msg, int wParam, int lParam, Event event) {
    postEvent(type, event);
    return true;
  }

  boolean sendMouseEvent(int type, int button, int msg, int wParam, int lParam) {
    Event event = new Event();
    event.button = button;
    event.x = (short)(lParam & 0xFFFF);
    event.y = (short)(lParam >> 16);
    setInputState(event, type);
    return sendMouseEvent(type, msg, wParam, lParam, event);
  }

  boolean sendMouseEvent(int type, int msg, int wParam, int lParam, Event event) {
    postEvent(type, event);
    return true;
  }

  /**
   * Sets the receiver's background color to the color specified
   * by the argument, or to the default system color for the control
   * if the argument is null.
   *
   * @param color the new color (or null)
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setBackground(Color color) {
    checkWidget();
    if(color == null) {
      // TODO: find the system color through UIManager, but per component...
      getHandle().setBackground(UIManager.getColor("control"));
    } else {
      getHandle().setBackground(color.swingHandle);
    }
//    int pixel = -1;
//    if(color != null) {
//      if(color.isDisposed()) {
//        SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//      }
//      pixel = color.handle;
//    }
//    setBackgroundPixel(pixel);
  }

//  void setBackgroundPixel(int pixel) {
//    if(background == pixel) {
//      return;
//    }
//    background = pixel;
//    OS.InvalidateRect(handle, null, true);
//  }

  /**
   * Sets the receiver's size and location to the rectangular
   * area specified by the arguments. The <code>x</code> and
   * <code>y</code> arguments are relative to the receiver's
   * parent (or its display if its parent is null).
   * <p>
   * Note: Attempting to set the width or height of the
   * receiver to a negative number will cause that
   * value to be set to zero instead.
   * </p>
   *
   * @param x the new x coordinate for the receiver
   * @param y the new y coordinate for the receiver
   * @param width the new width for the receiver
   * @param height the new height for the receiver
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setBounds(int x, int y, int width, int height) {
    checkWidget();
    Control parent = getParent();
    java.awt.Point location = new java.awt.Point(x, y);
    if(parent != null) {
      Container parentHandle = parent.getHandle();
      if(parentHandle instanceof javax.swing.RootPaneContainer) {
        location = SwingUtilities.convertPoint(((javax.swing.RootPaneContainer)parentHandle).getContentPane(), location, parent.getContentPane());
      } else {
        location = SwingUtilities.convertPoint(parentHandle, location, parent.getContentPane());
      }
    }
    Container handle = getHandle();
    handle.setBounds((int)location.getX(), (int)location.getY(), width, height);
    // Needed or some contents of containers are not correct
    if(handle instanceof JComponent) {
      ((JComponent)handle).revalidate();
    } else {
      handle.validate();
    }
    isInitSizeSet = true;
//    swingComponentMoved(null);
//    swingComponentResized(null);
  }

  /**
   * Sets the receiver's size and location to the rectangular
   * area specified by the argument. The <code>x</code> and
   * <code>y</code> fields of the rectangle are relative to
   * the receiver's parent (or its display if its parent is null).
   * <p>
   * Note: Attempting to set the width or height of the
   * receiver to a negative number will cause that
   * value to be set to zero instead.
   * </p>
   *
   * @param rect the new bounds for the receiver
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setBounds(Rectangle rect) {
    checkWidget();
    if(rect == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
//    if(getClass().getName().endsWith("Composite")) {
//      Thread.dumpStack();
//      rect.width = 45;
//    }
    setBounds(rect.x, rect.y, rect.width, rect.height);
  }

  /**
   * If the argument is <code>true</code>, causes the receiver to have
   * all mouse events delivered to it until the method is called with
   * <code>false</code> as the argument.
   *
   * @param capture <code>true</code> to capture the mouse, and <code>false</code> to release it
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setCapture(boolean capture) {
    checkWidget();
    if(capture) {
      OS.SetCapture(handle);
    } else {
      if(OS.GetCapture() == handle) {
        OS.ReleaseCapture();
      }
    }
  }

  /**
   * Sets the receiver's cursor to the cursor specified by the
   * argument, or to the default cursor for that kind of control
   * if the argument is null.
   * <p>
   * When the mouse pointer passes over a control its appearance
   * is changed to match the control's cursor.
   * </p>
   *
   * @param cursor the new cursor (or null)
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setCursor(Cursor cursor) {
    checkWidget();
    getHandle().setCursor(cursor == null? null: cursor.swingHandle);
  }

//  void setDefaultFont() {
//    Display display = getDisplay();
//    int hFont = display.systemFont();
//    OS.SendMessage(handle, OS.WM_SETFONT, hFont, 0);
//  }

  /**
   * Enables the receiver if the argument is <code>true</code>,
   * and disables it otherwise. A disabled control is typically
   * not selectable from the user interface and draws with an
   * inactive or "grayed" look.
   *
   * @param enabled the new enabled state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setEnabled(boolean enabled) {
    checkWidget();
    Container handle = getHandle();
    if(!enabled && isFocusAncestor())
      handle.transferFocus();
    handle.setEnabled(enabled);
    contentPane.setEnabled(enabled);
    swingContainer.setEnabled(enabled);
  }

  /**
   * Causes the receiver to have the <em>keyboard focus</em>,
   * such that all keyboard events will be delivered to it.
   *
   * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #forceFocus
   */
  public boolean setFocus() {
    checkWidget();
    return forceFocus();
  }

  /**
   * Sets the font that the receiver will use to paint textual information
   * to the font specified by the argument, or to the default font for that
   * kind of control if the argument is null.
   *
   * @param font the new font (or null)
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setFont(Font font) {
    checkWidget();
//    int hFont = 0;
    if(font != null) {
      if(font.isDisposed()) {
        SWT.error(SWT.ERROR_INVALID_ARGUMENT);
      }
      getHandle().setFont(font.swingHandle);
//      hFont = font.handle;
    }
//    if(hFont == 0) {
//      hFont = defaultFont();
//    }
//    OS.SendMessage(handle, OS.WM_SETFONT, hFont, 1);
  }

  /**
   * Sets the receiver's foreground color to the color specified
   * by the argument, or to the default system color for the control
   * if the argument is null.
   *
   * @param color the new color (or null)
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setForeground(Color color) {
    checkWidget();
    if(color == null) {
      // TODO: find the system color through UIManager, but per component...
      getHandle().setForeground(UIManager.getColor("textText"));
    } else {
      getHandle().setForeground(color.swingHandle);
    }
//    int pixel = -1;
//    if(color != null) {
//      if(color.isDisposed()) {
//        SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//      }
//      pixel = color.handle;
//    }
//    setForegroundPixel(pixel);
  }

//  void setForegroundPixel(int pixel) {
//    if(foreground == pixel) {
//      return;
//    }
//    foreground = pixel;
//    OS.InvalidateRect(handle, null, true);
//  }

  /**
   * Sets the layout data associated with the receiver to the argument.
   *
   * @param layoutData the new layout data for the receiver.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setLayoutData(Object layoutData) {
    checkWidget();
    this.layoutData = layoutData;
  }

  /**
   * Sets the receiver's location to the point specified by
   * the arguments which are relative to the receiver's
   * parent (or its display if its parent is null).
   *
   * @param x the new x coordinate for the receiver
   * @param y the new y coordinate for the receiver
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setLocation(int x, int y) {
    checkWidget();
    Control parent = getParent();
    java.awt.Point location = new java.awt.Point(x, y);
    if(parent != null) {
      Container parentHandle = parent.getHandle();
      if(parentHandle instanceof javax.swing.RootPaneContainer) {
        location = SwingUtilities.convertPoint(((javax.swing.RootPaneContainer)parentHandle).getContentPane(), location, parent.getContentPane());
      } else {
        location = SwingUtilities.convertPoint(parentHandle, location, parent.getContentPane());
      }
    }
    getHandle().setLocation(location);
//    swingComponentMoved(null);
    // TODO: check if needed
//    getHandle().doLayout();
  }

  /**
   * Sets the receiver's location to the point specified by
   * the argument which is relative to the receiver's
   * parent (or its display if its parent is null).
   *
   * @param location the new location for the receiver
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setLocation(Point location) {
    checkWidget();
    if(location == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    setLocation(location.x, location.y);
  }

  /**
   * Sets the receiver's pop up menu to the argument.
   * All controls may optionally have a pop up
   * menu that is displayed when the user requests one for
   * the control. The sequence of key strokes, button presses
   * and/or button releases that are used to request a pop up
   * menu is platform specific.
   *
   * @param menu the new pop up menu
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
       *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setMenu(Menu menu) {
    checkWidget();
    if(menu != null) {
      if(menu.isDisposed()) {
        SWT.error(SWT.ERROR_INVALID_ARGUMENT);
      }
      if((menu.style & SWT.POP_UP) == 0) {
        error(SWT.ERROR_MENU_NOT_POP_UP);
      }
      if(menu.parent != menuShell()) {
        error(SWT.ERROR_INVALID_PARENT);
      }
    }
    this.menu = menu;
  }

  boolean setRadioFocus() {
    return false;
  }

  boolean setRadioSelection(boolean value) {
    return false;
  }

  /**
   * If the argument is <code>false</code>, causes subsequent drawing
   * operations in the receiver to be ignored. No drawing of any kind
   * can occur in the receiver until the flag is set to true.
   * Graphics operations that occurred while the flag was
   * <code>false</code> are lost. When the flag is set to <code>true</code>,
   * the entire widget is marked as needing to be redrawn.
   * <p>
   * Note: This operation is a hint and may not be supported on some
   * platforms or for some widgets.
   * </p>
   *
   * @param redraw the new redraw state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #redraw
   * @see #update
   */
  public void setRedraw(boolean redraw) {
    checkWidget();
    /*
     * This code is intentionally commented.
     *
     * Feature in Windows.  When WM_SETREDRAW is used to turn
     * off drawing in a widget, it clears the WS_VISIBLE bits
     * and then sets them when redraw is turned back on.  This
     * means that WM_SETREDRAW will make a widget unexpectedly
     * visible.
     *
     * There is no fix at this time.
     */
//	if (drawCount == 0) {
//		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//		if ((bits & OS.WS_VISIBLE) == 0) return;
//	}

    // TODO: see to what extent it can be supported
//    if(redraw) {
//      if(--drawCount == 0) {
//        OS.SendMessage(handle, OS.WM_SETREDRAW, 1, 0);
//        if(OS.IsWinCE) {
//          OS.InvalidateRect(handle, null, true);
//        } else {
//          int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE |
//              OS.RDW_ALLCHILDREN;
//          OS.RedrawWindow(handle, null, 0, flags);
//        }
//      }
//    } else {
//      if(drawCount++ == 0) {
//        OS.SendMessage(handle, OS.WM_SETREDRAW, 0, 0);
//      }
//    }
  }

  boolean setSavedFocus() {
    return forceFocus();
  }

  /**
   * Sets the receiver's size to the point specified by the arguments.
   * <p>
   * Note: Attempting to set the width or height of the
   * receiver to a negative number will cause that
   * value to be set to zero instead.
   * </p>
   *
   * @param width the new width for the receiver
   * @param height the new height for the receiver
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSize(int width, int height) {
    checkWidget();
    Container handle = getHandle();
    handle.setSize(width, height);
    // Needed or some contents of containers are not correct
    if(handle instanceof JComponent) {
      ((JComponent)handle).revalidate();
    } else {
      handle.validate();
    }
//    swingComponentResized(null);
    isInitSizeSet = true;
  }

  /**
   * Sets the receiver's size to the point specified by the argument.
   * <p>
   * Note: Attempting to set the width or height of the
   * receiver to a negative number will cause them to be
   * set to zero instead.
   * </p>
   *
   * @param size the new size for the receiver
   * @param height the new height for the receiver
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSize(Point size) {
    checkWidget();
    if(size == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    setSize(size.x, size.y);
  }

  boolean setTabGroupFocus() {
    return setTabItemFocus();
  }

  boolean setTabItemFocus() {
    if(!isShowing()) {
      return false;
    }
    return setFocus();
  }

  /**
   * Sets the receiver's tool tip text to the argument, which
   * may be null indicating that no tool tip text should be shown.
   *
   * @param string the new tool tip text (or null)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setToolTipText(String string) {
    checkWidget();
    Container swingContainer = getSwingContainer();
    toolTipText = string;
    if(swingContainer instanceof JComponent) {
      ((JComponent)swingContainer).setToolTipText(string == null? null: "<html>" + Utils.escapeHTML(string));
    }
  }

  /**
   * Marks the receiver as visible if the argument is <code>true</code>,
   * and marks it invisible otherwise.
   * <p>
   * If one of the receiver's ancestors is not visible or some
   * other condition makes the receiver not visible, marking
   * it visible may not actually cause it to be displayed.
   * </p>
   *
   * @param visible the new visibility state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setVisible(boolean visible) {
    checkWidget();
    Container handle = getHandle();
    handle.setVisible(visible);
    if(visible) {
      /*
       * It is possible (but unlikely), that application
       * code could have disposed the widget in the show
       * event.  If this happens, just return.
       */
      sendEvent(SWT.Show);
      if(isDisposed()) {
        return;
      }
    } else {
      if(isFocusAncestor())
        handle.transferFocus();
    }
    if(!visible) {
      /*
       * It is possible (but unlikely), that application
       * code could have disposed the widget in the show
       * event.  If this happens, just return.
       */
      sendEvent(SWT.Hide);
      if(isDisposed()) {
        return;
      }
    }
  }

  void sort(int[] items) {
    /* Shell Sort from K&R, pg 108 */
    int length = items.length;
    for(int gap = length / 2; gap > 0; gap /= 2) {
      for(int i = gap; i < length; i++) {
        for(int j = i - gap; j >= 0; j -= gap) {
          if(items[j] <= items[j + gap]) {
            int swap = items[j];
            items[j] = items[j + gap];
            items[j + gap] = swap;
          }
        }
      }
    }
  }

  void subclass() {
    int oldProc = windowProc();
    int newProc = getDisplay().windowProc;
    if(oldProc == newProc) {
      return;
    }
    OS.SetWindowLong(handle, OS.GWL_WNDPROC, newProc);
  }

  /**
   * Returns a point which is the result of converting the
   * argument, which is specified in display relative coordinates,
   * to coordinates relative to the receiver.
   * <p>
   * @param x the x coordinate to be translated
   * @param y the y coordinate to be translated
   * @return the translated coordinates
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 2.1
   */
  public Point toControl(int x, int y) {
    checkWidget();
//    POINT pt = new POINT();
//    pt.x = x;
//    pt.y = y;
//    OS.ScreenToClient(handle, pt);
    java.awt.Point pt = new java.awt.Point(x, y);
    SwingUtilities.convertPointFromScreen(pt, swingHandle);
    return new Point(pt.x, pt.y);
  }

  /**
   * Returns a point which is the result of converting the
   * argument, which is specified in display relative coordinates,
   * to coordinates relative to the receiver.
   * <p>
   * @param point the point to be translated (must not be null)
   * @return the translated coordinates
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Point toControl(Point point) {
    checkWidget();
    if(point == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    return toControl(point.x, point.y);
  }

  /**
   * Returns a point which is the result of converting the
   * argument, which is specified in coordinates relative to
   * the receiver, to display relative coordinates.
   * <p>
   * @param x the x coordinate to be translated
   * @param y the y coordinate to be translated
   * @return the translated coordinates
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 2.1
   */
  public Point toDisplay(int x, int y) {
    checkWidget();
//    POINT pt = new POINT();
//    pt.x = x;
//    pt.y = y;
    java.awt.Point pt = new java.awt.Point(x, y);
    Container handle = getHandle();
    if(handle instanceof javax.swing.RootPaneContainer) {
      pt = SwingUtilities.convertPoint(((javax.swing.RootPaneContainer)handle).getContentPane(), pt, handle);
    }
    SwingUtilities.convertPointToScreen(pt, swingHandle);
//    OS.ClientToScreen(handle, pt);
    return new Point(pt.x, pt.y);
  }

  /**
   * Returns a point which is the result of converting the
   * argument, which is specified in coordinates relative to
   * the receiver, to display relative coordinates.
   * <p>
   * @param point the point to be translated (must not be null)
   * @return the translated coordinates
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Point toDisplay(Point point) {
    checkWidget();
    if(point == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    return toDisplay(point.x, point.y);
  }

  boolean translateAccelerator(MSG msg) {
    return menuShell().translateAccelerator(msg);
  }

  boolean translateMnemonic(char key) {
    if(!isVisible() || !isEnabled()) {
      return false;
    }
    Event event = new Event();
    event.doit = mnemonicMatch(key);
    event.detail = SWT.TRAVERSE_MNEMONIC;
    Display display = getDisplay();
    display.lastKey = 0;
    display.lastAscii = key;
    display.lastVirtual = display.lastNull = false;
    if(!setKeyState(event, SWT.Traverse)) {
      return false;
    }
    return traverse(event);
  }

  boolean translateMnemonic(MSG msg) {
    int hwnd = msg.hwnd;
    if(OS.GetKeyState(OS.VK_MENU) >= 0) {
      int code = OS.SendMessage(hwnd, OS.WM_GETDLGCODE, 0, 0);
      if((code & OS.DLGC_WANTALLKEYS) != 0) {
        return false;
      }
      if((code & OS.DLGC_BUTTON) == 0) {
        return false;
      }
    }
    Decorations shell = menuShell();
    if(shell.isVisible() && shell.isEnabled()) {
      char ch = mbcsToWcs((char)msg.wParam);
      return ch != 0 && shell.translateMnemonic(ch);
    }
    return false;
  }

  boolean translateTraversal(MSG msg) {
    int key = msg.wParam;
    if(key == OS.VK_MENU) {
      Shell shell = getShell();
      int hwndShell = shell.handle;
      OS.SendMessage(hwndShell, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
      return false;
    }
    int hwnd = msg.hwnd;
    int detail = SWT.TRAVERSE_NONE;
    boolean doit = true, all = false;
    boolean lastVirtual = false;
    int lastKey = key, lastAscii = 0;
    switch(key) {
      case OS.VK_ESCAPE: {
        all = true;
        lastAscii = 27;
        int code = OS.SendMessage(hwnd, OS.WM_GETDLGCODE, 0, 0);
        if((code & OS.DLGC_WANTALLKEYS) != 0) {
          /*
           * Use DLGC_HASSETSEL to determine that the control
           * is a text widget.  A text widget normally wants
           * all keys except VK_ESCAPE.  If this bit is not
           * set, then assume the control wants all keys,
           * including VK_ESCAPE.
           */
          if((code & OS.DLGC_HASSETSEL) == 0) {
            doit = false;
          }
        }
        detail = SWT.TRAVERSE_ESCAPE;
        break;
      }
      case OS.VK_RETURN: {
        all = true;
        lastAscii = '\r';
        int code = OS.SendMessage(hwnd, OS.WM_GETDLGCODE, 0, 0);
        if((code & OS.DLGC_WANTALLKEYS) != 0) {
          doit = false;
        }
        detail = SWT.TRAVERSE_RETURN;
        break;
      }
      case OS.VK_TAB: {
        /*
         * NOTE: This code causes Shift+Tab and Ctrl+Tab to
         * always attempt traversal which is not correct.
         * The default should be the same as a plain Tab key.
         * This behavior is currently relied on by StyledText.
         *
         * The correct behavior is to give every key to a
         * control that answers DLGC_WANTALLKEYS.
         */
        lastAscii = '\t';
        boolean next = OS.GetKeyState(OS.VK_SHIFT) >= 0;
        int code = OS.SendMessage(hwnd, OS.WM_GETDLGCODE, 0, 0);
        if((code & (OS.DLGC_WANTTAB | OS.DLGC_WANTALLKEYS)) != 0) {
          if(next && OS.GetKeyState(OS.VK_CONTROL) >= 0) {
            doit = false;
          }
        }
        detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
        break;
      }
      case OS.VK_UP:
      case OS.VK_LEFT:
      case OS.VK_DOWN:
      case OS.VK_RIGHT: {
        /*
         * On WinCE SP there is no tab key.  Focus is assigned
         * using only the VK_UP and VK_DOWN keys, not with VK_LEFT
         * or VK_RIGHT.
         */
        if(OS.IsSP) {
          if(key == OS.VK_LEFT || key == OS.VK_RIGHT) {
            return false;
          }
        }
        lastVirtual = true;
        int code = OS.SendMessage(hwnd, OS.WM_GETDLGCODE, 0, 0);
        if((code & (OS.DLGC_WANTARROWS /*| OS.DLGC_WANTALLKEYS*/)) != 0) {
          doit = false;
        }
        boolean next = key == OS.VK_DOWN || key == OS.VK_RIGHT;
        detail = next ? SWT.TRAVERSE_ARROW_NEXT : SWT.TRAVERSE_ARROW_PREVIOUS;
        break;
      }
      case OS.VK_PRIOR:
      case OS.VK_NEXT: {
        all = true;
        lastVirtual = true;
        if(OS.GetKeyState(OS.VK_CONTROL) >= 0) {
          return false;
        }
        /*
         * The fact that this code is commented causes Ctrl+PgUp
         * and Ctrl+PgDn to always attempt traversal which is not
         * correct.  This behavior is relied on by StyledText.
         *
         * The correct behavior is to give every key to a control
         * that answers DLGC_WANTALLKEYS.
         */
//			int code = OS.SendMessage (hwnd, OS., 0, 0);
//			if ((code & OS.DLGC_WANTALLKEYS) != 0) doit = false;
        detail = key == OS.VK_PRIOR ? SWT.TRAVERSE_PAGE_PREVIOUS :
            SWT.TRAVERSE_PAGE_NEXT;
        break;
      }
      default:
        return false;
    }
    Event event = new Event();
    event.doit = doit;
    event.detail = detail;
    Display display = getDisplay();
    display.lastKey = lastKey;
    display.lastAscii = lastAscii;
    display.lastVirtual = lastVirtual;
    display.lastNull = false;
    if(!setKeyState(event, SWT.Traverse)) {
      return false;
    }
    Shell shell = getShell();
    Control control = this;
    do {
      if(control.traverse(event)) {
        int hwndShell = shell.handle;
        OS.SendMessage(hwndShell, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
        return true;
      }
      if(!event.doit && control.hooks(SWT.Traverse)) {
        return false;
      }
      if(control == shell) {
        return false;
      }
      control = control.parent;
    } while(all && control != null);
    return false;
  }

  boolean traverse(Event event) {
    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the traverse
     * event.  If this happens, return true to stop further
     * event processing.
     */
    sendEvent(SWT.Traverse, event);
    if(isDisposed()) {
      return false;
    }
    if(!event.doit) {
      return false;
    }
    switch(event.detail) {
      case SWT.TRAVERSE_NONE:
        return true;
      case SWT.TRAVERSE_ESCAPE:
        return traverseEscape();
      case SWT.TRAVERSE_RETURN:
        return traverseReturn();
      case SWT.TRAVERSE_TAB_NEXT:
        return traverseGroup(true);
      case SWT.TRAVERSE_TAB_PREVIOUS:
        return traverseGroup(false);
      case SWT.TRAVERSE_ARROW_NEXT:
        return traverseItem(true);
      case SWT.TRAVERSE_ARROW_PREVIOUS:
        return traverseItem(false);
      case SWT.TRAVERSE_MNEMONIC:
        return traverseMnemonic(event.character);
      case SWT.TRAVERSE_PAGE_NEXT:
        return traversePage(true);
      case SWT.TRAVERSE_PAGE_PREVIOUS:
        return traversePage(false);
    }
    return false;
  }

  /**
   * Based on the argument, perform one of the expected platform
   * traversal action. The argument should be one of the constants:
   * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
       * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
   * <code>SWT.TRAVERSE_ARROW_NEXT</code> and <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>.
   *
   * @param traversal the type of traversal
   * @return true if the traversal succeeded
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean traverse(int traversal) {
    checkWidget();
    if(!isFocusControl() && !setFocus()) {
      return false;
    }
    Event event = new Event();
    event.doit = true;
    event.detail = traversal;
    return traverse(event);
  }

  boolean traverseEscape() {
    return false;
  }

  boolean traverseGroup(boolean next) {
    Control root = computeTabRoot();
    Control group = computeTabGroup();
    Control[] list = root.computeTabList();
    int length = list.length;
    int index = 0;
    while(index < length) {
      if(list[index] == group) {
        break;
      }
      index++;
    }
    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in focus in
     * or out events.  Ensure that a disposed widget is
     * not accessed.
     */
    if(index == length) {
      return false;
    }
    int start = index, offset = (next) ? 1 : -1;
        while((index = ((index + offset + length) % length)) != start) {
      Control control = list[index];
      if(!control.isDisposed() && control.setTabGroupFocus()) {
        if(!isDisposed() && !isFocusControl()) {
          return true;
        }
      }
    }
    if(group.isDisposed()) {
      return false;
    }
    return group.setTabGroupFocus();
  }

  boolean traverseItem(boolean next) {
    Control[] children = parent._getChildren();
    int length = children.length;
    int index = 0;
    while(index < length) {
      if(children[index] == this) {
        break;
      }
      index++;
    }
    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in focus in
     * or out events.  Ensure that a disposed widget is
     * not accessed.
     */
    int start = index, offset = (next) ? 1 : -1;
        while((index = (index + offset + length) % length) != start) {
      Control child = children[index];
      if(!child.isDisposed() && child.isTabItem()) {
        if(child.setTabItemFocus()) {
          return true;
        }
      }
    }
    return false;
  }

  boolean traverseMnemonic(char key) {
    return mnemonicHit(key);
  }

  boolean traversePage(boolean next) {
    return false;
  }

  boolean traverseReturn() {
    return false;
  }

  void unsubclass() {
    int newProc = windowProc();
    int oldProc = getDisplay().windowProc;
    if(oldProc == newProc) {
      return;
    }
    OS.SetWindowLong(handle, OS.GWL_WNDPROC, newProc);
  }

  /**
   * Forces all outstanding paint requests for the widget
   * to be processed before this method returns.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #redraw
   */
  public void update() {
    checkWidget();
    update(false);
  }

  void update(boolean all) {
//	checkWidget ();
    if(OS.IsWinCE) {
      OS.UpdateWindow(handle);
    } else {
      int flags = OS.RDW_UPDATENOW;
      if(all) {
        flags |= OS.RDW_ALLCHILDREN;
      }
      OS.RedrawWindow(handle, null, 0, flags);
    }
  }

  void updateFont(Font oldFont, Font newFont) {
    Font font = getFont();
    if(font.equals(oldFont)) {
      setFont(newFont);
    }
  }

  int widgetExtStyle() {
    int bits = 0;
    if((style & SWT.BORDER) != 0) {
      bits |= OS.WS_EX_CLIENTEDGE;
    }
    bits |= OS.WS_EX_NOINHERITLAYOUT;
    if((style & SWT.RIGHT_TO_LEFT) != 0) {
      bits |= OS.WS_EX_LAYOUTRTL;
    }
    return bits;
  }

  int widgetStyle() {
    /* Force clipping of siblings by setting WS_CLIPSIBLINGS */
    return OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_CLIPSIBLINGS;

    /*
     * This code is intentionally commented.  When clipping
     * of both siblings and children is not enforced, it is
     * possible for application code to draw outside of the
     * control.
     */
//	int bits = OS.WS_CHILD | OS.WS_VISIBLE;
//	if ((style & SWT.CLIP_SIBLINGS) != 0) bits |= OS.WS_CLIPSIBLINGS;
//	if ((style & SWT.CLIP_CHILDREN) != 0) bits |= OS.WS_CLIPCHILDREN;
//	return bits;
  }

  /**
   * Changes the parent of the widget to be the one provided if
   * the underlying operating system supports this feature.
   * Answers <code>true</code> if the parent is successfully changed.
   *
   * @param parent the new parent for the control.
   * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
   * </ul>
   * @exception SWTError <ul>
   *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
   *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
   *	</ul>
   */
  public boolean setParent(Composite parent) {
    checkWidget();
    if(parent == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(parent.isDisposed()) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    if(OS.SetParent(handle, parent.handle) == 0) {
      return false;
    }
    this.parent = parent;
    return true;
  }

  TCHAR windowClass() {
    throw new IllegalStateException("Not supported");
  }

  int windowProc() {
    throw new IllegalStateException("Not supported");
  }

  int windowProc(int msg, int wParam, int lParam) {
    throw new IllegalStateException("Not supported");
  }

  LRESULT WM_ACTIVATE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_CHAR(int wParam, int lParam) {

    /*
     * Do not report a lead byte as a key pressed.
     */
    Display display = getDisplay();
    if(!OS.IsUnicode && OS.IsDBLocale) {
      byte lead = (byte)(wParam & 0xFF);
      if(OS.IsDBCSLeadByte(lead)) {
        return null;
      }
    }

    /*
     * Use VkKeyScan () to tell us if the character is a control
     * or a numeric key pad character with Num Lock down.  On
     * international keyboards, the control key may be down when
     * the character is not a control character.  In this case
     * use the last key (computed in WM_KEYDOWN) instead of wParam
     * as the keycode because there is not enough information to
     * compute the keycode in WPARAM.
     */
    display.lastAscii = wParam;
    display.lastNull = false;
    if(display.lastKey == 0) {
      display.lastKey = wParam;
      display.lastVirtual = display.isVirtualKey(wParam);
    } else {
      int result = OS.IsWinCE ? 0 : OS.VkKeyScan((short)wParam);
      if(!OS.IsWinCE && (result == -1 || (result >> 8) <= 2)) {
        if(OS.GetKeyState(OS.VK_CONTROL) < 0) {
          display.lastVirtual = display.isVirtualKey(display.lastKey);
        }
      } else {
        display.lastKey = wParam;
        display.lastVirtual = false;
      }
    }
    if(!sendKeyEvent(SWT.KeyDown, OS.WM_CHAR, wParam, lParam)) {
      return LRESULT.ZERO;
    }
    return null;
  }

  LRESULT WM_CLEAR(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_CLOSE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_CONTEXTMENU(int wParam, int lParam) {
    if(wParam != handle) {
      return null;
    }

    /*
     * Feature in Windows.  When the user presses  WM_NCRBUTTONUP,
     * a WM_CONTEXTMENU message is generated.  This happens when
     * the user releases the mouse over a scroll bar.  Normally,
     * window displays the default scrolling menu but applications
     * can process WM_CONTEXTMENU to display a different menu.
     * Typically, an application does not want to supply a special
     * scroll menu.  The fix is to look for a WM_CONTEXTMENU that
     * originated from a mouse event and display the menu when the
     * mouse was released in the client area.
     */
    POINT pt = new POINT();
    pt.x = (short)(lParam & 0xFFFF);
    pt.y = (short)(lParam >> 16);
    if(pt.x != -1 || pt.y != -1) {
      RECT rect = new RECT();
      OS.GetClientRect(handle, rect);
      OS.ScreenToClient(handle, pt);
      if(!OS.PtInRect(rect, pt)) {
        return null;
      }
    }

    /*
     * Because context menus can be shared between controls
     * and the parent of all menus is the shell, the menu may
     * have been destroyed.
     */
    if(menu != null && !menu.isDisposed()) {
//		menu.setLocation (x, y);
      menu.setVisible(true);
      return LRESULT.ZERO;
    }
    return null;
  }

  LRESULT WM_CTLCOLOR(int wParam, int lParam) {
    Display display = getDisplay();
    int hPalette = display.hPalette;
    if(hPalette != 0) {
      OS.SelectPalette(wParam, hPalette, false);
      OS.RealizePalette(wParam);
    }
    Control control = WidgetTable.get(lParam);
    if(control == null) {
      return null;
    }
    return control.wmColorChild(wParam, lParam);
  }

  LRESULT WM_CUT(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_DESTROY(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_ENDSESSION(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_ERASEBKGND(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_GETDLGCODE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_GETFONT(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_GETOBJECT(int wParam, int lParam) {
    if(accessible != null) {
      int result = accessible.internal_WM_GETOBJECT(wParam, lParam);
      if(result != 0) {
        return new LRESULT(result);
      }
    }
    return null;
  }

  LRESULT WM_HOTKEY(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_HELP(int wParam, int lParam) {
    if(OS.IsWinCE) {
      return null;
    }
    HELPINFO lphi = new HELPINFO();
    OS.MoveMemory(lphi, lParam, HELPINFO.sizeof);
    Decorations shell = menuShell();
    if(!shell.isEnabled()) {
      return null;
    }
    if(lphi.iContextType == OS.HELPINFO_MENUITEM) {
      MenuItem item = shell.findMenuItem(lphi.iCtrlId);
      if(item != null && item.isEnabled()) {
        Widget widget = null;
        if(item.hooks(SWT.Help)) {
          widget = item;
        } else {
          Menu menu = item.parent;
          if(menu.hooks(SWT.Help)) {
            widget = menu;
          }
        }
        if(widget != null) {
          int hwndShell = shell.handle;
          OS.SendMessage(hwndShell, OS.WM_CANCELMODE, 0, 0);
          widget.postEvent(SWT.Help);
          return LRESULT.ONE;
        }
      }
      return null;
    }
    if(hooks(SWT.Help)) {
      postEvent(SWT.Help);
      return LRESULT.ONE;
    }
    return null;
  }

  LRESULT WM_HSCROLL(int wParam, int lParam) {
    if(lParam == 0) {
      return null;
    }
    Control control = WidgetTable.get(lParam);
    if(control == null) {
      return null;
    }
    return control.wmScrollChild(wParam, lParam);
  }

  LRESULT WM_IME_CHAR(int wParam, int lParam) {
    Display display = getDisplay();
    display.lastKey = 0;
    display.lastAscii = wParam;
    display.lastVirtual = display.lastNull = false;
    sendKeyEvent(SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam);
    sendKeyEvent(SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
    display.lastKey = display.lastAscii = 0;
    return LRESULT.ZERO;
  }

  LRESULT WM_IME_COMPOSITION(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_INITMENUPOPUP(int wParam, int lParam) {

    /* Ignore WM_INITMENUPOPUP for an accelerator */
    Display display = getDisplay();
    if(display.accelKeyHit) {
      return null;
    }

    /*
     * If the high order word of LPARAM is non-zero,
     * the menu is the system menu and we can ignore
     * WPARAM.  Otherwise, use WPARAM to find the menu.
     */
    Shell shell = getShell();
    Menu oldMenu = shell.activeMenu, newMenu = null;
    if((lParam >> 16) == 0) {
      newMenu = menuShell().findMenu(wParam);
    }
    Menu menu = newMenu;
    while(menu != null && menu != oldMenu) {
      menu = menu.getParentMenu();
    }
    if(menu == null) {
      menu = shell.activeMenu;
      while(menu != null) {
        /*
         * It is possible (but unlikely), that application
         * code could have disposed the widget in the hide
         * event.  If this happens, stop searching up the
         * ancestor list because there is no longer a link
         * to follow.
         */
        menu.sendEvent(SWT.Hide);
        if(menu.isDisposed()) {
          break;
        }
        menu = menu.getParentMenu();
        Menu ancestor = newMenu;
        while(ancestor != null && ancestor != menu) {
          ancestor = ancestor.getParentMenu();
        }
        if(ancestor != null) {
          break;
        }
      }
    }

    /*
     * The shell and the new menu may be disposed because of
     * sending the hide event to the ancestor menus but setting
     * a field to null in a disposed shell is not harmful.
     */
    if(newMenu != null && newMenu.isDisposed()) {
      newMenu = null;
    }
    shell.activeMenu = newMenu;

    /*
     * Send the show event
     */
    if(newMenu != null && newMenu != oldMenu) {
      /*
       * SWT.Selection events are posted to allow stepping
       * in the VA/Java debugger.  SWT.Show events are
       * sent to ensure that application event handler
       * code runs before the menu is displayed.  This
       * means that SWT.Show events would normally occur
       * before SWT.Selection events.  While this is not
       * strictly incorrect, applications often use the
       * SWT.Selection event to update the state of menu
       * items and would like the ordering of events to
       * be the other way around.
       *
       * The fix is to run the deferred events before
       * the menu is shown.  This means that stepping
       * through a selection event that was caused by
       * a popup menu will fail in VA/Java.
       */
      display.runDeferredEvents();
      newMenu.sendEvent(SWT.Show);
      // widget could be disposed at this point
    }
    return null;
  }

  LRESULT WM_KEYDOWN(int wParam, int lParam) {

    /* Ignore repeating modifier keys by testing key down state */
    switch(wParam) {
      case OS.VK_SHIFT:
      case OS.VK_MENU:
      case OS.VK_CONTROL:
      case OS.VK_CAPITAL:
      case OS.VK_NUMLOCK:
      case OS.VK_SCROLL:
        if((lParam & 0x40000000) != 0) {
          return null;
        }
    }

    /* Clear last key and last ascii because a new key has been typed */
    Display display = getDisplay();
    display.lastAscii = display.lastKey = 0;
    display.lastVirtual = display.lastNull = false;

    /*
     * Do not report a lead byte as a key pressed.
     */
    if(!OS.IsUnicode && OS.IsDBLocale) {
      byte lead = (byte)(wParam & 0xFF);
      if(OS.IsDBCSLeadByte(lead)) {
        return null;
      }
    }

    /* Map the virtual key */
    /*
     * Bug on WinCE.  MapVirtualKey() returns incorrect values.
     * The fix is to rely on a key mappings table to determine
     * whether the key event must be sent now or if a WM_CHAR
     * event will follow.
     */
    int mapKey = OS.IsWinCE ? 0 : OS.MapVirtualKey(wParam, 2);

        /*
         * Bug in Windows 95 and NT.  When the user types an accent key such
         * as ^ to get an accented character on a German keyboard, the accent
         * key should be ignored and the next key that the user types is the
         * accented key.  On Windows 95 and NT, a call to ToAscii (), clears the
         * accented state such that the next WM_CHAR loses the accent.  The fix
         * is to detect the accent key stroke (called a dead key) by testing the
         * high bit of the value returned by MapVirtualKey ().  A further problem
         * is that the high bit on Windows NT is bit 32 while the high bit on
         * Windows 95 is bit 16.  They should both be bit 32.
         *
         * NOTE: This code is used to avoid a call to ToAscii ().
         */
    if(OS.IsWinNT) {
      if((mapKey & 0x80000000) != 0) {
        return null;
      }
    } else {
      if((mapKey & 0x8000) != 0) {
        return null;
      }
    }

    /*
     * Bug in Windows.  When the accent key is generated on an international
     * keyboard using Ctrl+Alt or the special key, MapVirtualKey () does not
     * have the high bit set indicating that this is an accent key stroke.
     * The fix is to iterate through all known accent, mapping them back to
     * their corresponding virtual key and key state.  If the virtual key
     * and key state match the current key, then this is an accent that has
     * been generated using an international keyboard and calling ToAscii ()
     * will clear the accent state.
     *
     * NOTE: This code is used to avoid a call to ToAscii ().
     */
    if(!OS.IsWinCE) {
      for(int i = 0; i < ACCENTS.length; i++) {
        int value = OS.VkKeyScan(ACCENTS[i]);
        if((value & 0xFF) == wParam && (value & 0x600) == 0x600) {
          return null;
        }
      }
    }

    /*
     * If we are going to get a WM_CHAR, ensure that last key has
     * the correct character value for the key down and key up
     * events.  It is not sufficient to ignore the WM_KEYDOWN
     * (when we know we are going to get a WM_CHAR) and compute
     * the key in WM_CHAR because there is not enough information
     * by the time we get the WM_CHAR.  For example, when the user
     * types Ctrl+Shift+6 on a US keyboard, we get a WM_CHAR with
     * wParam=30.  When the user types Ctrl+Shift+6 on a German
     * keyboard, we also get a WM_CHAR with wParam=30.  On the US
     * keyboard Shift+6 is ^, on the German keyboard Shift+6 is &.
     * There is no way to map wParam=30 in WM_CHAR to the correct
     * value.  Also, on international keyboards, the control key
     * may be down when the user has not entered a control character.
     */
    display.lastKey = wParam;
    display.lastVirtual = (mapKey == 0);
    if(display.lastVirtual) {
      /*
       * Feature in Windows.  The virtual key VK_DELETE is not
       * treated as both a virtual key and an ASCII key by Windows.
       * Therefore, we will not receive a WM_CHAR for this key.
       * The fix is to treat VK_DELETE as a special case and map
       * the ASCII value explictly (Delete is 0x7F).
       */
      if(display.lastKey == OS.VK_DELETE) {
        display.lastAscii = 0x7F;
        /*
         * It is possible to get a WM_CHAR for a virtual key when
         * Num Lock is on.  If the user types Home while Num Lock
         * is down, a WM_CHAR is issued with WPARM=55 (for the
         * character 7).  If we are going to get a WM_CHAR we need
         * to ensure that the last key has the correct value.  Note
         * that Ctrl+Home does not issue a WM_CHAR when Num Lock is
         * down.
         */
      }
      if(OS.VK_NUMPAD0 <= display.lastKey && display.lastKey <= OS.VK_DIVIDE) {
        if(display.asciiKey(display.lastKey) != 0) {
          return null;
        }
        display.lastAscii = display.numpadKey(display.lastKey);
      }
    } else {
      /*
       * Get the shifted state or convert to lower case if necessary.
       * If the user types Ctrl+A, LastKey should be 'a', not 'A'.  If
       * the user types Ctrl+Shift+A, LastKey should be 'A'.  If the user
       * types Ctrl+Shift+6, the value of LastKey will depend on the
       * international keyboard.
       */
      if(OS.GetKeyState(OS.VK_SHIFT) < 0) {
        display.lastKey = display.shiftedKey(display.lastKey);
        if(display.lastKey == 0) {
          display.lastKey = wParam;
        }
      } else {
        display.lastKey = OS.CharLower((short)mapKey);
      }
      /*
       * Some key combinations map to Windows ASCII keys depending
       * on the keyboard.  For example, Ctrl+Alt+Q maps to @ on a
       * German keyboard.  If the current key combination is special,
       * the correct character is placed in wParam for processing in
       * WM_CHAR.  If this is the case, issue the key down event from
       * inside WM_CHAR.
       */
      int asciiKey = display.asciiKey(wParam);
      if(asciiKey != 0) {
        /*
         * When the user types Ctrl+Space, ToAscii () maps this to
         * Space.  Normally, ToAscii () maps a key to a different
         * key if both a WM_KEYDOWN and a WM_CHAR will be issued.
         * To avoid the extra SWT.KeyDown, look for VK_SPACE and
         * issue the event from WM_CHAR.
         */
        if(asciiKey == OS.VK_SPACE) {
          display.lastVirtual = true;
          return null;
        }
        if(asciiKey != wParam) {
          return null;
        }
      }

      /*
       * If the control key is not down at this point, then
       * the key that was pressed was an accent key.  In that
       * case, do not issue the key down event.
       */
      if(OS.GetKeyState(OS.VK_CONTROL) >= 0) {
        display.lastKey = 0;
        return null;
      }

      /*
       * Virtual keys such as VK_RETURN are both virtual and ASCII keys.
       * Normally, these are marked virtual in WM_CHAR.  Since we will not
       * be getting a WM_CHAR for the key at this point, we need to test
       * LastKey to see if it is virtual.  This happens when the user types
       * Ctrl+Tab.
       */
      display.lastVirtual = display.isVirtualKey(wParam);
      display.lastAscii = display.controlKey(display.lastKey);
      display.lastNull = display.lastAscii == 0 && display.lastKey == '@';
    }
    if(!sendKeyEvent(SWT.KeyDown, OS.WM_KEYDOWN, wParam, lParam)) {
      return LRESULT.ZERO;
    }
    return null;
  }

  LRESULT WM_KEYUP(int wParam, int lParam) {
    Display display = getDisplay();

    /* Check for hardware keys */
    if(OS.IsWinCE) {
      if(OS.VK_APP1 <= wParam && wParam <= OS.VK_APP6) {
        display.lastKey = display.lastAscii = 0;
        display.lastVirtual = display.lastNull = false;
        Event event = new Event();
        event.detail = wParam - OS.VK_APP1 + 1;
        /* Check the bit 30 to get the key state */
        int type = (lParam & 0x40000000) != 0 ? SWT.HardKeyUp : SWT.HardKeyDown;
        if(setInputState(event, type)) {
          sendEvent(type, event);
        }
        return null;
      }
    }

    /*
     * If the key up is not hooked, reset last key
     * and last ascii in case the key down is hooked.
     */
    if(!hooks(SWT.KeyUp) && !display.filters(SWT.KeyUp)) {
      display.lastKey = display.lastAscii = 0;
      display.lastVirtual = display.lastNull = false;
      return null;
    }

    /* Map the virtual key. */
    /*
     * Bug on WinCE.  MapVirtualKey() returns incorrect values.
     * The fix is to rely on a key mappings table to determine
     * whether the key event must be sent now or if a WM_CHAR
     * event will follow.
     */
    int mapKey = OS.IsWinCE ? 0 : OS.MapVirtualKey(wParam, 2);

        /*
         * Bug in Windows 95 and NT.  When the user types an accent key such
         * as ^ to get an accented character on a German keyboard, the accent
         * key should be ignored and the next key that the user types is the
         * accented key.  On Windows 95 and NT, a call to ToAscii (), clears the
         * accented state such that the next WM_CHAR loses the accent.  The fix
         * is to detect the accent key stroke (called a dead key) by testing the
         * high bit of the value returned by MapVirtualKey ().  A further problem
         * is that the high bit on Windows NT is bit 32 while the high bit on
         * Windows 95 is bit 16.  They should both be bit 32.
         *
         * NOTE: This code is used to avoid a call to ToAscii ().
         *
         */
    if(OS.IsWinNT) {
      if((mapKey & 0x80000000) != 0) {
        return null;
      }
    } else {
      if((mapKey & 0x8000) != 0) {
        return null;
      }
    }

    /*
     * Bug in Windows.  When the accent key is generated on an international
     * keyboard using Ctrl+Alt or the special key, MapVirtualKey () does not
     * have the high bit set indicating that this is an accent key stroke.
     * The fix is to iterate through all known accent, mapping them back to
     * their corresponding virtual key and key state.  If the virtual key
     * and key state match the current key, then this is an accent that has
     * been generated using an international keyboard.
     *
     * NOTE: This code is used to avoid a call to ToAscii ().
     */
    if(!OS.IsWinCE) {
      for(int i = 0; i < ACCENTS.length; i++) {
        int value = OS.VkKeyScan(ACCENTS[i]);
        if((value & 0xFF) == wParam && (value & 0x600) == 0x600) {
          display.lastKey = display.lastAscii = 0;
          display.lastVirtual = display.lastNull = false;
          return null;
        }
      }
    }

    display.lastVirtual = (mapKey == 0);
    if(display.lastVirtual) {
      display.lastKey = wParam;
    } else {
      if(display.lastKey == 0) {
        display.lastAscii = 0;
        display.lastNull = false;
        return null;
      }
      display.lastVirtual = display.isVirtualKey(display.lastKey);
    }

    LRESULT result = null;
    if(!sendKeyEvent(SWT.KeyUp, OS.WM_KEYUP, wParam, lParam)) {
      result = LRESULT.ZERO;
    }
    display.lastKey = display.lastAscii = 0;
    display.lastVirtual = display.lastNull = false;
    return result;
  }

  LRESULT WM_KILLFOCUS(int wParam, int lParam) {
    int code = callWindowProc(OS.WM_KILLFOCUS, wParam, lParam);
    Display display = getDisplay();
    Shell shell = getShell();

    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the focus
     * out event.  If this happens keep going to send
     * the deactivate events.
     */
    sendEvent(SWT.FocusOut);
    // widget could be disposed at this point

    /*
     * It is possible that the shell may be
     * disposed at this point.  If this happens
     * don't send the activate and deactivate
     * events.
     */
    if(!shell.isDisposed()) {
      Control control = display.findControl(wParam);
      if(control == null || shell != control.getShell()) {
        shell.setActiveControl(null);
      }
    }

    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the focus
     * or deactivate events.  If this happens, end the
     * processing of the Windows message by returning
     * zero as the result of the window proc.
     */
    if(isDisposed()) {
      return LRESULT.ZERO;
    }
    if(code == 0) {
      return LRESULT.ZERO;
    }
    return new LRESULT(code);
  }

  LRESULT WM_LBUTTONDBLCLK(int wParam, int lParam) {
    /*
     * Feature in Windows. Windows sends the following
     * messages when the user double clicks the mouse:
     *
     *	WM_LBUTTONDOWN		- mouse down
     *	WM_LBUTTONUP		- mouse up
     *	WM_LBUTTONDBLCLK	- double click
     *	WM_LBUTTONUP		- mouse up
     *
     * Applications that expect matching mouse down/up
     * pairs will not see the second mouse down.  The
     * fix is to send a mouse down event.
     */
    sendMouseEvent(SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
    sendMouseEvent(SWT.MouseDoubleClick, 1, OS.WM_LBUTTONDBLCLK, wParam, lParam);
    int result = callWindowProc(OS.WM_LBUTTONDBLCLK, wParam, lParam);
    if(OS.GetCapture() != handle) {
      OS.SetCapture(handle);
    }
    return new LRESULT(result);
  }

  LRESULT WM_LBUTTONDOWN(int wParam, int lParam) {
    boolean dragging = false, mouseDown = true;
    boolean dragDetect = hooks(SWT.DragDetect);
    if(dragDetect) {
      if(!OS.IsWinCE) {
        /*
         * Feature in Windows.  It's possible that the drag
         * operation will not be started while the mouse is
         * down, meaning that the mouse should be captured.
         * This can happen when the user types the ESC key
         * to cancel the drag.  The fix is to query the state
         * of the mouse and capture the mouse accordingly.
         */
        POINT pt = new POINT();
        pt.x = (short)(lParam & 0xFFFF);
        pt.y = (short)(lParam >> 16);
        OS.ClientToScreen(handle, pt);
        dragging = OS.DragDetect(handle, pt);
        mouseDown = OS.GetKeyState(OS.VK_LBUTTON) < 0;
      }
    }
    sendMouseEvent(SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
    int result = callWindowProc(OS.WM_LBUTTONDOWN, wParam, lParam);
    if(OS.IsPPC) {
      if(menu != null && !menu.isDisposed()) {
        int x = (short)(lParam & 0xFFFF);
        int y = (short)(lParam >> 16);
        SHRGINFO shrg = new SHRGINFO();
        shrg.cbSize = SHRGINFO.sizeof;
        shrg.hwndClient = handle;
        shrg.ptDown_x = x;
        shrg.ptDown_y = y;
        shrg.dwFlags = OS.SHRG_RETURNCMD;
        int type = OS.SHRecognizeGesture(shrg);
        if(type == OS.GN_CONTEXTMENU) {
          menu.setVisible(true);
        }
      }
    }
    if(mouseDown) {
      if(OS.GetCapture() != handle) {
        OS.SetCapture(handle);
      }
    }
    if(dragging) {
      postEvent(SWT.DragDetect);
    } else {
      if(dragDetect) {
        /*
         * Feature in Windows.  DragDetect() captures the mouse
         * and tracks its movement until the user releases the
         * left mouse button, presses the ESC key, or moves the
         * mouse outside the drag rectangle.  If the user moves
         * the mouse outside of the drag rectangle, DragDetect()
         * returns true and a drag and drop operation can be
         * started.  When the left mouse button is released or
         * the ESC key is pressed, these events are consumed by
         * DragDetect() so that application code that matches
         * mouse down/up pairs or looks for the ESC key will not
         * function properly.  The fix is to send these events
         * when the drag has not started.
         *
         * NOTE: For now, don't send a fake WM_KEYDOWN/WM_KEYUP
         * events for the ESC key.  This would require computing
         * wParam (the key) and lParam (the repeat count, scan code,
         * extended-key flag, context code, previous key-state flag,
         * and transition-state flag) which is non-trivial.
         */
        if(OS.GetKeyState(OS.VK_ESCAPE) >= 0) {
          OS.SendMessage(handle, OS.WM_LBUTTONUP, wParam, lParam);
        }
      }
    }
    return new LRESULT(result);
  }

  LRESULT WM_LBUTTONUP(int wParam, int lParam) {
    sendMouseEvent(SWT.MouseUp, 1, OS.WM_LBUTTONUP, wParam, lParam);
    int result = callWindowProc(OS.WM_LBUTTONUP, wParam, lParam);
    if((wParam & (OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON)) == 0) {
      if(OS.GetCapture() == handle) {
        OS.ReleaseCapture();
      }
    }
    return new LRESULT(result);
  }

  LRESULT WM_MBUTTONDBLCLK(int wParam, int lParam) {
    /*
     * Feature in Windows. Windows sends the following
     * messages when the user double clicks the mouse:
     *
     *	WM_MBUTTONDOWN		- mouse down
     *	WM_MBUTTONUP		- mouse up
     *	WM_MLBUTTONDBLCLK	- double click
     *	WM_MBUTTONUP		- mouse up
     *
     * Applications that expect matching mouse down/up
     * pairs will not see the second mouse down.  The
     * fix is to send a mouse down event.
     */
    sendMouseEvent(SWT.MouseDown, 2, OS.WM_MBUTTONDOWN, wParam, lParam);
    sendMouseEvent(SWT.MouseDoubleClick, 2, OS.WM_MBUTTONDBLCLK, wParam, lParam);
    int result = callWindowProc(OS.WM_MBUTTONDBLCLK, wParam, lParam);
    if(OS.GetCapture() != handle) {
      OS.SetCapture(handle);
    }
    return new LRESULT(result);
  }

  LRESULT WM_MBUTTONDOWN(int wParam, int lParam) {
    sendMouseEvent(SWT.MouseDown, 2, OS.WM_MBUTTONDOWN, wParam, lParam);
    int result = callWindowProc(OS.WM_MBUTTONDOWN, wParam, lParam);
    if(OS.GetCapture() != handle) {
      OS.SetCapture(handle);
    }
    return new LRESULT(result);
  }

  LRESULT WM_MBUTTONUP(int wParam, int lParam) {
    sendMouseEvent(SWT.MouseUp, 2, OS.WM_MBUTTONUP, wParam, lParam);
    int result = callWindowProc(OS.WM_MBUTTONUP, wParam, lParam);
    if((wParam & (OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON)) == 0) {
      if(OS.GetCapture() == handle) {
        OS.ReleaseCapture();
      }
    }
    return new LRESULT(result);
  }

  LRESULT WM_MEASUREITEM(int wParam, int lParam) {
    MEASUREITEMSTRUCT struct = new MEASUREITEMSTRUCT();
    OS.MoveMemory(struct, lParam, MEASUREITEMSTRUCT.sizeof);
    if(struct.CtlType == OS.ODT_MENU) {
      Decorations shell = menuShell();
      MenuItem item = shell.findMenuItem(struct.itemID);
      if(item == null) {
        return null;
      }
      return null; // Chrriis changed :)
    }
    int hwnd = OS.GetDlgItem(handle, struct.CtlID);
    Control control = WidgetTable.get(hwnd);
    if(control == null) {
      return null;
    }
    return control.wmMeasureChild(wParam, lParam);
  }

  LRESULT WM_MENUCHAR(int wParam, int lParam) {
    /*
     * Feature in Windows.  When the user types Alt+<key>
     * and <key> does not match a mnemonic in the System
     * menu or the menu bar, Windows beeps.  This beep is
     * unexpected and unwanted by applications that look
     * for Alt+<key>.  The fix is to detect the case and
     * stop Windows from beeping by closing the menu.
     */
    int type = wParam >> 16;
    if(type == 0 || type == OS.MF_SYSMENU) {
      Display display = getDisplay();
      display.mnemonicKeyHit = false;
      return new LRESULT(OS.MNC_CLOSE << 16);
    }
    return null;
  }

  LRESULT WM_MENUSELECT(int wParam, int lParam) {
    int code = wParam >> 16;
    Shell shell = getShell();
    if(code == -1 && lParam == 0) {
      Display display = getDisplay();
      display.mnemonicKeyHit = true;
      Menu menu = shell.activeMenu;
      while(menu != null) {
        /*
         * It is possible (but unlikely), that application
         * code could have disposed the widget in the hide
         * event.  If this happens, stop searching up the
         * parent list because there is no longer a link
         * to follow.
         */
        menu.sendEvent(SWT.Hide);
        if(menu.isDisposed()) {
          break;
        }
        menu = menu.getParentMenu();
      }
      /*
       * The shell may be disposed because of sending the hide
       * event to the last active menu menu but setting a field
       * to null in a destroyed widget is not harmful.
       */
      shell.activeMenu = null;
      return null;
    }
    if((code & OS.MF_SYSMENU) != 0) {
      return null;
    }
    if((code & OS.MF_HILITE) != 0) {
      MenuItem item = null;
      Decorations menuShell = menuShell();
      if((code & OS.MF_POPUP) != 0) {
        int index = wParam & 0xFFFF;
        MENUITEMINFO info = new MENUITEMINFO();
        info.cbSize = MENUITEMINFO.sizeof;
        info.fMask = OS.MIIM_SUBMENU;
        if(OS.GetMenuItemInfo(lParam, index, true, info)) {
          Menu newMenu = menuShell.findMenu(info.hSubMenu);
          if(newMenu != null) {
            item = newMenu.cascade;
          }
        }
      } else {
        Menu newMenu = menuShell.findMenu(lParam);
        if(newMenu != null) {
          int id = wParam & 0xFFFF;
          item = menuShell.findMenuItem(id);
        }
        Menu oldMenu = shell.activeMenu;
        if(oldMenu != null) {
          Menu ancestor = oldMenu;
          while(ancestor != null && ancestor != newMenu) {
            ancestor = ancestor.getParentMenu();
          }
          if(ancestor == newMenu) {
            ancestor = oldMenu;
            while(ancestor != newMenu) {
              /*
               * It is possible (but unlikely), that application
               * code could have disposed the widget in the hide
               * event or the item about to be armed.  If this
               * happens, stop searching up the ancestor list
               * because there is no longer a link to follow.
               */
              ancestor.sendEvent(SWT.Hide);
              if(ancestor.isDisposed()) {
                break;
              }
              ancestor = ancestor.getParentMenu();
            }
            /*
             * The shell and/or the item could be disposed when
             * processing hide events from above.  If this happens,
             * ensure that the shell is not accessed and that no
             * arm event is sent to the item.
             */
            if(!shell.isDisposed()) {
              if(newMenu != null && newMenu.isDisposed()) {
                newMenu = null;
              }
              shell.activeMenu = newMenu;
            }
            if(item != null && item.isDisposed()) {
              item = null;
            }
          }
        }
      }
      if(item != null) {
        item.sendEvent(SWT.Arm);
      }
    }
    return null;
  }

  LRESULT WM_MOUSEACTIVATE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_MOUSEHOVER(int wParam, int lParam) {
    sendMouseEvent(SWT.MouseHover, 0, OS.WM_MOUSEHOVER, wParam, lParam);
    return null;
  }

  LRESULT WM_MOUSELEAVE(int wParam, int lParam) {
    int pos = OS.GetMessagePos();
    POINT pt = new POINT();
    pt.x = (short)(pos & 0xFFFF);
    pt.y = (short)(pos >> 16);
    OS.ScreenToClient(handle, pt);
    lParam = pt.x | (pt.y << 16);
    sendMouseEvent(SWT.MouseExit, 0, OS.WM_MOUSELEAVE, wParam, lParam);
    return null;
  }

  LRESULT WM_MOUSEMOVE(int wParam, int lParam) {
    if(!OS.IsWinCE) {
      Display display = getDisplay();
      boolean mouseEnter = hooks(SWT.MouseEnter) ||
          display.filters(SWT.MouseEnter);
      boolean mouseExit = hooks(SWT.MouseExit) || display.filters(SWT.MouseExit);
      boolean mouseHover = hooks(SWT.MouseHover) ||
          display.filters(SWT.MouseHover);
      if(mouseEnter || mouseExit || mouseHover) {
        TRACKMOUSEEVENT lpEventTrack = new TRACKMOUSEEVENT();
        lpEventTrack.cbSize = TRACKMOUSEEVENT.sizeof;
        lpEventTrack.dwFlags = OS.TME_QUERY;
        lpEventTrack.hwndTrack = handle;
        OS.TrackMouseEvent(lpEventTrack);
        if(lpEventTrack.dwFlags == 0) {
          lpEventTrack.dwFlags = OS.TME_LEAVE | OS.TME_HOVER;
          lpEventTrack.hwndTrack = handle;
          OS.TrackMouseEvent(lpEventTrack);
          if(mouseEnter) {
            sendMouseEvent(SWT.MouseEnter, 0, OS.WM_MOUSEMOVE, wParam, lParam);
          }
        } else {
          lpEventTrack.dwFlags = OS.TME_HOVER;
          OS.TrackMouseEvent(lpEventTrack);
        }
      }
    }
    Display display = getDisplay();
    int pos = OS.GetMessagePos();
    if(pos != display.lastMouse) {
      display.lastMouse = pos;
      sendMouseEvent(SWT.MouseMove, 0, OS.WM_MOUSEMOVE, wParam, lParam);
    }
    return null;
  }

  LRESULT WM_MOUSEWHEEL(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_MOVE(int wParam, int lParam) {
    sendEvent(SWT.Move);
    // widget could be disposed at this point
    return null;
  }

  LRESULT WM_NCACTIVATE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_NCCALCSIZE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_NCHITTEST(int wParam, int lParam) {
    if(!OS.IsWindowEnabled(handle)) {
      return null;
    }
    if(!isActive()) {
      return new LRESULT(OS.HTTRANSPARENT);
    }
    return null;
  }

  LRESULT WM_NOTIFY(int wParam, int lParam) {
    NMHDR hdr = new NMHDR();
    OS.MoveMemory(hdr, lParam, NMHDR.sizeof);
    int hwnd = hdr.hwndFrom;
    if(hwnd == 0) {
      return null;
    }
    Control control = WidgetTable.get(hwnd);
    if(control == null) {
      return null;
    }
    return control.wmNotifyChild(wParam, lParam);
  }

  LRESULT WM_PAINT(int wParam, int lParam) {

    /* Exit early - don't draw the background */
    if(!hooks(SWT.Paint) && !filters(SWT.Paint)) {
      return null;
    }

    /* Get the damage */
    int result = 0;
    if(OS.IsWinCE) {
      RECT rect = new RECT();
      OS.GetUpdateRect(handle, rect, false);
      result = callWindowProc(OS.WM_PAINT, wParam, lParam);
      OS.InvalidateRect(handle, rect, false);
    } else {
      int rgn = OS.CreateRectRgn(0, 0, 0, 0);
      OS.GetUpdateRgn(handle, rgn, false);
      result = callWindowProc(OS.WM_PAINT, wParam, lParam);
      OS.InvalidateRgn(handle, rgn, false);
      OS.DeleteObject(rgn);
    }

    /* Create the paint GC */
    PAINTSTRUCT ps = new PAINTSTRUCT();
    GCData data = new GCData();
    data.ps = ps;
    GC gc = GC.win32_new(this, data);

    /* Send the paint event */
    Event event = new Event();
    event.gc = gc;
    event.x = ps.left;
    event.y = ps.top;
    event.width = ps.right - ps.left;
    event.height = ps.bottom - ps.top;
    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the paint
     * event.  If this happens, attempt to give back the
     * paint GC anyways because this is a scarce Windows
     * resource.
     */
    sendEvent(SWT.Paint, event);
    // widget could be disposed at this point

    /* Dispose the paint GC	*/
    event.gc = null;
    gc.dispose();

    if(result == 0) {
      return LRESULT.ZERO;
    }
    return new LRESULT(result);
  }

  LRESULT WM_PALETTECHANGED(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_PASTE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_PRINTCLIENT(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_QUERYENDSESSION(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_QUERYNEWPALETTE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_QUERYOPEN(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_RBUTTONDBLCLK(int wParam, int lParam) {
    /*
     * Feature in Windows. Windows sends the following
     * messages when the user double clicks the mouse:
     *
     *	WM_RBUTTONDOWN		- mouse down
     *	WM_RBUTTONUP		- mouse up
     *	WM_RBUTTONDBLCLK	- double click
     *	WM_LBUTTONUP		- mouse up
     *
     * Applications that expect matching mouse down/up
     * pairs will not see the second mouse down.  The
     * fix is to send a mouse down event.
     */
    sendMouseEvent(SWT.MouseDown, 3, OS.WM_RBUTTONDOWN, wParam, lParam);
    sendMouseEvent(SWT.MouseDoubleClick, 3, OS.WM_RBUTTONDBLCLK, wParam, lParam);
    int result = callWindowProc(OS.WM_RBUTTONDBLCLK, wParam, lParam);
    if(OS.GetCapture() != handle) {
      OS.SetCapture(handle);
    }
    return new LRESULT(result);
  }

  LRESULT WM_RBUTTONDOWN(int wParam, int lParam) {
    sendMouseEvent(SWT.MouseDown, 3, OS.WM_RBUTTONDOWN, wParam, lParam);
    int result = callWindowProc(OS.WM_RBUTTONDOWN, wParam, lParam);
    if(OS.GetCapture() != handle) {
      OS.SetCapture(handle);
    }
    return new LRESULT(result);
  }

  LRESULT WM_RBUTTONUP(int wParam, int lParam) {
    sendMouseEvent(SWT.MouseUp, 3, OS.WM_RBUTTONUP, wParam, lParam);
    int result = callWindowProc(OS.WM_RBUTTONUP, wParam, lParam);
    if((wParam & (OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON)) == 0) {
      if(OS.GetCapture() == handle) {
        OS.ReleaseCapture();
      }
    }
    return new LRESULT(result);
  }

  LRESULT WM_SETCURSOR(int wParam, int lParam) {
    int hitTest = lParam & 0xFFFF;
    if(hitTest == OS.HTCLIENT) {
      Control control = WidgetTable.get(wParam);
      if(control == null) {
        return null;
      }
      int hCursor = control.findCursor();
      if(hCursor != 0) {
        OS.SetCursor(hCursor);
        return LRESULT.ONE;
      }
    }
    return null;
  }

  LRESULT WM_SETFOCUS(int wParam, int lParam) {
    int code = callWindowProc(OS.WM_SETFOCUS, wParam, lParam);
    Shell shell = getShell();

    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the focus
     * in event.  If this happens keep going to send
     * the activate events.
     */
    sendEvent(SWT.FocusIn);
    // widget could be disposed at this point

    /*
     * It is possible that the shell may be
     * disposed at this point.  If this happens
     * don't send the activate and deactivate
     * events.
     */
    if(!shell.isDisposed()) {
      shell.setActiveControl(this);
    }

    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the focus
     * or activate events.  If this happens, end the
     * processing of the Windows message by returning
     * zero as the result of the window proc.
     */
    if(isDisposed()) {
      return LRESULT.ZERO;
    }
    if(code == 0) {
      return LRESULT.ZERO;
    }
    return new LRESULT(code);
  }

  LRESULT WM_SETTINGCHANGE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_SETFONT(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_SETREDRAW(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_SHOWWINDOW(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_SIZE(int wParam, int lParam) {
    sendEvent(SWT.Resize);
    // widget could be disposed at this point
    return null;
  }

  LRESULT WM_SYSCHAR(int wParam, int lParam) {
    Display display = getDisplay();

    /* Set last key and last ascii because a new key has been typed */
    display.lastAscii = display.lastKey = wParam;
    display.lastVirtual = display.isVirtualKey(wParam);
    display.lastNull = false;

    /* Do not issue a key down if a menu bar mnemonic was invoked */
    if(!hooks(SWT.KeyDown) && !display.filters(SWT.KeyDown)) {
      return null;
    }
    display.mnemonicKeyHit = true;
    int result = callWindowProc(OS.WM_SYSCHAR, wParam, lParam);
    if(!display.mnemonicKeyHit) {
      sendKeyEvent(SWT.KeyDown, OS.WM_SYSCHAR, wParam, lParam);
    }
    display.mnemonicKeyHit = false;
    return new LRESULT(result);
  }

  LRESULT WM_SYSCOLORCHANGE(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_SYSKEYDOWN(int wParam, int lParam) {
    /*
     * Feature in Windows.  WM_SYSKEYDOWN is sent when
     * the user presses ALT-<aKey> or F10 without the ALT key.
     * In order to issue events for F10 (without the ALT key)
     * but ignore all other key presses without the ALT key,
     * make F10 a special case.
     */
    if(wParam != OS.VK_F10) {
      /* Make sure WM_SYSKEYDOWN was sent by ALT-<aKey>. */
      if((lParam & 0x20000000) == 0) {
        return null;
      }
    }

    /* Ignore repeating modifier keys by testing key down state */
    switch(wParam) {
      case OS.VK_SHIFT:
      case OS.VK_MENU:
      case OS.VK_CONTROL:
      case OS.VK_CAPITAL:
      case OS.VK_NUMLOCK:
      case OS.VK_SCROLL:
        if((lParam & 0x40000000) != 0) {
          return null;
        }
    }

    /* Clear last key and last ascii because a new key has been typed */
    Display display = getDisplay();
    display.lastAscii = display.lastKey = 0;
    display.lastVirtual = display.lastNull = false;

    /* If are going to get a WM_SYSCHAR, ignore this message. */
    /*
     * Bug on WinCE.  MapVirtualKey() returns incorrect values.
     * The fix is to rely on a key mappings table to determine
     * whether the key event must be sent now or if a WM_SYSCHAR
     * event will follow.
     */
    if(!OS.IsWinCE) {
      if(OS.MapVirtualKey(wParam, 2) != 0) {
        /*
         * Feature in Windows.  MapVirtualKey() indicates that
         * a WM_SYSCHAR message will occur for Alt+Enter but
         * this message never happens.  The fix is to issue the
         * event from WM_SYSKEYDOWN and map VK_RETURN to '\r'.
         */
        if(wParam != OS.VK_RETURN) {
          return null;
        }
        display.lastAscii = '\r';
      }
    }
    display.lastKey = wParam;
    display.lastVirtual = true;

    /*
     * Feature in Windows.  The virtual key VK_DELETE is not
     * treated as both a virtual key and an ASCII key by Windows.
     * Therefore, we will not receive a WM_CHAR for this key.
     * The fix is to treat VK_DELETE as a special case and map
     * the ASCII value explictly (Delete is 0x7F).
     */
    if(display.lastKey == OS.VK_DELETE) {
      display.lastAscii = 0x7F;

      /*
       * It is possible to get a WM_CHAR for a virtual key when
       * Num Lock is on.  If the user types Home while Num Lock
       * is down, a WM_CHAR is issued with WPARM=55 (for the
       * character 7).  If we are going to get a WM_CHAR we need
       * to ensure that the last key has the correct value.  Note
       * that Ctrl+Home does not issue a WM_CHAR when Num Lock is
       * down.
       */
    }
    if(OS.VK_NUMPAD0 <= display.lastKey && display.lastKey <= OS.VK_DIVIDE) {
      if(display.asciiKey(display.lastKey) != 0) {
        return null;
      }
    }

    if(!sendKeyEvent(SWT.KeyDown, OS.WM_SYSKEYDOWN, wParam, lParam)) {
      return LRESULT.ZERO;
    }
    return null;
  }

  LRESULT WM_SYSKEYUP(int wParam, int lParam) {
    return WM_KEYUP(wParam, lParam);
  }

  LRESULT WM_TIMER(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_UNDO(int wParam, int lParam) {
    return null;
  }

  LRESULT WM_VSCROLL(int wParam, int lParam) {
    if(lParam == 0) {
      return null;
    }
    Control control = WidgetTable.get(lParam);
    if(control == null) {
      return null;
    }
    return control.wmScrollChild(wParam, lParam);
  }

  LRESULT WM_WINDOWPOSCHANGING(int wParam, int lParam) {
    return null;
  }

  LRESULT wmColorChild(int wParam, int lParam) {
    if(background == -1 && foreground == -1) {
      return null;
    }
    int forePixel = foreground, backPixel = background;
    if(forePixel == -1) {
      forePixel = defaultForeground();
    }
    if(backPixel == -1) {
      backPixel = defaultBackground();
    }
    OS.SetTextColor(wParam, forePixel);
    OS.SetBkColor(wParam, backPixel);
    return new LRESULT(findBrush(backPixel));
  }

  LRESULT wmCommandChild(int wParam, int lParam) {
    return null;
  }

  LRESULT wmDrawChild(int wParam, int lParam) {
    return null;
  }

  LRESULT wmMeasureChild(int wParam, int lParam) {
    return null;
  }

  LRESULT wmNotifyChild(int wParam, int lParam) {
    return null;
  }

  LRESULT wmScrollChild(int wParam, int lParam) {
    return null;
  }

}
