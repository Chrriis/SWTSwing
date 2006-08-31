/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.Image;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;

class CSpinnerImplementation extends JSpinner implements CSpinner {

  protected Spinner handle;

  protected SpinnerNumberModel model;

  public Container getSwingComponent() {
    return this;
  }

  public CSpinnerImplementation(Spinner spinner, int style) {
    this.handle = spinner;
    model = new SpinnerNumberModel(0, minimum, maximum, 1.0);
    setModel(model);
    model.setMinimum(new Comparable() {
      public int compareTo(Object o) {
        return minimum - Math.round(((Number)o).floatValue() * (int)Math.pow(10, digitCount));
      }
    });
    model.setMaximum(new Comparable() {
      public int compareTo(Object o) {
        return maximum - Math.round(((Number)o).floatValue() * (int)Math.pow(10, digitCount));
      }
    });
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.READ_ONLY) != 0) {
      ((DefaultEditor)getEditor()).getTextField().setEditable(false);
    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return this;
  }

  public int getStepSize() {
    return Math.round(model.getStepSize().floatValue() * (int)Math.pow(10, digitCount));
  }

  public void setStepSize(int stepSize) {
    model.setStepSize(new Float((float)stepSize / (int)Math.pow(10, digitCount)));
  }

  protected int minimum = 0;
  protected int maximum = 100;
  
  public int getMinimum() {
    return minimum;
  }

  public int getMaximum() {
    return maximum;
  }
  
  public void setMinimum(int minimum) {
    this.minimum = minimum;
  }

  public void setMaximum(int maximum) {
    this.maximum = maximum;
  }
  
  public void setSelectedValue(int value) {
    model.setValue(new Float((float)value / (int)Math.pow(10, digitCount)));
  }

  public int getSelectedValue() {
    return Math.round(((Number)model.getValue()).floatValue() * (int)Math.pow(10, digitCount));
  }

  public void copy() {
    ((DefaultEditor)getEditor()).getTextField().copy();
  }

  public void cut() {
    ((DefaultEditor)getEditor()).getTextField().cut();
  }

  public void paste() {
    ((DefaultEditor)getEditor()).getTextField().paste();
  }

  protected int digitCount;

  public void setDigitCount(int digitCount) {
    if(digitCount == this.digitCount) {
      return;
    }
    this.digitCount = digitCount;
  }

  public int getDigitCount() {
    return digitCount;
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
 * The spinner equivalent on the Swing side.
 * @version 1.0 2006.03.12
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CSpinner extends CControl {

  public static class Instanciator {
    private Instanciator() {}

    public static CSpinner createInstance(Spinner spinner, int style) {
      return new CSpinnerImplementation(spinner, style);
    }

  }

  public int getStepSize();

  public void setStepSize(int stepSize);

  public int getMinimum();

  public int getMaximum();
  
  public void setMinimum(int minimum);

  public void setMaximum(int maximum);

  public void setSelectedValue(int value);

  public int getSelectedValue();

  public void copy();

  public void cut();

  public void paste();

  public void setDigitCount(int digitCount);

  public int getDigitCount();

}
