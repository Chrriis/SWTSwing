/*
 * @(#)LayeredPane.java
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
import java.awt.Rectangle;

import javax.swing.JLayeredPane;

/**
 * Implementation of a layered container.
 * @version 1.0 2004.01.19
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class LayeredContainer extends JLayeredPane {

  /** The component that is manipulated. */
  protected Component component;

  public Dimension getPreferredSize() {
    Insets i = getInsets();
    Dimension d = component.getPreferredSize();
    return new Dimension(d.width + i.left + i.right, d.height + i.top + i.bottom);
  }

//  public Rectangle getBounds() {
//    System.err.println("--- " + component.getBounds() + ", " + super.getBounds());
//    return component.getBounds();
//  }

  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    Insets i = getInsets();
    component.setSize(width - i.left - i.right, height - i.top - i.bottom);
  }

  /**
   * Construct a LayeredContainer.
   * @param component The component to handle.
   */
  public LayeredContainer(final Component component) {
    this.component = component;
    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent e) {
        Rectangle b = getBounds();
        Insets i = getInsets();
        component.setSize(b.width - i.right - i.left, b.height - i.top - i.bottom);
      }
    });
    add(component, new Integer(-1));
  }

  /**
   * Get the main component.
   * @return The component.
   */
  public Component getComponent() {
    return component;
  }

}

