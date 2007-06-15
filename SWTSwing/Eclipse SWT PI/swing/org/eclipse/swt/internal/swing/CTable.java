/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.PaintEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.swing.CTableItem.TableItemObject;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;

class CTableImplementation extends JScrollPane implements CTable {

  protected Table handle;
  protected JTable table;
  protected boolean isCheckType;

  public Container getSwingComponent() {
    return table;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected class CheckBoxCellRenderer extends JPanel {
    protected JStateCheckBox checkBox = new JStateCheckBox();
    public CheckBoxCellRenderer(Component c) {
      super(new BorderLayout(0, 0));
      setOpaque(false);
      checkBox.setOpaque(false);
      add(checkBox, BorderLayout.WEST);
      add(c, BorderLayout.CENTER);
      addNotify();
    }
    public JStateCheckBox getStateCheckBox() {
      return checkBox;
    }
  }

  public class CTableModel extends AbstractTableModel {
    protected Table table;
    protected CTableModel(Table table) {
      this.table = table;
    }
    public int getRowCount() {
//      if(table.isDisposed()) return 0;
//      return table.getItemCount();
      return rowCount;
    }
    public int getColumnCount() {
      return table.getColumnCount();
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
      return table.getItem(rowIndex).handle.getTableItemObject(columnIndex);
    }
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CTableImplementation(Table table, int style) {
    handle = table;
    this.table = new JTable(new CTableModel(table)) {
      public boolean getScrollableTracksViewportWidth() {
        if(handle.isDisposed()) {
          return false;
        }
        return handle.getColumnCount() == 0 && getPreferredSize().width < getParent().getWidth();
      }
      public boolean getScrollableTracksViewportHeight() {
        if(handle.isDisposed()) {
          return false;
        }
        return getPreferredSize().height < getParent().getHeight();
      }
      public Dimension getPreferredScrollableViewportSize() {
        if(handle.getColumnCount() != 0) {
          return getPreferredSize();
        }
        // TODO: use some caching mecanism?
        int columnCount = getColumnCount();
        int width = 0;
        for(int i=0; i<columnCount; i++) {
          width += getPreferredColumnWidth(i);
        }
//        if(isGridVisible()) {
          width += columnCount - 1;
//        }
        // TODO: check why we need to add the columnCount again.
        width += columnCount;
        return new Dimension(width, getPreferredSize().height);
      }
      final JTable table = this;
      protected TableCellRenderer renderer = new DefaultTableCellRenderer() {
        protected boolean isInitialized;
        protected boolean isDefaultOpaque;
        protected boolean isSelectionOpaque;
        protected Color defaultForeground;
        protected Color defaultBackground;
        protected Font defaultFont;
        protected Color selectionForeground;
        protected Color selectionBackground;
        protected Font selectionFont;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          if(value instanceof CTableItem.TableItemObject) {
            tableItemObject = (CTableItem.TableItemObject)value;
            CellPaintEvent event = new CellPaintEvent(table, CellPaintEvent.ERASE_TYPE);
            event.row = row;
            event.column = column;
            event.tableItem = tableItemObject.getTableItem();
            event.ignoreDrawSelection = !isSelected;
            event.ignoreDrawFocused = !hasFocus;
            handle.processEvent(event);
            ignoreDrawForeground = event.ignoreDrawForeground;
            ignoreDrawBackground = event.ignoreDrawBackground;
            ignoreDrawSelection = event.ignoreDrawSelection;
            ignoreDrawFocused = event.ignoreDrawFocused;
            isSelected = !event.ignoreDrawSelection;
            hasFocus = !event.ignoreDrawFocused;
            this.row = row;
            this.column = column;
          } else {
            tableItemObject = null;
          }
          if(!isInitialized) {
            Component c = super.getTableCellRendererComponent(CTableImplementation.this.table, "", true, false, 0, 0);
            if(c instanceof JComponent) {
              isSelectionOpaque = ((JComponent)c).isOpaque();
            }
            selectionForeground = c.isForegroundSet()? c.getForeground(): null;
            selectionBackground = c.isBackgroundSet()? c.getBackground(): null;
            selectionFont = c.getFont();
            c.setForeground(null);
            c.setBackground(null);
          }
          if(!isInitialized) {
            Component c = super.getTableCellRendererComponent(CTableImplementation.this.table, "", false, false, 0, 0);
            if(c instanceof JComponent) {
              isDefaultOpaque = ((JComponent)c).isOpaque();
            }
            defaultForeground = c.isForegroundSet()? c.getForeground(): null;
            defaultBackground = c.isBackgroundSet()? c.getBackground(): null;
            defaultFont = c.getFont();
            isInitialized = true;
          }
          Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//          if(!isInitialized) {
//            defaultForeground = c.isForegroundSet()? c.getForeground(): null;
//            defaultBackground = c.isBackgroundSet()? c.getBackground(): null;
//            defaultFont = c.getFont();
//            isInitialized = true;
//          }
          if(value == null) {
            return c;
          }
          if(!(value instanceof CTableItem.TableItemObject)) {
            return c;
          }
          CTableItem.TableItemObject tableItemObject = (CTableItem.TableItemObject)value;
          c.setForeground(isSelected? selectionForeground: defaultForeground);
          c.setBackground(isSelected? selectionBackground: defaultBackground);
          c.setFont(isSelected? selectionFont: defaultFont);
          if(c instanceof JComponent) {
            ((JComponent)c).setOpaque(isSelected? isSelectionOpaque: isDefaultOpaque);
          }
          if(tableItemObject != null) {
            if(c instanceof JLabel) {
              TableColumn tableColumn = table.getColumnModel().getColumn(table.convertColumnIndexToView(column));
              JLabel label = (JLabel)c;
              if(tableColumn instanceof CTableColumn) {
                CTableColumn cTableColumn = (CTableColumn)tableColumn;
                label.setHorizontalAlignment(cTableColumn.getAlignment());
              }
              label.setIcon(tableItemObject.getIcon());
            }
            CTableItem cTableItem = tableItemObject.getTableItem();
            Color foreground = tableItemObject.getForeground();
            if(foreground != null) {
              c.setForeground(foreground);
            } else {
              foreground = cTableItem.getForeground();
              if(foreground != null) {
                c.setForeground(foreground);
              }
            }
            if(!isSelected) {
              Color background = tableItemObject.getBackground();
              if(background != null) {
                if(c instanceof JComponent) {
                  ((JComponent)c).setOpaque(true);
                }
                c.setBackground(background);
              } else {
                background = cTableItem.getBackground();
                if(background != null) {
                  if(c instanceof JComponent) {
                    ((JComponent)c).setOpaque(true);
                  }
                  c.setBackground(background);
                }
              }
            }
            Font font = tableItemObject.getFont();
            if(font != null) {
              c.setFont(font);
            } else {
              font = cTableItem.getFont();
              if(font != null) {
                c.setFont(font);
              }
            }
            // TODO: Complete with other properties from tableItemObject
          }
          if(column != 0 || !isCheckType) {
            return c;
          }
          CheckBoxCellRenderer checkBoxCellRenderer = new CheckBoxCellRenderer(c);
          if(tableItemObject != null) {
            checkBoxCellRenderer.getStateCheckBox().setSelected(tableItemObject.isChecked());
          }
          return checkBoxCellRenderer;
        }
        protected CTableItem.TableItemObject tableItemObject;
        protected int row;
        protected int column;
        protected boolean ignoreDrawForeground;
        protected boolean ignoreDrawBackground;
        protected boolean ignoreDrawSelection;
        protected boolean ignoreDrawFocused;
        protected void paintComponent (Graphics g) {
          if(ignoreDrawForeground) {
            setText(null);
          }
          if(ignoreDrawBackground) {
            setOpaque(false);
          }
//          graphics = g;
          super.paintComponent(g);
          if(tableItemObject != null) {
            CellPaintEvent event = new CellPaintEvent(table, CellPaintEvent.PAINT_TYPE);
            event.row = row;
            event.column = column;
            event.tableItem = tableItemObject.getTableItem();
            event.ignoreDrawForeground = this.ignoreDrawForeground;
            event.ignoreDrawBackground = this.ignoreDrawBackground;
            event.ignoreDrawSelection = this.ignoreDrawSelection;
            event.ignoreDrawFocused = this.ignoreDrawFocused;
            handle.processEvent(event);
          }
//          graphics = null;
        }
      };
      public TableCellRenderer getCellRenderer(int row, int column) {
        return renderer;
      }
      protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
          public String getToolTipText(MouseEvent e) {
            int index = columnModel.getColumnIndexAtX(e.getX());
            if(index < 0) {
              return null;
            }
            return ((CTableColumn)columnModel.getColumn(index)).getToolTipText();
          }
        };
      }
      public Color getBackground() {
        return CTableImplementation.this != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CTableImplementation.this != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CTableImplementation.this != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        return CTableImplementation.this != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
      }
      protected Graphics graphics;
      public Graphics getGraphics() {
        Graphics g;
        if(graphics != null) {
          g = graphics.create();
        } else {
          g = super.getGraphics();
        }
        return g;
      }
      protected void paintComponent (Graphics g) {
        graphics = g;
        putClientProperty(Utils.SWTSwingPaintingClientProperty, Boolean.TRUE);
        super.paintComponent(g);
//        Utils.paintTiledImage(this, g, backgroundImageIcon);
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        putClientProperty(Utils.SWTSwingPaintingClientProperty, null);
        graphics = null;
      }
      protected void processEvent(AWTEvent e) {
        if(e instanceof MouseEvent) {
          MouseEvent me = (MouseEvent)e;
          if(isCheckType) {
            Point location = me.getPoint();
            int column = table.columnAtPoint(location);
            int row = table.rowAtPoint(location);
            if(row != -1 && column != -1) {
              CheckBoxCellRenderer checkBoxCellRenderer = (CheckBoxCellRenderer)table.getCellRenderer(row, column).getTableCellRendererComponent(this, table.getValueAt(row, column), table.isCellSelected(row, column), false, row, column);
              Rectangle cellBounds = table.getCellRect(row, column, false);
              checkBoxCellRenderer.setSize(cellBounds.width, cellBounds.height);
              checkBoxCellRenderer.doLayout();
              Component component = checkBoxCellRenderer.getComponentAt(location.x - cellBounds.x, location.y - cellBounds.y);
              JStateCheckBox stateCheckBox = checkBoxCellRenderer.getStateCheckBox();
              // TODO: find a way to rely on the Look&Feel for mouse events.
              if(component == stateCheckBox) {
                switch(me.getID()) {
                case MouseEvent.MOUSE_PRESSED:
                  CTableItem cTableItem = handle.getItem(row).handle;
                  TableItemObject tableItemObject = cTableItem.getTableItemObject(0);
                  boolean ischecked = !tableItemObject.isChecked();
                  tableItemObject.getTableItem().setChecked(ischecked);
                  table.repaint();
                  handle.processEvent(new ItemEvent(stateCheckBox, ItemEvent.ITEM_STATE_CHANGED, cTableItem, ischecked? ItemEvent.SELECTED: ItemEvent.DESELECTED));
                  return;
                case MouseEvent.MOUSE_DRAGGED:
                case MouseEvent.MOUSE_RELEASED:
                case MouseEvent.MOUSE_CLICKED:
                  return;
                }
              }
            }
          }
          switch(me.getID()) {
          case MouseEvent.MOUSE_PRESSED:
            switch(me.getButton()) {
            case MouseEvent.BUTTON3: {
              // We have to assume that popup menus are triggered with the mouse button 3.
              int row = rowAtPoint(me.getPoint());
              if(row != -1) {
                ListSelectionModel selectionModel = getSelectionModel();
                if(!selectionModel.isSelectedIndex(row)) {
                  selectionModel.setSelectionInterval(row, row);
                }
              }
              break;
            }
            }
            break;
          }
        }
        super.processEvent(e);
      }
//      protected Set validItemSet = new HashSet();
//      public int getRowHeight(int row) {
//        int rowHeight = super.getRowHeight(row);
//        int columnCount = getColumnCount();
//        if(columnCount == 0 || handle.isDisposed()) {
//          return rowHeight;
//        }
//        if(adjustItemHeight) {
//          adjustItemHeight = false;
//          validItemSet.clear();
//        }
//        if(validItemSet.contains(new Integer(row))) {
//          return rowHeight;
//        }
//        validItemSet.add(new Integer(row));
//        int maxHeight = 0;
//        for(int column=0; column<columnCount; column++) {
//          Object value = handle.getItem(row).handle.getTableItemObject(column);
//          if(value instanceof CTableItem.TableItemObject) {
//            CellPaintEvent event = new CellPaintEvent(this, CellPaintEvent.MEASURE_TYPE);
//            event.row = row;
//            event.column = column;
//            event.tableItem = ((CTableItem.TableItemObject)value).getTableItem();
//            event.rowHeight = rowHeight;
//            handle.processEvent(event);
//            maxHeight = Math.max(event.rowHeight, maxHeight);
//          }
//        }
//        setRowHeight(row, maxHeight);
//        return maxHeight;
//      }
    };
    userAttributeHandler = new UserAttributeHandler(this.table);
    this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JTableHeader tableHeader = this.table.getTableHeader();
    final TableCellRenderer headerRenderer = tableHeader.getDefaultRenderer();
    tableHeader.setDefaultRenderer(new TableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value == null || "".equals(value)) {
          value = " ";
        }
        Component c = headerRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(c instanceof JLabel) {
          TableColumn tableColumn = CTableImplementation.this.table.getColumnModel().getColumn(column);
          if(tableColumn instanceof CTableColumn) {
            JLabel label = (JLabel)c;
            CTableColumn cTableColumn = (CTableColumn)tableColumn;
            label.setHorizontalAlignment(cTableColumn.getAlignment());
            label.setIcon(cTableColumn.getIcon());
          }
        }
        return c;
      }
    });
    // TODO: add a first bogus column? (1)
    javax.swing.table.TableColumnModel columnModel = this.table.getColumnModel();
    javax.swing.table.TableColumn tableColumn = new javax.swing.table.TableColumn(0);
    columnModel.addColumn(tableColumn);
    setFocusable(false);
    JViewport viewport = getViewport();
    viewport.setView(this.table);
    viewport.setBackground(this.table.getBackground());
    setColumnHeader(createViewport());
    setHeaderVisible(false);
    init(style);
  }

  protected void init(int style) {
    isCheckType = (style & SWT.CHECK) != 0;
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    } else {
      setBorder(BorderFactory.createEmptyBorder());
    }
