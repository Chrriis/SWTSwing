/*
 * @(#)Utils.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.eclipse.swt.widgets.Control;

/**
 * General util methods.
 * @version 1.0 2005.03.18
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class Utils {

  protected Utils() {}

  protected static Canvas panel = new Canvas();

  public static Component getDefaultComponent() {
    return panel;
  }

  static void installMouseListener(Component component, final Control control) {
    component.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseReleased(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseClicked(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseEntered(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseExited(MouseEvent e) {
        control.processEvent(e);
      }
    });
    component.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseMoved(MouseEvent e) {
        control.processEvent(e);
      }
    });
    component.addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installKeyListener(Component component, final Control control) {
    component.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        control.processEvent(e);
      }
      public void keyReleased(KeyEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installFocusListener(Component component, final Control control) {
    component.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        control.processEvent(e);
      }
      public void focusLost(FocusEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installComponentListener(Component component, final Control control) {
    component.addComponentListener(new ComponentListener() {
      public void componentHidden(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentResized(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        control.processEvent(e);
      }
    });
  }

}
