/*
 * @(#)ScrollPane.java
 * 
 * Swing port: Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com/swtswing
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 * A simple scroll pane that has a viewport, but does not make links between the
 * actions of the scroll bars and the content (this has to be done externally)
 * @version 1.1 2003.06.03
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class ScrollPane extends JPanel {

  private JScrollBar horizontalScrollBar;
  private JScrollBar verticalScrollBar;

  public static final int H_SCROLL = 1;
  public static final int V_SCROLL = 2;

  private Container view = new JPanel(new BorderLayout());

  Rectangle computeViewContentBounds() {
    Component[] components = view.getComponents();
    if(components.length == 0) {
      return view.getBounds();
    }
    Rectangle bounds = components[0].getBounds();
    int x1 = bounds.x;
    int y1 = bounds.y;
    int x2 = bounds.x + bounds.width;
    int y2 = bounds.y + bounds.height;
    for(int i=1; i<components.length; i++) {
      bounds = components[i].getBounds();
      x1 = Math.min(x1, bounds.x);
      y1 = Math.min(y1, bounds.y);
      x2 = Math.max(x2, bounds.x + bounds.width);
      y2 = Math.max(y2, bounds.y + bounds.height);
    }
    return new Rectangle(x1, y1, x2 - x1, y2 - y1);
  }

  public ScrollPane(int direction) {
    // Set the layout
    GridBagLayout gridbag = new GridBagLayout();
    setLayout(gridbag);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1.1;
    c.weighty = 1.1;
    // Set the view
    add(view, c);
    c.weightx = 0.0;
    c.weighty = 0.0;
    // Set the scroll bars
    if((direction & H_SCROLL) != 0) {
      verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
      verticalScrollBar.addComponentListener(new java.awt.event.ComponentAdapter() {
        public void componentResized(java.awt.event.ComponentEvent e) {
          Rectangle cBounds = computeViewContentBounds();
          Rectangle vBounds = view.getBounds();
          int extent = (verticalScrollBar.getMaximum() - verticalScrollBar.getMinimum()) * vBounds.height / cBounds.height;
          int value = verticalScrollBar.getValue();
          if(value > verticalScrollBar.getMaximum() - extent) {
            verticalScrollBar.setValue(verticalScrollBar.getMinimum());
            value = verticalScrollBar.getMaximum() - extent;
            verticalScrollBar.setVisibleAmount(extent);
            verticalScrollBar.setValue(verticalScrollBar.getMaximum() - extent);
          }
        }
      });
      c.gridx = 1;
      c.gridy = 0;
      add(verticalScrollBar, c);
    }
    if((direction & V_SCROLL) != 0) {
      horizontalScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
      horizontalScrollBar.addComponentListener(new java.awt.event.ComponentAdapter() {
        public void componentResized(java.awt.event.ComponentEvent e) {
          Rectangle cBounds = computeViewContentBounds();
          Rectangle vBounds = view.getBounds();
          int extent = (horizontalScrollBar.getMaximum() - horizontalScrollBar.getMinimum()) * vBounds.width / cBounds.width;
          int value = horizontalScrollBar.getValue();
          if(value > horizontalScrollBar.getMaximum() - extent) {
            horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
            value = horizontalScrollBar.getMaximum() - extent;
            horizontalScrollBar.setVisibleAmount(extent);
            horizontalScrollBar.setValue(horizontalScrollBar.getMaximum() - extent);
          }
        }
      });
      c.gridx = 0;
      c.gridy = 1;
      add(horizontalScrollBar, c);
    }
  }

  public JScrollBar getHorizontalScrollBar() {
    return horizontalScrollBar;
  }

  public JScrollBar getVerticalScrollBar() {
    return verticalScrollBar;
  }

  public Container getViewport() {
    return view; 
  }

}