//    if((style & SWT.H_SCROLL) == 0) {
//      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
//    }
//    if((style & SWT.V_SCROLL) == 0) {
//      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
//    }
//    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
//      setBorder(null);
//    }
    if((style & SWT.FULL_SELECTION) == 0) {
      // Not perfect because it does not prevent the selection of the first cell by clicking anywhere on the row.
      table.setCellSelectionEnabled(true);
      table.getColumnModel().setSelectionModel(new DefaultListSelectionModel() {
        public void addSelectionInterval(int index0, int index1) {
          index0 = table.convertColumnIndexToView(0);
          index1 = table.convertColumnIndexToView(0);
          super.addSelectionInterval(index0, index1);
        }
        public void setAnchorSelectionIndex(int anchorIndex) {
          anchorIndex = table.convertColumnIndexToView(0);
          super.setAnchorSelectionIndex(anchorIndex);
        }
        public void setLeadSelectionIndex(int leadIndex) {
          leadIndex = table.convertColumnIndexToView(0);
          super.setLeadSelectionIndex(leadIndex);
        }
        public void moveLeadSelectionIndex(int leadIndex) {
          leadIndex = table.convertColumnIndexToView(0);
          super.moveLeadSelectionIndex(leadIndex);
        }
        public void setSelectionInterval(int index0, int index1) {
          index0 = table.convertColumnIndexToView(0);
          index1 = table.convertColumnIndexToView(0);
          super.setSelectionInterval(index0, index1);
        }
      });
    }
    setGridVisible(false);
    if((style & SWT.MULTI) == 0) {
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    // TODO: Map other events for table header click etc.
    Utils.installMouseListener(table, handle);
    Utils.installKeyListener(table, handle);
    Utils.installFocusListener(table, handle);
    Utils.installComponentListener(this, handle);
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return table;
  }

  public JTableHeader getTableHeader() {
    return table.getTableHeader();
  }

  protected boolean isGridVisible;

  public void setGridVisible(boolean isGridVisible) {
    this.isGridVisible = isGridVisible;
    table.setIntercellSpacing(isGridVisible? new Dimension(1, 1): new Dimension(0, 0));
    table.setShowHorizontalLines(isGridVisible);
    table.setShowVerticalLines(isGridVisible);
    repaint();
  }

  public boolean isGridVisible() {
    return isGridVisible;
  }

  public DefaultListSelectionModel getSelectionModel() {
    return (DefaultListSelectionModel)table.getSelectionModel();
  }

  public AbstractTableModel getModel() {
    return (AbstractTableModel)table.getModel();
  }

  public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
    return table.getCellRect(row, column, includeSpacing);
  }

  public TableColumnModel getColumnModel() {
    return table.getColumnModel();
  }

