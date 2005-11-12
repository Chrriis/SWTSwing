/*
 * @(#)CCombo.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;

public interface CCombo extends CComposite {

  class CComboBox extends JComboBox implements CCombo {

    protected Combo handle;

    public CComboBox(Combo combo, int style) {
      this.handle = combo;
      init(style);
    }

    protected void init(int style) {
      setEditable((style & SWT.READ_ONLY) == 0);
      ((AbstractDocument)((JTextComponent)getEditor().getEditorComponent()).getDocument()).setDocumentFilter(new DocumentFilter() {
//        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//        }
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
          if(getEditorText().length() - length + text.length() > getEditorTextLimit()) {
            return;
          }
          super.replace(fb, offset, length, text, attrs);
        }
//        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
//        }
      });
    }

    public JScrollBar getHorizontalScrollBar() {
      // TODO: implement
      return null;
    }

    public JScrollBar getVerticalScrollBar() {
      // TODO: implement
      return null;
    }

    public Container getClientArea() {
      return this;
    }

    public void insertElementAt(Object anObject, int index) {
      ((DefaultComboBoxModel)getModel()).insertElementAt(anObject, index);
    }

    public String getEditorText() {
      return ((JTextComponent)getEditor().getEditorComponent()).getText();
    }

    public void setEditorText(String text) {
      ((JTextComponent)getEditor().getEditorComponent()).setText(text);
    }

    public void copyEditor() {
      ((JTextComponent)getEditor().getEditorComponent()).copy();
    }

    public void cutEditor() {
      ((JTextComponent)getEditor().getEditorComponent()).cut();
    }

    public void pasteEditor() {
      ((JTextComponent)getEditor().getEditorComponent()).paste();
    }

    public void setEditorCaretPosition(int index) {
      ((JTextComponent)getEditor().getEditorComponent()).setCaretPosition(index);
    }

    public int getEditorSelectionStart() {
      return ((JTextComponent)getEditor().getEditorComponent()).getSelectionStart();
    }

    public void setEditorSelectionStart(int selectionStart) {
      ((JTextComponent)getEditor().getEditorComponent()).setSelectionStart(selectionStart);
    }

    public int getEditorSelectionEnd() {
      return ((JTextComponent)getEditor().getEditorComponent()).getSelectionEnd();
    }

    public void setEditorSelectionEnd(int selectionEnd) {
      ((JTextComponent)getEditor().getEditorComponent()).setSelectionEnd(selectionEnd);
    }

    public void clearEditorSelection() {
      JTextComponent textComponent = (JTextComponent)getEditor().getEditorComponent();
      textComponent.setSelectionStart(textComponent.getSelectionEnd());
    }

    public void setEditorTextLimit(int limit) {
      textLimit = limit;
      String text = getEditorText();
      if(text.length() > limit) {
        setEditorText(text.substring(0, limit));
      }
    }

    protected int textLimit = Combo.LIMIT;

    public int getEditorTextLimit() {
      return textLimit;
    }

    public Dimension getEditorSize() {
      return ((JTextComponent)getEditor().getEditorComponent()).getSize();
    }

  }

  public static class Instanciator {
    private Instanciator() {}

    public static CCombo createInstance(Combo combo, int style) {
//      if ((style & SWT.SIMPLE) != 0) {
      return new CComboBox(combo, style);
    }

  }

  public void addItem(Object anObject);

  public Object getItemAt(int index);

  public void removeItemAt(int anIndex);

  public void removeAllItems();

  public void insertElementAt(Object anObject, int index);

  public int getItemCount();

  public int getSelectedIndex();

  public void setSelectedIndex(int index);

  public int getMaximumRowCount();

  public void setMaximumRowCount(int count);

  public void setComponentOrientation(ComponentOrientation o);

  public String getEditorText();

  public void setEditorText(String text);

  public void copyEditor();

  public void cutEditor();

  public void pasteEditor();

  public void setEditorCaretPosition(int index);

  public int getEditorSelectionStart();

  public void setEditorSelectionStart(int selectionStart);

  public int getEditorSelectionEnd();

  public void setEditorSelectionEnd(int selectionEnd);

  public void clearEditorSelection();

  public void setEditorTextLimit(int limit);

  public int getEditorTextLimit();

  public Dimension getEditorSize();

}
