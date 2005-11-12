/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//import org.eclipse.swt.internal.*;
//import org.eclipse.swt.internal.swing.CEventQueue;
import org.eclipse.swt.internal.swing.Utils;
//import org.eclipse.swt.internal.win32.OS;
//import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are responsible for managing the
 * connection between SWT and the underlying operating
 * system. Their most important function is to implement
 * the SWT event loop in terms of the platform event model.
 * They also provide various methods for accessing information
 * about the operating system, and have overall control over
 * the operating system resources which SWT allocates.
 * <p>
 * Applications which are built with SWT will <em>almost always</em>
 * require only a single display. In particular, some platforms
 * which SWT supports will not allow more than one <em>active</em>
 * display. In other words, some platforms do not support
 * creating a new display if one already exists that has not been
 * sent the <code>dispose()</code> message.
 * <p>
 * In SWT, the thread which creates a <code>Display</code>
 * instance is distinguished as the <em>user-interface thread</em>
 * for that display.
 * </p>
 * The user-interface thread for a particular display has the
 * following special attributes:
 * <ul>
 * <li>
 * The event loop for that display must be run from the thread.
 * </li>
 * <li>
 * Some SWT API methods (notably, most of the public methods in
 * <code>Widget</code> and its subclasses), may only be called
 * from the thread. (To support multi-threaded user-interface
 * applications, class <code>Display</code> provides inter-thread
 * communication methods which allow threads other than the 
 * user-interface thread to request that it perform operations
 * on their behalf.)
 * </li>
 * <li>
 * The thread is not allowed to construct other 
 * <code>Display</code>s until that display has been disposed.
 * (Note that, this is in addition to the restriction mentioned
 * above concerning platform support for multiple displays. Thus,
 * the only way to have multiple simultaneously active displays,
 * even on platforms which support it, is to have multiple threads.)
 * </li>
 * </ul>
 * Enforcing these attributes allows SWT to be implemented directly
 * on the underlying operating system's event model. This has 
 * numerous benefits including smaller footprint, better use of 
 * resources, safer memory management, clearer program logic,
 * better performance, and fewer overall operating system threads
 * required. The down side however, is that care must be taken
 * (only) when constructing multi-threaded applications to use the
 * inter-thread communication mechanisms which this class provides
 * when required.
 * </p><p>
 * All SWT API methods which may only be called from the user-interface
 * thread are distinguished in their documentation by indicating that
 * they throw the "<code>ERROR_THREAD_INVALID_ACCESS</code>"
 * SWT exception.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Close, Dispose</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * @see #syncExec
 * @see #asyncExec
 * @see #wake
 * @see #readAndDispatch
 * @see #sleep
 * @see Device#dispose
 */

public class Display extends Device {

//	/**
//	 * the handle to the OS message queue
//	 * (Warning: This field is platform dependent)
//	 * <p>
//	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
//	 * public API. It is marked public only so that it can be shared
//	 * within the packages provided by SWT. It is not available on all
//	 * platforms and should never be accessed from application code.
//	 * </p>
//	 */
//	public MSG msg = new MSG ();
//
//	/* Windows and Events */
	Event [] eventQueue;
//	Callback windowCallback;
//	int windowProc, threadId;
//	TCHAR windowClass, windowShadowClass;
//	static int WindowClassCount;
//	static final String WindowName = "SWT_Window"; //$NON-NLS-1$
//	static final String WindowShadowName = "SWT_WindowShadow"; //$NON-NLS-1$
	EventTable eventTable, filterTable;
//
//	/* Widget Table */
//	int freeSlot;
//	int [] indexTable;
//	Control [] controlTable;
//	static final int GROW_SIZE = 1024;
//	static final int SWT_OBJECT_INDEX;
//	static final boolean USE_PROPERTY = !OS.IsWinCE;
//	static {
//		if (USE_PROPERTY) {
//			SWT_OBJECT_INDEX = OS.GlobalAddAtom (new TCHAR (0, "SWT_OBJECT_INDEX", true)); //$NON-NLS-1$
//		} else {
//			SWT_OBJECT_INDEX = 0;
//		}
//	}
//	
//	/* Focus */
//	int focusEvent;
//	Control focusControl;
	
	/* Menus */
	Menu [] bars, popups;
	ArrayList menuItemsList;
	
//	/*
//	* The start value for WM_COMMAND id's.
//	* Windows reserves the values 0..100.
//	* 
//	* The SmartPhone SWT resource file reserves
//	* the values 101..107.
//	*/
//	static final int ID_START = 108;
//	
//	/* Filter Hook */
//	Callback msgFilterCallback;
//	int msgFilterProc, filterHook;
//	MSG hookMsg = new MSG ();
//	boolean ignoreMsgFilter;
//	
//	/* Idle Hook */
//	Callback foregroundIdleCallback;
//	int foregroundIdleProc, idleHook;
//	
//	/* Message Hook and Embedding */
//	Callback getMsgCallback, embeddedCallback;
//	int getMsgProc, msgHook, embeddedHwnd, embeddedProc;
//	static final String AWT_WINDOW_CLASS = "SunAwtWindow";

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Thread thread;

//	/* Display Shutdown */
  ArrayList disposeList;
//	Runnable [] disposeList;
	
	/* System Tray */
	Tray tray;
//	int nextTrayId = 0;
	
//	/* Timers */
//	int [] timerIds;
//	Runnable [] timerList;
//	int nextTimerId;
//	
//	/* Keyboard and Mouse State */
//	int lastKey, lastAscii, lastMouse;
//	boolean lastVirtual, lastNull, lastDead;
//	byte [] keyboard = new byte [256];
//	boolean accelKeyHit, mnemonicKeyHit;
//	boolean lockActiveWindow;
//	
//	/* MDI */
//	boolean ignoreRestoreFocus;
//	Control lastHittestControl;
//	int lastHittest;
//	
//	/* Message Only Window */
//	Callback messageCallback;
//	int hwndMessage, messageProc;
//	int [] systemFonts;
	
	/* System Images Cache */
	java.awt.Image errorIcon, infoIcon, questionIcon, warningIcon;

	/* System Cursors Cache */
	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];
//
//	/* ImageList Cache */	
//	ImageList[] imageList, toolImageList, toolHotImageList, toolDisabledImageList;
//
//	/* Custom Colors for ChooseColor */
//	int lpCustColors;

	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;
	
	/* Key Mappings */
	static final int [] [] KeyTable = {
		
		/* Keyboard and Mouse Masks */
		{java.awt.event.KeyEvent.VK_ALT,	SWT.ALT},
		{java.awt.event.KeyEvent.VK_SHIFT,	SWT.SHIFT},
		{java.awt.event.KeyEvent.VK_CONTROL,	SWT.CONTROL},
//		{java.awt.event.KeyEvent.VK_????,	SWT.COMMAND},

		/* NOT CURRENTLY USED */		
//		{OS.VK_LBUTTON, SWT.BUTTON1},
//		{OS.VK_MBUTTON, SWT.BUTTON3},
//		{OS.VK_RBUTTON, SWT.BUTTON2},
		
		/* Non-Numeric Keypad Keys */
		{java.awt.event.KeyEvent.VK_UP,		SWT.ARROW_UP},
		{java.awt.event.KeyEvent.VK_DOWN,	SWT.ARROW_DOWN},
		{java.awt.event.KeyEvent.VK_LEFT,	SWT.ARROW_LEFT},
		{java.awt.event.KeyEvent.VK_RIGHT,	SWT.ARROW_RIGHT},
		{java.awt.event.KeyEvent.VK_PAGE_UP,	SWT.PAGE_UP},
		{java.awt.event.KeyEvent.VK_PAGE_DOWN,	SWT.PAGE_DOWN},
		{java.awt.event.KeyEvent.VK_HOME,	SWT.HOME},
		{java.awt.event.KeyEvent.VK_END,		SWT.END},
		{java.awt.event.KeyEvent.VK_INSERT,	SWT.INSERT},

		/* Virtual and Ascii Keys */
		{java.awt.event.KeyEvent.VK_BACK_SPACE,	SWT.BS},
		{java.awt.event.KeyEvent.VK_ENTER,	SWT.CR},
		{java.awt.event.KeyEvent.VK_DELETE,	SWT.DEL},
		{java.awt.event.KeyEvent.VK_ESCAPE,	SWT.ESC},
		{java.awt.event.KeyEvent.VK_ENTER,	SWT.LF},
		{java.awt.event.KeyEvent.VK_TAB,		SWT.TAB},
	
		/* Functions Keys */
		{java.awt.event.KeyEvent.VK_F1,	SWT.F1},
		{java.awt.event.KeyEvent.VK_F2,	SWT.F2},
		{java.awt.event.KeyEvent.VK_F3,	SWT.F3},
		{java.awt.event.KeyEvent.VK_F4,	SWT.F4},
		{java.awt.event.KeyEvent.VK_F5,	SWT.F5},
		{java.awt.event.KeyEvent.VK_F6,	SWT.F6},
		{java.awt.event.KeyEvent.VK_F7,	SWT.F7},
		{java.awt.event.KeyEvent.VK_F8,	SWT.F8},
		{java.awt.event.KeyEvent.VK_F9,	SWT.F9},
		{java.awt.event.KeyEvent.VK_F10,	SWT.F10},
		{java.awt.event.KeyEvent.VK_F11,	SWT.F11},
		{java.awt.event.KeyEvent.VK_F12,	SWT.F12},
		{java.awt.event.KeyEvent.VK_F13,	SWT.F13},
		{java.awt.event.KeyEvent.VK_F14,	SWT.F14},
		{java.awt.event.KeyEvent.VK_F15,	SWT.F15},
		
		/* Numeric Keypad Keys */
		{java.awt.event.KeyEvent.VK_MULTIPLY,	SWT.KEYPAD_MULTIPLY},
		{java.awt.event.KeyEvent.VK_ADD,			SWT.KEYPAD_ADD},
		{java.awt.event.KeyEvent.VK_ENTER,		SWT.KEYPAD_CR},
		{java.awt.event.KeyEvent.VK_SUBTRACT,	SWT.KEYPAD_SUBTRACT},
		{java.awt.event.KeyEvent.VK_DECIMAL,		SWT.KEYPAD_DECIMAL},
		{java.awt.event.KeyEvent.VK_DIVIDE,		SWT.KEYPAD_DIVIDE},
		{java.awt.event.KeyEvent.VK_NUMPAD0,		SWT.KEYPAD_0},
		{java.awt.event.KeyEvent.VK_NUMPAD1,		SWT.KEYPAD_1},
		{java.awt.event.KeyEvent.VK_NUMPAD2,		SWT.KEYPAD_2},
		{java.awt.event.KeyEvent.VK_NUMPAD3,		SWT.KEYPAD_3},
		{java.awt.event.KeyEvent.VK_NUMPAD4,		SWT.KEYPAD_4},
		{java.awt.event.KeyEvent.VK_NUMPAD5,		SWT.KEYPAD_5},
		{java.awt.event.KeyEvent.VK_NUMPAD6,		SWT.KEYPAD_6},
		{java.awt.event.KeyEvent.VK_NUMPAD7,		SWT.KEYPAD_7},
		{java.awt.event.KeyEvent.VK_NUMPAD8,		SWT.KEYPAD_8},
		{java.awt.event.KeyEvent.VK_NUMPAD9,		SWT.KEYPAD_9},
//		{java.awt.event.KeyEvent.VK_????,		SWT.KEYPAD_EQUAL},

		/* Other keys */
		{java.awt.event.KeyEvent.VK_CAPS_LOCK,		SWT.CAPS_LOCK},
		{java.awt.event.KeyEvent.VK_NUM_LOCK,		SWT.NUM_LOCK},
		{java.awt.event.KeyEvent.VK_SCROLL_LOCK,		SWT.SCROLL_LOCK},
		{java.awt.event.KeyEvent.VK_PAUSE,		SWT.PAUSE},
		{java.awt.event.KeyEvent.VK_CANCEL,		SWT.BREAK},
		{java.awt.event.KeyEvent.VK_PRINTSCREEN,	SWT.PRINT_SCREEN},
		{java.awt.event.KeyEvent.VK_HELP,		SWT.HELP},
		
	};

	/* Multiple Displays */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Multiple Monitors */
