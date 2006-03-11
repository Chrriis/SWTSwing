/*
 * @(#)CTableItem.java
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

import org.eclipse.swt.widgets.TableItem;

class CTableItemImplementation implements CTableItem {

  protected TableItem handle;

  public CTableItemImplementation(TableItem tableItem, int style) {
    setUserObjects(new TableItemObject[] {new TableItemObject(this)});
    handle = tableItem;
    init(style);
  }

  public TableItem getTableItem() {
    return handle;
  }

  protected void init(int style) {
  }

  public TableItemObject getTableItemObject(int index) {
    TableItemObject tableItemObject = getUserObject(index);
    if(tableItemObject == null) {
      tableItemObject = new TableItemObject(this);
      setUserObject(index, tableItemObject);
    }
    return tableItemObject;
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
    TableItemObject[] objects = getUserObjects();
    TableItemObject[] newObjects = new TableItemObject[objects.length + 1];
    System.arraycopy(objects, 0, newObjects, 0, index);
    System.arraycopy(objects, index, newObjects, index + 1, objects.length - index);
    newObjects[index] = new TableItemObject(this);
    setUserObjects(newObjects);
  }

  public void removeColumn(int index) {
    TableItemObject[] objects = getUserObjects();
    TableItemObject[] newObjects = new TableItemObject[objects.length - 1];
    System.arraycopy(objects, 0, newObjects, 0, index);
    System.arraycopy(objects, index + 1, newObjects, index, newObjects.length - index);
    setUserObjects(newObjects);
  }

  protected TableItemObject[] userObjects;

  protected void setUserObjects(TableItemObject[] userObjects) {
    this.userObjects = userObjects;
  }

  protected TableItemObject[] getUserObjects() {
    return userObjects;
  }

  protected TableItemObject getUserObject(int index) {
    if(userObjects == null) {
      return null;
    }
    if(index < userObjects.length) {
      return userObjects[index];
    }
    return null;
  }

  protected void setUserObject(int index, TableItemObject userObject) {
    if(userObjects == null) {
      userObjects = new TableItemObject[index + 1];
    } else if(userObjects.length <= index) {
      TableItemObject[] newUserObjects = new TableItemObject[index + 1];
      System.arraycopy(userObjects, 0, newUserObjects, 0, userObjects.length);
      userObjects = newUserObjects;
    }
    userObjects[index] = userObject;
  }

}

public interface CTableItem {

  public static class TableItemObject {

    protected CTableItem tableItem;

    public CTableItem getTableItem() {
      return tableItem;
    }

    protected TableItemObject(CTableItem tableItem) {
      this.tableItem = tableItem;
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
      return tableItem.isChecked();
    }

    public boolean isGrayed() {
      return tableItem.isGrayed();
    }

  }

  public static class Instanciator {
    private Instanciator() {}

    public static CTableItem createInstance(TableItem tableItem, int style) {
      return new CTableItemImplementation(tableItem, style);
    }

  }

  public TableItemObject getTableItemObject(int index);

  public void setChecked(boolean isChecked);

  public boolean isChecked();

  public void setGrayed(boolean isGrayed);

  public boolean isGrayed();

  public void insertColumn(int index);

  public void removeColumn(int index);

}