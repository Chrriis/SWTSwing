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

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import javax.swing.UIManager;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.*;

/**
 * Class <code>GC</code> is where all of the drawing capabilities that are
 * supported by SWT are located. Instances are used to draw on either an
 * <code>Image</code>, a <code>Control</code>, or directly on a <code>Display</code>.
 * <p>
 * Application code must explicitly invoke the <code>GC.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required. This is <em>particularly</em>
     * important on Windows95 and Windows98 where the operating system has a limited
 * number of device contexts available.
 * </p>
 *
 * @see org.eclipse.swt.events.PaintEvent
 */

public final class GC {

  /**
   * the handle to the OS device context
   * (Warning: This field is platform dependent)
   */
  public int handle;

  /**
   * The handle to the OS device context
   * (Warning: This field is platform dependent)
   */
  public Graphics swingHandle;

  Device device;
  
  Drawable drawable;

//  GCData data;

  /**
   * Prevents uninitialized instances from being created outside the package.
   */
  GC() {
  }

  /**
   * Constructs a new instance of this class which has been
   * configured to draw on the specified drawable. Sets the
   * foreground and background color in the GC to match those
   * in the drawable.
   * <p>
   * You must dispose the graphics context when it is no longer required.
   * </p>
   * @param drawable the drawable to draw on
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
   *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
   *    <li>ERROR_INVALID_ARGUMENT
   *          - if the drawable is an image that is not a bitmap or an icon
   *          - if the drawable is an image or printer that is already selected
   *            into another graphics context</li>
   * </ul>
   * @exception SWTError <ul>
       *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
   * </ul>
   */
  public GC(Drawable drawable) {
    if(drawable == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    init(drawable);
    if(device.tracking) {
      device.new_Object(this);
    }
  }

  /**  
   * Constructs a new instance of this class which has been
   * configured to draw on the specified drawable. Sets the
   * foreground and background color in the GC to match those
   * in the drawable.
   * <p>
   * You must dispose the graphics context when it is no longer required. 
   * </p>
   * 
   * @param drawable the drawable to draw on
   * @param style the style of GC to construct
   * 
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
   *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
   *    <li>ERROR_INVALID_ARGUMENT
   *          - if the drawable is an image that is not a bitmap or an icon
   *          - if the drawable is an image or printer that is already selected
   *            into another graphics context</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
   * </ul>
   *  
   * @since 3.0
   */
  public GC(Drawable drawable, int style) {
    if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
    // TODO: implement constructor content
    throw new IllegalStateException("Not yet implemented!");
//    GCData data = new GCData ();
//    data.style = checkStyle(style);
//    int hDC = drawable.internal_new_GC(data);
//    Device device = data.device;
//    if (device == null) device = Device.getDevice();
//    if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
//    data.device = device;
//    init (drawable, data, hDC);
//    if (device.tracking) device.new_Object(this); 
  }

  static int checkStyle(int style) {
    if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
    return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
  }

  /**
   * Copies a rectangular area of the receiver at the specified
   * position into the image, which must be of type <code>SWT.BITMAP</code>.
   *
   * @param x the x coordinate in the receiver of the area to be copied
   * @param y the y coordinate in the receiver of the area to be copied
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the image is not a bitmap or has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void copyArea(Image image, int x, int y) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(image == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(image.type != SWT.BITMAP || image.isDisposed()) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);

    }

    // TODO: try to check at the printer API to do that.
    
//    /* Get the HDC for the device */
//    int hDC = device.internal_new_GC(null);
//
//    /* Copy the bitmap area */
//    Rectangle rect = image.getBounds();
//    int memHdc = OS.CreateCompatibleDC(hDC);
//    int hOldBitmap = OS.SelectObject(memHdc, image.handle);
//    OS.BitBlt(memHdc, 0, 0, rect.width, rect.height, handle, x, y, OS.SRCCOPY);
//    OS.SelectObject(memHdc, hOldBitmap);
//    OS.DeleteDC(memHdc);
//
//    /* Release the HDC for the device */
//    device.internal_dispose_GC(hDC, null);
  }

  /**
   * Copies a rectangular area of the receiver at the source
   * position onto the receiver at the destination position.
   *
   * @param srcX the x coordinate in the receiver of the area to be copied
   * @param srcY the y coordinate in the receiver of the area to be copied
   * @param width the width of the area to copy
   * @param height the height of the area to copy
   * @param destX the x coordinate in the receiver of the area to copy to
   * @param destY the y coordinate in the receiver of the area to copy to
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void copyArea(int srcX, int srcY, int width, int height, int destX,
                       int destY) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    swingHandle.copyArea(srcX, srcY, width, height, destX - srcX, destY - srcY);
  }

  /**
   * Disposes of the operating system resources associated with
   * the graphics context. Applications must dispose of all GCs
   * which they allocate.
   */
  public void dispose() {
//    if(device.isDisposed()) {
//      return;
//    }
    swingHandle = null;

//    /*
//     * The only way for pens and brushes to get
//     * selected into the HDC is for the receiver to
//     * create them. When we are destroying the
//     * hDC we also destroy any pens and brushes that
//     * we have allocated. This code assumes that it
//     * is OK to delete stock objects. This will
//     * happen when a GC is disposed and the user has
//     * not caused new pens or brushes to be allocated.
//     */
//    int nullPen = OS.GetStockObject(OS.NULL_PEN);
//    int oldPen = OS.SelectObject(handle, nullPen);
//    OS.DeleteObject(oldPen);
//    int nullBrush = OS.GetStockObject(OS.NULL_BRUSH);
//    int oldBrush = OS.SelectObject(handle, nullBrush);
//    OS.DeleteObject(oldBrush);
//
//    /*
//     * Put back the original bitmap into the device context.
//     * This will ensure that we have not left a bitmap
//     * selected in it when we delete the HDC.
//     */
//    int hNullBitmap = data.hNullBitmap;
//    if(hNullBitmap != 0) {
//      OS.SelectObject(handle, hNullBitmap);
//      data.hNullBitmap = 0;
//    }
//    Image image = data.image;
//    if(image != null) {
//      image.memGC = null;
//
//      /*
//       * Dispose the HDC.
//       */
//    }
//    Device device = data.device;
//    drawable.internal_dispose_GC(handle, data);
//    drawable = null;
//    handle = 0;
//    data.image = null;
//    data.ps = null;
//    if(device.tracking) {
//      device.dispose_Object(this);
//    }
//    data.device = null;
//    data = null;
  }

