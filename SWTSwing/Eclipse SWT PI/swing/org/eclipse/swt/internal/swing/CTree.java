/*
 * @(#)CTree.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Container;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.eclipse.swt.widgets.Tree;

class CTreeImplementation extends JScrollPane implements CTree {

  protected Tree handle;
  protected JTree tree;

  public CTreeImplementation(Tree tree, int style) {
    handle = tree;
    this.tree = new JTree();
    setFocusable(false);
    getViewport().setView(this.tree);
    init(style);
  }

  protected void init(int style) {
    
  }

  public Container getClientArea() {
    return tree;
  }

}

public interface CTree extends CComposite {

  public static class Instanciator {
    private Instanciator() {}

    public static CTree createInstance(Tree tree, int style) {
      return new CTreeImplementation(tree, style);
    }

  }

}
