/*
 * @(#)CLabel.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;

class CSeparator extends JPanel implements CLabel {

  protected Label handle;

  protected JSeparator separator;

  public CSeparator(Label label, int style) {
    this.handle = label;
    GridBagLayout gridBag = new GridBagLayout();
    setLayout(gridBag);
    GridBagConstraints c = new GridBagConstraints();
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.fill = (style & SWT.HORIZONTAL) != 0? GridBagConstraints.HORIZONTAL: GridBagConstraints.VERTICAL;
    separator = new JSeparator((style & SWT.HORIZONTAL) != 0? JSeparator.HORIZONTAL: JSeparator.VERTICAL);
    gridBag.setConstraints(separator, c);
    add(separator);
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    }
    separator.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseReleased(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseClicked(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseEntered(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseExited(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    separator.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseMoved(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    separator.addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        handle.processEvent(e);
      }
    });
    separator.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        handle.processEvent(e);
      }
      public void keyReleased(KeyEvent e) {
        handle.processEvent(e);
      }
    });
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return separator;
  }

  public String getText() {
    return null;
  }

  public void setText(String text) {
  }

  public void setAlignment(int alignment) {
  }

  public void setIcon(Icon icon) {
  }

}

class CLabelImplementation extends JLabel implements CLabel {

  protected Label handle;

  public CLabelImplementation(Label label, int style) {
    this.handle = label;
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    }
    addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseReleased(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseClicked(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseEntered(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseExited(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseMoved(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        handle.processEvent(e);
      }
    });
    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        handle.processEvent(e);
      }
      public void keyReleased(KeyEvent e) {
        handle.processEvent(e);
      }
    });
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }

}

/**
 * The button equivalent on the Swing side.
 * @version 1.0 2005.08.20
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CLabel extends CComponent {

  public static class Instanciator {
    private Instanciator() {}

    public static CLabel createInstance(Label label, int style) {
      if((style & SWT.SEPARATOR) != 0) {
        return new CSeparator(label, style);
      }
      return new CLabelImplementation(label, style);
    }

  }

  public String getText();

  public void setText(String text);

  public void setAlignment(int alignment);

  public void setIcon(Icon icon);

}
