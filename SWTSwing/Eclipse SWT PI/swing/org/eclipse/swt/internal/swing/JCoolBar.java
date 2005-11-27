/*
 * @(#)JCoolBar.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * Implementation of a cool bar.
 * @version 1.1 2003.08.21
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class JCoolBar extends JPanel {

  public JCoolBar() {
    super(new JCoolBarLayout());
//    setBackground(java.awt.Color.red);
//    setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
  }

  public void setSize(int width, int height) {
    super.setSize(width, getPreferredSize().height);
  }

  protected void addImpl(Component comp, Object constraints, int index) {
    if(!(comp instanceof JCoolBarItem)) {
      throw new IllegalArgumentException("Can only add JCoolBarItems to the cool bar!");
    }
    super.addImpl(comp, constraints, index);
  }

//  public Component add(Component comp) {
//    if(!(comp instanceof JCoolBarItem)) {
//      JCoolBarItem item = new JCoolBarItem();
//      item.add(comp);
//      comp = item;
//    }
//    return super.add(comp);
//  }
//
//  public Component add(Component comp, int index) {
//    if(!(comp instanceof JCoolBarItem)) {
//      JCoolBarItem item = new JCoolBarItem();
//      item.add(comp);
//      comp = item;
//    }
//    return super.add(comp, index);
//  }

  /** Indicate if the cool bar items are locked. */
  private boolean isLocked = false;

  /**
   * Indicate if the cool bar items are locked.
   * @return True if the cool bar items are locked.
   */
  public boolean isLocked() {
    return isLocked;
  }
  
  /**
   * Set whether the cool bar items are locked.
   * @param isLocked True if the cool bar items are to be locked.
   */
  public void setLocked(boolean isLocked) {
    this.isLocked = isLocked;
    for(int i=0; i<getComponentCount(); i++) {
      ((JCoolBarItem)getComponent(i)).setLocked(isLocked);
    }
  }

  /** Indicate if the cool bar items are extractable. */
  private boolean isExtractable = false;

  /**
   * Indicate if the cool bar items are extractable.
   * @return True if the cool bar items are extractable.
   */
  public boolean isExtractable() {
    return isExtractable;
  }
  
  /**
   * Set whether the cool bar items are extractable.
   * @param isExtractable True if the cool bar items are to be extractable.
   */
  public void setExtractable(boolean isExtractable) {
    this.isExtractable = isExtractable;
    for(int i=0; i<getComponentCount(); i++) {
      ((JCoolBarItem)getComponent(i)).setExtractable(isExtractable);
    }
  }

  /**
   * Get the list of indice of wrapped items. The first one is never returned.
   * @return An array containing the indice of wrapped items.
   */
  public int[] getWrapIndices() {
    ArrayList indiceList = new ArrayList();
    for(int i=1; i<getComponentCount(); i++) {
      if(((JCoolBarItem)getComponent(i)).isWrapped()) {
        indiceList.add(new Integer(i));
      }
    }
    int[] indices = new int[indiceList.size()];
    for(int i=0; i<indiceList.size(); i++) {
      indices[i] = ((Integer)indiceList.get(i)).intValue();
    }
    return indices;
  }

  public void setWrappedIndices(int[] indices) {
    int cursor = 0;
    Component[] components = getComponents();
    for(int i=1; i<components.length; i++) {
      while(indices.length > cursor && indices[cursor] < i) {
        cursor++;
      }
      if(indices.length > cursor && indices[cursor] == i) {
        ((JCoolBarItem)components[i]).setWrapped(true);
      } else {
        ((JCoolBarItem)components[i]).setWrapped(false);
      }
    }
  }

  /**
   * Get the number of rows.
   * @return The number of rows.
   */
  public int getRowCount() {
    int rowCount = getItemCount()>0? 1: 0;
    for(int i=0; i<getItemCount(); i++) {
      if(getItem(i).isWrapped()) {
        rowCount++;
      }
    }
    return rowCount;
  }

  /**
   * Get the number of items.
   * @return The number of items.
   */
  public int getItemCount() {
    return getComponentCount();
  }
  
  /**
   * Get the item at the specified index.
   * @param index The index of the item.
   * @return The item at the index.
   */
  public JCoolBarItem getItem(int index) {
    return (JCoolBarItem)getComponent(index);
  }
  
}
