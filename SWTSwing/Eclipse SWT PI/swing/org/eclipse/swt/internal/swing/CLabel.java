/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

class CSeparator extends JPanel implements CLabel {

  protected Label handle;

  protected JSeparator separator;

  public Container getSwingComponent() {
    return separator;
  }

  public Control getSWTHandle() {
    return handle;
  }

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

  public void setText(String text, int mnemonicIndex) {
  }

  public void setAlignment(int alignment) {
  }

  public void setIcon(Icon icon) {
  }

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      separator.setOpaque(true);
      break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      separator.setOpaque(false);
      break;
    }
  }

}

class CLabelImplementation extends JMultiLineLabel implements CLabel {

  protected Label handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CLabelImplementation(Label label, int style) {
    this.handle = label;
    init(style);
  }

  protected void init(int style) {
    setWrapping((style & SWT.WRAP) != 0);
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    }
    if((style & SWT.RIGHT) != 0) {
      setAlignment(JMultiLineLabel.RIGHT);
    } else if((style & SWT.CENTER) != 0) {
      setAlignment(JMultiLineLabel.CENTER);
    } else  {
      setAlignment(JMultiLineLabel.LEFT);
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

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE: setOpaque(false); break;
    }
  }

}

/**
 * The label equivalent on the Swing side.
 * @version 1.0 2005.08.20
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CLabel extends CControl {

  public static class Instanciator {
    private Instanciator() {}

    public static CLabel createInstance(Label label, int style) {
      if((style & SWT.SEPARATOR) != 0) {
        return new CSeparator(label, style);
      }
      return new CLabelImplementation(label, style);
    }

  }

  public void setText(String text, int mnemonicIndex);

  public void setAlignment(int alignment);

  public void setIcon(Icon icon);

}
