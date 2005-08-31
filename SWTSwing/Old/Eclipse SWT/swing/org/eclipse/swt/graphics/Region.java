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
package org.eclipse.swt.graphics;

import java.awt.Shape;
import java.awt.geom.Area;
import org.eclipse.swt.*;

/**
 * Instances of this class represent areas of an x-y coordinate
 * system that are aggregates of the areas covered by a number
 * of rectangles.
 * <p>
 * Application code must explicitly invoke the <code>Region.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 */

public final class Region {

  /**
   * the OS resource for the region
   * (Warning: This field is platform dependent)
   */
  public int handle;

  /**
   * the device where this region was created
   */
  Device device;

  /**
   * The OS resource for the region
   * (Warning: This field is platform dependent)
   */
  public Area swingHandle;

  /**
   * Constructs a new empty region.
   *
   * @exception SWTError <ul>
   *    <li>ERROR_NO_HANDLES if a handle could not be obtained for region creation</li>
   * </ul>
   */
  public Region() {
    this((Device)null);
  }

  /**
   * Constructs a new empty region.
   * <p>
   * You must dispose the region when it is no longer required. 
   * </p>
   *
   * @param device the device on which to allocate the region
   *
  * @exception SWTError <ul>
   *    <li>ERROR_NO_HANDLES if a handle could not be obtained for region creation</li>
   * </ul>
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
   * </ul>
   *
   * @see #dispose
   * 
   * @since 3.0
   */
  public Region (Device device) {
    if (device == null) device = Device.getDevice();
    if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    this.device = device;
    swingHandle = new Area();
//    if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
    if (device.tracking) device.new_Object(this);
  }

  /**
   * Constructs a new region given a handle to the operating
   * system resources that it should represent.
   *
   * @param handle the handle for the result
   */
  Region(Shape handle) {
    swingHandle = new Area(handle);
  }

  /**
   * Constructs a new region given a handle to the operating
   * system resources that it should represent.
   *
   * @param handle the handle for the result
   */
  Region(int handle) {
    throw new IllegalStateException("Not supported. Use with the Shape constructor");
  }

  /**
   * Adds the given polygon to the collection of rectangles
   * the receiver maintains to describe its area.
   *
   * @param pointArray points that describe the polygon to merge with the receiver
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @since 3.0
  *
   */
  public void add (int[] pointArray) {
    if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    int[] xPoints = new int[pointArray.length/2];
    int[] yPoints = new int[xPoints.length];
    for(int i=0; i<pointArray.length; i++) {
      xPoints[i] = pointArray[i * 2];
      yPoints[i] = pointArray[i * 2 + 1];
    }
    swingHandle.add(new Area(new java.awt.Polygon(xPoints, yPoints, xPoints.length)));
  }

  /**
   * Adds the given rectangle to the collection of rectangles
   * the receiver maintains to describe its area.
   *
   * @param rect the rectangle to merge with the receiver
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void add(Rectangle rect) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(rect == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(rect.width < 0 || rect.height < 0) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);

    }
    swingHandle.add(new Area(new java.awt.Rectangle(rect.x, rect.y, rect.width, rect.height)));
  }

  /**
   * Adds all of the rectangles which make up the area covered
   * by the argument to the collection of rectangles the receiver
   * maintains to describe its area.
   *
   * @param region the region to merge
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void add(Region region) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(region == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(region.isDisposed()) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    swingHandle.add(region.swingHandle);
  }

  /**
   * Returns <code>true</code> if the point specified by the
   * arguments is inside the area specified by the receiver,
   * and <code>false</code> otherwise.
   *
   * @param x the x coordinate of the point to test for containment
   * @param y the y coordinate of the point to test for containment
   * @return <code>true</code> if the region contains the point and <code>false</code> otherwise
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public boolean contains(int x, int y) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return swingHandle.contains(new java.awt.Point(x, y));
  }

  /**
   * Returns <code>true</code> if the given point is inside the
   * area specified by the receiver, and <code>false</code>
   * otherwise.
   *
   * @param pt the point to test for containment
   * @return <code>true</code> if the region contains the point and <code>false</code> otherwise
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public boolean contains(Point pt) {
    if(pt == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    return contains(pt.x, pt.y);
  }

  /**
   * Disposes of the operating system resources associated with
   * the region. Applications must dispose of all regions which
   * they allocate.
   */
  public void dispose() {
    swingHandle = null;
  }

  /**
   * Compares the argument to the receiver, and returns true
   * if they represent the <em>same</em> object using a class
   * specific comparison.
   *
   * @param object the object to compare with this object
   * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
   *
   * @see #hashCode
   */
  public boolean equals(Object object) {
    if(this == object) {
      return true;
    }
    if(!(object instanceof Region)) {
      return false;
    }
    Region rgn = (Region)object;
    return swingHandle == rgn.swingHandle;
  }

