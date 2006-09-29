/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Sash;

class CSashImplementation extends JPanel implements CSash {

  protected Sash handle;

  protected BasicSplitPaneDivider divider;

  public Container getSwingComponent() {
    return divider;
  }

  public Control getSWTHandle() {
    return handle;
  }

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
          dragLocation = (handle.getStyle() & SWT.VERTICAL) != 0? getLocation().x: getLocation().y;
          paintHandler = new PaintHandler() {
            public void paintComponent(Graphics2D g) {};
            public void paint(Graphics2D g) {
              Rectangle bounds = divider.getBounds();
              Window window = SwingUtilities.getWindowAncestor(CSashImplementation.this);
              bounds = SwingUtilities.convertRectangle(divider, bounds, window);
              Point p = SwingUtilities.convertPoint(CSashImplementation.this.getParent(), new Point(dragLocation, dragLocation), window);
              if((handle.getStyle() & SWT.VERTICAL) != 0) {
                bounds.x = p.x;
              } else {
                bounds.y = p.y;
              }
              g.setXORMode(window.getBackground());
              g.setColor(Color.BLACK);
              g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            };
          };
          ((CShell)SwingUtilities.getWindowAncestor(CSashImplementation.this)).addPaintHandler(paintHandler);
          SwingUtilities.getWindowAncestor(CSashImplementation.this).repaint();
        }
        public void mouseReleased(MouseEvent e) {
          if((e.getButton() & MouseEvent.BUTTON1) == 0) {
            return;
          }
          SwingUtilities.getWindowAncestor(CSashImplementation.this).repaint();
          ((CShell)SwingUtilities.getWindowAncestor(CSashImplementation.this)).removePaintHandler(paintHandler);
        }
      });
      divider.addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
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

  protected int dragLocation = Integer.MIN_VALUE;

  public void setDragLocation(int dragLocation) {
    this.dragLocation = dragLocation;
    repaint();
  }

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    // TODO: implement
  }

}

public interface CSash extends CControl {

  public static class Instanciator {
    private Instanciator() {}

    public static CSash createInstance(Sash sash, int style) {
      return new CSashImplementation(sash, style);
    }

  }

  public void setDragLocation(int dragLocation);

}