//	static Monitor[] monitors = null;
//	static int monitorCount = 0;
	
	/* Modality */
	Shell [] modalShells;
	Shell modalDialogShell;
	static boolean TrimEnabled = false;

//	/* Private SWT Window Messages */
//	static final int SWT_GETACCELCOUNT	= OS.WM_APP;
//	static final int SWT_GETACCEL 		= OS.WM_APP + 1;
//	static final int SWT_KEYMSG	 		= OS.WM_APP + 2;
//	static final int SWT_DESTROY	 	= OS.WM_APP + 3;
//	static final int SWT_TRAYICONMSG	= OS.WM_APP + 4;
//	static int SWT_TASKBARCREATED;
	
	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets."; //$NON-NLS-1$
	/*
	* This code is intentionally commented.  In order
	* to support CLDC, .class cannot be used because
	* it does not compile on some Java compilers when
	* they are targeted for CLDC.
	*/
//	static {
//		String name = Display.class.getName ();
//		int index = name.lastIndexOf ('.');
//		PACKAGE_PREFIX = name.substring (0, index + 1);
//	}

  static {
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
      public void eventDispatched(AWTEvent event) {
        java.awt.event.InputEvent me = (java.awt.event.InputEvent)event;
        previousModifiersEx = modifiersEx;
        modifiersEx = me.getModifiersEx();
      }
    }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
  }
	/*
	* TEMPORARY CODE.  Install the runnable that
	* gets the current display. This code will
	* be removed in the future.
	*/
	static {
		DeviceFinder = new Runnable () {
			public void run () {
				Device device = getCurrent ();
				if (device == null) {
					device = getDefault ();
				}
				setDevice (device);
			}
		};
	}	

/*
* TEMPORARY CODE.
*/
static void setDevice (Device device) {
	CurrentDevice = device;
}
	
/**
 * Constructs a new instance of this class.
 * <p>
 * Note: The resulting display is marked as the <em>current</em>
 * display. If this is the first display which has been 
 * constructed since the application started, it is also
 * marked as the <em>default</em> display.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see #getCurrent
 * @see #getDefault
 * @see Widget#checkSubclass
 * @see Shell
 */
public Display () {
	this (null);
}

static final String LOOK_AND_FEEL_PROPERTY = "swt.swing.laf";

//CEventQueue managedEventQueue;

public Display (DeviceData data) {
	super (data);
  boolean isLookAndFeelInstalled = false;
  String lafName = System.getProperty(LOOK_AND_FEEL_PROPERTY);
  if(lafName != null) {
    try {
      javax.swing.UIManager.setLookAndFeel(lafName);
      isLookAndFeelInstalled = true;
    } catch(Exception e) {e.printStackTrace();}
  }
  // If no look and feel is specified, install one that looks native.
  if(!isLookAndFeelInstalled) {
    try {
      javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {}
  }
//  managedEventQueue = new CEventQueue();
//  managedEventQueue.attach();
}

//Control _getFocusControl () {
//	return findControl (OS.GetFocus ());
//}

void addBar (Menu menu) {
	if (bars == null) bars = new Menu [4];
	int length = bars.length;
	for (int i=0; i<length; i++) {
		if (bars [i] == menu) return;
	}
	int index = 0;
	while (index < length) {
		if (bars [index] == null) break;
		index++;
	}
	if (index == length) {
		Menu [] newBars = new Menu [length + 4];
		System.arraycopy (bars, 0, newBars, 0, length);
		bars = newBars;
	}
	bars [index] = menu;
}

void addControl (Component handle, Control control) {
	if (handle == null) return;
  componentToControlMap.put(handle, control);
//	if (freeSlot == -1) {
//		int length = (freeSlot = indexTable.length) + GROW_SIZE;
//		int [] newIndexTable = new int [length];
//		Control [] newControlTable = new Control [length];
//		System.arraycopy (indexTable, 0, newIndexTable, 0, freeSlot);
//		System.arraycopy (controlTable, 0, newControlTable, 0, freeSlot);
//		for (int i=freeSlot; i<length-1; i++) newIndexTable [i] = i + 1;
//		newIndexTable [length - 1] = -1;
//		indexTable = newIndexTable;
//		controlTable = newControlTable;
//	}
//	if (USE_PROPERTY) {
//		OS.SetProp (handle, SWT_OBJECT_INDEX, freeSlot + 1);
//	} else {
//		OS.SetWindowLong (handle, OS.GWL_USERDATA, freeSlot + 1);
//	}
//	int oldSlot = freeSlot;
//	freeSlot = indexTable [oldSlot];
//	indexTable [oldSlot] = -2;
//	controlTable [oldSlot] = control;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs anywhere
 * in a widget. When the event does occur, the listener is
 * notified by sending it the <code>handleEvent()</code> message.
 * <p>
 * Setting the type of an event to <code>SWT.None</code> from
 * within the <code>handleEvent()</code> method can be used to
 * change the event type and stop subsequent Java listeners
 * from running. Because event filters run before other listeners,
 * event filters can both block other listeners and set arbitrary
 * fields within an event. For this reason, event filters are both
 * powerful and dangerous. They should generally be avoided for
 * performance, debugging and code maintenance reasons.
 * </p>
 * 
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see #removeFilter
 * @see #removeListener
 * 
 * @since 3.0 
 */
public void addFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) filterTable = new EventTable ();
	filterTable.hook (eventType, listener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs. When the
 * event does occur in the display, the listener is notified by
 * sending it the <code>handleEvent()</code> message.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see #removeListener
 * 
 * @since 2.0 
 */
public void addListener (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, listener);
}

void addMenuItem (MenuItem item) {
  menuItemsList.add(item);
//	if (items == null) items = new MenuItem [64];
//	for (int i=0; i<items.length; i++) {
//		if (items [i] == null) {
//			item.id = i + ID_START;
//			items [i] = item;
//			return;
//		}
//	}
//	item.id = items.length + ID_START;
//	MenuItem [] newItems = new MenuItem [items.length + 64];
//	newItems [items.length] = item;
//	System.arraycopy (items, 0, newItems, 0, items.length);
//	items = newItems;
}

void addPopup (Menu menu) {
	if (popups == null) popups = new Menu [4];
	int length = popups.length;
	for (int i=0; i<length; i++) {
		if (popups [i] == menu) return;
	}
	int index = 0;
	while (index < length) {
		if (popups [index] == null) break;
		index++;
	}
	if (index == length) {
		Menu [] newPopups = new Menu [length + 4];
		System.arraycopy (popups, 0, newPopups, 0, length);
		popups = newPopups;
	}
	popups [index] = menu;
}

//int asciiKey (int key) {
//	if (OS.IsWinCE) return 0;
//	
//	/* Get the current keyboard. */
//	for (int i=0; i<keyboard.length; i++) keyboard [i] = 0;
//	if (!OS.GetKeyboardState (keyboard)) return 0;
//		
//	/* Translate the key to ASCII or UNICODE using the virtual keyboard */
//	if (OS.IsUnicode) {
//		char [] result = new char [1];
//		if (OS.ToUnicode (key, key, keyboard, result, 1, 0) == 1) return result [0];
//	} else {
//		short [] result = new short [1];
//		if (OS.ToAscii (key, key, keyboard, result, 0) == 1) return result [0];
//	}
//	return 0;
//}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next 
 * reasonable opportunity. The caller of this method continues 
 * to run in parallel, and is not notified when the
 * runnable has completed.
 *
 * @param runnable code to run on the user-interface thread.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #syncExec
 */
public void asyncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.asyncExec (runnable);
}

/**
 * Causes the system hardware to emit a short sound
 * (if it supports this capability).
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void beep () {
	checkDevice ();
  Toolkit.getDefaultToolkit().beep();
}

/**
 * Checks that this class can be subclassed.
 * <p>
 * IMPORTANT: See the comment in <code>Widget.checkSubclass()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Widget#checkSubclass
 */
protected void checkSubclass () {
	if (!isValidClass (getClass ())) error (SWT.ERROR_INVALID_SUBCLASS);
}

protected void checkDevice () {
	if (thread == null) error (SWT.ERROR_WIDGET_DISPOSED);
	if (thread != Thread.currentThread () && !SwingUtilities.isEventDispatchThread()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}

static synchronized void checkDisplay (Thread thread) {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] != null && Displays [i].thread == thread) {
			SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}
}

//void clearModal (Shell shell) {
//	if (modalShells == null) return;
//	int index = 0, length = modalShells.length;
//	while (index < length) {
//		if (modalShells [index] == shell) break;
//		if (modalShells [index] == null) return;
//		index++;
//	}
//	if (index == length) return;
//	System.arraycopy (modalShells, index + 1, modalShells, index, --length - index);
//	modalShells [length] = null;
//	if (index == 0 && modalShells [0] == null) modalShells = null;
//	Shell [] shells = getShells ();
//	for (int i=0; i<shells.length; i++) shells [i].updateModal ();
//}
//
//int controlKey (int key) {
//	int upper = OS.CharUpper ((short) key);
//	if (64 <= upper && upper <= 95) return upper & 0xBF;
//	return key;
//}

/**
 * Requests that the connection between SWT and the underlying
 * operating system be closed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Device#dispose
 * 
 * @since 2.0
 */
public void close () {
	checkDevice ();
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit) dispose ();
}

/**
 * Creates the device in the operating system.  If the device
 * does not have a handle, this method may do nothing depending
 * on the device.
 * <p>
 * This method is called before <code>init</code>.
 * </p>
 *
 * @param data the DeviceData which describes the receiver
 *
 * @see #init
 */
protected void create (DeviceData data) {
	checkSubclass ();
	checkDisplay (thread = Thread.currentThread ());
	createDisplay (data);
	register (this);
	if (Default == null) Default = this;
}

void createDisplay (DeviceData data) {
}

static synchronized void deregister (Display display) {
	for (int i=0; i<Displays.length; i++) {
		if (display == Displays [i]) Displays [i] = null;
	}
}

/**
 * Destroys the device in the operating system and releases
 * the device's handle.  If the device does not have a handle,
 * this method may do nothing depending on the device.
 * <p>
 * This method is called after <code>release</code>.
 * </p>
 * @see #dispose
 * @see #release
 */
protected void destroy () {
	if (this == Default) Default = null;
	deregister (this);
	destroyDisplay ();
}

