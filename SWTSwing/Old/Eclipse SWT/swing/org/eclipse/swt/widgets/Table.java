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
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.swing.ExtJScrollPane;
import org.eclipse.swt.internal.swing.LayeredContainer;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class implement a selectable user interface
 * object that displays a list of images and strings and issue
 * notificiation when selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class Table extends Composite {
  TableItem[] items;
  TableColumn[] columns;
  ImageList imageList;
  boolean ignoreSelect, dragStarted, ignoreResize, mouseDown, customDraw;

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
   * @see SWT#SINGLE
   * @see SWT#MULTI
   * @see SWT#CHECK
   * @see SWT#FULL_SELECTION
   * @see SWT#HIDE_SELECTION
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public Table(Composite parent, int style) {
    super(parent, checkStyle(style));
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the receiver's selection changes, by sending
   * it one of the messages defined in the <code>SelectionListener</code>
   * interface.
   * <p>
   * When <code>widgetSelected</code> is called, the item field of the event object is valid.
   * If the reciever has <code>SWT.CHECK</code> style set and the check selection changes,
   * the event object detail field contains the value <code>SWT.CHECK</code>.
   * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
   * The item field of the event object is valid for default selection, but the detail field is not used.
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
    /*
     * Feature in Windows.  It is not possible to create
     * a table that does not have scroll bars.  Therefore,
     * no matter what style bits are specified, set the
     * H_SCROLL and V_SCROLL bits so that the SWT style
     * will match the widget that Windows creates.
     */
    style |= SWT.H_SCROLL | SWT.V_SCROLL;
    return checkBits(style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
  }

  protected void checkSubclass() {
    if(!isValidSubclass()) {
      error(SWT.ERROR_INVALID_SUBCLASS);
    }
  }

//  public Point computeSize(int wHint, int hHint, boolean changed) {
//    checkWidget();
//    int bits = 0;
//    if(wHint != SWT.DEFAULT) {
//      bits |= wHint & 0xFFFF;
//    } else {
//      int width = 0;
//      int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
//      int count = OS.SendMessage(hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//      for(int i = 0; i < count; i++) {
//        width += OS.SendMessage(handle, OS.LVM_GETCOLUMNWIDTH, i, 0);
//      }
//      bits |= width & 0xFFFF;
//    }
//    if(hHint != SWT.DEFAULT) {
//      bits |= hHint << 16;
//    }
//    int result = OS.SendMessage(handle, OS.LVM_APPROXIMATEVIEWRECT, -1, bits);
//    int width = result & 0xFFFF, height = result >> 16;
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
//    int border = getBorderWidth();
//    width += border * 2;
//    height += border * 2;
//    /*
//     * Feature in Windows.  For some reason, LVM_APPROXIMATEVIEWRECT
//     * does not include the space for the vertical scroll bar but does
//     * take into account the horizontal scroll bar when calculating the
//     * space needed to show the items.  The fix is to add in this space.
//     */
//    if((style & SWT.V_SCROLL) != 0) {
//      width += OS.GetSystemMetrics(OS.SM_CXVSCROLL);
//    }
//    if(((style & SWT.H_SCROLL) != 0) && (hHint != SWT.DEFAULT)) {
//      height += OS.GetSystemMetrics(OS.SM_CYHSCROLL);
//    }
//    return new Point(width, height);
//  }

  void createHandle() {
    super.createHandle();
    state &= ~CANVAS;

    /*
     * This code is intentionally commented.  According to
     * the documentation, setting the default item size is
     * supposed to improve performance.  By experimentation,
     * this does not seem to have much of an effect.
     */
//	OS.SendMessage (handle, OS.LVM_SETITEMCOUNT, 1024 * 2, 0);

//    /* Set the checkbox image list */
//    if((style & SWT.CHECK) != 0) {
//      int empty = OS.SendMessage(handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
//      int oneItem = OS.SendMessage(handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
//      int width = (oneItem >> 16) - (empty >> 16), height = width;
//      setCheckboxImageList(width, height);
//    }

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

    /*
     * Bug in Windows.  When the first column is inserted
     * without setting the header text, Windows will never
     * allow the header text for the first column to be set.
     * The fix is to set the text to an empty string when
     * the column is inserted.
     */
//    LVCOLUMN lvColumn = new LVCOLUMN();
//    lvColumn.mask = OS.LVCF_TEXT;
//    int hHeap = OS.GetProcessHeap();
//    int pszText = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
//    lvColumn.pszText = pszText;
//    OS.SendMessage(handle, OS.LVM_INSERTCOLUMN, 0, lvColumn);
//    OS.HeapFree(hHeap, 0, pszText);
//
//    /* Set the extended style bits */
//    int bits = OS.LVS_EX_SUBITEMIMAGES | OS.LVS_EX_LABELTIP;
//    if((style & SWT.FULL_SELECTION) != 0) {
//      bits |= OS.LVS_EX_FULLROWSELECT;
//    }
//    OS.SendMessage(handle, OS.LVM_SETEXTENDEDLISTVIEWSTYLE, bits, bits);
//
//    /*
//     * Feature in Windows.  Windows does not explicitly set the orientation of
//     * the header.  Instead, the orientation is inherited when WS_EX_LAYOUTRTL
//     * is specified for the table.  This means that when both WS_EX_LAYOUTRTL
//     * and WS_EX_NOINHERITLAYOUT are specified for the table, the header will
//     * not be oriented correctly.  The fix is to explicitly set the orientation
//     * for the header.
//     *
//     * NOTE: WS_EX_LAYOUTRTL is not supported on Windows NT.
//     */
//    if((OS.WIN32_MAJOR << 16 | OS.WIN32_MINOR) < (4 << 16 | 10)) {
//      return;
//    }
//    if((style & SWT.RIGHT_TO_LEFT) != 0) {
//      int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
//      int extStyle = OS.GetWindowLong(hwndHeader, OS.GWL_EXSTYLE);
//      OS.SetWindowLong(hwndHeader, OS.GWL_EXSTYLE,
//                       extStyle | OS.WS_EX_LAYOUTRTL);
//    }
  }

  void createItem(TableColumn column, int index) {
    int columnCount = table.getColumnCount();
    if(columnCount == 1 && columns[0] == null) {
      columnCount = 0;
    }
    if(!(0 <= index && index <= columnCount)) {
      error(SWT.ERROR_INVALID_RANGE);
    }
    if(columnCount == columns.length) {
      TableColumn[] newColumns = new TableColumn[columns.length + 4];
      System.arraycopy(columns, 0, newColumns, 0, columns.length);
      columns = newColumns;
    }

    System.arraycopy(columns, index, columns, index + 1, columnCount - index);
    columns[index] = column;

    int[] widths = new int[columnCount];
    for(int i=0; i<widths.length; i++) {
      widths[i] = table.getColumnModel().getColumn(i).getPreferredWidth();
    }
    ((DefaultTableModel)table.getModel()).addColumn(new javax.swing.table.TableColumn(index));

    for(int i=0; i<widths.length; i++) {
      int i2 = index<=i? i+1:i;
      table.getColumnModel().getColumn(i2).setPreferredWidth(widths[i]);
    }

    int rowCount = table.getRowCount();
    for(int i=columnCount-1; i>=index; i--) {
      for(int j=0; j<rowCount; j++) {
        table.setValueAt(table.getValueAt(j, i), j, i+1);
      }
    }
    
//    if(index == 0) {
//      if(count > 0) {
//        LVCOLUMN lvColumn = new LVCOLUMN();
//        lvColumn.mask = OS.LVCF_WIDTH;
//        OS.SendMessage(handle, OS.LVM_INSERTCOLUMN, 1, lvColumn);
//        OS.SendMessage(handle, OS.LVM_GETCOLUMN, 1, lvColumn);
//        int width = lvColumn.cx;
//        int cchTextMax = 1024;
//        int hHeap = OS.GetProcessHeap();
//        int byteCount = cchTextMax * TCHAR.sizeof;
//        int pszText = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//        LVITEM lvItem = new LVITEM();
//        lvItem.mask = OS.LVIF_TEXT | OS.LVIF_IMAGE | OS.LVIF_STATE;
//        int itemCount = OS.SendMessage(handle, OS.LVM_GETITEMCOUNT, 0, 0);
//        for(int i = 0; i < itemCount; i++) {
//          lvItem.iItem = i;
//          lvItem.iSubItem = 0;
//          lvItem.pszText = pszText;
//          lvItem.cchTextMax = cchTextMax;
//          OS.SendMessage(handle, OS.LVM_GETITEM, 0, lvItem);
//          lvItem.iSubItem = 1;
//          OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
//          lvItem.iSubItem = 0;
//          lvItem.pszText = lvItem.cchTextMax = 0;
//          lvItem.iImage = OS.I_IMAGENONE;
//          OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
//          items[i].text = ""; //$NON-NLS-1$
//          items[i].image = null;
//        }
//        lvColumn.mask = OS.LVCF_TEXT | OS.LVCF_IMAGE | OS.LVCF_WIDTH |
//            OS.LVCF_FMT;
//        lvColumn.pszText = pszText;
//        lvColumn.cchTextMax = cchTextMax;
//        OS.SendMessage(handle, OS.LVM_GETCOLUMN, 0, lvColumn);
//        OS.SendMessage(handle, OS.LVM_SETCOLUMN, 1, lvColumn);
//        lvColumn.fmt = OS.LVCFMT_IMAGE;
//        lvColumn.cx = width;
//        lvColumn.iImage = OS.I_IMAGENONE;
//        lvColumn.pszText = lvColumn.cchTextMax = 0;
//        OS.SendMessage(handle, OS.LVM_SETCOLUMN, 0, lvColumn);
//        lvColumn.mask = OS.LVCF_FMT;
//        lvColumn.fmt = OS.LVCFMT_LEFT;
//        OS.SendMessage(handle, OS.LVM_SETCOLUMN, 0, lvColumn);
//        if(pszText != 0) {
//          OS.HeapFree(hHeap, 0, pszText);
//        }
//      }
//    } else {
//      int fmt = OS.LVCFMT_LEFT;
//      if((column.style & SWT.CENTER) == SWT.CENTER) {
//        fmt = OS.LVCFMT_CENTER;
//      }
//      if((column.style & SWT.RIGHT) == SWT.RIGHT) {
//        fmt = OS.LVCFMT_RIGHT;
//      }
//      LVCOLUMN lvColumn = new LVCOLUMN();
//      lvColumn.mask = OS.LVCF_WIDTH | OS.LVCF_FMT;
//      lvColumn.fmt = fmt;
//      OS.SendMessage(handle, OS.LVM_INSERTCOLUMN, index, lvColumn);
//    }
  }

  void createItem(TableItem item, int index) {
    item.foreground = item.background = -1;
    int count = table.getRowCount();
    if(!(0 <= index && index <= count)) {
      error(SWT.ERROR_INVALID_RANGE);
    }
    if(count == items.length) {
      TableItem[] newItems = new TableItem[items.length + 4];
      System.arraycopy(items, 0, newItems, 0, items.length);
      items = newItems;
    }
    ((DefaultTableModel)table.getModel()).insertRow(index, (Object[])null);
    table.setValueAt(item, index, 0); 

//    
//    LVITEM lvItem = new LVITEM();
//    lvItem.iItem = index;
//
//    /*
//     * Bug in Windows.  Despite the fact that the image list
//     * index has never been set for the item, Windows always
//     * assumes that the image index for the item is valid.
//     * When an item is inserted, the image index is zero.
//     * Therefore, when the first image is inserted and is
//     * assigned image index zero, every item draws with this
//     * image.  The fix is to set the image index to none when
//     * the image is created.
//     */
//    lvItem.iImage = OS.I_IMAGENONE;
//    lvItem.mask = OS.LVIF_IMAGE;
//
//    /* Set the initial unchecked state */
//    if((style & SWT.CHECK) != 0) {
//      lvItem.mask = lvItem.mask | OS.TVIF_STATE;
//      lvItem.state = 1 << 12;
//      lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
//    }
//
//    /* Insert the item */
//    ignoreSelect = true;
//    int result = OS.SendMessage(handle, OS.LVM_INSERTITEM, 0, lvItem);
//    ignoreSelect = false;
//    if(result == -1) {
//      error(SWT.ERROR_ITEM_NOT_ADDED);
//    }
    System.arraycopy(items, index, items, index + 1, count - index);
    items[index] = item;
  }

  void createWidget() {
    super.createWidget();
    items = new TableItem[4];
    columns = new TableColumn[4];
  }

  JTable table;
  
  JScrollPane scrollPane;
  
  JScrollBar getHorizontalScrollBar() {
    return scrollPane.getHorizontalScrollBar();
  }

  JScrollBar getVerticalScrollBar() {
    return scrollPane.getVerticalScrollBar();
  }
  
  boolean isSelectable = true;
  
  Container getNewHandle() {
    table = new JTable(new DefaultTableModel() {
      public String getColumnName(int col) {
        if(col == 0 && columns[0] == null) {
          return " ";
        }
        return columns[col].getText();
      }
      public boolean isCellEditable(int row, int col) {
        return (style & SWT.CHECK) != 0 && col == 0;
      }
      public void setValueAt(Object value, int row, int col) {
        if(table.getRowCount() == row) {
          addRow((Object[])null);
        }
        super.setValueAt(value, row, col);
      }
    }) {
      public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        if(isSelectable) {
          super.changeSelection(rowIndex, columnIndex, toggle, extend);
        }
      } 
      public TableCellRenderer getCellRenderer(int row, int column) {
        return new DefaultTableCellRenderer() {
          public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int alignment = 0;
            TableColumn tableColumn = columns[table.convertColumnIndexToView(column)];
            if(tableColumn != null) {
              int columnStyle = tableColumn.style;
              if((columnStyle & SWT.LEFT) != 0) {
                alignment = SwingConstants.LEFT;
              } else if((columnStyle & SWT.CENTER) != 0) {
                alignment = SwingConstants.CENTER;
              } else if((columnStyle & SWT.RIGHT) != 0) {
                alignment = SwingConstants.RIGHT;
              }
            }
            if(value != null && column == table.convertColumnIndexToView(0)) {
              TableItem item = (TableItem)value;
              if((style & SWT.CHECK) != 0) {
                JCheckBox checkBox = new JCheckBox();
                final boolean isGrayed = item.getGrayed();
                checkBox.setModel(new javax.swing.JToggleButton.ToggleButtonModel() {
                  public boolean isPressed() {
                    return isGrayed? true: super.isPressed();
                  }
                  public boolean isArmed() {
                    return isGrayed? true: super.isArmed();
                  }
                });
                if(isSelected) {
                  checkBox.setForeground(table.getSelectionForeground());
                  checkBox.setBackground(table.getSelectionBackground());
                }
                else {
                  checkBox.setForeground(table.getForeground());
                  checkBox.setBackground(table.getBackground());
                }
                checkBox.setSelected(item.getChecked());
                checkBox.setFont(table.getFont());
                // To be consistent with the editor.
                JLabel label = new JLabel(item.getText());
                Image image = item.getImage();
                if(image != null) {
                  // TODO: handle setImageIndent
                  label.setIcon(new javax.swing.ImageIcon(image.swingHandle));
                }
                label.setFont(table.getFont());
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(checkBox, BorderLayout.WEST);
                panel.add(label, BorderLayout.CENTER);
                if(isSelected) {
                  panel.setForeground(table.getSelectionForeground());
                  panel.setBackground(table.getSelectionBackground());
                }
                else {
                  panel.setForeground(table.getForeground());
                  panel.setBackground(table.getBackground());
                }
                return panel;
              } else {
                JLabel c = (JLabel)super.getTableCellRendererComponent(table, item.getText(), isSelected, false, row, column);
                Image image = item.getImage();
                if(image != null) {
                  // TODO: handle setImageIndent
                  c.setIcon(new javax.swing.ImageIcon(image.swingHandle));
                }
                c.setHorizontalAlignment(alignment);
                return c;
              }
            }
            JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            Object item = table.getValueAt(row, 0);
            if(item != null) {
              Image image = ((TableItem)item).getImage();
              if(image != null) {
                // TODO: handle setImageIndent
                c.setIcon(new javax.swing.ImageIcon(image.swingHandle));
              }
            }
            c.setHorizontalAlignment(alignment);
            return c;
          }
        };
      }
      public TableCellEditor getCellEditor(int row, int column) {
        if(column != table.convertColumnIndexToView(0) || (style & SWT.CHECK) == 0) {
          return super.getCellEditor(row, column);
        }
        final TableItem item = (TableItem)table.getValueAt(row, column);
        return new TableCellEditor() {
          protected ChangeEvent changeEvent = null;
          protected EventListenerList listenerList = new EventListenerList();
          public boolean isCellEditable(EventObject anEvent) {
            // TODO: Kind of ugly. Is there a better way?
            if(anEvent instanceof java.awt.event.MouseEvent) { 
              java.awt.event.MouseEvent e = (java.awt.event.MouseEvent)anEvent;
              int row = table.rowAtPoint(e.getPoint());
              if(row != -1) {
                Component c = table.getCellRenderer(row, 0).getTableCellRendererComponent(table, table.getModel().getValueAt(row, 0), table.isRowSelected(row), false, row, 0);
                java.awt.Rectangle rowBounds = table.getCellRect(row, 0, false);
                c.setBounds(rowBounds);
                c.doLayout();
                java.awt.Point hit = new java.awt.Point(e.getX() - (int)rowBounds.getX(), e.getY() - (int)rowBounds.getY());
                return c.getComponentAt(hit) instanceof JCheckBox;
              }
            }
            return false;
          } 
          public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            final JCheckBox checkBox = new JCheckBox();
            final boolean isGrayed = item.getGrayed();
            checkBox.setModel(new javax.swing.JToggleButton.ToggleButtonModel() {
              public boolean isPressed() {
                return isGrayed? true: super.isPressed();
              }
              public boolean isArmed() {
                return isGrayed? true: super.isArmed();
              }
            });
            if (isSelected) {
              checkBox.setForeground(table.getSelectionForeground());
              checkBox.setBackground(table.getSelectionBackground());
            }
            else {
              checkBox.setForeground(table.getForeground());
              checkBox.setBackground(table.getBackground());
            }
            checkBox.setSelected(item.getChecked());
            checkBox.setFont(table.getFont());
            checkBox.addItemListener(new ItemListener() {
              public void itemStateChanged(ItemEvent e) {
                item.setChecked(e.getStateChange() == ItemEvent.SELECTED);
                Event event = new Event();
                event.item = item;
                event.detail = SWT.CHECK;
                Table.this.postEvent(SWT.Selection, event);
              }
            });
            // This detaches the checkbox from the rest of the cell, so that clicking the cell outside of the checkbox will not check it.
            JLabel label = new JLabel(item.getText());
            Image image = item.getImage();
            if(image != null) {
              // TODO: handle setImageIndent
              label.setIcon(new javax.swing.ImageIcon(image.swingHandle));
            }
            label.setFont(table.getFont());
            JPanel panel = new JPanel(new java.awt.BorderLayout());
            panel.add(checkBox, java.awt.BorderLayout.WEST);
            panel.add(label, java.awt.BorderLayout.CENTER);
            if (isSelected) {
              panel.setForeground(table.getSelectionForeground());
              panel.setBackground(table.getSelectionBackground());
            }
            else {
              panel.setForeground(table.getForeground());
              panel.setBackground(table.getBackground());
            }
            checkBox.setFocusable(false);
            label.setFocusable(false);
            panel.setFocusable(false);
            return panel;
          } 
          public Object getCellEditorValue() {
            return item;
          }
          public boolean shouldSelectCell(EventObject anEvent) {
            return false; 
          }
          public void cancelCellEditing() { 
            fireEditingCanceled(); 
          }
          public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
          }
          protected void fireEditingCanceled() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length-2; i>=0; i-=2) {
              if (listeners[i]==CellEditorListener.class) {
              if (changeEvent == null)
                changeEvent = new ChangeEvent(this);
                ((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
              }        
            }
          }
          public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
          }
          public boolean stopCellEditing() { 
            fireEditingStopped(); 
            return true;
          }
          protected void fireEditingStopped() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length-2; i>=0; i-=2) {
              if (listeners[i]==CellEditorListener.class) {
                if (changeEvent == null)
                changeEvent = new ChangeEvent(this);
                ((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
              }        
            }
          }
        };
      }  
      public void setValueAt(Object value, int row, int col) {
        if(table.getColumnCount() == 0) {
          ((DefaultTableModel)table.getModel()).addColumn(null);
        }
        super.setValueAt(value, row, col);
      }
    };
    setSwingContainer(table);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    setLinesVisible(false);
    final JTableHeader header = table.getTableHeader();
    // Reordering is not supported by all the calls. Have to disable it.
    header.setReorderingAllowed(false);
    header.setVisible(false);
    header.addMouseListener(new java.awt.event.MouseAdapter() {
      private javax.swing.table.TableColumn getResizingColumn(java.awt.Point p, int column) {
        if (column == -1) {
          return null;
        }
        java.awt.Rectangle r = header.getHeaderRect(column);
        r.grow(-3, 0);
        if (r.contains(p)) {
          return null;
        }
        int midPoint = r.x + r.width / 2;
        int columnIndex;
        if (header.getComponentOrientation().isLeftToRight()) {
          columnIndex = (p.x < midPoint) ? column - 1 : column;
        } else {
          columnIndex = (p.x < midPoint) ? column : column - 1;
        }
        if (columnIndex == -1) {
          return null;
        }
        return header.getColumnModel().getColumn(columnIndex);
      }
      private boolean canResize(javax.swing.table.TableColumn column) { 
        return (column != null) && header.getResizingAllowed() && column.getResizable(); 
      }
      public void mouseClicked(java.awt.event.MouseEvent e) {
        // Packing of the column
        if (e.getClickCount() == 2) {
          java.awt.Point p = e.getPoint();
          int index = header.columnAtPoint(p);
          java.awt.Point p2 = null;
          if (header.getComponentOrientation().isLeftToRight()) {
            p2 = new java.awt.Point((int)p.getX() - 3, (int)p.getY());
          } else {
            p2 = new java.awt.Point((int)p.getX() + 3, (int)p.getY());
          }
          int index2 = header.columnAtPoint(p2);
          if(index2 != index) {
            p = p2;
            index = index2;
          }
          if (index != -1) {
            // The last 3 pixels + 3 pixels of next column are for resizing
            javax.swing.table.TableColumn resizingColumn = getResizingColumn(p, index);
            if (canResize(resizingColumn)) {
              columns[index].pack();
            }
          }
        }
        if(e.getClickCount() == 1 || e.getClickCount() == 2) {
          java.awt.Point p = e.getPoint();
          int index = header.columnAtPoint(p);
          java.awt.Rectangle r = header.getHeaderRect(index);
          r.grow(-3, 0);
          if (r.contains(p)) {
            if(e.getClickCount() == 1 || !hooks(SWT.DefaultSelection)) {
              columns[index].postEvent(SWT.Selection);
            } else {
              columns[index].postEvent(SWT.DefaultSelection);
            }
          }
        }
      }
    });
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
      public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
          Event event = new Event();
          event.item = items[table.getSelectionModel().getLeadSelectionIndex()];
          postEvent(SWT.Selection, event);
        }
      }
    });
    table.setSelectionMode((style & SWT.MULTI) != 0? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION: ListSelectionModel.SINGLE_SELECTION);

    final java.awt.event.MouseListener[] mouseListeners = table.getMouseListeners();
    for(int i=0; i<mouseListeners.length; i++) {
      table.removeMouseListener(mouseListeners[i]);
    }
    table.addMouseListener(new java.awt.event.MouseAdapter(){
      public void mousePressed(java.awt.event.MouseEvent e) {
        if(e.getClickCount() == 1 && hooks(SWT.Selection)) {
          int row = table.rowAtPoint(e.getPoint());
          if(table.isRowSelected(row)) {
            Event event = new Event();
            event.item = items[row];
            postEvent(SWT.Selection, event);
          }
        }
        if(e.getClickCount() == 2 && hooks(SWT.DefaultSelection)) {
          java.awt.Point p = e.getPoint();
          int row = table.rowAtPoint(p);
          int column = table.columnAtPoint(p);
          if ((column != -1) && (row != -1)) {
            isSelectable = false;
            Event event = new Event();
            event.item = items[row];
            postEvent(SWT.DefaultSelection, event);
          }
        }
      }
    });
    for(int i=0; i<mouseListeners.length; i++) {
      table.addMouseListener(mouseListeners[i]);
    }
    table.addMouseListener(new java.awt.event.MouseAdapter(){
      public void mousePressed(java.awt.event.MouseEvent e) {
        isSelectable = true;
      }
    });
    if((style & SWT.CHECK) != 0) {
      table.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent e) {
          if(e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
            int row = table.getSelectedRow();
            if(row >= 0) {
              table.editCellAt(-1, -1);
              items[row].setChecked(!items[row].getChecked());
              ((DefaultTableModel)table.getModel()).fireTableCellUpdated(row, 0);
              Event event = new Event();
              event.item = items[row];
              event.detail = SWT.CHECK;
              Table.this.postEvent(SWT.Selection, event);
            }
          }
        }
      });
    }
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      scrollPane = new ExtJScrollPane(table);
      scrollPane.setColumnHeader(new JViewport());
      scrollPane.getColumnHeader().setVisible(false);
      if((style & SWT.BORDER) == 0) {
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
      }
      scrollPane.getViewport().setBackground(UIManager.getColor("Table.background"));
      LayeredContainer layeredContainer = new LayeredContainer(scrollPane);
      setContentPane(layeredContainer);
      return layeredContainer;
    }
    if((style & SWT.BORDER) != 0) {
      table.setBorder(UIManager.getBorder("TextField.border"));
    }
    LayeredContainer layeredContainer = new LayeredContainer(table);
    setContentPane(layeredContainer);
    return layeredContainer;
  }

  int defaultBackground() {
    return OS.GetSysColor(OS.COLOR_WINDOW);
  }

  /**
   * Deselects the items at the given zero-relative indices in the receiver.
   * If the item at the given zero-relative index in the receiver
   * is selected, it is deselected.  If the item at the index
   * was not selected, it remains deselected. Indices that are out
   * of range and duplicate indices are ignored.
   *
   * @param indices the array of indices for the items to deselect
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void deselect(int[] indices) {
    checkWidget();
    if(indices == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    int count = table.getRowCount();
    for(int i = 0; i < indices.length; i++) {
      int row = indices[i];
      if(row >= 0 && row < count) {
        table.removeRowSelectionInterval(row, row);
      }
    }
  }

  /**
   * Deselects the item at the given zero-relative index in the receiver.
   * If the item at the index was already deselected, it remains
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
    int count = table.getRowCount();
    if(index >= 0 && index < count) {
      table.removeRowSelectionInterval(index, index);
    }
  }

  /**
   * Deselects the items at the given zero-relative indices in the receiver.
   * If the item at the given zero-relative index in the receiver
   * is selected, it is deselected.  If the item at the index
   * was not selected, it remains deselected.  The range of the
   * indices is inclusive. Indices that are out of range are ignored.
   *
   * @param start the start index of the items to deselect
   * @param end the end index of the items to deselect
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void deselect(int start, int end) {
    checkWidget();
    int count = table.getRowCount();
    if(count == 0 || start >= count || end < 0) {
      return;
    }
    if(start < 0) {
      start = 0;
    }
    if(end >= count) {
      end = count - 1;
    }
    table.removeRowSelectionInterval(start, end);
  }

  /**
   * Deselects all selected items in the receiver.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void deselectAll() {
    checkWidget();
    table.clearSelection();
//    LVITEM lvItem = new LVITEM();
//    lvItem.mask = OS.LVIF_STATE;
//    lvItem.stateMask = OS.LVIS_SELECTED;
//    OS.SendMessage(handle, OS.LVM_SETITEMSTATE, -1, lvItem);
  }

  void destroyItem(TableColumn column) {
    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
    int count = OS.SendMessage(hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
    int index = 0;
    while(index < count) {
      if(columns[index] == column) {
        break;
      }
      index++;
    }
    if(index == count) {
      return;
    }
    boolean first = false;
    if(index == 0) {
      first = true;
      if(count > 1) {
        index = 1;
        int cchTextMax = 1024;
        int hHeap = OS.GetProcessHeap();
        int byteCount = cchTextMax * TCHAR.sizeof;
        int pszText = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
        LVCOLUMN lvColumn = new LVCOLUMN();
        lvColumn.mask = OS.LVCF_TEXT | OS.LVCF_WIDTH;
        lvColumn.pszText = pszText;
        lvColumn.cchTextMax = cchTextMax;
        OS.SendMessage(handle, OS.LVM_GETCOLUMN, 1, lvColumn);
        lvColumn.mask |= OS.LVCF_FMT;
        lvColumn.fmt = OS.LVCFMT_LEFT;
        OS.SendMessage(handle, OS.LVM_SETCOLUMN, 0, lvColumn);
        LVITEM lvItem = new LVITEM();
        lvItem.mask = OS.LVIF_TEXT | OS.LVIF_IMAGE | OS.LVIF_STATE;
        lvItem.pszText = pszText;
        lvItem.cchTextMax = cchTextMax;
        int itemCount = OS.SendMessage(handle, OS.LVM_GETITEMCOUNT, 0, 0);
        for(int i = 0; i < itemCount; i++) {
          lvItem.iItem = i;
          lvItem.iSubItem = 1;
          OS.SendMessage(handle, OS.LVM_GETITEM, 0, lvItem);
          lvItem.iSubItem = 0;
          OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
          TCHAR buffer = new TCHAR(getCodePage(), cchTextMax);
          OS.MoveMemory(buffer, pszText, byteCount);
          items[i].text = buffer.toString(0, buffer.strlen());
          if(imageList != null) {
            items[i].image = imageList.get(lvItem.iImage);
          }
        }
        if(pszText != 0) {
          OS.HeapFree(hHeap, 0, pszText);
        }
      } else {
        int hHeap = OS.GetProcessHeap();
        int pszText = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
        LVCOLUMN lvColumn = new LVCOLUMN();
        lvColumn.mask = OS.LVCF_TEXT;
        lvColumn.pszText = pszText;
        OS.SendMessage(handle, OS.LVM_SETCOLUMN, 0, lvColumn);
        if(pszText != 0) {
          OS.HeapFree(hHeap, 0, pszText);
        }
        OS.SendMessage(handle, OS.LVM_SETCOLUMNWIDTH, 0, OS.LVSCW_AUTOSIZE);
      }
    }
    if(count > 1) {
      if(OS.SendMessage(handle, OS.LVM_DELETECOLUMN, index, 0) == 0) {
        error(SWT.ERROR_ITEM_NOT_REMOVED);
      }
    }
    if(first) {
      index = 0;
    }
    System.arraycopy(columns, index + 1, columns, index, --count - index);
    columns[count] = null;
  }

  void destroyItem(TableItem item) {
    int count = table.getRowCount();
    int index = 0;
    while(index < count) {
      if(items[index] == item) {
        break;
      }
      index++;
    }
    if(index == count) {
      return;
    }
    ((DefaultTableModel)table.getModel()).removeRow(index);
    
//    ignoreSelect = true;
//    int code = OS.SendMessage(handle, OS.LVM_DELETEITEM, index, 0);
//    ignoreSelect = false;
//    if(code == 0) {
//      error(SWT.ERROR_ITEM_NOT_REMOVED);
//    }
    System.arraycopy(items, index + 1, items, index, --count - index);
//    items[count] = null;

    if(count == 0) {
      items = new TableItem[4];
      // TODO: find about destroying imagelist
    }

//    if(count == 0) {
//      if(imageList != null) {
//        int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
//        int columnCount = OS.SendMessage(hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//        if(columnCount == 1 && columns[0] == null) {
//          columnCount = 0;
//        }
//        int i = 0;
//        while(i < columnCount) {
//          TableColumn column = columns[i];
//          if(column.getImage() != null) {
//            break;
//          }
//          i++;
//        }
//        if(i == columnCount) {
//          OS.SendMessage(handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
//          Display display = getDisplay();
//          display.releaseImageList(imageList);
//          imageList = null;
//        }
//      }
//      customDraw = false;
//      items = new TableItem[4];
//    }
  }

  void fixCheckboxImageList() {
    /*
     * Bug in Windows.  When the state image list is larger than the
     * image list, Windows incorrectly positions the state images.  When
     * the table is scrolled, Windows draws garbage.  The fix is to force
     * the state image list to be the same size as the image list.
     */
    if((style & SWT.CHECK) == 0) {
      return;
    }
    int hImageList = OS.SendMessage(handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL,
                                    0);
    if(hImageList == 0) {
      return;
    }
    int[] cx = new int[1], cy = new int[1];
    OS.ImageList_GetIconSize(hImageList, cx, cy);
    int hOldStateList = OS.SendMessage(handle, OS.LVM_GETIMAGELIST,
                                       OS.LVSIL_STATE, 0);
    if(hOldStateList == 0) {
      return;
    }
    int[] stateCx = new int[1], stateCy = new int[1];
    OS.ImageList_GetIconSize(hOldStateList, stateCx, stateCy);
    if(cx[0] == stateCx[0] && cy[0] == stateCy[0]) {
      return;
    }
    setCheckboxImageList(cx[0], cy[0]);
  }

  int getBackgroundPixel() {
    return OS.SendMessage(handle, OS.LVM_GETBKCOLOR, 0, 0);
  }

  /**
   * Returns the column at the given, zero-relative index in the
   * receiver. Throws an exception if the index is out of range.
   * If no <code>TableColumn</code>s were created by the programmer,
   * this method will throw <code>ERROR_INVALID_RANGE</code> despite
   * the fact that a single column of data may be visible in the table.
   * This occurs when the programmer uses the table like a list, adding
   * items but never creating a column.
   *
   * @param index the index of the column to return
   * @return the column at the given index
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public TableColumn getColumn(int index) {
    checkWidget();
    int columnCount = table.getColumnCount();
    if(columnCount == 1 && columns[0] == null) {
      columnCount = 0;
    }
    if(!(0 <= index && index < columnCount)) {
      error(SWT.ERROR_INVALID_RANGE);
    }
    return columns[index];
  }

  /**
   * Returns the number of columns contained in the receiver.
   * If no <code>TableColumn</code>s were created by the programmer,
   * this value is zero, despite the fact that visually, one column
   * of items is may be visible. This occurs when the programmer uses
   * the table like a list, adding items but never creating a column.
   *
   * @return the number of columns
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public int getColumnCount() {
    checkWidget();
    int columnCount = table.getColumnCount();
    if(columnCount == 1 && columns[0] == null) {
      columnCount = 0;
    }
    return columnCount;
//    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
//    int count = OS.SendMessage(hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//    if(count == 1 && columns[0] == null) {
//      count = 0;
//    }
//    return count;
  }

  /**
   * Returns an array of <code>TableColumn</code>s which are the
   * columns in the receiver. If no <code>TableColumn</code>s were
   * created by the programmer, the array is empty, despite the fact
   * that visually, one column of items may be visible. This occurs
   * when the programmer uses the table like a list, adding items but
   * never creating a column.
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
  public TableColumn[] getColumns() {
    checkWidget();
    int count = table.getColumnCount();
    if(count == 1 && columns[0] == null) {
      count = 0;
    }
    TableColumn[] result = new TableColumn[count];
    System.arraycopy(columns, 0, result, 0, count);
    return result;
  }

  int getFocusIndex() {
    return OS.SendMessage(handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
  }

  int getForegroundPixel() {
    return OS.SendMessage(handle, OS.LVM_GETTEXTCOLOR, 0, 0);
  }

  /**
   * Returns the width in pixels of a grid line.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getGridLineWidth() {
    checkWidget();
    return 1;
  }

  /**
   * Returns the height of the receiver's header
   *
   * @return the height of the header or zero if the header is not visible
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 2.0
   */
  public int getHeaderHeight() {
    checkWidget();
    return table.getTableHeader().getHeight();
//    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
//    if(hwndHeader == 0) {
//      return 0;
//    }
//    RECT rect = new RECT();
//    OS.GetWindowRect(hwndHeader, rect);
//    return rect.bottom - rect.top;
  }

  /**
   * Returns <code>true</code> if the receiver's header is visible,
   * and <code>false</code> otherwise.
   * <p>
   * If one of the receiver's ancestors is not visible or some
   * other condition makes the receiver not visible, this method
   * may still indicate that it is considered visible even though
   * it may not actually be showing.
   * </p>
   *
   * @return the receiver's header's visibility state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean getHeaderVisible() {
    checkWidget();
    return table.getTableHeader().isVisible();
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
  public TableItem getItem(int index) {
    checkWidget();
    int count = table.getRowCount();
    if(!(0 <= index && index < count)) {
      error(SWT.ERROR_INVALID_RANGE);
    }
    return items[index];
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
   *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public TableItem getItem(Point point) {
    checkWidget();
    if(point == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    java.awt.Point sPoint = new java.awt.Point(point.x, point.y);
    sPoint = SwingUtilities.convertPoint(getHandle(), sPoint, table);
    int row = table.rowAtPoint(sPoint);
    if(row >= 0) {
      return items[row];
    } else {
      return null;
    }
//    LVHITTESTINFO pinfo = new LVHITTESTINFO();
//    pinfo.x = point.x;
//    pinfo.y = point.y;
//    OS.SendMessage(handle, OS.LVM_HITTEST, 0, pinfo);
//    if(pinfo.iItem != -1) {
//      return items[pinfo.iItem];
//    }
//    return null;
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
    return table.getRowCount();
  }

  /**
   * Returns the height of the area which would be used to
   * display <em>one</em> of the items in the receiver's.
   *
   * @return the height of one item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getItemHeight() {
    checkWidget();
    return table.getRowHeight();
//    int empty = OS.SendMessage(handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
//    int oneItem = OS.SendMessage(handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
//    return(oneItem >> 16) - (empty >> 16);
  }

  /**
   * Returns an array of <code>TableItem</code>s which are the items
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
  public TableItem[] getItems() {
    checkWidget();
    TableItem[] result = new TableItem[table.getRowCount()];
    System.arraycopy(items, 0, result, 0, result.length);
//    int count = OS.SendMessage(handle, OS.LVM_GETITEMCOUNT, 0, 0);
//    TableItem[] result = new TableItem[count];
//    System.arraycopy(items, 0, result, 0, count);
    return result;
  }

  /**
   * Returns <code>true</code> if the receiver's lines are visible,
   * and <code>false</code> otherwise.
   * <p>
   * If one of the receiver's ancestors is not visible or some
   * other condition makes the receiver not visible, this method
   * may still indicate that it is considered visible even though
   * it may not actually be showing.
   * </p>
   *
   * @return the visibility state of the lines
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean getLinesVisible() {
    checkWidget();
    return table.getShowHorizontalLines() || table.getShowVerticalLines();
//    int bits = OS.SendMessage(handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
//    return(bits & OS.LVS_EX_GRIDLINES) != 0;
  }

  /**
   * Returns an array of <code>TableItem</code>s that are currently
   * selected in the receiver. An empty array indicates that no
   * items are selected.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its selection, so modifying the array will
   * not affect the receiver.
   * </p>
   * @return an array representing the selection
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public TableItem[] getSelection() {
    checkWidget();
    int[] selectedRows = table.getSelectedRows();
    TableItem[] result = new TableItem[selectedRows.length];
    for(int i=0; i<result.length; i++) {
      result[i] = items[selectedRows[i]];
    }
//    int i = -1, j = 0,
//        count = OS.SendMessage(handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
//    TableItem[] result = new TableItem[count];
//    while((i = OS.SendMessage(handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) !=
//          -1) {
//      result[j++] = items[i];
//    }
    return result;
  }

  /**
   * Returns the number of selected items contained in the receiver.
   *
   * @return the number of selected items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getSelectionCount() {
    checkWidget();
    return table.getSelectedRowCount();
//    return OS.SendMessage(handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
  }

  /**
   * Returns the zero-relative index of the item which is currently
   * selected in the receiver, or -1 if no item is selected.
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
    return table.getSelectedRow();
//    int focusIndex = OS.SendMessage(handle, OS.LVM_GETNEXTITEM, -1,
//                                    OS.LVNI_FOCUSED);
//    int selectedIndex = OS.SendMessage(handle, OS.LVM_GETNEXTITEM, -1,
//                                       OS.LVNI_SELECTED);
//    if(focusIndex == selectedIndex) {
//      return selectedIndex;
//    }
//    int i = -1;
//    while((i = OS.SendMessage(handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) !=
//          -1) {
//      if(i == focusIndex) {
//        return i;
//      }
//    }
//    return selectedIndex;
  }

  /**
   * Returns the zero-relative indices of the items which are currently
   * selected in the receiver.  The array is empty if no items are selected.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its selection, so modifying the array will
   * not affect the receiver.
   * </p>
   * @return the array of indices of the selected items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int[] getSelectionIndices() {
    checkWidget();
    return table.getSelectedRows();
//    int i = -1, j = 0,
//        count = OS.SendMessage(handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
//    int[] result = new int[count];
//    while((i = OS.SendMessage(handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) !=
//          -1) {
//      result[j++] = i;
//    }
//    return result;
  }

  /**
   * Returns the zero-relative index of the item which is currently
   * at the top of the receiver. This index can change when items are
   * scrolled or new items are added or removed.
   *
   * @return the index of the top item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getTopIndex() {
    checkWidget();
    java.awt.Rectangle rec = table.getVisibleRect();
    int i = table.rowAtPoint(new java.awt.Point(rec.x, rec.y));
    return i;
  }

  int imageIndex(Image image) {
    if(image == null) {
      return OS.I_IMAGENONE;
    }
    if(imageList == null) {
      Rectangle bounds = image.getBounds();
      imageList = getDisplay().getImageList(new Point(bounds.width,
          bounds.height));
      int index = imageList.indexOf(image);
      if(index == -1) {
        index = imageList.add(image);
      }
      int hImageList = imageList.getHandle();
      OS.SendMessage(handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, hImageList);
      return index;
    }
    int index = imageList.indexOf(image);
    if(index != -1) {
      return index;
    }
    return imageList.add(image);
  }

  /**
   * Searches the receiver's list starting at the first column
   * (index 0) until a column is found that is equal to the
   * argument, and returns the index of that column. If no column
   * is found, returns -1.
   *
   * @param column the search column
   * @return the index of the column
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int indexOf(TableColumn column) {
    checkWidget();
    if(column == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    int columnCount = table.getColumnCount();
    if(columnCount == 1 && columns[0] == null) {
      columnCount = 0;
    }
    for(int i = 0; i < columnCount; i++) {
      if(columns[i] == column) {
        return i;
      }
    }
    return -1;
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
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int indexOf(TableItem item) {
    checkWidget();
    if(item == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    int count = table.getRowCount();
    for(int i = 0; i < count; i++) {
      if(items[i] == item) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns <code>true</code> if the item is selected,
   * and <code>false</code> otherwise.  Indices out of
   * range are ignored.
   *
   * @param index the index of the item
   * @return the selection state of the item at the index
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean isSelected(int index) {
    checkWidget();
    if(index >= table.getRowCount()) {
      return false;
    } else {
      return table.isRowSelected(index);
    }
  }

  void releaseWidget() {
//    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
    int columnCount = table.getColumnCount();
    if(columnCount == 1 && columns[0] == null) {
      columnCount = 0;
    }
    for(int i = 0; i < columnCount; i++) {
      TableColumn column = columns[i];
      if(!column.isDisposed()) {
        column.releaseResources();
      }
    }
    columns = null;
    int itemCount = table.getRowCount();

    for(int i = 0; i < itemCount; i++) {
      TableItem item = items[i];
      if(!item.isDisposed()) {
        item.releaseResources();
      }
    }

    // TODO: release images
//    customDraw = false;
//    items = null;
//    if(imageList != null) {
//      OS.SendMessage(handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
//      Display display = getDisplay();
//      display.releaseImageList(imageList);
//    }
//    imageList = null;
//    int hOldList = OS.SendMessage(handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE,
//                                  0);
//    OS.SendMessage(handle, OS.LVM_SETIMAGELIST, OS.LVSIL_STATE, 0);
//    if(hOldList != 0) {
//      OS.ImageList_Destroy(hOldList);
//    }
    super.releaseWidget();
  }

  /**
   * Removes the items from the receiver's list at the given
   * zero-relative indices.
   *
   * @param indices the array of indices of the items
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
   * </ul>
   */
  public void remove(int[] indices) {
    checkWidget();
    if(indices == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    int count = table.getRowCount();
    for(int i=0; i<indices.length; i++) {
      if(indices[i] < 0 || indices[i] >= count) {
        error(SWT.ERROR_INVALID_RANGE);
      }
    }
    int[] newIndices = new int[indices.length];
    System.arraycopy(indices, 0, newIndices, 0, indices.length);
    Arrays.sort(newIndices);
    for(int i=newIndices.length - 1; i>=0; i--) {
      int index = newIndices[i];
      ((DefaultTableModel)table.getModel()).removeRow(index);
      TableItem item = items[index];
      System.arraycopy(items, index + 1, items, index, --count - index);
      items[count] = null;
      item.releaseResources();
    }
//    
//    sort(newIndices);
//    int last = -1;
//    int count = OS.SendMessage(handle, OS.LVM_GETITEMCOUNT, 0, 0);
//    for(int i = 0; i < newIndices.length; i++) {
//      int index = newIndices[i];
//      if(index != last || i == 0) {
//        ignoreSelect = true;
//        int code = OS.SendMessage(handle, OS.LVM_DELETEITEM, index, 0);
//        ignoreSelect = false;
//        if(code == 0) {
//          if(0 <= index && index < count) {
//            error(SWT.ERROR_ITEM_NOT_REMOVED);
//          } else {
//            error(SWT.ERROR_INVALID_RANGE);
//          }
//        }
//
//        // BUG - disposed callback could remove an item
//        items[index].releaseResources();
//        System.arraycopy(items, index + 1, items, index, --count - index);
//        items[count] = null;
//        last = index;
//      }
//    }
  }

  /**
   * Removes the item from the receiver at the given
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
    int count = table.getRowCount();
    if(index >= count) {
      error(SWT.ERROR_INVALID_RANGE);
    }
    ((DefaultTableModel)table.getModel()).removeRow(index);
    TableItem item = items[index];
    System.arraycopy(items, index + 1, items, index, --count - index);
    items[count] = null;
    item.releaseResources();
  }

  /**
   * Removes the items from the receiver which are
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
    int count = table.getRowCount();
    int index = start;
    while(index <= end) {
      try {
        ((DefaultTableModel)table.getModel()).removeRow(index);
      } catch(Exception e) {
        break;
      }
      items[index].releaseResources();
      index++;
    }
    System.arraycopy(items, index, items, start, count - index);
    for(int i = count - (index - start); i < count; i++) {
      items[i] = null;
    }
    if(index <= end) {
      if(0 <= index && index < count) {
        error(SWT.ERROR_ITEM_NOT_REMOVED);
      } else {
        error(SWT.ERROR_INVALID_RANGE);
      }
    }
  }

  /**
   * Removes all of the items from the receiver.
   * <p>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void removeAll() {
    checkWidget();
//    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
//    int columnCount = OS.SendMessage(hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//    if(columnCount == 1 && columns[0] == null) {
//      columnCount = 0;
//    }
//    int itemCount = OS.SendMessage(handle, OS.LVM_GETITEMCOUNT, 0, 0);
//
//    /*
//     * Feature in Windows.  When there are a large number
//     * of columns and items in a table (>1000) where each
//     * of the subitems in the table has a string, it is much
//     * faster to delete each item with LVM_DELETEITEM rather
//     * than using LVM_DELETEALLITEMS.  The fix is to detect
//     * this case and delete the items, one by one.
//     *
//     * NOTE: LVM_DELETEALLITEMS is also sent by the table
//     * when the table is destroyed.
//     */
//    if(columnCount > 1) {
//      boolean redraw = drawCount == 0 && OS.IsWindowVisible(handle);
//      if(redraw) {
//        OS.SendMessage(handle, OS.WM_SETREDRAW, 0, 0);
//      }
//      int index = itemCount - 1;
//      while(index >= 0) {
//        ignoreSelect = true;
//        int code = OS.SendMessage(handle, OS.LVM_DELETEITEM, index, 0);
//        ignoreSelect = false;
//        if(code == 0) {
//          break;
//        }
//
//        // BUG - disposed callback could remove an item
//        items[index].releaseResources();
//        --index;
//      }
//      if(redraw) {
//        OS.SendMessage(handle, OS.WM_SETREDRAW, 1, 0);
//        /*
//         * This code is intentionally commented.  The window proc
//         * for the table implements WM_SETREDRAW to invalidate
//         * and erase the table so it is not necessary to do this
//         * again.
//         */
////			int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
////			OS.RedrawWindow (handle, null, 0, flags);
//      }
//      if(index != -1) {
//        error(SWT.ERROR_ITEM_NOT_REMOVED);
//      }
//    } else {
//      ignoreSelect = true;
//      int code = OS.SendMessage(handle, OS.LVM_DELETEALLITEMS, 0, 0);
//      ignoreSelect = false;
//      if(code == 0) {
//        error(SWT.ERROR_ITEM_NOT_REMOVED);
//      }
//      for(int i = 0; i < itemCount; i++) {
//        TableItem item = items[i];
//        if(!item.isDisposed()) {
//          item.releaseResources();
//        }
//      }
//    }

    int columnCount = table.getColumnCount();
    if(columnCount == 1 && columns[0] == null) {
      columnCount = 0;
    }

    ((DefaultTableModel)table.getModel()).setRowCount(0);

    if(imageList != null) {
      int i = 0;
      while(i < columnCount) {
        TableColumn column = columns[i];
        if(column.getImage() != null) {
          break;
        }
        i++;
      }
      if(i == columnCount) {
        OS.SendMessage(handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
        Display display = getDisplay();
        display.releaseImageList(imageList);
        imageList = null;
      }
    }
    customDraw = false;
    items = new TableItem[4];
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
   * Selects the items at the given zero-relative indices in the receiver.
   * If the item at the given zero-relative index in the receiver
   * is not selected, it is selected.  If the item at the index
   * was selected, it remains selected. Indices that are out
   * of range and duplicate indices are ignored.
   *
   * @param indices the array of indices for the items to select
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void select(int[] indices) {
    checkWidget();
    if(indices == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    int length = indices.length;
    if(length == 0) {
      return;
    }
    int count = table.getRowCount();
    for(int i = 0; i < indices.length; i++) {
      int row = indices[i];
      if(row >= 0 && row < count) {
        table.addRowSelectionInterval(row, row);
      }
    }
  }

  /**
   * Selects the item at the given zero-relative index in the receiver.
   * If the item at the index was already selected, it remains
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
    table.getSelectionModel().addSelectionInterval(index, index);
//    LVITEM lvItem = new LVITEM();
//    lvItem.mask = OS.LVIF_STATE;
//    lvItem.state = OS.LVIS_SELECTED;
//    lvItem.stateMask = OS.LVIS_SELECTED;
//    lvItem.iItem = index;
//    ignoreSelect = true;
//    OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
//    ignoreSelect = false;
  }

  /**
   * Selects the items at the given zero-relative indices in the receiver.
   * If the item at the index was already selected, it remains
   * selected. The range of the indices is inclusive. Indices that are
   * out of range are ignored and no items will be selected if start is
   * greater than end.
   *
   * @param start the start of the range
   * @param end the end of the range
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void select(int start, int end) {
    checkWidget();
    table.getSelectionModel().addSelectionInterval(start, end);
//    LVITEM lvItem = new LVITEM();
//    lvItem.mask = OS.LVIF_STATE;
//    lvItem.state = OS.LVIS_SELECTED;
//    lvItem.stateMask = OS.LVIS_SELECTED;
//    for(int i = start; i <= end; i++) {
//      lvItem.iItem = i;
//      ignoreSelect = true;
//      OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
//      ignoreSelect = false;
//    }
  }

  /**
   * Selects all the items in the receiver.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void selectAll() {
    checkWidget();
    if((style & SWT.SINGLE) != 0) {
      return;
    }
    table.selectAll();
//    LVITEM lvItem = new LVITEM();
//    lvItem.mask = OS.LVIF_STATE;
//    lvItem.state = OS.LVIS_SELECTED;
//    lvItem.stateMask = OS.LVIS_SELECTED;
//    ignoreSelect = true;
//    OS.SendMessage(handle, OS.LVM_SETITEMSTATE, -1, lvItem);
//    ignoreSelect = false;
  }

  LRESULT sendMouseDownEvent(int type, int button, int msg, int wParam,
                             int lParam) {
    /*
     * Feature in Windows.  Inside WM_LBUTTONDOWN and WM_RBUTTONDOWN,
     * the widget starts a modal loop to determine if the user wants
     * to begin a drag/drop operation or marque select.  Unfortunately,
     * this modal loop eats the corresponding mouse up.  The fix is to
     * detect the cases when the modal loop has eaten the mouse up and
     * issue a fake mouse up.
     *
     * By observation, when the mouse is clicked anywhere but the check
     * box, the widget eats the mouse up.  When the mouse is dragged,
     * the widget does not eat the mouse up.
     */
    LVHITTESTINFO pinfo = new LVHITTESTINFO();
    pinfo.x = (short)(lParam & 0xFFFF);
    pinfo.y = (short)(lParam >> 16);
    OS.SendMessage(handle, OS.LVM_HITTEST, 0, pinfo);
    sendMouseEvent(type, button, msg, wParam, lParam);

    /*
     * Force the table to have focus so that when the user
     * reselects the focus item, the LVIS_FOCUSED state bits
     * for the item will be set.  These bits are used when
     * the table is multi-select to issue the selection
     * event.  If the user did not click on an item, then
     * set focus to the table so that it will come to the
     * front and take focus in the work around below.
     */
    OS.SetFocus(handle);

    /*
     * Feature in Windows.  When the user selects outside of
     * a table item, Windows deselects all the items, even
     * when the table is multi-select.  While not strictly
     * wrong, this is unexpected.  The fix is to detect the
     * case and avoid calling the window proc.
     */
    if(pinfo.iItem == -1) {
      if(OS.GetCapture() != handle) {
        OS.SetCapture(handle);
      }
      return LRESULT.ZERO;
    }

    /*
     * Feature in Windows.  When a table item is reselected,
     * the table does not issue a WM_NOTIFY when the item
     * state has not changed.  This is inconsistent with
     * the list widget and other widgets in Windows.  The
     * fix is to detect the case when an item is reselected
     * and issue the notification.
     */
    boolean wasSelected = false;
    int count = OS.SendMessage(handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
    if(count == 1 && pinfo.iItem != -1) {
      LVITEM lvItem = new LVITEM();
      lvItem.mask = OS.LVIF_STATE;
      lvItem.stateMask = OS.LVIS_SELECTED;
      lvItem.iItem = pinfo.iItem;
      OS.SendMessage(handle, OS.LVM_GETITEM, 0, lvItem);
      wasSelected = (lvItem.state & OS.LVIS_SELECTED) != 0;
      if(wasSelected) {
        ignoreSelect = true;
      }
    }
    dragStarted = false;
    int code = callWindowProc(msg, wParam, lParam);
    if(wasSelected) {
      ignoreSelect = false;
      Event event = new Event();
      event.item = items[pinfo.iItem];
      postEvent(SWT.Selection, event);
    }
    if(dragStarted) {
      if(OS.GetCapture() != handle) {
        OS.SetCapture(handle);
      }
    } else {
      int flags = OS.LVHT_ONITEMLABEL | OS.LVHT_ONITEMICON;
      boolean fakeMouseUp = (pinfo.flags & flags) != 0;
      if(!fakeMouseUp && (style & SWT.MULTI) != 0) {
        fakeMouseUp = (pinfo.flags & OS.LVHT_ONITEMSTATEICON) == 0;
      }
      if(fakeMouseUp) {
        mouseDown = false;
        sendMouseEvent(SWT.MouseUp, button, msg, wParam, lParam);
      }
    }
    dragStarted = false;
    return new LRESULT(code);
  }

  void setBackgroundPixel(int pixel) {
    if(background == pixel) {
      return;
    }
    background = pixel;

    /*
     * Feature in Windows.  Setting the color to be
     * the current default is not correct because the
     * widget will not change colors when the colors
     * are changed from the control panel.  There is
     * no fix at this time.
     */
    if(pixel == -1) {
      pixel = defaultBackground();
    }
    OS.SendMessage(handle, OS.LVM_SETBKCOLOR, 0, pixel);
    OS.SendMessage(handle, OS.LVM_SETTEXTBKCOLOR, 0, pixel);
    if((style & SWT.CHECK) != 0) {
      setCheckboxImageListColor();

      /*
       * Feature in Windows.  When the background color is
       * changed, the table does not redraw until the next
       * WM_PAINT.  The fix is to force a redraw.
       */
    }
    OS.InvalidateRect(handle, null, true);
  }

  void setCheckboxImageListColor() {
    if((style & SWT.CHECK) == 0) {
      return;
    }
    int hOldStateList = OS.SendMessage(handle, OS.LVM_GETIMAGELIST,
                                       OS.LVSIL_STATE, 0);
    if(hOldStateList == 0) {
      return;
    }
    int[] cx = new int[1], cy = new int[1];
    OS.ImageList_GetIconSize(hOldStateList, cx, cy);
    setCheckboxImageList(cx[0], cy[0]);
  }

  void setCheckboxImageList(int width, int height) {
    if((style & SWT.CHECK) == 0) {
      return;
    }
    //TODO: implement content of method...
    throw new IllegalStateException("Not implemented...");
//    
//    int count = 4;
//    int hStateList = OS.ImageList_Create(width, height, OS.ILC_COLOR, count,
//                                         count);
//    int hDC = OS.GetDC(handle);
//    int memDC = OS.CreateCompatibleDC(hDC);
//    int hBitmap = OS.CreateCompatibleBitmap(hDC, width * count, height);
//    int hOldBitmap = OS.SelectObject(memDC, hBitmap);
//    RECT rect = new RECT();
//    OS.SetRect(rect, 0, 0, width * count, height);
//    int hBrush = OS.CreateSolidBrush(getBackgroundPixel());
//    OS.FillRect(memDC, rect, hBrush);
//    OS.DeleteObject(hBrush);
//    int oldFont = OS.SelectObject(hDC, defaultFont());
//    TEXTMETRIC tm = new TEXTMETRIC();
//    OS.GetTextMetrics(hDC, tm);
//    OS.SelectObject(hDC, oldFont);
//    int itemWidth = Math.min(tm.tmHeight, width);
//    int itemHeight = Math.min(tm.tmHeight, height);
//    int left = (width - itemWidth) / 2, top = (height - itemHeight) / 2 + 1;
//    OS.SetRect(rect, left, top, left + itemWidth, top + itemHeight);
//    OS.DrawFrameControl(memDC, rect, OS.DFC_BUTTON,
//                        OS.DFCS_BUTTONCHECK | OS.DFCS_FLAT);
//    rect.left += width;
//    rect.right += width;
//    OS.DrawFrameControl(memDC, rect, OS.DFC_BUTTON,
//                        OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_FLAT);
//    rect.left += width;
//    rect.right += width;
//    OS.DrawFrameControl(memDC, rect, OS.DFC_BUTTON,
//                        OS.DFCS_BUTTONCHECK | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
//    rect.left += width;
//    rect.right += width;
//    OS.DrawFrameControl(memDC, rect, OS.DFC_BUTTON,
//                        OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED |
//                        OS.DFCS_INACTIVE | OS.DFCS_FLAT);
//    OS.SelectObject(memDC, hOldBitmap);
//    OS.DeleteDC(memDC);
//    OS.ReleaseDC(handle, hDC);
//    OS.ImageList_AddMasked(hStateList, hBitmap, 0);
//    OS.DeleteObject(hBitmap);
//    int hOldStateList = OS.SendMessage(handle, OS.LVM_GETIMAGELIST,
//                                       OS.LVSIL_STATE, 0);
//    OS.SendMessage(handle, OS.LVM_SETIMAGELIST, OS.LVSIL_STATE, hStateList);
//    if(hOldStateList != 0) {
//      OS.ImageList_Destroy(hOldStateList);
//    }
  }

  void setFocusIndex(int index) {
    // TODO: is it the way?
    table.getSelectionModel().setLeadSelectionIndex(index);
//    LVITEM lvItem = new LVITEM();
//    lvItem.mask = OS.LVIF_STATE;
//    lvItem.state = OS.LVIS_FOCUSED;
//    lvItem.stateMask = OS.LVIS_FOCUSED;
//    lvItem.iItem = index;
//    ignoreSelect = true;
//    OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
//    ignoreSelect = false;
  }

  public void setFont(Font font) {
    checkWidget();
    super.setFont(font);
    setScrollWidth();
    /*
     * Bug in Windows.  Setting the font will cause the
     * table area to be redrawn but not the column headers.
     * Fix is to force a redraw on the column headers.
     */
    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
    OS.InvalidateRect(hwndHeader, null, true);
    int bits = OS.SendMessage(handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
    if((bits & OS.LVS_EX_GRIDLINES) == 0) {
      return;
    }
    bits = OS.GetWindowLong(handle, OS.GWL_STYLE);
    if((bits & OS.LVS_NOCOLUMNHEADER) != 0) {
      return;
    }
    setRowHeight();
  }

  void setForegroundPixel(int pixel) {
    if(foreground == pixel) {
      return;
    }
    foreground = pixel;

    /*
     * Feature in Windows.  Setting the color to be
     * the current default is not correct because the
     * table will not change colors when the colors
     * are changed from the control panel.  There is
     * no fix at this time.
     */
    if(pixel == -1) {
      pixel = defaultForeground();
    }
    OS.SendMessage(handle, OS.LVM_SETTEXTCOLOR, 0, pixel);

    /*
     * Feature in Windows.  When the foreground color is
     * changed, the table does not redraw until the next
     * WM_PAINT.  The fix is to force a redraw.
     */
    OS.InvalidateRect(handle, null, true);
  }

  /**
   * Marks the receiver's header as visible if the argument is <code>true</code>,
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
  public void setHeaderVisible(boolean show) {
    checkWidget();
    table.getTableHeader().setVisible(show);
    if(scrollPane != null) {
      scrollPane.getColumnHeader().setVisible(show);
    }
//    int newBits = OS.GetWindowLong(handle, OS.GWL_STYLE);
//    newBits &= ~OS.LVS_NOCOLUMNHEADER;
//    if(!show) {
//      newBits |= OS.LVS_NOCOLUMNHEADER;
//      /*
//       * Feature in Windows.  Setting or clearing LVS_NOCOLUMNHEADER
//       * causes the table to scroll to the beginning.  The fix is to
//       * save and restore the top index.
//       */
//    }
//    int topIndex = getTopIndex();
//    OS.SetWindowLong(handle, OS.GWL_STYLE, newBits);
//    if(topIndex != 0) {
//      setTopIndex(topIndex);
//    }
//    if(show) {
//      int bits = OS.SendMessage(handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
//      if((bits & OS.LVS_EX_GRIDLINES) != 0) {
//        setRowHeight();
//      }
//    }
  }

  /**
       * Marks the receiver's lines as visible if the argument is <code>true</code>,
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
  public void setLinesVisible(boolean show) {
    checkWidget();
    table.setShowGrid(show);
  }

  public void setRedraw(boolean redraw) {
    checkWidget();
    if(redraw) {
      if(--drawCount == 0) {
        setScrollWidth();
        /*
         * This code is intentionally commented.  When many items
         * are added to a table, it is slightly faster to temporarily
         * unsubclass the window proc so that messages are dispatched
         * directly to the table.  This is optimization is dangerous
         * because any operation can occur when redraw is turned off,
         * even operations where the table must be subclassed in order
         * to have the correct behavior or work around a Windows bug.
         * For now, don't attempt it.
         */
//			subclass ();

        /*
         * Bug in Windows.  For some reason, when WM_SETREDRAW is used
         * to turn redraw back on this may result in a WM_SIZE.  If the
         * table column widths are adjusted in WM_SIZE, blank lines may
         * be inserted at the top of the widget.  A call to LVM_GETTOPINDEX
         * will return a negative number (this is an impossible result).
         * The fix is to ignore any resize generated by WM_SETREDRAW and
         * defer the work until the WM_SETREDRAW has returned.
         */
        ignoreResize = true;
        OS.SendMessage(handle, OS.WM_SETREDRAW, 1, 0);
        if(!ignoreResize) {
          setResizeChildren(false);
          sendEvent(SWT.Resize);
          // widget may be disposed at this point
          if(isDisposed()) {
            return;
          }
          if(layout != null) {
            layout.layout(this, false);
          }
          setResizeChildren(true);
        }
        ignoreResize = false;

        /*
         * This code is intentionally commented.  The window proc
         * for the table implements WM_SETREDRAW to invalidate
         * and erase the table and the header.  This is undocumented
         * behavior.  The commented code below shows what is actually
         * happening and reminds us that we are relying on this
         * undocumented behavior.
         *
         * NOTE: The window proc for the table does not redraw the
         * non-client area (ie. the border and scroll bars).  This
         * must be explicitly redrawn.  This code can be removed
         * if we stop relying on the undocuemented behavior.
         */
        if(OS.IsWinCE) {
          OS.InvalidateRect(handle, null, false);
        } else {
          OS.RedrawWindow(handle, null, 0, OS.RDW_FRAME | OS.RDW_INVALIDATE);
        }
//			int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//			if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETREDRAW, 1, 0);
//			int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
//			OS.RedrawWindow (handle, null, 0, flags);
//			if (hwndHeader != 0) OS.RedrawWindow (hwndHeader, null, 0, flags);
      }
    } else {
      if(drawCount++ == 0) {
        OS.SendMessage(handle, OS.WM_SETREDRAW, 0, 0);
        int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
        if(hwndHeader != 0) {
          OS.SendMessage(hwndHeader, OS.WM_SETREDRAW, 0, 0);
          /*
           * This code is intentionally commented.  When many items
           * are added to a table, it is slightly faster to temporarily
           * unsubclass the window proc so that messages are dispatched
           * directly to the table.  This is optimization is dangerous
           * because any operation can occur when redraw is turned off,
           * even operations where the table must be subclassed in order
           * to have the correct behavior or work around a Windows bug.
           * For now, don't attempt it.
           */
//			unsubclass ();
        }
      }
    }
  }

  void setRowHeight() {
    int hOldList = OS.SendMessage(handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL,
                                  0);
    if(hOldList != 0) {
      return;
    }
    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
    RECT rect = new RECT();
    OS.GetWindowRect(hwndHeader, rect);
    int height = rect.bottom - rect.top - 1;
    int hImageList = OS.ImageList_Create(1, height, 0, 0, 0);
    OS.SendMessage(handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, hImageList);
    OS.SendMessage(handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
    OS.ImageList_Destroy(hImageList);
  }

  void setScrollWidth() {
    if(drawCount != 0) {
      return;
    }
    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
    int count = OS.SendMessage(hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
    if(count == 1 && columns[0] == null) {
      OS.SendMessage(handle, OS.LVM_SETCOLUMNWIDTH, 0, OS.LVSCW_AUTOSIZE);
    }
  }

  /**
   * Selects the items at the given zero-relative indices in the receiver.
   * The current selected is first cleared, then the new items are selected.
   *
   * @param indices the indices of the items to select
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#deselectAll()
   * @see Table#select(int[])
   */
  public void setSelection(int[] indices) {
    checkWidget();
    if(indices == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    deselectAll();
    select(indices);
    if(indices.length != 0) {
      int focusIndex = indices[0];
      if(focusIndex != -1) {
        setFocusIndex(focusIndex);
      }
    }
    showSelection();
  }

  /**
   * Sets the receiver's selection to be the given array of items.
   * The current selected is first cleared, then the new items are
   * selected.
   *
   * @param items the array of items
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if one of the item has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#deselectAll()
   * @see Table#select(int)
   */
  public void setSelection(TableItem[] items) {
    checkWidget();
    if(items == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    deselectAll();
    int length = items.length;
    if(length == 0) {
      return;
    }
    int focusIndex = -1;
    if((style & SWT.SINGLE) != 0) {
      length = 1;
    }
    for(int i = length - 1; i >= 0; --i) {
      int index = indexOf(items[i]);
      if(index != -1) {
        select(focusIndex = index);
      }
    }
    if(focusIndex != -1) {
      setFocusIndex(focusIndex);
    }
    showSelection();
  }

  /**
   * Selects the item at the given zero-relative index in the receiver.
   * The current selected is first cleared, then the new item is selected.
   *
   * @param index the index of the item to select
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#deselectAll()
   * @see Table#select(int)
   */
  public void setSelection(int index) {
    checkWidget();
    deselectAll();
    select(index);
    if(index != -1) {
      setFocusIndex(index);
    }
    showSelection();
  }

  /**
   * Selects the items at the given zero-relative indices in the receiver.
   * The current selection is first cleared, then the new items are selected.
   * Indices that are out of range are ignored and no items will be selected
   * if start is greater than end.
   *
   * @param start the start index of the items to select
   * @param end the end index of the items to select
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#deselectAll()
   * @see Table#select(int,int)
   */
  public void setSelection(int start, int end) {
    checkWidget();
    deselectAll();
    select(start, end);
    /*
     * NOTE: This code relies on the select (int, int)
     * selecting the last item in the range for a single
     * selection table.
     */
    int focusIndex = (style & SWT.SINGLE) != 0 ? end : start;
    if(focusIndex != -1) {
      setFocusIndex(focusIndex);
    }
    showSelection();
  }

  /**
   * Sets the zero-relative index of the item which is currently
   * at the top of the receiver. This index can change when items
   * are scrolled or new items are added and removed.
   *
   * @param index the index of the top item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setTopIndex(int index) {
    checkWidget();
    int count = table.getRowCount();
    table.scrollRectToVisible(table.getCellRect(count - 1, 0, true));
    if(index != count - 1) {
      table.scrollRectToVisible(table.getCellRect(index, 0, true));
    }
//    int topIndex = OS.SendMessage(handle, OS.LVM_GETTOPINDEX, 0, 0);
//    if(index == topIndex) {
//      return;
//    }
//
//    /*
//     * Bug in Windows.  For some reason, LVM_SCROLL refuses to
//     * scroll a table vertically when the width and height of
//     * the table is smaller than a certain size.  The values
//     * that the author is seeing are width=68 and height=6
//     * but there is no guarantee that these values are absolute.
//     * They may depend on the font and any number of other
//     * factors.  In fact, the author has observed that setting
//     * the font to anything but the default seems to sometimes
//     * fix the problem.  The fix is to use LVM_GETCOUNTPERPAGE
//     * to detect the case when the number of visible items is
//     * zero and use LVM_ENSUREVISIBLE to scroll the table to
//     * make the index visible.
//     */
//
//    /*
//     * Bug in Windows.  When the table header is visible and
//     * there is not enough space to show a single table item,
//     * LVM_GETCOUNTPERPAGE can return a negative number instead
//     * of zero.  The fix is to test for negative or zero.
//     */
//    if(OS.SendMessage(handle, OS.LVM_GETCOUNTPERPAGE, 0, 0) <= 0) {
//
//      /*
//       * Bug in Windows.  For some reason, LVM_ENSUREVISIBLE can
//       * scroll one item more or one item less when there is not
//       * enough space to show a single table item.  The fix is
//       * to detect the case and call LVM_ENSUREVISIBLE again with
//       * the same arguments.  It seems that once LVM_ENSUREVISIBLE
//       * has scrolled into the general area, it is able to scroll
//       * to the exact item.
//       */
//      OS.SendMessage(handle, OS.LVM_ENSUREVISIBLE, index, 1);
//      if(index != OS.SendMessage(handle, OS.LVM_GETTOPINDEX, 0, 0)) {
//        OS.SendMessage(handle, OS.LVM_ENSUREVISIBLE, index, 1);
//      }
//      return;
//    }
//
//    /* Use LVM_SCROLL to scroll the table */
//    RECT rect = new RECT();
//    rect.left = OS.LVIR_BOUNDS;
//    OS.SendMessage(handle, OS.LVM_GETITEMRECT, 0, rect);
//    int dy = (index - topIndex) * (rect.bottom - rect.top);
//    OS.SendMessage(handle, OS.LVM_SCROLL, 0, dy);
  }

  void showItem(int index) {
    if(index != -1) {
      table.scrollRectToVisible(table.getCellRect(index, 0, true));
    }
//    /*
//     * Bug in Windows.  For some reason, when there is insufficient space
//     * to show an item, LVM_ENSUREVISIBLE causes blank lines to be
//     * inserted at the top of the widget.  A call to LVM_GETTOPINDEX will
//     * return a negative number (this is an impossible result).  The fix
//     * is to use LVM_GETCOUNTPERPAGE to detect the case when the number
//     * of visible items is zero and use LVM_ENSUREVISIBLE with the fPartialOK
//     * flag to scroll the table.
//     */
//    if(OS.SendMessage(handle, OS.LVM_GETCOUNTPERPAGE, 0, 0) <= 0) {
//      /*
//       * Bug in Windows.  For some reason, LVM_ENSUREVISIBLE can
//       * scroll one item more or one item less when there is not
//       * enough space to show a single table item.  The fix is
//       * to detect the case and call LVM_ENSUREVISIBLE again with
//       * the same arguments.  It seems that once LVM_ENSUREVISIBLE
//       * has scrolled into the general area, it is able to scroll
//       * to the exact item.
//       */
//      OS.SendMessage(handle, OS.LVM_ENSUREVISIBLE, index, 1);
//      if(index != OS.SendMessage(handle, OS.LVM_GETTOPINDEX, 0, 0)) {
//        OS.SendMessage(handle, OS.LVM_ENSUREVISIBLE, index, 1);
//      }
//    } else {
//      OS.SendMessage(handle, OS.LVM_ENSUREVISIBLE, index, 0);
//    }
  }

  /**
   * Shows the item.  If the item is already showing in the receiver,
   * this method simply returns.  Otherwise, the items are scrolled until
   * the item is visible.
   *
   * @param item the item to be shown
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#showSelection()
   */
  public void showItem(TableItem item) {
    checkWidget();
    if(item == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(item.isDisposed()) {
      error(SWT.ERROR_INVALID_ARGUMENT);
    }
    int index = indexOf(item);
    if(index != -1) {
      showItem(index);
    }
  }

  /**
   * Shows the selection.  If the selection is already showing in the receiver,
   * this method simply returns.  Otherwise, the items are scrolled until
   * the selection is visible.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#showItem(TableItem)
   */
  public void showSelection() {
    checkWidget();
    int index = table.getSelectedRow();
    if(index != -1) {
      showItem(index);
    }
  }

  int widgetStyle() {
    int bits = super.widgetStyle() | OS.LVS_SHAREIMAGELISTS |
        OS.WS_CLIPCHILDREN;
    if((style & SWT.HIDE_SELECTION) == 0) {
      bits |= OS.LVS_SHOWSELALWAYS;
    }
    if((style & SWT.SINGLE) != 0) {
      bits |= OS.LVS_SINGLESEL;
      /*
       * This code is intentionally commented.  In the future,
       * the FLAT bit may be used to make the header flat and
       * unresponsive to mouse clicks.
       */
//	if ((style & SWT.FLAT) != 0) bits |= OS.LVS_NOSORTHEADER;
    }
    bits |= OS.LVS_REPORT | OS.LVS_NOCOLUMNHEADER;
    return bits;
  }

  LRESULT WM_GETOBJECT(int wParam, int lParam) {
    /*
     * Ensure that there is an accessible object created for this
     * control because support for checked item accessibility is
     * temporarily implemented in the accessibility package.
     */
    if((style & SWT.CHECK) != 0) {
      if(accessible == null) {
        accessible = new_Accessible(this);
      }
    }
    return super.WM_GETOBJECT(wParam, lParam);
  }

  LRESULT WM_KEYDOWN(int wParam, int lParam) {
    LRESULT result = super.WM_KEYDOWN(wParam, lParam);
    if(result != null) {
      return result;
    }
    if((style & SWT.CHECK) != 0 && wParam == OS.VK_SPACE) {
      int index = OS.SendMessage(handle, OS.LVM_GETNEXTITEM, -1,
                                 OS.LVNI_FOCUSED);
      if(index != -1) {
        LVITEM lvItem = new LVITEM();
        lvItem.mask = OS.LVIF_STATE;
        lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
        lvItem.iItem = index;
        OS.SendMessage(handle, OS.LVM_GETITEM, 0, lvItem);
        int state = lvItem.state >> 12;
        if((state & 0x1) != 0) {
          state++;
        } else {
          --state;
        }
        lvItem.state = state << 12;
        OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
      }
    }
    return result;
  }

  LRESULT WM_LBUTTONDBLCLK(int wParam, int lParam) {
    /*
     * Feature in Windows.  When the user selects outside of
     * a table item, Windows deselects all the items, even
     * when the table is multi-select.  While not strictly
     * wrong, this is unexpected.  The fix is to detect the
     * case and avoid calling the window proc.
     */
    LVHITTESTINFO pinfo = new LVHITTESTINFO();
    pinfo.x = (short)(lParam & 0xFFFF);
    pinfo.y = (short)(lParam >> 16);
    OS.SendMessage(handle, OS.LVM_HITTEST, 0, pinfo);
    sendMouseEvent(SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
    sendMouseEvent(SWT.MouseDoubleClick, 1, OS.WM_LBUTTONDBLCLK, wParam, lParam);
    if(pinfo.iItem != -1) {
      callWindowProc(OS.WM_LBUTTONDBLCLK, wParam, lParam);
    }
    if(OS.GetCapture() != handle) {
      OS.SetCapture(handle);
    }
    return LRESULT.ZERO;
  }

  LRESULT WM_LBUTTONDOWN(int wParam, int lParam) {
    mouseDown = true;

    /*
     * Feature in Windows.  For some reason, capturing
     * the mouse after processing the mouse event for the
     * widget interferes with the normal mouse processing
     * for the widget.  The fix is to avoid the automatic
     * mouse capture.
     */
    LRESULT result = sendMouseDownEvent(SWT.MouseDown, 1, OS.WM_LBUTTONDOWN,
                                        wParam, lParam);

    /* Look for check/uncheck */
    if((style & SWT.CHECK) != 0) {
      LVHITTESTINFO pinfo = new LVHITTESTINFO();
      pinfo.x = (short)(lParam & 0xFFFF);
      pinfo.y = (short)(lParam >> 16);
      /*
       * Note that when the table has LVS_EX_FULLROWSELECT and the
       * user clicks anywhere on a row except on the check box, all
       * of the bits are set.  The hit test flags are LVHT_ONITEM.
       * This means that a bit test for LVHT_ONITEMSTATEICON is not
       * the correct way to determine that the user has selected
       * the check box.
       */
      int index = OS.SendMessage(handle, OS.LVM_HITTEST, 0, pinfo);
      if(index != -1 && pinfo.flags == OS.LVHT_ONITEMSTATEICON) {
        LVITEM lvItem = new LVITEM();
        lvItem.mask = OS.LVIF_STATE;
        lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
        lvItem.iItem = index;
        OS.SendMessage(handle, OS.LVM_GETITEM, 0, lvItem);
        int state = lvItem.state >> 12;
        if((state & 0x1) != 0) {
          state++;
        } else {
          --state;
        }
        lvItem.state = state << 12;
        OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
      }
    }

    return result;
  }

  LRESULT WM_LBUTTONUP(int wParam, int lParam) {
    mouseDown = false;
    return super.WM_LBUTTONUP(wParam, lParam);
  }

  LRESULT WM_MOUSEHOVER(int wParam, int lParam) {
    /*
     * Feature in Windows.  Despite the fact that hot
     * tracking is not enabled, the hot tracking code
     * in WM_MOUSEHOVER is executed causing the item
     * under the cursor to be selected.  The fix is to
     * avoid calling the window proc.
     */
    LRESULT result = super.WM_MOUSEHOVER(wParam, lParam);
    int bits = OS.SendMessage(handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
    int mask = OS.LVS_EX_ONECLICKACTIVATE | OS.LVS_EX_TRACKSELECT |
        OS.LVS_EX_TWOCLICKACTIVATE;
    if((bits & mask) != 0) {
      return result;
    }
    return LRESULT.ZERO;
  }

  LRESULT WM_NOTIFY(int wParam, int lParam) {
    NMHDR hdr = new NMHDR();
    OS.MoveMemory(hdr, lParam, NMHDR.sizeof);
    int hwndHeader = OS.SendMessage(handle, OS.LVM_GETHEADER, 0, 0);
    if(hdr.hwndFrom == hwndHeader) {
      /*
       * Feature in Windows.  On NT, the automatically created
       * header control is created as a UNICODE window, not an
       * ANSI window despite the fact that the parent is created
       * as an ANSI window.  This means that it sends UNICODE
       * notification messages to the parent window on NT for
       * no good reason.  The data and size in the NMHEADER and
       * HDITEM structs is identical between the platforms so no
       * different message is actually necessary.  Despite this,
       * Windows sends different messages.  The fix is to look
       * for both messages, despite the platform.  This works
       * because only one will be sent on either platform, never
       * both.
       */
      switch(hdr.code) {
        case OS.HDN_BEGINTRACKW:
        case OS.HDN_BEGINTRACKA:
        case OS.HDN_DIVIDERDBLCLICKW:
        case OS.HDN_DIVIDERDBLCLICKA: {
          NMHEADER phdn = new NMHEADER();
          OS.MoveMemory(phdn, lParam, NMHEADER.sizeof);
          TableColumn column = columns[phdn.iItem];
          if(column != null && !column.getResizable()) {
            return LRESULT.ONE;
          }
          break;
        }
        case OS.HDN_ITEMCHANGEDW:
        case OS.HDN_ITEMCHANGEDA: {
          NMHEADER phdn = new NMHEADER();
          OS.MoveMemory(phdn, lParam, NMHEADER.sizeof);
          Event event = new Event();
          if(phdn.pitem != 0) {
            HDITEM pitem = new HDITEM();
            OS.MoveMemory(pitem, phdn.pitem, HDITEM.sizeof);
            if((pitem.mask & OS.HDI_WIDTH) != 0) {
              TableColumn column = columns[phdn.iItem];
              if(column != null) {
                column.sendEvent(SWT.Resize, event);
                /*
                 * It is possible (but unlikely), that application
                 * code could have disposed the widget in the resize
                 * event.  If this happens, end the processing of the
                 * Windows message by returning zero as the result of
                 * the window proc.
                 */
                if(isDisposed()) {
                  return LRESULT.ZERO;
                }
                int count = OS.SendMessage(hwndHeader, OS.HDM_GETITEMCOUNT, 0,
                                           0);
                if(count == 1 && columns[0] == null) {
                  count = 0;
                  /*
                   * It is possible (but unlikely), that application
                   * code could have disposed the column in the move
                   * event.  If this happens, process the move event
                   * for those columns that have not been destroyed.
                   */
                }
                TableColumn[] newColumns = new TableColumn[count];
                System.arraycopy(columns, 0, newColumns, 0, count);
                for(int i = phdn.iItem + 1; i < count; i++) {
                  if(!newColumns[i].isDisposed()) {
                    newColumns[i].sendEvent(SWT.Move, event);
                  }
                }
              }
            }
          }
          break;
        }
        case OS.HDN_ITEMDBLCLICKW:
        case OS.HDN_ITEMDBLCLICKA: {
          NMHEADER phdn = new NMHEADER();
          OS.MoveMemory(phdn, lParam, NMHEADER.sizeof);
          TableColumn column = columns[phdn.iItem];
          if(column != null) {
            column.postEvent(SWT.DefaultSelection);
          }
          break;
        }
      }
    }
    return super.WM_NOTIFY(wParam, lParam);
  }

  LRESULT WM_RBUTTONDBLCLK(int wParam, int lParam) {
    /*
     * Feature in Windows.  When the user selects outside of
     * a table item, Windows deselects all the items, even
     * when the table is multi-select.  While not strictly
     * wrong, this is unexpected.  The fix is to detect the
     * case and avoid calling the window proc.
     */
    LVHITTESTINFO pinfo = new LVHITTESTINFO();
    pinfo.x = (short)(lParam & 0xFFFF);
    pinfo.y = (short)(lParam >> 16);
    OS.SendMessage(handle, OS.LVM_HITTEST, 0, pinfo);
    sendMouseEvent(SWT.MouseDown, 1, OS.WM_RBUTTONDOWN, wParam, lParam);
    sendMouseEvent(SWT.MouseDoubleClick, 1, OS.WM_RBUTTONDBLCLK, wParam, lParam);
    if(pinfo.iItem != -1) {
      callWindowProc(OS.WM_RBUTTONDBLCLK, wParam, lParam);
    }
    if(OS.GetCapture() != handle) {
      OS.SetCapture(handle);
    }
    return LRESULT.ZERO;
  }

  LRESULT WM_RBUTTONDOWN(int wParam, int lParam) {
    /*
     * Feature in Windows.  For some reason, capturing
     * the mouse after processing the mouse event for the
     * widget interferes with the normal mouse processing
     * for the widget.  The fix is to avoid the automatic
     * mouse capture.
     */
    return sendMouseDownEvent(SWT.MouseDown, 3, OS.WM_RBUTTONDOWN, wParam,
                              lParam);
  }

  LRESULT WM_SETFOCUS(int wParam, int lParam) {
    LRESULT result = super.WM_SETFOCUS(wParam, lParam);
    /*
     * Bug in Windows.  For some reason, the table does
     * not set the default focus rectangle to be the first
     * item in the table when it gets focus and there is
     * no selected item.  The fix to make the first item
     * be the focus item.
     */
    int count = OS.SendMessage(handle, OS.LVM_GETITEMCOUNT, 0, 0);
    if(count == 0) {
      return result;
    }
    int index = OS.SendMessage(handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
    if(index == -1) {
      LVITEM lvItem = new LVITEM();
      lvItem.mask = OS.LVIF_STATE;
      lvItem.state = OS.LVIS_FOCUSED;
      lvItem.stateMask = OS.LVIS_FOCUSED;
      ignoreSelect = true;
      OS.SendMessage(handle, OS.LVM_SETITEM, 0, lvItem);
      ignoreSelect = false;
    }
    return result;
  }

  LRESULT WM_SIZE(int wParam, int lParam) {
    if(ignoreResize) {
      ignoreResize = false;
      int code = callWindowProc(OS.WM_SIZE, wParam, lParam);
      return new LRESULT(code);
    }
    return super.WM_SIZE(wParam, lParam);
  }

  LRESULT WM_SYSCOLORCHANGE(int wParam, int lParam) {
    LRESULT result = super.WM_SYSCOLORCHANGE(wParam, lParam);
    if(result != null) {
      return result;
    }
    if((style & SWT.CHECK) != 0) {
      setCheckboxImageListColor();
    }
    return result;
  }

  LRESULT wmNotifyChild(int wParam, int lParam) {
    NMHDR hdr = new NMHDR();
    OS.MoveMemory(hdr, lParam, NMHDR.sizeof);
    switch(hdr.code) {
      case OS.NM_CUSTOMDRAW: {
        if(!customDraw) {
          break;
        }
        NMLVCUSTOMDRAW nmcd = new NMLVCUSTOMDRAW();
        OS.MoveMemory(nmcd, lParam, NMLVCUSTOMDRAW.sizeof);
        switch(nmcd.dwDrawStage) {
          case OS.CDDS_PREPAINT:
            return new LRESULT(OS.CDRF_NOTIFYITEMDRAW);
          case OS.CDDS_ITEMPREPAINT:
            return new LRESULT(OS.CDRF_NOTIFYSUBITEMDRAW);
          case OS.CDDS_ITEMPREPAINT | OS.CDDS_SUBITEM: {
            TableItem item = items[nmcd.dwItemSpec];
            int clrText = item.foreground, clrTextBk = item.background;
            if(clrText == -1 && clrTextBk == -1) {
              break;
            }
            nmcd.clrText = clrText == -1 ? getForegroundPixel() : clrText;
            nmcd.clrTextBk = clrTextBk == -1 ? getBackgroundPixel() : clrTextBk;
            OS.MoveMemory(lParam, nmcd, NMLVCUSTOMDRAW.sizeof);
            return new LRESULT(OS.CDRF_NEWFONT);
          }
        }
        break;
      }
      case OS.LVN_MARQUEEBEGIN:
        return LRESULT.ONE;
      case OS.LVN_BEGINDRAG:
      case OS.LVN_BEGINRDRAG: {
        dragStarted = true;
        if(hdr.code == OS.LVN_BEGINDRAG) {
          postEvent(SWT.DragDetect);
        }
        break;
      }
      case OS.LVN_COLUMNCLICK: {
        NMLISTVIEW pnmlv = new NMLISTVIEW();
        OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
        TableColumn column = columns[pnmlv.iSubItem];
        if(column != null) {
          column.postEvent(SWT.Selection);
        }
        break;
      }
      case OS.LVN_ITEMACTIVATE: {
        NMLISTVIEW pnmlv = new NMLISTVIEW();
        OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
        if(pnmlv.iItem != -1) {
          Event event = new Event();
          event.item = items[pnmlv.iItem];
          postEvent(SWT.DefaultSelection, event);
        }
        break;
      }
      case OS.LVN_ITEMCHANGED: {
        if(!ignoreSelect) {
          NMLISTVIEW pnmlv = new NMLISTVIEW();
          OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
          if(pnmlv.iItem != -1 && (pnmlv.uChanged & OS.LVIF_STATE) != 0) {
            int oldBits = pnmlv.uOldState & OS.LVIS_STATEIMAGEMASK;
            int newBits = pnmlv.uNewState & OS.LVIS_STATEIMAGEMASK;
            if(oldBits != newBits) {
              Event event = new Event();
              event.item = items[pnmlv.iItem];
              event.detail = SWT.CHECK;
              /*
               * This code is intentionally commented.
               */
//						OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, pnmlv.iItem, 0);
              postEvent(SWT.Selection, event);
            } else {
              boolean isFocus = (pnmlv.uNewState & OS.LVIS_FOCUSED) != 0;
              int index = OS.SendMessage(handle, OS.LVM_GETNEXTITEM, -1,
                                         OS.LVNI_FOCUSED);
              if((style & SWT.MULTI) != 0) {
                if(OS.GetKeyState(OS.VK_CONTROL) < 0) {
                  if(!isFocus) {
                    if(index == pnmlv.iItem) {
                      boolean isSelected = (pnmlv.uNewState & OS.LVIS_SELECTED) !=
                          0;
                      boolean wasSelected = (pnmlv.uOldState & OS.LVIS_SELECTED) !=
                          0;
                      isFocus = isSelected != wasSelected;
                    }
                  } else {
                    isFocus = mouseDown;
                  }
                }
              }
              if(OS.GetKeyState(OS.VK_SPACE) < 0) {
                isFocus = true;
              }
              if(isFocus) {
                Event event = new Event();
                if(index != -1) {
                  /*
                   * This code is intentionally commented.
                   */
//								OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
                  event.item = items[index];
                }
                postEvent(SWT.Selection, event);
              }
            }
          }
        }
        break;
      }
    }
    return super.wmNotifyChild(wParam, lParam);
  }

}
