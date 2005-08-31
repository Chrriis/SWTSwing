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

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.swing.CArrowButton;
import org.eclipse.swt.internal.swing.Utils;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class ToolItem extends Item {
  ToolBar parent;
  Control control;
  String toolTipText;
  Image disabledImage, hotImage;
  Image disabledImage2;
  int id;

  /**
   * Constructs a new instance of this class given its parent
   * (which must be a <code>ToolBar</code>) and a style value
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
   * @param parent a composite control which will be the parent of the new instance (cannot be null)
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
   * @see SWT#PUSH
   * @see SWT#CHECK
   * @see SWT#RADIO
   * @see SWT#SEPARATOR
   * @see SWT#DROP_DOWN
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public ToolItem(ToolBar parent, int style) {
    super(parent, checkStyle(style));
    this.parent = parent;
    swingHandle = getNewHandle();
    addSwingListeners();
    parent.createItem(this, parent.getItemCount());
  }

  /**
   * Constructs a new instance of this class given its parent
   * (which must be a <code>ToolBar</code>), a style value
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
   * @param parent a composite control which will be the parent of the new instance (cannot be null)
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
   * @see SWT#PUSH
   * @see SWT#CHECK
   * @see SWT#RADIO
   * @see SWT#SEPARATOR
   * @see SWT#DROP_DOWN
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public ToolItem(ToolBar parent, int style, int index) {
    super(parent, checkStyle(style));
    this.parent = parent;
    swingHandle = getNewHandle();
    parent.createItem(this, index);
  }
  
  class CComboButton extends javax.swing.JPanel {
    JButton pushButton = new JButton() {
      protected void paintBorder(java.awt.Graphics g) {
        java.awt.Insets i = getBorder().getBorderInsets(this);
        int width = this.getWidth();
        int height = this.getHeight();
        if(width - i.left - i.right < i.right) {
          super.paintBorder(g);
        } else {
          java.awt.Shape clip = g.getClip();
          g.clipRect(0, 0, width - i.right, height);
          super.paintBorder(g);
          g.setClip(clip);
          g.clipRect(width - i.right, 0, width, height);
          g.translate(i.right, 0);
          super.paintBorder(g);
        }
      }
    };
    JButton getPushButton() {
      return pushButton;
    }
    JButton dropButton = new CArrowButton(CArrowButton.SOUTH);
    public void setIcon(Icon icon) {
      pushButton.setIcon(icon);
    }
    public void setDisabledIcon(Icon icon) {
      pushButton.setDisabledIcon(icon);
    }
    public void setSelectedIcon(Icon icon) {
      pushButton.setSelectedIcon(icon);
    }
    public void setRolloverIcon(Icon icon) {
      pushButton.setRolloverIcon(icon);
    }
    public void setPressedIcon(Icon icon) {
      pushButton.setPressedIcon(icon);
    }
    public String getToolTipText() {
      return pushButton.getToolTipText();
    }
    public void setToolTipText(String text) {
      pushButton.setToolTipText(text);
      dropButton.setToolTipText(text);
    }
    public CComboButton() {
      setLayout(new java.awt.BorderLayout(0, 0) {
        public java.awt.Dimension minimumLayoutSize(Container target) {
          return preferredLayoutSize(target);
        }
        public java.awt.Dimension maximumLayoutSize(Container target) {
          return preferredLayoutSize(target);
        }
      });
      if((parent.getStyle() & SWT.FLAT) != 0) {
        pushButton.setBorderPainted(false);
        dropButton.setBorderPainted(false);
        java.awt.event.MouseAdapter flatListener = new java.awt.event.MouseAdapter() {
          public void mouseEntered(java.awt.event.MouseEvent e) {
            pushButton.setBorderPainted(pushButton.isEnabled());
            dropButton.setBorderPainted(pushButton.isEnabled());
          }
          public void mouseExited(java.awt.event.MouseEvent e) {
            pushButton.setBorderPainted(pushButton.isSelected());
            dropButton.setBorderPainted(pushButton.isSelected());
          }
        };
        pushButton.addMouseListener(flatListener);
        dropButton.addMouseListener(flatListener);
      }
      pushButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          ToolItem.this.postEvent(SWT.Selection);
        }
      });
      dropButton.setPreferredSize(new java.awt.Dimension(16, 20));
      dropButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Event event = new Event();
          event.detail = SWT.ARROW;
          ToolItem.this.postEvent(SWT.Selection, event);
        }
      });
      pushButton.setMargin(new Insets(0, 0, 0, 0));
      dropButton.setMargin(new Insets(0, 0, 0, 0));
      add(pushButton, java.awt.BorderLayout.CENTER);
      add(dropButton, java.awt.BorderLayout.EAST);
    }
  }

  Container getHandle() {
    return swingHandle;
  }

  Container swingHandle;

  Container getNewHandle() {
    Container newHandle = null;
    if((style & SWT.PUSH) != 0) {
      JButton button = new JButton();
      button.setMargin(new java.awt.Insets(0, 1, 0, 1));
      newHandle = button;
    } else if((style & SWT.CHECK) != 0) {
      AbstractButton button = new JToggleButton();
      button.setMargin(new java.awt.Insets(0, 1, 0, 1));
      newHandle = button;
    } else if((style & SWT.RADIO) != 0) {
      final AbstractButton button = new JToggleButton();
      button.setMargin(new java.awt.Insets(0, 1, 0, 1));
      button.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if((style & SWT.RADIO) != 0) {
            if((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
              if(e.getStateChange() == ItemEvent.SELECTED) {
                selectRadio();
              }
            }
          }
        }
      });
      newHandle = button;
    } else if((style & SWT.DROP_DOWN) != 0) {
      newHandle = new CComboButton();
    } else if((style & SWT.SEPARATOR) != 0) {
      newHandle = new javax.swing.JToolBar.Separator();
    }
    if(((parent.getStyle() & SWT.FLAT) != 0) && (style & (SWT.PUSH | SWT.CHECK | SWT.RADIO)) != 0) {
      final AbstractButton button = (AbstractButton)newHandle;
      button.setBorderPainted(false);
      button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
          button.setBorderPainted(button.isEnabled());
        }
        public void mouseExited(java.awt.event.MouseEvent e) {
          button.setBorderPainted(button.isSelected());
        }
      });
    }
    return newHandle;
  }

  void addSwingListeners() {
    addSwingToolItemListeners();
  }

  void addSwingToolItemListeners() {
    if((style & SWT.PUSH) != 0) {
      ((AbstractButton)getHandle()).addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          swingActionPerformed(e);
        }
      });
    } else if((style & (SWT.CHECK | SWT.RADIO)) != 0) {
      ((AbstractButton)getHandle()).addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          swingItemStateChanged(e);
        }
      });
    }
  }

  void swingActionPerformed(ActionEvent e) {
    sendEvent(SWT.Selection);
  }

  void swingItemStateChanged(ItemEvent e) {
    sendEvent(SWT.Selection);
  }

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the control is selected, by sending
   * it one of the messages defined in the <code>SelectionListener</code>
   * interface.
   * <p>
   * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
   * the event object detail field contains the value <code>SWT.ARROW</code>.
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

  static int checkStyle(int style) {
    return checkBits(style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR,
                     SWT.DROP_DOWN, 0);
  }

  protected void checkSubclass() {
    if(!isValidSubclass()) {
      error(SWT.ERROR_INVALID_SUBCLASS);
    }
  }

  void click(boolean dropDown) {
    /*
     * In order to emulate all the processing that
     * happens when a mnemonic key is pressed, fake
     * a mouse press and release.  This will ensure
     * that radio and pull down items are handled
     * properly.
     */
    int hwnd = parent.handle;
    if(OS.GetKeyState(OS.VK_LBUTTON) < 0) {
      return;
    }
    int index = OS.SendMessage(hwnd, OS.TB_COMMANDTOINDEX, id, 0);
    RECT rect = new RECT();
    OS.SendMessage(hwnd, OS.TB_GETITEMRECT, index, rect);
    int lParam = (dropDown ? rect.right - 1 : rect.left) | (rect.top << 16);
    int hotIndex = OS.SendMessage(hwnd, OS.TB_GETHOTITEM, 0, 0);
    OS.SendMessage(hwnd, OS.WM_LBUTTONDOWN, 0, lParam);
    OS.SendMessage(hwnd, OS.WM_LBUTTONUP, 0, lParam);
    if(hotIndex != -1) {
      OS.SendMessage(hwnd, OS.TB_SETHOTITEM, hotIndex, 0);
    }
  }

  Image createDisabledImage(Image image, Color color) {
    Display display = getDisplay();
    if(OS.IsWinCE) {
      return new Image(display, image, SWT.IMAGE_DISABLE);
    }
    Rectangle rect = image.getBounds();
    Image disabled = new Image(display, rect);
    GC gc = new GC(disabled);
    gc.setBackground(color);
    gc.fillRectangle(rect);
    int hDC = gc.handle;
    int hImage = image.handle;
    int fuFlags = OS.DSS_DISABLED;
    switch(image.type) {
      case SWT.BITMAP:
        fuFlags |= OS.DST_BITMAP;
        break;
      case SWT.ICON:
        fuFlags |= OS.DST_ICON;
        break;
    }
    OS.DrawState(hDC, 0, 0, hImage, 0, 0, 0, rect.width, rect.height, fuFlags);
    gc.dispose();
    return disabled;
  }

  /**
   * Returns a rectangle describing the receiver's size and location
   * relative to its parent.
   *
   * @return the receiver's bounding rectangle
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Rectangle getBounds() {
    checkWidget();
    Container handle = getHandle();
    java.awt.Rectangle bounds = handle.getBounds();
    return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
//    int hwnd = parent.handle;
//    int index = OS.SendMessage(hwnd, OS.TB_COMMANDTOINDEX, id, 0);
//    RECT rect = new RECT();
//    OS.SendMessage(hwnd, OS.TB_GETITEMRECT, index, rect);
//    int width = rect.right - rect.left;
//    int height = rect.bottom - rect.top;
//    return new Rectangle(rect.left, rect.top, width, height);
  }

  /**
   * Returns the control that is used to fill the bounds of
   * the item when the items is a <code>SEPARATOR</code>.
   *
   * @return the control
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Control getControl() {
    checkWidget();
    return control;
  }

  /**
   * Returns the receiver's disabled image if it has one, or null
   * if it does not.
   * <p>
   * The disabled image is displayed when the receiver is disabled.
   * </p>
   *
   * @return the receiver's disabled image
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Image getDisabledImage() {
    checkWidget();
    return disabledImage;
  }

  public Display getDisplay() {
    ToolBar parent = this.parent;
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
//    int hwnd = parent.handle;
//    int fsState = OS.SendMessage(hwnd, OS.TB_GETSTATE, id, 0);
//    return(fsState & OS.TBSTATE_ENABLED) != 0;
  }

  /**
   * Returns the receiver's hot image if it has one, or null
   * if it does not.
   * <p>
   * The hot image is displayed when the mouse enters the receiver.
   * </p>
   *
   * @return the receiver's hot image
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Image getHotImage() {
    checkWidget();
    return hotImage;
  }

  /**
   * Returns the receiver's parent, which must be a <code>ToolBar</code>.
   *
   * @return the receiver's parent
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public ToolBar getParent() {
    checkWidget();
    return parent;
  }

  /**
   * Returns <code>true</code> if the receiver is selected,
   * and false otherwise.
   * <p>
   * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
   * it is selected when it is checked (which some platforms draw as a
   * pushed in button). If the receiver is of any other type, this method
   * returns false.
   * </p>
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
//    int hwnd = parent.handle;
//    int fsState = OS.SendMessage(hwnd, OS.TB_GETSTATE, id, 0);
//    return(fsState & OS.TBSTATE_CHECKED) != 0;
  }

  /**
   * Returns the receiver's tool tip text, or null if it has not been set.
   *
   * @return the receiver's tool tip text
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public String getToolTipText() {
    checkWidget();
    return toolTipText;
  }

  /**
   * Gets the width of the receiver.
   *
   * @return the width
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getWidth() {
    checkWidget();
    int hwnd = parent.handle;
    int index = OS.SendMessage(hwnd, OS.TB_COMMANDTOINDEX, id, 0);
    RECT rect = new RECT();
    OS.SendMessage(hwnd, OS.TB_GETITEMRECT, index, rect);
    return rect.right - rect.left;
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
    checkWidget();
    return getEnabled() && parent.isEnabled();
  }

  void releaseChild() {
    super.releaseChild();
    parent.destroyItem(this);
  }

  void releaseWidget() {
    super.releaseWidget();
    parent = null;
    control = null;
    toolTipText = null;
    disabledImage = hotImage = null;
    if(disabledImage2 != null) {
      disabledImage2.dispose();
    }
    disabledImage2 = null;
  }

  void releaseImages() {
    TBBUTTONINFO info = new TBBUTTONINFO();
    info.cbSize = TBBUTTONINFO.sizeof;
    info.dwMask = OS.TBIF_IMAGE | OS.TBIF_STYLE;
    int hwnd = parent.handle;
    OS.SendMessage(hwnd, OS.TB_GETBUTTONINFO, id, info);
    /*
     * Feature in Windows.  For some reason, a tool item that has
     * the style BTNS_SEP does not return I_IMAGENONE when queried
     * for an image index, despite the fact that no attempt has been
     * made to assign an image to the item.  As a result, operations
     * on an image list that use the wrong index cause random results.
     * The fix is to ensure that the tool item is not a separator
     * before using the image index.  Since separators cannot have
     * an image and one is never assigned, this is not a problem.
     */
    if((info.fsStyle & OS.BTNS_SEP) == 0 && info.iImage != OS.I_IMAGENONE) {
      ImageList imageList = parent.getImageList();
      ImageList hotImageList = parent.getHotImageList();
      ImageList disabledImageList = parent.getDisabledImageList();
      if(imageList != null) {
        imageList.put(info.iImage, null);
      }
      if(hotImageList != null) {
        hotImageList.put(info.iImage, null);
      }
      if(disabledImageList != null) {
        disabledImageList.put(info.iImage, null);
      }
    }
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

  void resizeControl() {
    if(control != null && !control.isDisposed()) {
      /*
       * Set the size and location of the control
       * separately to minimize flashing in the
       * case where the control does not resize
       * to the size that was requested.  This
       * case can occur when the control is a
       * combo box.
       */
      Rectangle itemRect = getBounds();
      control.setSize(itemRect.width, itemRect.height);
      Rectangle rect = control.getBounds();
      rect.x = itemRect.x + (itemRect.width - rect.width) / 2;
      rect.y = itemRect.y + (itemRect.height - rect.height) / 2;
      control.setLocation(rect.x, rect.y);
    }
  }

  void selectRadio() {
    int index = 0;
    ToolItem[] items = parent.getItems();
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
   * Sets the control that is used to fill the bounds of
   * the item when the items is a <code>SEPARATOR</code>.
   *
   * @param control the new control
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
   *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setControl(Control control) {
    checkWidget();
    if(control != null) {
      if(control.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
      }
      if(control.parent != parent) {
        error(SWT.ERROR_INVALID_PARENT);
      }
    }
    if((style & SWT.SEPARATOR) == 0) {
      return;
    }
    if(this.control != null) {
      getHandle().remove(this.control.getHandle());
    }
    this.control = control;
    getHandle().add(control.getHandle());
    getHandle().setSize(new java.awt.Dimension(200, 200));
    // TODO: find how to have the separator with the appropriate size.
    resizeControl();
  }

  /**
   * Enables the receiver if the argument is <code>true</code>,
   * and disables it otherwise.
   * <p>
   * A disabled control is typically
   * not selectable from the user interface and draws with an
   * inactive or "grayed" look.
   * </p>
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
//    int hwnd = parent.handle;
//    int fsState = OS.SendMessage(hwnd, OS.TB_GETSTATE, id, 0);
//    fsState &= ~OS.TBSTATE_ENABLED;
//    if(enabled) {
//      fsState |= OS.TBSTATE_ENABLED;
//    }
//    OS.SendMessage(hwnd, OS.TB_SETSTATE, id, fsState);
//    if(image != null) {
//      updateImages();
//    }
  }

  /**
   * Sets the receiver's disabled image to the argument, which may be
   * null indicating that no disabled image should be displayed.
   * <p>
   * The disbled image is displayed when the receiver is disabled.
   * </p>
   *
   * @param image the disabled image to display on the receiver (may be null)
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setDisabledImage(Image image) {
    checkWidget();
    if((style & SWT.SEPARATOR) != 0) {
      return;
    }
    if(image != null && image.isDisposed()) {
      error(SWT.ERROR_INVALID_ARGUMENT);
    }
    disabledImage = image;
    updateImages();
  }

  /**
   * Sets the receiver's hot image to the argument, which may be
   * null indicating that no hot image should be displayed.
   * <p>
   * The hot image is displayed when the mouse enters the receiver.
   * </p>
   *
   * @param image the hot image to display on the receiver (may be null)
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setHotImage(Image image) {
    checkWidget();
    if((style & SWT.SEPARATOR) != 0) {
      return;
    }
    if(image != null && image.isDisposed()) {
      error(SWT.ERROR_INVALID_ARGUMENT);
    }
    hotImage = image;
    updateImages();
  }

  public void setImage(Image image) {
    checkWidget();
    if((style & SWT.SEPARATOR) != 0) {
      return;
    }
    if(image != null && image.isDisposed()) {
      error(SWT.ERROR_INVALID_ARGUMENT);
    }
    super.setImage(image);
    if((style & SWT.DROP_DOWN) != 0) {
      ((CComboButton)getHandle()).setIcon(new ImageIcon(image.swingHandle));
    } else {
      ((AbstractButton)getHandle()).setIcon(new ImageIcon(image.swingHandle));
    }
    // TODO: actually do the commented code?
//    updateImages();
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
   * it is selected when it is checked (which some platforms draw as a
   * pushed in button).
   * </p>
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
    AbstractButton button = (AbstractButton)getHandle();
    if(button.isSelected() ^ selected) {
      button.setSelected(selected);
    }
//    int hwnd = parent.handle;
//    int fsState = OS.SendMessage(hwnd, OS.TB_GETSTATE, id, 0);
//    fsState &= ~OS.TBSTATE_CHECKED;
//    if(selected) {
//      fsState |= OS.TBSTATE_CHECKED;
//    }
//    OS.SendMessage(hwnd, OS.TB_SETSTATE, id, fsState);
  }

  /**
   * Sets the receiver's text. The string may include
   * the mnemonic character.
   * </p>
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
   */
  public void setText(String string) {
    checkWidget();
    if(string == null) {
      error(SWT.ERROR_NULL_ARGUMENT);
    }
    if((style & SWT.SEPARATOR) != 0) {
      return;
    }
    super.setText(string);
    if((style & (SWT.PUSH | SWT.CHECK | SWT.RADIO)) != 0) {
      AbstractButton button = (AbstractButton)getHandle();
      int index = findMnemonicIndex(string);
      if(index != -1) {
        button.setMnemonic(string.charAt(index));
        string = string.substring(0, index - 1) + string.substring(index);
      }
      button.setText(string);
    } else if((style & SWT.DROP_DOWN) != 0) {
      JButton pushButton = ((CComboButton)getHandle()).getPushButton();
      pushButton.setText(string);
      pushButton.setSize(pushButton.getPreferredSize());
      pushButton.revalidate();
    }
    
//    int hwnd = parent.handle;
//    int hHeap = OS.GetProcessHeap();
//    TCHAR buffer = new TCHAR(parent.getCodePage(), string, true);
//    int byteCount = buffer.length() * TCHAR.sizeof;
//    int pszText = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//    OS.MoveMemory(pszText, buffer, byteCount);
//    TBBUTTONINFO info = new TBBUTTONINFO();
//    info.cbSize = TBBUTTONINFO.sizeof;
//    info.dwMask = OS.TBIF_TEXT | OS.TBIF_STYLE;
//    info.pszText = pszText;
//    info.fsStyle = (byte)(widgetStyle() | OS.BTNS_AUTOSIZE);
//    OS.SendMessage(hwnd, OS.TB_SETBUTTONINFO, id, info);
//    OS.HeapFree(hHeap, 0, pszText);
//
//    /*
//     * Bug in Windows.  For some reason, when the font is set
//     * before any tool item has text, the tool items resize to
//     * a very small size.  Also, a tool item will only show text
//     * when text has already been set on one item and then a new
//     * item is created.  The fix is to use WM_SETFONT to force
//     * the tool bar to redraw and layout.  [1G0G7TV, 1G0FUJ5]
//     */
//    int hFont = OS.SendMessage(hwnd, OS.WM_GETFONT, 0, 0);
//    OS.SendMessage(hwnd, OS.WM_SETFONT, hFont, 0);

    parent.layoutItems();
  }

  /**
   * Sets the receiver's tool tip text to the argument, which
   * may be null indicating that no tool tip text should be shown.
   *
   * @param string the new tool tip text (or null)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setToolTipText(String string) {
    checkWidget();
    toolTipText = string;
    Container handle = getHandle();
    if(handle instanceof JComponent) {
      ((JComponent)handle).setToolTipText(string == null? null: "<html>" + Utils.escapeHTML(string));
    }
  }

  /**
   * Sets the width of the receiver.
   *
   * @param width the new width
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setWidth(int width) {
    checkWidget();
    if((style & SWT.SEPARATOR) == 0) {
      return;
    }
    if(width < 0) {
      return;
    }
    Container handle = getHandle();
    handle.setSize(width, handle.getHeight());
//    int hwnd = parent.handle;
//    TBBUTTONINFO info = new TBBUTTONINFO();
//    info.cbSize = TBBUTTONINFO.sizeof;
//    info.dwMask = OS.TBIF_SIZE;
//    info.cx = (short)width;
//    OS.SendMessage(hwnd, OS.TB_SETBUTTONINFO, id, info);
    parent.layoutItems();
  }

  void updateImages() {
    int hwnd = parent.handle;
    TBBUTTONINFO info = new TBBUTTONINFO();
    info.cbSize = TBBUTTONINFO.sizeof;
    info.dwMask = OS.TBIF_IMAGE;
    OS.SendMessage(hwnd, OS.TB_GETBUTTONINFO, id, info);
    if(info.iImage == OS.I_IMAGENONE && image == null) {
      return;
    }
    ImageList imageList = parent.getImageList();
    ImageList hotImageList = parent.getHotImageList();
    ImageList disabledImageList = parent.getDisabledImageList();
    if(info.iImage == OS.I_IMAGENONE) {
      Display display = getDisplay();
      Rectangle bounds = image.getBounds();
      Point size = new Point(bounds.width, bounds.height);
      if(imageList == null) {
        imageList = display.getToolImageList(size);
      }
      info.iImage = imageList.add(image);
      parent.setImageList(imageList);
      if(disabledImageList == null) {
        disabledImageList = display.getToolDisabledImageList(size);
      }
      Image disabled = disabledImage;
      if(disabledImage == null) {
        if(disabledImage2 != null) {
          disabledImage2.dispose();
        }
        disabledImage2 = null;
        disabled = image;
        if(!getEnabled()) {
          Color color = parent.getBackground();
          disabled = disabledImage2 = createDisabledImage(image, color);
        }
      }
      disabledImageList.add(disabled);
      parent.setDisabledImageList(disabledImageList);
//		if ((parent.style & SWT.FLAT) != 0) {
      if(hotImageList == null) {
        hotImageList = display.getToolHotImageList(size);
      }
      hotImageList.add(hotImage != null ? hotImage : image);
      parent.setHotImageList(hotImageList);
//		}
    } else {
      if(imageList != null) {
        imageList.put(info.iImage, image);
      }
      if(disabledImageList != null) {
        Image disabled = null;
        if(image != null) {
          if(disabledImage2 != null) {
            disabledImage2.dispose();
          }
          disabledImage2 = null;
          disabled = disabledImage;
          if(disabledImage == null) {
            disabled = image;
            if(!getEnabled()) {
              Color color = parent.getBackground();
              disabled = disabledImage2 = createDisabledImage(image, color);
            }
          }
        }
        disabledImageList.put(info.iImage, disabled);
      }
      if(hotImageList != null) {
        Image hot = null;
        if(image != null) {
          hot = hotImage != null ? hotImage : image;
            }
        hotImageList.put(info.iImage, hot);
      }
      if(image == null) {
        info.iImage = OS.I_IMAGENONE;
      }
    }
    OS.SendMessage(hwnd, OS.TB_SETBUTTONINFO, id, info);

    parent.layoutItems();
  }

  int widgetStyle() {
    if((style & SWT.DROP_DOWN) != 0) {
      return OS.BTNS_DROPDOWN;
    }
    if((style & SWT.PUSH) != 0) {
      return OS.BTNS_BUTTON;
    }
    if((style & SWT.CHECK) != 0) {
      return OS.BTNS_CHECK;
    }
    /*
     * This code is intentionally commented.  In order to
     * consistently support radio tool items across platforms,
     * the platform radio behavior is not used.
     */
//	if ((style & SWT.RADIO) != 0) return OS.BTNS_CHECKGROUP;
    if((style & SWT.RADIO) != 0) {
      return OS.BTNS_CHECK;
    }
    if((style & SWT.SEPARATOR) != 0) {
      return OS.BTNS_SEP;
    }
    return OS.BTNS_BUTTON;
  }

  LRESULT wmCommandChild(int wParam, int lParam) {
    if((style & SWT.RADIO) != 0) {
      if((parent.getStyle() & SWT.NO_RADIO_GROUP) == 0) {
        selectRadio();
      }
    }
    Event event = new Event();
    setInputState(event, SWT.Selection);
    postEvent(SWT.Selection, event);
    return null;
  }

}
