/*
 * @(#)PopEventQueue.java
 * 
 * Swing port: Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com/swtswing
 */
package org.eclipse.swt.internal.swing;

import java.awt.EventQueue;

/**
 * An event queue that makes it removal visible.
 * @version 1.0 2003.06.04
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class PopEventQueue extends EventQueue {
  public void pop() {
    super.pop();
  }

}