//  protected boolean adjustItemHeight;
  protected int rowCount;

  public void addItem(int index) {
    rowCount++;
//    adjustItemHeight = true;
    getModel().fireTableRowsInserted(index, index);
  }

  public void removeItem(int index) {
//    adjustItemHeight = true;
    rowCount--;
    getModel().fireTableRowsDeleted(index, index);
  }
  
  public TableCellRenderer getCellRenderer(int row, int column) {
    return table.getCellRenderer(row, column);
  }

  public int getPreferredColumnWidth(int columnIndex) {
    int count = handle.getItemCount();
    int newWidth = Math.max(table.getColumnModel().getColumn(columnIndex).getMinWidth(), 10);
    if((handle.getStyle() & SWT.VIRTUAL) != 0) {
      // TODO: is there a way to know the preferred size? The method below generates an exception (cf Snippet144)
      return newWidth;
    }
    // TODO: is there a better way than this hack?
    for(int i=0; i<count; i++) {
      javax.swing.table.TableCellRenderer renderer = getCellRenderer(i, columnIndex);
      java.awt.Component component = renderer.getTableCellRendererComponent(table, getModel().getValueAt(i, columnIndex), false, false, i, columnIndex);
      newWidth = Math.max(newWidth, (int)component.getPreferredSize().getWidth());
    }
    return newWidth;
  }

