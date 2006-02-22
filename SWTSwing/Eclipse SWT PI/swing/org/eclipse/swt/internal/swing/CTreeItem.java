/*
 * @(#)CToolItem.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.tree.TreeNode;

import org.eclipse.swt.widgets.TreeItem;

class CTreeItemImplementation extends DefaultMutableTreeTableNode implements CTreeItem {

  protected DefaultMutableTreeTableNode mutableTreeTableNode;

  protected TreeItem handle;

  public CTreeItemImplementation(TreeItem treeItem, int style) {
    setUserObjects(new Object[] {new TreeItemObject(this)});
    handle = treeItem;
    init(style);
  }

  public TreeItem getTreeItem() {
    return handle;
  }

  protected void init(int style) {
  }

  public TreeItemObject getTreeItemObject(int index) {
    TreeItemObject treeItemObject = (TreeItemObject)getUserObject(index);
    if(treeItemObject == null) {
      treeItemObject = new TreeItemObject(this);
      setUserObject(index, treeItemObject);
    }
    return treeItemObject;
  }

  protected boolean isChecked;

  public void setChecked(boolean isChecked) {
    this.isChecked = isChecked;
  }

  public boolean isChecked() {
    return isChecked;
  }

  protected boolean isGrayed;

  public void setGrayed(boolean isGrayed) {
    this.isGrayed = isGrayed;
  }

  public boolean isGrayed() {
    return isGrayed;
  }

  public void insertColumn(int index) {
    Object[] objects = getUserObjects();
    Object[] newObjects = new Object[objects.length + 1];
    System.arraycopy(objects, 0, newObjects, 0, index);
    System.arraycopy(objects, index, newObjects, index + 1, objects.length - index);
    newObjects[index] = new TreeItemObject(this);
    setUserObject(newObjects);
  }

  public void removeColumn(int index) {
    Object[] objects = getUserObjects();
    Object[] newObjects = new Object[objects.length - 1];
    System.arraycopy(objects, 0, newObjects, 0, index);
    System.arraycopy(objects, index + 1, newObjects, index, newObjects.length - index);
    setUserObject(newObjects);
  }

}

public interface CTreeItem {

  public static class TreeItemObject {

    protected CTreeItem treeItem;

    public CTreeItem getTreeItem() {
      return treeItem;
    }

    protected TreeItemObject(CTreeItem treeItem) {
      this.treeItem = treeItem;
    }

    protected String text;

    public void setText(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

    protected Icon icon;

    public void setIcon(Icon icon) {
      this.icon = icon;
    }

    public Icon getIcon() {
      return icon;
    }

    protected Color background;

    public void setBackground(Color color) {
      background = color;
    }

    public Color getBackground() {
      return background;
    }

    protected Color foreground;

    public void setForeground(Color color) {
      foreground = color;
    }

    public Color getForeground() {
      return foreground;
    }

    protected Font font;

    public void setFont(Font font) {
      this.font = font;
    }

    public Font getFont() {
      return font;
    }

    public String toString() {
      return getText();
    }

    public boolean isChecked() {
      return treeItem.isChecked();
    }

    public boolean isGrayed() {
      return treeItem.isGrayed();
    }

  }

  public static class Instanciator {
    private Instanciator() {}

    public static CTreeItem createInstance(TreeItem treeItem, int style) {
      return new CTreeItemImplementation(treeItem, style);
    }

  }

  public TreeNode[] getPath();

  public TreeItemObject getTreeItemObject(int index);

  public int getChildCount();

  public void setChecked(boolean isChecked);

  public boolean isChecked();

  public void setGrayed(boolean isGrayed);

  public boolean isGrayed();

  public void insertColumn(int index);

  public void removeColumn(int index);

  public TreeItem getTreeItem();

}
