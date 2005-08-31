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

import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;

import org.eclipse.swt.internal.swing.ScrollPane;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are controls which are capable
 * of containing other controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE, NO_RADIO_GROUP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: The <code>NO_BACKGROUND</code>, <code>NO_FOCUS</code>, <code>NO_MERGE_PAINTS</code>,
 * and <code>NO_REDRAW_RESIZE</code> styles are intended for use with <code>Canvas</code>.
 * They can be used with <code>Composite</code> if you are drawing your own, but their
 * behavior is undefined if they are used with subclasses of <code>Composite</code> other
 * than <code>Canvas</code>.
 * </p><p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are constructed from aggregates
 * of other controls.
 * </p>
 *
 * @see Canvas
 */

public class Composite extends Scrollable {
  Layout layout;
  int font;
  WINDOWPOS[] lpwp;
  Control[] tabList;

  /**
   * Prevents uninitialized instances from being created outside the package.
   */
  Composite() {
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
   * @param parent a widget which will be the parent of the new instance (cannot be null)
   * @param style the style of widget to construct
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
   * </ul>
   *
   * @see SWT#NO_BACKGROUND
   * @see SWT#NO_FOCUS
   * @see SWT#NO_MERGE_PAINTS
   * @see SWT#NO_REDRAW_RESIZE
   * @see SWT#NO_RADIO_GROUP
   * @see Widget#getStyle
   */
  public Composite(Composite parent, int style) {
    super(parent, style);
  }

  Control[] _getChildren() {
    int count = 0;
    int hwndChild = OS.GetWindow(handle, OS.GW_CHILD);
    if(hwndChild == 0) {
      return new Control[0];
    } while(hwndChild != 0) {
      count++;
      hwndChild = OS.GetWindow(hwndChild, OS.GW_HWNDNEXT);
    }
    Control[] children = new Control[count];
    int index = 0;
    hwndChild = OS.GetWindow(handle, OS.GW_CHILD);
    while(hwndChild != 0) {
      Control control = WidgetTable.get(hwndChild);
      if(control != null && control != this) {
        children[index++] = control;
      }
      hwndChild = OS.GetWindow(hwndChild, OS.GW_HWNDNEXT);
    }
    if(count == index) {
      return children;
    }
    Control[] newChildren = new Control[index];
    System.arraycopy(children, 0, newChildren, 0, index);
    return newChildren;
  }

  Control[] _getTabList() {
    if(tabList == null) {
      return tabList;
    }
    int count = 0;
    for(int i = 0; i < tabList.length; i++) {
      if(!tabList[i].isDisposed()) {
        count++;
      }
    }
    if(count == tabList.length) {
      return tabList;
    }
    Control[] newList = new Control[count];
    int index = 0;
    for(int i = 0; i < tabList.length; i++) {
      if(!tabList[i].isDisposed()) {
        newList[index++] = tabList[i];
      }
    }
    tabList = newList;
    return tabList;
  }

  protected void checkSubclass() {
    /* Do nothing - Subclassing is allowed */
  }

  Control[] computeTabList() {
    Control result[] = super.computeTabList();
    if(result.length == 0) {
      return result;
    }
    Control[] list = tabList != null ? _getTabList() : _getChildren();
    for(int i = 0; i < list.length; i++) {
      Control child = list[i];
      Control[] childList = child.computeTabList();
      if(childList.length != 0) {
        Control[] newResult = new Control[result.length + childList.length];
        System.arraycopy(result, 0, newResult, 0, result.length);
        System.arraycopy(childList, 0, newResult, result.length,
                         childList.length);
        result = newResult;
      }
    }
    return result;
  }
  
  ScrollPane scrollPane = null;

  JScrollBar getHorizontalScrollBar() {
    return scrollPane.getHorizontalScrollBar();
  }

  JScrollBar getVerticalScrollBar() {
    return scrollPane.getVerticalScrollBar();
  }

  Container content;
  
  Container getNewHandle() {
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      int direction = (style & SWT.H_SCROLL) != 0? ScrollPane.H_SCROLL: 0;
      direction |= (style & SWT.V_SCROLL) != 0? ScrollPane.V_SCROLL: 0;
      scrollPane = new ScrollPane(direction);
      content = scrollPane.getViewport();
      setSwingContainer(content);
      if((style & SWT.BORDER) != 0) {
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
      }
      return scrollPane;
    } else {
      content = new JPanel(new BorderLayout()) {
        public void paint(Graphics g) {
          super.paint(g);
          if(hooks(SWT.Paint)) {
            Event event = new Event();
            event.gc = GC.swing_new(Composite.this);
            Composite.this.postEvent(SWT.Paint, event);
          }
        }
        public java.awt.Dimension getPreferredSize() {
          int x1 = 0;
          int y1 = 0;
          int x2 = 0;
          int y2 = 0;
          for(int i=0; i<getComponentCount(); i++) {
            java.awt.Rectangle b = getComponent(i).getBounds();
            x1 = Math.min(x1, b.x);
            y1 = Math.min(y1, b.y);
            x2 = Math.max(x2, b.width + b.x);
            y2 = Math.max(y2, b.height + b.y);
          }
          java.awt.Insets i = getInsets();
          return new java.awt.Dimension(x2 - x1 + i.left + i.right, y2 - y1 + i.top + i.bottom);
        }
      };
      setSwingContainer(content);
      if((style & SWT.BORDER) != 0) {
        ((JPanel)content).setBorder(BorderFactory.createEtchedBorder());
      }
      return content;
    }
  }

