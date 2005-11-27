/*
 * @(#)CCoolItem.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Dimension;

import org.eclipse.swt.widgets.CoolItem;

class CCoolItemImplementation extends JCoolBarItem implements CCoolItem {

  protected CoolItem handle;

  public CCoolItemImplementation(CoolItem coolItem, int style) {
    handle = coolItem;
    init(style);
  }

  protected void init(int style) {
//    getToolBar().setLayout(null);
  }

  public void setToolBarPreferredSize(Dimension preferredSize) {
    getToolBar().setPreferredSize(preferredSize);
  }

  public Dimension getToolBarPreferredSize() {
    return getToolBar().getPreferredSize();
  }

  public void addToolBarComponent(Component component) {
    getToolBar().add(component);
  }

  public void removeToolBarComponent(Component component) {
    getToolBar().remove(component);
  }

}

public interface CCoolItem {

  public static class Instanciator {
    private Instanciator() {}

    public static CCoolItem createInstance(CoolItem coolItem, int style) {
      return new CCoolItemImplementation(coolItem, style);
    }
  }

  public boolean isWrapped();

  public void setWrapped(boolean isWrapped);

  public void setToolBarPreferredSize(Dimension preferredSize);

  public Dimension getToolBarPreferredSize();

  public void addToolBarComponent(Component component);

  public void removeToolBarComponent(Component component);

}
