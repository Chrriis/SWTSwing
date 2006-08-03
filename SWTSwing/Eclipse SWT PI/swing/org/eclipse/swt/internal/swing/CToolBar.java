/*
 * @(#)CToolBar.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.Image;

import javax.swing.JScrollBar;
import javax.swing.JToolBar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;

class CToolBarImplementation extends JToolBar implements CToolBar {

  protected ToolBar handle;

  public CToolBarImplementation(ToolBar toolBar, int style) {
    super((style & SWT.VERTICAL) != 0? JToolBar.VERTICAL: JToolBar.HORIZONTAL);
    this.handle = toolBar;
    init(style);
  }

  protected void init(int style) {
    setFloatable(false);
    if((style & SWT.BORDER) != 0) {
      setBorder(javax.swing.BorderFactory.createEtchedBorder());
    }
    if((style & SWT.WRAP) != 0) {
      setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
//    } else {
//      setLayout(null);
    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return this;
  }

  public JScrollBar getHorizontalScrollBar() {
    return null;
  }

  public JScrollBar getVerticalScrollBar() {
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

public interface CToolBar extends CScrollable {

  public static class Instanciator {
    private Instanciator() {}

    public static CToolBar createInstance(ToolBar toolBar, int style) {
      return new CToolBarImplementation(toolBar, style);
    }

  }

}
