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

import java.awt.Graphics;

public interface Drawable2 extends Drawable {

  /**
   * Invokes platform specific functionality to dispose a GC handle.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Drawable</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @param handle the platform specific GC handle
   * @param data the platform specific GC data
   */
  public void internal_dispose_GC(int handle, GCData data);


  /**  
   * Invokes platform specific functionality to allocate a new GC handle.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Drawable</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @param data the platform specific GC data 
   * @return the platform specific GC handle
   */
 
  public int internal_new_GC (GCData data);


  /**
   * Gets the device in use by this drawable.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Drawable</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @return the Device of this drawable object.
   */

  public Device internal_get_Device();


  /**
   * Invokes platform specific functionality to allocate a new GC handle.
   * <p>
   * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
   * API for <code>Drawable</code>. It is marked public only so that it
   * can be shared within the packages provided by SWT. It is not
   * available on all platforms, and should never be called from
   * application code.
   * </p>
   *
   * @return the platform specific GC handle
   */

  public Graphics internal_new_GC();

}
