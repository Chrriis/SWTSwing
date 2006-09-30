/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.PaintEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

class CShellFrame extends JFrame implements CShell {

  protected Container contentPane;
  protected JScrollPane scrollPane;

  protected Shell handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  public CShellFrame(Shell shell, int style) {
    this.handle = shell;
    setLocationByPlatform(true);
    init(style);
  }
  
  protected void init(int style) {
    if((style & SWT.ON_TOP) != 0) {
      // TODO: Check if that should always be the case. Do we apply the non focusable state also to shells with a title bar?
      setFocusableWindowState(false);
    }
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    java.awt.Rectangle bounds = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    setSize(bounds.width * 3 / 4, bounds.height * 3 / 4);
    if((style & SWT.RESIZE) == 0) {
      setResizable(false);
    }
    boolean isDecorated = (style & SWT.TITLE) != 0;
    if(isDecorated) {
      if(JFrame.isDefaultLookAndFeelDecorated()) {
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
      } else {
        setUndecorated(!isDecorated);
      }
    } else {
      if(JFrame.isDefaultLookAndFeelDecorated()) {
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
      }
      setUndecorated(!isDecorated);
    }
    // BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, APPLICATION_MODAL, MODELESS, PRIMARY_MODAL, SYSTEM_MODAL
    contentPane = getContentPane();
    JPanel panel = new JPanel(null) {
      protected Graphics graphics;
      public Graphics getGraphics() {
        Graphics g;
        if(graphics != null) {
          g = graphics.create();
        } else {
          g = super.getGraphics();
        }
        return g;
      }
      protected void paintComponent (Graphics g) {
        graphics = g;
        putClientProperty(Utils.SWTSwingPaintingClientProperty, Boolean.TRUE);
        super.paintComponent(g);
        if(backgroundImageIcon != null) {
          Dimension size = getSize();
          g.drawImage(backgroundImageIcon.getImage(), 0, 0, size.width, size.height, null);
        }
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        if(paintHandlerList != null) {
          int size = paintHandlerList.size();
          Point origin = SwingUtilities.convertPoint(this, new Point(0, 0), CShellFrame.this);
          for(int i=0; i<size; i++) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.translate(-origin.x, -origin.y);
            ((PaintHandler)paintHandlerList.get(i)).paintComponent(g2);
            g2.dispose();
          }
        }
        putClientProperty(Utils.SWTSwingPaintingClientProperty, null);
        graphics = null;
      }
      public void paint(Graphics g) {
        super.paint(g);
        if(paintHandlerList != null) {
          int size = paintHandlerList.size();
          Point origin = SwingUtilities.convertPoint(this, new Point(0, 0), CShellFrame.this);
          for(int i=0; i<size; i++) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.translate(-origin.x, -origin.y);
            ((PaintHandler)paintHandlerList.get(i)).paint(g2);
            g2.dispose();
          }
        }
      }
    };
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new UnmanagedScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      scrollPane.setBorder(null);
      contentPane.add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      contentPane.add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.setFocusable(true);
