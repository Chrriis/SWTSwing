/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.Display;

/**
 * General UI Thread realted utility methods.
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public class UIThreadUtils {

  protected UIThreadUtils() {}
  
  public static class SwingEventQueue extends EventQueue {
    protected AWTEvent event;
    public boolean sleep() {
      event = null;
      try {
        event = getNextEvent();
      } catch(InterruptedException e) {}
      return event != null;
    }
    public boolean dispatchEvent() {
      if(event != null) {
        AWTEvent theEvent = event;
        event = null;
        try {
          dispatchEvent(theEvent);
        } catch(Throwable t) {
          t.printStackTrace();
        }
      }
      return false;
    }
    public void pop() {
      super.pop();
    }
  }

  public static SwingEventQueue swingEventQueue;

  protected static void pushQueue() {
    if(isRealDispatch()) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      if(eventQueue != swingEventQueue) {
        UIThreadUtils.exclusiveSectionCount++;
        eventQueue.push(swingEventQueue);
      }
    }
  }

  public static void popQueue() {
    if(isRealDispatch()) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      if(eventQueue == swingEventQueue) {
        swingEventQueue.pop();
        swingEventQueue = null;
        UIThreadUtils.exclusiveSectionCount--;
      }
    }
  }

  public static boolean isRealDispatch() {
    return swingEventQueue != null;
  }

  public static void main(final String[] args) {
    swtExec(new Runnable() {
      public void run() {
        try {
          Method method = Class.forName(args[0]).getDeclaredMethod("main", new Class[] {String[].class});
          String[] newArgs = new String[args.length - 1];
          System.arraycopy(args, 1, newArgs, 0, newArgs.length);
          method.invoke(null, new Object[] {newArgs});
        } catch(Throwable t) {
          t.printStackTrace();
        }
      }
    });
  }

  public static void swtExec(Runnable runnable) {
    if(swingEventQueue == null) {
      swingEventQueue = new SwingEventQueue();
    }
    pushQueue();
    SwingUtilities.invokeLater(runnable);
  }

  public static int exclusiveSectionCount = 0;
  public static final Object UI_LOCK = new Object();

  public static void swtSync(Display display, Runnable runnable) {
    try {
      startExclusiveSection(display);
      runnable.run();
    } finally {
      stopExclusiveSection();
    }
  }

  public static void startExclusiveSection(Display display) {
    if(isRealDispatch() || !SwingUtilities.isEventDispatchThread()) {
      exclusiveSectionCount++;
      return;
    }
    synchronized(UI_LOCK) {
      exclusiveSectionCount++;
      if(exclusiveSectionCount == 1) {
        try {
          display.wake();
          UI_LOCK.wait();
        } catch(Exception e) {
        }
      }
    }
  }

  public static void stopExclusiveSection() {
    if(isRealDispatch() || !SwingUtilities.isEventDispatchThread()) {
      exclusiveSectionCount--;
      return;
    }
    synchronized(UI_LOCK) {
      exclusiveSectionCount--;
      if(exclusiveSectionCount == 0) {
        UI_LOCK.notify();
      }
    }
  }


  
}
