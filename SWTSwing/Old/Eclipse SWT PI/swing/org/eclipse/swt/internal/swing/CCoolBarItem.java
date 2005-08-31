/*
 * @(#)CCoolItem.java
 * 
 * Swing port: Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com/swtswing
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicToolBarUI;

/**
 * Implementation of a cool item.
 * @version 1.0 2003.08.20
 * @author Christopher Deckers (chrriis@brainlex.com)
 */
public class CCoolBarItem extends JPanel {

  /**
   * Construct a CCoolItem, which will not be wrapped.
   */
  public CCoolBarItem() {
    this(false);
  }

  /**
   * Construct a CCoolItem, with the specified wrap style.
   * @param isWrapped Indicate if the item is wrapped.
   */  
  public CCoolBarItem(boolean isWrapped) {
    this(new JToolBar(), isWrapped);
  }
  
  /**
   * Construct a CCoolItem, with the specified tool bar and not wrapped.
   * @param toolBar The tool bar to use.
   */  
  public CCoolBarItem(JToolBar toolBar) {
    this(toolBar, false);
  }
  
  /** The glue. */
  private JPanel glue;
  
  /** The tool bar. */
  private JToolBar toolBar;

  /** The panel that shows the separation of cool bar items. */
  private JPanel itemSeparatorPanel;

/*------------------------------------------------------------------------------
 * Mouse handling - Begining
 *----------------------------------------------------------------------------*/

  /**
   * Indicate if the specified point is located in the bumps of the toolbar.
   * @param point The point to consider, in toolbar coordinates.
   * @return True if the point is in the bumps.
   */
  private boolean isInBumps(Point point) {
    Rectangle bumpRect = new Rectangle();
    int x = toolBar.getComponentOrientation().isLeftToRight() ? 0 : toolBar.getSize().width-14;
    bumpRect.setBounds(x, 0, 14, toolBar.getSize().height);
    return bumpRect.contains(point);
  }

  /**
   * Indicate if the specified point is located in the toolbar but not the bumps.
   * @param point The point to consider, in cool item coordinates.
   * @return True if the point is in the bumps.
   */
  private boolean isInToolBar(Point point) {
    Rectangle bumpRect = new Rectangle();
    point = SwingUtilities.convertPoint(this, point, toolBar);
    int x = toolBar.getComponentOrientation().isLeftToRight() ? 14: 0;
    bumpRect.setBounds(x, 0, toolBar.getSize().width-14, toolBar.getSize().height);
    return bumpRect.contains(point);
  }

  private Point mouseLocation;

  /** The listener for mouse events. */
  private MouseListener mouseListener = new MouseAdapter() {
    public void mousePressed(MouseEvent e) {
      if(isInBumps(e.getPoint())) {
        mouseLocation = e.getPoint();
      }
    }
    public void mouseReleased(MouseEvent e) {
      mouseLocation = null;
    }
    public void mouseClicked(MouseEvent e) {
      if(!isExtracted && e.getClickCount() == 2) {
        setIndent(0);
      }
    }
  };

  private class ItemInformation {
    public final int index;
    public final int startRowItem;
    public final int endRowItem;
    public final int rowHeight;
    public final int totalIndent;
    public ItemInformation(int index, int startRowItem, int endRowItem, int rowHeight, int totalIndent) {
      this.index = index;
      this.startRowItem = startRowItem;
      this.endRowItem = endRowItem;
      this.rowHeight = rowHeight;
      this.totalIndent = totalIndent;
    }
  }

  private CCoolBar getCoolBar() {
    return (CCoolBar)CCoolBarItem.this.getParent();
  }

