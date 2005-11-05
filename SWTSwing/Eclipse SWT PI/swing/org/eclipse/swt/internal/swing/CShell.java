/*
 * @(#)CShell.java
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
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

class CShellFrame extends JFrame implements CShell {

  protected Container contentPane;
  protected JScrollPane scrollPane;

  protected Shell handle;

  public CShellFrame(Shell shell, int style) {
    this.handle = shell;
    setLocationByPlatform(true);
    init(style);
  }
  
  protected void init(int style) {
    java.awt.Rectangle bounds = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    setSize(bounds.width * 3 / 4, bounds.height * 3 / 4);
    if((style & SWT.RESIZE) == 0) {
      setResizable(false);
    }
    setUndecorated((style & SWT.TITLE) == 0);
    // BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, APPLICATION_MODAL, MODELESS, PRIMARY_MODAL, SYSTEM_MODAL
    contentPane = getContentPane();
    JPanel panel = new JPanel(null);
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new JScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      contentPane.add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      contentPane.add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
        transferFocusDownCycle();
      }
      public void windowClosing(WindowEvent e) {
        handle.processEvent(e);
      };
    });
  }

  public void forceActive() {
    toFront();
    requestFocus();
  }

  public Container getClientArea() {
    return contentPane;
  }

  public String getToolTipText() {
    // TODO: implement properly
    return "Not yet implemented";
  }

  // TODO: implement so that it traverses the complete hierarchy
  public void setToolTipText(String string) {
    // TODO: implement properly
  }

  public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
  }

  public void setDefaultButton(CButton button) {
    if(button instanceof JButton) {
      getRootPane().setDefaultButton((JButton)button);
    }
  }

}

class CShellDialog extends JDialog implements CShell {

  protected Container contentPane;
  protected JScrollPane scrollPane;
  protected Shell handle;

  public CShellDialog(Shell shell, CShellDialog parent, int style) {
    super(parent);
    setLocationByPlatform(true);
    this.handle = shell;
    init(style);
  }

  public CShellDialog(Shell shell, CShellFrame parent, int style) {
    super(parent);
    setLocationByPlatform(true);
    this.handle = shell;
    init(style);
  }

  protected void init(int style) {
//    if((style & SWT.APPLICATION_MODAL) != 0) {
//      setModal(true);
//      // TODO: implement
//    }
    java.awt.Rectangle bounds = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    setSize(bounds.width * 3 / 4, bounds.height * 3 / 4);
    if((style & SWT.RESIZE) == 0) {
      setResizable(false);
    }
    setUndecorated((style & SWT.TITLE) == 0);
    contentPane = getContentPane();
    JPanel panel = new JPanel(null);
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new JScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      contentPane.add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      contentPane.add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        handle.processEvent(e);
      }
    });
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
        transferFocusDownCycle();
      }
      public void windowClosing(WindowEvent e) {
        handle.processEvent(e);
      };
    });
  }

  public void forceActive() {
    toFront();
    requestFocus();
  }

  public Container getClientArea() {
    return contentPane;
  }

  public String getToolTipText() {
    // TODO: implement properly
    return "Not yet implemented";
  }

  // TODO: implement so that it traverses the complete hierarchy
  public void setToolTipText(String string) {
    // TODO: implement properly
  }

  public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
  }

  public int getExtendedState() {
    return 0;
  }

  public void setExtendedState(int state) {
    // TODO: implement fake maximize/minimize?
  }

  public void setIconImage(Image image) {
  }

  public void setDefaultButton(CButton button) {
    if(button instanceof JButton) {
      getRootPane().setDefaultButton((JButton)button);
    }
  }

}

/**
 * The shell equivalent on the Swing side.
 * @version 1.0 2005.03.13
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CShell extends CScrollable {

  public static int MAXIMIZED_BOTH = JFrame.MAXIMIZED_BOTH;
  public static int ICONIFIED = JFrame.ICONIFIED;
  public static int NORMAL = JFrame.NORMAL;

  public static class Instanciator {
    private Instanciator() {}

    public static CShell createInstance(Shell shell, CShell parent, int style) {
      if(parent instanceof CShellFrame) {
        return new CShellDialog(shell, (CShellFrame)parent, style);
      }
      if(parent instanceof CShellDialog) {
        return new CShellDialog(shell, (CShellDialog)parent, style);
      }
      return new CShellFrame(shell, style);
    }

  }

  public void forceActive();

  public String getTitle();

  public void setTitle(String title);

  public void toFront();

  public int getExtendedState();

  public void setExtendedState(int state);

  public void setIconImage(Image image);

  public void setJMenuBar(JMenuBar menuBar);

  public void setDefaultButton(CButton button);

}