void destroyDisplay () {
//  managedEventQueue.detach();
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread just before the
 * receiver is disposed.
 *
 * @param runnable code to run at dispose time.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void disposeExec (Runnable runnable) {
	checkDevice ();
  if (disposeList == null) disposeList = new ArrayList();
  disposeList.add(runnable);
//	if (disposeList == null) disposeList = new Runnable [4];
//	for (int i=0; i<disposeList.length; i++) {
//		if (disposeList [i] == null) {
//			disposeList [i] = runnable;
//			return;
//		}
//	}
//	Runnable [] newDisposeList = new Runnable [disposeList.length + 4];
//	System.arraycopy (disposeList, 0, newDisposeList, 0, disposeList.length);
//	newDisposeList [disposeList.length] = runnable;
//	disposeList = newDisposeList;
}

//void drawMenuBars () {
//	if (bars == null) return;
//	for (int i=0; i<bars.length; i++) {
//		Menu menu = bars [i];
//		if (menu != null && !menu.isDisposed ()) menu.update ();
//	}
//	bars = null;
//}
//
//int embeddedProc (int hwnd, int msg, int wParam, int lParam) {
//	switch (msg) {
//		case SWT_KEYMSG: {
//			MSG keyMsg = new MSG ();
//			OS.MoveMemory (keyMsg, lParam, MSG.sizeof);
//			OS.TranslateMessage (keyMsg);
//			OS.DispatchMessage (keyMsg);
//			int hHeap = OS.GetProcessHeap ();
//			OS.HeapFree (hHeap, 0, lParam);
//			break;
//		}
//		case SWT_DESTROY: {
//			OS.DestroyWindow (hwnd);
//			if (embeddedCallback != null) embeddedCallback.dispose ();
//			if (getMsgCallback != null) getMsgCallback.dispose ();
//			embeddedCallback = getMsgCallback = null;
//			embeddedProc = getMsgProc = 0;
//			break;
//		}
//	}
//	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
//}

/**
 * Does whatever display specific cleanup is required, and then
 * uses the code in <code>SWTError.error</code> to handle the error.
 *
 * @param code the descriptive error code
 *
 * @see SWTError#error
 */
void error (int code) {
	SWT.error (code);
}

boolean filterEvent (Event event) {
	if (filterTable != null) filterTable.sendEvent (event);
	return false;
}

boolean filters (int eventType) {
	if (filterTable == null) return false;
	return filterTable.hooks (eventType);
}

HashMap componentToControlMap = new HashMap();

Control findControl (Component handle) {
  if (handle == null) return null;
  do {
    Control control = getControl (handle);
    if (control != null) return control;
  } while ((handle = handle.getParent()) != null);
  return null;
}

//boolean filterMessage (MSG msg) {
//	int message = msg.message;
//	if (OS.WM_KEYFIRST <= message && message <= OS.WM_KEYLAST) {
//		Control control = findControl (msg.hwnd);
//		if (control != null) {
//			if (translateAccelerator (msg, control) || translateMnemonic (msg, control) || translateTraversal (msg, control)) {	
//				lastAscii = lastKey = 0;
//				lastVirtual = lastNull = lastDead = false;
//				return true;
//			}
//		}
//	}
//	return false;
//}
//
//Control findControl (int handle) {
//	if (handle == 0) return null;
//	do {
//		Control control = getControl (handle);
//		if (control != null) return control;
//	} while ((handle = OS.GetParent (handle)) != 0);
//	return null;
//}
//
///**
// * Given the operating system handle for a widget, returns
// * the instance of the <code>Widget</code> subclass which
// * represents it in the currently running application, if
// * such exists, or null if no matching widget can be found.
// * <p>
// * <b>IMPORTANT:</b> This method should not be called from
// * application code. The arguments are platform-specific.
// * </p>
// *
// * @param handle the handle for the widget
// * @return the SWT widget that the handle represents
// *
// * @exception SWTException <ul>
// *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
// *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
// * </ul>
// */
//public Widget findWidget (int handle) {
//	checkDevice ();
//	return getControl (handle);
//}
//
///**
// * Given the operating system handle for a widget,
// * and widget-specific id, returns the instance of
// * the <code>Widget</code> subclass which represents
// * the handle/id pair in the currently running application,
// * if such exists, or null if no matching widget can be found.
// * <p>
// * <b>IMPORTANT:</b> This method should not be called from
// * application code. The arguments are platform-specific.
// * </p>
// *
// * @param handle the handle for the widget
// * @param id the id for the subwidget (usually an item)
// * @return the SWT widget that the handle/id pair represents
// *
// * @exception SWTException <ul>
// *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
// *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
// * </ul>
// * 
// * @since 3.1
// */
//public Widget findWidget (int handle, int id) {
//	Control control = getControl (handle);
//	return control != null ? control.findItem (id) : null;
//}

//int foregroundIdleProc (int code, int wParam, int lParam) {
//	if (code >= 0) {
//		if (getMessageCount () > 0) wakeThread ();
//	}
//	return OS.CallNextHookEx (idleHook, code, wParam, lParam);
//}

/**
 * Returns the display which the given thread is the
 * user-interface thread for, or null if the given thread
 * is not a user-interface thread for any display.
 *
 * @param thread the user-interface thread
 * @return the display for the given thread
 */
public static synchronized Display findDisplay (Thread thread) {
	for (int i=0; i<Displays.length; i++) {
		Display display = Displays [i];
		if (display != null && display.thread == thread) {
			return display;
		}
	}
	return null;
}

/**
 * Returns the currently active <code>Shell</code>, or null
 * if no shell belonging to the currently running application
 * is active.
 *
 * @return the active shell or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Shell getActiveShell () {
	checkDevice ();
  Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
  if(window == null) return null;
  return (Shell)findControl(window);
}

/**
 * Returns a rectangle describing the receiver's size and location.
 *
 * @return the bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkDevice ();
  // TODO: support multiple monitors
  Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
  return new Rectangle(0, 0, d.width, d.height);
}

/**
 * Returns the display which the currently running thread is
 * the user-interface thread for, or null if the currently
 * running thread is not a user-interface thread for any display.
 *
 * @return the current display
 */
public static synchronized Display getCurrent () {
	return findDisplay (Thread.currentThread ());
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data.
 * 
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getBounds
 */
public Rectangle getClientArea () {
	checkDevice ();
  // TODO: check screen configuration API to find real bounds for multi screens
  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  java.awt.Rectangle rectangle = ge.getMaximumWindowBounds();
  return new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
}

Control getControl (Component handle) {
  return (Control)componentToControlMap.get(handle);
//	if (handle == 0) return null;
//	int index;
//	if (USE_PROPERTY) {
//		index = OS.GetProp (handle, SWT_OBJECT_INDEX) - 1;
//	} else {
//		index = OS.GetWindowLong (handle, OS.GWL_USERDATA) - 1;
//	}
//	if (0 <= index && index < controlTable.length) {
//		Control control = controlTable [index];
//		/*
//		* Because GWL_USERDATA can be used by native widgets that
//		* do not belong to SWT, it is possible that GWL_USERDATA
//		* could return an index that is in the range of the table,
//		* but was not put there by SWT.  Therefore, it is necessary
//		* to check the handle of the control that is in the table
//		* against the handle that provided the GWL_USERDATA.
//		*/
//		if (control != null && control.checkHandle (handle)) {
//			return control;
//		}
//	}
//	return null;
}

/**
 * Returns the control which the on-screen pointer is currently
 * over top of, or null if it is not currently over one of the
 * controls built by the currently running application.
 *
 * @return the control under the cursor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Control getCursorControl () {
  checkDevice ();
  java.awt.Point point = MouseInfo.getPointerInfo().getLocation();
  // TODO: what about windows?
  Frame[] frames = Frame.getFrames();
  for(int i=0; i<frames.length; i++) {
    Frame frame = frames[i];
    Component component = frame.findComponentAt(point);
    if(component != null) {
      Control control = findControl(component);
      if(control != null) return control;
    }
  }
	return null;
}

/**
 * Returns the location of the on-screen pointer relative
 * to the top left corner of the screen.
 *
 * @return the cursor location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point getCursorLocation () {
	checkDevice ();
  java.awt.Point point = MouseInfo.getPointerInfo().getLocation();
  return new Point(point.x, point.y);
}

/**
 * Returns an array containing the recommended cursor sizes.
 *
 * @return the array of cursor sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public Point [] getCursorSizes () {
	checkDevice ();
  HashSet set = new HashSet();
  Toolkit toolkit = Toolkit.getDefaultToolkit();
  for(int i=8; i<64; i++) {
    set.add(toolkit.getBestCursorSize(i, i));
  }
  set.remove(new java.awt.Dimension(0, 0));
  Point[] sizes = new Point[set.size()];
  int i=0;
  for(Iterator it=set.iterator(); it.hasNext(); ) {
    java.awt.Dimension d = ((java.awt.Dimension)it.next());
    sizes[i++] = new Point(d.width, d.height);
  }
  return sizes;
}

/**
 * Returns the default display. One is created (making the
 * thread that invokes this method its user-interface thread)
 * if it did not already exist.
 *
 * @return the default display
 */
public static synchronized Display getDefault () {
	if (Default == null) Default = new Display ();
	return Default;
}

static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
}

/**
 * Returns the application defined property of the receiver
 * with the specified name, or null if it has not been set.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the display is disposed
 * of, it is the application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param key the name of the property
 * @return the value of the property or null if it has not been set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setData(String, Object)
 * @see #disposeExec(Runnable)
 */
public Object getData (String key) {
	checkDevice ();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
	}
	return null;
}

/**
 * Returns the application defined, display specific data
 * associated with the receiver, or null if it has not been
 * set. The <em>display specific data</em> is a single,
 * unnamed field that is stored with every display. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the display specific data needs to
 * be notified when the display is disposed of, it is the
 * application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @return the display specific data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setData(Object)
 * @see #disposeExec(Runnable)
 */
public Object getData () {
	checkDevice ();
	return data;
}

/**
 * Returns the button dismissal alignment, one of <code>LEFT</code> or <code>RIGHT</code>.
 * The button dismissal alignment is the ordering that should be used when positioning the
 * default dismissal button for a dialog.  For example, in a dialog that contains an OK and
 * CANCEL button, on platforms where the button dismissal alignment is <code>LEFT</code>, the
 * button ordering should be OK/CANCEL.  When button dismissal alignment is <code>RIGHT</code>,
 * the button ordering should be CANCEL/OK.
 *
 * @return the button dismissal order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1
 */
public int getDismissalAlignment () {
	checkDevice ();
	return SWT.LEFT;
}


/**
 * Returns the longest duration, in milliseconds, between
 * two mouse button clicks that will be considered a
 * <em>double click</em> by the underlying operating system.
 *
 * @return the double click time
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getDoubleClickTime () {
	checkDevice ();
  // TODO: Method in Swing to do that?
	return 200;
}

/**
 * Returns the control which currently has keyboard focus,
 * or null if keyboard events are not currently going to
 * any of the controls built by the currently running
 * application.
 *
 * @return the control under the cursor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Control getFocusControl () {
	checkDevice ();
  Component component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
  if(component == null) return null;
  return findControl(component);
}

/**
 * Returns true when the high contrast mode is enabled.
 * Otherwise, false is returned.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @return the high contrast mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public boolean getHighContrast () {
	checkDevice ();
  // TODO: What about other platforms
  Boolean highContrast = (Boolean)Toolkit.getDefaultToolkit().getDesktopProperty("win.highContrast.on");
  return highContrast != null? highContrast.booleanValue(): false;
}

/**
 * Returns the maximum allowed depth of icons on this display.
 * On some platforms, this may be different than the actual
 * depth of the display.
 *
 * @return the maximum icon depth
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getIconDepth () {
	checkDevice ();
  // TODO: is thsi correct?
  int bitDepth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getBitDepth();
  return bitDepth == -1? 32: bitDepth;
}

/**
 * Returns an array containing the recommended icon sizes.
 *
 * @return the array of icon sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Decorations#setImages(Image[])
 * 
 * @since 3.0
 */
public Point [] getIconSizes () {
	checkDevice ();
  // TODO: method to do so in Swing?
	return new Point [] {
		new Point (32, 32),
		new Point (64, 64),
	};	
}

//ImageList getImageList (Point size) {
//	if (imageList == null) imageList = new ImageList [4];
//	
//	int i = 0;
//	int length = imageList.length; 
//	while (i < length) {
//		ImageList list = imageList [i];
//		if (list == null) break;
//		if (list.getImageSize().equals(size)) {
//			list.addRef();
//			return list;
//		}
//		i++;
//	}
//	
//	if (i == length) {
//		ImageList [] newList = new ImageList [length + 4];
//		System.arraycopy (imageList, 0, newList, 0, length);
//		imageList = newList;
//	}
//	
//	ImageList list = new ImageList();
//	imageList [i] = list;
//	list.addRef();
//	return list;
//}
//
//ImageList getToolImageList (Point size) {
//	if (toolImageList == null) toolImageList = new ImageList [4];
//	
//	int i = 0;
//	int length = toolImageList.length; 
//	while (i < length) {
//		ImageList list = toolImageList [i];
//		if (list == null) break;
//		if (list.getImageSize().equals(size)) {
//			list.addRef();
//			return list;
//		}
//		i++;
//	}
//	
//	if (i == length) {
//		ImageList [] newList = new ImageList [length + 4];
//		System.arraycopy (toolImageList, 0, newList, 0, length);
//		toolImageList = newList;
//	}
//	
//	ImageList list = new ImageList();
//	toolImageList [i] = list;
//	list.addRef();
//	return list;
//}
//
//ImageList getToolHotImageList (Point size) {
//	if (toolHotImageList == null) toolHotImageList = new ImageList [4];
//	
//	int i = 0;
//	int length = toolHotImageList.length; 
//	while (i < length) {
//		ImageList list = toolHotImageList [i];
//		if (list == null) break;
//		if (list.getImageSize().equals(size)) {
//			list.addRef();
//			return list;
//		}
//		i++;
//	}
//	
//	if (i == length) {
//		ImageList [] newList = new ImageList [length + 4];
//		System.arraycopy (toolHotImageList, 0, newList, 0, length);
//		toolHotImageList = newList;
//	}
//	
//	ImageList list = new ImageList();
//	toolHotImageList [i] = list;
//	list.addRef();
//	return list;
//}
//
//ImageList getToolDisabledImageList (Point size) {
//	if (toolDisabledImageList == null) toolDisabledImageList = new ImageList [4];
//	
//	int i = 0;
//	int length = toolDisabledImageList.length; 
//	while (i < length) {
//		ImageList list = toolDisabledImageList [i];
//		if (list == null) break;
//		if (list.getImageSize().equals(size)) {
//			list.addRef();
//			return list;
//		}
//		i++;
//	}
//	
//	if (i == length) {
//		ImageList [] newList = new ImageList [length + 4];
//		System.arraycopy (toolDisabledImageList, 0, newList, 0, length);
//		toolDisabledImageList = newList;
//	}
//	
//	ImageList list = new ImageList();
//	toolDisabledImageList [i] = list;
//	list.addRef();
//	return list;
//}

