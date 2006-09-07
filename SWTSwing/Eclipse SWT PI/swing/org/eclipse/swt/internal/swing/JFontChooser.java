/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

// -- Code contributed --

//A font chooser that allows users to pick a font by name, size, style, and
//color.  The color selection is provided by a JColorChooser pane.  This
//dialog builds an AttributeSet suitable for use with JTextPane.
//
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class JFontChooser extends JDialog implements ActionListener {

  private JColorChooser colorChooser;
  private JComboBox fontName;
  private JCheckBox fontBold, fontItalic;
  private JTextField fontSize;
  private JLabel previewLabel;
  private SimpleAttributeSet attributes;
  private Font newFont;
  private Color newColor;

  
  public JFontChooser(Dialog parent) {
    super(parent, "Font Chooser", true);
    init();
  }
  
  public JFontChooser(Frame parent) {
    super(parent, "Font Chooser", true);
    init();
  }
  
  protected void init() {
    setSize(484, 494);
    setLocation(270, 137);
    attributes = new SimpleAttributeSet();

    // Make sure that any way the user cancels the window does the right thing
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        closeAndCancel();
      }
    });

    // Start the long process of setting up our interface
    Container c = getContentPane();
    
    Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    Vector names = new Vector();
    
    for (int i=0; i<fonts.length; i++) {
      Font font = fonts[i];
      if (!names.contains(font.getFamily()))
          names.add(font.getFamily());
    }
    
    JPanel fontPanel = new JPanel();
    fontName = new JComboBox(names);
    fontName.setSelectedItem(fontName.getFont().getFamily());
    fontName.addActionListener(this);
    fontSize = new JTextField("" + fontName.getFont().getSize(), 4);
    fontSize.setHorizontalAlignment(SwingConstants.RIGHT);
    fontSize.addActionListener(this);
    fontBold = new JCheckBox("Bold");
    fontBold.setSelected(fontName.getFont().isBold());
    fontBold.addActionListener(this);
    fontItalic = new JCheckBox("Italic");
    fontItalic.addActionListener(this);
    fontItalic.setSelected(fontName.getFont().isItalic());

    fontPanel.add(fontName);
    fontPanel.add(new JLabel(" Size: "));
    fontPanel.add(fontSize);
    fontPanel.add(fontBold);
    fontPanel.add(fontItalic);

    c.add(fontPanel, BorderLayout.NORTH);

    // Set up the color chooser panel and attach a change listener so that color
    // updates get reflected in our preview label.
    colorChooser = new JColorChooser(fontName.getForeground());
    colorChooser.getSelectionModel().addChangeListener(
        new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            updatePreviewColor();
          }
        });
    c.add(colorChooser, BorderLayout.CENTER);

    JPanel previewPanel = new JPanel(new BorderLayout());
    previewLabel = new JLabel("The quick brown fox jumps over the lazy red dog.");
    previewLabel.setForeground(colorChooser.getColor());
    previewPanel.add(previewLabel, BorderLayout.CENTER);

    // Add in the Ok and Cancel buttons for our dialog box
    JButton okButton = new JButton("   OK   ");
    okButton.setToolTipText("Select the current font and color and clsoe the dialog");
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndSave();
      }
    });
    getRootPane().setDefaultButton(okButton);
    
    JButton cancelButton = new JButton("Cancel");
    cancelButton.setToolTipText("Cancel the changes and close the dialog");
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndCancel();
      }
    });

    JPanel controlPanel = new JPanel();
    controlPanel.add(cancelButton);
    controlPanel.add(okButton);
    previewPanel.add(controlPanel, BorderLayout.SOUTH);

    // Give the preview label room to grow.
    previewPanel.setMinimumSize(new Dimension(100, 100));
    previewPanel.setPreferredSize(new Dimension(100, 100));

    c.add(previewPanel, BorderLayout.SOUTH);
  }

  // Ok, something in the font changed, so figure that out and make a
  // new font for the preview label
  public void actionPerformed(ActionEvent ae) {
    // Check the name of the font
    if (!StyleConstants.getFontFamily(attributes).equals(
        fontName.getSelectedItem())) {
      StyleConstants.setFontFamily(attributes, (String) fontName
          .getSelectedItem());
    }
    // Check the font size (no error checking yet)
    if (StyleConstants.getFontSize(attributes) != Integer.parseInt(fontSize
        .getText())) {
      StyleConstants.setFontSize(attributes, Integer.parseInt(fontSize
          .getText()));
    }
    // Check to see if the font should be bold
    if (StyleConstants.isBold(attributes) != fontBold.isSelected()) {
      StyleConstants.setBold(attributes, fontBold.isSelected());
    }
    // Check to see if the font should be italic
    if (StyleConstants.isItalic(attributes) != fontItalic.isSelected()) {
      StyleConstants.setItalic(attributes, fontItalic.isSelected());
    }
    // and update our preview label
    updatePreviewFont();
  }

  // Get the appropriate font from our attributes object and update
  // the preview label
  protected void updatePreviewFont() {
    String name = StyleConstants.getFontFamily(attributes);
    boolean bold = StyleConstants.isBold(attributes);
    boolean ital = StyleConstants.isItalic(attributes);
    int size = StyleConstants.getFontSize(attributes);

    //Bold and italic don???t work properly in beta 4.
    Font f = new Font(name, (bold ? Font.BOLD : 0)
        + (ital ? Font.ITALIC : 0), size);
    previewLabel.setFont(f);
  }

  // Get the appropriate color from our chooser and update previewLabel
  protected void updatePreviewColor() {
    previewLabel.setForeground(colorChooser.getColor());
    // Manually force the label to repaint
    previewLabel.repaint();
  }

  public Font getNewFont() {
    return newFont;
  }

  public Color getNewColor() {
    return newColor;
  }

  public AttributeSet getAttributes() {
    return attributes;
  }

  public void closeAndSave() {
    // Save font & color information
    newFont = previewLabel.getFont();
    newColor = previewLabel.getForeground();

    // Close the window
    setVisible(false);
  }

  public void closeAndCancel() {
    // Erase any font information and then close the window
    newFont = null;
    newColor = null;
    setVisible(false);
  }

  public void setDefaultFont(Font currentFont) {
    if (currentFont == null) return;
    
    newFont = currentFont;
    fontName.setSelectedItem(currentFont.getFamily());
    fontBold.setSelected(currentFont.isBold());
    fontItalic.setSelected(currentFont.isItalic());
  }

  public void setDefaultColor(Color currentColor) {
    if (currentColor == null) return;
    
    newColor = currentColor;
    colorChooser.setColor(currentColor);
  }
}