//  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
//    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      table.setOpaque(true);
      break;
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      table.setOpaque(false);
      break;
    }
  }

  public void ensureRowVisible(int index) {
    if(index < 0 || index >= table.getRowCount()) {
      return;
    }
    Rectangle bounds = getCellRect(index, 0, true);
    bounds.width = table.getWidth();
    bounds.height = table.getRowHeight(index);
    table.scrollRectToVisible(bounds);
  }

  public void ensureColumnVisible(int index) {
    if(index < 0 || index >= table.getColumnCount()) {
      return;
    }
    Rectangle bounds = new Rectangle();
    TableColumnModel columnModel = getColumnModel();
    for(int i=0; i<index; i++) {
      bounds.x += columnModel.getColumn(i).getPreferredWidth();
    }
    bounds.width = columnModel.getColumn(index).getPreferredWidth();
    bounds.height = table.getHeight();
    table.scrollRectToVisible(bounds);
  }
  
  public void setHeaderVisible(boolean isHeaderVisible) {
    getColumnHeader().setVisible(isHeaderVisible);
    table.getTableHeader().setVisible(isHeaderVisible);
  }

  public int getRowHeight() {
    return table.getRowHeight();
  }

  public void setRowHeight(int rowHeight) {
    table.setRowHeight(rowHeight);
  }

  public int rowAtPoint(Point point) {
    point = SwingUtilities.convertPoint(this, point.x, point.y, table);
    return table.rowAtPoint(point);
  }

  public void setTopIndex(int index) {
    int rowCount = table.getRowCount();
    if(rowCount == 0) {
      return;
    }
    ensureRowVisible(rowCount - 1);
    if(index != 0) {
      ensureRowVisible(index);
    }
  }

  public int getTopIndex() {
    return rowAtPoint(new Point(0, 0));
  }
  
  public void moveColumn(int column, int targetColumn) {
    table.moveColumn(column, targetColumn);
  }

  public void setFont(Font font) {
    super.setFont(font);
    if(table != null) {
      table.setFont(font);
    }
  }

  public void setForeground(Color foreground) {
    super.setForeground(foreground);
    if(table != null) {
      table.setForeground(foreground);
    }
  }

  public void setBackground(Color background) {
    super.setBackground(background);
    getViewport().setBackground(background);
    if(table != null) {
      table.setBackground(background);
    }
  }
  
  public boolean isFocusable() {
    return table.isFocusable();
  }

  public void requestFocus() {
    table.requestFocus();
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    table.setEnabled(enabled);
  }

}