static long lastTime = System.currentTimeMillis();

int getLastEventTime () {
	return (int)(System.currentTimeMillis() - lastTime);
}

MenuItem getMenuItem (JComponent component) {
  for(int i=0; i<menuItemsList.size(); i++) {
    MenuItem menuItem = (MenuItem)menuItemsList.get(i);
    if(menuItem.handle == component) {
      return menuItem;
    }
  }
  return null;
}

//int getMessageCount () {
//	return synchronizer.getMessageCount ();
//}
//
//
//Shell getModalShell () {
//	if (modalShells == null) return null;
//	int index = modalShells.length;
//	while (--index >= 0) {
//		Shell shell = modalShells [index];
//		if (shell != null) return shell;
//	}
//	return null;
//}
//
//Shell getModalDialogShell () {
//	if (modalDialogShell != null && modalDialogShell.isDisposed ()) modalDialogShell = null;
//	return modalDialogShell;
//}

/**
 * Returns an array of monitors attached to the device.
 * 
 * @return the array of monitors
 * 
 * @since 3.0
 */
public Monitor [] getMonitors () {
	checkDevice ();
  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  GraphicsDevice[] gs = ge.getScreenDevices();
  ArrayList monitorsList = new ArrayList();
  for (int j = 0; j < gs.length; j++) {
    GraphicsDevice gd = gs[j];
    // Always true isnt'it?
//    if(gd.getType() == GraphicsDevice.TYPE_RASTER_SCREEN) {
      GraphicsConfiguration[] gcs = gd.getConfigurations();
      for (int i=0; i < gcs.length; i++) {
        GraphicsConfiguration gc = gcs[i];
        Monitor monitor = new Monitor();
        monitor.handle = gc;
        java.awt.Rectangle bounds = gc.getBounds();
        monitor.x = bounds.x;
        monitor.y = bounds.y;
        monitor.width = bounds.width;
        monitor.height = bounds.height;
        java.awt.Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        monitor.clientX = bounds.x + insets.left;
        monitor.clientY = bounds.y + insets.top;
        monitor.clientWidth = bounds.x - insets.left - insets.right;
        monitor.clientHeight = bounds.y - insets.top - insets.bottom;
        monitorsList.add(monitor);
      }
//    }
  }
  return (Monitor[])monitorsList.toArray(new Monitor[0]);
}

//int getMsgProc (int code, int wParam, int lParam) {
//	if (embeddedHwnd == 0) {
//		int hInstance = OS.GetModuleHandle (null);
//		embeddedHwnd = OS.CreateWindowEx (0,
//			windowClass,
//			null,
//			OS.WS_OVERLAPPED,
//			0, 0, 0, 0,
//			0,
//			0,
//			hInstance,
//			null);
//		embeddedCallback = new Callback (this, "embeddedProc", 4); //$NON-NLS-1$
//		embeddedProc = embeddedCallback.getAddress ();
//		if (embeddedProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
//		OS.SetWindowLong (embeddedHwnd, OS.GWL_WNDPROC, embeddedProc);
//	}
//	if (code >= 0 && wParam != OS.PM_NOREMOVE) {
//		MSG msg = new MSG ();
//		OS.MoveMemory (msg, lParam, MSG.sizeof);
//		switch (msg.message) {
//			case OS.WM_KEYDOWN:
//			case OS.WM_KEYUP:
//			case OS.WM_SYSKEYDOWN:
//			case OS.WM_SYSKEYUP: {
//				int hHeap = OS.GetProcessHeap ();
//				int keyMsg = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, MSG.sizeof);
//				OS.MoveMemory (keyMsg, msg, MSG.sizeof);
//				OS.PostMessage (hwndMessage, SWT_KEYMSG, wParam, keyMsg);
//				msg.message = OS.WM_NULL;
//				OS.MoveMemory (lParam, msg, MSG.sizeof);
//			}
//		}
//	}
//	return OS.CallNextHookEx (msgHook, code, wParam, lParam);
//}

/**
 * Returns the primary monitor for that device.
 * 
 * @return the primary monitor
 * 
 * @since 3.0
 */
public Monitor getPrimaryMonitor () {
	checkDevice ();
  GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
  Monitor monitor = new Monitor();
  monitor.handle = gc;
  java.awt.Rectangle bounds = gc.getBounds();
  monitor.x = bounds.x;
  monitor.y = bounds.y;
  monitor.width = bounds.width;
  monitor.height = bounds.height;
  java.awt.Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
  monitor.clientX = bounds.x + insets.left;
  monitor.clientY = bounds.y + insets.top;
  monitor.clientWidth = bounds.x - insets.left - insets.right;
  monitor.clientHeight = bounds.y - insets.top - insets.bottom;
	return monitor;		
}

/**
 * Returns an array containing all shells which have not been
 * disposed and have the receiver as their display.
 *
 * @return the receiver's shells
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Shell [] getShells () {
	checkDevice ();
  ArrayList controlsList = new ArrayList();
  for(Iterator it = componentToControlMap.keySet().iterator(); it.hasNext(); ) {
    Component component = (Component)it.next();
    // TODO: what about file dialogs and such?
    if(component instanceof Window) {
      Control control = findControl(component);
      if(control instanceof Shell) {
        controlsList.add(control);
      }
    }
  }
  return (Shell[])controlsList.toArray(new Shell[0]);
}

/**
 * Returns the thread that has invoked <code>syncExec</code>
 * or null if no such runnable is currently being invoked by
 * the user-interface thread.
 * <p>
 * Note: If a runnable invoked by asyncExec is currently
 * running, this method will return null.
 * </p>
 *
 * @return the receiver's sync-interface thread
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Thread getSyncThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return synchronizer.syncThread;
}

/**
 * Returns the matching standard color for the given
 * constant, which should be one of the color constants
 * specified in class <code>SWT</code>. Any value other
 * than one of the SWT color constants which is passed
 * in will result in the color black. This color should
 * not be free'd because it was allocated by the system,
 * not the application.
 *
 * @param id the color constant
 * @return the matching color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT
 */
public Color getSystemColor (int id) {
	checkDevice ();
	java.awt.Color swingColor;
  switch(id) {
    case SWT.COLOR_WIDGET_DARK_SHADOW:
      swingColor = UIManager.getColor("controlDkShadow"); break;
    case SWT.COLOR_WIDGET_NORMAL_SHADOW:
      swingColor = UIManager.getColor("controlShadow"); break;
    case SWT.COLOR_WIDGET_LIGHT_SHADOW:
      swingColor = UIManager.getColor("controlLtHighlight"); break;
    case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:
      swingColor = UIManager.getColor("controlHighlight"); break;
    case SWT.COLOR_WIDGET_BACKGROUND:
      swingColor = UIManager.getColor("control"); break;
    case SWT.COLOR_WIDGET_BORDER:
      swingColor = UIManager.getColor("windowBorder"); break;
    case SWT.COLOR_WIDGET_FOREGROUND:
      swingColor = UIManager.getColor("controlText"); break;
    case SWT.COLOR_LIST_FOREGROUND:
      swingColor = UIManager.getColor("controlText"); break;
    case SWT.COLOR_LIST_BACKGROUND:
      swingColor = UIManager.getColor("control"); break;
    case SWT.COLOR_LIST_SELECTION:
      swingColor = UIManager.getColor("control"); break;
    case SWT.COLOR_LIST_SELECTION_TEXT:
      swingColor = UIManager.getColor("controlText"); break;
    case SWT.COLOR_INFO_FOREGROUND:
      swingColor = UIManager.getColor("infoText"); break;
    case SWT.COLOR_INFO_BACKGROUND:
      swingColor = UIManager.getColor("info"); break;
    case SWT.COLOR_TITLE_FOREGROUND:
      swingColor = UIManager.getColor("activeCaptionText"); break;
    case SWT.COLOR_TITLE_BACKGROUND:
      swingColor = UIManager.getColor("activeCaption"); break;
    case SWT.COLOR_TITLE_BACKGROUND_GRADIENT:
      swingColor = UIManager.getColor("activeCaption"); break;
    case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:
      swingColor = UIManager.getColor("inactiveCaptionText"); break;
    case SWT.COLOR_TITLE_INACTIVE_BACKGROUND:
      swingColor = UIManager.getColor("inactiveCaption"); break;
    case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT:
      swingColor = UIManager.getColor("inactiveCaption"); break;
    default:
      return super.getSystemColor(id);
  }
  return Color.swing_new(this, swingColor);
}

/**
 * Returns the matching standard platform cursor for the given
 * constant, which should be one of the cursor constants
 * specified in class <code>SWT</code>. This cursor should
 * not be free'd because it was allocated by the system,
 * not the application.  A value of <code>null</code> will
 * be returned if the supplied constant is not an swt cursor
 * constant. 
 *
 * @param id the swt cursor constant
 * @return the corresponding cursor or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT#CURSOR_ARROW
 * @see SWT#CURSOR_WAIT
 * @see SWT#CURSOR_CROSS
 * @see SWT#CURSOR_APPSTARTING
 * @see SWT#CURSOR_HELP
 * @see SWT#CURSOR_SIZEALL
 * @see SWT#CURSOR_SIZENESW
 * @see SWT#CURSOR_SIZENS
 * @see SWT#CURSOR_SIZENWSE
 * @see SWT#CURSOR_SIZEWE
 * @see SWT#CURSOR_SIZEN
 * @see SWT#CURSOR_SIZES
 * @see SWT#CURSOR_SIZEE
 * @see SWT#CURSOR_SIZEW
 * @see SWT#CURSOR_SIZENE
 * @see SWT#CURSOR_SIZESE
 * @see SWT#CURSOR_SIZESW
 * @see SWT#CURSOR_SIZENW
 * @see SWT#CURSOR_UPARROW
 * @see SWT#CURSOR_IBEAM
 * @see SWT#CURSOR_NO
 * @see SWT#CURSOR_HAND
 * 
 * @since 3.0
 */
public Cursor getSystemCursor (int id) {
	checkDevice ();
	if (!(0 <= id && id < cursors.length)) return null;
	if (cursors [id] == null) {
		cursors [id] = new Cursor (this, id);
	}
	return cursors [id];
}

/**
 * Returns a reasonable font for applications to use.
 * On some platforms, this will match the "default font"
 * or "system font" if such can be found.  This font
 * should not be free'd because it was allocated by the
 * system, not the application.
 * <p>
 * Typically, applications which want the default look
 * should simply not set the font on the widgets they
 * create. Widgets are always created with the correct
 * default font for the class of user-interface component
 * they represent.
 * </p>
 *
 * @return a font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getSystemFont () {
	checkDevice ();
  // TODO: Support non metal look and feels by finding the system font differently
  return Font.swing_new(this, javax.swing.plaf.metal.MetalLookAndFeel.getSystemTextFont());
}

/**
 * Returns the matching standard platform image for the given
 * constant, which should be one of the icon constants
 * specified in class <code>SWT</code>. This image should
 * not be free'd because it was allocated by the system,
 * not the application.  A value of <code>null</code> will
 * be returned either if the supplied constant is not an
 * swt icon constant or if the platform does not define an
 * image that corresponds to the constant. 
 *
 * @param id the swt icon constant
 * @return the corresponding image or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT#ICON_ERROR
 * @see SWT#ICON_INFORMATION
 * @see SWT#ICON_QUESTION
 * @see SWT#ICON_WARNING
 * @see SWT#ICON_WORKING
 * 
 * @since 3.0
 */
