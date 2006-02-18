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
    super(new Object[] {new TreeItemObject()});
    handle = treeItem;
    init(style);
  }

  protected void init(int style) {
  }

  public TreeItemObject getTreeItemObject(int index) {
    TreeItemObject treeItemObject = (TreeItemObject)getUserObject(index);
    if(treeItemObject == null) {
      treeItemObject = new TreeItemObject();
      setUserObject(index, treeItemObject);
    }
    return treeItemObject;
  }

}

public interface CTreeItem {

  public static class TreeItemObject {

    protected TreeItemObject() {
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

    protected boolean isChecked;

    public void setChecked(boolean isChecked) {
      this.isChecked = isChecked;
    }

    public boolean isChecked() {
      return isChecked;
    }

    public String toString() {
      return getText();
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

}