  java.awt.Dimension computePreferredSize(Container c) {
    java.awt.Component[] components = c.getComponents();
    if(components.length == 0) {
      return c.getSize();
    }
    java.awt.Rectangle bounds = components[0].getBounds();
    int x1 = bounds.x;
    int y1 = bounds.y;
    int x2 = bounds.x + bounds.width;
    int y2 = bounds.y + bounds.height;
    for(int i=1; i<components.length; i++) {
      bounds = components[i].getBounds();
      x1 = Math.min(x1, bounds.x);
      y1 = Math.min(y1, bounds.y);
      x2 = Math.max(x2, bounds.x + bounds.width);
      y2 = Math.max(y2, bounds.y + bounds.height);
    }
    java.awt.Insets insets = c.getInsets();
    return new java.awt.Dimension(insets.left + x2 - x1 + insets.right, insets.top + y2 - y1 + insets.bottom);
  }

  public Point computeSize(int wHint, int hHint, boolean changed) {
    checkWidget();
    Point size;
    if(layout != null) {
      if(wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
        size = layout.computeSize(this, wHint, hHint, changed);
      } else {
        size = new Point(wHint, hHint);
      }
    } else {
      size = minimumSize();
    }
    Container handle = getHandle();
    if(handle.getLayout() instanceof java.awt.FlowLayout) {
      // new method to consider hints: this seems to work with flow layouts only
      java.awt.Dimension oldSize = handle.getSize();
      int width = size.x != 0? size.x: oldSize.width;
      if(wHint != SWT.DEFAULT) {
        width = wHint;
      }
      if(width == 0) {
        width = handle.getPreferredSize().width;
      }
      int height = size.y != 0? size.y: oldSize.height;
      if(hHint != SWT.DEFAULT) {
        height = hHint;
      }
      if(height == 0) {
        height = handle.getPreferredSize().height;
      }
      handle.setSize(new java.awt.Dimension(width, height));
      handle.validate();
      java.awt.Dimension newSize = computePreferredSize(handle);
      size.x = newSize.width;
      size.y = newSize.height;
      handle.setSize(oldSize);
      handle.validate();    
    } else {
      if(size.x == 0) {
        size.x = handle.getPreferredSize().width;
  //      size.x = DEFAULT_WIDTH;
      }
      if(size.y == 0) {
        size.y = handle.getPreferredSize().height;
  //      size.y = DEFAULT_HEIGHT;
      }
      if(wHint != SWT.DEFAULT) {
        size.x = wHint;
      }
      if(hHint != SWT.DEFAULT) {
        size.y = hHint;
      }
    }
    Rectangle trim = computeTrim(0, 0, size.x, size.y);
    return new Point(trim.width, trim.height);
  }