public Image getSystemImage (int id) {
	checkDevice ();
	java.awt.Image hIcon = null;
	switch (id) {
		case SWT.ICON_ERROR:
			if (errorIcon == null) {
        errorIcon = getImage(UIManager.getIcon("OptionPane.errorIcon"));
			}
			hIcon = errorIcon;
			break;
		case SWT.ICON_WORKING:
		case SWT.ICON_INFORMATION:
			if (infoIcon == null) {
        infoIcon = getImage(UIManager.getIcon("OptionPane.informationIcon"));
			}
			hIcon = infoIcon;
			break;
		case SWT.ICON_QUESTION:
			if (questionIcon == null) {
        questionIcon = getImage(UIManager.getIcon("OptionPane.questionIcon"));
			}
			hIcon = questionIcon;
			break;
		case SWT.ICON_WARNING:
			if (warningIcon == null) {
        warningIcon = getImage(UIManager.getIcon("OptionPane.warningIcon"));
			}
			hIcon = warningIcon;
			break;
	}
	if (hIcon == null) return null;
	return Image.swing_new (this, SWT.ICON, hIcon);
}

static java.awt.Image getImage(Icon icon) {
  BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TRANSLUCENT);
  Graphics g = image.getGraphics();
  icon.paintIcon(Utils.getDefaultComponent(), g, 0, 0);
  g.dispose();
  return image;
}

/**
 * Returns the single instance of the system tray or null
 * when there is no system tray available for the platform.
 *
 * @return the system tray or <code>null</code>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public Tray getSystemTray () {
	checkDevice ();
	if (tray != null) return tray;
	return tray = new Tray (this, SWT.NONE);
}

/**
 * Returns the user-interface thread for the receiver.
 *
 * @return the receiver's user-interface thread
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Thread getThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return thread;
}

/**	 
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Display</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data 
 * @return the platform specific GC handle
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
 * </ul>
 */
public Graphics2D internal_new_GC (GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
  if (data != null) {
    data.device = this;
  }
//	int hDC = OS.GetDC (0);
//	if (hDC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
//	if (data != null) {
//		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
//		if ((data.style & mask) != 0) {
//			data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
//		} else {
//			data.style |= SWT.LEFT_TO_RIGHT;
//		}
//		data.device = this;
//		data.hFont = systemFont ();
//	}
//	return hDC;
  // TODO: implement
  throw new IllegalStateException("Not implemented!");
}

/**
 * Initializes any internal resources needed by the
 * device.
 * <p>
 * This method is called after <code>create</code>.
 * </p>
 * 
 * @see #create
 */
protected void init () {
	super.init ();
//		
//	/* Create the callbacks */
//	windowCallback = new Callback (this, "windowProc", 4); //$NON-NLS-1$
//	windowProc = windowCallback.getAddress ();
//	if (windowProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
//	
//	/* Remember the current thread id */
//	threadId = OS.GetCurrentThreadId ();
//	
//	/* Use the character encoding for the default locale */
//	windowClass = new TCHAR (0, WindowName + WindowClassCount, true);
//	windowShadowClass = new TCHAR (0, WindowShadowName + WindowClassCount, true);
//	WindowClassCount++;
//
//	/* Register the SWT window class */
//	int hHeap = OS.GetProcessHeap ();
//	int hInstance = OS.GetModuleHandle (null);
//	WNDCLASS lpWndClass = new WNDCLASS ();
//	lpWndClass.hInstance = hInstance;
//	lpWndClass.lpfnWndProc = windowProc;
//	lpWndClass.style = OS.CS_BYTEALIGNWINDOW | OS.CS_DBLCLKS;
//	lpWndClass.hCursor = OS.LoadCursor (0, OS.IDC_ARROW);
//	int byteCount = windowClass.length () * TCHAR.sizeof;
//	lpWndClass.lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//	OS.MoveMemory (lpWndClass.lpszClassName, windowClass, byteCount);
//	OS.RegisterClass (lpWndClass);
//
//	/* Register the SWT drop shadow window class */
//	if (OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
//		lpWndClass.style |= OS.CS_DROPSHADOW;
//	}
//	byteCount = windowShadowClass.length () * TCHAR.sizeof;
//	lpWndClass.lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//	OS.MoveMemory (lpWndClass.lpszClassName, windowShadowClass, byteCount);
//	OS.RegisterClass (lpWndClass);
//	
//	/* Initialize the system font */
//	int systemFont = 0;
//	if (!OS.IsWinCE) {
//		NONCLIENTMETRICS info = OS.IsUnicode ? (NONCLIENTMETRICS) new NONCLIENTMETRICSW () : new NONCLIENTMETRICSA ();
//		info.cbSize = NONCLIENTMETRICS.sizeof;
//		if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
//			systemFont = OS.CreateFontIndirect (OS.IsUnicode ? (LOGFONT) ((NONCLIENTMETRICSW)info).lfMessageFont : ((NONCLIENTMETRICSA)info).lfMessageFont);
//		}
//	}
//	if (systemFont == 0) systemFont = OS.GetStockObject (OS.DEFAULT_GUI_FONT);
//	if (systemFont == 0) systemFont = OS.GetStockObject (OS.SYSTEM_FONT);
//	if (systemFont != 0) systemFonts = new int [] {systemFont};
//	
//	/* Create the message only HWND */
//	hwndMessage = OS.CreateWindowEx (0,
//		windowClass,
//		null,
//		OS.WS_OVERLAPPED,
//		0, 0, 0, 0,
//		0,
//		0,
//		hInstance,
//		null);
//	messageCallback = new Callback (this, "messageProc", 4); //$NON-NLS-1$
//	messageProc = messageCallback.getAddress ();
//	if (messageProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
//	OS.SetWindowLong (hwndMessage, OS.GWL_WNDPROC, messageProc);
//
//	/* Create the filter hook */
//	if (!OS.IsWinCE) {
//		msgFilterCallback = new Callback (this, "msgFilterProc", 3); //$NON-NLS-1$
//		msgFilterProc = msgFilterCallback.getAddress ();
//		if (msgFilterProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
//		filterHook = OS.SetWindowsHookEx (OS.WH_MSGFILTER, msgFilterProc, 0, threadId);
//	}
//	
//	/* Create the idle hook */
//	if (!OS.IsWinCE) {
//		foregroundIdleCallback = new Callback (this, "foregroundIdleProc", 3); //$NON-NLS-1$
//		foregroundIdleProc = foregroundIdleCallback.getAddress ();
//		if (foregroundIdleProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
//		idleHook = OS.SetWindowsHookEx (OS.WH_FOREGROUNDIDLE, foregroundIdleProc, 0, threadId);
//	}
//	
//	/* Register the task bar created message */
//	SWT_TASKBARCREATED = OS.RegisterWindowMessage (new TCHAR (0, "TaskbarCreated", true));
//
//	/* Initialize OLE */
//	if (!OS.IsWinCE) {
//		OS.OleInitialize (0);
//	}
//	
//	/* Initialize the Widget Table */
//	indexTable = new int [GROW_SIZE];
//	controlTable = new Control [GROW_SIZE];
//	for (int i=0; i<GROW_SIZE-1; i++) indexTable [i] = i + 1;
//	indexTable [GROW_SIZE - 1] = -1;
}

/**	 
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Display</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param handle the platform specific GC handle
 * @param data the platform specific GC data 
 */
public void internal_dispose_GC (Graphics2D handle, GCData data) {
	handle.dispose();
}