  /**
   * Draws the outline of a circular or elliptical arc
   * within the specified rectangular area.
   * <p>
   * The resulting arc begins at <code>startAngle</code> and extends
   * for <code>arcAngle</code> degrees, using the current color.
   * Angles are interpreted such that 0 degrees is at the 3 o'clock
   * position. A positive value indicates a counter-clockwise rotation
   * while a negative value indicates a clockwise rotation.
   * </p><p>
   * The center of the arc is the center of the rectangle whose origin
   * is (<code>x</code>, <code>y</code>) and whose size is specified by the
   * <code>width</code> and <code>height</code> arguments.
   * </p><p>
   * The resulting arc covers an area <code>width + 1</code> pixels wide
   * by <code>height + 1</code> pixels tall.
   * </p>
   *
   * @param x the x coordinate of the upper-left corner of the arc to be drawn
   * @param y the y coordinate of the upper-left corner of the arc to be drawn
   * @param width the width of the arc to be drawn
   * @param height the height of the arc to be drawn
   * @param startAngle the beginning angle
   * @param arcAngle the angular extent of the arc, relative to the start angle
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if any of the width, height or endAngle is zero.</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawArc(int x, int y, int width, int height, int startAngle,
                      int endAngle) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(width < 0) {
      x = x + width;
      width = -width;
    }
    if(height < 0) {
      y = y + height;
      height = -height;
    }
    if(width == 0 || height == 0 || endAngle == 0) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    swingHandle.drawArc(x, y, width, height, startAngle, endAngle < 0? endAngle + startAngle: endAngle - startAngle);
  }

  /**
   * Draws a rectangle, based on the specified arguments, which has
   * the appearance of the platform's <em>focus rectangle</em> if the
   * platform supports such a notion, and otherwise draws a simple
   * rectangle in the receiver's foreground color.
   *
   * @param x the x coordinate of the rectangle
   * @param y the y coordinate of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #drawRectangle
   */
  public void drawFocus(int x, int y, int width, int height) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    java.awt.Color oldColor = swingHandle.getColor();
    java.awt.Color newColor = oldColor;
    // Swing's Look and Feels have a property ComponentName.focus, and we use JComponents. So let's remove the "J". 
    try {
      String focusName = drawable.getClass().getName();
      focusName = focusName.substring(focusName.lastIndexOf('.') + 2) + ".focus";
      // TODO: test this theory. Is that working only with MetalL&F?
      newColor = UIManager.getColor(focusName);
    } catch(Exception e) {}
    swingHandle.setColor(newColor);
    swingHandle.drawRect(x, y, width, height);
    swingHandle.setColor(oldColor);
  }

  /**
   * Draws the given image in the receiver at the specified
   * coordinates.
   *
   * @param image the image to draw
   * @param x the x coordinate of where to draw
   * @param y the y coordinate of where to draw
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the given coordinates are outside the bounds of the image</li>
   * @exception SWTError <ul>
   *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawImage(Image image, int x, int y) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(image == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(image.isDisposed()) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    drawImage(image, 0, 0, -1, -1, x, y, -1, -1, true);
  }

  /**
   * Copies a rectangular area from the source image into a (potentially
   * different sized) rectangular area in the receiver. If the source
   * and destination areas are of differing sizes, then the source
   * area will be stretched or shrunk to fit the destination area
   * as it is copied. The copy fails if any part of the source rectangle
   * lies outside the bounds of the source image, or if any of the width
   * or height arguments are negative.
   *
   * @param image the source image
   * @param srcX the x coordinate in the source image to copy from
   * @param srcY the y coordinate in the source image to copy from
   * @param srcWidth the width in pixels to copy from the source
   * @param srcHeight the height in pixels to copy from the source
   * @param destX the x coordinate in the destination to copy to
   * @param destY the y coordinate in the destination to copy to
   * @param destWidth the width in pixels of the destination rectangle
   * @param destHeight the height in pixels of the destination rectangle
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
   *    <li>ERROR_INVALID_ARGUMENT - if any of the width or height arguments are negative.
   *    <li>ERROR_INVALID_ARGUMENT - if the source rectangle is not contained within the bounds of the source image</li>
   * </ul>
   * @exception SWTError <ul>
   *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawImage(Image image, int srcX, int srcY, int srcWidth,
                        int srcHeight, int destX, int destY, int destWidth,
                        int destHeight) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) {
      return;
    }
    if(srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 ||
       destHeight < 0) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    if(image == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(image.isDisposed()) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth,
              destHeight, false);
  }

  void drawImage(Image srcImage, int srcX, int srcY, int srcWidth,
                 int srcHeight, int destX, int destY, int destWidth,
                 int destHeight, boolean simple) {
    // Simple == no stretch
    if(!simple) {
      swingHandle.drawImage(srcImage.swingHandle, destX, destY, destWidth - destX, destHeight - destY, srcX, srcY, srcWidth - srcX, srcHeight - srcY, null);
    } else {
      Shape oldClip = swingHandle.getClip();
      swingHandle.setClip(destX, destY, destWidth, destHeight);
      swingHandle.drawImage(srcImage.swingHandle, destX, destY, destWidth - destX, destHeight - destY, srcX, srcY, srcWidth - srcX, srcHeight - srcY, null);
      swingHandle.setClip(oldClip);
    }
//    switch(srcImage.type) {
//      case SWT.BITMAP:
//        drawBitmap(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY,
//                   destWidth, destHeight, simple);
//        break;
//      case SWT.ICON:
//        drawIcon(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY,
//                 destWidth, destHeight, simple);
//        break;
//      default:
//        SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
//    }
  }

//  void drawIcon(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight,
//                int destX, int destY, int destWidth, int destHeight,
//                boolean simple) {
//    /* Simple case: no stretching, entire icon */
//    if(simple) {
//      OS.DrawIconEx(handle, destX, destY, srcImage.handle, 0, 0, 0, 0,
//                    OS.DI_NORMAL);
//      return;
//    }
//
//    /* Get the icon info */
//    ICONINFO srcIconInfo = new ICONINFO();
//    if(OS.IsWinCE) {
//      Image.GetIconInfo(srcImage, srcIconInfo);
//    } else {
//      OS.GetIconInfo(srcImage.handle, srcIconInfo);
//    }
//
//    /* Get the icon width and height */
//    int hBitmap = srcIconInfo.hbmColor;
//    if(hBitmap == 0) {
//      hBitmap = srcIconInfo.hbmMask;
//    }
//    BITMAP bm = new BITMAP();
//    OS.GetObject(hBitmap, BITMAP.sizeof, bm);
//    int iconWidth = bm.bmWidth, iconHeight = bm.bmHeight;
//    if(hBitmap == srcIconInfo.hbmMask) {
//      iconHeight /= 2;
//
//    }
//    if(simple) {
//      srcWidth = destWidth = iconWidth;
//      srcHeight = destHeight = iconHeight;
//    }
//
//    /* Draw the icon */
//    boolean failed = srcX + srcWidth > iconWidth ||
//        srcY + srcHeight > iconHeight;
//    if(!failed) {
//      simple = srcX == 0 && srcY == 0 &&
//          srcWidth == destWidth && srcHeight == destHeight &&
//          srcWidth == iconWidth && srcHeight == iconHeight;
//      if(simple) {
//        /* Simple case: no stretching, entire icon */
//        OS.DrawIconEx(handle, destX, destY, srcImage.handle, 0, 0, 0, 0,
//                      OS.DI_NORMAL);
//      } else {
//        /* Get the HDC for the device */
//        Device device = data.device;
//        int hDC = device.internal_new_GC(null);
//
//        /* Create the icon info and HDC's */
//        ICONINFO newIconInfo = new ICONINFO();
//        newIconInfo.fIcon = true;
//        int srcHdc = OS.CreateCompatibleDC(hDC);
//        int dstHdc = OS.CreateCompatibleDC(hDC);
//
//        /* Blt the color bitmap */
//        int srcColorY = srcY;
//        int srcColor = srcIconInfo.hbmColor;
//        if(srcColor == 0) {
//          srcColor = srcIconInfo.hbmMask;
//          srcColorY += iconHeight;
//        }
//        int oldSrcBitmap = OS.SelectObject(srcHdc, srcColor);
//        newIconInfo.hbmColor = OS.CreateCompatibleBitmap(srcHdc, destWidth,
//            destHeight);
//        if(newIconInfo.hbmColor == 0) {
//          SWT.error(SWT.ERROR_NO_HANDLES);
//        }
//        int oldDestBitmap = OS.SelectObject(dstHdc, newIconInfo.hbmColor);
//        if(!OS.IsWinCE) {
//          OS.SetStretchBltMode(dstHdc, OS.COLORONCOLOR);
//        }
//        OS.StretchBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX,
//                      srcColorY, srcWidth, srcHeight, OS.SRCCOPY);
//
//        /* Blt the mask bitmap */
//        OS.SelectObject(srcHdc, srcIconInfo.hbmMask);
//        newIconInfo.hbmMask = OS.CreateBitmap(destWidth, destHeight, 1, 1, null);
//        if(newIconInfo.hbmMask == 0) {
//          SWT.error(SWT.ERROR_NO_HANDLES);
//        }
//        OS.SelectObject(dstHdc, newIconInfo.hbmMask);
//        OS.StretchBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY,
//                      srcWidth, srcHeight, OS.SRCCOPY);
//
//        /* Select old bitmaps before creating the icon */
//        OS.SelectObject(srcHdc, oldSrcBitmap);
//        OS.SelectObject(dstHdc, oldDestBitmap);
//
//        /* Create the new icon */
//        int hIcon = OS.CreateIconIndirect(newIconInfo);
//        if(hIcon == 0) {
//          SWT.error(SWT.ERROR_NO_HANDLES);
//
//          /* Draw the new icon */
//        }
//        OS.DrawIconEx(handle, destX, destY, hIcon, destWidth, destHeight, 0, 0,
//                      OS.DI_NORMAL);
//
//        /* Destroy the new icon and hdc's*/
//        OS.DestroyIcon(hIcon);
//        OS.DeleteObject(newIconInfo.hbmMask);
//        OS.DeleteObject(newIconInfo.hbmColor);
//        OS.DeleteDC(dstHdc);
//        OS.DeleteDC(srcHdc);
//
//        /* Release the HDC for the device */
//        device.internal_dispose_GC(hDC, null);
//      }
//    }
//
//    /* Free icon info */
//    OS.DeleteObject(srcIconInfo.hbmMask);
//    if(srcIconInfo.hbmColor != 0) {
//      OS.DeleteObject(srcIconInfo.hbmColor);
//    }
//
//    if(failed) {
//      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//    }
//  }