  void createHandle() {
    super.createHandle();
    state |= CANVAS;
  }

  /**
   * Returns an array containing the receiver's children.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its list of children, so modifying the array will
   * not affect the receiver.
   * </p>
   *
   * @return an array of children
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Control[] getChildren() {
    checkWidget();
    Container parent = getContentPane();
    Control[] children = new Control[getChildrenCount()];
    int offset = 0;
    for(int i=0; i<children.length; i++) {
      Control control = WidgetTable.get((Container)parent.getComponent(i + offset));
      if(control != null) {
        children[i] = control;
      } else {
        i--;
        offset++;
      }
    }
    return children;
//    return _getChildren();
  }

  int getChildrenCount() {
    int count = 0;
    Container parent = getContentPane();
    for(int i=0; i<parent.getComponentCount(); i++) {
      if(WidgetTable.get((Container)parent.getComponent(i)) != null)
        count++;
    }
    return count;
  }

  /**
   * Returns layout which is associated with the receiver, or
   * null if one has not been set.
   *
   * @return the receiver's layout or null
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Layout getLayout() {
    checkWidget();
    return layout;
  }

  /**
   * Gets the last specified tabbing order for the control.
   *
   * @return tabList the ordered list of controls representing the tab order
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #setTabList
   */
  public Control[] getTabList() {
    checkWidget();
    Control[] tabList = _getTabList();
    if(tabList == null) {
      int count = 0;
      Control[] list = _getChildren();
      for(int i = 0; i < list.length; i++) {
        if(list[i].isTabGroup()) {
          count++;
        }
      }
      tabList = new Control[count];
      int index = 0;
      for(int i = 0; i < list.length; i++) {
        if(list[i].isTabGroup()) {
          tabList[index++] = list[i];
        }
      }
    }
    return tabList;
  }

  boolean hooksKeys() {
    return hooks(SWT.KeyDown) || hooks(SWT.KeyUp);
  }