boolean isValidThread () {
	return thread == Thread.currentThread () || SwingUtilities.isEventDispatchThread();
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 * 
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 * 
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param point to be mapped 
 * @return point with mapped coordinates 
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public Point map (Control from, Control to, Point point) {
	checkDevice ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);	
	return map (from, to, point.x, point.y);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 * 
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 * 
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param x coordinates to be mapped
 * @param y coordinates to be mapped
 * @return point with mapped coordinates
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public Point map (Control from, Control to, int x, int y) {
	checkDevice ();
	if (from != null && from.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (to != null && to.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
  java.awt.Point point = SwingUtilities.convertPoint(from == null? null: from.handle, x, y, to == null? null: to.handle);
  return new Point(point.x, point.y);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 * 
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 * 
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param rectangle to be mapped
 * @return rectangle with mapped coordinates
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public Rectangle map (Control from, Control to, Rectangle rectangle) {
	checkDevice ();
	if (rectangle == null) error (SWT.ERROR_NULL_ARGUMENT);	
	return map (from, to, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 * 
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 * 
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param x coordinates to be mapped
 * @param y coordinates to be mapped
 * @param width coordinates to be mapped
 * @param height coordinates to be mapped
 * @return rectangle with mapped coordinates
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public Rectangle map (Control from, Control to, int x, int y, int width, int height) {
	checkDevice ();
  java.awt.Point point = SwingUtilities.convertPoint(from == null? null: from.handle, x, y, to == null? null: to.handle);
  return new Rectangle(point.x, point.y, point.x + width - x, point.y + height - y);
}

static int previousModifiersEx;
static int modifiersEx;

static int getPreviousInputState() {
  return convertModifiersEx(previousModifiersEx);
}

static int getInputState() {
  return convertModifiersEx(modifiersEx);
}

static int convertModifiersEx(int modifiersEx) {
  int state = 0;
  if((modifiersEx & java.awt.event.KeyEvent.SHIFT_DOWN_MASK) != 0) {
    state |= SWT.SHIFT;
  }
  if((modifiersEx & java.awt.event.KeyEvent.ALT_DOWN_MASK) != 0) {
    state |= SWT.ALT;
  }
  if((modifiersEx & java.awt.event.KeyEvent.CTRL_DOWN_MASK) != 0) {
    state |= SWT.CTRL;
  }
  if((modifiersEx & java.awt.event.KeyEvent.BUTTON1_DOWN_MASK) != 0) {
    state |= SWT.BUTTON1;
  }
  if((modifiersEx & java.awt.event.KeyEvent.BUTTON2_DOWN_MASK) != 0) {
    state |= SWT.BUTTON2;
  }
  if((modifiersEx & java.awt.event.KeyEvent.BUTTON3_DOWN_MASK) != 0) {
    state |= SWT.BUTTON3;
  }
  return state;
}

///*
// * Returns a single character, converted from the default
// * multi-byte character set (MBCS) used by the operating
// * system widgets to a wide character set (WCS) used by Java.
// *
// * @param ch the MBCS character
// * @return the WCS character
// */
//static char mbcsToWcs (int ch) {
//	return mbcsToWcs (ch, 0);
//}
//
///*
// * Returns a single character, converted from the specified
// * multi-byte character set (MBCS) used by the operating
// * system widgets to a wide character set (WCS) used by Java.
// *
// * @param ch the MBCS character
// * @param codePage the code page used to convert the character
// * @return the WCS character
// */
//static char mbcsToWcs (int ch, int codePage) {
//	if (OS.IsUnicode) return (char) ch;
//	int key = ch & 0xFFFF;
//	if (key <= 0x7F) return (char) ch;
//	byte [] buffer;
//	if (key <= 0xFF) {
//		buffer = new byte [1];
//		buffer [0] = (byte) key;
//	} else {
//		buffer = new byte [2];
//		buffer [0] = (byte) ((key >> 8) & 0xFF);
//		buffer [1] = (byte) (key & 0xFF);
//	}
//	char [] unicode = new char [1];
//	int cp = codePage != 0 ? codePage : OS.CP_ACP;
//	int count = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, buffer.length, unicode, 1);
//	if (count == 0) return 0;
//	return unicode [0];
//}
//
//int messageProc (int hwnd, int msg, int wParam, int lParam) {
//	switch (msg) {
//		case SWT_KEYMSG:
//			boolean consumed = false;
//			MSG keyMsg = new MSG ();
//			OS.MoveMemory (keyMsg, lParam, MSG.sizeof);
//			Control control = findControl (keyMsg.hwnd);
//			if (control != null) {
//				keyMsg.hwnd = control.handle;
//				int flags = OS.PM_REMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
//				do {
//					if (!(consumed |= filterMessage (keyMsg))) {
//						OS.TranslateMessage (keyMsg);
//						consumed |= OS.DispatchMessage (keyMsg) == 1;	
//					}
//				} while (OS.PeekMessage (keyMsg, keyMsg.hwnd, OS.WM_KEYFIRST, OS.WM_KEYLAST, flags));
//			}
//			if (consumed) {
//				int hHeap = OS.GetProcessHeap ();
//				OS.HeapFree (hHeap, 0, lParam);
//			} else {
//				OS.PostMessage (embeddedHwnd, SWT_KEYMSG, wParam, lParam);
//			}
//			return 0;
//		case SWT_TRAYICONMSG:
//			if (tray != null) {
//				TrayItem [] items = tray.items;
//				for (int i=0; i<items.length; i++) {
//					TrayItem item = items [i];
//					if (item != null && item.id == wParam) {
//						return item.messageProc (hwnd, msg, wParam, lParam);
//					}
//				}
//			}
//			return 0;
//		case OS.WM_ACTIVATEAPP:
//			/*
//			* Feature in Windows.  When multiple shells are
//			* disabled and one of the shells has an enabled
//			* dialog child and the user selects a disabled
//			* shell that does not have the enabled dialog
//			* child using the Task bar, Windows brings the
//			* disabled shell to the front.  As soon as the
//			* user clicks on the disabled shell, the enabled
//			* dialog child comes to the front.  This behavior
//			* is unspecified and seems strange.  Normally, a
//			* disabled shell is frozen on the screen and the
//			* user cannot change the z-order by clicking with
//			* the mouse.  The fix is to look for WM_ACTIVATEAPP
//			* and force the enabled dialog child to the front.
//			* This is typically what the user is expecting.
//			* 
//			* NOTE: If the modal shell is disabled for any reason,
//			* it should not be brought to the front.
//			*/
//			if (wParam != 0) {
//				if (modalDialogShell != null && modalDialogShell.isDisposed ()) modalDialogShell = null;
//				Shell modal = modalDialogShell != null ? modalDialogShell : getModalShell ();
//				if (modal != null) {
//					int hwndModal = modal.handle;
//					if (OS.IsWindowEnabled (hwndModal)) {
//						modal.bringToTop ();
//					}
//					int hwndPopup = OS.GetLastActivePopup (hwndModal);
//					if (hwndPopup != 0 && hwndPopup != modal.handle) {
//						if (getControl (hwndPopup) == null) {
//							if (OS.IsWindowEnabled (hwndPopup)) {
//								OS.SetActiveWindow (hwndPopup);
//							}
//						}
//					}
//				}
//			}
//			break;
//		case OS.WM_ENDSESSION:
//			if (wParam != 0) {
//				dispose ();
//				/*
//				* When the session is ending, no SWT program can continue
//				* to run.  In order to avoid running code after the display
//				* has been disposed, exit from Java.
//				*/
//				System.exit (0);
//			}
//			break;
//		case OS.WM_QUERYENDSESSION:
//			Event event = new Event ();
//			sendEvent (SWT.Close, event);
//			if (!event.doit) return 0;
//			break;
//		case OS.WM_SETTINGCHANGE:
//			updateFont ();
//			break;
//		case OS.WM_TIMER:
//			runTimer (wParam);
//			break;
//	}
//	if (msg == SWT_TASKBARCREATED) {
//		if (tray != null) {
//			TrayItem [] items = tray.items;
//			for (int i=0; i<items.length; i++) {
//				TrayItem item = items [i];
//				if (item != null) item.recreate ();
//			}
//		}
//	}
//	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
//}
//
//int monitorEnumProc (int hmonitor, int hdc, int lprcMonitor, int dwData) {
//	if (monitorCount >= monitors.length) {
//		Monitor[] newMonitors = new Monitor [monitors.length + 4];
//		System.arraycopy (monitors, 0, newMonitors, 0, monitors.length);
//		monitors = newMonitors;
//	}
//	MONITORINFO lpmi = new MONITORINFO ();
//	lpmi.cbSize = MONITORINFO.sizeof;
//	OS.GetMonitorInfo (hmonitor, lpmi);
//	Monitor monitor = new Monitor ();
//	monitor.handle = hmonitor;
//	monitor.x = lpmi.rcMonitor_left;
//	monitor.y = lpmi.rcMonitor_top;
//	monitor.width = lpmi.rcMonitor_right - lpmi.rcMonitor_left;
//	monitor.height = lpmi.rcMonitor_bottom - lpmi.rcMonitor_top;
//	monitor.clientX = lpmi.rcWork_left;
//	monitor.clientY = lpmi.rcWork_top;
//	monitor.clientWidth = lpmi.rcWork_right - lpmi.rcWork_left;
//	monitor.clientHeight = lpmi.rcWork_bottom - lpmi.rcWork_top;
//	monitors [monitorCount++] = monitor;
//	return 1;
//}
//
//int msgFilterProc (int code, int wParam, int lParam) {
//	if (!ignoreMsgFilter) {
//		if (code >= 0) {
//			OS.MoveMemory (hookMsg, lParam, MSG.sizeof);
//			if (hookMsg.message == OS.WM_NULL) {
//				if (runAsyncMessages (false)) wakeThread ();
//			}
//		}
//	}
//	return OS.CallNextHookEx (filterHook, code, wParam, lParam);
//}
//
//int numpadKey (int key) {
//	switch (key) {
//		case OS.VK_NUMPAD0:	return '0';
//		case OS.VK_NUMPAD1:	return '1';
//		case OS.VK_NUMPAD2:	return '2';
//		case OS.VK_NUMPAD3:	return '3';
//		case OS.VK_NUMPAD4:	return '4';
//		case OS.VK_NUMPAD5:	return '5';
//		case OS.VK_NUMPAD6:	return '6';
//		case OS.VK_NUMPAD7:	return '7';
//		case OS.VK_NUMPAD8:	return '8';
//		case OS.VK_NUMPAD9:	return '9';
//		case OS.VK_MULTIPLY:	return '*';
//		case OS.VK_ADD: 		return '+';
//		case OS.VK_SEPARATOR:	return '\0';
//		case OS.VK_SUBTRACT:	return '-';
//		case OS.VK_DECIMAL:	return '.';
//		case OS.VK_DIVIDE:		return '/';
//	}
//	return 0;
//}

/**
 * Generate a low level system event.
 * 
 * <code>post</code> is used to generate low level keyboard
 * and mouse events. The intent is to enable automated UI
 * testing by simulating the input from the user.  Most
 * SWT applications should never need to call this method.
 * <p>
 * Note that this operation can fail when the operating system
 * fails to generate the event for any reason.  For example,
 * this can happen when there is no such key or mouse button
 * or when the system event queue is full.
 * </p>
 * <p>
 * <b>Event Types:</b>
 * <p>KeyDown, KeyUp
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type KeyDown or KeyUp</li>
 * <p> Either one of:
 * <li>(in) character a character that corresponds to a keyboard key</li>
 * <li>(in) keyCode the key code of the key that was typed,
 *          as defined by the key code constants in class <code>SWT</code></li>
 * </ul>
 * <p>MouseDown, MouseUp</p>
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type MouseDown or MouseUp
 * <li>(in) button the button that is pressed or released
 * </ul>
 * <p>MouseMove</p>
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type MouseMove
 * <li>(in) x the x coordinate to move the mouse pointer to in screen coordinates
 * <li>(in) y the y coordinate to move the mouse pointer to in screen coordinates
 * </ul>
 * </dl>
 * 
 * @param event the event to be generated
 * 
 * @return true if the event was generated or false otherwise
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 * 
 */
public boolean post (Event event) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
  try {
  	int type = event.type;
  	switch (type) {
  		case SWT.KeyDown:
        new Robot().keyPress(untranslateKey (event.keyCode));
        return true;
  		case SWT.KeyUp:
        new Robot().keyRelease(untranslateKey (event.keyCode));
        return true;
      case SWT.MouseMove: 
        new Robot().mouseMove(event.x, event.y);
        return true;
  		case SWT.MouseDown:
  		case SWT.MouseUp: {
        int buttons;
        switch (event.button) {
          case 1: buttons = java.awt.event.InputEvent.BUTTON1_DOWN_MASK; break;
          case 2: buttons = java.awt.event.InputEvent.BUTTON2_DOWN_MASK; break;
          case 3: buttons = java.awt.event.InputEvent.BUTTON3_DOWN_MASK; break;
          default: return false;
        }
        if(type == SWT.MouseDown) {
          new Robot().mousePress(buttons);
        } else {
          new Robot().mouseRelease(buttons);
        }
        return true;
  		}
  	} 
  } catch(Exception e) {
    return false;
  }
	return false;
}

void postEvent (final Event event) {
	/*
	* Place the event at the end of the event queue.
	* This code is always called in the Display's
	* thread so it must be re-enterant but does not
	* need to be synchronized.
	*/
	if (eventQueue == null) eventQueue = new Event [4];
	int index = 0;
	int length = eventQueue.length;
	while (index < length) {
		if (eventQueue [index] == null) break;
		index++;
	}
	if (index == length) {
		Event [] newQueue = new Event [length + 4];
		System.arraycopy (eventQueue, 0, newQueue, 0, length);
		eventQueue = newQueue;
	}
	eventQueue [index] = event;
}

/**
 * Reads an event from the operating system's event queue,
 * dispatches it appropriately, and returns <code>true</code>
 * if there is potentially more work to do, or <code>false</code>
 * if the caller can sleep until another event is placed on
 * the event queue.
 * <p>
 * In addition to checking the system event queue, this method also
 * checks if any inter-thread messages (created by <code>syncExec()</code>
 * or <code>asyncExec()</code>) are waiting to be processed, and if
 * so handles them before returning.
 * </p>
 *
 * @return <code>false</code> if the caller can sleep upon return from this method
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_FAILED_EXEC - if an exception occurred while running an inter-thread message</li>
 * </ul>
 *
 * @see #sleep
 * @see #wake
 */
public boolean readAndDispatch () {
	checkDevice ();
  synchronized(UI_LOCK) {
    if(exclusiveSectionCount == 0) {
      return false;
    }
    try {
      UI_LOCK.notify();
      UI_LOCK.wait();
    } catch(Exception e) {
    }
  }
//	drawMenuBars ();
	runPopups ();
//	if (OS.PeekMessage (msg, 0, 0, 0, OS.PM_REMOVE)) {
//		if (!filterMessage (msg)) {
//			OS.TranslateMessage (msg);
//			OS.DispatchMessage (msg);
//		}
		runDeferredEvents ();
//		return true;
//	}
	/*boolean result =*/ runAsyncMessages (false);
  return true;
}

static synchronized void register (Display display) {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] == null) {
			Displays [i] = display;
			return;
		}
	}
	Display [] newDisplays = new Display [Displays.length + 4];
	System.arraycopy (Displays, 0, newDisplays, 0, Displays.length);
	newDisplays [Displays.length] = display;
	Displays = newDisplays;
}

/**
 * Releases any internal resources back to the operating
 * system and clears all fields except the device handle.
 * <p>
 * Disposes all shells which are currently open on the display. 
 * After this method has been invoked, all related related shells
 * will answer <code>true</code> when sent the message
 * <code>isDisposed()</code>.
 * </p><p>
 * When a device is destroyed, resources that were acquired
 * on behalf of the programmer need to be returned to the
 * operating system.  For example, if the device allocated a
 * font to be used as the system font, this font would be
 * freed in <code>release</code>.  Also,to assist the garbage
 * collector and minimize the amount of memory that is not
 * reclaimed when the programmer keeps a reference to a
 * disposed device, all fields except the handle are zero'd.
 * The handle is needed by <code>destroy</code>.
 * </p>
 * This method is called before <code>destroy</code>.
 * 
 * @see #dispose
 * @see #destroy
 */
protected void release () {
	sendEvent (SWT.Dispose, new Event ());
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.dispose ();
	}
	if (tray != null) tray.dispose ();
	tray = null;
	while (readAndDispatch ()) {}
	if (disposeList != null) {
    for(Iterator it = disposeList.iterator(); it.hasNext(); ) {
      ((Runnable)it.next()).run();
    }
//		for (int i=0; i<disposeList.length; i++) {
//			if (disposeList [i] != null) disposeList [i].run ();
//		}
	}
	disposeList = null;
	synchronizer.releaseSynchronizer ();
	synchronizer = null;
	releaseDisplay ();
	super.release ();
}

void releaseDisplay () {
//	if (embeddedHwnd != 0) {
//		OS.PostMessage (embeddedHwnd, SWT_DESTROY, 0, 0);
//	}
//
//	/* Unhook the message hook */
//	if (!OS.IsWinCE) {
//		if (msgHook != 0) OS.UnhookWindowsHookEx (msgHook);
//		msgHook = 0;
//	}
//
//	/* Unhook the filter hook */
//	if (!OS.IsWinCE) {
//		if (filterHook != 0) OS.UnhookWindowsHookEx (filterHook);
//		filterHook = 0;
//		msgFilterCallback.dispose ();
//		msgFilterCallback = null;
//		msgFilterProc = 0;
//	}
//	
//	/* Unhook the idle hook */
//	if (!OS.IsWinCE) {
//		if (idleHook != 0) OS.UnhookWindowsHookEx (idleHook);
//		idleHook = 0;
//		foregroundIdleCallback.dispose ();
//		foregroundIdleCallback = null;
//		foregroundIdleProc = 0;
//	}
//	
//	/* Destroy the message only HWND */
//	if (hwndMessage != 0) OS.DestroyWindow (hwndMessage);
//	hwndMessage = 0;
//	messageCallback.dispose ();
//	messageCallback = null;
//	messageProc = 0;
//	
//	/* Unregister the SWT window class */
//	int hHeap = OS.GetProcessHeap ();
//	int hInstance = OS.GetModuleHandle (null);
//	WNDCLASS lpWndClass = new WNDCLASS ();
//	OS.GetClassInfo (0, windowClass, lpWndClass);
//	OS.UnregisterClass (windowClass, hInstance);
//	OS.HeapFree (hHeap, 0, lpWndClass.lpszClassName);
//	
//	/* Unregister the SWT drop shadow window class */
//	OS.GetClassInfo (0, windowShadowClass, lpWndClass);
//	OS.UnregisterClass (windowShadowClass, hInstance);
//	OS.HeapFree (hHeap, 0, lpWndClass.lpszClassName);
//	windowClass = windowShadowClass = null;
//	windowCallback.dispose ();
//	windowCallback = null;
//	windowProc = 0;
//	
//	/* Release the system fonts */
//	if (systemFonts != null) {
//		for (int i=0; i<systemFonts.length; i++) {
//			if (systemFonts [i] != 0) OS.DeleteObject (systemFonts [i]);
//		}
//	}
//	systemFonts = null;
//	
//	/* Release the System Images */
//	if (errorIcon != 0) OS.DestroyIcon (errorIcon);
//	if (infoIcon != 0) OS.DestroyIcon (infoIcon);
//	if (questionIcon != 0) OS.DestroyIcon (questionIcon);
//	if (warningIcon != 0) OS.DestroyIcon (warningIcon);
	errorIcon = infoIcon = questionIcon = warningIcon = null;
//	
//	/* Release the System Cursors */
//	for (int i = 0; i < cursors.length; i++) {
//		if (cursors [i] != null) cursors [i].dispose ();
//	}
//	cursors = null;
//
//	/* Release Custom Colors for ChooseColor */
//	if (lpCustColors != 0) OS.HeapFree (hHeap, 0, lpCustColors);
//	lpCustColors = 0;
//	
//	/* Uninitialize OLE */
//	if (!OS.IsWinCE) {
//		OS.OleUninitialize ();
//	}
//	
//	/* Release references */
//	thread = null;
//	msg = null;
//	keyboard = null;
//	modalDialogShell = null;
//	modalShells = null;
//	data = null;
//	keys = null;
//	values = null;
	bars = popups = null;
//	indexTable = null;
//	controlTable = null;
//	lastHittestControl = null;
//	imageList = toolImageList = toolHotImageList = toolDisabledImageList = null;
}

//void releaseImageList (ImageList list) {
//	int i = 0;
//	int length = imageList.length; 
//	while (i < length) {
//		if (imageList [i] == list) {
//			if (list.removeRef () > 0) return;
//			list.dispose ();
//			System.arraycopy (imageList, i + 1, imageList, i, --length - i);
//			imageList [length] = null;
//			for (int j=0; j<length; j++) {
//				if (imageList [j] != null) return;
//			}
//			imageList = null;
//			return;
//		}
//		i++;
//	}
//}
//
//void releaseToolImageList (ImageList list) {
//	int i = 0;
//	int length = toolImageList.length; 
//	while (i < length) {
//		if (toolImageList [i] == list) {
//			if (list.removeRef () > 0) return;
//			list.dispose ();
//			System.arraycopy (toolImageList, i + 1, toolImageList, i, --length - i);
//			toolImageList [length] = null;
//			for (int j=0; j<length; j++) {
//				if (toolImageList [j] != null) return;
//			}
//			toolImageList = null;
//			return;
//		}
//		i++;
//	}
//}
//
//void releaseToolHotImageList (ImageList list) {
//	int i = 0;
//	int length = toolHotImageList.length; 
//	while (i < length) {
//		if (toolHotImageList [i] == list) {
//			if (list.removeRef () > 0) return;
//			list.dispose ();
//			System.arraycopy (toolHotImageList, i + 1, toolHotImageList, i, --length - i);
//			toolHotImageList [length] = null;
//			for (int j=0; j<length; j++) {
//				if (toolHotImageList [j] != null) return;
//			}
//			toolHotImageList = null;
//			return;
//		}
//		i++;
//	}
//}
//
//void releaseToolDisabledImageList (ImageList list) {
//	int i = 0;
//	int length = toolDisabledImageList.length; 
//	while (i < length) {
//		if (toolDisabledImageList [i] == list) {
//			if (list.removeRef () > 0) return;
//			list.dispose ();
//			System.arraycopy (toolDisabledImageList, i + 1, toolDisabledImageList, i, --length - i);
//			toolDisabledImageList [length] = null;
//			for (int j=0; j<length; j++) {
//				if (toolDisabledImageList [j] != null) return;
//			}
//			toolDisabledImageList = null;
//			return;
//		}
//		i++;
//	}
//}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs anywhere in
 * a widget.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #addFilter
 * @see #addListener
 * 
 * @since 3.0
 */
public void removeFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) return;
	filterTable.unhook (eventType, listener);
	if (filterTable.size () == 0) filterTable = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see #addListener
 * 
 * @since 2.0 
 */
