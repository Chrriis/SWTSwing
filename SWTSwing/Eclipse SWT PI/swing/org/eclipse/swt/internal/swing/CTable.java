/*
 * @(#)CTree.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

class CTableImplementation extends JScrollPane implements CTable {

  protected Table handle;
  protected JTable table;
  protected boolean isCheckType;

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
      return itemList.size();
    }
    public int getColumnCount() {
      return table.getColumnCount();
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
      return ((CTableItem)itemList.get(rowIndex)).getTableItemObject(columnIndex);
    }
  }

  public CTableImplementation(Table table, int style) {
    handle = table;
    this.table = new JTable(new CTableModel(table)) {
      public boolean getScrollableTracksViewportWidth() {
        return handle.getColumnCount() == 0 && getPreferredSize().width < getParent().getWidth();
      }
      public boolean getScrollableTracksViewportHeight() {
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
      protected TableCellRenderer renderer = new DefaultTableCellRenderer() {
        protected boolean isInitialized;
        protected boolean isOpaque;
        protected Color defaultForeground;
        protected Color defaultBackground;
        protected Font defaultFont;
        protected Color selectionForeground;
        protected Color selectionBackground;
        protected Font selectionFont;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          if(!isInitialized) {
            Component c = super.getTableCellRendererComponent(CTableImplementation.this.table, "", true, false, 0, 0);
            if(c instanceof JComponent) {
              isOpaque = ((JComponent)c).isOpaque();
            }
            selectionForeground = c.getForeground();
            selectionBackground = c.getBackground();
            selectionFont = c.getFont();
          }
          Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          if(!isInitialized) {
            defaultForeground = c.getForeground();
            defaultBackground = c.getBackground();
            defaultFont = c.getFont();
            isInitialized = true;
          }
          if(value == null) {
            return c;
          }
          if(!(value instanceof CTableItem.TableItemObject)) {
            return c;
          }
          c.setForeground(isSelected? selectionForeground: defaultForeground);
          c.setBackground(isSelected? selectionBackground: defaultBackground);
          c.setFont(isSelected? selectionFont: defaultFont);
          if(c instanceof JComponent) {
            ((JComponent)c).setOpaque(isOpaque);
          }
          CTableItem.TableItemObject tableItemObject = (CTableItem.TableItemObject)value;
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
            Color foreground = tableItemObject.getForeground();
            if(foreground != null) {
              c.setForeground(foreground);
            } else {
              foreground = tableItemObject.getTableItem().getForeground();
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
                background = tableItemObject.getTableItem().getBackground();
                if(background != null) {
                  c.setBackground(background);
                }
              }
            }
            Font font = tableItemObject.getFont();
            if(font != null) {
              c.setFont(font);
            } else {
              font = tableItemObject.getTableItem().getFont();
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
      };
      public TableCellRenderer getCellRenderer(int row, int column) {
        return renderer;
      }
    };
    this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JTableHeader tableHeader = this.table.getTableHeader();
    final TableCellRenderer headerRenderer = tableHeader.getDefaultRenderer();
    tableHeader.setDefaultRenderer(new TableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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
    init(style);
  }

  protected void init(int style) {
    isCheckType = (style & SWT.CHECK) != 0;
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    } else {
      setBorder(null);
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
    setGridVisible(false);
    if((style & SWT.MULTI) == 0) {
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    // TODO: continue initialisation with event handling
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

  protected ArrayList itemList = new ArrayList();

  public void addItem(CTableItem tableItem, int index) {
    itemList.add(index, tableItem);
    getModel().fireTableRowsInserted(index, index);
  }

  public void removeItem(int index) {
    itemList.remove(index);
    getModel().fireTableRowsDeleted(index, index);
  }
  
  public TableCellRenderer getCellRenderer(int row, int column) {
    return table.getCellRenderer(row, column);
  }

  public int getPreferredColumnWidth(int columnIndex) {
    int count = itemList.size();
    int newWidth = Math.max(table.getColumnModel().getColumn(columnIndex).getMinWidth(), 10);
    // TODO: is there a better way than this hack?
    for(int i=0; i<count; i++) {
      javax.swing.table.TableCellRenderer renderer = getCellRenderer(i, columnIndex);
      java.awt.Component component = renderer.getTableCellRendererComponent(table, getModel().getValueAt(i, columnIndex), false, false, i, columnIndex);
      newWidth = Math.max(newWidth, (int)component.getPreferredSize().getWidth());
    }
    return newWidth;
  }

}

public interface CTable extends CComposite {

  public static class Instanciator {
    private Instanciator() {}

    public static CTable createInstance(Table table, int style) {
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

  public void addItem(CTableItem tableItem, int index);

  public void removeItem(int index);

  public TableCellRenderer getCellRenderer(int row, int column);

  public int getPreferredColumnWidth(int columnIndex);

}
