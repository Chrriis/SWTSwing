/*
 * @(#)CScale.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.Image;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scale;

class CScaleImplementation extends JSlider implements CScale {

  protected Scale handle;

  public CScaleImplementation(Scale scale, int style) {
    handle = scale;
    init(style);
  }

  protected void init(int style) {
    setMaximum(100);
    setMinorTickSpacing(1);
    setMajorTickSpacing(10);
    setPaintTicks(true);
    setSnapToTicks(true);
    setValue(0);
    setOrientation((style & SWT.HORIZONTAL) != 0? JSlider.HORIZONTAL: JSlider.VERTICAL);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return this;
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

public interface CScale extends CComponent {

  public static class Instanciator {
    private Instanciator() {}

    public static CScale createInstance(Scale scale, int style) {
      return new CScaleImplementation(scale, style);
    }

  }

  public int getMinimum();

  public int getMaximum();

  public int getValue();

  public void setMinimum(int maximum);

  public void setMaximum(int maximum);

  public void setValue(int n);

  public void setMinorTickSpacing(int n);

  public void setMajorTickSpacing(int n);

  public int getMinorTickSpacing();

  public int getMajorTickSpacing();

}