//  void drawBitmap(Image srcImage, int srcX, int srcY, int srcWidth,
//                  int srcHeight, int destX, int destY, int destWidth,
//                  int destHeight, boolean simple) {
//    BITMAP bm = new BITMAP();
//    OS.GetObject(srcImage.handle, BITMAP.sizeof, bm);
//    int imgWidth = bm.bmWidth;
//    int imgHeight = bm.bmHeight;
//    if(simple) {
//      srcWidth = destWidth = imgWidth;
//      srcHeight = destHeight = imgHeight;
//    } else {
//      if(srcX + srcWidth > imgWidth || srcY + srcHeight > imgHeight) {
//        SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//      }
//      simple = srcX == 0 && srcY == 0 &&
//          srcWidth == destWidth && destWidth == imgWidth &&
//          srcHeight == destHeight && destHeight == imgHeight;
//    }
//    boolean mustRestore = false;
//    GC memGC = srcImage.memGC;
//    if(memGC != null && !memGC.isDisposed()) {
//      mustRestore = true;
//      GCData data = memGC.data;
//      if(data.hNullBitmap != 0) {
//        OS.SelectObject(memGC.handle, data.hNullBitmap);
//        data.hNullBitmap = 0;
//      }
//    }
//    if(srcImage.alpha != -1 || srcImage.alphaData != null) {
//      drawBitmapAlpha(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY,
//                      destWidth, destHeight, simple, bm, imgWidth, imgHeight);
//    } else if(srcImage.transparentPixel != -1) {
//      drawBitmapTransparent(srcImage, srcX, srcY, srcWidth, srcHeight, destX,
//                            destY, destWidth, destHeight, simple, bm, imgWidth,
//                            imgHeight);
//    } else {
//      drawBitmap(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY,
//                 destWidth, destHeight, simple, bm, imgWidth, imgHeight);
//    }
//    if(mustRestore) {
//      int hOldBitmap = OS.SelectObject(memGC.handle, srcImage.handle);
//      memGC.data.hNullBitmap = hOldBitmap;
//    }
//  }
//
//  void drawBitmapAlpha(Image srcImage, int srcX, int srcY, int srcWidth,
//                       int srcHeight, int destX, int destY, int destWidth,
//                       int destHeight, boolean simple, BITMAP bm, int imgWidth,
//                       int imgHeight) {
//    /* Simple cases */
//    if(srcImage.alpha == 0) {
//      return;
//    }
//    if(srcImage.alpha == 255) {
//      drawBitmap(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY,
//                 destWidth, destHeight, simple, bm, imgWidth, imgHeight);
//      return;
//    }
//
//    /* Check clipping */
//    Rectangle rect = getClipping();
//    rect = rect.intersection(new Rectangle(destX, destY, destWidth, destHeight));
//    if(rect.isEmpty()) {
//      return;
//    }
//
//    /*
//     * Optimization.  Recalculate src and dest rectangles so that
//     * only the clipping area is drawn.
//     */
//    int sx1 = srcX + (((rect.x - destX) * srcWidth) / destWidth);
//    int sx2 = srcX + ((((rect.x + rect.width) - destX) * srcWidth) / destWidth);
//    int sy1 = srcY + (((rect.y - destY) * srcHeight) / destHeight);
//    int sy2 = srcY +
//        ((((rect.y + rect.height) - destY) * srcHeight) / destHeight);
//    destX = rect.x;
//    destY = rect.y;
//    destWidth = rect.width;
//    destHeight = rect.height;
//    srcX = sx1;
//    srcY = sy1;
//    srcWidth = Math.max(1, sx2 - sx1);
//    srcHeight = Math.max(1, sy2 - sy1);
//
//    /* Create resources */
//    int srcHdc = OS.CreateCompatibleDC(handle);
//    int oldSrcBitmap = OS.SelectObject(srcHdc, srcImage.handle);
//    int memHdc = OS.CreateCompatibleDC(handle);
//    int memDib = createDIB(Math.max(srcWidth, destWidth),
//                           Math.max(srcHeight, destHeight));
//    int oldMemBitmap = OS.SelectObject(memHdc, memDib);
//
//    BITMAP dibBM = new BITMAP();
//    OS.GetObject(memDib, BITMAP.sizeof, dibBM);
//    int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;
//
//    /* Get the background pixels */
//    OS.BitBlt(memHdc, 0, 0, destWidth, destHeight, handle, destX, destY,
//              OS.SRCCOPY);
//    byte[] destData = new byte[sizeInBytes];
//    OS.MoveMemory(destData, dibBM.bmBits, sizeInBytes);
//
//    /* Get the foreground pixels */
//    OS.BitBlt(memHdc, 0, 0, srcWidth, srcHeight, srcHdc, srcX, srcY, OS.SRCCOPY);
//    byte[] srcData = new byte[sizeInBytes];
//    OS.MoveMemory(srcData, dibBM.bmBits, sizeInBytes);
//
//    /* Merge the alpha channel in place */
//    int alpha = srcImage.alpha;
//    final boolean hasAlphaChannel = (srcImage.alpha == -1);
//    if(hasAlphaChannel) {
//      final int apinc = imgWidth - srcWidth;
//      final int spinc = dibBM.bmWidthBytes - srcWidth * 4;
//      int ap = srcY * imgWidth + srcX, sp = 3;
//      byte[] alphaData = srcImage.alphaData;
//      for(int y = 0; y < srcHeight; ++y) {
//        for(int x = 0; x < srcWidth; ++x) {
//          srcData[sp] = alphaData[ap++];
//          sp += 4;
//        }
//        ap += apinc;
//        sp += spinc;
//      }
//    }
//
//    /* Scale the foreground pixels with alpha */
//    if(!OS.IsWinCE) {
//      OS.SetStretchBltMode(memHdc, OS.COLORONCOLOR);
//    }
//    OS.MoveMemory(dibBM.bmBits, srcData, sizeInBytes);
//    /*
//     * Bug in WinCE and Win98.  StretchBlt does not correctly stretch when
//     * the source and destination HDCs are the same.  The workaround is to
//     * stretch to a temporary HDC and blit back into the original HDC.
//     * Note that on WinCE StretchBlt correctly compresses the image when the
//     * source and destination HDCs are the same.
//     */
//    if((OS.IsWinCE && (destWidth > srcWidth || destHeight > srcHeight)) ||
//       (!OS.IsWinNT && !OS.IsWinCE)) {
//      int tempHdc = OS.CreateCompatibleDC(handle);
//      int tempDib = createDIB(destWidth, destHeight);
//      int oldTempBitmap = OS.SelectObject(tempHdc, tempDib);
//      OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0,
//                    srcWidth, srcHeight, OS.SRCCOPY);
//      OS.BitBlt(memHdc, 0, 0, destWidth, destHeight, tempHdc, 0, 0, OS.SRCCOPY);
//      OS.SelectObject(tempHdc, oldTempBitmap);
//      OS.DeleteObject(tempDib);
//      OS.DeleteDC(tempHdc);
//    } else {
//      OS.StretchBlt(memHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, srcWidth,
//                    srcHeight, OS.SRCCOPY);
//    }
//    OS.MoveMemory(srcData, dibBM.bmBits, sizeInBytes);
//
//    /* Compose the pixels */
//    final int dpinc = dibBM.bmWidthBytes - destWidth * 4;
//    int dp = 0;
//    for(int y = 0; y < destHeight; ++y) {
//      for(int x = 0; x < destWidth; ++x) {
//        if(hasAlphaChannel) {
//          alpha = srcData[dp + 3] & 0xff;
//        }
//        destData[dp] += ((srcData[dp] & 0xff) - (destData[dp] & 0xff)) * alpha /
//            255;
//        destData[dp +
//            1] += ((srcData[dp + 1] & 0xff) - (destData[dp + 1] & 0xff)) *
//            alpha / 255;
//        destData[dp +
//            2] += ((srcData[dp + 2] & 0xff) - (destData[dp + 2] & 0xff)) *
//            alpha / 255;
//        dp += 4;
//      }
//      dp += dpinc;
//    }
//
//    /* Draw the composed pixels */
//    OS.MoveMemory(dibBM.bmBits, destData, sizeInBytes);
//    OS.BitBlt(handle, destX, destY, destWidth, destHeight, memHdc, 0, 0,
//              OS.SRCCOPY);
//
//    /* Free resources */
//    OS.SelectObject(memHdc, oldMemBitmap);
//    OS.DeleteDC(memHdc);
//    OS.DeleteObject(memDib);
//    OS.SelectObject(srcHdc, oldSrcBitmap);
//    OS.DeleteDC(srcHdc);
//  }
//
//  void drawBitmapTransparent(Image srcImage, int srcX, int srcY, int srcWidth,
//                             int srcHeight, int destX, int destY, int destWidth,
//                             int destHeight, boolean simple, BITMAP bm,
//                             int imgWidth, int imgHeight) {
//
//    /* Get the HDC for the device */
//    Device device = data.device;
//    int hDC = device.internal_new_GC(null);
//
//    /* Find the RGB values for the transparent pixel. */
//    int transBlue = 0, transGreen = 0, transRed = 0;
//    boolean isDib = bm.bmBits != 0;
//    int hBitmap = srcImage.handle;
//    int srcHdc = OS.CreateCompatibleDC(handle);
//    int oldSrcBitmap = OS.SelectObject(srcHdc, hBitmap);
//    byte[] originalColors = null;
//    if(bm.bmBitsPixel <= 8) {
//      if(isDib) {
//        /* Palette-based DIBSECTION */
//        if(OS.IsWinCE) {
//          byte[] pBits = new byte[1];
//          OS.MoveMemory(pBits, bm.bmBits, 1);
//          byte oldValue = pBits[0];
//          int mask = (0xFF << (8 - bm.bmBitsPixel)) & 0x00FF;
//          pBits[0] = (byte)((srcImage.transparentPixel << (8 - bm.bmBitsPixel)) |
//                            (pBits[0] & ~mask));
//          OS.MoveMemory(bm.bmBits, pBits, 1);
//          int color = OS.GetPixel(srcHdc, 0, 0);
//          pBits[0] = oldValue;
//          OS.MoveMemory(bm.bmBits, pBits, 1);
//          transBlue = (color & 0xFF0000) >> 16;
//          transGreen = (color & 0xFF00) >> 8;
//          transRed = color & 0xFF;
//        } else {
//          int maxColors = 1 << bm.bmBitsPixel;
//          byte[] oldColors = new byte[maxColors * 4];
//          OS.GetDIBColorTable(srcHdc, 0, maxColors, oldColors);
//          int offset = srcImage.transparentPixel * 4;
//          byte[] newColors = new byte[oldColors.length];
//          transRed = transGreen = transBlue = 0xff;
//          newColors[offset] = (byte)transBlue;
//          newColors[offset + 1] = (byte)transGreen;
//          newColors[offset + 2] = (byte)transRed;
//          OS.SetDIBColorTable(srcHdc, 0, maxColors, newColors);
//          originalColors = oldColors;
//        }
//      } else {
//        /* Palette-based bitmap */
//        int numColors = 1 << bm.bmBitsPixel;
//        /* Set the few fields necessary to get the RGB data out */
//        BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
//        bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//        bmiHeader.biPlanes = bm.bmPlanes;
//        bmiHeader.biBitCount = bm.bmBitsPixel;
//        byte[] bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
//        OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//        if(OS.IsWinCE) {
//          SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//        }
//        OS.GetDIBits(srcHdc, srcImage.handle, 0, 0, 0, bmi, OS.DIB_RGB_COLORS);
//        int offset = BITMAPINFOHEADER.sizeof + 4 * srcImage.transparentPixel;
//        transRed = bmi[offset + 2] & 0xFF;
//        transGreen = bmi[offset + 1] & 0xFF;
//        transBlue = bmi[offset] & 0xFF;
//      }
//    } else {
//      /* Direct color image */
//      int pixel = srcImage.transparentPixel;
//      switch(bm.bmBitsPixel) {
//        case 16:
//          transBlue = (pixel & 0x1F) << 3;
//          transGreen = (pixel & 0x3E0) >> 2;
//          transRed = (pixel & 0x7C00) >> 7;
//          break;
//        case 24:
//          transBlue = (pixel & 0xFF0000) >> 16;
//          transGreen = (pixel & 0xFF00) >> 8;
//          transRed = pixel & 0xFF;
//          break;
//        case 32:
//          transBlue = (pixel & 0xFF000000) >>> 24;
//          transGreen = (pixel & 0xFF0000) >> 16;
//          transRed = (pixel & 0xFF00) >> 8;
//          break;
//      }
//    }
//
//    if(OS.IsWinCE) {
//      /*
//       * Note in WinCE. TransparentImage uses the first entry of a palette
//       * based image when there are multiple entries that have the same
//       * transparent color.
//       */
//      int transparentColor = transBlue << 16 | transGreen << 8 | transRed;
//      OS.TransparentImage(handle, destX, destY, destWidth, destHeight,
//                          srcHdc, srcX, srcY, srcWidth, srcHeight,
//                          transparentColor);
//    } else {
//      /* Create the mask for the source image */
//      int maskHdc = OS.CreateCompatibleDC(hDC);
//      int maskBitmap = OS.CreateBitmap(imgWidth, imgHeight, 1, 1, null);
//      int oldMaskBitmap = OS.SelectObject(maskHdc, maskBitmap);
//      OS.SetBkColor(srcHdc, (transBlue << 16) | (transGreen << 8) | transRed);
//      OS.BitBlt(maskHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, 0, OS.SRCCOPY);
//      if(originalColors != null) {
//        OS.SetDIBColorTable(srcHdc, 0, 1 << bm.bmBitsPixel, originalColors);
//
//        /* Draw the source bitmap transparently using invert/and mask/invert */
//      }
//      int tempHdc = OS.CreateCompatibleDC(hDC);
//      int tempBitmap = OS.CreateCompatibleBitmap(hDC, destWidth, destHeight);
//      int oldTempBitmap = OS.SelectObject(tempHdc, tempBitmap);
//      OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, handle, destX, destY,
//                OS.SRCCOPY);
//      if(!OS.IsWinCE) {
//        OS.SetStretchBltMode(tempHdc, OS.COLORONCOLOR);
//      }
//      OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY,
//                    srcWidth, srcHeight, OS.SRCINVERT);
//      OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, maskHdc, srcX, srcY,
//                    srcWidth, srcHeight, OS.SRCAND);
//      OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY,
//                    srcWidth, srcHeight, OS.SRCINVERT);
//      OS.BitBlt(handle, destX, destY, destWidth, destHeight, tempHdc, 0, 0,
//                OS.SRCCOPY);
//
//      /* Release resources */
//      OS.SelectObject(tempHdc, oldTempBitmap);
//      OS.DeleteDC(tempHdc);
//      OS.DeleteObject(tempBitmap);
//      OS.SelectObject(maskHdc, oldMaskBitmap);
//      OS.DeleteDC(maskHdc);
//      OS.DeleteObject(maskBitmap);
//    }
//    OS.SelectObject(srcHdc, oldSrcBitmap);
//    if(hBitmap != srcImage.handle) {
//      OS.DeleteObject(hBitmap);
//    }
//    OS.DeleteDC(srcHdc);
//
//    /* Release the HDC for the device */
//    device.internal_dispose_GC(hDC, null);
//  }
//
//  void drawBitmap(Image srcImage, int srcX, int srcY, int srcWidth,
//                  int srcHeight, int destX, int destY, int destWidth,
//                  int destHeight, boolean simple, BITMAP bm, int imgWidth,
//                  int imgHeight) {
//    int srcHdc = OS.CreateCompatibleDC(handle);
//    int oldSrcBitmap = OS.SelectObject(srcHdc, srcImage.handle);
//    int mode = 0, rop2 = 0;
//    if(!OS.IsWinCE) {
//      rop2 = OS.GetROP2(handle);
//      mode = OS.SetStretchBltMode(handle, OS.COLORONCOLOR);
//    } else {
//      rop2 = OS.SetROP2(handle, OS.R2_COPYPEN);
//      OS.SetROP2(handle, rop2);
//    }
//    int dwRop = rop2 == OS.R2_XORPEN ? OS.SRCINVERT : OS.SRCCOPY;
//    OS.StretchBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX,
//                  srcY, srcWidth, srcHeight, dwRop);
//    if(!OS.IsWinCE) {
//      OS.SetStretchBltMode(handle, mode);
//    }
//    OS.SelectObject(srcHdc, oldSrcBitmap);
//    OS.DeleteDC(srcHdc);
//  }

  /**
   * Draws a line, using the foreground color, between the points
   * (<code>x1</code>, <code>y1</code>) and (<code>x2</code>, <code>y2</code>).
   *
   * @param x1 the first point's x coordinate
   * @param y1 the first point's y coordinate
   * @param x2 the second point's x coordinate
   * @param y2 the second point's y coordinate
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawLine(int x1, int y1, int x2, int y2) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    swingHandle.drawLine(x1, y1, x2, y2);
    
//    if(OS.IsWinCE) {
//      int[] points = new int[] {
//          x1, y1, x2, y2};
//      OS.Polyline(handle, points, points.length / 2);
//    } else {
//      OS.MoveToEx(handle, x1, y1, 0);
//      OS.LineTo(handle, x2, y2);
//    }
//    OS.SetPixel(handle, x2, y2, OS.GetTextColor(handle));
  }

  /**
   * Draws the outline of an oval, using the foreground color,
   * within the specified rectangular area.
   * <p>
   * The result is a circle or ellipse that fits within the
   * rectangle specified by the <code>x</code>, <code>y</code>,
   * <code>width</code>, and <code>height</code> arguments.
   * </p><p>
   * The oval covers an area that is <code>width + 1</code>
   * pixels wide and <code>height + 1</code> pixels tall.
   * </p>
   *
   * @param x the x coordinate of the upper left corner of the oval to be drawn
   * @param y the y coordinate of the upper left corner of the oval to be drawn
   * @param width the width of the oval to be drawn
   * @param height the height of the oval to be drawn
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawOval(int x, int y, int width, int height) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
      // Check performance impact of always setting null brush. If the user has not
      // set the background color, we may not have to do this work?
    }
    swingHandle.drawOval(x, y, width, height);
    
//    int nullBrush = OS.GetStockObject(OS.NULL_BRUSH);
//    int oldBrush = OS.SelectObject(handle, nullBrush);
//    OS.Ellipse(handle, x, y, x + width + 1, y + height + 1);
//    OS.SelectObject(handle, oldBrush);
  }

  /**
   * Draws the closed polygon which is defined by the specified array
   * of integer coordinates, using the receiver's foreground color. The array
   * contains alternating x and y values which are considered to represent
   * points which are the vertices of the polygon. Lines are drawn between
   * each consecutive pair, and between the first pair and last pair in the
   * array.
   *
   * @param pointArray an array of alternating x and y values which are the vertices of the polygon
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawPolygon(int[] pointArray) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(pointArray == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    int[] xPoints = new int[pointArray.length/2];
    int[] yPoints = new int[xPoints.length];
    for(int i=0; i<xPoints.length; i++) {
      xPoints[i] = pointArray[i++];
      yPoints[i] = pointArray[i];
    }
    swingHandle.drawPolygon(xPoints, yPoints, xPoints.length);
    
//    int nullBrush = OS.GetStockObject(OS.NULL_BRUSH);
//    int oldBrush = OS.SelectObject(handle, nullBrush);
//    OS.Polygon(handle, pointArray, pointArray.length / 2);
//    OS.SelectObject(handle, oldBrush);
  }

  /**
   * Draws the polyline which is defined by the specified array
   * of integer coordinates, using the receiver's foreground color. The array
   * contains alternating x and y values which are considered to represent
   * points which are the corners of the polyline. Lines are drawn between
   * each consecutive pair, but not between the first pair and last pair in
   * the array.
   *
   * @param pointArray an array of alternating x and y values which are the corners of the polyline
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the point array is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawPolyline(int[] pointArray) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(pointArray == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    int[] xPoints = new int[pointArray.length/2];
    int[] yPoints = new int[xPoints.length];
    for(int i=0; i<xPoints.length; i++) {
      xPoints[i] = pointArray[i++];
      yPoints[i] = pointArray[i];
    }
    swingHandle.drawPolyline(xPoints, yPoints, xPoints.length);
//    OS.Polyline(handle, pointArray, pointArray.length / 2);
  }

  /**
   * Draws the outline of the rectangle specified by the arguments,
   * using the receiver's foreground color. The left and right edges
   * of the rectangle are at <code>x</code> and <code>x + width</code>.
       * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
   *
   * @param x the x coordinate of the rectangle to be drawn
   * @param y the y coordinate of the rectangle to be drawn
   * @param width the width of the rectangle to be drawn
   * @param height the height of the rectangle to be drawn
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawRectangle(int x, int y, int width, int height) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    swingHandle.drawRect(x, y, width, height);
//    int hOld = OS.SelectObject(handle, OS.GetStockObject(OS.NULL_BRUSH));
//    OS.Rectangle(handle, x, y, x + width + 1, y + height + 1);
//    OS.SelectObject(handle, hOld);
  }

  /**
   * Draws the outline of the specified rectangle, using the receiver's
   * foreground color. The left and right edges of the rectangle are at
   * <code>rect.x</code> and <code>rect.x + rect.width</code>. The top
   * and bottom edges are at <code>rect.y</code> and
   * <code>rect.y + rect.height</code>.
   *
   * @param rect the rectangle to draw
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawRectangle(Rectangle rect) {
    if(rect == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    drawRectangle(rect.x, rect.y, rect.width, rect.height);
  }

  /**
   * Draws the outline of the round-cornered rectangle specified by
   * the arguments, using the receiver's foreground color. The left and
   * right edges of the rectangle are at <code>x</code> and <code>x + width</code>.
       * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
   * The <em>roundness</em> of the corners is specified by the
   * <code>arcWidth</code> and <code>arcHeight</code> arguments.
   *
   * @param x the x coordinate of the rectangle to be drawn
   * @param y the y coordinate of the rectangle to be drawn
   * @param width the width of the rectangle to be drawn
   * @param height the height of the rectangle to be drawn
   * @param arcWidth the horizontal diameter of the arc at the four corners
   * @param arcHeight the vertical diameter of the arc at the four corners
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawRoundRectangle(int x, int y, int width, int height,
                                 int arcWidth, int arcHeight) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    swingHandle.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    
//    if(OS.IsWinCE) {
//      /*
//       * Bug in WinCE PPC.  On certain devices, RoundRect does not draw
//       * all the pixels.  The workaround is to draw a round rectangle
//       * using lines and arcs.
//       */
//      if(width == 0 || height == 0) {
//        return;
//      }
//      if(arcWidth == 0 || arcHeight == 0) {
//        drawRectangle(x, y, width, height);
//        return;
//      }
//      if(width < 0) {
//        x += width;
//        width = -width;
//      }
//      if(height < 0) {
//        y += height;
//        height = -height;
//      }
//      ;
//      if(arcWidth < 0) {
//        arcWidth = -arcWidth;
//      }
//      if(arcHeight < 0) {
//        arcHeight = -arcHeight;
//      }
//      if(arcWidth > width) {
//        arcWidth = width;
//      }
//      if(arcHeight > height) {
//        arcHeight = height;
//
//      }
//      if(arcWidth < width) {
//        drawLine(x + arcWidth / 2, y, x + width - arcWidth / 2, y);
//        drawLine(x + arcWidth / 2, y + height - 1, x + width - arcWidth / 2,
//                 y + height - 1);
//      }
//      if(arcHeight < height) {
//        drawLine(x, y + arcHeight / 2, x, y + height - arcHeight / 2);
//        drawLine(x + width - 1, y + arcHeight / 2, x + width - 1,
//                 y + height - arcHeight / 2);
//      }
//      if(arcWidth != 0 && arcHeight != 0) {
//        drawArc(x, y, arcWidth, arcHeight, 90, 90);
//        drawArc(x + width - arcWidth - 1, y, arcWidth, arcHeight, 0, 90);
//        drawArc(x + width - arcWidth - 1, y + height - arcHeight - 1, arcWidth,
//                arcHeight, 0, -90);
//        drawArc(x, y + height - arcHeight - 1, arcWidth, arcHeight, 180, 90);
//      }
//    } else {
//      int nullBrush = OS.GetStockObject(OS.NULL_BRUSH);
//      int oldBrush = OS.SelectObject(handle, nullBrush);
//      OS.RoundRect(handle, x, y, x + width, y + height, arcWidth, arcHeight);
//      OS.SelectObject(handle, oldBrush);
//    }
  }

  /**
   * Draws the given string, using the receiver's current font and
   * foreground color. No tab expansion or carriage return processing
   * will be performed. The background of the rectangular area where
   * the string is being drawn will be filled with the receiver's
   * background color.
   *
   * @param string the string to be drawn
   * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
   * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawString(String string, int x, int y) {
    drawString(string, x, y, false);
  }

  /**
   * Draws the given string, using the receiver's current font and
   * foreground color. No tab expansion or carriage return processing
   * will be performed. If <code>isTransparent</code> is <code>true</code>,
   * then the background of the rectangular area where the string is being
   * drawn will not be modified, otherwise it will be filled with the
   * receiver's background color.
   *
   * @param string the string to be drawn
   * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
   * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
   * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawString(String string, int x, int y, boolean isTransparent) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(string == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(!isTransparent) {
      Point extent = stringExtent(string);
      fillRectangle(x, y, extent.x, extent.y);
//      java.awt.Color oldColor = swingHandle.getColor();
////      swingHandle.setColor(java.awt.Color.red);
//      swingHandle.setColor(background.swingHandle);
//      Point extent = stringExtent(string);
//      swingHandle.fillRect(x, y, extent.x, extent.y);
//      swingHandle.setColor(oldColor);
    }
    swingHandle.drawString(string, x, y + swingHandle.getFontMetrics().getMaxAscent());
  }

  /**
   * Draws the given string, using the receiver's current font and
   * foreground color. Tab expansion and carriage return processing
   * are performed. The background of the rectangular area where
   * the text is being drawn will be filled with the receiver's
   * background color.
   *
   * @param string the string to be drawn
   * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
   * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawText(String string, int x, int y) {
    drawText(string, x, y, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
  }

  /**
   * Draws the given string, using the receiver's current font and
   * foreground color. Tab expansion and carriage return processing
   * are performed. If <code>isTransparent</code> is <code>true</code>,
   * then the background of the rectangular area where the text is being
   * drawn will not be modified, otherwise it will be filled with the
   * receiver's background color.
   *
   * @param string the string to be drawn
   * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
   * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
   * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawText(String string, int x, int y, boolean isTransparent) {
    int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB;
    if(isTransparent) {
      flags |= SWT.DRAW_TRANSPARENT;
    }
    drawText(string, x, y, flags);
  }

  /**
   * Draws the given string, using the receiver's current font and
   * foreground color. Tab expansion, line delimiter and mnemonic
   * processing are performed according to the specified flags. If
   * <code>flags</code> includes <code>DRAW_TRANSPARENT</code>,
   * then the background of the rectangular area where the text is being
   * drawn will not be modified, otherwise it will be filled with the
   * receiver's background color.
   * <p>
   * The parameter <code>flags</code> may be a combination of:
   * <dl>
   * <dt><b>DRAW_DELIMITER</b></dt>
   * <dd>draw multiple lines</dd>
   * <dt><b>DRAW_TAB</b></dt>
   * <dd>expand tabs</dd>
   * <dt><b>DRAW_MNEMONIC</b></dt>
   * <dd>underline the mnemonic character</dd>
   * <dt><b>DRAW_TRANSPARENT</b></dt>
   * <dd>transparent background</dd>
   * </dl>
   * </p>
   *
   * @param string the string to be drawn
   * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
   * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
   * @param flags the flags specifing how to process the text
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void drawText(String string, int x, int y, int flags) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(string == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(string.length() == 0) {
      return;
    }
    
    // TODO: find how to really implement this method (for now, just do not handle delimiters...
    if((flags & SWT.DRAW_TRANSPARENT) == 0) {
      java.awt.Color oldColor = swingHandle.getColor();
      swingHandle.setColor(background.swingHandle);
      Point extent = stringExtent(string);
      fillRectangle(x, y, extent.x, extent.y);
      swingHandle.setColor(oldColor);
    }
    drawString(string, x, y);
    
//    TCHAR buffer = new TCHAR(getCodePage(), string, false);
//    int length = buffer.length();
//    if(length == 0) {
//      return;
//    }
//    RECT rect = new RECT();
//    OS.SetRect(rect, x, y, 0x7FFF, 0x7FFF);
//    int uFormat = OS.DT_LEFT;
//    if((flags & SWT.DRAW_DELIMITER) == 0) {
//      uFormat |= OS.DT_SINGLELINE;
//    }
//    if((flags & SWT.DRAW_TAB) != 0) {
//      uFormat |= OS.DT_EXPANDTABS;
//    }
//    if((flags & SWT.DRAW_MNEMONIC) == 0) {
//      uFormat |= OS.DT_NOPREFIX;
//    }
//    int oldBkMode = OS.SetBkMode(handle,
//                                 (flags & SWT.DRAW_TRANSPARENT) != 0 ?
//                                 OS.TRANSPARENT : OS.OPAQUE);
//    OS.DrawText(handle, buffer, length, rect, uFormat);
//    OS.SetBkMode(handle, oldBkMode);
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
    return(object == this) ||
        ((object instanceof GC) && (swingHandle == ((GC)object).swingHandle));
  }

  /**
   * Fills the interior of a circular or elliptical arc within
   * the specified rectangular area, with the receiver's background
   * color.
   * <p>
   * The resulting arc begins at <code>startAngle</code> and extends
   * for <code>arcAngle</code> degrees, using the current color.
   * Angles are interpreted such that 0 degrees is at the 3 o'clock
   * position. A positive value indicates a counter-clockwise rotation
   * while a negative value indicates a clockwise rotation.
   * </p><p>
   * The center of the arc is the center of the rectangle whose origin
   * is (<code>x</code>, <code>y</code>) and whose size is specified by the
   * <code>width</code> and <code>height</code> arguments.
   * </p><p>
   * The resulting arc covers an area <code>width + 1</code> pixels wide
   * by <code>height + 1</code> pixels tall.
   * </p>
   *
   * @param x the x coordinate of the upper-left corner of the arc to be filled
   * @param y the y coordinate of the upper-left corner of the arc to be filled
   * @param width the width of the arc to be filled
   * @param height the height of the arc to be filled
   * @param startAngle the beginning angle
   * @param arcAngle the angular extent of the arc, relative to the start angle
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if any of the width, height or endAngle is zero.</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #drawArc
   */
  public void fillArc(int x, int y, int width, int height, int startAngle,
                      int endAngle) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(width < 0) {
      x = x + width;
      width = -width;
    }
    if(height < 0) {
      y = y + height;
      height = -height;
    }
    if(width == 0 || height == 0 || endAngle == 0) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    java.awt.Color oldColor = swingHandle.getColor();
    swingHandle.setColor(background.swingHandle);
    swingHandle.fillArc(x, y, width, height, startAngle, endAngle < 0? endAngle + startAngle: endAngle - startAngle);
    swingHandle.setColor(oldColor);
