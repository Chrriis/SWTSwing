/*
 * @(#)CToolItem.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar.Separator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;

class CToolItemPush extends JButton implements CToolItem {

  protected ToolItem handle;

  public CToolItemPush(ToolItem toolItem, int style) {
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    setMargin(new Insets(0, 1, 0, 1));
  }

}

class CToolItemCheck extends JToggleButton implements CToolItem {

  protected ToolItem handle;

  public CToolItemCheck(ToolItem toolItem, int style) {
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    setMargin(new Insets(0, 1, 0, 1));
  }

}

class CToolItemRadio extends JToggleButton implements CToolItem {

  protected ToolItem handle;

  public CToolItemRadio(ToolItem toolItem, int style) {
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    setMargin(new Insets(0, 1, 0, 1));
    addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        handle.processEvent(e);
      }
    });
  }

}

class CToolItemDropDown extends CComboButton implements CToolItem {

  protected ToolItem handle;

  public CToolItemDropDown(ToolItem toolItem, int style) {
    super((style & SWT.FLAT) != 0);
    handle = toolItem;
    init(style);
  }

  protected void init(int style) {
    getPushButton().addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    });
    getDropButton().addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(new ActionEvent(e.getSource(), e.getID(), "Arrow", e.getWhen(), e.getModifiers()));
      }
    });
  }

  public boolean isSelected() {
    return false;
  }

  public void setSelected(boolean isSelected) {
  }

}

class CToolItemSeparator extends Separator implements CToolItem {

  protected ToolItem handle;

  public CToolItemSeparator(ToolItem toolItem, int style) {
    handle = toolItem;
  }

  public boolean isSelected() {
    return false;
  }

  public void setSelected(boolean isSelected) {
  }

  public void setIcon(Icon icon) {
  }

  public void setDisabledIcon(Icon disabledIcon) {
  }

  public void setRolloverIcon(Icon rolloverIcon) {
  }

  public void setText(String text) {
  }

  public void setMnemonic(char mnemonic) {
  }

}

public interface CToolItem {

  public static class Instanciator {
    private Instanciator() {}

    public static CToolItem createInstance(ToolItem toolItem, int style) {
      if((style & SWT.PUSH) != 0) {
        return new CToolItemPush(toolItem, style);
      }
      if((style & SWT.CHECK) != 0) {
        return new CToolItemCheck(toolItem, style);
      }
      if((style & SWT.RADIO) != 0) {
        return new CToolItemRadio(toolItem, style);
      }
      if((style & SWT.DROP_DOWN) != 0) {
        return new CToolItemDropDown(toolItem, style);
      }
      if((style & SWT.SEPARATOR) != 0) {
        return new CToolItemSeparator(toolItem, style);
      }
      return null;
    }

  }

  public boolean isSelected();

  public void setSelected(boolean isSelected);

  public String getToolTipText();

  public void setToolTipText(String toolTipText);

  public void setIcon(Icon icon);

  public void setDisabledIcon(Icon disabledIcon);

  public void setRolloverIcon(Icon rolloverIcon);

  public void setText(String text);

  public void setMnemonic(char mnemonic);

}
