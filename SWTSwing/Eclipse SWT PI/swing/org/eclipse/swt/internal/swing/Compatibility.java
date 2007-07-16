/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

public class Compatibility {

  protected static final String JAVA_VERSION = System.getProperty("java.version");
  public static final boolean IS_JAVA_6_OR_GREATER = JAVA_VERSION.compareTo("1.6") >= 0;
  public static final boolean IS_JAVA_5_OR_GREATER = IS_JAVA_6_OR_GREATER || JAVA_VERSION.compareTo("1.5") >= 0;
  
  public static void run(Runnable runnable) {
    runnable.run();
  }
  
}