//    int x1, y1, x2, y2, tmp;
//    boolean isNegative;
//    if(endAngle >= 360 || endAngle <= -360) {
//      x1 = x2 = x + width;
//      y1 = y2 = y + height / 2;
//    } else {
//      isNegative = endAngle < 0;
//
//      endAngle = endAngle + startAngle;
//      if(isNegative) {
//        // swap angles
//        tmp = startAngle;
//        startAngle = endAngle;
//        endAngle = tmp;
//      }
//      x1 = Compatibility.cos(startAngle, width) + x + width / 2;
//      y1 = -1 * Compatibility.sin(startAngle, height) + y + height / 2;
//
//      x2 = Compatibility.cos(endAngle, width) + x + width / 2;
//      y2 = -1 * Compatibility.sin(endAngle, height) + y + height / 2;
//    }
//
//    int nullPen = OS.GetStockObject(OS.NULL_PEN);
//    int oldPen = OS.SelectObject(handle, nullPen);
//    OS.Pie(handle, x, y, x + width + 1, y + height + 1, x1, y1, x2, y2);
//    OS.SelectObject(handle, oldPen);
  }

  /**
   * Fills the interior of the specified rectangle with a gradient
   * sweeping from left to right or top to bottom progressing
   * from the receiver's foreground color to its background color.
   *
   * @param x the x coordinate of the rectangle to be filled
   * @param y the y coordinate of the rectangle to be filled
   * @param width the width of the rectangle to be filled, may be negative
   *        (inverts direction of gradient if horizontal)
   * @param height the height of the rectangle to be filled, may be negative
   *        (inverts direction of gradient if vertical)
   * @param vertical if true sweeps from top to bottom, else
   *        sweeps from left to right
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #drawRectangle
   */
  public void fillGradientRectangle(int x, int y, int width, int height,
                                    boolean vertical) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(width == 0 || height == 0) {
      return;
    }
    java.awt.Color fromColor = swingHandle.getColor();
    java.awt.Color toColor = background.swingHandle;

    // Should always be true with swing components
    if(swingHandle instanceof Graphics2D) {
      // TODO: check that it works :)
      Graphics2D g2D = ((Graphics2D)swingHandle);
      Paint oldPaint = g2D.getPaint();
      java.awt.Point p1 = null;
      java.awt.Point p2 = null;
      if(vertical) {
        p1 = new java.awt.Point(x + width/2, y);
        p2 = new java.awt.Point(x + width/2, y + height);
      }
      g2D.setPaint(new GradientPaint(p1, fromColor, p2, toColor));
      g2D.fillRect(x, y, width, height);
      g2D.setPaint(oldPaint);
    } else {
      // TODO: determine if do nothing, or paint something. Perhaps an intermediate between from and to colors...
      java.awt.Color oldColor = swingHandle.getColor();
      swingHandle.setColor(toColor);
      swingHandle.fillRect(x, y, width, height);
      swingHandle.setColor(oldColor);
    }

