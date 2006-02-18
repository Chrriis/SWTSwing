/*
 * @(#)DefaultTreeTableCellRenderer.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DefaultTreeTableCellRenderer implements TreeTableCellRenderer {

  protected DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

  public Component getTreeTableCellRendererComponent(JTreeTable treeTable, Object value, boolean selected, boolean expanded, boolean leaf, int row, int column, boolean hasFocus) {
    Component component = renderer.getTreeCellRendererComponent(treeTable.getTree(), value, selected, expanded, leaf, row, hasFocus);
    if(column != 0 && component instanceof JLabel) {
      ((JLabel)component).setIcon(null);
    }
    return component;
  }

}
