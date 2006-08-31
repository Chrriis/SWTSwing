/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.Image;

import javax.swing.JScrollBar;
import javax.swing.UIManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.CoolBar;

class CCoolBarImplementation extends JCoolBar implements CCoolBar {

  protected CoolBar handle;

  public Container getSwingComponent() {
    return this;
  }

  public CCoolBarImplementation(CoolBar coolBar, int style) {
    handle = coolBar;
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    }
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

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE: setOpaque(false); break;
    }
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