  /**
   * If the receiver has a layout, asks the layout to <em>lay out</em>
   * (that is, set the size and location of) the receiver's children.
   * If the receiver does not have a layout, do nothing.
   * <p>
   * This is equivalent to calling <code>layout(true)</code>.
   * </p>
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void layout() {
    checkWidget();
    layout(true);
  }

  /**
   * If the receiver has a layout, asks the layout to <em>lay out</em>
   * (that is, set the size and location of) the receiver's children.
   * If the the argument is <code>true</code> the layout must not rely
   * on any cached information it is keeping about the children. If it
   * is <code>false</code> the layout may (potentially) simplify the
   * work it is doing by assuming that the state of the none of the
   * receiver's children has changed since the last layout.
   * If the receiver does not have a layout, do nothing.
   *
   * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void layout(boolean changed) {
    checkWidget();
    if(layout == null) {
      return;
    }
//    setResizeChildren(false);
    layout.layout(this, changed);
//    setResizeChildren(true);
  }

  Point minimumSize() {
    java.awt.Dimension size = getHandle().getPreferredSize();
    Control[] children = getChildren();
    int width = 0, height = 0;
    for(int i = 0; i < children.length; i++) {
      Rectangle rect = children[i].getBounds();
      width = Math.max(width, rect.x + rect.width);
      height = Math.max(height, rect.y + rect.height);
    }
    return new Point(Math.max(size.width, width), Math.max(size.height, height));
  }

  void releaseChildren() {
    Control[] children = getChildren();
    for(int i = 0; i < children.length; i++) {
      Control child = children[i];
      if(!child.isDisposed()) {
        child.releaseResources();
      }
    }
  }

  void resizeChildren() {
    if(lpwp == null) {
      return;
    }
    do {
      WINDOWPOS[] currentLpwp = lpwp;
      lpwp = null;
      if(!resizeChildren(true, currentLpwp)) {
        resizeChildren(false, currentLpwp);
      }
    } while(lpwp != null);
  }

  boolean resizeChildren(boolean defer, WINDOWPOS[] pwp) {
    if(pwp == null) {
      return true;
    }
    int hdwp = 0;
    if(defer) {
      hdwp = OS.BeginDeferWindowPos(pwp.length);
      if(hdwp == 0) {
        return false;
      }
    }
    for(int i = 0; i < pwp.length; i++) {
      WINDOWPOS wp = pwp[i];
      if(wp != null) {
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
        if(defer) {
          hdwp = OS.DeferWindowPos(hdwp, wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy,
                                   wp.flags);
          if(hdwp == 0) {
            return false;
          }
        } else {
          OS.SetWindowPos(wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
        }
      }
    }
    if(defer) {
      return OS.EndDeferWindowPos(hdwp);
    }
    return true;
  }

  void releaseWidget() {
    releaseChildren();
    super.releaseWidget();
    layout = null;
    tabList = null;
    lpwp = null;
  }

  public boolean setFocus() {
    checkWidget();
    if((style & SWT.NO_FOCUS) != 0) {
      return false;
    }
    Control[] children = _getChildren();
    for(int i = 0; i < children.length; i++) {
      Control child = children[i];
      if(child.setRadioFocus()) {
        return true;
      }
    }
    for(int i = 0; i < children.length; i++) {
      Control child = children[i];
      if(child.setFocus()) {
        return true;
      }
    }
    return super.setFocus();
  }

  /**
   * Sets the layout which is associated with the receiver to be
   * the argument which may be null.
   *
   * @param layout the receiver's new layout or null
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setLayout(Layout layout) {
    checkWidget();
    this.layout = layout;
  }

  /**
   * Sets the tabbing order for the specified controls to
   * match the order that they occur in the argument list.
   *
       * @param tabList the ordered list of controls representing the tab order or null
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if a widget in the tabList is null or has been disposed</li>
   *    <li>ERROR_INVALID_PARENT - if widget in the tabList is not in the same widget tree</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setTabList(Control[] tabList) {
    checkWidget();
    if(tabList != null) {
      for(int i = 0; i < tabList.length; i++) {
        Control control = tabList[i];
        if(control == null) {
          error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if(control.isDisposed()) {
          error(SWT.ERROR_INVALID_ARGUMENT);
          /*
           * This code is intentionally commented.
           * Tab lists are currently only supported
           * for the direct children of a composite.
           */
//			Shell shell = control.getShell ();
//			while (control != shell && control != this) {
//				control = control.parent;
//			}
//			if (control != this) error (SWT.ERROR_INVALID_PARENT);
        }
        if(control.parent != this) {
          error(SWT.ERROR_INVALID_PARENT);
        }
      }
      Control[] newList = new Control[tabList.length];
      System.arraycopy(tabList, 0, newList, 0, tabList.length);
      tabList = newList;
    }
    this.tabList = tabList;
  }

  void setResizeChildren(boolean resize) {
    if(resize) {
      resizeChildren();
    } else {
      int count = getChildrenCount();
      if(count > 1 && lpwp == null) {
        lpwp = new WINDOWPOS[count];
      }
    }
  }

  boolean setTabGroupFocus() {
    if(isTabItem()) {
      return setTabItemFocus();
    }
    if((style & SWT.NO_FOCUS) == 0) {
      boolean takeFocus = true;
      if((state & CANVAS) != 0) {
        takeFocus = hooksKeys();
      }
      if(takeFocus && setTabItemFocus()) {
        return true;
      }
    }
    Control[] children = _getChildren();
    for(int i = 0; i < children.length; i++) {
      Control child = children[i];
      if(child.isTabItem() && child.setRadioFocus()) {
        return true;
      }
    }
    for(int i = 0; i < children.length; i++) {
      Control child = children[i];
      if(child.isTabItem() && child.setTabItemFocus()) {
        return true;
      }
    }
    return false;
  }

  boolean setTabItemFocus() {
    if((style & SWT.NO_FOCUS) == 0) {
      boolean takeFocus = true;
      if((state & CANVAS) != 0) {
        takeFocus = hooksKeys();
      }
      if(takeFocus) {
        if(!isShowing()) {
          return false;
        }
        if(forceFocus()) {
          return true;
        }
      }
    }
    return super.setTabItemFocus();
  }

  String toolTipText(NMTTDISPINFO hdr) {
    if((hdr.uFlags & OS.TTF_IDISHWND) == 0) {
      return null;
    }
    int hwnd = hdr.idFrom;
    if(hwnd == 0) {
      return null;
    }
    Control control = WidgetTable.get(hwnd);
    if(control == null) {
      return null;
    }
    return control.toolTipText;
  }

  boolean translateMnemonic(char key) {
    if(super.translateMnemonic(key)) {
      return true;
    }
    Control[] children = _getChildren();
    for(int i = 0; i < children.length; i++) {
      Control child = children[i];
      if(child.translateMnemonic(key)) {
        return true;
      }
    }
    return false;
  }

  void updateFont(Font oldFont, Font newFont) {
    Control[] children = _getChildren();
    for(int i = 0; i < children.length; i++) {
      Control control = children[i];
      if(!control.isDisposed()) {
        control.updateFont(oldFont, newFont);
      }
    }
    super.updateFont(oldFont, newFont);
    layout(true);
  }

  int widgetStyle() {
    /* Force clipping of children by setting WS_CLIPCHILDREN */
    return super.widgetStyle() | OS.WS_CLIPCHILDREN;
  }

  LRESULT WM_ERASEBKGND(int wParam, int lParam) {
    LRESULT result = super.WM_ERASEBKGND(wParam, lParam);
    if(result != null) {
      return result;
    }
    if((state & CANVAS) != 0) {
      if((style & SWT.NO_BACKGROUND) != 0) {
        return LRESULT.ONE;
      }
    }
    return result;
  }

  LRESULT WM_GETDLGCODE(int wParam, int lParam) {
    LRESULT result = super.WM_GETDLGCODE(wParam, lParam);
    if(result != null) {
      return result;
    }
    if((state & CANVAS) != 0) {
      if((style & SWT.NO_FOCUS) != 0) {
        return new LRESULT(OS.DLGC_STATIC);
      }
      if(hooksKeys()) {
        int flags = OS.DLGC_WANTALLKEYS | OS.DLGC_WANTARROWS | OS.DLGC_WANTTAB;
        return new LRESULT(flags);
      }
      int count = getChildrenCount();
      if(count != 0) {
        return new LRESULT(OS.DLGC_STATIC);
      }
    }
    return result;
  }

  LRESULT WM_GETFONT(int wParam, int lParam) {
    LRESULT result = super.WM_GETFONT(wParam, lParam);
    if(result != null) {
      return result;
    }
    int code = callWindowProc(OS.WM_GETFONT, wParam, lParam);
    if(code != 0) {
      return new LRESULT(code);
    }
    if(font == 0) {
      font = defaultFont();
    }
    return new LRESULT(font);
  }

  LRESULT WM_LBUTTONDOWN(int wParam, int lParam) {
    LRESULT result = super.WM_LBUTTONDOWN(wParam, lParam);

    /* Set focus for a canvas with no children */
    if((state & CANVAS) != 0) {
      if((style & SWT.NO_FOCUS) != 0) {
        return result;
      }
      if(OS.GetWindow(handle, OS.GW_CHILD) == 0) {
        setFocus();
      }
    }
    return result;
  }

  LRESULT WM_NOTIFY(int wParam, int lParam) {
    if(!OS.IsWinCE) {
      NMHDR hdr = new NMHDR();
      OS.MoveMemory(hdr, lParam, NMHDR.sizeof);
      switch(hdr.code) {
        /*
         * Feature in Windows.  When the tool tip control is
         * created, the parent of the tool tip is the shell.
         * If SetParent () is used to reparent the tool bar
         * into a new shell, the tool tip is not reparented
         * and pops up underneath the new shell.  The fix is
         * to make sure the tool tip is a topmost window.
         */
        case OS.TTN_SHOW:
        case OS.TTN_POP: {
          /*
           * Bug in Windows 98 and NT.  Setting the tool tip to be the
           * top most window using HWND_TOPMOST can result in a parent
           * dialog shell being moved behind its parent if the dialog
           * has a sibling that is currently on top.  The fix is to lock
           * the z-order of the active window.
           */
          Display display = getDisplay();
          display.lockActiveWindow = true;
          int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOSIZE;
          int hwndInsertAfter = hdr.code == OS.TTN_SHOW ? OS.HWND_TOPMOST :
              OS.HWND_NOTOPMOST;
          OS.SetWindowPos(hdr.hwndFrom, hwndInsertAfter, 0, 0, 0, 0, flags);
          display.lockActiveWindow = false;
          break;
        }
//        /*
//         * Bug in Windows 98.  For some reason, the tool bar control
//         * sends both TTN_GETDISPINFOW and TTN_GETDISPINFOA to get
//         * the tool tip text and the tab folder control sends only
//         * TTN_GETDISPINFOW.  The fix is to handle only TTN_GETDISPINFOW,
//         * even though it should never be sent on Windows 98.
//         *
//         * NOTE:  Because the size of NMTTDISPINFO differs between
//         * Windows 98 and NT, guard against the case where the wrong
//         * kind of message occurs by inlining the memory moves and
//         * the UNICODE conversion code.
//         */
//        case OS.TTN_GETDISPINFOA:
//        case OS.TTN_GETDISPINFOW: {
//          NMTTDISPINFO lpnmtdi = new NMTTDISPINFO();
//          if(hdr.code == OS.TTN_GETDISPINFOA) {
//            OS.MoveMemoryA(lpnmtdi, lParam, NMTTDISPINFO.sizeofA);
//          } else {
//            OS.MoveMemoryW(lpnmtdi, lParam, NMTTDISPINFO.sizeofW);
//          }
//          String string = toolTipText(lpnmtdi);
//          if(string != null) {
//            Shell shell = getShell();
//            string = Display.withCrLf(string);
//            int length = string.length();
//            char[] chars = new char[length + 1];
//            string.getChars(0, length, chars, 0);
//            if(hdr.code == OS.TTN_GETDISPINFOA) {
//              byte[] bytes = new byte[chars.length * 2];
//              OS.WideCharToMultiByte(OS.CP_ACP, 0, chars, chars.length, bytes,
//                                     bytes.length, null, null);
//              shell.setToolTipText(lpnmtdi, bytes);
//              OS.MoveMemoryA(lParam, lpnmtdi, NMTTDISPINFO.sizeofA);
//            } else {
//              shell.setToolTipText(lpnmtdi, chars);
//              OS.MoveMemoryW(lParam, lpnmtdi, NMTTDISPINFO.sizeofW);
//            }
//            return LRESULT.ZERO;
//          }
//          break;
//        }
      }
    }
    return super.WM_NOTIFY(wParam, lParam);
  }

  LRESULT WM_PAINT(int wParam, int lParam) {
    if((state & CANVAS) == 0) {
      return super.WM_PAINT(wParam, lParam);
    }

    /*
     * This code is intentionally commented.  Don't exit
     * early because the background must still be painted,
     * even though no application code will be painting
     * the widget.
     *
     * Do not uncomment this code.
     */
//	if (!hooks (SWT.Paint)) return null;

    /* Get the damage */
    int[] lpRgnData = null;
    boolean isComplex = false;
    boolean exposeRegion = false;
    if((style & SWT.NO_MERGE_PAINTS) != 0) {
      int rgn = OS.CreateRectRgn(0, 0, 0, 0);
      isComplex = OS.GetUpdateRgn(handle, rgn, false) == OS.COMPLEXREGION;
      if(isComplex) {
        int nBytes = OS.GetRegionData(rgn, 0, null);
        lpRgnData = new int[nBytes / 4];
        exposeRegion = OS.GetRegionData(rgn, nBytes, lpRgnData) != 0;
      }
      OS.DeleteObject(rgn);
    }

    /* Set the clipping bits */
    int oldBits = 0;
    if(!OS.IsWinCE) {
      oldBits = OS.GetWindowLong(handle, OS.GWL_STYLE);
      int newBits = oldBits | OS.WS_CLIPSIBLINGS | OS.WS_CLIPCHILDREN;
      OS.SetWindowLong(handle, OS.GWL_STYLE, newBits);
    }

    /* Create the paint GC */
    PAINTSTRUCT ps = new PAINTSTRUCT();
    GCData data = new GCData();
    data.ps = ps;
    GC gc = GC.win32_new(this, data);
    int hDC = gc.handle;

    /* Send the paint event */
    Event event = new Event();
    event.gc = gc;
    if(isComplex && exposeRegion) {
      RECT rect = new RECT();
      int count = lpRgnData[2];
      for(int i = 0; i < count; i++) {
        OS.SetRect(rect,
                   lpRgnData[8 + (i << 2)],
                   lpRgnData[8 + (i << 2) + 1],
                   lpRgnData[8 + (i << 2) + 2],
                   lpRgnData[8 + (i << 2) + 3]);
        if((style & SWT.NO_BACKGROUND) == 0) {
          drawBackground(hDC, rect);
        }
        event.x = rect.left;
        event.y = rect.top;
        event.width = rect.right - rect.left;
        event.height = rect.bottom - rect.top;
        event.count = count - 1 - i;
        /*
         * It is possible (but unlikely), that application
         * code could have disposed the widget in the paint
         * event.  If this happens, attempt to give back the
         * paint GC anyways because this is a scarce Windows
         * resource.
         */
        sendEvent(SWT.Paint, event);
        if(isDisposed()) {
          break;
        }
      }
    } else {
      if((style & SWT.NO_BACKGROUND) == 0) {
        RECT rect = new RECT();
        OS.SetRect(rect, ps.left, ps.top, ps.right, ps.bottom);
        drawBackground(hDC, rect);
      }
      event.x = ps.left;
      event.y = ps.top;
      event.width = ps.right - ps.left;
      event.height = ps.bottom - ps.top;
      sendEvent(SWT.Paint, event);
    }
    // widget could be disposed at this point

    /* Dispose the paint GC */
    event.gc = null;
    gc.dispose();

    if(!OS.IsWinCE) {
      /*
       * It is possible (but unlikely), that application
       * code could have disposed the widget in the paint
       * event.  If this happens, don't attempt to restore
       * the style.
       */
      if(!isDisposed()) {
        OS.SetWindowLong(handle, OS.GWL_STYLE, oldBits);
      }
    }
    return LRESULT.ZERO;
  }

  LRESULT WM_SETFONT(int wParam, int lParam) {
    return super.WM_SETFONT(font = wParam, lParam);
  }

  void swingComponentResized(ComponentEvent e) {
    sendEvent(SWT.Resize);
    super.swingComponentResized(e);
    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the resize
     * event.  If this happens, end the processing.
     */
    if(isDisposed()) {
      return;
    }
    if(layout != null) {
      layout.layout(this, false);
    }
  }



  LRESULT WM_SIZE(int wParam, int lParam) {

    /* Begin deferred window positioning */
    setResizeChildren(false);

    /* Resize and Layout */
    LRESULT result = super.WM_SIZE(wParam, lParam);
    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the resize
     * event.  If this happens, end the processing of the
     * Windows message by returning the result of the
     * WM_SIZE message.
     */
    if(isDisposed()) {
      return result;
    }
    if(layout != null) {
      layout.layout(this, false);

      /* End deferred window positioning */
    }
    setResizeChildren(true);

    /* Damage the widget to cause a repaint */
    if((state & CANVAS) != 0) {
      if((style & SWT.NO_REDRAW_RESIZE) == 0) {
        if(hooks(SWT.Paint)) {
          OS.InvalidateRect(handle, null, true);
        }
      }
    }
    return result;
  }

  LRESULT WM_SYSCOLORCHANGE(int wParam, int lParam) {
    Control[] children = _getChildren();
    for(int i = 0; i < children.length; i++) {
      int hwndChild = children[i].handle;
      OS.SendMessage(hwndChild, OS.WM_SYSCOLORCHANGE, 0, 0);
    }
    return null;
  }

}
