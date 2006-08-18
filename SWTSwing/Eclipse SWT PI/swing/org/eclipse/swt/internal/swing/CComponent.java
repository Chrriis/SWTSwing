/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
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

  public static final int NO_BACKGROUND_INHERITANCE = 0;
  public static final int PREFERRED_BACKGROUND_INHERITANCE = 1;
  public static final int BACKGROUND_INHERITANCE = 2;

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

  public void setBackgroundInheritance(int backgroundInheritanceType);

}
