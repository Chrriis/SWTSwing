/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import org.eclipse.swt.SWT;

class CTableColumnImplementation extends TableColumn implements CTableColumn {

  protected DefaultMutableTreeTableNode mutableTreeTableNode;

  protected org.eclipse.swt.widgets.TableColumn handle;

  public CTableColumnImplementation(org.eclipse.swt.widgets.TableColumn tableColumn, int style) {
    handle = tableColumn;
    setPreferredWidth(0);
    setMinWidth(0);
    init(style);
  }

  protected void init(int style) {
    setMinWidth(0);
    if ((style & SWT.LEFT) == SWT.LEFT) setAlignment(SwingConstants.LEFT);
    if ((style & SWT.CENTER) == SWT.CENTER) setAlignment(SwingConstants.CENTER);
    if ((style & SWT.RIGHT) == SWT.RIGHT) setAlignment(SwingConstants.RIGHT);
  }

  protected Icon icon;

  public void setIcon(Icon icon) {
    this.icon = icon;
  }

  public Icon getIcon() {
    return icon;
  }

  protected int alignment;

  public void setAlignment(int alignment) {
    this.alignment = alignment;
  }

  public int getAlignment() {
    return alignment;
  }

  protected String toolTipText;

  public void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
  }
  
  public String getToolTipText() {
    return toolTipText;
  }

}

public interface CTableColumn {

  public static class Instanciator {
    private Instanciator() {}

    public static CTableColumn createInstance(org.eclipse.swt.widgets.TableColumn tableColumn, int style) {
      return new CTableColumnImplementation(tableColumn, style);
    }

  }

  public void setIcon(Icon icon);

  public Icon getIcon();

  public void setAlignment(int alignment);

  public int getAlignment();

  public void setToolTipText(String toolTipText);
  
  public String getToolTipText();

}
