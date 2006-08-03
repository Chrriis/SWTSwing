/*
 * @(#)CSlider.java
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
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;
import javax.swing.JSlider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Slider;

class CSliderImplementation extends JScrollBar implements CSlider {

  protected Slider handle;

  public CSliderImplementation(Slider slider, int style) {
    super((style & SWT.HORIZONTAL) != 0? JSlider.HORIZONTAL: JSlider.VERTICAL);
    handle = slider;
    init(style);
  }

  protected void init(int style) {
    setMaximum(100);
    setValue(0);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
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

public interface CSlider extends CComponent {

  public static class Instanciator {
    private Instanciator() {}

    public static CSlider createInstance(Slider slider, int style) {
      return new CSliderImplementation(slider, style);
    }

  }

  public int getMinimum();

  public int getMaximum();

  public int getValue();

  public int getVisibleAmount();

  public void setMinimum(int maximum);

  public void setMaximum(int maximum);

  public void setValue(int value);

  public void setVisibleAmount(int visibleAmount);

  public void setValues(int newValue, int newExtent, int newMin, int newMax);

  public int getUnitIncrement();

  public void setUnitIncrement(int unitIncrement);

  public int getBlockIncrement();

  public void setBlockIncrement(int blockIncrement);

}
