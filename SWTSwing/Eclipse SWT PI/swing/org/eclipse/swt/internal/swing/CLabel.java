/*
 * @(#)CLabel.java
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;

class CSeparator extends JPanel implements CLabel {

  protected Label handle;

  protected JSeparator separator;

  public CSeparator(Label label, int style) {
    this.handle = label;
    GridBagLayout gridBag = new GridBagLayout();
    setLayout(gridBag);
    GridBagConstraints c = new GridBagConstraints();
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.fill = (style & SWT.HORIZONTAL) != 0? GridBagConstraints.HORIZONTAL: GridBagConstraints.VERTICAL;
    separator = new JSeparator((style & SWT.HORIZONTAL) != 0? JSeparator.HORIZONTAL: JSeparator.VERTICAL);
    gridBag.setConstraints(separator, c);
    add(separator);
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    }
    Utils.installMouseListener(separator, handle);
    Utils.installKeyListener(separator, handle);
    Utils.installFocusListener(separator, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return separator;
  }

  public String getLabelText() {
    return null;
  }

  public void setLabelText(String text) {
  }

  public void setAlignment(int alignment) {
  }

  public void setIcon(Icon icon) {
  }

}

class CLabelImplementation extends JLabel implements CLabel {

  protected Label handle;

  protected boolean isWrapping;

  public CLabelImplementation(Label label, int style) {
    this.handle = label;
    init(style);
  }

//  public void reshape(int x, int y, int w, int h) {
//    if(isWrapping) {
//      View view = (View)getClientProperty(BasicHTML.propertyKey);
//      Dimension size = super.getSize();
//      view.setSize(size.width, Integer.MAX_VALUE);
//      Dimension preferredSize = super.getPreferredSize();
//      view.setSize(size.width, size.height);
////      if(size.height != preferredSize.height) {
//        super.reshape(x, y, w, preferredSize.height);
//        revalidate();
//        repaint();
////      }
//    }
//  }

  public Dimension getPreferredSize() {
    if(isWrapping) {
      Dimension preferredSize = super.getPreferredSize();
      View view = ((View)getClientProperty(BasicHTML.propertyKey)).getView(0);
      Dimension size = super.getSize();
      view.setSize(size.width, 0);
      preferredSize.height = super.getPreferredSize().height;
//      view.setSize(size.width, size.height);
//      System.err.println(preferredSize);
      return preferredSize;
    }
//    System.err.println(super.getPreferredSize());
    return super.getPreferredSize();
  }

  protected void init(int style) {
    isWrapping = (style & SWT.WRAP) != 0;
    if((style & SWT.BORDER) != 0) {
      setBorder(UIManager.getBorder("TextField.border"));
    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }

  public void setLabelText(String text) {
    if(isWrapping) {
      super.setText("<html>" + escapeXML(text) + "</html>");
    } else {
      super.setText(text);
    }
  }

  public String getLabelText() {
    if(isWrapping) {
      String text = getText();
      text = text.substring(6, text.length() - 7);
      return unescapeXML(text);
    }
    return getText();
  }

  public static String escapeXML(String s) {
    if(s == null || s.length() == 0) {
      return s;
    }
    StringBuffer sb = new StringBuffer((int)(s.length() * 1.1));
    for(int i=0; i<s.length(); i++) {
      char c = s.charAt(i);
      switch(c) {
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '&':
          sb.append("&amp;");
          break;
        case '\'':
          sb.append("&apos;");
          break;
        case '\"':
          sb.append("&quot;");
          break;
        default:
          sb.append(c);
        break;
      }
    }
    return sb.toString();
  }

  public static String unescapeXML(String s) {
    if(s == null || s.length() < 3) {
      return s;
    }
    char[] chars = new char[s.length()];
    int pos = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if(c == '&') {
        String right = s.substring(i + 1);
        if(right.startsWith("lt;")) {
          chars[pos] = '<';
          i += 3;
        } else if(right.startsWith("gt;")) {
          chars[pos] = '>';
          i += 3;
        } else if(right.startsWith("amp;")) {
          chars[pos] = '&';
          i += 4;
        } else if(right.startsWith("apos;")) {
          chars[pos] = '\'';
          i += 5;
        } else if(right.startsWith("quot;")) {
          chars[pos] = '\"';
          i += 5;
        } else {
          chars[pos++] = c;
        }
      } else {
        chars[pos++] = c;
      }
    }
    if(pos == chars.length) {
      return s;
    }
    return new String(chars, 0, pos);
  }

}

/**
 * The button equivalent on the Swing side.
 * @version 1.0 2005.08.20
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CLabel extends CComponent {

  public static class Instanciator {
    private Instanciator() {}

    public static CLabel createInstance(Label label, int style) {
      if((style & SWT.SEPARATOR) != 0) {
        return new CSeparator(label, style);
      }
      return new CLabelImplementation(label, style);
    }

  }

  public String getLabelText();

  public void setLabelText(String text);

  public void setAlignment(int alignment);

  public void setIcon(Icon icon);

}
