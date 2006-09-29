/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventObject;

import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

class CTextMulti extends JScrollPane implements CText {

  protected Text handle;
  protected JTextArea textArea;

  public Container getSwingComponent() {
    return textArea;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CTextMulti(Text text, int style) {
    this.handle = text;
//    textArea = new JTextArea(4, 7);
    textArea = new JTextArea() {
      public Cursor getCursor() {
        if(!isCursorSet()) {
          return super.getCursor();
        }
        for(Component parent = this; (parent = parent.getParent()) != null; ) {
          if(parent.isCursorSet()) {
            Cursor cursor = parent.getCursor();
            if(!(parent instanceof Window) || cursor != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) {
              return cursor;
            }
          }
        }
        return super.getCursor();
      }
    };
    setFocusable(false);
    getViewport().setView(textArea);
    init(style);
  }

  public void requestFocus() {
    textArea.requestFocus();
  }
  
  protected KeyEvent keyEvent = null;

  protected void init(int style) {
    setFont(textArea.getFont());
    if((style & SWT.BORDER) == 0) {
      setBorder(null);
      textArea.setBorder(null);
    }
    if((style & SWT.WRAP) != 0) {
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
    }
    textArea.setEditable((style & SWT.READ_ONLY) == 0);
    if((style & SWT.H_SCROLL) == 0) {
      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }
    if((style & SWT.V_SCROLL) == 0) {
      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
      setBorder(null);
    }
    Utils.installMouseListener(textArea, handle);
    Utils.installKeyListener(textArea, handle);
    Utils.installFocusListener(textArea, handle);
    Utils.installComponentListener(this, handle);
    textArea.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e) {
        keyEvent = e;
      }
      public void keyReleased(KeyEvent e) {
        keyEvent = null;
      }
      public void keyTyped(KeyEvent e) {
      }
    });
    textArea.getDocument().addDocumentListener(new DocumentListener() {
    	public void changedUpdate(DocumentEvent e) {
    		handle.processEvent(e);
    	}
    	public void insertUpdate(DocumentEvent e) {
    		handle.processEvent(e);
    	}
    	public void removeUpdate(DocumentEvent e) {
    		handle.processEvent(e);
    	}
    });
    // TODO: find out the expected behaviour for default selection event
//    textArea.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        handle.processEvent(e);
//      }
//    });
    ((AbstractDocument)textArea.getDocument()).setDocumentFilter(new DocumentFilter() {
//      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//      }
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if(getText().length() - length + text.length() > getTextLimit()) {
          return;
        }
        FilterEvent filterEvent = new FilterEvent(this, text, offset, length, keyEvent);
        handle.processEvent(filterEvent);
        String s = filterEvent.getText();
        if(s != null) {
          super.replace(fb, offset, length, s, attrs);
        }
      }
      public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        FilterEvent filterEvent = new FilterEvent(this, "", offset, length, keyEvent);
        handle.processEvent(filterEvent);
        String s = filterEvent.getText();
        if(s != null) {
          super.replace(fb, offset, length, s, null);
        }
      }
    });
  }

  public void setFont(Font font) {
    super.setFont(font);
    if(textArea != null) {
      textArea.setFont(font);
    }
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
      int width = textArea.getFontMetrics(textArea.getFont()).stringWidth(textArea.getText().substring(textArea.getLineStartOffset(line), caretPosition));
      return new Point(width, line * getRowHeight());
    } catch(BadLocationException e) {
    }
    return null;
  }

  public void showSelection() {
    try {
      Rectangle rec1 = textArea.modelToView(getSelectionStart());
      Rectangle rec2 = textArea.modelToView(getSelectionEnd());
      if(rec1.y < rec2.y) {
        Dimension size = textArea.getSize();
        rec1.x = 0;
        rec1.width = size.width;
        rec2.x = 0;
        rec2.width = size.width;
      }
      rec1.add(rec2);
      scrollRectToVisible(rec1);
    } catch(Exception e) {
    }
  }

  public int getCaretLineNumber() {
    try {
      return textArea.getLineOfOffset(textArea.getCaretPosition());
    } catch(Exception e) {
      return -1;
    }
  }

  public int getRowHeight() {
    return textArea.getFontMetrics(textArea.getFont()).getHeight();
  }

  public void setComponentOrientation(ComponentOrientation o) {
    super.setComponentOrientation(o);
    textArea.setComponentOrientation(o);
  }

  public Point getViewPosition() {
    return getViewport().getViewPosition();
  }

  public void setViewPosition(Point p) {
    getViewport().setViewPosition(p);
  }

  public void setTextLimit(int limit) {
    textLimit = limit;
    String text = getText();
    if(text.length() > limit) {
      setText(text.substring(0, limit));
    }
  }

  protected int textLimit = Text.LIMIT;

  public int getTextLimit() {
    return textLimit;
  }

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      textArea.setOpaque(true);
      break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      textArea.setOpaque(false);
      break;
    }
  }

  public void setForeground(Color foreground) {
    super.setForeground(foreground);
    if(textArea != null) {
      textArea.setForeground(foreground);
    }
  }

  public void setBackground(Color background) {
    super.setBackground(background);
    if(textArea != null) {
      textArea.setBackground(background);
    }
  }
  
}

class CTextField extends JScrollPane implements CText {