  /**
   * Returns a rectangle which represents the rectangular
   * union of the collection of rectangles the receiver
   * maintains to describe its area.
   *
   * @return a bounding rectangle for the region
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see Rectangle#union
   */
  public Rectangle getBounds() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    java.awt.Rectangle bounds = swingHandle.getBounds();
    return new Rectangle((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
  }

  /**
   * Returns an integer hash code for the receiver. Any two
   * objects which return <code>true</code> when passed to
   * <code>equals</code> must return the same value for this
   * method.
   *
   * @return the receiver's hash
   *
   * @see #equals
   */
  public int hashCode() {
    return swingHandle.hashCode();
  }

  /**
   * Intersects the given rectangle to the collection of rectangles
   * the receiver maintains to describe its area.
   *
   * @param rect the rectangle to intersect with the receiver
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   * 
   * @since 3.0
   */
  public void intersect (Rectangle rect) {
    if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    swingHandle.intersect(new Area(new java.awt.Rectangle(rect.x, rect.y, rect.width, rect.height)));
  }

  /**
   * Intersects all of the rectangles which make up the area covered
   * by the argument to the collection of rectangles the receiver
   * maintains to describe its area.
   *
   * @param region the region to intersect
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   * 
   * @since 3.0
   */
  public void intersect (Region region) {
    if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    swingHandle.intersect(region.swingHandle);
  }

  /**
   * Returns <code>true</code> if the rectangle described by the
   * arguments intersects with any of the rectangles the receiver
   * mainains to describe its area, and <code>false</code> otherwise.
   *
   * @param x the x coordinate of the origin of the rectangle
   * @param y the y coordinate of the origin of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see Rectangle#intersects
   */
  public boolean intersects(int x, int y, int width, int height) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return swingHandle.intersects(x, y, width, height);
  }

  /**
   * Returns <code>true</code> if the given rectangle intersects
   * with any of the rectangles the receiver mainains to describe
   * its area and <code>false</code> otherwise.
   *
   * @param rect the rectangle to test for intersection
   * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see Rectangle#intersects
   */
  public boolean intersects(Rectangle rect) {
    if(rect == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    return intersects(rect.x, rect.y, rect.width, rect.height);
  }

  /**
   * Returns <code>true</code> if the region has been disposed,
   * and <code>false</code> otherwise.
   * <p>
   * This method gets the dispose state for the region.
   * When a region has been disposed, it is an error to
   * invoke any other method using the region.
   *
   * @return <code>true</code> when the region is disposed, and <code>false</code> otherwise
   */
  public boolean isDisposed() {
    return swingHandle == null;
  }

  /**
   * Returns <code>true</code> if the receiver does not cover any
   * area in the (x, y) coordinate plane, and <code>false</code> if
   * the receiver does cover some area in the plane.
   *
   * @return <code>true</code> if the receiver is empty, and <code>false</code> otherwise
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public boolean isEmpty() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return swingHandle.isEmpty();
//    RECT rect = new RECT();
//    int result = OS.GetRgnBox(handle, rect);
//    if(result == OS.NULLREGION) {
//      return true;
//    }
//    return((rect.right - rect.left) <= 0) || ((rect.bottom - rect.top) <= 0);
  }

  /**
   * Subtracts the given polygon from the collection of rectangles
   * the receiver maintains to describe its area.
   *
   * param pointArray points that describe the polygon to merge with the receiver
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   * 
   * @since 3.0
   */
  public void subtract (int[] pointArray) {
    if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    int[] xPoints = new int[pointArray.length/2];
    int[] yPoints = new int[xPoints.length];
    for(int i=0; i<pointArray.length; i++) {
      xPoints[i] = pointArray[i * 2];
      yPoints[i] = pointArray[i * 2 + 1];
    }
    swingHandle.subtract(new Area(new java.awt.Polygon(xPoints, yPoints, xPoints.length)));
  }

  /**
   * Subtracts the given rectangle from the collection of rectangles
   * the receiver maintains to describe its area.
   *
   * @param rect the rectangle to subtract from the receiver
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   * 
   * @since 3.0
   */
  public void subtract (Rectangle rect) {
    if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    swingHandle.subtract(new Area(new java.awt.Rectangle(rect.x, rect.y, rect.width, rect.height)));
  }

  /**
   * Subtracts all of the rectangles which make up the area covered
   * by the argument from the collection of rectangles the receiver
   * maintains to describe its area.
   *
   * @param region the region to subtract
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   * 
   * @since 3.0
   */
  public void subtract (Region region) {
    if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    swingHandle.subtract(region.swingHandle);
  }

  /**
   * Invokes platform specific functionality to allocate a new region.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Region</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @param handle the handle for the region
   */
  public static Region win32_new(int handle) {
    throw new IllegalStateException("To be removed");
//    return new Region(handle);
  }

  /**
   * Returns a string containing a concise, human-readable
   * description of the receiver.
   *
   * @return a string representation of the receiver
   */
  public String toString() {
    if(isDisposed()) {
      return "Region {*DISPOSED*}";
    }
    return "Region {" + swingHandle + "}";
  }

}
