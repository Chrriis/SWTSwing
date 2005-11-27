/*
 * @(#)JCoolBarLayout.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;


/**
 * Te layout used by the cool bar
 * @version 1.0 2003.08.21
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
class JCoolBarLayout implements LayoutManager {

  int hgap;
  int vgap;

  public JCoolBarLayout() {
    this.hgap = 0;
    this.vgap = 0;
  }

  public void addLayoutComponent(String name, Component comp) {}

  public void removeLayoutComponent(Component comp) {}

  public Dimension preferredLayoutSize(Container target) {
    synchronized (target.getTreeLock()) {
      Dimension dim = new Dimension(0, 0);
      int nmembers = target.getComponentCount();
      boolean firstVisibleComponent = true;
      int h = 0;
      int w = 0;
      boolean isWrapped = false;
      for (int i = 0; i < nmembers; i++) {
        JCoolBarItem m = (JCoolBarItem)target.getComponent(i);
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

  private void moveComponents(Container target, int x, int y, int width, int height, int rowStart, int rowEnd, boolean ltr) {
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

  public void layoutContainer(Container target) {
    synchronized (target.getTreeLock()) {
      Insets insets = target.getInsets();
      int maxwidth = target.getWidth() - (insets.left + insets.right + hgap * 2);
      int nmembers = target.getComponentCount();
      int x = 0, y = insets.top + vgap;
      int rowh = 0, start = 0;
      boolean ltr = target.getComponentOrientation().isLeftToRight();
      boolean isWrapped = false;
      int[] heights = new int[nmembers];
      int cursor = 0;
      for (int i = 0; i < nmembers; i++) {
        JCoolBarItem coolItem = (JCoolBarItem)target.getComponent(i);
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
        if(!isWrapped && m instanceof JCoolBarItem && i != 0) {
          JCoolBarItem coolItem = (JCoolBarItem)m;
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
            moveComponents(target, insets.left + hgap, y, maxwidth - x, rowh, start, i, ltr);
            x = d.width;
            y += vgap + rowh;
            rowh = d.height;
            start = i;
          }
        }
      }
      moveComponents( target, insets.left + hgap, y, maxwidth - x, rowh, start, nmembers, ltr);
    }
  }

}
