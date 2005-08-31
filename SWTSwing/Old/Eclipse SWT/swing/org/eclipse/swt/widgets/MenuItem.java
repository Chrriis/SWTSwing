/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * 
 * Swing port: Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com/swtswing
*******************************************************************************/
package org.eclipse.swt.widgets;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CHECK, CASCADE, PUSH, RADIO, SEPARATOR</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Arm, Help, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, CASCADE, PUSH, RADIO and SEPARATOR
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class MenuItem extends Item {
  Menu parent, menu;
  int id, accelerator;

  /**
   * Constructs a new instance of this class given its parent
   * (which must be a <code>Menu</code>) and a style value
   * describing its behavior and appearance. The item is added
   * to the end of the items maintained by its parent.
   * <p>
   * The style value is either one of the style constants defined in
   * class <code>SWT</code> which is applicable to instances of this
   * class, or must be built by <em>bitwise OR</em>'ing together
   * (that is, using the <code>int</code> "|" operator) two or more
   * of those <code>SWT</code> style constants. The class description
   * lists the style constants that are applicable to the class.
   * Style bits are also inherited from superclasses.
   * </p>
   *
   * @param parent a menu control which will be the parent of the new instance (cannot be null)
   * @param style the style of control to construct
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
       *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
   * </ul>
   *
   * @see SWT#CHECK
   * @see SWT#CASCADE
   * @see SWT#PUSH
   * @see SWT#RADIO
   * @see SWT#SEPARATOR
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public MenuItem(Menu parent, int style) {
    super(parent, checkStyle(style));
    this.parent = parent;
    swingHandle = getNewHandle();
    parent.createItem(this, parent.getItemCount());
  }

  Container getHandle() {
    return swingHandle;
  }

  Container swingHandle;

  /**
   * Constructs a new instance of this class given its parent
   * (which must be a <code>Menu</code>), a style value
   * describing its behavior and appearance, and the index
   * at which to place it in the items maintained by its parent.
   * <p>
   * The style value is either one of the style constants defined in
   * class <code>SWT</code> which is applicable to instances of this
   * class, or must be built by <em>bitwise OR</em>'ing together
   * (that is, using the <code>int</code> "|" operator) two or more
   * of those <code>SWT</code> style constants. The class description
   * lists the style constants that are applicable to the class.
   * Style bits are also inherited from superclasses.
   * </p>
   *
   * @param parent a menu control which will be the parent of the new instance (cannot be null)
   * @param style the style of control to construct
   * @param index the index to store the receiver in its parent
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
       *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
   * </ul>
   *
   * @see SWT#CHECK
   * @see SWT#CASCADE
   * @see SWT#PUSH
   * @see SWT#RADIO
   * @see SWT#SEPARATOR
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public MenuItem(Menu parent, int style, int index) {
    super(parent, checkStyle(style));
    this.parent = parent;
    swingHandle = getNewHandle();
    parent.createItem(this, index);
  }

  MenuItem(Menu parent, Menu menu, int style, int index) {
    super(parent, checkStyle(style));
    this.parent = parent;
    this.menu = menu;
    if(menu != null) {
      menu.cascade = this;
    }
    Decorations shell = parent.parent;
    shell.add(this);
  }

  Container getNewHandle() {
    if((style & SWT.SEPARATOR) != 0) {
      return new JSeparator();
    }
    AbstractButton button = null;
    if((style & SWT.CASCADE) != 0) {
      button = new JMenu();
    } else if((style & SWT.PUSH) != 0) {
      button = new JMenuItem();
    } else if((style & SWT.CHECK) != 0) {
      button = new JCheckBoxMenuItem();
    } else if((style & SWT.RADIO) != 0) {
      button = new JRadioButtonMenuItem();
    }
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent e) {
        swingMenuItemMouseEntered(e);
      }
    });
    // TODO: find what events to handle
    button.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        swingMenuItemItemStateChanged(e);
      }
    });
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        swingMenuItemActionPerformed(e);
      }
    });
//    ((JMenuItem)button).addItemListener(new java.awt.event.ItemListener() {
//      public void itemStateChanged(java.awt.event.ItemEvent e) {
//        Event event = new Event();
//        event.widget = MenuItem.this;
//        postEvent(e.getStateChange() == java.awt.event.ItemEvent.SELECTED? SWT.Show: SWT.Hide, event);
//      }
//    });
    return button;
  }

  void swingMenuItemMouseEntered(java.awt.event.MouseEvent e) {
    if((style & SWT.CASCADE) == 0) {
      Event event = new Event();
      event.stateMask = parent.getDisplay().getInputState();
      sendEvent(SWT.Arm, event);
    }
  }

  void swingMenuItemItemStateChanged(ItemEvent e) {
    if(e.getStateChange() == ItemEvent.SELECTED) {
      Event event = new Event();
      event.stateMask = parent.getDisplay().getInputState();
      sendEvent(SWT.Arm, event);
    }
  }

  void swingMenuItemActionPerformed(ActionEvent e) {
    Event event = new Event();
    event.stateMask = parent.getDisplay().getInputState();
    postEvent(SWT.Selection, event);
  }
  
  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the arm events are generated for the control, by sending
   * it one of the messages defined in the <code>ArmListener</code>
   * interface.
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see ArmListener
   * @see #removeArmListener
   */
  public void addArmListener(ArmListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Arm, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the help events are generated for the control, by sending
   * it one of the messages defined in the <code>HelpListener</code>
   * interface.
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see HelpListener
   * @see #removeHelpListener
   */
  public void addHelpListener(HelpListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Help, typedListener);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the control is selected, by sending
   * it one of the messages defined in the <code>SelectionListener</code>
   * interface.
   * <p>
   * When <code>widgetSelected</code> is called, the stateMask field of the event object is valid.
   * <code>widgetDefaultSelected</code> is not called.
   * </p>
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SelectionListener
   * @see #removeSelectionListener
   * @see SelectionEvent
   */
  public void addSelectionListener(SelectionListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Selection, typedListener);
    addListener(SWT.DefaultSelection, typedListener);
  }

  protected void checkSubclass() {
    if(!isValidSubclass()) {
      error(SWT.ERROR_INVALID_SUBCLASS);
    }
  }

  static int checkStyle(int style) {
    return checkBits(style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR,
                     SWT.CASCADE, 0);
  }

  void fillAccel(ACCEL accel) {
    accel.fVirt = 0;
    accel.cmd = accel.key = 0;
    if(accelerator == 0) {
      return;
    }
    int fVirt = OS.FVIRTKEY;
    int key = accelerator & SWT.KEY_MASK;
    int vKey = Display.untranslateKey(key);
    if(vKey != 0) {
      key = vKey;
    } else {
      switch(key) {
        /*
         * Bug in Windows.  For some reason, VkKeyScan
         * fails to map ESC to VK_ESCAPE and DEL to
         * VK_DELETE.  The fix is to map these keys
         * as a special case.
         */
        case 27:
          key = OS.VK_ESCAPE;
          break;
        case 127:
          key = OS.VK_DELETE;
          break;
        default: {
          key = wcsToMbcs((char)key);
          if(key == 0) {
            return;
          }
          if(OS.IsWinCE) {
            key = OS.CharUpper((short)key);
          } else {
            vKey = OS.VkKeyScan((short)key) & 0xFF;
            if(vKey == -1) {
              fVirt = 0;
            } else {
              key = vKey;
            }
          }
        }
      }
    }
    accel.key = (short)key;
    accel.cmd = (short)id;
    accel.fVirt = (byte)fVirt;
    if((accelerator & SWT.ALT) != 0) {
      accel.fVirt |= OS.FALT;
    }
    if((accelerator & SWT.SHIFT) != 0) {
      accel.fVirt |= OS.FSHIFT;
    }
    if((accelerator & SWT.CONTROL) != 0) {
      accel.fVirt |= OS.FCONTROL;
    }
  }

  /**
   * Return the widget accelerator.  An accelerator is the bit-wise
   * OR of zero or more modifier masks and a key. Examples:
   * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
   *
   * @return the accelerator
   *
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getAccelerator() {
    checkWidget();
    return accelerator;
  }

  public Display getDisplay() {
    Menu parent = this.parent;
    if(parent == null) {
      error(SWT.ERROR_WIDGET_DISPOSED);
    }
    return parent.getDisplay();
  }

  /**
   * Returns <code>true</code> if the receiver is enabled, and
   * <code>false</code> otherwise. A disabled control is typically
   * not selectable from the user interface and draws with an
   * inactive or "grayed" look.
   *
   * @return the receiver's enabled state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #isEnabled
   */
  public boolean getEnabled() {
    checkWidget();
    return getHandle().isEnabled();
  }

  /**
   * Returns the receiver's cascade menu if it has one or null
   * if it does not. Only <code>CASCADE</code> menu items can have
   * a pull down menu. The sequence of key strokes, button presses
   * and/or button releases that are used to request a pull down
   * menu is platform specific.
   *
   * @return the receiver's menu
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Menu getMenu() {
    checkWidget();
    return menu;
  }

  String getNameText() {
    if((style & SWT.SEPARATOR) != 0) {
      return "|";
    }
    return super.getNameText();
  }

  /**
   * Returns the receiver's parent, which must be a <code>Menu</code>.
   *
   * @return the receiver's parent
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Menu getParent() {
    checkWidget();
    return parent;
  }

  /**
   * Returns <code>true</code> if the receiver is selected,
   * and false otherwise.
   * <p>
   * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
   * it is selected when it is checked.
   *
   * @return the selection state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean getSelection() {
    checkWidget();
    if((style & (SWT.CHECK | SWT.RADIO)) == 0) {
      return false;
    }
    return ((AbstractButton)getHandle()).isSelected();
  }

  /**
   * Returns <code>true</code> if the receiver is enabled and all
   * of the receiver's ancestors are enabled, and <code>false</code>
   * otherwise. A disabled control is typically not selectable from the
   * user interface and draws with an inactive or "grayed" look.
   *
   * @return the receiver's enabled state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #getEnabled
   */
  public boolean isEnabled() {
    return getEnabled() && parent.isEnabled();
  }

  void releaseChild() {
    super.releaseChild();
    if(menu != null) {
      menu.dispose();
    }
    menu = null;
    parent.destroyItem(this);
  }

  void releaseMenu() {
    menu = null;
  }

  void releaseWidget() {
    if(menu != null) {
      menu.releaseResources();
    }
    menu = null;
    super.releaseWidget();
    accelerator = 0;
    Decorations shell = parent.parent;
    shell.remove(this);
    parent = null;
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the arm events are generated for the control.
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see ArmListener
   * @see #addArmListener
   */
  public void removeArmListener(ArmListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Arm, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the help events are generated for the control.
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see HelpListener
   * @see #addHelpListener
   */
  public void removeHelpListener(HelpListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Help, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the control is selected.
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SelectionListener
   * @see #addSelectionListener
   */
  public void removeSelectionListener(SelectionListener listener) {
    checkWidget();
    if(listener == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(eventTable == null) {
      return;
    }
    eventTable.unhook(SWT.Selection, listener);
    eventTable.unhook(SWT.DefaultSelection, listener);
  }

  void selectRadio() {
    int index = 0;
    MenuItem[] items = parent.getItems();
    while(index < items.length && items[index] != this) {
      index++;
    }
    int i = index - 1;
    while(i >= 0 && items[i].setRadioSelection(false)) {
      --i;
    }
    int j = index + 1;
    while(j < items.length && items[j].setRadioSelection(false)) {
      j++;
    }
    setSelection(true);
  }

  /**
   * Sets the widget accelerator.  An accelerator is the bit-wise
   * OR of zero or more modifier masks and a key. Examples:
   * <code>SWT.MOD1 | SWT.MOD2 | 'T', SWT.MOD3 | SWT.F2</code>.
   * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
   *
   * @param accelerator an integer that is the bit-wise OR of masks and a key
   *
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setAccelerator(int accelerator) {
    checkWidget();
    if(this.accelerator == accelerator) {
      return;
    }
    this.accelerator = accelerator;
    int key = accelerator & SWT.KEY_MASK;
    int vKey = Display.untranslateKey(key);
    if(vKey != 0) {
      key = vKey; 
    }
    Container handle = getHandle();
    if(handle instanceof JMenu) {
      ((JMenu)handle).setMnemonic(key);
    } else {
      int modifiers = 0;
      if((accelerator & SWT.ALT) != 0) {
        modifiers |= java.awt.event.KeyEvent.ALT_DOWN_MASK;
      } 
      if((accelerator & SWT.SHIFT) != 0) {
        modifiers |= java.awt.event.KeyEvent.SHIFT_DOWN_MASK;
      } 
      if((accelerator & SWT.CONTROL) != 0) {
        modifiers |= java.awt.event.KeyEvent.CTRL_DOWN_MASK;
      } 
      ((JMenuItem)handle).setAccelerator(KeyStroke.getKeyStroke(key, modifiers));
    }
  }

  /**
   * Enables the receiver if the argument is <code>true</code>,
   * and disables it otherwise. A disabled control is typically
   * not selectable from the user interface and draws with an
   * inactive or "grayed" look.
   *
   * @param enabled the new enabled state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setEnabled(boolean enabled) {
    checkWidget();
    getHandle().setEnabled(enabled);
  }

  /**
   * Sets the image the receiver will display to the argument.
   * <p>
   * Note: This feature is not available on all window systems (for example, Window NT),
   * in which case, calling this method will silently do nothing.
   *
   * @param menu the image to display
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setImage(Image image) {
    checkWidget();
    if((style & SWT.SEPARATOR) != 0) {
      return;
    }
    super.setImage(image);
    // TODO: set image
    Container handle = getHandle();
    ((AbstractButton)getHandle()).setIcon(new ImageIcon(image.swingHandle));
  }

  /**
   * Sets the receiver's pull down menu to the argument.
   * Only <code>CASCADE</code> menu items can have a
   * pull down menu. The sequence of key strokes, button presses
   * and/or button releases that are used to request a pull down
   * menu is platform specific.
   *
   * @param menu the new pull down menu
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_MENU_NOT_DROP_DOWN - if the menu is not a drop down menu</li>
   *    <li>ERROR_MENUITEM_NOT_CASCADE - if the menu item is not a <code>CASCADE</code></li>
   *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li>
       *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setMenu(Menu menu) {
    checkWidget();

    /* Check to make sure the new menu is valid */
    if((style & SWT.CASCADE) == 0) {
      error(SWT.ERROR_MENUITEM_NOT_CASCADE);
    }
    if(menu != null) {
      if(menu.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
      }
      if((menu.style & SWT.DROP_DOWN) == 0) {
        error(SWT.ERROR_MENU_NOT_DROP_DOWN);
      }
      if(menu.parent != parent.parent) {
        error(SWT.ERROR_INVALID_PARENT);
      }
    }

    /* Assign the new menu */
    Menu oldMenu = this.menu;
    if(oldMenu == menu) {
      return;
    }
    if(oldMenu != null) {
      oldMenu.cascade = null;
    }
    this.menu = menu;
    if (menu != null) {
      menu.cascade = this; 
    }
    JMenu swingMenuHandle = (JMenu)getHandle();
    menu.swingHandle = swingMenuHandle.getPopupMenu();
    swingMenuHandle.addMenuListener(new javax.swing.event.MenuListener() {
      public void menuCanceled(javax.swing.event.MenuEvent e) {
      }
      public void menuDeselected(javax.swing.event.MenuEvent e) {
        if(MenuItem.this.menu != null) {
          Event event = new Event();
          MenuItem.this.menu.postEvent(SWT.Hide, event);
        }
      }
      public void menuSelected(javax.swing.event.MenuEvent e) {
        if(MenuItem.this.menu != null) {
          Event event = new Event();
          MenuItem.this.menu.postEvent(SWT.Show, event);
        }
      }
    });
    // Reset the handle of the parent
    menu.parent.remove(menu);
    menu.parent.add(menu);
  }

  boolean setRadioSelection(boolean value) {
    if((style & SWT.RADIO) == 0) {
      return false;
    }
    if(getSelection() != value) {
      setSelection(value);
      postEvent(SWT.Selection);
    }
    return true;
  }

  /**
   * Sets the selection state of the receiver.
   * <p>
   * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
   * it is selected when it is checked.
   *
   * @param selected the new selection state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSelection(boolean selected) {
    checkWidget();
    if((style & (SWT.CHECK | SWT.RADIO)) == 0) {
      return;
    }
    ((AbstractButton)getHandle()).setSelected(selected);
  }

  /**
   * Sets the receiver's text. The string may include
   * the mnemonic character and accelerator text.
   * <p>
   * Mnemonics are indicated by an '&amp' that causes the next
   * character to be the mnemonic.  When the user presses a
   * key sequence that matches the mnemonic, a selection
   * event occurs. On most platforms, the mnemonic appears
   * underlined but may be emphasised in a platform specific
   * manner.  The mnemonic indicator character '&amp' can be
   * escaped by doubling it in the string, causing a single
   *'&amp' to be displayed.
   * </p>
   * <p>
   * Accelerator text is indicated by the '\t' character.
   * On platforms that support accelerator text, the text
   * that follows the '\t' character is displayed to the user,
   * typically indicating the key stroke that will cause
   * the item to become selected.  On most platforms, the
   * accelerator text appears right aligned in the menu.
   * Setting the accelerator text does not install the
   * accelerator key sequence. The accelerator key sequence
   * is installed using #setAccelerator.
   * </p>
   *
   * @param string the new text
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #setAccelerator
   */
  public void setText(String string) {
    checkWidget();
    if(string == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if((style & SWT.SEPARATOR) != 0) {
      return;
    }
    if(text.equals(string)) {
      return;
    }
    super.setText(string);
    AbstractButton button = (AbstractButton)getHandle();
    int index = findMnemonicIndex(string);
    if(index != -1) {
      button.setMnemonic(string.charAt(index));
      string = string.substring(0, index - 1) + string.substring(index);
    }
    // TODO: check what to do for cascade (CHECK, CASCADE, PUSH, RADIO and SEPARATOR)
    index = string.lastIndexOf('\t');
    if(index != -1) {
      string = string.substring(0, index);
    }
    // Set the text
    button.setText(string);
    java.awt.Component comp = button.getParent();
    if(comp instanceof javax.swing.JPopupMenu) {
      ((javax.swing.JPopupMenu)comp).pack();
    }
  }

}
