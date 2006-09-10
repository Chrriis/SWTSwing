/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class JMultiLineLabel extends JPanel implements SwingConstants {

  protected class InnerLabel extends JLabel {

//    public String getText() {
//      if(getIcon() != null) {
//        return "";
//      }
//      return super.getText();
//    }

    public Dimension getPreferredSize() {
//      if(isWrapping) {
//        Dimension preferredSize = super.getPreferredSize();
//        View view = ((View)getClientProperty(BasicHTML.propertyKey)).getView(0);
//        Dimension size = super.getSize();
//        view.setSize(size.width, 0);
//        preferredSize.height = super.getPreferredSize().height;
//        return preferredSize;
//      }
      Dimension size = super.getPreferredSize();
      if(getIcon() == null && getText().length() == 0) {
        size.height += getFontMetrics(getFont()).getHeight();
      }
      return size;
    }
    public Dimension getMaximumSize() {
      return new Dimension(Integer.MAX_VALUE, super.getMaximumSize().height);
    }

  }

  public JMultiLineLabel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    createContent();
  }

  protected String text = "";
  protected int mnemonicIndex = -1;

  public void setText(String text, int mnemonicIndex) {
    if(text == null) {
      text = "";
    }
    if(this.text.equals(text) && this.mnemonicIndex == mnemonicIndex) {
      return;
    }
    this.text = text;
    this.mnemonicIndex = mnemonicIndex;
    createContent();
  }

  protected void createContent() {
    removeAll();
    String[] labels = text.split("\n");
    int count = 0;
    for(int i=0; i<labels.length; i++) {
      String label = labels[i];
      if(isWrapping()) {
        label = "<html>" + Utils.escapeSwingXML(label) + "</html>";
      }
      InnerLabel innerLabel = new InnerLabel();
      innerLabel.setHorizontalAlignment(alignment);
      innerLabel.setText(label);
      int newCount = count + label.length() + 1;
      // TODO: check with HTML style what to do with mnemonics
      if(count < mnemonicIndex && newCount > mnemonicIndex) {
        innerLabel.setDisplayedMnemonicIndex(mnemonicIndex - count);
      }
      count = newCount;
      add(innerLabel);
    }
    revalidate();
    repaint();
  }

  protected boolean isWrapping;

  public void setWrapping(boolean isWrapping) {
    if(this.isWrapping == isWrapping) {
      return;
    }
    this.isWrapping = isWrapping;
    createContent();
  }

  public boolean isWrapping() {
    return isWrapping;
  }

  protected int alignment = JLabel.LEFT;

  public void setHorizontalAlignment(int alignment) {
    this.alignment = alignment;
    for(int i=getComponentCount()-1; i>=0; i--) {
      ((InnerLabel)getComponent(i)).setHorizontalAlignment(alignment);
    }
  }

  public void setIcon(Icon icon) {
    if(getComponentCount() > 0) {
      ((InnerLabel)getComponent(0)).setIcon(icon);
    }
  }

}
