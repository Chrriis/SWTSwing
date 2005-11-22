/*
 * @(#)CComboButton.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CComboButton extends JPanel {

  JButton pushButton = new JButton() {
    protected void paintBorder(java.awt.Graphics g) {
      java.awt.Insets i = getBorder().getBorderInsets(this);
      int width = this.getWidth();
      int height = this.getHeight();
      if(width - i.left - i.right < i.right) {
        super.paintBorder(g);
      } else {
        java.awt.Shape clip = g.getClip();
        g.clipRect(0, 0, width - i.right, height);
        super.paintBorder(g);
        g.setClip(clip);
        g.clipRect(width - i.right, 0, width, height);
        g.translate(i.right, 0);
        super.paintBorder(g);
      }
    }
  };
  JButton dropButton = new CArrowButton(CArrowButton.SOUTH);
  public JButton getPushButton() {
    return pushButton;
  }
  public JButton getDropButton() {
    return dropButton;
  }
  public void setIcon(Icon icon) {
    pushButton.setIcon(icon);
  }
  public void setDisabledIcon(Icon icon) {
    pushButton.setDisabledIcon(icon);
  }
  public void setSelectedIcon(Icon icon) {
    pushButton.setSelectedIcon(icon);
  }
  public void setRolloverIcon(Icon icon) {
    pushButton.setRolloverIcon(icon);
  }
  public void setPressedIcon(Icon icon) {
    pushButton.setPressedIcon(icon);
  }
  public String getToolTipText() {
    return pushButton.getToolTipText();
  }
  public void setToolTipText(String text) {
    pushButton.setToolTipText(text);
    dropButton.setToolTipText(text);
  }
  public CComboButton(boolean isFlat) {
    setLayout(new java.awt.BorderLayout(0, 0) {
      public java.awt.Dimension minimumLayoutSize(Container target) {
        return preferredLayoutSize(target);
      }
      public java.awt.Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target);
      }
    });
    if(isFlat) {
      pushButton.setBorderPainted(false);
      dropButton.setBorderPainted(false);
      java.awt.event.MouseAdapter flatListener = new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
          pushButton.setBorderPainted(pushButton.isEnabled());
          dropButton.setBorderPainted(pushButton.isEnabled());
        }
        public void mouseExited(java.awt.event.MouseEvent e) {
          pushButton.setBorderPainted(pushButton.isSelected());
          dropButton.setBorderPainted(pushButton.isSelected());
        }
      };
      pushButton.addMouseListener(flatListener);
      dropButton.addMouseListener(flatListener);
    }
    dropButton.setPreferredSize(new java.awt.Dimension(16, 20));
    pushButton.setMargin(new Insets(0, 0, 0, 0));
    dropButton.setMargin(new Insets(0, 0, 0, 0));
    add(pushButton, java.awt.BorderLayout.CENTER);
    add(dropButton, java.awt.BorderLayout.EAST);
  }

}
