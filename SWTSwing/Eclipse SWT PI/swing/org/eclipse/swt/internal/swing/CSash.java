/*
 * @(#)CSash.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Sash;

class CSashImplementation extends JPanel implements CSash {

  protected Sash handle;

  protected BasicSplitPaneDivider divider;

  public CSashImplementation(Sash sash, int style) {
    super(new BorderLayout(0, 0));
    this.handle = sash;
    JSplitPane splitPane = new JSplitPane((style & SWT.HORIZONTAL) != 0? JSplitPane.VERTICAL_SPLIT: JSplitPane.HORIZONTAL_SPLIT);
    divider = ((BasicSplitPaneUI)splitPane.getUI()).getDivider();
    add(divider, BorderLayout.CENTER);
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      divider.setBorder(javax.swing.UIManager.getBorder("TextField.border"));
    }
    Utils.installMouseListener(divider, handle);
    Utils.installKeyListener(divider, handle);
    Utils.installFocusListener(divider, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return divider;
  }

}

public interface CSash extends CComponent {

  public static class Instanciator {
    private Instanciator() {}

    public static CSash createInstance(Sash sash, int style) {
      return new CSashImplementation(sash, style);
    }

  }

}
