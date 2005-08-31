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

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.eclipse.swt.*;

/**
 * Instances of this class manage operating system resources that
 * specify the appearance of the on-screen pointer. To create a
 * cursor you specify the device and either a simple cursor style
 * describing one of the standard operating system provided cursors
 * or the image and mask data for the desired appearance.
 * <p>
 * Application code must explicitly invoke the <code>Cursor.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>
 *   CURSOR_ARROW, CURSOR_WAIT, CURSOR_CROSS, CURSOR_APPSTARTING, CURSOR_HELP,
 *   CURSOR_SIZEALL, CURSOR_SIZENESW, CURSOR_SIZENS, CURSOR_SIZENWSE, CURSOR_SIZEWE,
 *   CURSOR_SIZEN, CURSOR_SIZES, CURSOR_SIZEE, CURSOR_SIZEW, CURSOR_SIZENE, CURSOR_SIZESE,
 *   CURSOR_SIZESW, CURSOR_SIZENW, CURSOR_UPARROW, CURSOR_IBEAM, CURSOR_NO, CURSOR_HAND
 * </dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p>
 */

public final class Cursor {

  /**
   * the handle to the OS cursor resource
   * (Warning: This field is platform dependent)
   */
  public int handle;

  /**
   * The handle to the OS cursor resource
   * (Warning: This field is platform dependent)
   */
  public java.awt.Cursor swingHandle;

  /**
   * the device where this cursor was created
   */
  Device device;

  /**
   * Prevents uninitialized instances from being created outside the package.
   */
  Cursor() {
  }

