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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.eclipse.swt.widgets.Control;

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

  static void installMouseListener(Component component, final Control control) {
    component.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseReleased(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseClicked(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseEntered(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseExited(MouseEvent e) {
        control.processEvent(e);
      }
    });
    component.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseMoved(MouseEvent e) {
        control.processEvent(e);
      }
    });
    component.addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installKeyListener(Component component, final Control control) {
    component.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        control.processEvent(e);
      }
      public void keyReleased(KeyEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installFocusListener(Component component, final Control control) {
    component.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        control.processEvent(e);
      }
      public void focusLost(FocusEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installComponentListener(Component component, final Control control) {
    component.addComponentListener(new ComponentListener() {
      public void componentHidden(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentResized(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        control.processEvent(e);
      }
    });
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

  public static String convertStringToHTML(String string) {
    StringBuffer sb = new StringBuffer("<html>");
    for(int i=0; i<string.length(); i++) {
      char c = string.charAt(i);
      switch(c) {
      case '\r':
        sb.append("<p>");
        break;
      case '\n':
        sb.append("<p>");
        break;
      default:
        sb.append(Utils.escapeXML(String.valueOf(c)));
        break;
      }
    }
    sb.append("</html>");
    return sb.toString();
  }

}