//    boolean swapColors = false;
//    if(width < 0) {
//      x += width;
//      width = -width;
//      if(!vertical) {
//        swapColors = true;
//      }
//    }
//    if(height < 0) {
//      y += height;
//      height = -height;
//      if(vertical) {
//        swapColors = true;
//      }
//    }
//    if(swapColors) {
//      final int t = fromColor;
//      fromColor = toColor;
//      toColor = t;
//    }
//    final RGB fromRGB = new RGB(fromColor & 0xff, (fromColor >>> 8) & 0xff,
//                                (fromColor >>> 16) & 0xff);
//    final RGB toRGB = new RGB(toColor & 0xff, (toColor >>> 8) & 0xff,
//                              (toColor >>> 16) & 0xff);
//    if((fromRGB.red == toRGB.red) && (fromRGB.green == toRGB.green) &&
//       (fromRGB.blue == toRGB.blue)) {
//      OS.PatBlt(handle, x, y, width, height, OS.PATCOPY);
//      return;
//    }
//
//    /* Use GradientFill if supported, only on Windows 98, 2000 and newer */
//    if(!OS.IsWinCE) {
//      final int hHeap = OS.GetProcessHeap();
//      final int pMesh = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY,
//                                     GRADIENT_RECT.sizeof +
//                                     TRIVERTEX.sizeof * 2);
//      final int pVertex = pMesh + GRADIENT_RECT.sizeof;
//
//      GRADIENT_RECT gradientRect = new GRADIENT_RECT();
//      gradientRect.UpperLeft = 0;
//      gradientRect.LowerRight = 1;
//      OS.MoveMemory(pMesh, gradientRect, GRADIENT_RECT.sizeof);
//
//      TRIVERTEX trivertex = new TRIVERTEX();
//      trivertex.x = x;
//      trivertex.y = y;
//      trivertex.Red = (short)((fromRGB.red << 8) | fromRGB.red);
//      trivertex.Green = (short)((fromRGB.green << 8) | fromRGB.green);
//      trivertex.Blue = (short)((fromRGB.blue << 8) | fromRGB.blue);
//      trivertex.Alpha = -1;
//      OS.MoveMemory(pVertex, trivertex, TRIVERTEX.sizeof);
//
//      trivertex.x = x + width;
//      trivertex.y = y + height;
//      trivertex.Red = (short)((toRGB.red << 8) | toRGB.red);
//      trivertex.Green = (short)((toRGB.green << 8) | toRGB.green);
//      trivertex.Blue = (short)((toRGB.blue << 8) | toRGB.blue);
//      trivertex.Alpha = -1;
//      OS.MoveMemory(pVertex + TRIVERTEX.sizeof, trivertex, TRIVERTEX.sizeof);
//
//      boolean success = OS.GradientFill(handle, pVertex, 2, pMesh, 1,
//                                        vertical ? OS.GRADIENT_FILL_RECT_V :
//                                        OS.GRADIENT_FILL_RECT_H);
//      OS.HeapFree(hHeap, 0, pMesh);
//      if(success) {
//        return;
//      }
//    }
//
//    final int depth = OS.GetDeviceCaps(handle, OS.BITSPIXEL);
//    final int bitResolution = (depth >= 24) ? 8 : (depth >= 15) ? 5 : 0;
//    ImageData.fillGradientRectangle(this, data.device,
//                                    x, y, width, height, vertical, fromRGB,
//                                    toRGB,
//                                    bitResolution, bitResolution, bitResolution);
  }

  /**
   * Fills the interior of an oval, within the specified
   * rectangular area, with the receiver's background
   * color.
   *
       * @param x the x coordinate of the upper left corner of the oval to be filled
       * @param y the y coordinate of the upper left corner of the oval to be filled
   * @param width the width of the oval to be filled
   * @param height the height of the oval to be filled
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #drawOval
   */
  public void fillOval(int x, int y, int width, int height) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    java.awt.Color oldColor = swingHandle.getColor();
    swingHandle.setColor(background.swingHandle);
    swingHandle.fillOval(x, y, width, height);
    swingHandle.setColor(oldColor);
    
