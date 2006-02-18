/*
 * @(#)JTreeTable.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.plaf.UIResource;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class JTreeTable extends JPanel implements Scrollable {

  protected JTable table;
  protected JTree tree;

  protected ListSelectionModel tableSelectionModel = new DefaultListSelectionModel() {
//    public void addListSelectionListener(ListSelectionListener x) {
//      
//    }
    public void addSelectionInterval(int index0, int index1) {
      super.addSelectionInterval(index0, index1);
      boolean isReversed = index0 > index1;
      if(isReversed) {
        int tmp = index1;
        index1 = index0;
        index0 = tmp;
      }
      TreePath[] treePaths = new TreePath[index1 + 1 - index0];
      for(int i=index0; i<=index1; i++) {
        treePaths[i - index0] = tree.getPathForRow(isReversed? index1 + index0 - i: i);
      }
      tree.getSelectionModel().addSelectionPaths(treePaths);
    }
    public void clearSelection() {
      super.clearSelection();
      tree.getSelectionModel().clearSelection();
    }
    public void setSelectionInterval(int index0, int index1) {
      super.setSelectionInterval(index0, index1);
      boolean isReversed = index0 > index1;
      if(isReversed) {
        int tmp = index1;
        index1 = index0;
        index0 = tmp;
      }
      TreePath[] treePaths = new TreePath[index1 + 1 - index0];
      for(int i=index0; i<=index1; i++) {
        treePaths[i - index0] = tree.getPathForRow(isReversed? index1 + index0 - i: i);
      }
      tree.getSelectionModel().setSelectionPaths(treePaths);
    }
    public void removeSelectionInterval(int index0, int index1) {
      super.removeSelectionInterval(index0, index1);
      boolean isReversed = index0 > index1;
      if(isReversed) {
        int tmp = index1;
        index1 = index0;
        index0 = tmp;
      }
      TreePath[] treePaths = new TreePath[index1 + 1 - index0];
      for(int i=index0; i<=index1; i++) {
        treePaths[i - index0] = tree.getPathForRow(isReversed? index1 + index0 - i: i);
      }
      tree.getSelectionModel().removeSelectionPaths(treePaths);
    }
    public int getMinSelectionIndex() {
      return tree.getSelectionModel().getMinSelectionRow();
    }
    public int getMaxSelectionIndex() {
      return tree.getSelectionModel().getMaxSelectionRow();
    }
    public boolean isSelectedIndex(int index) {
      return tree.getSelectionModel().isRowSelected(index);
    }
  };

  protected class TreeTableModel implements TableModel { 

    public int getRowCount() {
      return tree.getRowCount();
    }

    public int getColumnCount() {
      return getColumnModel().getColumnCount();
    }

    public String getColumnName(int columnIndex) {
      return null;
//      Object headerValue = getColumnModel().getColumn(columnIndex).getHeaderValue();
//      return headerValue == null? null: headerValue.toString();
    }

    public Class getColumnClass(int columnIndex) {
      return null;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
      TreePath path = tree.getPathForRow(rowIndex);
      TreeNode node = (TreeNode)path.getLastPathComponent();
      if(node instanceof TreeTableNode) {
        return ((TreeTableNode)node).getUserObject(columnIndex);
      }
      if(columnIndex == 0 && node instanceof DefaultMutableTreeNode) {
        return ((DefaultMutableTreeNode)node).getUserObject();
      }
      return null;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    protected EventListenerList listenerList = new EventListenerList();

    public void addTableModelListener(TableModelListener l) {
      listenerList.add(TableModelListener.class, l);
    }

    public void removeTableModelListener(TableModelListener l) {
      listenerList.remove(TableModelListener.class, l);
    }
    
    public void fireTableChanged(TableModelEvent e) {
      Object[] listeners = listenerList.getListenerList();
      for(int i = listeners.length-2; i>=0; i-=2) {
        if (listeners[i]==TableModelListener.class) {
          ((TableModelListener)listeners[i+1]).tableChanged(e);
        }
      }
    }

  };

  protected TreeTableModel tableModel = new TreeTableModel();

  protected TableCellRenderer tableCellRenderer = new TableCellRenderer() {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      final TreePath path = tree.getPathForRow(row);
      column = table.convertColumnIndexToModel(column);
      if(column == 0) {
        JComponent c = new JComponent() {
          public void paint(Graphics g) {
            int row = tree.getRowForPath(path);
            Rectangle rowBounds = tree.getRowBounds(row);
            rowBounds.width += rowBounds.x;
            rowBounds.x = 0;
            g = g.create();
            int width = JTreeTable.this.table.getColumnModel().getColumn(JTreeTable.this.table.convertColumnIndexToView(0)).getWidth();
            g.clipRect(0, 0, width, rowBounds.height);
            if(isFullLineSelection() && JTreeTable.this.table.isRowSelected(row)) {
              g.setColor(JTreeTable.this.table.getSelectionBackground());
              g.fillRect(0, 0, width, rowBounds.height);
            }
            g.translate(0, -rowBounds.y);
            tree.paint(g);
            g.dispose();
          }
        };
        return c;
      }
      TreeNode node = (TreeNode)path.getLastPathComponent();
      if(node instanceof TreeTableNode) {
        value = ((TreeTableNode)node).getUserObject(column);
      }
      hasFocus = false;
      if(!isFullLineSelection()) {
        isSelected = false;
      }
      return renderer.getTreeTableCellRendererComponent(JTreeTable.this, value, isSelected, tree.isExpanded(row), node.isLeaf(), row, column, hasFocus);
    }
  };

  class CTable extends JTable {

    protected CTable() {
      enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
      setSelectionModel(tableSelectionModel);
    }

    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
      Rectangle cellBounds = super.getCellRect(row, column, includeSpacing);
      Rectangle rowBounds = tree.getRowBounds(row);
//      rowBounds.width += rowBounds.x;
//      rowBounds.x = 0;
      if(rowBounds == null) {
        return cellBounds;
      }
      cellBounds.y = rowBounds.y;
      cellBounds.y += row * getIntercellSpacing().height;
      return cellBounds;
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
      return tableCellRenderer;
    }

    public int getRowHeight(int row) {
      return tree.getRowBounds(row).height + getRowMargin();
    }

    public void reshape(int x, int y, int w, int h) {
      super.reshape(x, y, w, h);
      tree.reshape(x, y, w, h);
    }

    public int rowAtPoint(Point point) {
      int row = tree.getClosestRowForLocation(0, point.y);
      if(row == -1) {
        return -1;
      }
      Rectangle rect = getCellRect(getRowCount() - 1, 0, false);
      if(point.y > rect.y + rect.height) {
        return -1;
      }
      while(getCellRect(row, 0, false).y > point.y) {
        row--;
        if(row == -1) {
          return -1;
        }
      }
      return row;
    }

    protected void processEvent(AWTEvent e) {
      if(e instanceof MouseEvent) {
        MouseEvent me = (MouseEvent)e;
        Point point = me.getPoint();
        int row = rowAtPoint(point);
        Rectangle treeRect = getCellRect(row, convertColumnIndexToView(0), false);
        if(!treeRect.contains(point)) {
          if(isFullLineSelection()) {
            super.processEvent(e);
          }
          return;
        }
        int xOffset = treeRect.x;
        int yOffset = row * getIntercellSpacing().height;
        int x = me.getX() - xOffset;
        int y = me.getY() - yOffset;
        Rectangle rowBounds = tree.getRowBounds(row);
        if(rowBounds != null) {
          if(!rowBounds.contains(x, y)) {
            me = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), x, y, me.getClickCount(), me.isPopupTrigger(), me.getButton());
            boolean isExpanded = tree.isExpanded(row);
            tree.dispatchEvent(me);
            if(isFullLineSelection() && tree.isExpanded(row) == isExpanded) {
              super.processEvent(e);
            }
          } else {
            if(processMouseOnTreeRenderer(row, new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), x - rowBounds.x, y - rowBounds.y, me.getClickCount(), me.isPopupTrigger(), me.getButton()), new Dimension(rowBounds.width, rowBounds.height))) {
//              System.err.println(me.getPoint() + ", " + xOffset + ", " + x + ", " + rowBounds);
              super.processEvent(me);
            }
          }
        } else {
          super.processEvent(e);
        }
//        if(!rowBounds.contains(x, y)) {
//          super.processEvent(e);
//        }
//        if(me.getID() != MouseEvent.MOUSE_DRAGGED) {
//        }
      } else if(e instanceof KeyEvent) {
        tree.dispatchEvent(e);
        super.processEvent(e);
      } else {
        super.processEvent(e);
      }
      repaint();
    }

  }

  public JTreeTable(TreeModel newModel) {
    this();
    setModel(newModel);
  }

  public JTreeTable() {
    super(new BorderLayout(0, 0));
    table = new CTable();
    add(table, BorderLayout.CENTER);
    tree = new JTree() {
      public boolean hasFocus() {
        return table.hasFocus();
      }
    };
    tree.setOpaque(false);
    tree.setCellRenderer(new TreeCellRenderer() {
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        return renderer.getTreeTableCellRendererComponent(JTreeTable.this, value, selected, expanded, leaf, row, 0, !isFullLineSelection() && hasFocus);
      }
    });
    tree.addTreeExpansionListener(new TreeExpansionListener() {
      protected int countNodeElements(TreePath treePath) {
        int count = 1;
        if(tree.hasBeenExpanded(treePath)) {
          TreeNode node = (TreeNode)treePath.getLastPathComponent();
          Object[] path = treePath.getPath();
          for(int i=0; i<node.getChildCount(); i++) {
            Object[] childPath = new Object[path.length + 1];
            System.arraycopy(path, 0, childPath, 0, path.length);
            childPath[path.length] = node.getChildAt(i);
            count += countNodeElements(new TreePath(childPath));
          }
        }
        return count;
      }
      public void treeCollapsed(TreeExpansionEvent event) {
        TreePath path = event.getPath();
        int collapsedCount = countNodeElements(path) - 1;
        if(collapsedCount != 0) {
          int firstRow = tree.getRowForPath(path);
          tableModel.fireTableChanged(new TableModelEvent(tableModel, firstRow + 1, firstRow + collapsedCount, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
        }
//        tableModel.fireTableChanged(new TableModelEvent(tableModel));
      }
      public void treeExpanded(TreeExpansionEvent event) {
        TreePath path = event.getPath();
        int expandedCount = countNodeElements(path) - 1;
        if(expandedCount != 0) {
          int firstRow = tree.getRowForPath(path);
          tableModel.fireTableChanged(new TableModelEvent(tableModel, firstRow + 1, firstRow + expandedCount, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
        }
//        tableModel.fireTableChanged(new TableModelEvent(tableModel));
      }
    });
    table.setModel(tableModel);
    setGridVisible(true);
  }

  protected boolean isGridVisible = false;

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

  public void setModel(TreeModel newModel) {
    tree.setModel(newModel);
  }

  public TreeModel getModel() {
    return tree.getModel();
  }

  protected TreeTableCellRenderer renderer = new DefaultTreeTableCellRenderer();

  public void setCellRenderer(TreeTableCellRenderer renderer) {
    this.renderer = renderer;
  }

  public TreeTableCellRenderer getCellRenderer() {
    return renderer;
  }

  protected JTree getTree() {
    return tree;
  }

  public void updateUI() {
    super.updateUI();
    if(tree != null) {
      tree.updateUI();
    }
  }

  public TableColumnModel getColumnModel() {
    return table.getColumnModel();
  }

  public void setColumnModel(TableColumnModel columnModel) {
    table.setColumnModel(columnModel);
  }

  public Dimension getPreferredScrollableViewportSize() {
    return table.getPreferredScrollableViewportSize();
  }

  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    return table.getScrollableUnitIncrement(visibleRect, orientation, direction);
  }

  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
    return table.getScrollableBlockIncrement(visibleRect, orientation, direction);
  }

  public boolean getScrollableTracksViewportWidth() {
    return table.getScrollableTracksViewportWidth();
  }

  public boolean getScrollableTracksViewportHeight() {
    return table.getScrollableTracksViewportHeight();
  }

  public void addNotify() {
    super.addNotify();
    configureEnclosingScrollPane();
  }

  protected void configureEnclosingScrollPane() {
    Container parent = getParent();
    if (parent instanceof JViewport) {
      Container grandParent = parent.getParent();
      if (grandParent instanceof JScrollPane) {
        JScrollPane scrollPane = (JScrollPane)grandParent;
        JViewport viewport = scrollPane.getViewport();
        if (viewport == null || viewport.getView() != this) {
          return;
        }
        scrollPane.setColumnHeaderView(getTableHeader());
        Border border = scrollPane.getBorder();
        if (border == null || border instanceof UIResource) {
          scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
        }
      }
    }
  }

  public void removeNotify() {
    unconfigureEnclosingScrollPane();
    super.removeNotify();
  }

  protected void unconfigureEnclosingScrollPane() {
    Container parent = getParent();
    if (parent instanceof JViewport) {
      Container grandParent = parent.getParent();
      if (grandParent instanceof JScrollPane) {
        JScrollPane scrollPane = (JScrollPane)grandParent;
        JViewport viewport = scrollPane.getViewport();
        if (viewport == null || viewport.getView() != this) {
          return;
        }
        scrollPane.setColumnHeaderView(null);
      }
    }
  }

  public JTableHeader getTableHeader() {
    return table.getTableHeader();
  }

  protected boolean isFullLineSelection = false;

  public void setFullLineSelection(boolean isFullLineSelection) {
    this.isFullLineSelection = isFullLineSelection;
    repaint();
  }

  public boolean isFullLineSelection() {
    return isFullLineSelection;
  }

  public TreeSelectionModel getSelectionModel() {
    return tree.getSelectionModel();
  }

  public void clearSelection() {
    tree.clearSelection();
  }

  public void selectAll() {
    int rowCount = tree.getRowCount();
    if(rowCount > 0) {
      tree.setSelectionInterval(0, rowCount - 1);
    }
  }

  public void expandedPath(TreePath treePath) {
    tree.expandPath(treePath);
  }

  public void collapsePath(TreePath treePath) {
    tree.collapsePath(treePath);
  }

  public boolean isExpanded(TreePath path) {
    return tree.isExpanded(path);
  }

  public void setSelectionMode(int mode) {
    tree.getSelectionModel().setSelectionMode(mode);
    switch(mode) {
    case TreeSelectionModel.SINGLE_TREE_SELECTION:
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      break;
    case TreeSelectionModel.CONTIGUOUS_TREE_SELECTION:
      table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
      break;
    case TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION:
      table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      break;
    }
  }

  public TreePath getPathForRow(int row) {
    return tree.getPathForRow(row);
  }

  public boolean isRowSelected(int row) {
    return tree.isRowSelected(row);
  }

  /**
   * @return True if the event should be dispatched as if it was not processed by the tree renderer.
   */
  protected boolean processMouseOnTreeRenderer(int row, MouseEvent e, Dimension cellSize) {
    return true;
  }

}