  protected Text handle;
  protected JPasswordField passwordField;

  public Container getSwingComponent() {
    return passwordField;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CTextField(Text text, int style) {
    this.handle = text;
    passwordField = new JPasswordField() {
      public Cursor getCursor() {
        if(!isCursorSet()) {
          return super.getCursor();
        }
        for(Component parent = this; (parent = parent.getParent()) != null; ) {
          if(parent.isCursorSet()) {
            Cursor cursor = parent.getCursor();
            if(!(parent instanceof Window) || cursor != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) {
              return cursor;
            }
          }
        }
        return super.getCursor();
      }
    };
    passwordField.setEchoChar('\0');
    setFocusable(false);
    getViewport().setView(passwordField);
    init(style);
  }
  
  public void requestFocus() {
    passwordField.requestFocus();
  }
  
  protected KeyEvent keyEvent = null;
  
  protected void init(int style) {
    setFont(passwordField.getFont());
    if((style & SWT.BORDER) == 0) {
      setBorder(null);
      passwordField.setBorder(null);
    }
    passwordField.setEditable((style & SWT.READ_ONLY) == 0);
    if((style & SWT.H_SCROLL) == 0) {
      setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }
    if((style & SWT.V_SCROLL) == 0) {
      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
      setBorder(null);
    }
    Utils.installMouseListener(passwordField, handle);
    Utils.installKeyListener(passwordField, handle);
    Utils.installFocusListener(passwordField, handle);
    Utils.installComponentListener(this, handle);
    passwordField.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e) {
        keyEvent = e;
      }
      public void keyReleased(KeyEvent e) {
        keyEvent = null;
      }
      public void keyTyped(KeyEvent e) {
      }
    });
    passwordField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        handle.processEvent(e);
      }
      public void insertUpdate(DocumentEvent e) {
        handle.processEvent(e);
      }
      public void removeUpdate(DocumentEvent e) {
        handle.processEvent(e);
      }
    });
    ((AbstractDocument)passwordField.getDocument()).setDocumentFilter(new DocumentFilter() {
//      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//      }
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if(getText().length() - length + text.length() > getTextLimit()) {
          return;
        }
        FilterEvent filterEvent = new FilterEvent(this, text, offset, length, keyEvent);
        handle.processEvent(filterEvent);
        String s = filterEvent.getText();
        if(s != null) {
          super.replace(fb, offset, length, s, attrs);
        }
      }
      public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        FilterEvent filterEvent = new FilterEvent(this, "", offset, length, keyEvent);
        handle.processEvent(filterEvent);
        String s = filterEvent.getText();
        if(s != null) {
          super.replace(fb, offset, length, s, null);
        }
      }
    });
  }

  public void setFont(Font font) {
    super.setFont(font);
    if(passwordField != null) {
      passwordField.setFont(font);
    }
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
    passwordField.setEchoChar(echoChar);
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

  public void showSelection() {
    try {
      Rectangle rec1 = passwordField.modelToView(getSelectionStart());
      Rectangle rec2 = passwordField.modelToView(getSelectionEnd());
      rec1.add(rec2);
      scrollRectToVisible(rec1);
    } catch(Exception e) {
    }
  }

  public int getCaretLineNumber() {
    return 0;
  }

  public int getRowHeight() {
    return passwordField.getFontMetrics(passwordField.getFont()).getHeight();
  }

  public void setComponentOrientation(ComponentOrientation o) {
    super.setComponentOrientation(o);
    passwordField.setComponentOrientation(o);
  }

  public Point getViewPosition() {
    return getViewport().getViewPosition();
  }

  public void setViewPosition(Point p) {
    getViewport().setViewPosition(p);
  }

  public void setTextLimit(int limit) {
    textLimit = limit;
    String text = getText();
    if(text.length() > limit) {
      setText(text.substring(0, limit));
    }
  }

  protected int textLimit = Text.LIMIT;

  public int getTextLimit() {
    return textLimit;
  }

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      passwordField.setOpaque(true);
      break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      passwordField.setOpaque(false);
      break;
    }
  }

  public void setForeground(Color foreground) {
    super.setForeground(foreground);
    if(passwordField != null) {
      passwordField.setForeground(foreground);
    }
  }

  public void setBackground(Color background) {
    super.setBackground(background);
    if(passwordField != null) {
      passwordField.setBackground(background);
    }
  }
 
}

/**
 * The text equivalent on the Swing side.
 * @version 1.0 2005.08.30
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CText extends CScrollable {

  public static class FilterEvent extends EventObject {

    protected String text;
    protected int start;
    protected int end;
    protected KeyEvent keyEvent;

    public FilterEvent(Object source, String text, int start, int end, KeyEvent keyEvent) {
      super(source);
      this.text = text;
      this.start = start;
      this.end = end;
      this.keyEvent = keyEvent;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public int getStart() {
      return start;
    }

    public int getEnd() {
      return end;
    }

    public KeyEvent getKeyEvent() {
      return keyEvent;
    }

  }


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

  public void showSelection();

  public int getCaretLineNumber();

  public int getRowHeight();

  public void setComponentOrientation(ComponentOrientation o);

  public Point getViewPosition();

  public void setViewPosition(Point p);

  public void setTextLimit(int limit);

  public int getTextLimit();

}
