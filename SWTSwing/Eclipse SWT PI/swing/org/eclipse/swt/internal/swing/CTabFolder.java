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
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;

class CTabFolderImplementation extends JTabbedPane implements CTabFolder {

  protected TabFolder handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CTabFolderImplementation(TabFolder tabFolder, int style) {
    this.handle = tabFolder;
    init(style);
  }

  protected void init(int style) {
    setTabPlacement((style & SWT.BOTTOM) != 0? BOTTOM: TOP);
    setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return this;
  }

  public JScrollBar getHorizontalScrollBar() {
    return null;
  }

  public JScrollBar getVerticalScrollBar() {
    return null;
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

  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    int count = getTabCount();
    if(count > 0) {
      Rectangle bounds = getUI().getTabBounds(this, count - 1);
      size.width = Math.max(size.width, bounds.x + bounds.width + 10);
    }
    return size;
  }

}

public interface CTabFolder extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CTabFolder newInstance(TabFolder tabFolder, int style) {
      return new CTabFolderImplementation(tabFolder, style);
    }

  }

  public void setTitleAt(int index, String title);

  public void setMnemonicAt(int tabIndex, int mnemonic);

  public void setIconAt(int index, Icon icon);

  public int getSelectedIndex();

  public void setSelectedIndex(int index);

  public void setToolTipTextAt(int index, String toolTipText);

}