//    int nullPen = OS.GetStockObject(OS.NULL_PEN);
//    int oldPen = OS.SelectObject(handle, nullPen);
//    OS.Ellipse(handle, x, y, x + width + 1, y + height + 1);
//    OS.SelectObject(handle, oldPen);
  }

  /**
   * Fills the interior of the closed polygon which is defined by the
   * specified array of integer coordinates, using the receiver's
   * background color. The array contains alternating x and y values
   * which are considered to represent points which are the vertices of
   * the polygon. Lines are drawn between each consecutive pair, and
   * between the first pair and last pair in the array.
   *
   * @param pointArray an array of alternating x and y values which are the vertices of the polygon
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #drawPolygon
   */
  public void fillPolygon(int[] pointArray) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    int[] xPoints = new int[pointArray.length/2];
    int[] yPoints = new int[xPoints.length];
    for(int i=0; i<xPoints.length; i++) {
      xPoints[i] = pointArray[i++];
      yPoints[i] = pointArray[i];
    }
    java.awt.Color oldColor = swingHandle.getColor();
    swingHandle.setColor(background.swingHandle);
    swingHandle.fillPolygon(xPoints, yPoints, xPoints.length);
    swingHandle.setColor(oldColor);
//    int nullPen = OS.GetStockObject(OS.NULL_PEN);
//    int oldPen = OS.SelectObject(handle, nullPen);
//    OS.Polygon(handle, pointArray, pointArray.length / 2);
//    OS.SelectObject(handle, oldPen);
  }

  /**
   * Fills the interior of the rectangle specified by the arguments,
   * using the receiver's background color.
   *
   * @param x the x coordinate of the rectangle to be filled
   * @param y the y coordinate of the rectangle to be filled
   * @param width the width of the rectangle to be filled
   * @param height the height of the rectangle to be filled
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #drawRectangle
   */
  public void fillRectangle(int x, int y, int width, int height) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    java.awt.Color oldColor = swingHandle.getColor();
    swingHandle.setColor(background.swingHandle);
    swingHandle.fillRect(x, y, width, height);
    swingHandle.setColor(oldColor);
