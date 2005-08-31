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

import javax.swing.JToolBar;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class support the layout of selectable
 * tool bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ToolItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL, SHADOW_OUT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class ToolBar extends Composite {
  int lastFocusId;
  ToolItem[] items;
  boolean ignoreResize;
  ImageList imageList, disabledImageList, hotImageList;

  /*
   * From the Windows SDK for TB_SETBUTTONSIZE:
   *
   *   "If an application does not explicitly
   *	set the button size, the size defaults
   *	to 24 by 22 pixels".
   */
  static final int DEFAULT_WIDTH = 24;
  static final int DEFAULT_HEIGHT = 22;

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
   * @see SWT#FLAT
   * @see SWT#WRAP
   * @see SWT#RIGHT
   * @see SWT#HORIZONTAL
   * @see SWT#SHADOW_OUT
   * @see SWT#VERTICAL
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public ToolBar(Composite parent, int style) {
    super(parent, checkStyle(style));
    /*
     * Ensure that either of HORIZONTAL or VERTICAL is set.
     * NOTE: HORIZONTAL and VERTICAL have the same values
     * as H_SCROLL and V_SCROLL so it is necessary to first
     * clear these bits to avoid scroll bars and then reset
     * the bits using the original style supplied by the
     * programmer.
     */
    if((style & SWT.VERTICAL) != 0) {
      this.style |= SWT.VERTICAL;
    } else {
      this.style |= SWT.HORIZONTAL;
    }
  }

  static int checkStyle(int style) {
    /*
     * On Windows, only flat tool bars can be traversed.
     */
    if((style & SWT.FLAT) == 0) {
      style |= SWT.NO_FOCUS;

      /*
       * A vertical tool bar cannot wrap because TB_SETROWS
       * fails when the toobar has TBSTYLE_WRAPABLE.
       */
      /*
       * This code is intentionally commented.
       */
      //if ((style & SWT.VERTICAL) != 0) style &= ~SWT.WRAP;

      /*
       * The TB_SETROWS calls are currently commented, so force
       * the wrap style if this bar is vertical.
       */
    }
    if((style & SWT.VERTICAL) != 0) {
      style |= SWT.WRAP;

      /*
       * Even though it is legal to create this widget
       * with scroll bars, they serve no useful purpose
       * because they do not automatically scroll the
       * widget's client area.  The fix is to clear
       * the SWT style.
       */
    }
    return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
  }

  protected void checkSubclass() {
    if(!isValidSubclass()) {
      error(SWT.ERROR_INVALID_SUBCLASS);
    }
  }

//  public Point computeSize(int wHint, int hHint, boolean changed) {
//    checkWidget();
//    if(layout != null) {
//      return super.computeSize(wHint, hHint, changed);
//    }
//    int width = 0, height = 0;
//    RECT oldRect = new RECT();
//    OS.GetWindowRect(handle, oldRect);
//    int oldWidth = oldRect.right - oldRect.left;
//    int oldHeight = oldRect.bottom - oldRect.top;
//    int border = getBorderWidth();
//    int newWidth = wHint == SWT.DEFAULT ? 0x3FFF : wHint + (border * 2);
//    int newHeight = hHint == SWT.DEFAULT ? 0x3FFF : hHint + (border * 2);
//    boolean redraw = drawCount == 0 && OS.IsWindowVisible(handle);
//    ignoreResize = true;
//    if(redraw) {
//      OS.UpdateWindow(handle);
//    }
//    int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOREDRAW |
//        OS.SWP_NOZORDER;
//    OS.SetWindowPos(handle, 0, 0, 0, newWidth, newHeight, flags);
//    int count = OS.SendMessage(handle, OS.TB_BUTTONCOUNT, 0, 0);
//    if(count != 0) {
//      RECT rect = new RECT();
//      OS.SendMessage(handle, OS.TB_GETITEMRECT, count - 1, rect);
//      width = Math.max(width, rect.right);
//      height = Math.max(height, rect.bottom);
//    }
//    OS.SetWindowPos(handle, 0, 0, 0, oldWidth, oldHeight, flags);
//    if(redraw) {
//      OS.ValidateRect(handle, null);
//    }
//    ignoreResize = false;
//
//    /*
//     * From the Windows SDK for TB_SETBUTTONSIZE:
//     *
//     *   "If an application does not explicitly
//     *	set the button size, the size defaults
//     *	to 24 by 22 pixels".
//     */
//    if(width == 0) {
//      width = DEFAULT_WIDTH;
//    }
//    if(height == 0) {
//      height = DEFAULT_HEIGHT;
//    }
//    if(wHint != SWT.DEFAULT) {
//      width = wHint;
//    }
//    if(hHint != SWT.DEFAULT) {
//      height = hHint;
//    }
//    Rectangle trim = computeTrim(0, 0, width, height);
//    width = trim.width;
//    height = trim.height;
//    return new Point(width, height);
//  }
//
//  public Rectangle computeTrim(int x, int y, int width, int height) {
//    checkWidget();
//    Rectangle trim = super.computeTrim(x, y, width, height);
//    int bits = OS.GetWindowLong(handle, OS.GWL_STYLE);
//    if((bits & OS.CCS_NODIVIDER) == 0) {
//      trim.height += 2;
//    }
//    return trim;
//  }

  JToolBar toolBar;

  Container getNewHandle() {
    //FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL, SHADOW_OUT
    // TODO: styles
    toolBar = new JToolBar((style & SWT.VERTICAL) != 0? JToolBar.VERTICAL: JToolBar.HORIZONTAL);
    toolBar.setFloatable(false);
    if((style & SWT.BORDER) != 0) {
      toolBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    }
    if((style & SWT.WRAP) != 0) {
      toolBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
    }
    return toolBar;
  }
  
  boolean overrideLayout() {
    return false;
  }

  void createHandle() {
    super.createHandle();
    state &= ~CANVAS;

    /*
     * Feature in Windows.  Despite the fact that the
     * tool tip text contains \r\n, the tooltip will
     * not honour the new line unless TTM_SETMAXTIPWIDTH
     * is set.  The fix is to set TTM_SETMAXTIPWIDTH to
     * a large value.
     */
    /*
     * These lines are intentionally commented.  The tool
     * bar currently sets this value to 300 so it is not
     * necessary to set TTM_SETMAXTIPWIDTH.
     */
//	int hwndToolTip = OS.SendMessage (handle, OS.TB_GETTOOLTIPS, 0, 0);
//	OS.SendMessage (hwndToolTip, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);

    /*
     * Feature in Windows.  When the control is created,
     * it does not use the default system font.  A new HFONT
     * is created and destroyed when the control is destroyed.
     * This means that a program that queries the font from
     * this control, uses the font in another control and then
     * destroys this control will have the font unexpectedly
     * destroyed in the other control.  The fix is to assign
     * the font ourselves each time the control is created.
     * The control will not destroy a font that it did not
     * create.
     */
//    int hFont = OS.GetStockObject(OS.SYSTEM_FONT);
//    OS.SendMessage(handle, OS.WM_SETFONT, hFont, 0);
//
//    /* Set the button struct, bitmap and button sizes */
//    OS.SendMessage(handle, OS.TB_BUTTONSTRUCTSIZE, TBBUTTON.sizeof, 0);
//    OS.SendMessage(handle, OS.TB_SETBITMAPSIZE, 0, 0);
//    OS.SendMessage(handle, OS.TB_SETBUTTONSIZE, 0, 0);
//
//    /* Set the extended style bits */
//    OS.SendMessage(handle, OS.TB_SETEXTENDEDSTYLE, 0,
//                   OS.TBSTYLE_EX_DRAWDDARROWS);
  }

  void createItem(ToolItem item, int index) {
    int count = getItemCount();
    if(!(0 <= index && index <= count)) {
      error(SWT.ERROR_INVALID_RANGE);
    }
    int id = 0;
    while(id < items.length && items[id] != null) {
      id++;
    }
    if(id == items.length) {
      ToolItem[] newItems = new ToolItem[items.length + 4];
      System.arraycopy(items, 0, newItems, 0, items.length);
      items = newItems;
    }
//    int bits = item.widgetStyle();
//    TBBUTTON lpButton = new TBBUTTON();
//    lpButton.idCommand = id;
//    lpButton.fsStyle = (byte)bits;
//    lpButton.fsState = (byte)OS.TBSTATE_ENABLED;
//
//    /*
//     * Bug in Windows.  Despite the fact that the image list
//     * index has never been set for the item, Windows always
//     * assumes that the image index for the item is valid.
//     * When an item is inserted, the image index is zero.
//     * Therefore, when the first image is inserted and is
//     * assigned image index zero, every item draws with this
//     * image.  The fix is to set the image index to none
//     * when the item is created.  This is not necessary in
//     * the case when the item has the BTNS_SEP style because
//     * separators cannot show images.
//     */
//    if((bits & OS.BTNS_SEP) == 0) {
//      lpButton.iBitmap = OS.I_IMAGENONE;
//    }
//    if(OS.SendMessage(handle, OS.TB_INSERTBUTTON, index, lpButton) == 0) {
//      error(SWT.ERROR_ITEM_NOT_ADDED);
//    }
    items[item.id = id] = item;
    toolBar.add(item.getHandle(), index);
    layoutItems();
  }

  void createWidget() {
    super.createWidget();
    items = new ToolItem[4];
    lastFocusId = -1;
  }

  int defaultBackground() {
    if(OS.IsWinCE) {
      return OS.GetSysColor(OS.COLOR_BTNFACE);
    }
    return super.defaultBackground();
  }

  void destroyItem(ToolItem item) {
    TBBUTTONINFO info = new TBBUTTONINFO();
    info.cbSize = TBBUTTONINFO.sizeof;
    info.dwMask = OS.TBIF_IMAGE | OS.TBIF_STYLE;
    int index = OS.SendMessage(handle, OS.TB_GETBUTTONINFO, item.id, info);
    /*
     * Feature in Windows.  For some reason, a tool item that has
     * the style BTNS_SEP does not return I_IMAGENONE when queried
     * for an image index, despite the fact that no attempt has been
     * made to assign an image to the item.  As a result, operations
     * on an image list that use the wrong index cause random results.
     * The fix is to ensure that the tool item is not a separator
     * before using the image index.  Since separators cannot have
     * an image and one is never assigned, this is not a problem.
     */
    if((info.fsStyle & OS.BTNS_SEP) == 0 && info.iImage != OS.I_IMAGENONE) {
      if(imageList != null) {
        imageList.put(info.iImage, null);
      }
      if(hotImageList != null) {
        hotImageList.put(info.iImage, null);
      }
      if(disabledImageList != null) {
        disabledImageList.put(info.iImage, null);
      }
    }
    OS.SendMessage(handle, OS.TB_DELETEBUTTON, index, 0);
    if(item.id == lastFocusId) {
      lastFocusId = -1;
    }
    items[item.id] = null;
    item.id = -1;
    int count = OS.SendMessage(handle, OS.TB_BUTTONCOUNT, 0, 0);
    if(count == 0) {
      Display display = getDisplay();
      if(imageList != null) {
        OS.SendMessage(handle, OS.TB_SETIMAGELIST, 0, 0);
        display.releaseToolImageList(imageList);
      }
      if(hotImageList != null) {
        OS.SendMessage(handle, OS.TB_SETHOTIMAGELIST, 0, 0);
        display.releaseToolHotImageList(hotImageList);
      }
      if(disabledImageList != null) {
        OS.SendMessage(handle, OS.TB_SETDISABLEDIMAGELIST, 0, 0);
        display.releaseToolDisabledImageList(disabledImageList);
      }
      imageList = hotImageList = disabledImageList = null;
      items = new ToolItem[4];
    }
    /*
     * This code is intentionally commented.
     */
//	if ((style & SWT.VERTICAL) != 0) {
//		OS.SendMessage (handle, OS.TB_SETROWS, count-1, 0);
//	}
    layoutItems();
  }

  ImageList getDisabledImageList() {
    return disabledImageList;
  }

  ImageList getHotImageList() {
    return hotImageList;
  }

  ImageList getImageList() {
    return imageList;
  }

  /**
   * Returns the item at the given, zero-relative index in the
   * receiver. Throws an exception if the index is out of range.
   *
   * @param index the index of the item to return
   * @return the item at the given index
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public ToolItem getItem(int index) {
    checkWidget();
    int count = OS.SendMessage(handle, OS.TB_BUTTONCOUNT, 0, 0);
    if(!(0 <= index && index < count)) {
      error(SWT.ERROR_INVALID_RANGE);
    }
    TBBUTTON lpButton = new TBBUTTON();
    int result = OS.SendMessage(handle, OS.TB_GETBUTTON, index, lpButton);
    if(result == 0) {
      error(SWT.ERROR_CANNOT_GET_ITEM);
    }
    return items[lpButton.idCommand];
  }

  /**
   * Returns the item at the given point in the receiver
   * or null if no such item exists. The point is in the
   * coordinate system of the receiver.
   *
   * @param point the point used to locate the item
   * @return the item at the given point
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public ToolItem getItem(Point point) {
    checkWidget();
    if(point == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    ToolItem[] items = getItems();
    for(int i = 0; i < items.length; i++) {
      Rectangle rect = items[i].getBounds();
      if(rect.contains(point)) {
        return items[i];
      }
    }
    return null;
  }

  /**
   * Returns the number of items contained in the receiver.
   *
   * @return the number of items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getItemCount() {
    checkWidget();
    return getHandle().getComponentCount();
  }

  /**
   * Returns an array of <code>TabItem</code>s which are the items
   * in the receiver.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its list of items, so modifying the array will
   * not affect the receiver.
   * </p>
   *
   * @return the items in the receiver
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public ToolItem[] getItems() {
    checkWidget();
    int count = swingHandle.getComponentCount();
    ToolItem[] result = new ToolItem[count];
    for(int i = 0; i < count; i++) {
      result[i] = this.items[i];
    }
//    int count = OS.SendMessage(handle, OS.TB_BUTTONCOUNT, 0, 0);
//    TBBUTTON lpButton = new TBBUTTON();
//    ToolItem[] result = new ToolItem[count];
//    for(int i = 0; i < count; i++) {
//      OS.SendMessage(handle, OS.TB_GETBUTTON, i, lpButton);
//      result[i] = items[lpButton.idCommand];
//    }
    return result;
  }

  /**
   * Returns the number of rows in the receiver. When
   * the receiver has the <code>WRAP</code> style, the
   * number of rows can be greater than one.  Otherwise,
   * the number of rows is always one.
   *
   * @return the number of items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getRowCount() {
    checkWidget();
    return OS.SendMessage(handle, OS.TB_GETROWS, 0, 0);
  }

  /**
   * Searches the receiver's list starting at the first item
   * (index 0) until an item is found that is equal to the
   * argument, and returns the index of that item. If no item
   * is found, returns -1.
   *
   * @param item the search item
   * @return the index of the item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the tool item is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the tool item has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int indexOf(ToolItem item) {
    checkWidget();
    if(item == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(item.isDisposed()) {
      error(SWT.ERROR_INVALID_ARGUMENT);
    }
    return OS.SendMessage(handle, OS.TB_COMMANDTOINDEX, item.id, 0);
  }

  void layoutItems() {
    for(int i = 0; i < items.length; i++) {
      ToolItem item = items[i];
      if(item != null) {
        item.resizeControl();
      }
    }
  }

  boolean mnemonicHit(char ch) {
    int key = wcsToMbcs(ch);
    int[] id = new int[1];
    if(OS.SendMessage(handle, OS.TB_MAPACCELERATOR, key, id) == 0) {
      return false;
    }
    if(!setTabGroupFocus()) {
      return false;
    }
    int index = OS.SendMessage(handle, OS.TB_COMMANDTOINDEX, id[0], 0);
    if(index == -1) {
      return false;
    }
    OS.SendMessage(handle, OS.TB_SETHOTITEM, index, 0);
    items[id[0]].click(false);
    return true;
  }

  boolean mnemonicMatch(char ch) {
    int key = wcsToMbcs(ch);
    int[] id = new int[1];
    return OS.SendMessage(handle, OS.TB_MAPACCELERATOR, key, id) != 0;
  }

  void releaseWidget() {
    for(int i = 0; i < items.length; i++) {
      ToolItem item = items[i];
      if(item != null && !item.isDisposed()) {
        // TODO: find how to release the resources appropriately
//        item.releaseImages();
        item.releaseResources();
      }
    }
    items = null;
    Display display = getDisplay();
    if(imageList != null) {
      OS.SendMessage(handle, OS.TB_SETIMAGELIST, 0, 0);
      display.releaseToolImageList(imageList);
    }
    if(hotImageList != null) {
      OS.SendMessage(handle, OS.TB_SETHOTIMAGELIST, 0, 0);
      display.releaseToolHotImageList(hotImageList);
    }
    if(disabledImageList != null) {
      OS.SendMessage(handle, OS.TB_SETDISABLEDIMAGELIST, 0, 0);
      display.releaseToolDisabledImageList(disabledImageList);
    }
    imageList = hotImageList = disabledImageList = null;
    super.releaseWidget();
  }

//  void setBounds(int x, int y, int width, int height, int flags) {
//    /*
//     * Feature in Windows.  For some reason, when a tool bar is
//     * repositioned more than once using DeferWindowPos () into
//     * the same HDWP, the toolbar redraws more than once, defeating
//     * the puropse of DeferWindowPos ().  The fix is to end the
//     * defered positioning before the next tool bar is added,
//     * ensuring that only one tool bar position is deferred at
//     * any given time.
//     */
//    if(parent.lpwp != null) {
//      if(drawCount == 0 && OS.IsWindowVisible(handle)) {
//        parent.setResizeChildren(false);
//        parent.setResizeChildren(true);
//      }
//    }
//    super.setBounds(x, y, width, height, flags);
//  }

//  void setDefaultFont() {
//    super.setDefaultFont();
//    OS.SendMessage(handle, OS.TB_SETBITMAPSIZE, 0, 0);
//    OS.SendMessage(handle, OS.TB_SETBUTTONSIZE, 0, 0);
//  }

  void setDisabledImageList(ImageList imageList) {
    if(disabledImageList == imageList) {
      return;
    }
    int hImageList = 0;
    if((disabledImageList = imageList) != null) {
      hImageList = disabledImageList.getHandle();
    }
    OS.SendMessage(handle, OS.TB_SETDISABLEDIMAGELIST, 0, hImageList);
  }

  void setHotImageList(ImageList imageList) {
    if(hotImageList == imageList) {
      return;
    }
    int hImageList = 0;
    if((hotImageList = imageList) != null) {
      hImageList = hotImageList.getHandle();
    }
    OS.SendMessage(handle, OS.TB_SETHOTIMAGELIST, 0, hImageList);
  }

  void setImageList(ImageList imageList) {
    if(this.imageList == imageList) {
      return;
    }
    int hImageList = 0;
    if((this.imageList = imageList) != null) {
      hImageList = imageList.getHandle();
    }
    OS.SendMessage(handle, OS.TB_SETIMAGELIST, 0, hImageList);
  }

  boolean setTabItemFocus() {
    int index = 0;
    while(index < items.length) {
      ToolItem item = items[index];
      if(item != null && (item.style & SWT.SEPARATOR) == 0) {
        if(item.getEnabled()) {
          break;
        }
      }
      index++;
    }
    if(index == items.length) {
      return false;
    }
    return super.setTabItemFocus();
  }

  String toolTipText(NMTTDISPINFO hdr) {
    if((hdr.uFlags & OS.TTF_IDISHWND) != 0) {
      return null;
    }
    int index = hdr.idFrom;
    int hwndToolTip = OS.SendMessage(handle, OS.TB_GETTOOLTIPS, 0, 0);
    if(hwndToolTip == hdr.hwndFrom) {
      if(toolTipText != null) {
        return ""; //$NON-NLS-1$
      }
      if(0 <= index && index < items.length) {
        ToolItem item = items[index];
        if(item != null) {
          return item.toolTipText;
        }
      }
    }
    return super.toolTipText(hdr);
  }

  int widgetStyle() {
    int bits = super.widgetStyle() | OS.CCS_NORESIZE | OS.TBSTYLE_TOOLTIPS;
    if((style & SWT.SHADOW_OUT) == 0) {
      bits |= OS.CCS_NODIVIDER;
    }
    if((style & SWT.WRAP) != 0) {
      bits |= OS.TBSTYLE_WRAPABLE;
    }
    if((style & SWT.FLAT) != 0) {
      bits |= OS.TBSTYLE_FLAT;
    }
    if((style & SWT.RIGHT) != 0) {
      bits |= OS.TBSTYLE_LIST;
    }
    return bits;
  }

  LRESULT WM_GETDLGCODE(int wParam, int lParam) {
    LRESULT result = super.WM_GETDLGCODE(wParam, lParam);
    /*
     * Return DLGC_BUTTON so that mnemonics will be
     * processed without needing to press the ALT key
     * when the widget has focus.
     */
    if(result != null) {
      return result;
    }
    return new LRESULT(OS.DLGC_BUTTON);
  }

  LRESULT WM_KEYDOWN(int wParam, int lParam) {
    LRESULT result = super.WM_KEYDOWN(wParam, lParam);
    if(result != null) {
      return result;
    }
    switch(wParam) {
      case OS.VK_RETURN:
      case OS.VK_SPACE:
        int index = OS.SendMessage(handle, OS.TB_GETHOTITEM, 0, 0);
        if(index != -1) {
          TBBUTTON lpButton = new TBBUTTON();
          int code = OS.SendMessage(handle, OS.TB_GETBUTTON, index, lpButton);
          if(code != 0) {
            items[lpButton.idCommand].click(wParam == OS.VK_RETURN);
            return LRESULT.ZERO;
          }
        }
    }
    return result;
  }

  LRESULT WM_KILLFOCUS(int wParam, int lParam) {
    int index = OS.SendMessage(handle, OS.TB_GETHOTITEM, 0, 0);
    TBBUTTON lpButton = new TBBUTTON();
    int code = OS.SendMessage(handle, OS.TB_GETBUTTON, index, lpButton);
    if(code != 0) {
      lastFocusId = lpButton.idCommand;
    }
    return super.WM_KILLFOCUS(wParam, lParam);
  }

  LRESULT WM_NOTIFY(int wParam, int lParam) {
    /*
     * Feature in Windows.  When the toolbar window
     * proc processes WM_NOTIFY, it forwards this
     * message to its parent.  This is done so that
     * children of this control that send this message
     * type to their parent will notify not only
     * this control but also the parent of this control,
     * which is typically the application window and
     * the window that is looking for the message.
     * If the control did not forward the message,
     * applications would have to subclass the control
     * window to see the message. Because the control
     * window is subclassed by SWT, the message
     * is delivered twice, once by SWT and once when
     * the message is forwarded by the window proc.
     * The fix is to avoid calling the window proc
     * for this control.
     */
    LRESULT result = super.WM_NOTIFY(wParam, lParam);
    if(result != null) {
      return result;
    }
    return LRESULT.ZERO;
  }

  LRESULT WM_SETFOCUS(int wParam, int lParam) {
    LRESULT result = super.WM_SETFOCUS(wParam, lParam);
    if(lastFocusId != -1 && handle == OS.GetFocus()) {
      int index = OS.SendMessage(handle, OS.TB_COMMANDTOINDEX, lastFocusId, 0);
      OS.SendMessage(handle, OS.TB_SETHOTITEM, index, 0);
    }
    return result;
  }

  LRESULT WM_SIZE(int wParam, int lParam) {
    if(ignoreResize) {
      int code = callWindowProc(OS.WM_SIZE, wParam, lParam);
      if(code == 0) {
        return LRESULT.ZERO;
      }
      return new LRESULT(code);
    }
    /*
     * Feature in Windows.  When a tool bar that contains
     * separators is wrapped, under certain circumstances,
     * Windows redraws the entire tool bar unnecessarily
     * when resized no item is moves.  Whether the entire
     * toolbar is damaged or not seems to depend on the
     * size of the tool bar and the position of the separators.
     * The fix is to ensure that the newly exposed areas are
     * always damaged, and avoid the redraw when no tool item
     * moves.
     */
    RECT[] rects = null;
    int rgn = 0, oldCount = 0;
    boolean fixRedraw = drawCount == 0 &&
        (style & SWT.WRAP) != 0 &&
        OS.IsWindowVisible(handle) &&
        OS.SendMessage(handle, OS.TB_GETROWS, 0, 0) != 1;
    if(fixRedraw) {
      rgn = OS.CreateRectRgn(0, 0, 0, 0);
      OS.GetUpdateRgn(handle, rgn, false);
      oldCount = OS.SendMessage(handle, OS.TB_BUTTONCOUNT, 0, 0);
      rects = new RECT[oldCount];
      for(int i = 0; i < oldCount; i++) {
        rects[i] = new RECT();
        OS.SendMessage(handle, OS.TB_GETITEMRECT, i, rects[i]);
      }
    }

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

    if(fixRedraw) {
      int newCount = OS.SendMessage(handle, OS.TB_BUTTONCOUNT, 0, 0);
      if(newCount == oldCount) {
        int index = 0;
        RECT rect = new RECT();
        while(index < newCount) {
          OS.SendMessage(handle, OS.TB_GETITEMRECT, index, rect);
          if(!OS.EqualRect(rects[index], rect)) {
            break;
          }
          index++;
        }
        if(index == newCount) {
          OS.ValidateRect(handle, null);
          OS.InvalidateRgn(handle, rgn, false);
        }
      }
      OS.DeleteObject(rgn);
    }

    layoutItems();
    return result;
  }

  LRESULT WM_WINDOWPOSCHANGING(int wParam, int lParam) {
    LRESULT result = super.WM_WINDOWPOSCHANGING(wParam, lParam);
    if(result != null) {
      return result;
    }
    /*
     * Feature in Windows.  When a tool bar that contains
     * separators is wrapped, under certain circumstances,
     * Windows redraws the entire tool bar unnecessarily
     * when resized no item is moves.  Whether the entire
     * toolbar is damaged or not seems to depend on the
     * size of the tool bar and the position of the separators.
     * The fix is to ensure that the newly exposed areas are
     * always damaged, and avoid the redraw when no tool item
     * moves.
     */
    if(drawCount != 0) {
      return result;
    }
    if((style & SWT.WRAP) == 0) {
      return result;
    }
    if(!OS.IsWindowVisible(handle)) {
      return result;
    }
    if(OS.SendMessage(handle, OS.TB_GETROWS, 0, 0) == 1) {
      return result;
    }
    WINDOWPOS lpwp = new WINDOWPOS();
    OS.MoveMemory(lpwp, lParam, WINDOWPOS.sizeof);
    if((lpwp.flags & (OS.SWP_NOSIZE | OS.SWP_NOREDRAW)) != 0) {
      return result;
    }
    RECT oldRect = new RECT();
    OS.GetClientRect(handle, oldRect);
    RECT newRect = new RECT();
    OS.SetRect(newRect, 0, 0, lpwp.cx, lpwp.cy);
    OS.SendMessage(handle, OS.WM_NCCALCSIZE, 0, newRect);
    int oldWidth = oldRect.right - oldRect.left;
    int oldHeight = oldRect.bottom - oldRect.top;
    int newWidth = newRect.right - newRect.left;
    int newHeight = newRect.bottom - newRect.top;
    if(newWidth > oldWidth) {
      /*
       * Bug in Windows.  When a flat tool bar is wrapped,
       * Windows draws a horizontal separator between the
       * rows.  The tool bar does not draw the first or
       * the last two pixels of this separator.  When the
       * toolbar is resized to be bigger, only the new
       * area is drawn and the last two pixels, which are
       * blank are drawn over by separator.  This leaves
       * garbage on the screen.  The fix is to damage the
       * pixels.
       */
      RECT rect = new RECT();
      OS.SetRect(rect, oldWidth - 2, 0, oldWidth, newHeight);
      OS.InvalidateRect(handle, rect, false);
      OS.SetRect(rect, oldRect.right, newRect.top, newRect.right,
                 newRect.bottom);
      OS.InvalidateRect(handle, rect, true);
    }
    if(newHeight > oldHeight) {
      RECT rect = new RECT();
      OS.SetRect(rect, newRect.left, oldRect.bottom, newRect.right,
                 newRect.bottom);
      OS.InvalidateRect(handle, rect, true);
    }
    return result;
  }

  LRESULT wmCommandChild(int wParam, int lParam) {
    ToolItem child = items[wParam & 0xFFFF];
    if(child == null) {
      return null;
    }
    return child.wmCommandChild(wParam, lParam);
  }

  LRESULT wmNotifyChild(int wParam, int lParam) {
    NMHDR hdr = new NMHDR();
    OS.MoveMemory(hdr, lParam, NMHDR.sizeof);
    switch(hdr.code) {
      case OS.TBN_DROPDOWN:
        NMTOOLBAR lpnmtb = new NMTOOLBAR();
        OS.MoveMemory(lpnmtb, lParam, NMTOOLBAR.sizeof);
        ToolItem child = items[lpnmtb.iItem];
        if(child != null) {
          Event event = new Event();
          event.detail = SWT.ARROW;
          int index = OS.SendMessage(handle, OS.TB_COMMANDTOINDEX, lpnmtb.iItem,
                                     0);
          RECT rect = new RECT();
          OS.SendMessage(handle, OS.TB_GETITEMRECT, index, rect);
          event.x = rect.left;
          event.y = rect.bottom;
          child.postEvent(SWT.Selection, event);
          return null;
        }
        break;
    }
    return super.wmNotifyChild(wParam, lParam);
  }

}
