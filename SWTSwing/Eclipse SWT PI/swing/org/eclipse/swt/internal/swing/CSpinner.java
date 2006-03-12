/*
 * @(#)CSpinner.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;

class CSpinnerImplementation extends JSpinner implements CSpinner {

  protected Spinner handle;

  protected SpinnerNumberModel model;

  public CSpinnerImplementation(Spinner spinner, int style) {
    this.handle = spinner;
    model = new SpinnerNumberModel(0, minimum, maximum, 1);
    setModel(model);
    model.setMinimum(new Comparable() {
      public int compareTo(Object o) {
        return minimum - ((Number)o).intValue();
      }
    });
    model.setMaximum(new Comparable() {
      public int compareTo(Object o) {
        return maximum - ((Number)o).intValue();
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

  public void reshape(int x, int y, int w, int h) {
    super.reshape(x, y, w, getPreferredSize().height);
  }

  public Container getClientArea() {
    return this;
  }

  public int getStepSize() {
    return model.getStepSize().intValue();
  }

  public void setStepSize(int stepSize) {
    model.setStepSize(new Integer(stepSize));
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
    model.setValue(new Integer(value));
  }

  public int getSelectedValue() {
    return ((Number)model.getValue()).intValue();
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

}

/**
 * The spinner equivalent on the Swing side.
 * @version 1.0 2006.03.12
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CSpinner extends CComponent {

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

}
