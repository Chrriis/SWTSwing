/*
 * @(#)WrappedRuntimeException.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

public class WrappedRuntimeException extends RuntimeException {

  private Throwable target;

  public WrappedRuntimeException(Throwable target) {
    super();
    this.target = target;
  }

  public Throwable getTargetException() {
    return this.target;
  }

  public String getMessage() {
    return target.getMessage();
  }
}
