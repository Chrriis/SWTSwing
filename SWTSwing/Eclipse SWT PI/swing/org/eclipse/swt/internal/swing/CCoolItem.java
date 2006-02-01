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

import java.awt.BorderLayout;

import org.eclipse.swt.widgets.CoolItem;

class CCoolItemImplementation extends JCoolBarItem implements CCoolItem {

  protected CoolItem handle;

  public CCoolItemImplementation(CoolItem coolItem, int style) {
    setLayout(new BorderLayout(0, 0));
    handle = coolItem;
    init(style);
  }

  protected void init(int style) {
//    getToolBar().setLayout(null);
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

}
