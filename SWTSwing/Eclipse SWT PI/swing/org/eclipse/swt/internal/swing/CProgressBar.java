/*
 * @(#)CProgressBar.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;

import javax.swing.JProgressBar;

import org.eclipse.swt.widgets.ProgressBar;

class CProgressBarImplementation extends JProgressBar implements CProgressBar {

  protected ProgressBar handle;

  public CProgressBarImplementation(ProgressBar progressBar, int style) {
    handle = progressBar;
    init(style);
  }

  protected void init(int style) {
    
  }

  public Container getClientArea() {
    return this;
  }

}

public interface CProgressBar extends CComponent {

  public static class Instanciator {
    private Instanciator() {}

    public static CProgressBar createInstance(ProgressBar progressBar, int style) {
      return new CProgressBarImplementation(progressBar, style);
    }

  }

  public int getMinimum();

  public int getMaximum();

  public int getValue();

  public void setMinimum(int maximum);

  public void setMaximum(int maximum);

  public void setValue(int n);

}
