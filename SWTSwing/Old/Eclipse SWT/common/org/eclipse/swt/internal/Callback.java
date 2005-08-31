    /*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
     *******************************************************************************/
package org.eclipse.swt.internal;

/**
 * Instances of this class represent entry points into Java
 * which can be invoked from operating system level callback
 * routines.
 * <p>
 * IMPORTANT: A callback is only valid when invoked on the
 * thread which created it. The results are undefined (and
 * typically bad) when a callback is passed out to the
 * operating system (or other code) in such a way that the
 * callback is called from a different thread.
 */

public class Callback {

  Object object;
  String method, signature;
  int argCount, address;
  boolean isStatic, isArrayBased;

  /* Load the SWT library */
  static {
    Library.loadLibrary("swt"); //$NON-NLS-1$
  }

  /**
   * Constructs a new instance of this class given an object
   * to send the message to, a string naming the method to
   * invoke and an argument count. Note that, if the object
   * is an instance of <code>Class</code> it is assumed that
   * the method is a static method on that class.
   *
   * @param object the object to send the message to
   * @param method the name of the method to invoke
   * @param argCount the number of arguments that the method takes
   */
  public Callback(Object object, String method, int argCount) {
    this(object, method, argCount, false);
  }

  /**
   * Constructs a new instance of this class given an object
   * to send the message to, a string naming the method to
   * invoke, an argument count and a flag indicating whether
   * or not the arguments will be passed in an array. Note
   * that, if the object is an instance of <code>Class</code>
   * it is assumed that the method is a static method on that
   * class.
   *
   * @param object the object to send the message to
   * @param method the name of the method to invoke
   * @param argCount the number of arguments that the method takes
   * @param isArrayBased <code>true</code> if the arguments should be passed in an array and false otherwise
   */
  public Callback(Object object, String method, int argCount,
                  boolean isArrayBased) {

    /* Set the callback fields */
    this.object = object;
    this.method = method;
    this.argCount = argCount;
    isStatic = object instanceof Class;
    this.isArrayBased = isArrayBased;

    /* Inline the common cases */
    if(isArrayBased) {
      signature = "([I)I"; //$NON-NLS-1$
    } else {
      switch(argCount) {
        case 0:
          signature = "()I";
          break; //$NON-NLS-1$
        case 1:
          signature = "(I)I";
          break; //$NON-NLS-1$
        case 2:
          signature = "(II)I";
          break; //$NON-NLS-1$
        case 3:
          signature = "(III)I";
          break; //$NON-NLS-1$
        case 4:
          signature = "(IIII)I";
          break; //$NON-NLS-1$
        default:
          signature = "("; //$NON-NLS-1$
          for(int i = 0; i < argCount; i++) {
            signature += "I"; //$NON-NLS-1$
          }
          signature += ")I"; //$NON-NLS-1$
      }
    }

    /* Bind the address */
    address = bind(this);
  }

  /**
   * Allocates the native level resources associated with the
   * callback. This method is only invoked from within the
   * constructor for the argument.
   *
   * @param callback the callback to bind
   */
  static native synchronized int bind(Callback callback);

  /**
   * Releases the native level resources associated with the callback,
   * and removes all references between the callback and
   * other objects. This helps to prevent (bad) application code
   * from accidentally holding onto extraneous garbage.
   */
  public void dispose() {
    if(object == null) {
      return;
    }
    unbind(this);
    object = method = signature = null;
    address = 0;
  }

  /**
   * Returns the address of a block of machine code which will
   * invoke the callback represented by the receiver.
   *
   * @return the callback address
   */
  public int getAddress() {
    return address;
  }

  /**
   * Returns the SWT platform name.
   *
   * @return the platform name of the currently running SWT
   */
  public static String getPlatform() {
    return System.getProperty("os.name");
  }

  /**
   * Returns the number of times the system has been recursively entered
   * through a callback.
   * <p>
   * Note: This should not be called by application code.
   * </p>
   *
   * @return the entry count
   *
   * @since 2.1
   */
  public static native int getEntryCount();

  /**
   * Indicates whether or not callbacks which are triggered at the
   * native level should cause the messages described by the matching
   * <code>Callback</code> objects to be invoked. This method is used
   * to safely shut down SWT when it is run within environments
   * which can generate spurious events.
   * <p>
   * Note: This should not be called by application code.
   * </p>
   *
   * @param ignore true if callbacks should not be invoked
   */
  public static final native synchronized void setEnabled(boolean enable);

  /**
   * Returns whether or not callbacks which are triggered at the
   * native level should cause the messages described by the matching
   * <code>Callback</code> objects to be invoked. This method is used
   * to safely shut down SWT when it is run within environments
   * which can generate spurious events.
   * <p>
   * Note: This should not be called by application code.
   * </p>
   *
   * @return true if callbacks should not be invoked
   */
  public static final native synchronized boolean getEnabled();

  /**
   * This might be called directly from native code in environments
   * which can generate spurious events. Check before removing it.
   *
   * @deprecated
   *
   * @param ignore true if callbacks should not be invoked
   */
  static final void ignoreCallbacks(boolean ignore) {
    setEnabled(!ignore);
  }

  /**
   * Immediately wipes out all native level state associated
   * with <em>all</em> callbacks.
   * <p>
   * <b>WARNING:</b> This operation is <em>extremely</em> dangerous,
   * and should never be performed by application code.
   * </p>
   */
  public static final native synchronized void reset();

  /**
   * Releases the native level resources associated with the callback.
   *
   * @see #dispose
   */
  static final native synchronized void unbind(Callback callback);

}
