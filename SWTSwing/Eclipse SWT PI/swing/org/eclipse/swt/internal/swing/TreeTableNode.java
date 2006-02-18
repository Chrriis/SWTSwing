/*
 * @(#)TreeTableNode.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import javax.swing.tree.MutableTreeNode;

public interface TreeTableNode extends MutableTreeNode {

  public void setUserObjects(Object[] objects);

  public Object[] getUserObjects();

  public void setUserObject(int index, Object object);

  public Object getUserObject(int index);

}
