/*
 * @(#)CGroup.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;

class CGroupImplementation extends JPanel implements CGroup {

  protected TitledBorder titledBorder;
  protected Container contentPane;
  protected JScrollPane scrollPane;

  protected Group handle;

  public CGroupImplementation(Group group, int style) {
    super(new BorderLayout(0, 0));
    this.handle = group;
    init(style);
  }
  
  protected void init(int style) {
    // TODO: support styles
    titledBorder = BorderFactory.createTitledBorder("");
    setBorder(titledBorder);
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new JScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      add(scrollPane, BorderLayout.CENTER);
      contentPane = scrollPane.getViewport();
    } else {
      contentPane = new JPanel(null);
      add(contentPane, BorderLayout.CENTER);
    }
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
    Utils.installFocusListener(contentPane, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return contentPane;
  }

  public String getText () {
    return titledBorder.getTitle();
  }

  public void setText(String string) {
    titledBorder.setTitle(string);
  }

  public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
  }

}

/**
 * The group equivalent on the Swing side.
 * @version 1.0 2005.03.13
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CGroup extends CScrollable {

  public static class Instanciator {
    private Instanciator() {}

    public static CGroup createInstance(Group group, int style) {
      return new CGroupImplementation(group, style);
    }

  }

  public String getText();

  public void setText(String string);

}
