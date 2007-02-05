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
import java.util.Locale;

import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;

class CLinkImplementation extends JEditorPane implements CLink {

  protected Link handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CLinkImplementation(Link link, int style) {
    this.handle = link;
    setContentType("text/html");
    init(style);
  }

  public Dimension getPreferredSize() {
    Dimension preferredSize = super.getPreferredSize();
    View globalView = (View)getClientProperty(BasicHTML.propertyKey);
    if(globalView == null) {
      return super.getPreferredSize();
    }
    View view = globalView.getView(0);
    Dimension size = super.getSize();
    view.setSize(size.width, 0);
    preferredSize.height = super.getPreferredSize().height;
    return preferredSize;
  }

  protected void init(int style) {
    setEditable(false);
    setForeground(UIManager.getColor("Label.foreground"));
    setBackground(UIManager.getColor("Label.background"));
    setFont(UIManager.getFont("Label.font"));
    setOpaque(false);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addHyperlinkListener(new HyperlinkListener() {
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          handle.processEvent(e);
        }
      }
    });
  }

  public Container getClientArea() {
    return this;
  }

  protected String text = getText();

  public void setLinkText(String text) {
    this.text = text;
    super.setText("<html><body>" + escapeXML(text) + "</body></html>");
  }

  public static String escapeXML(String s) {
    if(s == null) {
      return s;
    }
    int length = s.length();
    if(length == 0) {
      return s;
    }
    StringBuffer sb = new StringBuffer((int)(length * 1.1));
    int start = -1;
    int lastEndTag = -1;
    for(int i=0; i<length; i++) {
      char c = s.charAt(i);
      switch(c) {
        case '<':
          start = i;
          break;
        case '>':
          if(start >= 0) {
            String tag = s.substring(start, i);
            String lTag = tag.toLowerCase(Locale.ENGLISH);
            if(lTag.equals("</a")) {
              if(lastEndTag >= 0) {
                String content = Utils.escapeSwingXML(s.substring(lastEndTag + 1, i - 3));
                sb.append("<a href=\"" + content + "\">" + content + "</a>");
                lastEndTag = -1;
                start = -1;
                break;
              }
              sb.append("</a>");
              start = -1;
              break;
            } else if(lTag.startsWith("<a ") || lTag.startsWith("<a")) {
              int hrefIndex = tag.indexOf("href=\"");
              if(hrefIndex == -1) {
                lastEndTag = i;
              } else {
                sb.append(s.substring(start, i + 1));
                start = -1;
              }
              break;
            }
          } else {
            sb.append("&gt;");
          }
          break;
        case '&':
          if(start < 0) {
            sb.append("&amp;");
          }
          break;
//        case '\'':
//          if(start < 0) {
//            sb.append("&apos;");
//          }
//          break;
        case '\"':
          if(start < 0) {
            sb.append("&quot;");
          }
          break;
        default:
          if(start < 0) {
            sb.append(c);
          }
        break;
      }
    }
    if(start >= 0) {
      sb.append(Utils.escapeSwingXML(s.substring(start, s.length())));
    }
    return sb.toString();
  }

  public String getLinkText() {
    return text;
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

/**
 * The label equivalent on the Swing side.
 * @version 1.0 2005.08.20
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CLink extends CControl {

  public static class Factory {
    private Factory() {}

    public static CLink newInstance(Link link, int style) {
      return new CLinkImplementation(link, style);
    }

  }

  public String getLinkText();

  public void setLinkText(String text);

}
