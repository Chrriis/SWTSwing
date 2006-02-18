/*
 * @(#)TreeTableCellRenderer.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;

public interface TreeTableCellRenderer {

  public Component getTreeTableCellRendererComponent(JTreeTable treeTable, Object value, boolean selected, boolean expanded, boolean leaf, int row, int column, boolean hasFocus);

}
