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

import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

class CCompositeImplementation extends JScrollPane implements CComposite {

  protected Composite handle;
  protected JPanel contentPane;

  public CCompositeImplementation(Composite composite, int style) {
    this.handle = composite;
    contentPane = new JPanel();
    getViewport().setView(contentPane);
    init(style);
  }
  
  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    } else {
      setBorder(null);
    }
    if((style & SWT.H_SCROLL) == 0) {
      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }
    if((style & SWT.V_SCROLL) == 0) {
      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
    contentPane.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
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
    return contentPane;
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
