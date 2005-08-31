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

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.Container;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class provide an etched border
 * with an optional title.
 * <p>
 * Shadow styles are hints and may not be honoured
 * by the platform.  To create a group with the
 * default shadow style for the platform, do not
 * specify a shadow style.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class Group extends Composite {
  /**
   * Constructs a new instance of this class given its parent
   * and a style value describing its behavior and appearance.
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
   * @see SWT#SHADOW_ETCHED_IN
   * @see SWT#SHADOW_ETCHED_OUT
   * @see SWT#SHADOW_IN
   * @see SWT#SHADOW_OUT
   * @see SWT#SHADOW_NONE
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public Group(Composite parent, int style) {
    super(parent, checkStyle(style));
  }

  static int checkStyle(int style) {
    style |= SWT.NO_FOCUS;
    /*
     * Even though it is legal to create this widget
     * with scroll bars, they serve no useful purpose
     * because they do not automatically scroll the
     * widget's client area.  The fix is to clear
     * the SWT style.
     */
    return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
  }

  protected void checkSubclass() {
    if(!isValidSubclass()) {
      error(SWT.ERROR_INVALID_SUBCLASS);
    }
  }

  void createHandle() {
    super.createHandle();
    state &= ~CANVAS;
  }

//  public Rectangle getClientArea() {
//    checkWidget();
//    forceResize();
//    RECT rect = new RECT();
//    OS.GetClientRect(handle, rect);
//    int newFont, oldFont = 0;
//    int hDC = OS.GetDC(handle);
//    newFont = OS.SendMessage(handle, OS.WM_GETFONT, 0, 0);
//    if(newFont != 0) {
//      oldFont = OS.SelectObject(hDC, newFont);
//    }
//    TEXTMETRIC tm = new TEXTMETRIC();
//    OS.GetTextMetrics(hDC, tm);
//    if(newFont != 0) {
//      OS.SelectObject(hDC, oldFont);
//    }
//    OS.ReleaseDC(handle, hDC);
//    int inset = 3, x = inset, y = tm.tmHeight;
//    return new Rectangle(x, y, rect.right - (inset * 2),
//                         rect.bottom - y - inset);
//  }
//
  String getNameText() {
    return getText();
  }

  /**
   * Returns the receiver's text, which is the string that the
   * is used as the <em>title</em>. If the text has not previously
   * been set, returns an empty string.
   *
   * @return the text
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public String getText() {
    checkWidget();
    return border.getTitle();
  }

//  boolean mnemonicHit(char key) {
//    return setFocus();
//  }
//
//  boolean mnemonicMatch(char key) {
//    char mnemonic = findMnemonic(getText());
//    if(mnemonic == '\0') {
//      return false;
//    }
//    return Character.toUpperCase(key) == Character.toUpperCase(mnemonic);
//  }

  /**
   * Sets the receiver's text, which is the string that will
   * be displayed as the receiver's <em>title</em>, to the argument,
   * which may not be null. The string may include the mnemonic character.
   * </p>
   * Mnemonics are indicated by an '&amp' that causes the next
   * character to be the mnemonic.  When the user presses a
   * key sequence that matches the mnemonic, focus is assgned
   * to the first child of the group. On most platforms, the
   * mnemonic appears underlined but may be emphasised in a
   * platform specific manner.  The mnemonic indicator character
   *'&amp' can be escaped by doubling it in the string, causing
   * a single '&amp' to be displayed.
   * </p>
   * @param text the new text
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setText(String string) {
    checkWidget();
    int index = findMnemonicIndex(string);
    if(index != -1) {
      // TODO: find a way to capture the mnemonic.
      string = string.substring(0, index - 1) + string.substring(index);
    }
    border.setTitle(string);
  }

  TitledBorder border;
  JPanel subPanel;

  Container getNewHandle() {
    JPanel panel = new JPanel(new BorderLayout(0, 0));
    subPanel = new JPanel();
    panel.add(subPanel, BorderLayout.CENTER);
    border = BorderFactory.createTitledBorder("");
    panel.setBorder(border);
    setContentPane(subPanel);
    return panel;
  }

  LRESULT WM_ERASEBKGND(int wParam, int lParam) {
    LRESULT result = super.WM_ERASEBKGND(wParam, lParam);
    if(result != null) {
      return result;
    }
    /*
     * Feaure in Windows.  Group boxes do not erase
     * the background before drawing.  The fix is to
     * fill the background.
     */
    drawBackground(wParam);
    return LRESULT.ONE;
  }

  LRESULT WM_NCHITTEST(int wParam, int lParam) {
    LRESULT result = super.WM_NCHITTEST(wParam, lParam);
    if(result != null) {
      return result;
    }
    /*
     * Feature in Windows.  The window proc for the group box
     * returns HTTRANSPARENT indicating that mouse messages
     * should not be delivered to the receiver and any children.
     * Normally, group boxes in Windows do not have children and
     * this is the correct behavior for this case.  Because we
     * allow children, answer HTCLIENT to allow mouse messages
     * to be delivered to the children.
     */
    int code = callWindowProc(OS.WM_NCHITTEST, wParam, lParam);
    if(code == OS.HTTRANSPARENT) {
      code = OS.HTCLIENT;
    }
    return new LRESULT(code);
  }

  LRESULT WM_MOUSEMOVE(int wParam, int lParam) {
    LRESULT result = super.WM_MOUSEMOVE(wParam, lParam);
    if(result != null) {
      return result;
    }
    /*
     * Feature in Windows.  In version 6.00 of COMCTL32.DLL,
     * every time the mouse moves, the group title redraws.
     * This only happens when WM_NCHITTEST returns HTCLIENT.
     * The fix is to avoid calling the group window proc.
     */
    return LRESULT.ZERO;
  }

  LRESULT WM_PRINTCLIENT(int wParam, int lParam) {
    LRESULT result = super.WM_PRINTCLIENT(wParam, lParam);
    if(result != null) {
      return result;
    }
    /*
     * Feature in Windows.  In version 6.00 of COMCTL32.DLL,
     * when WM_PRINTCLIENT is sent from a child BS_GROUP
     * control to a parent BS_GROUP, the parent BS_GROUP
     * clears the font from the HDC.  Normally, group boxes
     * in Windows do not have children so this behavior is
     * undefined.  When the parent of a BS_GROUP is not a
     * BS_GROUP, there is no problem.  The fix is to save
     * and restore the current font.
     */
    return result;
  }

  LRESULT WM_SIZE(int wParam, int lParam) {
    LRESULT result = super.WM_SIZE(wParam, lParam);
    if(OS.IsWinCE) {
      return result;
    }
    OS.InvalidateRect(handle, null, true);
    return result;
  }

}
