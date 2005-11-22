/*
 * @(#)CList.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;

class CListImplementation extends JScrollPane implements CList {

  protected List handle;
  protected JList list;

  public CListImplementation(List list, int style) {
    this.handle = list;
    this.list = new JList(new DefaultListModel());
    setFocusable(false);
    getViewport().setView(this.list);
    init(style);
  }

  protected void init(int style) {
    setFont(list.getFont());
    list.setSelectionMode((style & SWT.MULTI) != 0? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION: ListSelectionModel.SINGLE_SELECTION);
    Utils.installMouseListener(list, handle);
    Utils.installKeyListener(list, handle);
    Utils.installFocusListener(list, handle);
    Utils.installComponentListener(this, handle);
    list.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2) {
          handle.processEvent(new ActionEvent(list, ActionEvent.ACTION_PERFORMED, null));
        }
      }
    });
    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public void setFont(Font font) {
    super.setFont(font);
    if(list != null) {
      list.setFont(font);
    }
  }

  public Container getClientArea() {
    return list;
  }

  public void addElement(Object obj) {
    ((DefaultListModel)list.getModel()).addElement(obj);
  }

  public void insertElementAt(Object obj, int index) {
    ((DefaultListModel)list.getModel()).insertElementAt(obj, index);
  }

  public void removeElementAt(int index) {
    ((DefaultListModel)list.getModel()).removeElementAt(index);
  }

  public void removeRange(int fromIndex, int toIndex) {
    ((DefaultListModel)list.getModel()).removeRange(fromIndex, toIndex);
  }

  public void removeAllElements() {
    ((DefaultListModel)list.getModel()).removeAllElements();
  }

  public Object getElementAt(int index) {
    return list.getModel().getElementAt(index);
  }

  public void setElementAt(Object obj, int index) {
    ((DefaultListModel)list.getModel()).setElementAt(obj, index);
  }

  public void setElements(Object[] objects) {
    removeAllElements();
    DefaultListModel model = ((DefaultListModel)list.getModel());
    for(int i=0; i<objects.length; i++) {
      model.addElement(objects[i]);
    }
  }

  public int indexOf(Object obj, int index) {
    return ((DefaultListModel)list.getModel()).indexOf(obj, index);
  }

  public int getItemCount() {
    return list.getModel().getSize();
  }

  public int getMinSelectionIndex() {
    return list.getSelectionModel().getMinSelectionIndex();
  }

  public int getMaxSelectionIndex() {
    return list.getSelectionModel().getMaxSelectionIndex();
  }

  public int[] getSelectionIndices() {
    int min = getMinSelectionIndex();
    if(min == -1) {
      return new int[0];
    }
    int max = getMaxSelectionIndex();
    ArrayList list = new ArrayList(max - min + 1);
    for(int i=min; i<=max; i++) {
      if(isSelectedIndex(i)) {
        list.add(new Integer(i));
      }
    }
    int[] selectionIndices = new int[list.size()];
    for(int i=0; i<list.size(); i++) {
      selectionIndices[i] = ((Integer)list.get(i)).intValue();
    }
    return selectionIndices;
  }

  public boolean isSelectedIndex(int index) {
    return list.getSelectionModel().isSelectedIndex(index);
  }

  public void setSelectedElements(Object[] elements) {
    DefaultListModel listModel = ((DefaultListModel)list.getModel());
    for(int i=0; i<elements.length; i++) {
      int index = listModel.indexOf(elements[i]);
      if(index >= 0) {
        list.setSelectedIndex(index);
      }
    }
  }

  public void addSelectionInterval(int index0, int index1) {
    list.getSelectionModel().addSelectionInterval(index0, index1);
  }

  public void setSelectionInterval(int index0, int index1) {
    list.getSelectionModel().setSelectionInterval(index0, index1);
  }

  public void removeSelectionInterval(int index0, int index1) {
    list.getSelectionModel().removeSelectionInterval(index0, index1);
  }

  public void showSelection() {
    int min = list.getMinSelectionIndex();
    if(min == -1) {
      return;
    }
    int max = list.getMaxSelectionIndex();
    scrollRectToVisible(list.getCellBounds(min, max));
  }

  public int getFirstVisibleIndex() {
    return list.getFirstVisibleIndex();
  }

  public void setFirstVisibleIndex(int index) {
    if(index < 0) {
      return;
    }
    if(index >= getItemCount()) {
      return;
    }
    scrollRectToVisible(new Rectangle(0, 0, getWidth(), 0));
    scrollRectToVisible(list.getCellBounds(index, index));
  }

}

public interface CList extends CScrollable {

  public static class Instanciator {
    private Instanciator() {}

    public static CList createInstance(List list, int style) {
      return new CListImplementation(list, style);
    }

  }

  public void addElement(Object obj);

  public void insertElementAt(Object obj, int index);

  public void removeElementAt(int index);

  public void removeRange(int fromIndex, int toIndex);

  public void removeAllElements();

  public Object getElementAt(int index);

  public void setElementAt(Object obj, int index);

  public void setElements(Object[] objects);

  public int indexOf(Object obj, int index);

  public int getItemCount();

  public int getMinSelectionIndex();

  public int getMaxSelectionIndex();

  public int[] getSelectionIndices();

  public boolean isSelectedIndex(int index);

  public void setSelectedElements(Object[] elements);

  public void addSelectionInterval(int index0, int index1);

  public void setSelectionInterval(int index0, int index1);

  public void removeSelectionInterval(int index0, int index1);

  public void showSelection();

  public int getFirstVisibleIndex();

  public void setFirstVisibleIndex(int index);

}