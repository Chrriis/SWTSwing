/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
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

  public static class Factory {
    private Factory() {}

    public static CCoolItem newInstance(CoolItem coolItem, int style) {
      return new CCoolItemImplementation(coolItem, style);
    }
  }

  public boolean isWrapped();

  public void setWrapped(boolean isWrapped);

}
