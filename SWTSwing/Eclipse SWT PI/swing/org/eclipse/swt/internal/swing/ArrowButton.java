/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.UIManager;

/**
 * An arrow button.
 * @version 1.0 2003.08.03
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class ArrowButton extends JButton {

  private int direction;
  private Color shadow = UIManager.getColor("ComboBox.buttonShadow");
  private Color darkShadow = UIManager.getColor("ComboBox.buttonDarkShadow");
  private Color highlight = UIManager.getColor("ComboBox.buttonHighlight");

  /**
   * Construct an arrow button using the current look and feel. 
   * @param direction the direction of the arrow.
   */
  public ArrowButton(int direction) {
    this.direction = direction;
  }
  
  /**
   * Get the direction the arrow is pointing at.
   * @return The direction of the arrow.
   */
  public int getDirection() {
    return direction;
  }

  /**
   * Set the direction the arrow is pointing to.
   * @param direction The direction of the arrow.
   */
  public void setDirection(int direction) {
    this.direction = direction;
  }

  /**
   * Paint the button with an arrow on it. Using the installed colors for the current Look and Feel.
   */
  public void paint(java.awt.Graphics g) {
    super.paint(g);
    Color origColor;
    boolean isPressed, isEnabled;
    int w, h, size;
    w = getSize().width;
    h = getSize().height;
    origColor = g.getColor();
    isPressed = getModel().isPressed();
    isEnabled = this.isEnabled();
    size = Math.min((h - 4) / 3, (w - 4) / 3);
    size = Math.max(size, 2);
    paintTriangle(g, (w - size) / 2, (h - size) / 2, size, direction, isEnabled);
    if (isPressed) {
      g.translate(-1, -1);
    }
    g.setColor(origColor);
  }
  /**
   * Paint the triangle.
   * @param g The graphic.
   * @param x The starting point x.
   * @param y The starting point y.
   * @param size the size.
   * @param direction The direction the arrow is pointing.
   * @param isEnabled True if the arrow is painted enabled.
   */
  public void paintTriangle(java.awt.Graphics g, int x, int y, int size, int direction, boolean isEnabled) {
    java.awt.Color oldColor = g.getColor();
    size = Math.max(size, 2);
    int mid = (size / 2) - 1;
    g.translate(x, y);
    if (isEnabled) {
      g.setColor(darkShadow);
    } else {
      g.setColor(shadow);
    }
    switch (direction) {
      case NORTH :
        int i = 0;
        for (i = 0; i < size; i++) {
          g.drawLine(mid - i, i, mid + i, i);
        }
        if (!isEnabled) {
          g.setColor(highlight);
          g.drawLine(mid - i + 2, i, mid + i, i);
        }
        break;
      case SOUTH : {
        if (!isEnabled) {
          g.translate(1, 1);
          g.setColor(highlight);
          int j = 0;
          for (i = size - 1; i >= 0; i--) {
            g.drawLine(mid - i, j, mid + i, j);
            j++;
          }
          g.translate(-1, -1);
          g.setColor(shadow);
        }
        int j = 0;
        for (i = size - 1; i >= 0; i--) {
          g.drawLine(mid - i, j, mid + i, j);
          j++;
        }
        break;
      }
      case WEST :
        for (i = 0; i < size; i++) {
          g.drawLine(i, mid - i, i, mid + i);
        }
        if (!isEnabled) {
          g.setColor(highlight);
          g.drawLine(i, mid - i + 2, i, mid + i);
        }
        break;
      case EAST :
        if (!isEnabled) {
          g.translate(1, 1);
          g.setColor(highlight);
          int j = 0;
          for (i = size - 1; i >= 0; i--) {
            g.drawLine(j, mid - i, j, mid + i);
            j++;
          }
          g.translate(-1, -1);
          g.setColor(shadow);
        }
        int j = 0;
        for (i = size - 1; i >= 0; i--) {
          g.drawLine(j, mid - i, j, mid + i);
          j++;
        }
        break;
    }
    g.translate(-x, -y);
    g.setColor(oldColor);
  }

}
