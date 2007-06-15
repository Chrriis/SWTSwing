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
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Control;

class CBrowserImplementation extends JScrollPane implements CBrowser {
  
  protected Browser handle;
  protected JEditorPane editorPane;
  
  public Container getSwingComponent() {
    return editorPane;
  }
  
  public Control getSWTHandle() {
    return handle;
  }
  
  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CBrowserImplementation(Browser browser, int style) {
    this.handle = browser;
    editorPane = new JEditorPane() {
      public Color getBackground() {
        return CBrowserImplementation.this != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CBrowserImplementation.this != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CBrowserImplementation.this != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        return CBrowserImplementation.this != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
      }
    };
    userAttributeHandler = new UserAttributeHandler(editorPane);
    editorPane.setContentType("text/html");
    JViewport viewport = getViewport();
    viewport.setView(editorPane);
    init(style);
  }
  
  protected void init(int style) {
    editorPane.setEditable(false);
    editorPane.putClientProperty(JEditorPane.W3C_LENGTH_UNITS, Boolean.TRUE);
    editorPane.addHyperlinkListener(new HyperlinkListener() {
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          try {
            forwardActionList.clear();
            backActionList.add(getCurrentCommand());
            editorPane.setPage(e.getURL());
          } catch(Exception ex) {
          }
        }
      }
    });
    if((style & SWT.BORDER) == 0) {
      setBorder(null);
      editorPane.setBorder(null);
    }
  }
  
  public Container getClientArea() {
    return this;
  }
  
  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }
  
  public void setBackgroundInheritance(int backgroundInheritanceType) {
//      switch(backgroundInheritanceType) {
//      case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
//      case PREFERRED_BACKGROUND_INHERITANCE:
//      case BACKGROUND_INHERITANCE: setOpaque(false); break;
//      }
  }
  
  public void stop() {
    
  }
  
  protected Runnable getCurrentCommand() {
    final String text = editorPane.getText();
    return new Runnable() {
      public void run() {
        editorPane.setText(text);
      }
    };
  }
  
//    protected boolean isRunningAction;
  protected ArrayList backActionList = new ArrayList();
  protected ArrayList forwardActionList = new ArrayList();
  
  public boolean back() {
    if(!isBackEnabled()) {
      return false;
    }
//      isRunningAction = true;
    forwardActionList.add(getCurrentCommand());
    ((Runnable)backActionList.remove(backActionList.size() - 1)).run();
//      isRunningAction = false;
    return true;
  }
  
  public boolean forward() {
    if(!isForwardEnabled()) {
      return false;
    }
//      isRunningAction = true;
    backActionList.add(getCurrentCommand());
    ((Runnable)forwardActionList.remove(forwardActionList.size() - 1)).run();
//      isRunningAction = false;
    return true;
  }
  
  public String getURL() {
    URL url = editorPane.getPage();
    return url == null? "": url.toString();
  }
  
  public boolean setURL(String url) {
    if(url != null && url.startsWith("file://")) {
      url = "file:/" + url.substring("file://".length());
    }
    try {
      forwardActionList.clear();
      backActionList.add(getCurrentCommand());
      editorPane.setPage(url);
      return true;
    } catch(Exception e) {
      return false;
    }
  }
  
  public void refresh() {
    Runnable runnable = getCurrentCommand();
    editorPane.setText("");
    runnable.run();
  }
  
  public boolean isBackEnabled() {
    return !backActionList.isEmpty();
  }
  
  public boolean isForwardEnabled() {
    return !forwardActionList.isEmpty();
  }
  
  public boolean setText(String html) {
    try {
      forwardActionList.clear();
      backActionList.add(getCurrentCommand());
      editorPane.setText(html);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          editorPane.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
        }
      });
      return true;
    } catch(Exception e) {
      // TODO: test that an exception is thrown if the editor kit cannot handle the text.
      return false;
    }
  }
  
}

public interface CBrowser extends CComposite {


  public static class Factory {
    private Factory() {}

    public static CBrowser newInstance(Browser browser, int style) {
//      if ((style & SWT.SIMPLE) != 0) {
      return new CBrowserImplementation(browser, style);
    }

  }

  public void stop();
  
  public boolean back();

  public boolean forward();

  public String getURL();
  
  public boolean setURL(String url);
  
  public void refresh();

  public boolean isBackEnabled();

  public boolean isForwardEnabled();

  public boolean setText(String html);

}