public interface CTable extends CComposite {

  public static class CellPaintEvent extends EventObject {
  
    public static final int ERASE_TYPE = 1;
    public static final int PAINT_TYPE = 2;
    public static final int MEASURE_TYPE = 3;
    protected int type;
    public int row;
    public int column;
    public CTableItem tableItem;
    public boolean ignoreDrawForeground;
    public boolean ignoreDrawBackground;
    public boolean ignoreDrawSelection;
    public boolean ignoreDrawFocused;
    public int rowHeight;

    CellPaintEvent(Object source, int type) {
      super(source);
      this.type = type;
    }

    public int getType() {
      return type;
    }
    
  }
  
  
  public static class Factory {
    private Factory() {}

    public static CTable newInstance(Table table, int style) {
      return new CTableImplementation(table, style);
    }

  }

  public JTableHeader getTableHeader();

  public void setGridVisible(boolean isGridVisible);

  public boolean isGridVisible();

  public DefaultListSelectionModel getSelectionModel();

  public AbstractTableModel getModel();

  public Rectangle getCellRect(int row, int column, boolean includeSpacing);

  public TableColumnModel getColumnModel();

  public void addItem(int index);

  public void removeItem(int index);

  public TableCellRenderer getCellRenderer(int row, int column);

  public int getPreferredColumnWidth(int columnIndex);

  public void ensureRowVisible(int index);

  public void ensureColumnVisible(int index);

  public void setHeaderVisible(boolean isColumnHeaderVisible);

  public int getRowHeight();

  public void setRowHeight(int rowHeight);

  public int rowAtPoint(Point point);

  public void setTopIndex(int index);

  public int getTopIndex();
  
  public void moveColumn(int column, int targetColumn);

}
