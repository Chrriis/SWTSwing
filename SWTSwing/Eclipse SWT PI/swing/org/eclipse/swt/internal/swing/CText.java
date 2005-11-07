/*
 * @(#)CText.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.Point;
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

import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

class CTextMulti extends JScrollPane implements CText {

  protected Text handle;
  protected JTextArea textArea;

  public CTextMulti(Text text, int style) {
    this.handle = text;
    textArea = new JTextArea(4, 7);
    getViewport().setView(textArea);
    init(style);
  }
  
  protected void init(int style) {
    if((style & SWT.BORDER) == 0) {
      setBorder(null);
      textArea.setBorder(null);
    }
    if((style & SWT.H_SCROLL) == 0) {
      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }
    if((style & SWT.V_SCROLL) == 0) {
      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
      setBorder(null);
    }
    textArea.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseReleased(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseClicked(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseEntered(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseExited(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    textArea.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseMoved(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    textArea.addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        handle.processEvent(e);
      }
    });
    textArea.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        handle.processEvent(e);
      }
      public void keyReleased(KeyEvent e) {
        handle.processEvent(e);
      }
    });
    textArea.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        handle.processEvent(e);
      }
      public void focusLost(FocusEvent e) {
        handle.processEvent(e);
      }
    });
    addComponentListener(new ComponentListener() {
      public void componentHidden(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return textArea;
  }

  public String getText() {
    return textArea.getText();
  }

  public String getText(int offs, int len) throws BadLocationException {
    return textArea.getText(offs, len);
  }

  public void setText(String text) {
    textArea.setText(text);
  }

  public void setSelectionStart(int start) {
    textArea.setSelectionStart(start);
  }

  public void setSelectionEnd(int end) {
    textArea.setSelectionEnd(end);
  }

  public int getSelectionStart() {
    return textArea.getSelectionStart();
  }

  public int getSelectionEnd() {
    return textArea.getSelectionEnd();
  }

  public void selectAll() {
    textArea.selectAll();
  }

  public void setEditable(boolean isEditable) {
    textArea.setEditable(isEditable);
  }

  public void setEchoChar(char echoChar) {
  }

  public char getEchoChar() {
    return 0;
  }

  public void copy() {
    textArea.copy();
  }

  public void cut() {
    textArea.cut();
  }

  public void paste() {
    textArea.paste();
  }

  public void setTabSize(int tabSize) {
    textArea.setTabSize(tabSize);
  }

  public void replaceSelection(String content) {
    textArea.replaceSelection(content);
  }

  public boolean isEditable() {
    return textArea.isEditable();
  }

  public int getCaretPosition() {
    return textArea.getCaretPosition();
  }

  public int getLineCount() {
    return textArea.getLineCount();
  }

  public Point getCaretLocation() {
    int caretPosition = textArea.getCaretPosition();
    try {
      int line = textArea.getLineOfOffset(caretPosition);
      return new Point(caretPosition - textArea.getLineStartOffset(line), line);
    } catch(BadLocationException e) {
    }
    return null;
  }

}

class CTextField extends JScrollPane implements CText {

  protected Text handle;
  protected JPasswordField passwordField;

  public CTextField(Text text, int style) {
    this.handle = text;
    passwordField = new JPasswordField();
    passwordField.setEchoChar('\0');
    getViewport().setView(passwordField);
    init(style);
  }
  
  protected void init(int style) {
    if((style & SWT.BORDER) == 0) {
      setBorder(null);
      passwordField.setBorder(null);
    }
    if((style & SWT.H_SCROLL) == 0) {
      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }
    if((style & SWT.V_SCROLL) == 0) {
      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
      setBorder(null);
    }
    passwordField.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseReleased(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseClicked(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseEntered(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseExited(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    passwordField.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        handle.processEvent(e);
      }
      public void mouseMoved(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    passwordField.addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        handle.processEvent(e);
      }
    });
    passwordField.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        handle.processEvent(e);
      }
      public void keyReleased(KeyEvent e) {
        handle.processEvent(e);
      }
    });
    passwordField.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        handle.processEvent(e);
      }
      public void focusLost(FocusEvent e) {
        handle.processEvent(e);
      }
    });
    addComponentListener(new ComponentListener() {
      public void componentHidden(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return passwordField;
  }

  public String getText() {
    return new String(passwordField.getPassword());
  }

  public String getText(int offs, int len) throws BadLocationException {
    try {
      return new String(passwordField.getPassword()).substring(offs, len + offs);
    } catch(Exception e) {
      throw new BadLocationException(e.getMessage(), offs);
    }
  }

  public void setText(String text) {
    passwordField.setText(text);
  }

  public void setSelectionStart(int start) {
    passwordField.setSelectionStart(start);
  }

  public void setSelectionEnd(int end) {
    passwordField.setSelectionEnd(end);
  }

  public int getSelectionStart() {
    return passwordField.getSelectionStart();
  }

  public int getSelectionEnd() {
    return passwordField.getSelectionEnd();
  }

  public void selectAll() {
    passwordField.selectAll();
  }

  public void setEditable(boolean isEditable) {
    passwordField.setEditable(isEditable);
  }

  public void setEchoChar(char echoChar) {
  }

  public char getEchoChar() {
    return 0;
  }

  public void copy() {
    passwordField.copy();
  }

  public void cut() {
    passwordField.cut();
  }

  public void paste() {
    passwordField.paste();
  }

  public void setTabSize(int tabSize) {
  }

  public void replaceSelection(String content) {
    passwordField.replaceSelection(content);
  }

  public boolean isEditable() {
    return passwordField.isEditable();
  }

  public int getCaretPosition() {
    return passwordField.getCaretPosition();
  }

  public int getLineCount() {
    return 1;
  }

  public Point getCaretLocation() {
    return new Point(passwordField.getCaretPosition(), 0);
  }

}

/**
 * The text equivalent on the Swing side.
 * @version 1.0 2005.08.30
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CText extends CScrollable {

  public static class Instanciator {
    private Instanciator() {}

    public static CText createInstance(Text text, int style) {
      if((style & SWT.MULTI) != 0) {
        return new CTextMulti(text, style);
      }
      return new CTextField(text, style);
    }

  }

  public String getText();

  public String getText(int offs, int len) throws BadLocationException;

  public void setText(String text);

  public void setSelectionStart(int start);

  public void setSelectionEnd(int end);

  public int getSelectionStart();

  public int getSelectionEnd();

  public void selectAll();

  public void setEditable(boolean isEditable);

  public void setEchoChar(char echoChar);

  public char getEchoChar();

  public void copy();

  public void cut();

  public void paste();

  public void setTabSize(int tabSize);

  public void replaceSelection(String content);

  public boolean isEditable();

  public int getCaretPosition();

  public int getLineCount();

  public Point getCaretLocation();

}