  private ItemInformation getItemInformation() {
    CCoolBar coolBar = getCoolBar();
    int y1 = 0;
    int y2 = -1;
    int index = -1;
    int start = 0;
    int end = -1;
    int indent = 0;
    boolean itemFound = false;
    for(int i=0; i<coolBar.getComponentCount(); i++) {
      CCoolBarItem item = (CCoolBarItem)coolBar.getComponent(i);
      if(!itemFound) {
        if(item.isWrapped()) {
          indent = 0;
        }
      }
      if(i == 0) {
        y1 = item.getLocation().y;
        y2 = y1 + item.getHeight();
      }
      if(index == -1 && item.isWrapped()) {
        start = i;
        y1 = item.getLocation().y;
      }
      if(item == CCoolBarItem.this) {
        itemFound = true;
        if(item.isVisible()) {
          indent += item.getIndent();
        }
        index = i;
        end = index;
        y2 = item.getLocation().y + item.getHeight();
      } else {
        if(!itemFound && item.isVisible()) {
          indent += item.getWidth();
        }
        if(end != -1) {
          if(item.isWrapped()) {
            break;
          } else {
            end = i;
            y2 = item.getLocation().y + item.getHeight();
          }
        }
      }
    }
    return new ItemInformation(index, start, end, y2 - y1, indent);
  }

  /** Indicate if the bar is extractable. */
  private boolean isExtractable = false;

  /** Indicate if the tool bar is extracted. */
  private boolean isExtracted = false;

