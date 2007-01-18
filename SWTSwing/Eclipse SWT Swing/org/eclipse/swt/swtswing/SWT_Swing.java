/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.swtswing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.UIManager;

import org.eclipse.swt.internal.swing.CShell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWT_Swing {

  public static class EmbeddedShell extends Shell implements CShell.CEmbeddedShell {
    
    static {
      System.setProperty("swt.swing.laf", UIManager.getLookAndFeel().getClass().getName());
    }
    
    protected EmbeddedShell() {
      super(Display.getDefault());
      ((CShell)handle).getClientArea().setLayout(new BorderLayout());
    }
    
    public Component getComponent() {
      return handle;
    }
    
    protected void checkSubclass () {
      // Do nothing: subclassing is allowed.
    }

    protected boolean isLayoutManaged() {
      return true;
    }
    
  }
  
  /**
   * @return A parent for any SWT Control, from which a Component can be obtained and used in Swing interfaces.
   */
  public static EmbeddedShell newEmbeddedShell() {
    EmbeddedShell parent = new EmbeddedShell();
    manageEventDispatch(parent);
    return parent;
  }
  
  protected static List managedCompositeList = new ArrayList();
  
  /**
   * Add the indicated composite to the list of composites managed by the automatic process.
   * Composite must be disposed when not used anymore, to get removed from this handling.
   */
  protected static void manageEventDispatch(Composite composite) {
    synchronized(managedCompositeList) {
      if(managedCompositeList.contains(composite)) {
        return;
      }
      managedCompositeList.add(composite);
      if(managedCompositeList.size() == 1) {
        Display.swtExec(new Runnable() {
          public void run() {
            try {
              Display display = Display.getDefault();
              boolean hasElements = true;
              while(hasElements) {
                if(!display.readAndDispatch ()) {
                  display.sleep ();
                }
                synchronized(managedCompositeList) {
                  for(Iterator it=managedCompositeList.iterator(); it.hasNext(); ) {
                    if(((Composite)it.next()).isDisposed()) {
                      it.remove();
                    }
                  }
                  hasElements = !managedCompositeList.isEmpty();
                }
              }
            }
            catch(Throwable t) {
              t.printStackTrace();
            }
          }
        });
        new Thread() {
          public void run() {
          }
        }.start();
      }
    }
  }
  
}