public void removeListener (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
}

void removeBar (Menu menu) {
	if (bars == null) return;
	for (int i=0; i<bars.length; i++) {
		if (bars [i] == menu) {
			bars [i] = null;
			return;
		}
	}
}

Control removeControl (Component handle) {
	if (handle == null) return null;
  return (Control)componentToControlMap.remove(handle);
//	Control control = null;
//	int index;
//	if (USE_PROPERTY) {
//		index = OS.RemoveProp (handle, SWT_OBJECT_INDEX) - 1;
//	} else {
//		index = OS.GetWindowLong (handle, OS.GWL_USERDATA) - 1;
//	}
//	if (0 <= index && index < controlTable.length) {
//		control = controlTable [index];
//		controlTable [index] = null;
//		indexTable [index] = freeSlot;
//		freeSlot = index;
//		if (!USE_PROPERTY) {
//			OS.SetWindowLong (handle, OS.GWL_USERDATA, 0);
//		}
//	}
//	return control;
}

void removeMenuItem (MenuItem item) {
  menuItemsList.remove(item);
}

void removePopup (Menu menu) {
	if (popups == null) return;
	for (int i=0; i<popups.length; i++) {
		if (popups [i] == menu) {
			popups [i] = null;
			return;
		}
	}
}

boolean runAsyncMessages (boolean all) {
	return synchronizer.runAsyncMessages (all);
}

boolean runDeferredEvents () {
	/*
	* Run deferred events.  This code is always
	* called in the Display's thread so it must
	* be re-enterant but need not be synchronized.
	*/
	while (eventQueue != null) {
		
		/* Take an event off the queue */
		Event event = eventQueue [0];
		if (event == null) break;
		int length = eventQueue.length;
		System.arraycopy (eventQueue, 1, eventQueue, 0, --length);
		eventQueue [length] = null;

		/* Run the event */
		Widget widget = event.widget;
		if (widget != null && !widget.isDisposed ()) {
			Widget item = event.item;
			if (item == null || !item.isDisposed ()) {
				widget.sendEvent (event);
			}
		}

		/*
		* At this point, the event queue could
		* be null due to a recursive invokation
		* when running the event.
		*/
	}

	/* Clear the queue */
	eventQueue = null;
	return true;
}

boolean runPopups () {
	if (popups == null) return false;
	boolean result = false;
	while (popups != null) {
		Menu menu = popups [0];
		if (menu == null) break;
		int length = popups.length;
		System.arraycopy (popups, 1, popups, 0, --length);
		popups [length] = null;
//    managedEventQueue.processPostedRunnableList();
		runDeferredEvents ();
    menu.setVisible (true);
//		menu._setVisible (true);
		result = true;
	}
	popups = null;
	return result;
}

//boolean runTimer (int id) {
//	if (timerList != null && timerIds != null) {
//		int index = 0;
//		while (index <timerIds.length) {
//			if (timerIds [index] == id) {
//				OS.KillTimer (hwndMessage, timerIds [index]);
//				timerIds [index] = 0;
//				Runnable runnable = timerList [index];
//				timerList [index] = null;
//				if (runnable != null) runnable.run ();
//				return true;
//			}
//			index++;
//		}
//	}
//	return false;
//}

void sendEvent (int eventType, Event event) {
	if (eventTable == null && filterTable == null) {
		return;
	}
	if (event == null) event = new Event ();
	event.display = this;
	event.type = eventType;
	if (event.time == 0) event.time = getLastEventTime ();
	if (!filterEvent (event)) {
		if (eventTable != null) eventTable.sendEvent (event);
	}
}

/**
 * Sets the location of the on-screen pointer relative to the top left corner
 * of the screen.  <b>Note: It is typically considered bad practice for a
 * program to move the on-screen pointer location.</b>
 *
 * @param x the new x coordinate for the cursor
 * @param y the new y coordinate for the cursor
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1
 */
public void setCursorLocation (int x, int y) {
	checkDevice ();
  try {
    new Robot().mouseMove(x, y);
  } catch(Exception e) {}
}

/**
 * Sets the location of the on-screen pointer relative to the top left corner
 * of the screen.  <b>Note: It is typically considered bad practice for a
 * program to move the on-screen pointer location.</b>
 *
 * @param point new position
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setCursorLocation (Point point) {
	checkDevice ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	setCursorLocation (point.x, point.y);
}

/**
 * Sets the application defined property of the receiver
 * with the specified name to the given argument.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the display is disposed
 * of, it is the application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param key the name of the property
 * @param value the new value for the property
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getData(String)
 * @see #disposeExec(Runnable)
 */
public void setData (String key, Object value) {
	checkDevice ();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	/* Remove the key/value pair */
	if (value == null) {
		if (keys == null) return;
		int index = 0;
		while (index < keys.length && !keys [index].equals (key)) index++;
		if (index == keys.length) return;
		if (keys.length == 1) {
			keys = null;
			values = null;
		} else {
			String [] newKeys = new String [keys.length - 1];
			Object [] newValues = new Object [values.length - 1];
			System.arraycopy (keys, 0, newKeys, 0, index);
			System.arraycopy (keys, index + 1, newKeys, index, newKeys.length - index);
			System.arraycopy (values, 0, newValues, 0, index);
			System.arraycopy (values, index + 1, newValues, index, newValues.length - index);
			keys = newKeys;
			values = newValues;
		}
		return;
	}
	
	/* Add the key/value pair */
	if (keys == null) {
		keys = new String [] {key};
		values = new Object [] {value};
		return;
	}
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) {
			values [i] = value;
			return;
		}
	}
	String [] newKeys = new String [keys.length + 1];
	Object [] newValues = new Object [values.length + 1];
	System.arraycopy (keys, 0, newKeys, 0, keys.length);
	System.arraycopy (values, 0, newValues, 0, values.length);
	newKeys [keys.length] = key;
	newValues [values.length] = value;
	keys = newKeys;
	values = newValues;
}

/**
 * Sets the application defined, display specific data
 * associated with the receiver, to the argument.
 * The <em>display specific data</em> is a single,
 * unnamed field that is stored with every display. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the display specific data needs to
 * be notified when the display is disposed of, it is the
 * application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param data the new display specific data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getData()
 * @see #disposeExec(Runnable)
 */
