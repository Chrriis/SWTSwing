/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

class CComboImplementation extends JComboBox implements CCombo {

  protected Combo handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CComboImplementation(Combo combo, int style) {
    this.handle = combo;
    setLightWeightPopupEnabled(Utils.isLightweightPopups());
    init(style);
  }

  protected boolean isDefaultButtonHackActive;
  
  public boolean isPopupVisible() {
    boolean isPopupVisible = super.isPopupVisible();
    if(!isPopupVisible) {
      return isDefaultButtonHackActive;
    }
    return isPopupVisible;
  }

  protected void init(int style) {
    setEditable((style & SWT.READ_ONLY) == 0);
    JTextField textField = (JTextField)getEditor().getEditorComponent();
    // We put a listener before and after all existing listeners to place and remove the hack
    // The hack is there because the combo notifies the default button of the rootpane when its popup is not visible 
    ActionListener[] actionListeners = textField.getActionListeners();
    for(int i=actionListeners.length-1; i>=0; i--) {
      textField.removeActionListener(actionListeners[i]);
    }
    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        isDefaultButtonHackActive = false;
      }
    });
    for(int i=0; i<actionListeners.length; i++) {
      textField.addActionListener(actionListeners[i]);
    }
    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        isDefaultButtonHackActive = true;
        handle.processEvent(e);
      }
    });
    ((AbstractDocument)((JTextComponent)getEditor().getEditorComponent()).getDocument()).setDocumentFilter(new DocumentFilter() {
//      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//      }
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if(getEditorText().length() - length + text.length() > getEditorTextLimit()) {
          return;
        }
        super.replace(fb, offset, length, text, attrs);
      }
//      public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
//      }
    });
    addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        handle.processEvent(e);
      }
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

  public void reshape(int x, int y, int w, int h) {
    super.reshape(x, y, w, getPreferredSize().height);
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

public interface CCombo extends CComposite {

  public static class Factory {
    private Factory() {}

    public static CCombo newInstance(Combo combo, int style) {
//      if ((style & SWT.SIMPLE) != 0) {
      return new CComboImplementation(combo, style);
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
