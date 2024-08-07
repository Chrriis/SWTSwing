/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.awt.Container;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.swing.CDateTime;
import org.eclipse.swt.internal.swing.Utils;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify date
 * or time values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DATE, TIME, CALENDAR, SHORT, MEDIUM, LONG, DROP_DOWN, CALENDAR_WEEKNUMBERS</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DATE, TIME, or CALENDAR may be specified,
 * and only one of the styles SHORT, MEDIUM, or LONG may be specified.
 * The DROP_DOWN style is only valid with the DATE style.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#datetime">DateTime snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.3
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DateTime extends Composite {
	static final int MIN_YEAR = 1752; // Gregorian switchover in North America: September 19, 1752
	static final int MAX_YEAR = 9999;
//	boolean doubleClick, ignoreSelection;
//	SYSTEMTIME lastSystemTime;
//	SYSTEMTIME time = new SYSTEMTIME (); // only used in calendar mode
//	static final long DateTimeProc;
//	static final TCHAR DateTimeClass = new TCHAR (0, OS.DATETIMEPICK_CLASS, true);
//	static final long CalendarProc;
//	static final TCHAR CalendarClass = new TCHAR (0, OS.MONTHCAL_CLASS, true);
//	static {
//		WNDCLASS lpWndClass = new WNDCLASS ();
//		OS.GetClassInfo (0, DateTimeClass, lpWndClass);
//		DateTimeProc = lpWndClass.lpfnWndProc;
//		/*
//		* Feature in Windows.  The date time window class
//		* does not include CS_DBLCLKS.  This means that these
//		* controls will not get double click messages such as
//		* WM_LBUTTONDBLCLK.  The fix is to register a new
//		* window class with CS_DBLCLKS.
//		*
//		* NOTE:  Screen readers look for the exact class name
//		* of the control in order to provide the correct kind
//		* of assistance.  Therefore, it is critical that the
//		* new window class have the same name.  It is possible
//		* to register a local window class with the same name
//		* as a global class.  Since bits that affect the class
//		* are being changed, it is possible that other native
//		* code, other than SWT, could create a control with
//		* this class name, and fail unexpectedly.
//		*/
//		lpWndClass.hInstance = OS.GetModuleHandle (null);
//		lpWndClass.style &= ~OS.CS_GLOBALCLASS;
//		lpWndClass.style |= OS.CS_DBLCLKS;
//		OS.RegisterClass (DateTimeClass, lpWndClass);
//	}
//	static {
//		WNDCLASS lpWndClass = new WNDCLASS ();
//		OS.GetClassInfo (0, CalendarClass, lpWndClass);
//		CalendarProc = lpWndClass.lpfnWndProc;
//		/*
//		* Feature in Windows.  The date time window class
//		* does not include CS_DBLCLKS.  This means that these
//		* controls will not get double click messages such as
//		* WM_LBUTTONDBLCLK.  The fix is to register a new
//		* window class with CS_DBLCLKS.
//		*
//		* NOTE:  Screen readers look for the exact class name
//		* of the control in order to provide the correct kind
//		* of assistance.  Therefore, it is critical that the
//		* new window class have the same name.  It is possible
//		* to register a local window class with the same name
//		* as a global class.  Since bits that affect the class
//		* are being changed, it is possible that other native
//		* code, other than SWT, could create a control with
//		* this class name, and fail unexpectedly.
//		*/
//		lpWndClass.hInstance = OS.GetModuleHandle (null);;
//		lpWndClass.style &= ~OS.CS_GLOBALCLASS;
//		lpWndClass.style |= OS.CS_DBLCLKS;
//		OS.RegisterClass (CalendarClass, lpWndClass);
//	}
//	static final char SINGLE_QUOTE = '\''; //$NON-NLS-1$ short date format may include quoted text
//	static final char DAY_FORMAT_CONSTANT = 'd'; //$NON-NLS-1$ 1-4 lowercase 'd's represent day
//	static final char MONTH_FORMAT_CONSTANT = 'M'; //$NON-NLS-1$ 1-4 uppercase 'M's represent month
//	static final char YEAR_FORMAT_CONSTANT = 'y'; //$NON-NLS-1$ 1-5 lowercase 'y's represent year
//	static final char HOURS_FORMAT_CONSTANT = 'h'; //$NON-NLS-1$ 1-2 upper or lowercase 'h's represent hours
//	static final char MINUTES_FORMAT_CONSTANT = 'm'; //$NON-NLS-1$ 1-2 lowercase 'm's represent minutes
//	static final char SECONDS_FORMAT_CONSTANT = 's'; //$NON-NLS-1$ 1-2 lowercase 's's represent seconds
//	static final char AMPM_FORMAT_CONSTANT = 't'; //$NON-NLS-1$ 1-2 lowercase 't's represent am/pm


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
 * @see SWT#DATE
 * @see SWT#TIME
 * @see SWT#CALENDAR
 * @see SWT#CALENDAR_WEEKNUMBERS
 * @see SWT#SHORT
 * @see SWT#MEDIUM
 * @see SWT#LONG
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public DateTime (Composite parent, int style) {
	super (parent, checkStyle (style));
	if ((this.style & SWT.SHORT) != 0) {
		Utils.notImplemented();
//		String buffer = ((this.style & SWT.DATE) != 0) ? getCustomShortDateFormat() : getCustomShortTimeFormat();
//		TCHAR lpszFormat = new TCHAR (0, buffer, true);
//		OS.SendMessage (handle, OS.DTM_SETFORMAT, 0, lpszFormat);
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the control's value.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed.
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

//@Override
//long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
//	if (handle == 0) return 0;
//	return OS.CallWindowProc (windowProc (), hwnd, msg, wParam, lParam);
//}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style = checkBits (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
	style = checkBits (style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
	if ((style & SWT.DATE) == 0) style &=~ SWT.DROP_DOWN;
	return style;
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

//@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
//	checkWidget ();
//	int width = 0, height = 0;
//	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
//		if ((style & SWT.CALENDAR) != 0) {
//			RECT rect = new RECT ();
//			OS.SendMessage(handle, OS.MCM_GETMINREQRECT, 0, rect);
//			width = rect.right;
//			height = rect.bottom;
//		} else {
//			// customize the style of the drop-down calendar, to get the correct size
//			if ((style & SWT.CALENDAR_WEEKNUMBERS) != 0) {
//				// get current style and add week numbers to the calendar drop-down
//				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//				OS.SendMessage(handle, OS.DTM_SETMCSTYLE, 0, bits | OS.MCS_WEEKNUMBERS);
//			}
//			SIZE size = new SIZE ();
//			OS.SendMessage(handle, OS.DTM_GETIDEALSIZE, 0, size);
//			width = size.cx;
//			height = size.cy;
//			// TODO: Can maybe use DTM_GETDATETIMEPICKERINFO for this
//			int upDownHeight = OS.GetSystemMetrics (OS.SM_CYVSCROLL) + 7;
//			height = Math.max (height, upDownHeight);
//		}
//	}
//	if (width == 0) width = DEFAULT_WIDTH;
//	if (height == 0) height = DEFAULT_HEIGHT;
//	if (wHint != SWT.DEFAULT) width = wHint;
//	if (hHint != SWT.DEFAULT) height = hHint;
//	int border = getBorderWidthInPixels ();
//	width += border * 2;
//	height += border * 2;
//	return new Point (width, height);
//}

@Override
void createHandleInit () {
	super.createHandleInit ();
	state &= ~(CANVAS | THEME_BACKGROUND);
}

protected Container createHandle () {
	return (Container)CDateTime.Factory.newInstance(this, style);
}


/**
 * Returns the receiver's date, or day of the month.
 * <p>
 * The first day of the month is 1, and the last day depends on the month and year.
 * </p>
 *
 * @return a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getDay () {
	checkWidget ();
	Utils.notImplemented();
	return 1;
}

/**
 * Returns the receiver's hours.
 * <p>
 * Hours is an integer between 0 and 23.
 * </p>
 *
 * @return an integer between 0 and 23
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHours () {
	checkWidget ();
//	if ((style & SWT.CALENDAR) != 0) return time.wHour;
	Utils.notImplemented();
	return 0;
}

/**
 * Returns the receiver's minutes.
 * <p>
 * Minutes is an integer between 0 and 59.
 * </p>
 *
 * @return an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinutes () {
	checkWidget ();
//	if ((style & SWT.CALENDAR) != 0) return time.wMinute;
	Utils.notImplemented();
	return 0;
}

/**
 * Returns the receiver's month.
 * <p>
 * The first month of the year is 0, and the last month is 11.
 * </p>
 *
 * @return an integer between 0 and 11
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMonth () {
	checkWidget ();
	Utils.notImplemented();
	return 0;
}

@Override
String getNameText() {
	return (style & SWT.TIME) != 0 ? getHours() + ":" + getMinutes() + ":" + getSeconds()
			: (getMonth() + 1) + "/" + getDay() + "/" + getYear();
}

/**
 * Returns the receiver's seconds.
 * <p>
 * Seconds is an integer between 0 and 59.
 * </p>
 *
 * @return an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSeconds () {
	checkWidget ();
//	if ((style & SWT.CALENDAR) != 0) return time.wSecond;
	Utils.notImplemented();
	return 0;
}

/**
 * Returns the receiver's year.
 * <p>
 * The first year is 1752 and the last year is 9999.
 * </p>
 *
 * @return an integer between 1752 and 9999
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getYear () {
	checkWidget ();
	Utils.notImplemented();
	return 2024;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
//	lastSystemTime = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
 *
 * @param listener the listener which should no longer be notified
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);
}

/**
 * Sets the receiver's year, month, and day in a single operation.
 * <p>
 * This is the recommended way to set the date, because setting the year,
 * month, and day separately may result in invalid intermediate dates.
 * </p>
 *
 * @param year an integer between 1752 and 9999
 * @param month an integer between 0 and 11
 * @param day a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setDate (int year, int month, int day) {
	checkWidget ();
	if (year < MIN_YEAR || year > MAX_YEAR) return;
	Utils.notImplemented();
//	SYSTEMTIME systime = new SYSTEMTIME ();
//	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
//	OS.SendMessage (handle, msg, 0, systime);
//	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
//	systime.wYear = (short)year;
//	systime.wMonth = (short)(month + 1);
//	systime.wDay = (short)day;
//	OS.SendMessage (handle, msg, 0, systime);
//	lastSystemTime = null;
}

/**
 * Sets the receiver's date, or day of the month, to the specified day.
 * <p>
 * The first day of the month is 1, and the last day depends on the month and year.
 * If the specified day is not valid for the receiver's month and year, then it is ignored.
 * </p>
 *
 * @param day a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setDate
 */
public void setDay (int day) {
	checkWidget ();
	Utils.notImplemented();
//	SYSTEMTIME systime = new SYSTEMTIME ();
//	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
//	OS.SendMessage (handle, msg, 0, systime);
//	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
//	systime.wDay = (short)day;
//	OS.SendMessage (handle, msg, 0, systime);
//	lastSystemTime = null;
}

/**
 * Sets the receiver's hours.
 * <p>
 * Hours is an integer between 0 and 23.
 * </p>
 *
 * @param hours an integer between 0 and 23
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHours (int hours) {
	checkWidget ();
	if (hours < 0 || hours > 23) return;
	Utils.notImplemented();
//	SYSTEMTIME systime = new SYSTEMTIME ();
//	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
//	OS.SendMessage (handle, msg, 0, systime);
//	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
//	systime.wHour = (short)hours;
//	OS.SendMessage (handle, msg, 0, systime);
//	if ((style & SWT.CALENDAR) != 0 && hours >= 0 && hours <= 23) time.wHour = (short)hours;
}

/**
 * Sets the receiver's minutes.
 * <p>
 * Minutes is an integer between 0 and 59.
 * </p>
 *
 * @param minutes an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinutes (int minutes) {
	checkWidget ();
	if (minutes < 0 || minutes > 59) return;
	Utils.notImplemented();
//	SYSTEMTIME systime = new SYSTEMTIME ();
//	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
//	OS.SendMessage (handle, msg, 0, systime);
//	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
//	systime.wMinute = (short)minutes;
//	OS.SendMessage (handle, msg, 0, systime);
//	if ((style & SWT.CALENDAR) != 0 && minutes >= 0 && minutes <= 59) time.wMinute = (short)minutes;
}

/**
 * Sets the receiver's month.
 * <p>
 * The first month of the year is 0, and the last month is 11.
 * If the specified month is not valid for the receiver's day and year, then it is ignored.
 * </p>
 *
 * @param month an integer between 0 and 11
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setDate
 */
public void setMonth (int month) {
	checkWidget ();
	Utils.notImplemented();
//	SYSTEMTIME systime = new SYSTEMTIME ();
//	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
//	OS.SendMessage (handle, msg, 0, systime);
//	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
//	systime.wMonth = (short)(month + 1);
//	OS.SendMessage (handle, msg, 0, systime);
//	lastSystemTime = null;
}

@Override
public void setOrientation (int orientation) {
	/* Currently supported only for CALENDAR style. */
	if ((style & SWT.CALENDAR) != 0) super.setOrientation (orientation);
}
/**
 * Sets the receiver's seconds.
 * <p>
 * Seconds is an integer between 0 and 59.
 * </p>
 *
 * @param seconds an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSeconds (int seconds) {
	checkWidget ();
	if (seconds < 0 || seconds > 59) return;
	Utils.notImplemented();
//	SYSTEMTIME systime = new SYSTEMTIME ();
//	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
//	OS.SendMessage (handle, msg, 0, systime);
//	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
//	systime.wSecond = (short)seconds;
//	OS.SendMessage (handle, msg, 0, systime);
//	if ((style & SWT.CALENDAR) != 0 && seconds >= 0 && seconds <= 59) time.wSecond = (short)seconds;
}

/**
 * Sets the receiver's hours, minutes, and seconds in a single operation.
 *
 * @param hours an integer between 0 and 23
 * @param minutes an integer between 0 and 59
 * @param seconds an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setTime (int hours, int minutes, int seconds) {
	checkWidget ();
	if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) return;
//	SYSTEMTIME systime = new SYSTEMTIME ();
//	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
//	OS.SendMessage (handle, msg, 0, systime);
//	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
//	systime.wHour = (short)hours;
//	systime.wMinute = (short)minutes;
//	systime.wSecond = (short)seconds;
//	OS.SendMessage (handle, msg, 0, systime);
//	if ((style & SWT.CALENDAR) != 0
//			&& hours >= 0 && hours <= 23
//			&& minutes >= 0 && minutes <= 59
//			&& seconds >= 0 && seconds <= 59) {
//		time.wHour = (short)hours;
//		time.wMinute = (short)minutes;
//		time.wSecond = (short)seconds;
//	}
}

/**
 * Sets the receiver's year.
 * <p>
 * The first year is 1752 and the last year is 9999.
 * If the specified year is not valid for the receiver's day and month, then it is ignored.
 * </p>
 *
 * @param year an integer between 1752 and 9999
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setDate
 */
public void setYear (int year) {
	checkWidget ();
	if (year < MIN_YEAR || year > MAX_YEAR) return;
	Utils.notImplemented();
//	SYSTEMTIME systime = new SYSTEMTIME ();
//	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
//	OS.SendMessage (handle, msg, 0, systime);
//	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
//	systime.wYear = (short)year;
//	OS.SendMessage (handle, msg, 0, systime);
//	lastSystemTime = null;
}

//@Override
//int widgetStyle () {
//	int bits = super.widgetStyle () | OS.WS_TABSTOP;
//	if ((style & SWT.CALENDAR_WEEKNUMBERS) != 0) {
//		bits |= OS.MCS_WEEKNUMBERS;
//	}
//	if ((style & SWT.CALENDAR) != 0) return bits | OS.MCS_NOTODAY;
//	/*
//	* Bug in Windows: When WS_CLIPCHILDREN is set in a
//	* Date and Time Picker, the widget draws on top of
//	* the updown control. The fix is to clear the bits.
//	*/
//	bits &= ~OS.WS_CLIPCHILDREN;
//	if ((style & SWT.TIME) != 0) bits |= OS.DTS_TIMEFORMAT;
//	if ((style & SWT.DATE) != 0) {
//		bits |= ((style & SWT.MEDIUM) != 0 ? OS.DTS_SHORTDATECENTURYFORMAT : OS.DTS_LONGDATEFORMAT);
//		if ((style & SWT.DROP_DOWN) == 0) bits |= OS.DTS_UPDOWN;
//	}
//	return bits;
//}

}
