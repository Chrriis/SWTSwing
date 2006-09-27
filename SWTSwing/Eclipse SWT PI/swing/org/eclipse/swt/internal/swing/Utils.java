/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.dnd.DnDConstants;
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

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.widgets.Control;

/**
 * General util methods.
 * @version 1.0 2005.03.18
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class Utils {

  protected Utils() {}

  protected static final String LIGHTPOPUPS_PROPERTY = "swt.swing.lightpopups";
  protected static final String LOOK_AND_FEEL_PROPERTY = "swt.swing.laf";
  protected static final String LOOK_AND_FEEL_DECORATED_PROPERTY = "swt.swing.laf.decorated";

  public static final String SWTSwingPaintingClientProperty = "SWTSwingClientProperty";

  public static boolean isLightweightPopups() {
    return "true".equals(System.getProperty(LIGHTPOPUPS_PROPERTY));
  }

  public static String getLookAndFeel() {
    return System.getProperty(LOOK_AND_FEEL_PROPERTY);
  }

  public static Boolean isLookAndFeelDecorated() {
    String value = System.getProperty(LOOK_AND_FEEL_DECORATED_PROPERTY);
    return value == null? null: new Boolean(value);
  }

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
      public void keyTyped(KeyEvent e) {
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

  public static String escapeSwingXML(String s) {
    if(s == null) {
      return s;
    }
    int length = s.length();
    if(length == 0) {
      return s;
    }
    StringBuffer sb = new StringBuffer((int)(length * 1.1));
    for(int i=0; i < length; i++) {
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
//        case '\'':
//          sb.append("&apos;");
//          break;
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
    if(s == null) {
      return s;
    }
    int length = s.length();
    if(length < 3) {
      return s;
    }
    char[] chars = new char[length];
    int pos = 0;
    for (int i = 0; i < length; i++) {
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
    if (string == null) {
      return null;
    }
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
        sb.append(Utils.escapeSwingXML(String.valueOf(c)));
        break;
      }
    }
    sb.append("</html>");
    return sb.toString();
  }

  public static int convertDnDActionsToSWT(int actions) {
    if(actions == 0) {
      return 0;
    }
    int swtActions = 0;
    if((actions & DnDConstants.ACTION_COPY) != 0) {
      swtActions |= DND.DROP_COPY;
    }
    if((actions & DnDConstants.ACTION_MOVE) != 0) {
      swtActions |= DND.DROP_MOVE;
    }
    if((actions & DnDConstants.ACTION_LINK) != 0) {
      swtActions |= DND.DROP_LINK;
    }
    return swtActions;
  }

  public static int convertDnDActionsToSwing(int actions) {
    if(actions == 0) {
      return 0;
    }
    int swingActions = 0;
    if((actions & DND.DROP_COPY) != 0) {
      swingActions |= DnDConstants.ACTION_COPY;
    }
    if((actions & DND.DROP_MOVE) != 0) {
      swingActions |= DnDConstants.ACTION_MOVE;
    }
    if((actions & DND.DROP_LINK) != 0) {
      swingActions |= DnDConstants.ACTION_LINK;
    }
    return swingActions;
  }
  
  static long timeStamp = System.currentTimeMillis();

  public static int getCurrentTime () {
    return (int)(System.currentTimeMillis() - timeStamp);
  }

  /**
   * Indicates that the method is not implemented. It prints the corresponding frame from
   * the stack trace to the standard error if the "swt.swing.debug" property is defined.
   */
  public static void notImplemented() {
    if(System.getProperty("swt.swing.debug") == null) {
      return;
    }
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
    int i = 0;
    for(; i<stackTraceElements.length; i++) {
      StackTraceElement stackElement = stackTraceElements[i];
      if(stackElement.getMethodName().equals("notImplemented")) {
        System.err.println("Not implemented: " + stackTraceElements[i + 1]);
        break;
      }
    }
    return;
  }

}