  /** The listener for mouse motion events. */
  private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
    public void mouseDragged(MouseEvent e) {
      if(mouseLocation != null) {
        CCoolBar coolBar = getCoolBar();
        Point location = e.getPoint();

        if(isExtracted) {
          Component root = SwingUtilities.getRoot(toolBar);
          Point rootLocation = root.getLocation();
          root.setLocation(rootLocation.x - mouseLocation.x + location.x, rootLocation.y - mouseLocation.y + location.y);
          return;
        }
        // If out of the Cool bar, then extract if allowed to extract
        if(isExtractable && !coolBar.contains(SwingUtilities.convertPoint(toolBar, location, coolBar))) {
          Point barLocation = new Point(toolBar.getLocation());
          SwingUtilities.convertPointToScreen(barLocation, toolBar.getParent());
          ((BasicToolBarUI)toolBar.getUI()).setFloating(true, null);
          Point newBarLocation = toolBar.getLocation();
          SwingUtilities.convertPointToScreen(newBarLocation, toolBar.getParent());
          SwingUtilities.getRoot(toolBar).setLocation(barLocation.x - newBarLocation.x + location.x - mouseLocation.x, barLocation.y - newBarLocation.y + location.y - mouseLocation.y);
//          location = newLocation;
          return;
        }

        ItemInformation itemInformation = getItemInformation();
        int diffY = location.y - mouseLocation.y;
        int threshold = itemInformation.rowHeight / 2;
        // If changing line
        if(diffY > threshold || diffY < - threshold) {
          boolean isUp = diffY < 0;
          if(isUp) {
            // Already in intermediate line
            if(itemInformation.startRowItem == itemInformation.index && itemInformation.endRowItem == itemInformation.index) {
              Point coolBarPoint = SwingUtilities.convertPoint(e.getComponent(), location, coolBar);
              Component comp = coolBar.getComponentAt(coolBarPoint);
              if(comp instanceof CCoolBarItem) {
                CCoolBarItem item = (CCoolBarItem)comp;
                for(int i=itemInformation.startRowItem - 1; i>=0; i--) {
                  if(coolBar.getComponent(i) == item) {
                    Point itemPoint = SwingUtilities.convertPoint(coolBar, coolBarPoint, item);
                    if(item.isInToolBar(itemPoint)) {
                      setIndent(0);
                      coolBar.add(CCoolBarItem.this, i + 1);
                      setWrapped(false);
                    } else {
                      ItemInformation itemInfo = item.getItemInformation();
                      if(itemInfo.index == itemInfo.startRowItem) {
                        setIndent(0);
                      } else {
                        setIndent(getIndent() - itemInfo.totalIndent + item.getIndent());
                      }
                      CCoolBarItem.this.setWrapped(item.isWrapped());
                      item.setWrapped(false);
                      coolBar.add(CCoolBarItem.this, i);
                    }
                    break;
                  }
                }
              } else {
                setWrapped(false);
                coolBar.add(CCoolBarItem.this, itemInformation.startRowItem);
                setIndent(2*getIndent() - getItemInformation().totalIndent);
              }
            } else {
              setWrapped(true);
              coolBar.add(CCoolBarItem.this, itemInformation.startRowItem);
              ((CCoolBarItem)coolBar.getComponent(itemInformation.startRowItem + 1)).setWrapped(true);
              setIndent(itemInformation.totalIndent);
            }
          } else {
            // Already in intermediate line
            if(itemInformation.startRowItem == itemInformation.index && itemInformation.endRowItem == itemInformation.index) {
              Point coolBarPoint = SwingUtilities.convertPoint(e.getComponent(), location, coolBar);
              Component comp = coolBar.getComponentAt(coolBarPoint);
              if(comp instanceof CCoolBarItem) {
                CCoolBarItem item = (CCoolBarItem)comp;
                for(int i=itemInformation.endRowItem + 1; i<coolBar.getComponentCount(); i++) {
                  if(coolBar.getComponent(i) == item) {
                    Point itemPoint = SwingUtilities.convertPoint(coolBar, coolBarPoint, item);
                    if(item.isInToolBar(itemPoint)) {
                      // on bar
                      setIndent(0);
                      coolBar.add(CCoolBarItem.this, item.getItemInformation().index);
                      setWrapped(false);
                    } else {
                      // on rest of bar
                      ItemInformation itemInfo = item.getItemInformation();
                      if(itemInfo.index == itemInformation.index + 1) {
                        setWrapped(true);
                        item.setWrapped(false);
                      } else {
                        setWrapped(false);
                      }
                      coolBar.add(CCoolBarItem.this, itemInfo.index - 1);
                      setIndent(2*getIndent() - getItemInformation().totalIndent);
                    }
                    break;
                  }
                }
              } else {
                // not on item
                if(itemInformation.endRowItem + 1 < coolBar.getComponentCount()) {
                  CCoolBarItem item = (CCoolBarItem)coolBar.getComponent(itemInformation.endRowItem + 1);
                  ItemInformation itemInfo = item.getItemInformation();
                  coolBar.add(CCoolBarItem.this, itemInfo.endRowItem);
                  setWrapped(false);
                  setIndent(getIndent() - (getItemInformation().totalIndent - getIndent()));
                }
              }
            } else {
              setWrapped(true);
              coolBar.add(CCoolBarItem.this, itemInformation.endRowItem);
              setIndent(itemInformation.totalIndent);
              if(itemInformation.startRowItem == itemInformation.index) {
                ((CCoolBarItem)coolBar.getComponent(itemInformation.index)).setWrapped(true);
              }
            }
          }
          revalidate();
        }
        // Move
        int diffX = location.x - mouseLocation.x;
        if(!getComponentOrientation().isLeftToRight()) {
          diffX = -diffX;
        }
        if(diffX < 0) {
          int oldDiffX = diffX;
          for(int i=itemInformation.index; i>=itemInformation.startRowItem; i--) {
            CCoolBarItem item = (CCoolBarItem)coolBar.getComponent(i);
            int indent = item.getIndent();
            int newIndent = indent + diffX > 0? indent + diffX: 0;
            item.setIndent(newIndent);
            diffX += indent - newIndent;
          }
          if(itemInformation.index != itemInformation.endRowItem) {
            CCoolBarItem item = null;
            for(int i=itemInformation.index+1; i<=itemInformation.endRowItem && item == null; i++) {
              CCoolBarItem tempItem = (CCoolBarItem)coolBar.getComponent(i);
              if(tempItem.isVisible()) {
                item = tempItem;
              }
            }
            if(item != null) {
              item.setIndent(item.getIndent() - oldDiffX + diffX);
            }
          }
        } else {
          setIndent(getIndent() + diffX);
          for(int i=itemInformation.index + 1; i<=itemInformation.endRowItem; i++) {
            CCoolBarItem item = (CCoolBarItem)coolBar.getComponent(i);
            if(item.isVisible()) {
              int indent = item.getIndent();
              int newIndent = indent - diffX > 0? indent - diffX: 0;
              item.setIndent(newIndent);
              diffX -= indent - newIndent;
            }
          }
        }
        //Exchange components
        Point coolBarPoint = SwingUtilities.convertPoint(e.getComponent(), location, coolBar);
//        System.err.println(coolBarPoint);
        Component comp = coolBar.getComponentAt(coolBarPoint);
//        System.err.println(comp);
        if(comp instanceof CCoolBarItem) {
          CCoolBarItem item = (CCoolBarItem)comp;
          if(item != CCoolBarItem.this) {
            Point coolItemPoint = SwingUtilities.convertPoint(coolBar, coolBarPoint, item.getToolBar());
            if(item.isInBumps(coolItemPoint)) {
              int index = itemInformation.index;
              int itemIndex = item.getItemInformation().index;
              if(index < itemInformation.endRowItem) {
                ((CCoolBarItem)coolBar.getComponent(index + 1)).setWrapped(isWrapped());
              }
              setWrapped(item.isWrapped());
              item.setWrapped(false);
              coolBar.add(CCoolBarItem.this, itemIndex<index? itemIndex: itemIndex - 1);
              // TODO: set wrapping
              
            }
          }
        }
      }
    }
  };

