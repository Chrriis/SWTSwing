/*
 * @(#)CCoolBar.java
 * 
 * Swing port: Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com/swtswing
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
public class CCoolBar extends JPanel {

  public CCoolBar() {
    super();
//    setBackground(java.awt.Color.red);
//    setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
    setLayout(new CCoolBarLayout());
  }

  public void setSize(int width, int height) {
    super.setSize(width, getPreferredSize().height);
  }

  public Component add(Component comp) {
    if(!(comp instanceof CCoolBarItem)) {
      CCoolBarItem item = new CCoolBarItem();
      item.add(comp);
      comp = item;
    }
    return super.add(comp);
  }

  public Component add(Component comp, int index) {
    if(!(comp instanceof CCoolBarItem)) {
      CCoolBarItem item = new CCoolBarItem();
      item.add(comp);
      comp = item;
    }
    return super.add(comp, index);
  }

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
      ((CCoolBarItem)getComponent(i)).setLocked(isLocked);
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
      ((CCoolBarItem)getComponent(i)).setExtractable(isExtractable);
    }
  }

  /**
   * Get the list of indice of wrapped items. The first one is never returned.
   * @return An array containing the indice of wrapped items.
   */
  public int[] getWrapIndice() {
    ArrayList indiceList = new ArrayList();
    for(int i=1; i<getComponentCount(); i++) {
      if(((CCoolBarItem)getComponent(i)).isWrapped()) {
        indiceList.add(new Integer(i));
      }
    }
    int[] indice = new int[indiceList.size()];
    for(int i=0; i<indiceList.size(); i++) {
      indice[i] = ((Integer)indiceList.get(i)).intValue();
    }
    return indice;
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
  public CCoolBarItem getItem(int index) {
    return (CCoolBarItem)getComponent(index);
  }
  
}
