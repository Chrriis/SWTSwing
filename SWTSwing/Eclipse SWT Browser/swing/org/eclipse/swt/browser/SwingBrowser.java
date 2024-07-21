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
package org.eclipse.swt.browser;

import java.awt.Container;
import java.util.EventObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.swing.BrowserLocationChangedEvent;
import org.eclipse.swt.internal.swing.BrowserLocationChangingEvent;
import org.eclipse.swt.internal.swing.CBrowser;
import org.eclipse.swt.internal.swing.UIThreadUtils;
import org.eclipse.swt.internal.swing.Utils;
import org.eclipse.swt.widgets.Composite;

/**
 * Instances of this class implement the browser user interface
 * metaphor.  It allows the user to visualize and navigate through
 * HTML documents.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @since 3.0
 */
public class SwingBrowser extends WebBrowser {

	public Container handle;

	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$

	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.browser."; //$NON-NLS-1$

public void create(Composite parent, int style) {
	handle = browser.handle;
}

public static void clearSessions () {
	Utils.notImplemented();
}

public boolean back() {
	return ((CBrowser)handle).back();
}

public boolean execute(String script) {
	if (script == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	// TODO: impossible to implement. Check in future versions of Java...
	return false;
}

public boolean forward() {
	return ((CBrowser)handle).forward();
}

public String getBrowserType () {
	// TODO: implement
	return "swingbrowser";
}

public boolean isBackEnabled() {
	return ((CBrowser)handle).isBackEnabled();
}

public boolean isForwardEnabled() {
	return ((CBrowser)handle).isForwardEnabled();
}

public String getText () {
	return ((CBrowser)handle).getText();
}

public String getUrl() {
	return ((CBrowser)handle).getURL();
}

public void refresh() {
	((CBrowser)handle).refresh();
}

public void setJavascriptEnabled (boolean enabled) {
	Utils.notImplemented();
}

public boolean setText (String html) {
	return setText (html, true);
}

public boolean setText(String html, boolean trusted) {
	if (html == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return ((CBrowser)handle).setText(html, trusted);
}

public boolean setUrl (String url) {
	return setUrl (url, null, null);
}

public boolean setUrl (String url, String postData, String[] headers) {
	if (url == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return ((CBrowser)handle).setURL(url, postData, headers);
}

public void stop() {
	((CBrowser)handle).stop();
}

public void processEvent(EventObject e) {
	UIThreadUtils.startExclusiveSection(browser.getDisplay());
	if(browser.isDisposed()) {
		UIThreadUtils.stopExclusiveSection();
		return;
	}
	try {
		if(e instanceof BrowserLocationChangingEvent) {
			BrowserLocationChangingEvent browserLocationChangingEvent = (BrowserLocationChangingEvent)e;
			LocationEvent newEvent = new LocationEvent(browser);
			newEvent.display = browser.getDisplay();
			newEvent.widget = browser;
			newEvent.location = browserLocationChangingEvent.getURL();
			newEvent.doit = true;
			for (int i = 0; i < locationListeners.length; i++) {
				locationListeners[i].changing(newEvent);
			}
			if(!newEvent.doit) {
				browserLocationChangingEvent.consume();
			}
		} else if(e instanceof BrowserLocationChangedEvent) {
			BrowserLocationChangedEvent browserLocationChangedEvent = (BrowserLocationChangedEvent)e;
			LocationEvent newEvent = new LocationEvent(browser);
			newEvent.display = browser.getDisplay();
			newEvent.widget = browser;
			newEvent.location = browserLocationChangedEvent.getURL();
			for (int i = 0; i < locationListeners.length; i++) {
				locationListeners[i].changed(newEvent);
			}
		}
	} catch(Throwable t) {
		UIThreadUtils.storeException(t);
	} finally {
		UIThreadUtils.stopExclusiveSection();
	}
}

}