//    int rop2 = 0;
//    if(OS.IsWinCE) {
//      rop2 = OS.SetROP2(handle, OS.R2_COPYPEN);
//      OS.SetROP2(handle, rop2);
//    } else {
//      rop2 = OS.GetROP2(handle);
//    }
//    int dwRop = rop2 == OS.R2_XORPEN ? OS.PATINVERT : OS.PATCOPY;
//    OS.PatBlt(handle, x, y, width, height, dwRop);
  }

  /**
   * Fills the interior of the specified rectangle, using the receiver's
   * background color.
   *
   * @param rectangle the rectangle to be filled
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #drawRectangle
   */
  public void fillRectangle(Rectangle rect) {
    if(rect == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    fillRectangle(rect.x, rect.y, rect.width, rect.height);
  }

  /**
   * Fills the interior of the round-cornered rectangle specified by
   * the arguments, using the receiver's background color.
   *
   * @param x the x coordinate of the rectangle to be filled
   * @param y the y coordinate of the rectangle to be filled
   * @param width the width of the rectangle to be filled
   * @param height the height of the rectangle to be filled
   * @param arcWidth the horizontal diameter of the arc at the four corners
   * @param arcHeight the vertical diameter of the arc at the four corners
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #drawRoundRectangle
   */
  public void fillRoundRectangle(int x, int y, int width, int height,
                                 int arcWidth, int arcHeight) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    java.awt.Color oldColor = swingHandle.getColor();
    swingHandle.setColor(background.swingHandle);
    swingHandle.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    swingHandle.setColor(oldColor);
//    int nullPen = OS.GetStockObject(OS.NULL_PEN);
//    int oldPen = OS.SelectObject(handle, nullPen);
//    OS.RoundRect(handle, x, y, x + width, y + height, arcWidth, arcHeight);
//    OS.SelectObject(handle, oldPen);
  }

  /**
   * Returns the <em>advance width</em> of the specified character in
   * the font which is currently selected into the receiver.
   * <p>
   * The advance width is defined as the horizontal distance the cursor
   * should move after printing the character in the selected font.
   * </p>
   *
   * @param ch the character to measure
   * @return the distance in the x direction to move past the character before painting the next
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public int getAdvanceWidth(char ch) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return swingHandle.getFontMetrics().charWidth(ch);
//    if(OS.IsWinCE) {
//      SIZE size = new SIZE();
//      OS.GetTextExtentPoint32W(handle, new char[] {ch}
//                               , 1, size);
//      return size.cx;
//    }
//    int tch = ch;
//    if(ch > 0x7F) {
//      TCHAR buffer = new TCHAR(getCodePage(), ch, false);
//      tch = buffer.tcharAt(0);
//    }
//    int[] width = new int[1];
//    OS.GetCharWidth(handle, tch, tch, width);
//    return width[0];
  }

  /**
   * Returns the background color.
   *
   * @return the receiver's background color
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public Color getBackground() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    java.awt.Color color = swingHandle.getColor();
    return new Color(device, color.getRed(), color.getGreen(), color.getBlue());
//    int color = OS.GetBkColor(handle);
//    if(color == OS.CLR_INVALID) {
//      color = OS.GetSysColor(OS.COLOR_WINDOW);
//    }
//    return Color.win32_new(data.device, color);
  }

  /**
   * Returns the width of the specified character in the font
   * selected into the receiver.
   * <p>
   * The width is defined as the space taken up by the actual
   * character, not including the leading and tailing whitespace
   * or overhang.
   * </p>
   *
   * @param ch the character to measure
   * @return the width of the character
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public int getCharWidth(char ch) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    // TODO: find the difference between advance width and char width
    return swingHandle.getFontMetrics().charWidth(ch);
//    /* GetCharABCWidths only succeeds on truetype fonts */
//    if(!OS.IsWinCE) {
//      int tch = ch;
//      if(ch > 0x7F) {
//        TCHAR buffer = new TCHAR(getCodePage(), ch, false);
//        tch = buffer.tcharAt(0);
//      }
//      int[] width = new int[3];
//      if(OS.GetCharABCWidths(handle, tch, tch, width)) {
//        return width[1];
//      }
//    }
//
//    /* It wasn't a truetype font */
//    TEXTMETRIC tm = new TEXTMETRIC();
//    OS.GetTextMetricsW(handle, tm);
//    SIZE size = new SIZE();
//    OS.GetTextExtentPoint32W(handle, new char[] {ch}
//                             , 1, size);
//    return size.cx - tm.tmOverhang;
  }

  /**
   * Returns the bounding rectangle of the receiver's clipping
   * region. If no clipping region is set, the return value
   * will be a rectangle which covers the entire bounds of the
   * object the receiver is drawing on.
   *
   * @return the bounding rectangle of the clipping region
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public Rectangle getClipping() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    java.awt.Rectangle clipping = swingHandle.getClipBounds();
    return new Rectangle((int)clipping.getX(), (int)clipping.getY(), (int)clipping.getWidth(), (int)clipping.getHeight());
  }

  /**
   * Sets the region managed by the argument to the current
   * clipping region of the receiver.
   *
   * @param region the region to fill with the clipping region
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the region is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void getClipping(Region region) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(region == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    region.add(new Region(swingHandle.getClip()));
  }

//  int getCodePage() {
//    if(OS.IsWinCE) {
//      return OS.GetACP();
//    }
//    int[] lpCs = new int[8];
//    int cs = OS.GetTextCharset(handle);
//    OS.TranslateCharsetInfo(cs, lpCs, OS.TCI_SRCCHARSET);
//    return lpCs[1];
//  }

  /**
   * Returns the font currently being used by the receiver
   * to draw and measure text.
   *
   * @return the receiver's font
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public Font getFont() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return Font.swing_new(device, swingHandle.getFont());
  }

  /**
   * Returns a FontMetrics which contains information
   * about the font currently being used by the receiver
   * to draw and measure text.
   *
   * @return font metrics for the receiver's font
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public FontMetrics getFontMetrics() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return FontMetrics.swing_new(swingHandle.getFontMetrics());
  }

  Color background = null;

  /**
   * Returns the receiver's foreground color.
   *
   * @return the color used for drawing foreground things
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public Color getForeground() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return Color.swing_new(device, swingHandle.getColor());
//    int color = OS.GetTextColor(handle);
//    if(color == OS.CLR_INVALID) {
//      color = OS.GetSysColor(OS.COLOR_WINDOWTEXT);
//    }
//    return Color.win32_new(data.device, color);
  }

  /**
   * Returns the receiver's line style, which will be one
   * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
   * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
   * <code>SWT.LINE_DASHDOTDOT</code>.
   *
   * @return the style used for drawing lines
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public int getLineStyle() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    float[] dashArray = getCurrentBasicStroke().getDashArray();
    if(dashArray == null) {
      return SWT.LINE_SOLID;
    } else if(dashArray == lineDashArray) {
      return SWT.LINE_DASH;
    } else if(dashArray == lineDotArray) {
      return SWT.LINE_DOT;
    } else if(dashArray == lineDashDotArray) {
      return SWT.LINE_DASHDOT;
    } else if(dashArray == lineDashDotDotArray) {
      return SWT.LINE_DASHDOTDOT;
    }
    return SWT.LINE_SOLID;
  }

  /**
   * Returns the width that will be used when drawing lines
   * for all of the figure drawing operations (that is,
   * <code>drawLine</code>, <code>drawRectangle</code>,
   * <code>drawPolyline</code>, and so forth.
   *
   * @return the receiver's line width
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public int getLineWidth() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return (int)getCurrentBasicStroke().getLineWidth();
  }

  /**
   * Returns the receiver's style information.
   * <p>
   * Note that the value which is returned by this method <em>may
   * not match</em> the value which was provided to the constructor
   * when the receiver was created. This can occur when the underlying
   * operating system does not support a particular combination of
   * requested styles. 
   * </p>
   *
   * @return the style bits
   *  
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *   
   * @since 3.0
   */
  public int getStyle () {
    // TODO: implement method content
    throw new IllegalStateException("Not yet implemented!");
//    if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
//    return data.style;
  }

  boolean isXORMode = false;

  /**
   * Returns <code>true</code> if this GC is drawing in the mode
   * where the resulting color in the destination is the
   * <em>exclusive or</em> of the color values in the source
   * and the destination, and <code>false</code> if it is
   * drawing in the mode where the destination color is being
   * replaced with the source color value.
   *
   * @return <code>true</code> true if the receiver is in XOR mode, and false otherwise
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public boolean getXORMode() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    // TODO: Not a solution because if swing is used directly in future implementation, this does not detect.
    return isXORMode;
//    int rop2 = 0;
//    if(OS.IsWinCE) {
//      rop2 = OS.SetROP2(handle, OS.R2_COPYPEN);
//      OS.SetROP2(handle, rop2);
//    } else {
//      rop2 = OS.GetROP2(handle);
//    }
//    return rop2 == OS.R2_XORPEN;
  }

  void init(Drawable drawable) {
    this.drawable = drawable;
    swingHandle = ((Drawable2)drawable).internal_new_GC();
    device = ((Drawable2)drawable).internal_get_Device();
    if(device == null) {
      device = Device.getDevice();
    }
    if(device == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(drawable instanceof Control) {
      background = Color.swing_new(device, ((Control)drawable).swingHandle.getBackground());
    } else {
      background = Color.swing_new(device, swingHandle.getColor());
    }

//    
//    int foreground = data.foreground;
//    if(foreground != -1 && OS.GetTextColor(hDC) != foreground) {
//      OS.SetTextColor(hDC, foreground);
//      int hPen = OS.CreatePen(OS.PS_SOLID, 0, foreground);
//      OS.SelectObject(hDC, hPen);
//    }
//    int background = data.background;
//    if(background != -1 && OS.GetBkColor(hDC) != background) {
//      OS.SetBkColor(hDC, background);
//      int hBrush = OS.CreateSolidBrush(background);
//      OS.SelectObject(hDC, hBrush);
//    }
//    int hFont = data.hFont;
//    if(hFont != 0) {
//      OS.SelectObject(hDC, hFont);
//    }
//    int hPalette = data.device.hPalette;
//    if(hPalette != 0) {
//      OS.SelectPalette(hDC, hPalette, true);
//      OS.RealizePalette(hDC);
//    }
//    Image image = data.image;
//    if(image != null) {
//      data.hNullBitmap = OS.SelectObject(hDC, image.handle);
//      image.memGC = this;
//    }
//    this.drawable = drawable;
//    this.data = data;
//    handle = hDC;
  }

  /**
   * Returns an integer hash code for the receiver. Any two
   * objects which return <code>true</code> when passed to
   * <code>equals</code> must return the same value for this
   * method.
   *
   * @return the receiver's hash
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   *
   * @see #equals
   */
  public int hashCode() {
    return handle;
  }

  /**
   * Returns <code>true</code> if the receiver has a clipping
   * region set into it, and <code>false</code> otherwise.
   * If this method returns false, the receiver will draw on all
   * available space in the destination. If it returns true,
   * it will draw only in the area that is covered by the region
   * that can be accessed with <code>getClipping(region)</code>.
   *
   * @return <code>true</code> if the GC has a clipping region, and <code>false</code> otherwise
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public boolean isClipped() {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    return swingHandle.getClip() != null;
//    int region = OS.CreateRectRgn(0, 0, 0, 0);
//    int result = OS.GetClipRgn(handle, region);
//    OS.DeleteObject(region);
//    return(result > 0);
  }

  /**
   * Returns <code>true</code> if the GC has been disposed,
   * and <code>false</code> otherwise.
   * <p>
   * This method gets the dispose state for the GC.
   * When a GC has been disposed, it is an error to
   * invoke any other method using the GC.
   *
   * @return <code>true</code> when the GC is disposed and <code>false</code> otherwise
   */
  public boolean isDisposed() {
    return swingHandle == null;
  }

  /**
   * Sets the background color. The background color is used
   * for fill operations and as the background color when text
   * is drawn.
   *
   * @param color the new background color for the receiver
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void setBackground(Color color) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(color == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(color.isDisposed()) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    background = color;
//    if(OS.GetBkColor(handle) == color.handle) {
//      return;
//    }
//    OS.SetBkColor(handle, color.handle);
//    int newBrush = OS.CreateSolidBrush(color.handle);
//    int oldBrush = OS.SelectObject(handle, newBrush);
//    OS.DeleteObject(oldBrush);
  }

  /**
   * Sets the area of the receiver which can be changed
   * by drawing operations to the rectangular area specified
   * by the arguments.
   *
   * @param x the x coordinate of the clipping rectangle
   * @param y the y coordinate of the clipping rectangle
   * @param width the width of the clipping rectangle
   * @param height the height of the clipping rectangle
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void setClipping(int x, int y, int width, int height) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    swingHandle.setClip(x, y, width, height);
  }

  /**
   * Sets the area of the receiver which can be changed
   * by drawing operations to the rectangular area specified
   * by the argument.
   *
   * @param rect the clipping rectangle
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void setClipping(Rectangle rect) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(rect == null) {
      OS.SelectClipRgn(handle, 0);
      return;
    }
    setClipping(rect.x, rect.y, rect.width, rect.height);
  }

  /**
   * Sets the area of the receiver which can be changed
   * by drawing operations to the region specified
   * by the argument.
   *
   * @param rect the clipping region.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void setClipping(Region region) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    swingHandle.setClip(region.swingHandle);
  }

  /**
   * Sets the font which will be used by the receiver
   * to draw and measure text to the argument. If the
   * argument is null, then a default font appropriate
   * for the platform will be used instead.
   *
   * @param font the new font for the receiver, or null to indicate a default font
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */

  public void setFont(Font font) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(font == null) {
      swingHandle.setFont(device.swingSystemFont);
    } else {
      if(font.isDisposed()) {
        SWT.error(SWT.ERROR_INVALID_ARGUMENT);
      }
      swingHandle.setFont(font.swingHandle);
    }
  }

  /**
   * Sets the foreground color. The foreground color is used
   * for drawing operations including when text is drawn.
   *
   * @param color the new foreground color for the receiver
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void setForeground(Color color) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(color == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    if(color.isDisposed()) {
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
    swingHandle.setColor(color.swingHandle);
//    if(OS.GetTextColor(handle) == color.handle) {
//      return;
//    }
//    int hPen = OS.GetCurrentObject(handle, OS.OBJ_PEN);
//    LOGPEN logPen = new LOGPEN();
//    OS.GetObject(hPen, LOGPEN.sizeof, logPen);
//    OS.SetTextColor(handle, color.handle);
//    int newPen = OS.CreatePen(logPen.lopnStyle, logPen.x, color.handle);
//    int oldPen = OS.SelectObject(handle, newPen);
//    OS.DeleteObject(oldPen);
  }

  static final float[] lineDashArray = new float[] {18, 6};
  static final float[] lineDotArray = new float[] {3, 3};
  static final float[] lineDashDotArray = new float[] {9, 6, 3, 6};
  static final float[] lineDashDotDotArray = new float[] {9, 3, 3, 3, 3, 3};

  /**
   * Sets the receiver's line style to the argument, which must be one
   * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
   * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
   * <code>SWT.LINE_DASHDOTDOT</code>.
   *
   * @param lineStyle the style to be used for drawing lines
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void setLineStyle(int lineStyle) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(swingHandle instanceof Graphics2D) {
      Graphics2D g2D = ((Graphics2D)swingHandle);
      BasicStroke stroke = getCurrentBasicStroke();
      float[] array = null;
      switch(lineStyle) {
        case SWT.LINE_SOLID:
          break;
        case SWT.LINE_DASH:
          array = lineDashArray;
          break;
        case SWT.LINE_DOT:
          array = lineDotArray;
          break;
        case SWT.LINE_DASHDOT:
          array = lineDashDotArray;
          break;
        case SWT.LINE_DASHDOTDOT:
          array = lineDashDotDotArray;
          break;
        default:
          SWT.error(SWT.ERROR_INVALID_ARGUMENT);
      }
      g2D.setStroke(new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), array, 0));
    }
  }
  
  BasicStroke getCurrentBasicStroke() {
    if(swingHandle instanceof Graphics2D) {
      Graphics2D g2d = (Graphics2D)swingHandle;
      Stroke stroke = g2d.getStroke();
      if(stroke instanceof BasicStroke) {
        return (BasicStroke)stroke;
      }
    }
    return new BasicStroke();
  }

  /**
   * Sets the width that will be used when drawing lines
   * for all of the figure drawing operations (that is,
   * <code>drawLine</code>, <code>drawRectangle</code>,
   * <code>drawPolyline</code>, and so forth.
   *
   * @param lineWidth the width of a line
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void setLineWidth(int lineWidth) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(swingHandle instanceof Graphics2D) {
      Graphics2D g2D = ((Graphics2D)swingHandle);
      BasicStroke stroke = getCurrentBasicStroke();
      g2D.setStroke(new BasicStroke(lineWidth, stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), stroke.getDashArray(), stroke.getDashPhase()));
    }
  }

  /**
   * If the argument is <code>true</code>, puts the receiver
   * in a drawing mode where the resulting color in the destination
   * is the <em>exclusive or</em> of the color values in the source
   * and the destination, and if the argument is <code>false</code>,
   * puts the receiver in a drawing mode where the destination color
   * is replaced with the source color value.
   *
   * @param xor if <code>true</code>, then <em>xor</em> mode is used, otherwise <em>source copy</em> mode is used
   *
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public void setXORMode(boolean xor) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(xor) {
      swingHandle.setXORMode(java.awt.Color.white);
    } else {
      swingHandle.setPaintMode();
    }
    isXORMode = xor;
  }

  /**
   * Returns the extent of the given string. No tab
   * expansion or carriage return processing will be performed.
   * <p>
   * The <em>extent</em> of a string is the width and height of
   * the rectangular area it would cover if drawn in a particular
   * font (in this case, the current font in the receiver).
   * </p>
   *
   * @param string the string to measure
   * @return a point containing the extent of the string
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public Point stringExtent(String string) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(string == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    java.awt.FontMetrics fm = swingHandle.getFontMetrics();
    return new Point(fm.stringWidth(string), fm.getMaxAscent() + fm.getMaxDescent());
//    java.awt.geom.Rectangle2D bounds = swingHandle.getFontMetrics().getStringBounds(string, swingHandle);
//    return new Point((int)(bounds.getWidth() - bounds.getX()), (int)(bounds.getHeight() - bounds.getY()));

//    SIZE size = new SIZE();
//    int length = string.length();
//    if(length == 0) {
////		OS.GetTextExtentPoint32(handle, SPACE, SPACE.length(), size);
//      OS.GetTextExtentPoint32W(handle, new char[] {' '}
//                               , 1, size);
//      return new Point(0, size.cy);
//    } else {
////		TCHAR buffer = new TCHAR (getCodePage(), string, false);
//      char[] buffer = new char[length];
//      string.getChars(0, length, buffer, 0);
//      OS.GetTextExtentPoint32W(handle, buffer, length, size);
//      return new Point(size.cx, size.cy);
//    }
  }

  /**
   * Returns the extent of the given string. Tab expansion and
   * carriage return processing are performed.
   * <p>
   * The <em>extent</em> of a string is the width and height of
   * the rectangular area it would cover if drawn in a particular
   * font (in this case, the current font in the receiver).
   * </p>
   *
   * @param string the string to measure
   * @return a point containing the extent of the string
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public Point textExtent(String string) {
    return textExtent(string, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
  }

  /**
   * Returns the extent of the given string. Tab expansion, line
   * delimiter and mnemonic processing are performed according to
   * the specified flags, which can be a combination of:
   * <dl>
   * <dt><b>DRAW_DELIMITER</b></dt>
   * <dd>draw multiple lines</dd>
   * <dt><b>DRAW_TAB</b></dt>
   * <dd>expand tabs</dd>
   * <dt><b>DRAW_MNEMONIC</b></dt>
   * <dd>underline the mnemonic character</dd>
   * <dt><b>DRAW_TRANSPARENT</b></dt>
   * <dd>transparent background</dd>
   * </dl>
   * <p>
   * The <em>extent</em> of a string is the width and height of
   * the rectangular area it would cover if drawn in a particular
   * font (in this case, the current font in the receiver).
   * </p>
   *
   * @param string the string to measure
   * @param flags the flags specifing how to process the text
   * @return a point containing the extent of the string
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
   * </ul>
   */
  public Point textExtent(String string, int flags) {
    if(isDisposed()) {
      SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    }
    if(string == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    // TODO: find a way to implement this method
    return stringExtent(string);
//    if(string.length() == 0) {
//      SIZE size = new SIZE();
////		OS.GetTextExtentPoint32(handle, SPACE, SPACE.length(), size);
//      OS.GetTextExtentPoint32W(handle, new char[] {' '}
//                               , 1, size);
//      return new Point(0, size.cy);
//    }
//    RECT rect = new RECT();
//    TCHAR buffer = new TCHAR(getCodePage(), string, false);
//    int uFormat = OS.DT_LEFT | OS.DT_CALCRECT;
//    if((flags & SWT.DRAW_DELIMITER) == 0) {
//      uFormat |= OS.DT_SINGLELINE;
//    }
//    if((flags & SWT.DRAW_TAB) != 0) {
//      uFormat |= OS.DT_EXPANDTABS;
//    }
//    if((flags & SWT.DRAW_MNEMONIC) == 0) {
//      uFormat |= OS.DT_NOPREFIX;
//    }
//    OS.DrawText(handle, buffer, buffer.length(), rect, uFormat);
//    return new Point(rect.right, rect.bottom);
  }

  /**
   * Returns a string containing a concise, human-readable
   * description of the receiver.
   *
   * @return a string representation of the receiver
   */
  public String toString() {
    if(isDisposed()) {
      return "GC {*DISPOSED*}";
    }
    return "GC {" + swingHandle + "}";
  }

  /**
   * Invokes platform specific functionality to allocate a new graphics context.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>GC</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @param drawable the Drawable for the receiver.
   * @param data the data for the receiver.
   *
   * @return a new <code>GC</code>
   */
  public static GC win32_new(Drawable drawable, GCData data) {
    throw new IllegalStateException("Not supported");
  }

  /**
   * Invokes platform specific functionality to allocate a new graphics context.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>GC</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @param drawable the Drawable for the receiver.
   *
   * @return a new <code>GC</code>
   */
  public static GC swing_new(Drawable drawable) {
    GC gc = new GC();
    gc.init(drawable);
    return gc;
  }

}