  /**
   * Constructs a new cursor given a device and a style
   * constant describing the desired cursor appearance.
   * <p>
   * You must dispose the cursor when it is no longer required.
   * </p>
   *
   * @param device the device on which to allocate the cursor
   * @param style the style of cursor to allocate
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
   *    <li>ERROR_INVALID_ARGUMENT - when an unknown style is specified</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
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
   */
  public Cursor(Device device, int style) {
    if(device == null) {
      device = Device.getDevice();
    }
    if(device == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    this.device = device;
    switch(style) {
      case SWT.CURSOR_HAND:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
        break;
      case SWT.CURSOR_ARROW:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
        break;
      case SWT.CURSOR_WAIT:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR);
        break;
      case SWT.CURSOR_CROSS:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR);
        break;
      case SWT.CURSOR_SIZEALL:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR);
        break;
      case SWT.CURSOR_SIZEN:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR);
        break;
      case SWT.CURSOR_SIZENE:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR);
        break;
      case SWT.CURSOR_SIZEE:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR);
        break;
      case SWT.CURSOR_SIZESE:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR);
        break;
      case SWT.CURSOR_SIZES:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.S_RESIZE_CURSOR);
        break;
      case SWT.CURSOR_SIZESW:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR);
        break;
      case SWT.CURSOR_SIZEW:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.W_RESIZE_CURSOR);
        break;
      case SWT.CURSOR_SIZENW:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR);
        break;
      case SWT.CURSOR_IBEAM:
        swingHandle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR);
        break;
      case SWT.CURSOR_APPSTARTING:
      case SWT.CURSOR_HELP:
      case SWT.CURSOR_NO:
      case SWT.CURSOR_SIZENESW:
      case SWT.CURSOR_SIZENS:
      case SWT.CURSOR_SIZENWSE:
      case SWT.CURSOR_SIZEWE:
      case SWT.CURSOR_UPARROW:
        swingHandle = createCursor(style);
        break;
      default:
        SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    if(device.tracking) {
      device.new_Object(this);
    }
  }

  java.awt.Cursor createCursor(int type) {
    String name = "";
    switch(type) {
      case SWT.CURSOR_APPSTARTING: name = "AppCursor"; break;
      case SWT.CURSOR_NO: name = "NoCursor"; break;
      case SWT.CURSOR_SIZENESW: name = "NESWCursor"; break;
      case SWT.CURSOR_SIZENS: name = "DownCursor"; break;
      case SWT.CURSOR_SIZENWSE: name = "NWSECursor"; break;
      case SWT.CURSOR_SIZEWE: name = "WECursor"; break;
      case SWT.CURSOR_UPARROW: name = "UpCursor"; break;
      case SWT.CURSOR_HELP: name = "HelpCursor"; break;
      default: return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
    }
    
    String gifFile = null;
    String hotspot = null;
    ImageIcon icon;
    java.awt.Point point;

    // Get the Property file
    InputStream is =
      getClass().getResourceAsStream("resources/" + name + ".properties");
    if (is == null) {
      return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
    }
    try {
      ResourceBundle resource = new PropertyResourceBundle(is);
      gifFile = "resources/" + resource.getString("Cursor.File");
      hotspot = resource.getString("Cursor.HotSpot");
    } catch (MissingResourceException e) {
      return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
    } catch (IOException e2) {
      return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
    }

    // Create the icon
    byte[] buffer = null;
    try {
      /* Copies resource into a byte array.  This is
       * necessary because several browsers consider
       * Class.getResource a security risk because it
       * can be used to load additional classes.
       * Class.getResourceAsStream returns raw
       * bytes, which JH can convert to an image.
       */
      InputStream resource = getClass().getResourceAsStream(gifFile);
      if (resource == null) {
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
      }
      BufferedInputStream in = new BufferedInputStream(resource);
      ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
      buffer = new byte[1024];
      int n;
      while ((n = in.read(buffer)) > 0) {
        out.write(buffer, 0, n);
      }
      in.close();
      out.flush();

      buffer = out.toByteArray();
      if (buffer.length == 0) {
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
      }
    } catch (IOException ioe) {
      return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
    }

    icon = new ImageIcon(buffer);

    // create the point
    int k = hotspot.indexOf(',');
    point = new java.awt.Point(
        Integer.parseInt(hotspot.substring(0, k)),
        Integer.parseInt(hotspot.substring(k + 1)));
    try {
      return Toolkit.getDefaultToolkit().createCustomCursor(
        icon.getImage(),
        point,
        name);
    } catch (NoSuchMethodError err) {
      //      return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
      return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
    }
  }


  /**
   * Constructs a new cursor given a device, image and mask
   * data describing the desired cursor appearance, and the x
   * and y co-ordinates of the <em>hotspot</em> (that is, the point
   * within the area covered by the cursor which is considered
   * to be where the on-screen pointer is "pointing").
   * <p>
   * The mask data is allowed to be null, but in this case the source
   * must be an ImageData representing an icon that specifies both
   * color data and mask data.
   * <p>
   * You must dispose the cursor when it is no longer required.
   * </p>
   *
   * @param device the device on which to allocate the cursor
   * @param source the color data for the cursor
   * @param mask the mask data for the cursor (or null)
   * @param hotspotX the x coordinate of the cursor's hotspot
   * @param hotspotY the y coordinate of the cursor's hotspot
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
   *    <li>ERROR_NULL_ARGUMENT - if the source is null</li>
   *    <li>ERROR_NULL_ARGUMENT - if the mask is null and the source does not have a mask</li>
       *    <li>ERROR_INVALID_ARGUMENT - if the source and the mask are not the same
       *          size, or either is not of depth one, or if the hotspot is outside
   *          the bounds of the image</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
   * </ul>
   */
  public Cursor(Device device, ImageData source, ImageData mask, int hotspotX,
                int hotspotY) {
    if(device == null) {
      device = Device.getDevice();
    }
    if(device == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    this.device = device;
    if(source == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    
    if(mask == null) {
      if(source.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
        SWT.error(SWT.ERROR_NULL_ARGUMENT);
      }
      mask = source.getTransparencyMask();
    }
    /* Check the bounds. Mask must be the same size as source */
    if(mask.width != source.width || mask.height != source.height) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    /* Check color depths */
    if(mask.depth != 1) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    if(source.depth != 1) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
      /* Check the hotspots */
    }
    if(hotspotX >= source.width || hotspotX < 0 ||
       hotspotY >= source.height || hotspotY < 0) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }

    
    // TODO: create a real new image data taking into account the mask..., then reuse the method createCursor for the array of bytes
//    try {
      // TODO: find suitable name for accessibility
      swingHandle = Toolkit.getDefaultToolkit().createCustomCursor(
        new Image(device, source, mask).swingHandle, new java.awt.Point(hotspotX, hotspotY), "name");
//    } catch (NoSuchMethodError err) {
      //      return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
//      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//    }
    
    if(device.tracking) {
      device.new_Object(this);
    }
  }

  /**
   * Disposes of the operating system resources associated with
   * the cursor. Applications must dispose of all cursors which
   * they allocate.
   */
  public void dispose() {
    if(swingHandle == null) {
      return;
    }
    if(device.isDisposed()) {
      return;
    }
    
    swingHandle = null;

    if(device.tracking) {
      device.dispose_Object(this);
    }
    device = null;
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
    if(object == this) {
      return true;
    }
    if(!(object instanceof Cursor)) {
      return false;
    }
    Cursor cursor = (Cursor)object;
    return device == cursor.device && swingHandle == cursor.swingHandle;
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
   * Returns <code>true</code> if the cursor has been disposed,
   * and <code>false</code> otherwise.
   * <p>
   * This method gets the dispose state for the cursor.
   * When a cursor has been disposed, it is an error to
   * invoke any other method using the cursor.
   *
   * @return <code>true</code> when the cursor is disposed and <code>false</code> otherwise
   */
  public boolean isDisposed() {
    return swingHandle == null;
  }

  /**
   * Returns a string containing a concise, human-readable
   * description of the receiver.
   *
   * @return a string representation of the receiver
   */
  public String toString() {
    if(isDisposed()) {
      return "Cursor {*DISPOSED*}";
    }
    return "Cursor {" + swingHandle.getName() + "}";
  }

//  /**
//   * Invokes platform specific functionality to allocate a new cursor.
//   * <p>
//   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
//   * API for <code>Cursor</code>. It is marked public only so that it
//   * can be shared within the packages provided by SWT. It is not
//   * available on all platforms, and should never be called from
//   * application code.
//   * </p>
//   *
//   * @param device the device on which to allocate the color
//   * @param handle the handle for the cursor
//   */
//  public static Cursor win32_new(Device device, int handle) {
//    if(device == null) {
//      device = Device.getDevice();
//    }
//    Cursor cursor = new Cursor();
//    cursor.handle = handle;
//    cursor.device = device;
//    return cursor;
//  }

}
