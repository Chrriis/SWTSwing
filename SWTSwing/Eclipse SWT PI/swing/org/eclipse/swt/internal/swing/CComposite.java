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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.PaintEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

class CCompositeImplementation extends JPanel implements CComposite {

  protected Composite handle;
  protected JPanel contentPane;
  protected JScrollPane scrollPane;

  public Container getSwingComponent() {
    return contentPane;
  }

  public CCompositeImplementation(Composite composite, int style) {
    super(new BorderLayout(0, 0));
    this.handle = composite;
    init(style);
  }

  public void requestFocus() {
    contentPane.requestFocus();
  }

  public void setCursor(Cursor cursor) {
    contentPane.setCursor(cursor);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    } else {
      setBorder(null);
    }
    JPanel panel = new JPanel(null) {
      protected Graphics graphics;
      protected Shape clip;
      public Graphics getGraphics() {
        Graphics g;
        if(graphics != null) {
          g = graphics.create();
        } else {
          g = super.getGraphics();
        }
        if(g != null) {
          g.setClip(clip);
        }
        return g;
      }
      protected void paintComponent (Graphics g) {
        graphics = g;
        clip = graphics.getClip();
        super.paintComponent(g);
        if(backgroundImageIcon != null) {
          Dimension size = getSize();
          g.drawImage(backgroundImageIcon.getImage(), 0, 0, size.width, size.height, null);
        }
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        graphics = null;
      }
    };
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new UnmanagedScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      scrollPane.setBorder(null);
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

  public void setBackground(Color bg) {
    super.setBackground(bg);
    if(contentPane != null) {
      contentPane.setBackground(bg);
    }
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

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      contentPane.setOpaque(true);
      if(scrollPane != null) {
        scrollPane.setOpaque(true);
      }
      break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      contentPane.setOpaque(false);
      if(scrollPane != null) {
        scrollPane.setOpaque(false);
      }
      break;
    }
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
