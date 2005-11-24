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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

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
    Utils.installMouseListener(separator, handle);
    Utils.installKeyListener(separator, handle);
    Utils.installFocusListener(separator, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return separator;
  }

  public String getLabelText() {
    return null;
  }

  public void setLabelText(String text) {
  }

  public void setAlignment(int alignment) {
  }

  public void setIcon(Icon icon) {
  }

}

class CLabelImplementation extends JLabel implements CLabel {

  protected Label handle;

  protected boolean isWrapping;

  public CLabelImplementation(Label label, int style) {
    this.handle = label;
    init(style);
  }

  public Dimension getPreferredSize() {
    if(isWrapping) {
      Dimension preferredSize = super.getPreferredSize();
      View view = ((View)getClientProperty(BasicHTML.propertyKey)).getView(0);
      Dimension size = super.getSize();
      view.setSize(size.width, 0);
      preferredSize.height = super.getPreferredSize().height;
      return preferredSize;
    }
    return super.getPreferredSize();
  }

  protected void init(int style) {
    isWrapping = (style & SWT.WRAP) != 0;
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }

  public void setLabelText(String text) {
    if(isWrapping) {
      super.setText("<html>" + Utils.escapeXML(text) + "</html>");
    } else {
      super.setText(text);
    }
  }

  public String getLabelText() {
    if(isWrapping) {
      String text = getText();
      text = text.substring(6, text.length() - 7);
      return Utils.unescapeXML(text);
    }
    return getText();
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

  public String getLabelText();

  public void setLabelText(String text);

  public void setAlignment(int alignment);

  public void setIcon(Icon icon);

}
