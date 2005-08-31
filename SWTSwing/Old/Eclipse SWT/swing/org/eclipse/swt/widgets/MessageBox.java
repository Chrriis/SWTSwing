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

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.swing.PopEventQueue;

/**
 * Instances of this class are used used to inform or warn the user.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING, ICON_WORKING</dd>
 * <dd>OK, OK | CANCEL</dd>
 * <dd>YES | NO, YES | NO | CANCEL</dd>
 * <dd>RETRY | CANCEL</dd>
 * <dd>ABORT | RETRY | IGNORE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ICON_ERROR, ICON_INFORMATION, ICON_QUESTION,
 * ICON_WARNING and ICON_WORKING may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class MessageBox extends Dialog {
  String message = "";

  /**
   * Constructs a new instance of this class given only its
   * parent.
   * <p>
   * Note: Currently, null can be passed in for the parent.
   * This has the effect of creating the dialog on the currently active
   * display if there is one. If there is no current display, the
   * dialog is created on a "default" display. <b>Passing in null as
   * the parent is not considered to be good coding style,
   * and may not be supported in a future release of SWT.</b>
   * </p>
   *
   * @param parent a shell which will be the parent of the new instance
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
       *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
   * </ul>
   */
  public MessageBox(Shell parent) {
    this(parent, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
  }

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
   * Note: Currently, null can be passed in for the parent.
   * This has the effect of creating the dialog on the currently active
   * display if there is one. If there is no current display, the
   * dialog is created on a "default" display. <b>Passing in null as
   * the parent is not considered to be good coding style,
   * and may not be supported in a future release of SWT.</b>
   * </p>
   *
   * @param parent a shell which will be the parent of the new instance
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
       *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
   * </ul>
   */
  public MessageBox(Shell parent, int style) {
    super(parent, checkStyle(style));
    checkSubclass();
  }

  static int checkStyle(int style) {
    if((style & (SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) ==
       0) {
      style |= SWT.APPLICATION_MODAL;
    }
    int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY |
                SWT.IGNORE);
    int bits = style & mask;
    if(bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) {
      return style;
    }
    if(bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) ||
       bits == (SWT.YES | SWT.NO | SWT.CANCEL)) {
      return style;
    }
    if(bits == (SWT.RETRY | SWT.CANCEL) ||
       bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
      return style;
    }
    style = (style & ~mask) | SWT.OK;
    return style;
  }

  /**
   * Returns the dialog's message, which is a description of
   * the purpose for which it was opened. This message will be
   * visible on the dialog while it is open.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Makes the dialog visible and brings it to the front
   * of the display.
   *
   * @return the ID of the button that was selected to dismiss the
   *         message box (e.g. SWT.OK, SWT.CANCEL, etc...)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
   * </ul>
   */
  public int open() {

    int messageType = JOptionPane.PLAIN_MESSAGE;
    if((style & SWT.ICON_ERROR) != 0) {
      messageType = JOptionPane.ERROR_MESSAGE;
    }
    if((style & SWT.ICON_INFORMATION) != 0) {
      messageType = JOptionPane.INFORMATION_MESSAGE;
    }
    if((style & SWT.ICON_QUESTION) != 0) {
      messageType = JOptionPane.QUESTION_MESSAGE;
    }
    if((style & SWT.ICON_WARNING) != 0) {
      messageType = JOptionPane.WARNING_MESSAGE;
    }
    if((style & SWT.ICON_WORKING) != 0) {
      messageType = JOptionPane.INFORMATION_MESSAGE;
    }

    // TODO: create own dialog? to respect modality...
    // if((style & (SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) !=
    // TODO: respect alignment
//    int bits = buttonBits | iconBits | modalBits;
//    if((style & SWT.RIGHT_TO_LEFT) != 0) {
//      bits |= OS.MB_RTLREADING;
//    }
//    if(parent != null && (parent.style & SWT.RIGHT_TO_LEFT) != 0) {
//      bits |= OS.MB_RTLREADING;
//    }

    boolean isEventThread = SwingUtilities.isEventDispatchThread();
    PopEventQueue eq = null;
    if(!isEventThread) {
      eq = new PopEventQueue();
      Display.swingEventQueue.push(eq);
    }
    int value = SWT.CANCEL;
    if((style & SWT.OK) == SWT.OK) {
      int result = JOptionPane.showOptionDialog(getParent().getHandle(), message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"OK"}, null);
      if(result == JOptionPane.CLOSED_OPTION) {
        value = SWT.CANCEL; 
      }
      value = SWT.OK;
    } else if((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) {
      int result = JOptionPane.showOptionDialog(getParent().getHandle(), message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"OK", "Cancel"}, null);
      if(result == JOptionPane.CLOSED_OPTION) {
        value = SWT.CANCEL; 
      }
      value = result == 0? SWT.OK: SWT.CANCEL;
    } else if((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
      int result = JOptionPane.showOptionDialog(getParent().getHandle(), message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"Yes", "No"}, null);
      if(result == JOptionPane.CLOSED_OPTION) {
        value = SWT.CANCEL; 
      }
      value = result == 0? SWT.YES: SWT.NO;
    } else if((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) {
      int result = JOptionPane.showOptionDialog(getParent().getHandle(), message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"Yes", "No", "Cancel"}, null);
      if(result == JOptionPane.CLOSED_OPTION) {
        value = SWT.CANCEL; 
      }
      value = result == 0? SWT.YES: result == 1? SWT.NO: SWT.CANCEL;
    } else if((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
      int result = JOptionPane.showOptionDialog(getParent().getHandle(), message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"Retry", "Cancel"}, null);
      if(result == JOptionPane.CLOSED_OPTION) {
        value = SWT.CANCEL; 
      }
      value = result == 0? SWT.RETRY: SWT.CANCEL;
    } else if((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
      int result = JOptionPane.showOptionDialog(getParent().getHandle(), message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"Abort", "Retry", "Ignore"}, null);
      if(result == JOptionPane.CLOSED_OPTION) {
        value = SWT.CANCEL; 
      }
      value = result == 0? SWT.ABORT: result == 1? SWT.RETRY: SWT.IGNORE;
    } else {
      int result = JOptionPane.showOptionDialog(getParent().getHandle(), message, title, JOptionPane.DEFAULT_OPTION, messageType, null, new Object[] {"OK"}, null);
      if(result == JOptionPane.CLOSED_OPTION) {
        value = SWT.CANCEL; 
      }
      value = SWT.OK;
    }
    if(!isEventThread) {
      eq.pop();
    }
    return value;
  }

  /**
   * Sets the dialog's message, which is a description of
   * the purpose for which it was opened. This message will be
   * visible on the dialog while it is open.
   *
   * @param string the message
   */
  public void setMessage(String string) {
    message = string;
  }

}
