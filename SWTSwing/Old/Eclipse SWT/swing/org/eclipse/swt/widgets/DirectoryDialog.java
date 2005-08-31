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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.swing.PopEventQueue;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select a directory.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public class DirectoryDialog extends Dialog {
  String message = "", filterPath = ""; //$NON-NLS-1$//$NON-NLS-2$
  String directoryPath;

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
  public DirectoryDialog(Shell parent) {
    this(parent, SWT.PRIMARY_MODAL);
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
  public DirectoryDialog(Shell parent, int style) {
    super(parent, style);
    checkSubclass();
  }

  int BrowseCallbackProc(int hwnd, int uMsg, int lParam, int lpData) {
    switch(uMsg) {
      case OS.BFFM_INITIALIZED:
        if(filterPath != null && filterPath.length() != 0) {
          /* Use the character encoding for the default locale */
          TCHAR buffer = new TCHAR(0, filterPath.replace('/', '\\'), true);
          OS.SendMessage(hwnd, OS.BFFM_SETSELECTION, 1, buffer);
        }
        if(title != null && title.length() != 0) {
          /* Use the character encoding for the default locale */
          TCHAR buffer = new TCHAR(0, title, true);
          OS.SetWindowText(hwnd, buffer);
        }
        break;
      case OS.BFFM_VALIDATEFAILEDA:
      case OS.BFFM_VALIDATEFAILEDW:

        /* Use the character encoding for the default locale */
        TCHAR buffer = new TCHAR(0, 256);
        int byteCount = buffer.length() * TCHAR.sizeof;
        OS.MoveMemory(buffer, lParam, byteCount);
        directoryPath = buffer.toString(0, buffer.strlen());
        break;
    }
    return 0;
  }

  /**
   * Returns the path which the dialog will use to filter
   * the directories it shows.
   *
   * @return the filter path
   */
  public String getFilterPath() {
    return filterPath;
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
   * @return a string describing the absolute path of the selected directory,
   *         or null if the dialog was cancelled or an error occurred
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
   * </ul>
   */
  public String open() {
    final JFileChooser fileChooser = new JFileChooser();
    String fullPath = null;
    // TODO: file filters
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    boolean isEventThread = SwingUtilities.isEventDispatchThread();
    PopEventQueue eq = null;
    if(!isEventThread) {
      eq = new PopEventQueue();
      Display.swingEventQueue.push(eq);
    }
    int returnValue = 0;
    if((style & SWT.SAVE) != 0) {
      returnValue = fileChooser.showSaveDialog(getParent().getHandle());
    } else {
      returnValue = fileChooser.showOpenDialog(getParent().getHandle());
    }
    if(!isEventThread) {
      eq.pop();
    }
    if(returnValue == JFileChooser.APPROVE_OPTION) {
      File directory = fileChooser.getSelectedFile();
      fullPath = directory.getAbsolutePath();
      filterPath = new String(fullPath);
    }
    return fullPath;
//
//    
//    if(OS.IsWinCE) {
//      SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//
//      /* Initialize OLE */
//    }
//    OS.OleInitialize(0);
//
//    int hHeap = OS.GetProcessHeap();
//
//    /* Get the owner HWND for the dialog */
//    int hwndOwner = 0;
//    if(parent != null) {
//      hwndOwner = parent.handle;
//
//      /* Copy the message to OS memory */
//    }
//    int lpszTitle = 0;
//    if(message != null && message.length() != 0) {
//      /* Use the character encoding for the default locale */
//      TCHAR buffer = new TCHAR(0, message, true);
//      int byteCount = buffer.length() * TCHAR.sizeof;
//      lpszTitle = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//      OS.MoveMemory(lpszTitle, buffer, byteCount);
//    }
//
//    /* Create the BrowseCallbackProc */
//    Callback callback = new Callback(this, "BrowseCallbackProc", 4); //$NON-NLS-1$
//    int address = callback.getAddress();
//
//    /* Open the dialog */
//    directoryPath = null;
//    BROWSEINFO lpbi = new BROWSEINFO();
//    lpbi.hwndOwner = hwndOwner;
//    lpbi.lpszTitle = lpszTitle;
//    lpbi.ulFlags = OS.BIF_NEWDIALOGSTYLE | OS.BIF_RETURNONLYFSDIRS |
//        OS.BIF_EDITBOX | OS.BIF_VALIDATE;
//    lpbi.lpfn = address;
//    int lpItemIdList = OS.SHBrowseForFolder(lpbi);
//    boolean success = lpItemIdList != 0;
//    if(success) {
//      /* Use the character encoding for the default locale */
//      TCHAR buffer = new TCHAR(0, 256);
//      if(OS.SHGetPathFromIDList(lpItemIdList, buffer)) {
//        directoryPath = buffer.toString(0, buffer.strlen());
//        filterPath = directoryPath;
//      }
//    }
//
//    /* Free the BrowseCallbackProc */
//    callback.dispose();
//
//    /* Free the OS memory */
//    if(lpszTitle != 0) {
//      OS.HeapFree(hHeap, 0, lpszTitle);
//
//      /* Free the pointer to the ITEMIDLIST */
//    }
//    int[] ppMalloc = new int[1];
//    if(OS.SHGetMalloc(ppMalloc) == OS.S_OK) {
//      /* void Free (struct IMalloc *this, void *pv); */
//      OS.VtblCall(5, ppMalloc[0], lpItemIdList);
//    }
//
//    /* Uninitialize OLE */
//    OS.OleUninitialize();
//
//    /*
//     * This code is intentionally commented.  On some
//     * platforms, the owner window is repainted right
//     * away when a dialog window exits.  This behavior
//     * is currently unspecified.
//     */
////	if (hwndOwner != 0) OS.UpdateWindow (hwndOwner);
//
//    /* Return the directory path */
//    if(!success) {
//      return null;
//    }
//    return directoryPath;
  }

  /**
   * Sets the path which the dialog will use to filter
   * the directories it shows to the argument, which may be
   * null.
   *
   * @param string the filter path
   */
  public void setFilterPath(String string) {
    filterPath = string;
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
