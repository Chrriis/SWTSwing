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

import java.awt.GraphicsConfiguration;

import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are descriptions of monitors.
 *
 * @see Display
 * 
 * @since 3.0
 */
public final class Monitor {
	GraphicsConfiguration handle;
	int x, y, width, height;
	int clientX, clientY, clientWidth, clientHeight;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Monitor () {	
}
	
/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Monitor)) return false;
	Monitor monitor = (Monitor) object;
	return handle.equals(monitor.handle);
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its device.
 *
 * @return the receiver's bounding rectangle
 */	
public Rectangle getBounds () {
	return new Rectangle (x, y, width, height);
}
	
/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data.
 * 
 * @return the client area
 */
public Rectangle getClientArea () {
	return new Rectangle (clientX, clientY, clientWidth, clientHeight);
}
	
/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
 */
public int hashCode () {
	return handle.hashCode();
}

}
