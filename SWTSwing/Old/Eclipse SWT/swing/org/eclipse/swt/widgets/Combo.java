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

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class are controls that allow the user
 * to choose an item from a list of items, or optionally
 * enter a new value by typing it into an editable text
 * field. Often, <code>Combo</code>s are used in the same place
 * where a single selection <code>List</code> widget could
 * be used but space is limited. A <code>Combo</code> takes
 * less space than a <code>List</code> widget and shows
 * similar information.
 * <p>
 * Note: Since <code>Combo</code>s can contain both a list
 * and an editable text field, it is possible to confuse methods
 * which access one versus the other (compare for example,
 * <code>clearSelection()</code> and <code>deselectAll()</code>).
 * The API documentation is careful to indicate either "the
 * receiver's list" or the "the receiver's text field" to
 * distinguish between the two cases.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DROP_DOWN, READ_ONLY, SIMPLE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 */

public class Combo extends Composite {
  boolean noSelection;

  /**
   * the operating system limit for the number of characters
   * that the text field in an instance of this class can hold
   */
  public static final int LIMIT = 0x7FFFFFFF;

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
   * @see SWT#DROP_DOWN
   * @see SWT#READ_ONLY
   * @see SWT#SIMPLE
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public Combo(Composite parent, int style) {
    super(parent, checkStyle(style));
  }