//    setFocusable(false);
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
//    Utils.installFocusListener(contentPane, handle);
    addComponentListener(new ComponentAdapter() {
      public void componentHidden(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    contentPane.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
//        transferFocusDownCycle();
      }
      public void windowClosing(WindowEvent e) {
        handle.processEvent(e);
      };
      public void windowActivated(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowDeactivated(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowClosed(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowIconified(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowDeiconified(WindowEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public void setBackground(Color c) {
    super.setBackground(c);
    if(contentPane != null) {
      contentPane.setBackground(c);
    }
  }

  public void forceActive() {
    toFront();
    requestFocus();
  }

  public Container getClientArea() {
    return contentPane;
  }

  public String getToolTipText() {
    return ((JComponent)contentPane).getToolTipText();
  }

  public void setToolTipText(String string) {
    ((JComponent)contentPane).setToolTipText(string);
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

  protected ArrayList paintHandlerList;

  public void addPaintHandler(PaintHandler paintHandler) {
    if(paintHandlerList == null) {
      paintHandlerList = new ArrayList();
    }
    paintHandlerList.add(paintHandler);
  }

  public void removePaintHandler(PaintHandler paintHandler) {
    if(paintHandlerList == null) {
      return;
    }
    paintHandlerList.remove(paintHandler);
  }

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
  }

}

class CShellDialog extends JDialog implements CShell {

  protected Container contentPane;
  protected JScrollPane scrollPane;
  protected Shell handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

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

  public void setVisible(final boolean isVisible) {
    if(isVisible && isModal()) {
      Display display = handle.getDisplay();
      if(display != null && display.getThread() == Thread.currentThread() || SwingUtilities.isEventDispatchThread()) {
        final Object LOCK = new Object();
        Thread t = new Thread() {
          public void run() {
            synchronized(LOCK) {
              LOCK.notify();
            }
            setVisible(isVisible);
          }
        };
        synchronized(LOCK) {
          t.start();
          try {
            LOCK.wait();
          } catch(Exception e) {}
          int count = 0;
          while(!isVisible() && count++ < 10) {
            try {
              Thread.sleep(10);
            } catch(Exception e) {}
          }
        }
        return;
      }
    }
    super.setVisible(isVisible);
  }

  protected void init(int style) {
    if((style & SWT.ON_TOP) != 0) {
      // TODO: Check if that should always be the case. Do we apply the non focusable state also to shells with a title bar?
      setFocusableWindowState(false);
    }
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    if((style & SWT.APPLICATION_MODAL) != 0) {
      setModal(true);
    }
    java.awt.Rectangle bounds = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    setSize(bounds.width * 3 / 4, bounds.height * 3 / 4);
    if((style & SWT.RESIZE) == 0) {
      setResizable(false);
    }
    boolean isDecorated = (style & SWT.TITLE) != 0;
    if(isDecorated) {
      if(JDialog.isDefaultLookAndFeelDecorated()) {
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
      } else {
        setUndecorated(!isDecorated);
      }
    } else {
      if(JDialog.isDefaultLookAndFeelDecorated()) {
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
      }
      setUndecorated(!isDecorated);
    }
    contentPane = getContentPane();
    JPanel panel = new JPanel(null) {
      protected Graphics graphics;
      public Graphics getGraphics() {
        Graphics g;
        if(graphics != null) {
          g = graphics.create();
        } else {
          g = super.getGraphics();
        }
        return g;
      }
      protected void paintComponent (Graphics g) {
        graphics = g;
        putClientProperty(Utils.SWTSwingPaintingClientProperty, Boolean.TRUE);
        super.paintComponent(g);
        if(backgroundImageIcon != null) {
          Dimension size = getSize();
          g.drawImage(backgroundImageIcon.getImage(), 0, 0, size.width, size.height, null);
        }
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        if(paintHandlerList != null) {
          int size = paintHandlerList.size();
          Point origin = SwingUtilities.convertPoint(this, new Point(0, 0), CShellDialog.this);
          for(int i=0; i<size; i++) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.translate(-origin.x, -origin.y);
            ((PaintHandler)paintHandlerList.get(i)).paintComponent(g2);
            g2.dispose();
          }
        }
        putClientProperty(Utils.SWTSwingPaintingClientProperty, null);
        graphics = null;
      }
      public void paint(Graphics g) {
        super.paint(g);
        if(paintHandlerList != null) {
          int size = paintHandlerList.size();
          Point origin = SwingUtilities.convertPoint(this, new Point(0, 0), CShellDialog.this);
          for(int i=0; i<size; i++) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.translate(-origin.x, -origin.y);
            ((PaintHandler)paintHandlerList.get(i)).paint(g2);
            g2.dispose();
          }
        }
      }
    };
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new UnmanagedScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      scrollPane.setBorder(null);
      contentPane.add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      contentPane.add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.setFocusable(true);
//    setFocusable(false);
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
    getRootPane().registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
//    Utils.installFocusListener(contentPane, handle);
    addComponentListener(new ComponentAdapter() {
      public void componentHidden(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        handle.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    contentPane.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        handle.processEvent(e);
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
//        transferFocusDownCycle();
      }
      public void windowClosing(WindowEvent e) {
        handle.processEvent(e);
      };
      public void windowActivated(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowDeactivated(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowClosed(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowIconified(WindowEvent e) {
        handle.processEvent(e);
      }
      public void windowDeiconified(WindowEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public void setBackground(Color c) {
    super.setBackground(c);
    if(contentPane != null) {
      contentPane.setBackground(c);
    }
  }

  public void forceActive() {
    toFront();
    requestFocus();
  }

  public Container getClientArea() {
    return contentPane;
  }

  public String getToolTipText() {
    return ((JComponent)contentPane).getToolTipText();
  }

  public void setToolTipText(String string) {
    ((JComponent)contentPane).setToolTipText(string);
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

  protected ArrayList paintHandlerList;

  public void addPaintHandler(PaintHandler paintHandler) {
    if(paintHandlerList == null) {
      paintHandlerList = new ArrayList();
    }
    paintHandlerList.add(paintHandler);
  }

  public void removePaintHandler(PaintHandler paintHandler) {
    if(paintHandlerList == null) {
      return;
    }
    paintHandlerList.remove(paintHandler);
  }

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
  }

}

/**
 * The shell equivalent on the Swing side.
 * @version 1.0 2005.03.13
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public interface CShell extends CScrollable {

  public static interface PaintHandler {
    public void paintComponent(Graphics2D g);
    public void paint(Graphics2D g);
  }

  public static int MAXIMIZED_BOTH = JFrame.MAXIMIZED_BOTH;
  public static int ICONIFIED = JFrame.ICONIFIED;
  public static int NORMAL = JFrame.NORMAL;

  public static class Instanciator {
    private Instanciator() {}

    static {
      Toolkit.getDefaultToolkit().setDynamicLayout(true);
    }

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

  public void addPaintHandler(PaintHandler paintHandler);

  public void removePaintHandler(PaintHandler paintHandler);

}