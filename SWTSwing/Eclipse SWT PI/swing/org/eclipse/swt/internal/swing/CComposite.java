/*
 * @(#)Composite.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.PaintEvent;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

class CCompositeImplementation extends JPanel implements CComposite {

  protected Composite handle;
  protected JPanel contentPane;
  protected JScrollPane scrollPane;

  public CCompositeImplementation(Composite composite, int style) {
    super(new BorderLayout(0, 0));
    this.handle = composite;
    init(style);
  }

  protected Graphics graphics;

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    } else {
      setBorder(null);
    }
    JPanel panel = new JPanel(null) {
      public Graphics getGraphics() {
        if(graphics != null) {
          java.awt.Point point = new java.awt.Point(0, 0);
          point = SwingUtilities.convertPoint(getParent (), point, this);
          Graphics g = graphics.create();
          g.translate(point.x, point.y);
          g.setClip(new Rectangle(getSize()));
          return g;
        }
        return super.getGraphics();
      }

      protected void paintComponent (Graphics g) {
        graphics = g;
        super.paintComponent(g);
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        graphics = null;
      }
    };
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new UnmanagedScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.setFocusable (true);
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
    Utils.installFocusListener(contentPane, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return contentPane;
  }

  public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
  }

}

/**
 * The composite equivalent on the Swing side.
 * @version 1.0 2005.08.31
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CComposite extends CScrollable {

  public static class Instanciator {
    private Instanciator() {}

    public static CComposite createInstance(Composite composite, int style) {
      return new CCompositeImplementation(composite, style);
    }

  }

}
