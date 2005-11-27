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

import java.awt.Container;

import javax.swing.JScrollBar;

import org.eclipse.swt.widgets.CoolBar;

class CCoolBarImplementation extends JCoolBar implements CCoolBar {

  protected CoolBar handle;

  public CCoolBarImplementation(CoolBar coolBar, int style) {
    handle = coolBar;
    init(style);
  }

  protected void init(int style) {
  }

  public Container getClientArea() {
    return this;
  }

  public JScrollBar getVerticalScrollBar() {
    return null;
  }

  public JScrollBar getHorizontalScrollBar() {
    return null;
  }

}

public interface CCoolBar extends CComposite {

  public static class Instanciator {
    private Instanciator() {}

    public static CCoolBar createInstance(CoolBar coolBar, int style) {
      return new CCoolBarImplementation(coolBar, style);
    }

  }

  public int getItemCount();

  public boolean isLocked();

  public void setLocked(boolean isLocked);

  public void setWrappedIndices(int[] indices);

}
