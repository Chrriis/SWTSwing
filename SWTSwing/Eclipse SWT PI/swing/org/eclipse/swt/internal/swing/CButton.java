/*
 * @(#)CButton.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

class CButtonCommon {

  public static void applyStyle(AbstractButton component, int style) {
    component.setHorizontalAlignment((style & SWT.TRAIL) != 0? AbstractButton.TRAILING: (style & SWT.CENTER) != 0? AbstractButton.CENTER: AbstractButton.LEADING);
  }

}

class CButtonArrow extends CArrowButton implements CButton {

  protected Button handle;

  protected static int getDirection(int style) {
    int direction = 0;
    if((style & SWT.UP) != 0) {
      direction = CArrowButton.NORTH;
    } else if((style & SWT.DOWN) != 0) {
      direction = CArrowButton.SOUTH;
    } else if((style & SWT.LEFT) != 0) {
      direction = CArrowButton.WEST;
    } else if((style & SWT.RIGHT) != 0) {
      direction = CArrowButton.EAST;
    }
    return direction;
  }

  public CButtonArrow(Button button, int style) {
    super(getDirection(style));
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    enableEvents(handle.getAWTEvents());
    CButtonCommon.applyStyle(this, style);
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setDirection(alignment);
  }
  
  protected void fireActionPerformed(ActionEvent e) {
    processEvent(e);
  }

  public void processEvent(AWTEvent e) {
    if(handle.beforeProcessEvent(e)) {
      super.processEvent(e);
      handle.afterProcessEvent(e);
    }
  }

}

class CButtonPush extends JButton implements CButton {

  protected Button handle;

  public CButtonPush(Button button, int style) {
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    enableEvents(handle.getAWTEvents());
    CButtonCommon.applyStyle(this, style);
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }

  protected void fireActionPerformed(ActionEvent e) {
    processEvent(e);
  }

  public void processEvent(AWTEvent e) {
    if(handle.beforeProcessEvent(e)) {
      super.processEvent(e);
      handle.afterProcessEvent(e);
    }
  }

}

class CButtonCheck extends JCheckBox implements CButton {

  protected Button handle;

  public CButtonCheck(Button button, int style) {
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    enableEvents(handle.getAWTEvents());
    CButtonCommon.applyStyle(this, style);
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }
  
  protected void fireActionPerformed(ActionEvent e) {
    processEvent(e);
  }

  public void processEvent(AWTEvent e) {
    if(handle.beforeProcessEvent(e)) {
      super.processEvent(e);
      handle.afterProcessEvent(e);
    }
  }

}

class CButtonToggle extends JToggleButton implements CButton {

  protected Button handle;

  public CButtonToggle(Button button, int style) {
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    enableEvents(handle.getAWTEvents());
    CButtonCommon.applyStyle(this, style);
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }

  protected void fireActionPerformed(ActionEvent e) {
    processEvent(e);
  }

  public void processEvent(AWTEvent e) {
    if(handle.beforeProcessEvent(e)) {
      super.processEvent(e);
      handle.afterProcessEvent(e);
    }
  }

}

class CButtonRadio extends JRadioButton implements CButton {

  protected Button handle;

  public CButtonRadio(Button button, int style) {
    this.handle = button;
    init(style);
  }
  
  protected void init(int style) {
    enableEvents(handle.getAWTEvents());
    CButtonCommon.applyStyle(this, style);
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }
  
  protected void fireActionPerformed(ActionEvent e) {
    processEvent(e);
  }

  public void processEvent(AWTEvent e) {
    if(handle.beforeProcessEvent(e)) {
      super.processEvent(e);
      handle.afterProcessEvent(e);
    }
  }

}

/**
 * The button equivalent on the Swing side.
 * @version 1.0 2005.03.14
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CButton extends CComponent {

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

}
