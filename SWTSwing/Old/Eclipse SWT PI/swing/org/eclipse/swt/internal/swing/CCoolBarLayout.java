/*
 * @(#)CCoolBarLayout.java
 * 
 * Swing port: Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com/swtswing
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;


/**
 * Te layout used by the cool bar
 * @version 1.0 2003.08.21
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
class CCoolBarLayout implements LayoutManager, Serializable {

  /**
   * The flow layout manager allows a seperation of
   * components with gaps.  The horizontal gap will
   * specify the space between components.
   *
   * @serial
   * @see getHgap
   * @see setHgap
   */
  int hgap;

  /**
   * The flow layout manager allows a seperation of
   * components with gaps.  The vertical gap will
   * specify the space between rows.
   *
   * @serial
   * @see getVgap
   * @see setVgap
   */
  int vgap;

  /**
   * Constructs a new <code>FlowLayout</code> with a centered alignment and a
   * default 5-unit horizontal and vertical gap.
   */
  public CCoolBarLayout() {
    this.hgap = 0;
    this.vgap = 0;
  }

  /**
   * Adds the specified component to the layout. Not used by this class.
   * @param name the name of the component
   * @param comp the component to be added
   */
  public void addLayoutComponent(String name, Component comp) {}

  /**
   * Removes the specified component from the layout. Not used by
   * this class.
   * @param comp the component to remove
   * @see       java.awt.Container#removeAll
   */
  public void removeLayoutComponent(Component comp) {}

  /**
   * Returns the preferred dimensions for this layout given the 
   * <i>visible</i> components in the specified target container.
   * @param target the component which needs to be laid out
   * @return    the preferred dimensions to lay out the
   *            subcomponents of the specified container
   * @see Container
   * @see #minimumLayoutSize
   * @see       java.awt.Container#getPreferredSize
   */
  public Dimension preferredLayoutSize(Container target) {
    synchronized (target.getTreeLock()) {
      Dimension dim = new Dimension(0, 0);
      int nmembers = target.getComponentCount();
      boolean firstVisibleComponent = true;

      int h = 0;
      int w = 0;
      boolean isWrapped = false;

      for (int i = 0; i < nmembers; i++) {
        CCoolBarItem m = (CCoolBarItem)target.getComponent(i);
//        Component m = target.getComponent(i);
        if(m.isWrapped()) {
          isWrapped = true;
        }
        if (m.isVisible()) {
          if(isWrapped) {
            h += dim.height;
            dim.height = 0;
            w = Math.max(dim.width, w);
            dim.width = 0;
            isWrapped = false;
          }
          Dimension d = m.getPreferredSize();
          dim.height = Math.max(dim.height, d.height);
          if (firstVisibleComponent) {
            firstVisibleComponent = false;
          } else {
            dim.width += hgap;
          }
          dim.width += d.width;
        }
      }
      dim.height += h;
      dim.width = Math.max(dim.width, w);
      Insets insets = target.getInsets();
      dim.width += insets.left + insets.right + hgap * 2;
      dim.height += insets.top + insets.bottom + vgap * 2;
      return dim;
    }
  }

  /**
   * Returns the minimum dimensions needed to layout the <i>visible</i>
   * components contained in the specified target container.
   * @param target the component which needs to be laid out
   * @return    the minimum dimensions to lay out the
   *            subcomponents of the specified container
   * @see #preferredLayoutSize
   * @see       java.awt.Container
   * @see       java.awt.Container#doLayout
   */
  public Dimension minimumLayoutSize(Container target) {
    synchronized (target.getTreeLock()) {
      Dimension dim = new Dimension(0, 0);
      int nmembers = target.getComponentCount();

      int h = 0;
      int w = 0;
      boolean isWrapped = false;

      for (int i = 0; i < nmembers; i++) {
        Component m = target.getComponent(i);
//        CCoolBarItem m = (CCoolBarItem)target.getComponent(i);
        if (m.isVisible()) {
          if(isWrapped) {
            h += dim.height;
            dim.height = 0;
            w = Math.max(dim.width, w);
            dim.width = 0;
            isWrapped = false;
          }
          Dimension d = m.getMinimumSize();
          dim.height = Math.max(dim.height, d.height);
          if (i > 0) {
            dim.width += hgap;
          }
          dim.width += d.width;
        }
      }
      dim.height += h;
      dim.width = Math.max(dim.width, w);
      Insets insets = target.getInsets();
      dim.width += insets.left + insets.right + hgap * 2;
      dim.height += insets.top + insets.bottom + vgap * 2;
      return dim;
    }
  }

  /**
   * Centers the elements in the specified row, if there is any slack.
   * @param target the component which needs to be moved
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the width dimensions
   * @param height the height dimensions
   * @param rowStart the beginning of the row
   * @param rowEnd the the ending of the row
   */
  private void moveComponents(
    Container target,
    int x,
    int y,
    int width,
    int height,
    int rowStart,
    int rowEnd,
    boolean ltr) {
    synchronized (target.getTreeLock()) {
      for (int i = rowStart; i < rowEnd; i++) {
        Component m = target.getComponent(i);
        if (m.isVisible()) {
          if (ltr) {
            m.setLocation(x, y + (height - m.getHeight()) / 2);
          } else {
            m.setLocation(
              target.getWidth() - x - m.getWidth(),
              y + (height - m.getHeight()) / 2);
          }
          x += m.getWidth() + hgap;
        }
      }
    }
  }

  /**
   * Lays out the container. This method lets each component take
   * its preferred size by reshaping the components in the
   * target container in order to satisfy the alignment of
   * this <code>FlowLayout</code> object.
   * @param target the specified component being laid out
   * @see Container
   * @see       java.awt.Container#doLayout
   */
  public void layoutContainer(Container target) {
    synchronized (target.getTreeLock()) {
      Insets insets = target.getInsets();
      int maxwidth =
        target.getWidth() - (insets.left + insets.right + hgap * 2);
      int nmembers = target.getComponentCount();
      int x = 0, y = insets.top + vgap;
      int rowh = 0, start = 0;

      boolean ltr = target.getComponentOrientation().isLeftToRight();

      boolean isWrapped = false;

      int[] heights = new int[nmembers];
      int cursor = 0;
      for (int i = 0; i < nmembers; i++) {
        CCoolBarItem coolItem = (CCoolBarItem)target.getComponent(i);
        if(!isWrapped && i != 0) {
          isWrapped = coolItem.isWrapped();
        }
        if (coolItem.isVisible()) {
          if(isWrapped) {
            cursor++;
          }
          heights[cursor] = Math.max(heights[cursor], coolItem.getPreferredRowHeight());
          isWrapped = false;
        }
      }
      cursor = 0;

      isWrapped = false;
      for (int i = 0; i < nmembers; i++) {
        Component m = target.getComponent(i);
        if(!isWrapped && m instanceof CCoolBarItem && i != 0) {
          CCoolBarItem coolItem = (CCoolBarItem)m;
          isWrapped = coolItem.isWrapped();
        }
        if (m.isVisible()) {
          Dimension d = m.getPreferredSize();
          if(isWrapped) {
            cursor++;
          }
          m.setSize(d.width, heights[cursor]);

          if (!isWrapped && ((x == 0) || ((x + d.width) <= maxwidth))) {
            if (x > 0) {
              x += hgap;
            }
            x += d.width;
            rowh = Math.max(rowh, d.height);
          } else {
            isWrapped = false;
            moveComponents(
              target,
              insets.left + hgap,
              y,
              maxwidth - x,
              rowh,
              start,
              i,
              ltr);
            x = d.width;
            y += vgap + rowh;
            rowh = d.height;
            start = i;
          }
        }
      }
      moveComponents(
        target,
        insets.left + hgap,
        y,
        maxwidth - x,
        rowh,
        start,
        nmembers,
        ltr);
    }
  }

  /**
   * Reads this object out of a serialization stream, handling
   * objects written by older versions of the class that didn't contain all
   * of the fields we use now..
   */
  private void readObject(ObjectInputStream stream)
    throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
  }

}
