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

  /**
   * When a class does not exist on a certain version of Java, it would generate a no
   * class def found error simply when the surrounding class gets loaded. The fix is to
   * surround the access by an if statement and perform the access in a Runnable. Using
   * this method is not mandatory but allows a better tracking of such compatibility
   * checks.
   * e.g.:<br>
   * <code>
   * if(Compatibility.IS_JAVA_5_OR_GREATER) {<br>
   *   Compatibility.run(new Runnable() {<br>
   *     public void run() {<br>
   *       // Access the new class<br>
   *     }<br>
   *   }<br>
   * }
   * </code>
   */
  public static void run(Runnable runnable) {
    runnable.run();
  }
  
}
