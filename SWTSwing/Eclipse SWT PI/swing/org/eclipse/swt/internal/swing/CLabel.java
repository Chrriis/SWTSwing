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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    separator.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
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
    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
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