public void setData (Object data) {
	checkDevice ();
	this.data = data;
}

/**
 * On platforms which support it, sets the application name
 * to be the argument. On Motif, for example, this can be used
 * to set the name used for resource lookup.
 *
 * @param name the new app name
 */
public static void setAppName (String name) {
	/* Do nothing */
}

//void setModalDialogShell (Shell modalDailog) {
//	if (modalDialogShell != null && modalDialogShell.isDisposed ()) modalDialogShell = null;
//	this.modalDialogShell = modalDailog;
//	Shell [] shells = getShells ();
//	for (int i=0; i<shells.length; i++) shells [i].updateModal ();
//}
//
//void setModalShell (Shell shell) {
//	if (modalShells == null) modalShells = new Shell [4];
//	int index = 0, length = modalShells.length;
//	while (index < length) {
//		if (modalShells [index] == shell) return;
//		if (modalShells [index] == null) break;
//		index++;
//	}
//	if (index == length) {
//		Shell [] newModalShells = new Shell [length + 4];
//		System.arraycopy (modalShells, 0, newModalShells, 0, length);
//		modalShells = newModalShells;
//	}
//	modalShells [index] = shell;
//	Shell [] shells = getShells ();
//	for (int i=0; i<shells.length; i++) shells [i].updateModal ();
//}

/**
 * Sets the synchronizer used by the display to be
 * the argument, which can not be null.
 *
 * @param synchronizer the new synchronizer for the display (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the synchronizer is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_FAILED_EXEC - if an exception occurred while running an inter-thread message</li>
 * </ul>
 */
public void setSynchronizer (Synchronizer synchronizer) {
	checkDevice ();
	if (synchronizer == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (this.synchronizer != null) {
		this.synchronizer.runAsyncMessages(true);
	}
	this.synchronizer = synchronizer;
}

//int shiftedKey (int key) {
//	if (OS.IsWinCE) return 0;
//	
//	/* Clear the virtual keyboard and press the shift key */
//	for (int i=0; i<keyboard.length; i++) keyboard [i] = 0;
//	keyboard [OS.VK_SHIFT] |= 0x80;
//
//	/* Translate the key to ASCII or UNICODE using the virtual keyboard */
//	if (OS.IsUnicode) {
//		char [] result = new char [1];
//		if (OS.ToUnicode (key, key, keyboard, result, 1, 0) == 1) return result [0];
//	} else {
//		short [] result = new short [1];
//		if (OS.ToAscii (key, key, keyboard, result, 0) == 1) return result [0];
//	}
//	return 0;
//}

/**
 * Causes the user-interface thread to <em>sleep</em> (that is,
 * to be put in a state where it does not consume CPU cycles)
 * until an event is received or it is otherwise awakened.
 *
 * @return <code>true</code> if an event requiring dispatching was placed on the queue.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #wake
 */
public boolean sleep () {
	checkDevice ();
  synchronized(UI_LOCK) {
    if(exclusiveSectionCount == 0) {
      try {
        UI_LOCK.wait();
      } catch(Exception e) {
      }
    }
    return exclusiveSectionCount > 0;
  }
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next 
 * reasonable opportunity. The thread which calls this method
 * is suspended until the runnable completes.
 *
 * @param runnable code to run on the user-interface thread.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_FAILED_EXEC - if an exception occured when executing the runnable</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #asyncExec
 */
public void syncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.syncExec (runnable);
}

//int systemFont () {
//	int hFont = 0;
//	if (systemFonts != null) {
//		int length = systemFonts.length;
//		if (length != 0) hFont = systemFonts [length - 1];
//	}
//	if (hFont == 0) hFont = OS.GetStockObject (OS.DEFAULT_GUI_FONT);
//	if (hFont == 0) hFont = OS.GetStockObject (OS.SYSTEM_FONT);
//	return hFont;
//}

protected Vector timerList = new Vector();

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread after the specified
 * number of milliseconds have elapsed. If milliseconds is less
 * than zero, the runnable is not executed.
 *
 * @param milliseconds the delay before running the runnable
 * @param runnable code to run on the user-interface thread
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the runnable is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #asyncExec
 */
public void timerExec (final int milliseconds, final Runnable runnable) {
	checkDevice ();
	if (runnable == null) error (SWT.ERROR_NULL_ARGUMENT);
  if (milliseconds < 0) {
    timerList.remove(runnable);
    return;
  }
  timerList.add(runnable);
  new Thread() {
    public void run() {
      try {
        sleep(milliseconds);
        boolean isRemoved = timerList.remove(runnable);
        if(isRemoved) {
          SwingUtilities.invokeLater(runnable);
        }
      } catch(Exception e) {}
    }
  }.start();
}

//boolean translateAccelerator (MSG msg, Control control) {
//	accelKeyHit = true;
//	boolean result = control.translateAccelerator (msg);
//	accelKeyHit = false;
//	return result;
//}

static int translateKey (int key) {
	for (int i=0; i<KeyTable.length; i++) {
		if (KeyTable [i] [0] == key) return KeyTable [i] [1];
	}
	return 0;
}

//boolean translateMnemonic (MSG msg, Control control) {
//	switch (msg.message) {
//		case OS.WM_CHAR:
//		case OS.WM_SYSCHAR:
//			return control.translateMnemonic (msg);
//	}
//	return false;
//}
//
//boolean translateTraversal (MSG msg, Control control) {
//	switch (msg.message) {
//		case OS.WM_KEYDOWN:
//			switch (msg.wParam) {
//				case OS.VK_RETURN:
//				case OS.VK_ESCAPE:
//				case OS.VK_TAB:
//				case OS.VK_UP:
//				case OS.VK_DOWN:
//				case OS.VK_LEFT:
//				case OS.VK_RIGHT:
//				case OS.VK_PRIOR:
//				case OS.VK_NEXT:
//					return control.translateTraversal (msg);
//			}
//			break;
//		case OS.WM_SYSKEYDOWN:
//			switch (msg.wParam) {
//				case OS.VK_MENU:
//					return control.translateTraversal (msg);
//			}
//			break;
//	}
//	return false;
//}

static int untranslateKey (int key) {
	for (int i=0; i<KeyTable.length; i++) {
		if (KeyTable [i] [1] == key) return KeyTable [i] [0];
	}
	return 0;
}

/**
 * Forces all outstanding paint requests for the display
 * to be processed before this method returns.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Control#update()
 */
public void update() {
	checkDevice ();
	Shell[] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
//    if (!shell.isDisposed ()) shell.update (true);
		if (!shell.isDisposed ()) shell.update ();
	}
}

//void updateFont () {
//	if (OS.IsWinCE) return;
//	Font oldFont = getSystemFont ();
//	int systemFont = 0;
//	NONCLIENTMETRICS info = OS.IsUnicode ? (NONCLIENTMETRICS) new NONCLIENTMETRICSW () : new NONCLIENTMETRICSA ();
//	info.cbSize = NONCLIENTMETRICS.sizeof;
//	if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
//		systemFont = OS.CreateFontIndirect (OS.IsUnicode ? (LOGFONT) ((NONCLIENTMETRICSW)info).lfMessageFont : ((NONCLIENTMETRICSA)info).lfMessageFont);
//	}
//	if (systemFont == 0) systemFont = OS.GetStockObject (OS.DEFAULT_GUI_FONT);
//	if (systemFont == 0) systemFont = OS.GetStockObject (OS.SYSTEM_FONT);
//	if (systemFont == 0) return;
//	int length = systemFonts == null ? 0 : systemFonts.length;
//	int [] newFonts = new int [length + 1];
//	if (systemFonts != null) {
//		System.arraycopy (systemFonts, 0, newFonts, 0, length);
//	}
//	newFonts [length] = systemFont;
//	systemFonts = newFonts;
//	Font newFont = getSystemFont ();
//	Shell [] shells = getShells ();
//	for (int i=0; i<shells.length; i++) {
//		Shell shell = shells [i];
//		if (!shell.isDisposed ()) {
//			shell.updateFont (oldFont, newFont);
//		}
//	}
//}

protected final Object UI_LOCK = new Object();

/**
 * If the receiver's user-interface thread was <code>sleep</code>'ing, 
 * causes it to be awakened and start running again. Note that this
 * method may be called from any thread.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #sleep
 */
public void wake () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	if (thread == Thread.currentThread ()) return;
  synchronized(UI_LOCK) {
    UI_LOCK.notify();
  }
}

//void wakeThread () {
//	if (OS.IsWinCE) {
//		OS.PostMessage (hwndMessage, OS.WM_NULL, 0, 0);
//	} else {
//		OS.PostThreadMessage (threadId, OS.WM_NULL, 0, 0);
//	}
//}
//
///*
// * Returns a single character, converted from the wide
// * character set (WCS) used by Java to the specified
// * multi-byte character set used by the operating system
// * widgets.
// *
// * @param ch the WCS character
// * @param codePage the code page used to convert the character
// * @return the MBCS character
// */
//static int wcsToMbcs (char ch, int codePage) {
//	if (OS.IsUnicode) return ch;
//	if (ch <= 0x7F) return ch;
//	TCHAR buffer = new TCHAR (codePage, ch, false);
//	return buffer.tcharAt (0);
//}
//
///*
// * Returns a single character, converted from the wide
// * character set (WCS) used by Java to the default
// * multi-byte character set used by the operating system
// * widgets.
// *
// * @param ch the WCS character
// * @return the MBCS character
// */
//static int wcsToMbcs (char ch) {
//	return wcsToMbcs (ch, 0);
//}
//
//int windowProc (int hwnd, int msg, int wParam, int lParam) {
//	int index;
//	if (USE_PROPERTY) {
//		index = OS.GetProp (hwnd, SWT_OBJECT_INDEX) - 1;
//	} else {
//		index = OS.GetWindowLong (hwnd, OS.GWL_USERDATA) - 1;
//	}
//	if (0 <= index && index < controlTable.length) {
//		Control control = controlTable [index];
//		if (control != null) {
//			return control.windowProc (hwnd, msg, wParam, lParam);
//		}
//	}
//	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
//}
//
//static String withCrLf (String string) {
//
//	/* If the string is empty, return the string. */
//	int length = string.length ();
//	if (length == 0) return string;
//	
//	/*
//	* Check for an LF or CR/LF and assume the rest of
//	* the string is formated that way.  This will not
//	* work if the string contains mixed delimiters.
//	*/
//	int i = string.indexOf ('\n', 0);
//	if (i == -1) return string;
//	if (i > 0 && string.charAt (i - 1) == '\r') {
//		return string;
//	}
//
//	/*
//	* The string is formatted with LF.  Compute the
//	* number of lines and the size of the buffer
//	* needed to hold the result
//	*/
//	i++;	
//	int count = 1;
//	while (i < length) {
//		if ((i = string.indexOf ('\n', i)) == -1) break;
//		count++; i++;
//	}
//	count += length;
//
//	/* Create a new string with the CR/LF line terminator. */
//	i = 0;
//	StringBuffer result = new StringBuffer (count);
//	while (i < length) {
//		int j = string.indexOf ('\n', i);
//		if (j == -1) j = length;
//		result.append (string.substring (i, j));
//		if ((i = j) < length) {
//			result.append ("\r\n"); //$NON-NLS-1$
//			i++;
//		}
//	}
//	return result.toString ();
//}

protected int exclusiveSectionCount = 0;

protected void startExclusiveSection() {
  if(!SwingUtilities.isEventDispatchThread()) {
    exclusiveSectionCount++;
    return;
//    throw new IllegalStateException("This call must be done from the Swing Event Dispatch Thread!");
  }
  synchronized(UI_LOCK) {
    exclusiveSectionCount++;
    if(exclusiveSectionCount == 1) {
      try {
        wake();
        UI_LOCK.wait();
      } catch(Exception e) {
      }
    }
  }
}

protected void stopExclusiveSection() {
  if(!SwingUtilities.isEventDispatchThread()) {
    exclusiveSectionCount--;
    return;
//    throw new IllegalStateException("This call must be done from the Swing Event Dispatch Thread!");
  }
  synchronized(UI_LOCK) {
    exclusiveSectionCount--;
    if(exclusiveSectionCount == 0) {
      UI_LOCK.notify();
    }
  }
}

}
