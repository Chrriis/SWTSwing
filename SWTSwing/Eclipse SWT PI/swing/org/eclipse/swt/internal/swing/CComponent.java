/*
 * @(#)CComponent.java
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
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * The interface shared by all controls.
 * @version 1.0 2005.03.13
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CComponent {

  public Rectangle getBounds();

  public Point getLocation();

  public Dimension getSize();

  public Dimension getMinimumSize();

  public Container getClientArea();

  // TODO: implement so that it traverses the complete hierarchy
  public String getToolTipText();

  // TODO: implement so that it traverses the complete hierarchy
  public void setToolTipText(String string);

  public void setBackgroundImage(Image backgroundImage);

}
