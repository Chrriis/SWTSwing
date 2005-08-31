/*
 * @(#)Utils.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Canvas;
import java.awt.Component;

/**
 * General util methods.
 * @version 1.0 2005.03.18
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class Utils {

  protected Utils() {}

  protected static Canvas panel = new Canvas();

  public static Component getDefaultComponent() {
    return panel;
  }

}
