/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ExpandBar;

class CExpandBarImplementation extends JScrollPane implements CExpandBar {

  protected ExpandBar handle;
  protected JExpandPane expandPane;

  public Container getSwingComponent() {
    return this;
  }

  public CExpandBarImplementation(ExpandBar expandBar, int style) {
    this.handle = expandBar;
    expandPane = new JExpandPane();
    getViewport().setView(expandPane);
    init(style);
  }

  protected void init(int style) {
    setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    if((style & SWT.V_SCROLL) == 0) {
      setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    expandPane.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return expandPane;
  }

  public void addExpandPaneItem(String title, Icon icon, Component component) {
    expandPane.addExpandPaneItem(title, icon, component);
  }

  public void insertExpandPaneItem(String title, Icon icon, Component component, int index) {
    expandPane.insertExpandPaneItem(title, icon, component, index);
  }
  
  public void removeExpandPaneItem(Component component) {
    expandPane.removeExpandPaneItem(component);
  }

  public void setExpanded(Component component, boolean isExpanded) {
    expandPane.getExpandItem(component).setExpanded(isExpanded);
  }

  public boolean isExpanded(Component component) {
    return expandPane.getExpandItem(component).isExpanded();
  }

  public Dimension getTitleBarSize(Component component) {
    return expandPane.getExpandItem(component).getTitleBarSize();
  }

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
    case BACKGROUND_INHERITANCE: setOpaque(false); break;
    }
  }

  public void setIcon(Component component, ImageIcon icon) {
    expandPane.getExpandItem(component).setIcon(icon);
  }

  public void setText(Component component, String text) {
    expandPane.getExpandItem(component).setText(text);
  }
  
  public void setSpacing(int spacing) {
    expandPane.setSpacing(spacing);
  }

  public int getSpacing() {
    return expandPane.getSpacing();
  }

}

public interface CExpandBar extends CScrollable {

  public static class Instanciator {
    private Instanciator() {}

    public static CExpandBar createInstance(ExpandBar expandBar, int style) {
      return new CExpandBarImplementation(expandBar, style);
    }

  }

  public void addExpandPaneItem(String title, Icon icon, Component component);

  public void insertExpandPaneItem(String title, Icon icon, Component component, int index);
  
  public void removeExpandPaneItem(Component component);

  public void setExpanded(Component component, boolean isExpanded);

  public boolean isExpanded(Component component);

  public Dimension getTitleBarSize(Component component);

  public void setIcon(Component component, ImageIcon icon);

  public void setText(Component component, String text);

  public void setSpacing(int spacing);

  public int getSpacing();

}
