/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

class CButtonCommon {

  public static void applyStyle(AbstractButton component, int style) {
    component.setHorizontalAlignment((style & SWT.TRAIL) != 0? AbstractButton.TRAILING: (style & SWT.CENTER) != 0? AbstractButton.CENTER: AbstractButton.LEADING);
  }

}

class CButtonArrow extends ArrowButton implements CButton {

  protected Button handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected static int getDirection(int style) {
    int direction = 0;
    if((style & SWT.UP) != 0) {
      direction = ArrowButton.NORTH;
    } else if((style & SWT.DOWN) != 0) {
      direction = ArrowButton.SOUTH;
    } else if((style & SWT.LEFT) != 0) {
      direction = ArrowButton.WEST;
    } else if((style & SWT.RIGHT) != 0) {
      direction = ArrowButton.EAST;
    }
    return direction;
  }

  public CButtonArrow(Button button, int style) {
    super(getDirection(style));
    setMargin(new Insets(1, 1, 1, 1));
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    CButtonCommon.applyStyle(this, style);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    size.width = size.height;
    return size;
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setDirection(alignment);
  }
  
  protected void fireActionPerformed(ActionEvent e) {
    super.fireActionPerformed(e);
    handle.processEvent(e);
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

  public void reshape(int x, int y, int w, int h) {
    super.reshape(x, y, h, h);
  }

}

class CButtonPush extends JButton implements CButton {

  protected Button handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CButtonPush(Button button, int style) {
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    CButtonCommon.applyStyle(this, style);
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

  protected void fireActionPerformed(ActionEvent e) {
    super.fireActionPerformed(e);
    handle.processEvent(e);
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

class CButtonCheck extends JCheckBox implements CButton {

  protected Button handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CButtonCheck(Button button, int style) {
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    CButtonCommon.applyStyle(this, style);
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
  
  protected void fireActionPerformed(ActionEvent e) {
    super.fireActionPerformed(e);
    handle.processEvent(e);
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

class CButtonToggle extends JToggleButton implements CButton {

  protected Button handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CButtonToggle(Button button, int style) {
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    CButtonCommon.applyStyle(this, style);
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

  protected void fireActionPerformed(ActionEvent e) {
    super.fireActionPerformed(e);
    handle.processEvent(e);
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

class CButtonRadio extends JIconRadioButton implements CButton {

  protected Button handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CButtonRadio(Button button, int style) {
    this.handle = button;
    init(style);
  }

  protected void init(int style) {
    CButtonCommon.applyStyle(this, style);
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
  
  protected void fireActionPerformed(ActionEvent e) {
    if(!isSelected()) {
      setSelected(true);
    }
    Component[] components = getParent().getComponents();
    for(int i=0; i<components.length; i++) {
      Component component = components[i];
      if(component instanceof JRadioButton && component != this) {
        ((JRadioButton)component).setSelected(false);
      }
    }
    super.fireActionPerformed(e);
    handle.processEvent(e);
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
 * The button equivalent on the Swing side.
 * @version 1.0 2005.03.14
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CButton extends CControl {

  public static class Instanciator {
    private Instanciator() {}

    public static CButton createInstance(Button button, int style) {
      if((style & SWT.ARROW) != 0) {
        return new CButtonArrow(button, style);
      }
      if((style & SWT.PUSH) != 0) {
        return new CButtonPush(button, style);
      }
      if((style & (SWT.CHECK)) != 0) {
        return new CButtonCheck(button, style);
      }
      if((style & (SWT.TOGGLE)) != 0) {
        return new CButtonToggle(button, style);
      }
      if((style & (SWT.RADIO)) != 0) {
        return new CButtonRadio(button, style);
      }
      return null;
    }

  }

  public String getText();

  public boolean isSelected();

  public void setSelected(boolean isSelected);

  public void setAlignment(int alignment);

  public void setIcon(Icon icon);

  public void setText(String text);

  public void doClick();

  public void setDisplayedMnemonicIndex(int mnemonicIndex);

}
