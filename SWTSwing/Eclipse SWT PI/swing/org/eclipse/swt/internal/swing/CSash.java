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
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.swing.CShell.PaintHandler;
import org.eclipse.swt.widgets.Sash;

class CSashImplementation extends JPanel implements CSash {

  protected Sash handle;

  protected BasicSplitPaneDivider divider;

  protected Point originPoint;
  protected Point currentPoint;

  public CSashImplementation(Sash sash, int style) {
    super(new BorderLayout(0, 0));
    this.handle = sash;
    JSplitPane splitPane = new JSplitPane((style & SWT.HORIZONTAL) != 0? JSplitPane.VERTICAL_SPLIT: JSplitPane.HORIZONTAL_SPLIT);
    divider = ((BasicSplitPaneUI)splitPane.getUI()).getDivider();
//    divider.setBackground(java.awt.Color.blue);
    if((handle.getStyle() & SWT.SMOOTH) == 0) {
      divider.addMouseListener(new MouseAdapter() {
        protected PaintHandler paintHandler;
        public void mousePressed(MouseEvent e) {
          if((e.getButton() & MouseEvent.BUTTON1) == 0) {
            return;
          }
          paintHandler = new PaintHandler() {
            public void paintComponent(Graphics2D g) {};
            public void paint(Graphics2D g) {
              Rectangle bounds = divider.getBounds();
              if((handle.getStyle() & SWT.VERTICAL) != 0) {
                bounds.x += currentPoint.x - originPoint.x;
              } else {
                bounds.y += currentPoint.y - originPoint.y;
              }
              bounds = SwingUtilities.convertRectangle(divider, bounds, SwingUtilities.getWindowAncestor(CSashImplementation.this));
              g.setXORMode(SwingUtilities.getWindowAncestor(CSashImplementation.this).getBackground());
              g.setColor(Color.BLACK);
              g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            };
          };
          ((CShell)SwingUtilities.getWindowAncestor(CSashImplementation.this)).addPaintHandler(paintHandler);
          originPoint = e.getPoint();
          currentPoint = originPoint;
          SwingUtilities.getWindowAncestor(CSashImplementation.this).repaint();
        }
        public void mouseReleased(MouseEvent e) {
          if((e.getButton() & MouseEvent.BUTTON1) == 0) {
            return;
          }
          originPoint = null;
          currentPoint = null;
          SwingUtilities.getWindowAncestor(CSashImplementation.this).repaint();
          ((CShell)SwingUtilities.getWindowAncestor(CSashImplementation.this)).removePaintHandler(paintHandler);
        }
      });
      divider.addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
          if(originPoint == null) {
            return;
          }
          currentPoint = e.getPoint();
          SwingUtilities.getWindowAncestor(CSashImplementation.this).repaint();
        }
      });
    }
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

  public Integer getDragMove() {
    if(originPoint == null) {
      return null;
    }
    return (handle.getStyle() & SWT.HORIZONTAL) != 0? new Integer(currentPoint.y - originPoint.y): new Integer(currentPoint.x - originPoint.x);
  }

}

public interface CSash extends CComponent {

  public static class Instanciator {
    private Instanciator() {}

    public static CSash createInstance(Sash sash, int style) {
      return new CSashImplementation(sash, style);
    }

  }

  public Integer getDragMove();

}