/*------------------------------------------------------------------------------
 * Mouse handling - End
 *----------------------------------------------------------------------------*/

  /**
   * Construct a CCoolItem, with the specified tool bar and wrap style.
   * @param toolBar The tool bar to use.
   * @param isWrapped Indicate if the item is wrapped.
   */  
  public CCoolBarItem(JToolBar toolBar, boolean isWrapped) {
//    setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
    this.toolBar = toolBar;
    setLayout(new BorderLayout(0, 0));
    this.isWrapped = isWrapped;

    MouseListener[] listeners = toolBar.getMouseListeners();
    for(int i=0; i<listeners.length; i++) {
      toolBar.removeMouseListener(listeners[i]);
    }
    MouseMotionListener[] motionListeners = toolBar.getMouseMotionListeners();
    for(int i=0; i<motionListeners.length; i++) {
      toolBar.removeMouseMotionListener(motionListeners[i]);
    }
////    toolBar.addMouseListener(dragListener);
    toolBar.addMouseListener(mouseListener);
    toolBar.addMouseMotionListener(mouseMotionListener);

    glue = new JPanel(new BorderLayout(0, 0));
    JPanel toolBarPanel = new JPanel(new BorderLayout(0, 0));
    toolBarPanel.addContainerListener(new java.awt.event.ContainerAdapter() {
      public void componentAdded(java.awt.event.ContainerEvent e) {
        isExtracted = false;
        CCoolBarItem.this.setVisible(true);
      }
      public void componentRemoved(java.awt.event.ContainerEvent e) {
        isExtracted = true;
        CCoolBarItem.this.setVisible(false);
      }
    });
    itemSeparatorPanel = new JPanel(new BorderLayout());
    itemSeparatorPanel.setVisible(false);
    itemSeparatorPanel.setPreferredSize(new Dimension(4, 0));
    itemSeparatorPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, getBackground().darker()), javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, getBackground().brighter())));
    toolBarPanel.add(toolBar, BorderLayout.CENTER);
    JPanel toolBarCompositePanel = new JPanel(new BorderLayout(0, 0));
    if(getComponentOrientation().isLeftToRight()) {
      toolBarCompositePanel.add(itemSeparatorPanel, BorderLayout.WEST);
      toolBarCompositePanel.add(toolBarPanel, BorderLayout.EAST);
      add(toolBarCompositePanel, BorderLayout.EAST);
    } else {
      toolBarCompositePanel.add(itemSeparatorPanel, BorderLayout.EAST);
      toolBarCompositePanel.add(toolBarPanel, BorderLayout.WEST);
//      toolBarPanel.add(handlePanel, BorderLayout.EAST);
//      add(toolBarPanel, BorderLayout.WEST);
      add(toolBarCompositePanel, BorderLayout.WEST);
    }
    add(glue, BorderLayout.CENTER);
    setIndent(0);
  }

  /**
   * Get the toolbar.
   * @return The tool bar contained in this item.
   */
  JToolBar getToolBar() {
    return toolBar;
  }

  /**
   * Indicate if the cool bar items are locked.
   * @return True if the cool bar items are locked.
   */
  public boolean isLocked() {
    return !toolBar.isFloatable();
  }

  /**
   * Set whether the bar is extratable.
   * @param isExtractable True if the bar is extractable.
   */
  public void setExtractable(boolean isExtractable) {
    this.isExtractable = isExtractable;
    if(isExtracted) {
      javax.swing.plaf.basic.BasicToolBarUI ui = (javax.swing.plaf.basic.BasicToolBarUI)toolBar.getUI();
      if(ui.isFloating()) {
        ui.setFloating(false, null);
      }
    }
  }

  /**
   * Set whether the cool bar items are locked.
   * @param isLocked True if the cool bar items are to be locked.
   */
  public void setLocked(boolean isLocked) {
    if(isLocked) {
      javax.swing.plaf.basic.BasicToolBarUI ui = (javax.swing.plaf.basic.BasicToolBarUI)toolBar.getUI();
      if(ui.isFloating()) {
        ui.setFloating(false, null);
      }
    }
    itemSeparatorPanel.setVisible(isLocked);
    toolBar.setFloatable(!isLocked);
  }

  /**
   * Recompute the cool bar size.
   */
  protected void recomputeSize() {
    CCoolBar coolBar = getCoolBar();
    if(coolBar != null) {
      coolBar.setSize(coolBar.getWidth(), coolBar.getPreferredSize().height);
    }
  }

  /**
   * Get the height the row would prefer if it was alone in the line.
   * @return The preffered height.
   */
  protected int getPreferredRowHeight() {
    int height = 0;
    for(int i=0; i<getComponentCount(); i++) {
      height = Math.max(height, getComponent(i).getPreferredSize().height);
    }
    return height;
  }

  /**
   * Add a component.
   * @see java.awt.Container#add(java.awt.Component)
   */
  public Component add(Component comp) {
    Component c = toolBar.add(comp);
    recomputeSize();
    return c;
  }

  /**
   * Add a component at the specified index.
   * @see java.awt.Container#add(java.awt.Component, int)
   */
  public Component add(Component comp, int index) {
    Component c = toolBar.add(comp, index);
    recomputeSize();
    return c;
  }

  /**
   * Add a separator.
   */
  public void addSeparator() {
    toolBar.addSeparator();
  }

  /** The wrap state. */
  boolean isWrapped;
  
  /**
   * Get the wrap state.
   * @return True if the item is wrapped.
   */
  public boolean isWrapped() {
    return isWrapped;
  }
  
  /**
   * Set whether this item is wrapped.
   * @param isWrapped True if the item is to be wrapped.
   */
  public void setWrapped(boolean isWrapped) {
    if(this.isWrapped ^ isWrapped) {
      this.isWrapped = isWrapped;
      revalidate();
    }
  }
  
  /** The indent. */
  private int indent = 0;
  
  /**
   * Get the indent.
   * @return the indent.
   */
  int getIndent() {
    return indent;
  }
  
  /**
   * Set the indent.
   * @param indent The new indent.
   */
  void setIndent(int indent) {
    this.indent = indent;
    glue.setPreferredSize(new Dimension(indent, 0));
    revalidate();
    recomputeSize();
  }

}