  /**
   * Adds the argument to the end of the receiver's list.
   *
   * @param string the new item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
   * </ul>
   *
   * @see #add(String,int)
   */
  public void add(String string) {
    checkWidget();
    if(string == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if((style & SWT.SIMPLE) != 0) {
      ((DefaultListModel)valueList.getModel()).addElement(string);
    } else {
      comboBox.addItem(string);
    }
//    TCHAR buffer = new TCHAR(getCodePage(), string, true);
//    int result = OS.SendMessage(handle, OS.CB_ADDSTRING, 0, buffer);
//    if(result == OS.CB_ERR) {
//      error(SWT.ERROR_ITEM_NOT_ADDED);
//    }
//    if(result == OS.CB_ERRSPACE) {
//      error(SWT.ERROR_ITEM_NOT_ADDED);
//    }
  }

  /**
   * Adds the argument to the receiver's list at the given
   * zero-relative index.
   * <p>
   * Note: To add an item at the end of the list, use the
   * result of calling <code>getItemCount()</code> as the
   * index or use <code>add(String)</code>.
   * </p>
   *
   * @param string the new item
   * @param index the index for the item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
   * </ul>
   *
   * @see #add(String)
   */
  public void add(String string, int index) {
    checkWidget();
    if(string == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if((style & SWT.SIMPLE) != 0) {
      DefaultListModel model = (DefaultListModel)valueList.getModel();
      if(index < 0 || index > model.size()) {
        error(SWT.ERROR_INVALID_RANGE);
      }
      model.insertElementAt(string, index);
    } else {
      if(index < 0 || index > comboBox.getItemCount()) {
        error(SWT.ERROR_INVALID_RANGE);
      }
      comboBox.insertItemAt(string, index);
    }
//    TCHAR buffer = new TCHAR(getCodePage(), string, true);
//    int result = OS.SendMessage(handle, OS.CB_INSERTSTRING, index, buffer);
//    if(result == OS.CB_ERRSPACE) {
//      error(SWT.ERROR_ITEM_NOT_ADDED);
//    }
//    if(result == OS.CB_ERR) {
//      int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//      if(0 <= index && index <= count) {
//        error(SWT.ERROR_ITEM_NOT_ADDED);
//      } else {
//        error(SWT.ERROR_INVALID_RANGE);
//      }
//    }
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the receiver's text is modified, by sending
   * it one of the messages defined in the <code>ModifyListener</code>
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
   * @see ModifyListener
   * @see #removeModifyListener
   */
  public void addModifyListener(ModifyListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Modify, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the receiver's selection changes, by sending
   * it one of the messages defined in the <code>SelectionListener</code>
   * interface.
   * <p>
       * <code>widgetSelected</code> is called when the combo's list selection changes.
   * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed the combo's text area.
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

  protected void checkSubclass() {
    if(!isValidSubclass()) {
      error(SWT.ERROR_INVALID_SUBCLASS);
    }
  }

  static int checkStyle(int style) {
    /*
     * Feature in Windows.  It is not possible to create
     * a combo box that has a border using Windows style
     * bits.  All combo boxes draw their own border and
     * do not use the standard Windows border styles.
     * Therefore, no matter what style bits are specified,
     * clear the BORDER bits so that the SWT style will
     * match the Windows widget.
     *
     * The Windows behavior is currently implemented on
     * all platforms.
     */
    style &= ~SWT.BORDER;

    /*
     * Even though it is legal to create this widget
     * with scroll bars, they serve no useful purpose
     * because they do not automatically scroll the
     * widget's client area.  The fix is to clear
     * the SWT style.
     */
    style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
    style = checkBits(style, SWT.DROP_DOWN, SWT.SIMPLE, 0, 0, 0, 0);
    if((style & SWT.SIMPLE) != 0) {
      return style & ~SWT.READ_ONLY;
    }
    return style;
  }

  /**
   * Sets the selection in the receiver's text field to an empty
   * selection starting just before the first character. If the
   * text field is editable, this has the effect of placing the
   * i-beam at the start of the text.
   * <p>
   * Note: To clear the selected items in the receiver's list,
   * use <code>deselectAll()</code>.
   * </p>
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #deselectAll
   */
  public void clearSelection() {
    checkWidget();
    OS.SendMessage(handle, OS.CB_SETEDITSEL, 0, -1);
  }

//  public Point computeSize(int wHint, int hHint, boolean changed) {
//    checkWidget();
//    int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//    int itemHeight = OS.SendMessage(handle, OS.CB_GETITEMHEIGHT, 0, 0);
//    int width = 0, height = 0;
//    if((style & SWT.SIMPLE) != 0) {
//      height = count * itemHeight;
//    }
//    int newFont, oldFont = 0;
//    int hDC = OS.GetDC(handle);
//    newFont = OS.SendMessage(handle, OS.WM_GETFONT, 0, 0);
//    if(newFont != 0) {
//      oldFont = OS.SelectObject(hDC, newFont);
//    }
//    RECT rect = new RECT();
//    int flags = OS.DT_CALCRECT | OS.DT_NOPREFIX;
//    int length = OS.GetWindowTextLength(handle);
//    int cp = getCodePage();
//    TCHAR buffer = new TCHAR(cp, length + 1);
//    OS.GetWindowText(handle, buffer, length + 1);
//    OS.DrawText(hDC, buffer, length, rect, flags);
//    width = Math.max(width, rect.right - rect.left);
//    for(int i = 0; i < count; i++) {
//      length = OS.SendMessage(handle, OS.CB_GETLBTEXTLEN, i, 0);
//      if(length != OS.CB_ERR) {
//        if(length + 1 > buffer.length()) {
//          buffer = new TCHAR(cp, length + 1);
//        }
//        int result = OS.SendMessage(handle, OS.CB_GETLBTEXT, i, buffer);
//        if(result != OS.CB_ERR) {
//          OS.DrawText(hDC, buffer, length, rect, flags);
//          width = Math.max(width, rect.right - rect.left);
//        }
//      }
//    }
//    TEXTMETRIC tm = new TEXTMETRIC();
//    OS.GetTextMetrics(hDC, tm);
//    if(newFont != 0) {
//      OS.SelectObject(hDC, oldFont);
//    }
//    OS.ReleaseDC(handle, hDC);
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
//    int border = OS.GetSystemMetrics(OS.SM_CXEDGE);
//    width += OS.GetSystemMetrics(OS.SM_CXVSCROLL) +
//        (tm.tmInternalLeading + border) * 2;
//    int textHeight = OS.SendMessage(handle, OS.CB_GETITEMHEIGHT, -1, 0);
//    if((style & SWT.DROP_DOWN) != 0) {
//      height = textHeight + 6;
//    } else {
//      height += textHeight + 10;
//    }
//    return new Point(width, height);
//  }

  /**
   * Copies the selected text.
   * <p>
   * The current selection is copied to the clipboard.
   * </p>
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 2.1
   */
  public void copy() {
    checkWidget();
    OS.SendMessage(handle, OS.WM_COPY, 0, 0);
  }

  void createHandle() {
    super.createHandle();
    state &= ~CANVAS;
  }

  // Normal case
  JComboBox comboBox;
  // "Simple" case
  JTextField valueField;
  JList valueList;

  Container getNewHandle() {
    if((style & SWT.SIMPLE) != 0) {
      JPanel panel = new JPanel(new BorderLayout());
      valueField = new JTextField();
      valueField.setFont(UIManager.getFont("ComboBox.font"));
      valueField.setBackground(UIManager.getColor("List.background"));
      valueField.setForeground(UIManager.getColor("List.foreground"));
      panel.add(valueField, BorderLayout.NORTH);
      valueList = new JList(new DefaultListModel());
      valueList.setFont(UIManager.getFont("ComboBox.font"));
      valueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      valueList.addListSelectionListener(new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
          String value = (String)valueList.getSelectedValue();
          if(value != null) {
            valueField.setText(value);
          }
        }
      });
      panel.add(valueList, BorderLayout.CENTER);
      panel.setBorder(BorderFactory.createEtchedBorder());
      return panel;
    } else {
      comboBox = new JComboBox();
//      comboBox.addActionListener(new java.awt.event.ActionListener(){
//        public void actionPerformed(java.awt.event.ActionEvent e) {
//          System.err.println("action!");
//        }
//      });
      comboBox.setEditable((style & SWT.READ_ONLY) == 0);
      return comboBox;
    }
  }

  /**
   * Cuts the selected text.
   * <p>
   * The current selection is first copied to the
   * clipboard and then deleted from the widget.
   * </p>
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 2.1
   */
  public void cut() {
    checkWidget();
    OS.SendMessage(handle, OS.WM_CUT, 0, 0);
  }

//  int defaultBackground() {
//    return OS.GetSysColor(OS.COLOR_WINDOW);
//  }

  /**
   * Deselects the item at the given zero-relative index in the receiver's
   * list.  If the item at the index was already deselected, it remains
   * deselected. Indices that are out of range are ignored.
   *
   * @param index the index of the item to deselect
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void deselect(int index) {
    checkWidget();
    int selection = OS.SendMessage(handle, OS.CB_GETCURSEL, 0, 0);
    if(index != selection) {
      return;
    }
    OS.SendMessage(handle, OS.CB_SETCURSEL, -1, 0);
    sendEvent(SWT.Modify);
    // widget could be disposed at this point
  }

  /**
   * Deselects all selected items in the receiver's list.
   * <p>
   * Note: To clear the selection in the receiver's text field,
   * use <code>clearSelection()</code>.
   * </p>
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #clearSelection
   */
  public void deselectAll() {
    checkWidget();
    OS.SendMessage(handle, OS.CB_SETCURSEL, -1, 0);
    sendEvent(SWT.Modify);
    // widget could be disposed at this point
  }

//  boolean getEditable() {
//    int bits = OS.GetWindowLong(handle, OS.GWL_STYLE);
//    return(bits & 0x0F) == OS.CBS_DROPDOWNLIST;
//  }

  /**
   * Returns the item at the given, zero-relative index in the
   * receiver's list. Throws an exception if the index is out
   * of range.
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
   * @exception SWTError <ul>
   *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public String getItem(int index) {
    checkWidget();
    if((style & SWT.SIMPLE) != 0) {
      DefaultListModel model = (DefaultListModel)valueList.getModel();
      if(index < 0 || index >= model.size()) {
        error(SWT.ERROR_INVALID_RANGE);
      }
      return (String)model.get(index);
    } else {
      String item = (String)comboBox.getItemAt(index);
      if(item == null) {
        error(SWT.ERROR_INVALID_RANGE);
      }
      return item;
    }
//    int length = OS.SendMessage(handle, OS.CB_GETLBTEXTLEN, index, 0);
//    if(length != OS.CB_ERR) {
//      TCHAR buffer = new TCHAR(getCodePage(), length + 1);
//      int result = OS.SendMessage(handle, OS.CB_GETLBTEXT, index, buffer);
//      if(result != OS.CB_ERR) {
//        return buffer.toString(0, length);
//      }
//    }
//    int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//    if(0 <= index && index < count) {
//      error(SWT.ERROR_CANNOT_GET_ITEM);
//    }
//    error(SWT.ERROR_INVALID_RANGE);
//    return null;
  }

  /**
   * Returns the number of items contained in the receiver's list.
   *
   * @return the number of items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public int getItemCount() {
    checkWidget();
    if((style & SWT.SIMPLE) != 0) {
      return valueList.getModel().getSize();
    } else {
      return comboBox.getItemCount();
    }
  }

  /**
   * Returns the height of the area which would be used to
   * display <em>one</em> of the items in the receiver's list.
   *
   * @return the height of one item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public int getItemHeight() {
    checkWidget();
    int result = OS.SendMessage(handle, OS.CB_GETITEMHEIGHT, 0, 0);
    if(result == OS.CB_ERR) {
      error(SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
    }
    return result;
  }

  /**
   * Returns an array of <code>String</code>s which are the items
   * in the receiver's list.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its list of items, so modifying the array will
   * not affect the receiver.
   * </p>
   *
   * @return the items in the receiver's list
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public String[] getItems() {
    checkWidget();
    if((style & SWT.SIMPLE) != 0) {
      Object[] array = ((DefaultListModel)valueList.getModel()).toArray();
      String[] items = new String[array.length];
      for(int i=0; i<array.length; i++) {
        items[i] = (String)array[i];
      }
      return items;
    } else {
      int count = comboBox.getItemCount();
      String[] items = new String[count];
      for(int i=0; i<count; i++) {
        items[i] = (String)comboBox.getItemAt(i);
      }
      return items;
    }
  }

  String getNameText() {
    return getText();
  }

  /**
   * Returns a <code>Point</code> whose x coordinate is the start
   * of the selection in the receiver's text field, and whose y
   * coordinate is the end of the selection. The returned values
   * are zero-relative. An "empty" selection as indicated by
   * the the x and y coordinates having the same value.
   *
   * @return a point representing the selection start and end
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Point getSelection() {
    checkWidget();
    if((style & SWT.DROP_DOWN) != 0 && (style & SWT.READ_ONLY) != 0) {
      return new Point(0, OS.GetWindowTextLength(handle));
    }
    int[] start = new int[1], end = new int[1];
    OS.SendMessage(handle, OS.CB_GETEDITSEL, start, end);
    return new Point(start[0], end[0]);
  }

  /**
   * Returns the zero-relative index of the item which is currently
   * selected in the receiver's list, or -1 if no item is selected.
   *
   * @return the index of the selected item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getSelectionIndex() {
    checkWidget();
    if((style & SWT.SIMPLE) != 0) {
      return ((DefaultListModel)valueList.getModel()).indexOf(valueField.getText());
    } else {
      return comboBox.getSelectedIndex();
    }
//    if(noSelection) {
//      return -1;
//    }
//    return OS.SendMessage(handle, OS.CB_GETCURSEL, 0, 0);
  }

  /**
   * Returns a string containing a copy of the contents of the
   * receiver's text field.
   *
   * @return the receiver's text
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public String getText() {
    checkWidget();
    if((style & SWT.SIMPLE) != 0) {
      return valueField.getText();
    } else {
      return (String)comboBox.getSelectedItem();
    }
//    int length = OS.GetWindowTextLength(handle);
//    if(length == 0) {
//      return "";
//    }
//    TCHAR buffer = new TCHAR(getCodePage(), length + 1);
//    OS.GetWindowText(handle, buffer, length + 1);
//    return buffer.toString(0, length);
  }

//  String getText(int start, int stop) {
//    /*
//     * NOTE: The current implementation uses substring ()
//     * which can reference a potentially large character
//     * array.
//     */
//    return getText().substring(start, stop - 1);
//  }

  /**
   * Returns the height of the receivers's text field.
   *
   * @return the text height
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public int getTextHeight() {
    checkWidget();
    int result = OS.SendMessage(handle, OS.CB_GETITEMHEIGHT, -1, 0);
    if(result == OS.CB_ERR) {
      error(SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
    }
    return result + 6;
  }

  /**
   * Returns the maximum number of characters that the receiver's
   * text field is capable of holding. If this has not been changed
   * by <code>setTextLimit()</code>, it will be the constant
   * <code>Combo.LIMIT</code>.
   *
   * @return the text limit
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getTextLimit() {
    checkWidget();
    throw new IllegalStateException("Not yet supported");
//    int hwndText = OS.GetDlgItem(handle, CBID_EDIT);
//    if(hwndText == 0) {
//      return LIMIT;
//    }
//    return OS.SendMessage(hwndText, OS.EM_GETLIMITTEXT, 0, 0);
  }

//  boolean hasFocus() {
//    int hwndFocus = OS.GetFocus();
//    if(hwndFocus == handle) {
//      return true;
//    }
//    if(hwndFocus == 0) {
//      return false;
//    }
////    int hwndText = OS.GetDlgItem(handle, CBID_EDIT);
////    if(hwndFocus == hwndText) {
////      return true;
////    }
////    int hwndList = OS.GetDlgItem(handle, CBID_LIST);
////    if(hwndFocus == hwndList) {
////      return true;
////    }
//    return false;
//  }

  /**
   * Searches the receiver's list starting at the first item
   * (index 0) until an item is found that is equal to the
   * argument, and returns the index of that item. If no item
   * is found, returns -1.
   *
   * @param string the search item
   * @return the index of the item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int indexOf(String string) {
    return indexOf(string, 0);
  }

  /**
   * Searches the receiver's list starting at the given,
   * zero-relative index until an item is found that is equal
   * to the argument, and returns the index of that item. If
   * no item is found or the starting index is out of range,
   * returns -1.
   *
   * @param string the search item
   * @return the index of the item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int indexOf(String string, int start) {
    checkWidget();
    if(string == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }

    if((style & SWT.SIMPLE) != 0) {
      DefaultListModel model = (DefaultListModel)valueList.getModel();
      if(start < 0 || start >= model.size()) {
        return -1;
      }
      return ((DefaultListModel)valueList.getModel()).indexOf(string, start);
    } else {
      int count = comboBox.getItemCount();
      if(start < 0 || start >= count) {
        return -1;
      }
      for(int i=start; i<count; i++) {
        if(comboBox.getItemAt(i).equals(string)) {
          return i;
        }
      }
      return -1;
    }
//    if(string.length() == 0) {
//      int count = getItemCount();
//      for(int i = start; i < count; i++) {
//        if(string.equals(getItem(i))) {
//          return i;
//        }
//      }
//      return -1;
//    }
//
//    /* Use CB_FINDSTRINGEXACT to search for the item */
//    int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//    if(!(0 <= start && start < count)) {
//      return -1;
//    }
//    int index = start - 1, last = 0;
//    TCHAR buffer = new TCHAR(getCodePage(), string, true);
//    do {
//      index = OS.SendMessage(handle, OS.CB_FINDSTRINGEXACT, last = index,
//                             buffer);
//      if(index == OS.CB_ERR || index <= last) {
//        return -1;
//      }
//    } while(!string.equals(getItem(index)));
//    return index;
  }

  /**
   * Pastes text from clipboard.
   * <p>
   * The selected text is deleted from the widget
   * and new text inserted from the clipboard.
   * </p>
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 2.1
   */
  public void paste() {
    checkWidget();
    OS.SendMessage(handle, OS.WM_PASTE, 0, 0);
  }

  /**
   * Removes the item from the receiver's list at the given
   * zero-relative index.
   *
   * @param index the index for the item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public void remove(int index) {
    checkWidget();
    if((style & SWT.SIMPLE) != 0) {
      DefaultListModel model = (DefaultListModel)valueList.getModel();
      if(index < 0 || index >= model.size()) {
        error(SWT.ERROR_INVALID_RANGE);
      }
      model.remove(index);
    } else {
      if(index < 0 || index >= comboBox.getItemCount()) {
        error(SWT.ERROR_INVALID_RANGE);
      }
      comboBox.removeItemAt(index);
    }
    sendEvent(SWT.Modify);

//    
//    int length = OS.GetWindowTextLength(handle);
//    int code = OS.SendMessage(handle, OS.CB_DELETESTRING, index, 0);
//    if(code == OS.CB_ERR) {
//      int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//      if(0 <= index && index < count) {
//        error(SWT.ERROR_ITEM_NOT_REMOVED);
//      }
//      error(SWT.ERROR_INVALID_RANGE);
//    }
//    if(length != OS.GetWindowTextLength(handle)) {
//      /*
//       * It is possible (but unlikely), that application
//       * code could have disposed the widget in the modify
//       * event.  If this happens, just return.
//       */
//      sendEvent(SWT.Modify);
//      if(isDisposed()) {
//        return;
//      }
//    }
//    /*
//     * Bug in Windows.  When the combo box is read only
//     * with exactly one item that is currently selected
//     * and that item is removed, the combo box does not
//     * redraw to clear the text area.  The fix is to
//     * force a redraw.
//     */
//    if((style & SWT.READ_ONLY) != 0) {
//      int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//      if(count == 0) {
//        OS.InvalidateRect(handle, null, false);
//      }
//    }
  }

