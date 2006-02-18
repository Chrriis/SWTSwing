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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;

class CTreeImplementation extends JScrollPane implements CTree {

  protected Tree handle;
  protected JTreeTable treeTable;
  protected DefaultMutableTreeTableNode rootNode;
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

  public CTreeImplementation(Tree tree, int style) {
    handle = tree;
    rootNode = new DefaultMutableTreeTableNode() {
      public void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
        getModel().nodesWereInserted(this, new int[] {childIndex});
        treeTable.expandedPath(new TreePath(rootNode.getPath()));
      }
    };
    treeTable = new JTreeTable(new DefaultTreeModel(rootNode)) {
      public boolean getScrollableTracksViewportHeight() {
        return getPreferredSize().height < getParent().getHeight();
      }
      protected boolean processMouseOnTreeRenderer(int row, MouseEvent e, Dimension cellSize) {
        if(isCheckType) {
          TreePath treePath = treeTable.getPathForRow(row);
          DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode)treePath.getLastPathComponent();
          CheckBoxCellRenderer checkBoxCellRenderer = (CheckBoxCellRenderer)treeTable.getCellRenderer().getTreeTableCellRendererComponent(treeTable, node, treeTable.isRowSelected(row), treeTable.isExpanded(treePath), node.isLeaf(), row, 0, false);
          checkBoxCellRenderer.setSize(cellSize);
          checkBoxCellRenderer.doLayout();
          Point point = e.getPoint();
          Component component = checkBoxCellRenderer.getComponentAt(point);
          JStateCheckBox stateCheckBox = checkBoxCellRenderer.getStateCheckBox();
          if(component == stateCheckBox) {
            switch(e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
              CTreeItem.TreeItemObject treeItemObject = (CTreeItem.TreeItemObject)node.getUserObject(0);
              treeItemObject.setChecked(!treeItemObject.isChecked());
              ((DefaultTreeModel)treeTable.getModel()).nodeChanged(node);
            case MouseEvent.MOUSE_DRAGGED:
            case MouseEvent.MOUSE_RELEASED:
            case MouseEvent.MOUSE_CLICKED:
              return false;
            }
          }
        }
        return super.processMouseOnTreeRenderer(row, e, cellSize);
      }
    };
    treeTable.setCellRenderer(new DefaultTreeTableCellRenderer() {
      public Component getTreeTableCellRendererComponent(JTreeTable treeTable, Object value, boolean selected, boolean expanded, boolean leaf, int row, int column, boolean hasFocus) {
        Component c = super.getTreeTableCellRendererComponent(treeTable, value, selected, expanded, leaf, row, column, hasFocus);
        if(value == null) {
          return c;
        }
        CTreeItem.TreeItemObject treeItemObject = (CTreeItem.TreeItemObject)((DefaultMutableTreeTableNode)value).getUserObject(column);
        if(treeItemObject != null) {
          if(c instanceof JLabel) {
            ((JLabel)c).setIcon(treeItemObject.getIcon());
          }
          // TODO: Complete with other properties from treeItemObject
        }
        if(column != 0 || !isCheckType) {
          return c;
        }
        CheckBoxCellRenderer checkBoxCellRenderer = new CheckBoxCellRenderer(c);
        if(treeItemObject != null) {
          checkBoxCellRenderer.getStateCheckBox().setSelected(treeItemObject.isChecked());
        }
        return checkBoxCellRenderer;
      }
    });

    // TODO: add a first bogus column?
    javax.swing.table.TableColumnModel columnModel = treeTable.getColumnModel();
    javax.swing.table.TableColumn tableColumn = new javax.swing.table.TableColumn(0);
    columnModel.addColumn(tableColumn);

//    treeTable.expandedPath(new TreePath(rootNode.getPath()));
    treeTable.getTree().setRootVisible(false);
    treeTable.getTree().setShowsRootHandles(true);
    setFocusable(false);
    getViewport().setView(treeTable);
    setGridVisible(false);
    init(style);
  }

  protected void init(int style) {
    isCheckType = (style & SWT.CHECK) != 0;
  }

  public Container getClientArea() {
    return treeTable;
  }

  public void clearSelection() {
    treeTable.clearSelection();
  }

  public void selectAll() {
    treeTable.selectAll();
  }

  public TreeSelectionModel getSelectionModel() {
    return treeTable.getSelectionModel();
  }

  public void setGridVisible(boolean isGridVisible) {
    treeTable.setGridVisible(isGridVisible);
  }

  public boolean isGridVisible() {
    return treeTable.isGridVisible();
  }

  public JTableHeader getTableHeader() {
    return treeTable.getTableHeader();
  }

  public void expandedPath(TreePath treePath) {
    treeTable.expandedPath(treePath);
  }

  public void collapsePath(TreePath treePath) {
    treeTable.collapsePath(treePath);
  }

  public boolean isExpanded(TreePath path) {
    return treeTable.isExpanded(path);
  }

  public DefaultMutableTreeTableNode getRoot() {
    return rootNode;
  }

  public DefaultTreeModel getModel() {
    return (DefaultTreeModel)treeTable.getModel();
  }

}

public interface CTree extends CComposite {

  public static class Instanciator {
    private Instanciator() {}

    public static CTree createInstance(Tree tree, int style) {
      return new CTreeImplementation(tree, style);
    }

  }

  public void clearSelection();

  public void selectAll();

  public TreeSelectionModel getSelectionModel();

  public void setGridVisible(boolean isGridVisible);

  public boolean isGridVisible();

  public JTableHeader getTableHeader();

  public void expandedPath(TreePath treePath);

  public void collapsePath(TreePath treePath);

  public boolean isExpanded(TreePath path);

  public DefaultMutableTreeTableNode getRoot();

  public DefaultTreeModel getModel();

}
