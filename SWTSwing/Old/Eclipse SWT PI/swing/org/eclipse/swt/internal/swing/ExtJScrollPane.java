/*
 * @(#)ExtJScrollPane.java
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
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * A scroll pane that computes sizes correctly.
 * @version 1.0 2004.01.20
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class ExtJScrollPane extends JScrollPane {

  /** The view. */
  protected Component view;

  /**
   * Construct an ExtJScrollPane.
   * @param view The component viewed in this scroll pane.
   */
  public ExtJScrollPane(Component view) {
    super(view);
    this.view = view;
  }

  /**
   * Get the preferred size.
   * @return The preferred size.
   */
  public Dimension getPreferredSize() {
    Insets i = getInsets();
    int width = i.left + i.right;
    int height = i.top + i.bottom;
    JViewport header = getColumnHeader();
    if(header != null && header.isVisible()) {
      height += header.getPreferredSize().height;
    }
    Dimension dimension = getViewport().getView().getPreferredSize();
    width += dimension.width;
    height += dimension.height;
    
//    Dimension dim = super.getPreferredSize();
//    int width = (int)dim.getWidth();
//    int height = (int)dim.getHeight();
//    dim = getViewport().getPreferredSize();
//    width -= (int)dim.getWidth();
//    height -= (int)dim.getHeight();
//    dim = view.getPreferredSize();
//    width += (int)dim.getWidth();
//    height -= (int)dim.getHeight();
    return new Dimension(width, height);
  }

}