  /**
   * Removes the items from the receiver's list which are
   * between the given zero-relative start and end
   * indices (inclusive).
   *
   * @param start the start of the range
   * @param end the end of the range
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public void remove(int start, int end) {
    checkWidget();
    if(start > end) {
      return;
    }
    if((style & SWT.SIMPLE) != 0) {
      DefaultListModel model = (DefaultListModel)valueList.getModel();
      if(start < 0 || end >= model.size() || start > end) {
        error(SWT.ERROR_INVALID_RANGE);
      }
      model.removeRange(start, end);
    } else {
      if(start < 0 || end >= comboBox.getItemCount() || start > end) {
        error(SWT.ERROR_INVALID_RANGE);
      }
      for(int i=end; i>=start; i--) {
        comboBox.removeItemAt(i);
      }
    }
    sendEvent(SWT.Modify);
    
//    int length = OS.GetWindowTextLength(handle);
//    for(int i = start; i <= end; i++) {
//      int result = OS.SendMessage(handle, OS.CB_DELETESTRING, start, 0);
//      if(result == OS.CB_ERR) {
//        int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//        if(0 <= i && i < count) {
//          error(SWT.ERROR_ITEM_NOT_REMOVED);
//        }
//        error(SWT.ERROR_INVALID_RANGE);
//      }
//    }
//    if(length != OS.GetWindowTextLength(handle)) {
//      /*
//       * It is possible (but unlikely), that application
//       * code could have disposed the widget in the modify
//       * event.  If this happens, just return.
//       */
//      sendEvent(SWT.Modify);
//      if(isDisposed()) {
//        return;
//      }
//    }
//    /*
//     * Bug in Windows.  When the combo box is read only
//     * with exactly one item that is currently selected
//     * and that item is removed, the combo box does not
//     * redraw to clear the text area.  The fix is to
//     * force a redraw.
//     */
//    if((style & SWT.READ_ONLY) != 0) {
//      int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//      if(count == 0) {
//        OS.InvalidateRect(handle, null, false);
//      }
//    }
  }

  /**
   * Searches the receiver's list starting at the first item
   * until an item is found that is equal to the argument,
   * and removes that item from the list.
   *
   * @param string the item to remove
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
       *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public void remove(String string) {
    int index = indexOf(string, 0);
    if(index == -1) {
      error(SWT.ERROR_INVALID_ARGUMENT);
    }
    remove(index);
  }

  /**
   * Removes all of the items from the receiver's list.
   * <p>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void removeAll() {
    checkWidget();
    if((style & SWT.SIMPLE) != 0) {
      ((DefaultListModel)valueList.getModel()).removeAllElements();
    } else {
      comboBox.removeAllItems();
    }
    sendEvent(SWT.Modify);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the receiver's text is modified.
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
   * @see ModifyListener
   * @see #addModifyListener
   */
  public void removeModifyListener(ModifyListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Modify, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the receiver's selection changes.
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

  /**
   * Selects the item at the given zero-relative index in the receiver's
   * list. If the item at the index was already selected, it remains
   * selected. Indices that are out of range are ignored.
   *
   * @param index the index of the item to select
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void select(int index) {
    checkWidget();
    if((style & SWT.SIMPLE) != 0) {
      if(index >= 0 && index < valueList.getModel().getSize() && valueList.getSelectedIndex() != index) {
        valueList.setSelectedIndex(index);
        sendEvent(SWT.Modify);
      }
    } else {
      if(index > 0 && index < comboBox.getItemCount() && comboBox.getSelectedIndex() != index) {
        comboBox.setSelectedIndex(index);
        sendEvent(SWT.Modify);
      }
    }
//    int count = OS.SendMessage(handle, OS.CB_GETCOUNT, 0, 0);
//    if(0 <= index && index < count) {
//      int selection = OS.SendMessage(handle, OS.CB_GETCURSEL, 0, 0);
//      int code = OS.SendMessage(handle, OS.CB_SETCURSEL, index, 0);
//      if(code != OS.CB_ERR && code != selection) {
//        sendEvent(SWT.Modify);
//        // widget could be disposed at this point
//      }
//    }
  }

//  void setBackgroundPixel(int pixel) {
//    if(background == pixel) {
//      return;
//    }
//    super.setBackgroundPixel(pixel);
//    int hwndText = OS.GetDlgItem(handle, CBID_EDIT);
//    if(hwndText != 0) {
//      OS.InvalidateRect(hwndText, null, true);
//    }
//    int hwndList = OS.GetDlgItem(handle, CBID_LIST);
//    if(hwndList != 0) {
//      OS.InvalidateRect(hwndList, null, true);
//    }
//  }

//  void setBounds(int x, int y, int width, int height, int flags) {
//    /*
//     * Feature in Windows.  If the combo box has the CBS_DROPDOWN
//     * or CBS_DROPDOWNLIST style, Windows uses the height that the
//     * programmer sets in SetWindowPos () to control height of the
//     * drop down list.  When the width is non-zero, Windows remembers
//     * this value and sets the height to be the height of the text
//     * field part of the combo box.  If the width is zero, Windows
//     * allows the height to have any value.  Therefore, when the
//     * programmer sets and then queries the height, the values can
//     * be different depending on the width.  The problem occurs when
//     * the programmer uses computeSize () to determine the preferred
//     * height (always the height of the text field) and then uses
//     * this value to set the height of the combo box.  The result
//     * is a combo box with a zero size drop down list.  The fix, is
//     * to always set the height to show a fixed number of combo box
//     * items and ignore the height value that the programmer supplies.
//     */
//    if((style & SWT.DROP_DOWN) != 0) {
//      int textHeight = OS.SendMessage(handle, OS.CB_GETITEMHEIGHT, -1, 0);
//      int itemHeight = OS.SendMessage(handle, OS.CB_GETITEMHEIGHT, 0, 0);
//      height = textHeight + 6 + (itemHeight * 5) + 2;
//      /*
//       * Feature in Windows.  When a drop down combo box is resized,
//       * the combo box resizes the height of the text field and uses
//       * the height provided in SetWindowPos () to determine the height
//       * of the drop down list.  For some reason, the combo box redraws
//       * the whole area, not just the text field.  The fix is to set the
//       * SWP_NOSIZE bits when the height of text field and the drop down
//       * list is the same as the requested height.
//       *
//       * NOTE:  Setting the width of a combo box to zero does not update
//       * the width of the drop down control rect.  If the width of the
//       * combo box is zero, then do not set SWP_NOSIZE.
//       */
//      RECT rect = new RECT();
//      OS.GetWindowRect(handle, rect);
//      if(rect.right - rect.left != 0) {
//        if(OS.SendMessage(handle, OS.CB_GETDROPPEDCONTROLRECT, 0, rect) != 0) {
//          int oldWidth = rect.right - rect.left,
//              oldHeight = rect.bottom - rect.top;
//          if(oldWidth == width && oldHeight == height) {
//            flags |= OS.SWP_NOSIZE;
//          }
//        }
//      }
//      OS.SetWindowPos(handle, 0, x, y, width, height, flags);
//      return;
//    }
//
//    /*
//     * Bug in Windows.  If the combo box has the CBS_SIMPLE style,
//     * the list portion of the combo box is not redrawn when the
//     * combo box is resized.  The fix is to force a redraw when
//     * the size has changed.
//     */
//    if(parent.lpwp != null || (flags & OS.SWP_NOSIZE) != 0 ||
//       !OS.IsWindowVisible(handle)) {
//      super.setBounds(x, y, width, height, flags);
//      return;
//    }
//    RECT rect = new RECT();
//    OS.GetWindowRect(handle, rect);
//    super.setBounds(x, y, width, height, flags);
//    int oldWidth = rect.right - rect.left, oldHeight = rect.bottom - rect.top;
//    if(oldWidth != width || oldHeight != height) {
//      if(OS.IsWinCE) {
//        int hwndText = OS.GetDlgItem(handle, CBID_EDIT);
//        if(hwndText != 0) {
//          OS.InvalidateRect(hwndText, null, true);
//        }
//        int hwndList = OS.GetDlgItem(handle, CBID_LIST);
//        if(hwndList != 0) {
//          OS.InvalidateRect(hwndList, null, true);
//        }
//      } else {
//        int uFlags = OS.RDW_ERASE | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
//        OS.RedrawWindow(handle, null, 0, uFlags);
//      }
//    }
//  }

//  void setEditable(boolean editable) {
//    error(SWT.ERROR_NOT_IMPLEMENTED);
//  }

//  void setForegroundPixel(int pixel) {
//    if(foreground == pixel) {
//      return;
//    }
//    super.setForegroundPixel(pixel);
//    int hwndText = OS.GetDlgItem(handle, CBID_EDIT);
//    if(hwndText != 0) {
//      OS.InvalidateRect(hwndText, null, true);
//    }
//    int hwndList = OS.GetDlgItem(handle, CBID_LIST);
//    if(hwndList != 0) {
//      OS.InvalidateRect(hwndList, null, true);
//    }
//  }

  /**
   * Sets the text of the item in the receiver's list at the given
   * zero-relative index to the string argument. This is equivalent
   * to <code>remove</code>'ing the old item at the index, and then
   * <code>add</code>'ing the new item at that index.
   *
   * @param index the index for the item
   * @param string the new text for the item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_ITEM_NOT_REMOVED - if the remove operation fails because of an operating system failure</li>
   *    <li>ERROR_ITEM_NOT_ADDED - if the add operation fails because of an operating system failure</li>
   * </ul>
   */
  public void setItem(int index, String string) {
    checkWidget();
    if(string == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    remove(index);
    /*
     * It is possible (but unlikely), that application
     * code could have disposed the widget in the modify
     * event that might be sent when the index is removed.
     * If this happens, just exit.
     */
    if(isDisposed()) {
      return;
    }
    add(string, index);
  }

  /**
   * Sets the receiver's list to be the given array of items.
   *
   * @param items the array of items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public void setItems(String[] items) {
    checkWidget();
    if(items == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if((style & SWT.SIMPLE) != 0) {
      valueList.setListData(items);
    } else {
      comboBox.setModel(new DefaultComboBoxModel(items));
    }
    // widget could be disposed at this point
    sendEvent(SWT.Modify);
  }
  
  public void setSize(int width, int height) {
    if((style & SWT.SIMPLE) != 0) {
      super.setSize(width, height);
    } else {
      super.setSize(width, comboBox.getPreferredSize().height);
    }
  }
  
  boolean overrideLayout() {
    return false;
  }

  /**
   * Sets the selection in the receiver's text field to the
   * range specified by the argument whose x coordinate is the
   * start of the selection and whose y coordinate is the end
   * of the selection.
   *
   * @param a point representing the new selection start and end
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSelection(Point selection) {
    checkWidget();
    if(selection == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    int bits = selection.x | (selection.y << 16);
    OS.SendMessage(handle, OS.CB_SETEDITSEL, 0, bits);
  }

  /**
   * Sets the contents of the receiver's text field to the
   * given string.
   * <p>
   * Note: The text field in a <code>Combo</code> is typically
   * only capable of displaying a single line of text. Thus,
   * setting the text to a string containing line breaks or
   * other special characters will probably cause it to
   * display incorrectly.
   * </p>
   *
   * @param text the new text
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setText(String string) {
    checkWidget();
    if(string == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
//    if((style & SWT.READ_ONLY) != 0) {
//      int index = indexOf(string);
//      if(index != -1) {
//        select(index);
//      }
//      return;
//    }
    String oldString = null;
    if((style & SWT.SIMPLE) != 0) {
      oldString = valueField.getText();
      valueField.setText(string);
    } else {
      oldString = (String)comboBox.getSelectedItem();
      comboBox.setSelectedItem(string);
    }
    if(!string.equals(oldString)) {
      sendEvent(SWT.Modify);
    }
  }

  /**
   * Sets the maximum number of characters that the receiver's
   * text field is capable of holding to be the argument.
   *
   * @param limit new text limit
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setTextLimit(int limit) {
    checkWidget();
    if(limit == 0) {
      error(SWT.ERROR_CANNOT_BE_ZERO);
    }
    OS.SendMessage(handle, OS.CB_LIMITTEXT, limit, 0);
  }

  boolean translateAccelerator(MSG msg) {
    if(super.translateAccelerator(msg)) {
      return true;
    }

//    /*
//     * In order to see key events for the text widget in a combo box,
//     * filter the key events before they are dispatched to the text
//     * widget and invoke the cooresponding key handler for the combo
//     * box as if the key was sent directly to the combo box, not the
//     * text field.  The key is still dispatched to the text widget,
//     * in the normal fashion.  Note that we must call TranslateMessage
//     * in order to process accented keys properly.
//     */
//    int hwndText = OS.GetDlgItem(handle, CBID_EDIT);
//    if(hwndText != 0 && msg.hwnd == hwndText) {
//      switch(msg.message) {
//        case OS.WM_CHAR:
//        case OS.WM_SYSCHAR:
//        case OS.WM_KEYDOWN: {
//          Display display = getDisplay();
//          if(msg.message == OS.WM_KEYDOWN) {
//            if(display.translateTraversal(msg, this)) {
//              return true;
//            }
//          } else {
//            if(display.translateMnemonic(msg, this)) {
//              return true;
//            }
//          }
//        }
//      }
//      OS.TranslateMessage(msg);
//      switch(msg.message) {
//        case OS.WM_CHAR:
//          WM_CHAR(msg.wParam, msg.lParam);
//          break;
//        case OS.WM_IME_CHAR:
//          WM_IME_CHAR(msg.wParam, msg.lParam);
//          break;
//        case OS.WM_KEYDOWN:
//          WM_KEYDOWN(msg.wParam, msg.lParam);
//          break;
//        case OS.WM_KEYUP:
//          WM_KEYUP(msg.wParam, msg.lParam);
//          break;
//        case OS.WM_SYSCHAR:
//          WM_SYSCHAR(msg.wParam, msg.lParam);
//          break;
//        case OS.WM_SYSKEYDOWN:
//          WM_SYSKEYDOWN(msg.wParam, msg.lParam);
//          break;
//        case OS.WM_SYSKEYUP:
//          WM_SYSKEYUP(msg.wParam, msg.lParam);
//          break;
//      }
//      OS.DispatchMessage(msg);
//      return true;
//    }
    return false;
  }

  boolean translateTraversal(MSG msg) {
    /*
     * Feature in Windows.  For some reason, when the
     * user presses tab, return or escape, Windows beeps.
     * The fix is to look for these keys and not call
     * the window proc.
     */
//    int hwndText = OS.GetDlgItem(handle, CBID_EDIT);
//    if(hwndText != 0 && msg.hwnd == hwndText) {
//      switch(msg.wParam) {
//        case OS.VK_ESCAPE:
//
//          /* Allow the escape key to close the combo box */
//          if(OS.SendMessage(handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0) {
//            return false;
//          }
//          // FALL THROUGH
//        case OS.VK_TAB:
//        case OS.VK_RETURN:
//          boolean translated = super.translateTraversal(msg);
//          if(!translated) {
//            if(sendKeyEvent(SWT.KeyDown, msg.message, msg.wParam, msg.lParam)) {
//              if(msg.wParam == OS.VK_RETURN) {
//                sendEvent(SWT.DefaultSelection);
//                // widget could be disposed at this point
//              }
//            }
//          }
//          return true;
//      }
//    }
    return super.translateTraversal(msg);
  }

  boolean traverseEscape() {
    if(OS.SendMessage(handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0) {
      OS.SendMessage(handle, OS.CB_SHOWDROPDOWN, 0, 0);
      return true;
    }
    return super.traverseEscape();
  }

  int widgetExtStyle() {
    return super.widgetExtStyle() & ~OS.WS_EX_NOINHERITLAYOUT;
  }

  int widgetStyle() {
    int bits = super.widgetStyle() | OS.CBS_AUTOHSCROLL |
        OS.CBS_NOINTEGRALHEIGHT | OS.WS_VSCROLL;
    if((style & SWT.SIMPLE) != 0) {
      return bits | OS.CBS_SIMPLE;
    }
    if((style & SWT.READ_ONLY) != 0) {
      return bits | OS.CBS_DROPDOWNLIST;
    }
    return bits | OS.CBS_DROPDOWN;
  }

  LRESULT WM_CHAR(int wParam, int lParam) {
    LRESULT result = super.WM_CHAR(wParam, lParam);
    if(result != null) {
      return result;
    }
    if(wParam == OS.VK_RETURN) {
      postEvent(SWT.DefaultSelection);
    }
    return result;
  }

  LRESULT WM_CTLCOLOR(int wParam, int lParam) {
    return wmColorChild(wParam, lParam);
  }

  LRESULT WM_GETDLGCODE(int wParam, int lParam) {
    int code = callWindowProc(OS.WM_GETDLGCODE, wParam, lParam);
    return new LRESULT(code | OS.DLGC_WANTARROWS);
  }

  LRESULT WM_KILLFOCUS(int wParam, int lParam) {
    /*
     * Return NULL - Focus notification is
     * done in WM_COMMAND by CBN_KILLFOCUS.
     */
    return null;
  }

  LRESULT WM_SETFOCUS(int wParam, int lParam) {
    /*
     * Return NULL - Focus notification is
     * done by WM_COMMAND with CBN_SETFOCUS.
     */
    return null;
  }

  LRESULT WM_SIZE(int wParam, int lParam) {
    /*
     * Feature in Windows.  When an editable drop down combo box
     * contains text that does not correspond to an item in the
     * list, when the widget is resized, it selects the closest
     * match from the list.  The fix is to remember the original
     * text and reset it after the widget is resized.
     */
    if((style & SWT.READ_ONLY) != 0 || (style & SWT.DROP_DOWN) == 0) {
      return super.WM_SIZE(wParam, lParam);
    }
    int index = OS.SendMessage(handle, OS.CB_GETCURSEL, 0, 0);
    boolean redraw = false;
    TCHAR buffer = null;
    int[] start = null, end = null;
    if(index == OS.CB_ERR) {
      int length = OS.GetWindowTextLength(handle);
      if(length != 0) {
        buffer = new TCHAR(getCodePage(), length + 1);
        OS.GetWindowText(handle, buffer, length + 1);
        start = new int[1];
        end = new int[1];
        OS.SendMessage(handle, OS.CB_GETEDITSEL, start, end);
        redraw = drawCount == 0 && OS.IsWindowVisible(handle);
        if(redraw) {
          setRedraw(false);
        }
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
    if(buffer != null) {
      OS.SetWindowText(handle, buffer);
      int bits = start[0] | (end[0] << 16);
      OS.SendMessage(handle, OS.CB_SETEDITSEL, 0, bits);
      if(redraw) {
        setRedraw(true);
      }
    }
    return result;
  }

  LRESULT wmCommandChild(int wParam, int lParam) {
    int code = wParam >> 16;
    switch(code) {
      case OS.CBN_EDITCHANGE:

        /*
         * Feature in Windows.  If the combo box list selection is
         * queried using CB_GETCURSEL before the WM_COMMAND (with
         * CBM_EDITCHANGE) returns, CB_GETCURSEL returns the previous
         * selection in the list.  It seems that the combo box sends
         * the WM_COMMAND before it makes the selection in the list box
         * match the entry field.  The fix is remember that no selection
         * in the list should exist in this case.
         */
        noSelection = true;
        /*
         * It is possible (but unlikely), that application
         * code could have disposed the widget in the modify
         * event.  If this happens, end the processing of the
         * Windows message by returning zero as the result of
         * the window proc.
         */
        sendEvent(SWT.Modify);
        if(isDisposed()) {
          return LRESULT.ZERO;
        }
        noSelection = false;
        break;
      case OS.CBN_SELCHANGE:

        /*
         * Feature in Windows.  If the text in an editable combo box
         * is queried using GetWindowText () before the WM_COMMAND
         * (with CBM_SELCHANGE) returns, GetWindowText () returns is
         * the previous text in the combo box.  It seems that the combo
         * box sends the WM_COMMAND before it updates the text field to
         * match the list selection.  The fix is to force the text field
         * to match the list selection by re-selecting the list item.
         */
        int index = OS.SendMessage(handle, OS.CB_GETCURSEL, 0, 0);
        if(index != OS.CB_ERR) {
          OS.SendMessage(handle, OS.CB_SETCURSEL, index, 0);
          /*
           * It is possible (but unlikely), that application
           * code could have disposed the widget in the modify
           * event.  If this happens, end the processing of the
           * Windows message by returning zero as the result of
           * the window proc.
           */
        }
        sendEvent(SWT.Modify);
        if(isDisposed()) {
          return LRESULT.ZERO;
        }
        postEvent(SWT.Selection);
        break;
      case OS.CBN_SETFOCUS:
      case OS.CBN_KILLFOCUS:

        /*
         * It is possible (but unlikely), that application
         * code could have disposed the widget in the focus
         * event.  If this happens, end the processing of the
         * Windows message by returning zero as the result of
         * the window proc.
         */
        sendEvent(code == OS.CBN_SETFOCUS ? SWT.FocusIn : SWT.FocusOut);
        if(isDisposed()) {
          return LRESULT.ZERO;
        }
        break;
    }
    return super.wmCommandChild(wParam, lParam);
  }

}
