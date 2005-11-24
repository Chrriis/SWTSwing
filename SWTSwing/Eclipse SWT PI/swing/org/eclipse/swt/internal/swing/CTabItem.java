/*
 * @(#)CTabItem.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.eclipse.swt.widgets.TabItem;

class CTabItemImplementation extends JPanel implements CTabItem {

  protected TabItem handle;

  public CTabItemImplementation(TabItem tabItem, int style) {
    super(new BorderLayout(0, 0));
    this.handle = tabItem;
    init(style);
  }

  protected void init(int style) {
  }

}

public interface CTabItem {

  public static class Instanciator {
    private Instanciator() {}

    public static CTabItem createInstance(TabItem tabItem, int style) {
      return new CTabItemImplementation(tabItem, style);
    }
  }

}
