/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.View;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

class CDateTimeImplementation extends JTextField implements CDateTime {

  protected DateTime handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CDateTimeImplementation(DateTime dateTime, int style) {
    setFocusable(false);
    this.handle = dateTime;
    userAttributeHandler = new UserAttributeHandler(this) {
      public void setForeground(Color foreground) {
        super.setForeground(foreground);
        adjustStyles();
      }
      public void setFont(Font font) {
        super.setFont(font);
        adjustStyles();
      }
    };
    setText("Not implemented");
    init(style);
  }
  
  protected void init(int style) {
    setEditable(false);
    putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    setOpaque(false);
    adjustStyles();
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  protected void adjustStyles() {
    updateUI();
    LookAndFeelUtils.applyTextFieldStyle(this);
    setMargin(new Insets(0, 0, 0, 0));
    if((handle.getStyle() & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    }
    reshape(getX(), getY(), getWidth(), getHeight());
  }
  
  public void reshape(int x, int y, int w, int h) {
    super.reshape(x, y, w, h);
    View globalView = getUI().getRootView(this);
    if(globalView != null) {
      Border border = getBorder();
      if(border != null) {
        Insets insets = border.getBorderInsets(this);
        w -= insets.left + insets.right;
        h -= insets.top + insets.bottom;
      }
      globalView.setSize(w, h);
    }
  }

  public Dimension getMaximumSize() {
    return new Dimension(Integer.MAX_VALUE, super.getMaximumSize().height);
  }

  public boolean isOpaque() {
    return backgroundImageIcon == null && (userAttributeHandler != null && userAttributeHandler.background != null || super.isOpaque());
  }
  protected void paintComponent(Graphics g) {
    Utils.paintTiledImage(this, g, backgroundImageIcon);
    super.paintComponent(g);
  }
  
  public Container getClientArea() {
    return this;
  }

  public Color getBackground() {
    return userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
  }
  public Color getForeground() {
    return userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
  }
  public Font getFont() {
    return userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
  }
  public Cursor getCursor() {
    if(Utils.globalCursor != null) {
      return Utils.globalCursor;
    }
    return userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
  }
  
  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
//    switch(backgroundInheritanceType) {
//    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
//    case PREFERRED_BACKGROUND_INHERITANCE:
//    case BACKGROUND_INHERITANCE: setOpaque(false); break;
//    }
  }

}

/**
 * The label equivalent on the Swing side.
 * @version 1.0 2005.08.20
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CDateTime extends CControl {

  public static class Factory {
    private Factory() {}

    public static CDateTime newInstance(DateTime link, int style) {
      return new CDateTimeImplementation(link, style);
    }

  }

}
