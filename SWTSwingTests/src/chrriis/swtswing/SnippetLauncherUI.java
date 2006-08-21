/*
 * @(#)SnippetLauncher.java
 * 
 * Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com
 * 
 * See the file "LICENSE.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package chrriis.swtswing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import chrriis.swtswing.SnippetLauncher.Snippet;
import chrriis.swtswing.SnippetLauncher.SnippetCategory;
import chrriis.swtswing.source.SourcePane;

public class SnippetLauncherUI extends JFrame {

  protected static final ImageIcon APPLICATION_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/SWTSwingIcon32x32.gif"));
  protected static final ImageIcon SOURCE_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/source.gif"));

  protected static final Icon SWT_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/SWT100x55.png"));
  protected static final Icon SWTSWING_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/SWTSwing100x55.png"));

  protected static final Icon ROOT_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/snippets.gif"));
  protected static final Icon UNSUPPORTED_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/Unsupported.gif"));
  protected static final Icon WORKING_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/Working.gif"));
  protected static final Icon PARTIALLY_WORKING_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/PartiallyWorking.gif"));
  protected static final Icon NOT_WORKING_ICON = new ImageIcon(SnippetLauncherUI.class.getResource("resources/NotWorking.gif"));

  protected static final String SWT_SWING = "SWTSwing";
  protected static final String NATIVE_SWT = "Native SWT";

  protected static final String LAUNCH_TEXT = "Launch";
  protected static final String TERMINATE_TEXT = "Terminate";

  protected static final Color HIGHLIGHT_COLOR = Color.YELLOW;

  protected JButton viewSourceButton;
  protected JButton launchButton;
  protected JComboBox swtComboBox;
  protected JTextArea processTextArea;
  protected JCheckBox lookAndFeelCheckBox;
  protected JLabel lookAndFeelClassPathLabel;
  protected JCheckBox realDispatchCheckBox;
  protected JTextField lookAndFeelField;
  protected JTextField lookAndFeelClassPathField;
  protected JTextField classPathField;
  protected JTextField libraryPathField;

  protected String filterText;

  public SnippetLauncherUI() {
    super("SWTSwing Snippet Launcher");
    setIconImage(APPLICATION_ICON.getImage());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationByPlatform(true);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if(process != null) {
          try {
            process.destroy();
          } catch(Exception ex) {
          }
        }
      }
    });
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setContinuousLayout(true);
    JPanel leftComponent = new JPanel(new BorderLayout(0, 0));
    final JTextField filterTextField = new JTextField();
    leftComponent.add(filterTextField, BorderLayout.NORTH);
    final JTree snippetTree = createSnippetTree();
    filterTextField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        validateSelection();
      }
      public void insertUpdate(DocumentEvent e) {
        validateSelection();
      }
      public void removeUpdate(DocumentEvent e) {
        validateSelection();
      }
      protected void validateSelection() {
        filterText = filterTextField.getText();
        if(filterText.length() == 0) {
          filterText = null;
        } else {
          filterText = filterText.toLowerCase(Locale.ENGLISH);
        }
        snippetTree.repaint();
      }
    });
    leftComponent.add(new JScrollPane(snippetTree), BorderLayout.CENTER);
    splitPane.setLeftComponent(leftComponent);
    JPanel centerPane = new JPanel(new BorderLayout());
    JPanel swtSelectionPane = new JPanel(new BorderLayout());
    swtSelectionPane.setBorder(BorderFactory.createTitledBorder("SWT Selection"));
    JPanel swtChoicePane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
    swtComboBox = new JComboBox(new String[] {SWT_SWING, "Native SWT"});
    swtComboBox.setRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        DefaultListCellRenderer renderer = (DefaultListCellRenderer)super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        if(NATIVE_SWT.equals(value)) {
          renderer.setIcon(SWT_ICON);
        } else {
          renderer.setIcon(SWTSWING_ICON);
        }
        return renderer;
      }
    });
    swtChoicePane.add(swtComboBox);
    swtSelectionPane.add(swtChoicePane, BorderLayout.NORTH);
    final JPanel swtConfigurationPane = new JPanel(new BorderLayout(0, 0));
    final JPanel swtSwingPanel = createSWTSwingPanel();
    final JPanel nativeSWTPanel = createNativeSWTPanel();
    swtComboBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        swtConfigurationPane.removeAll();
        if(SWT_SWING.equals(e.getItem())) {
          swtConfigurationPane.add(swtSwingPanel, BorderLayout.CENTER);
        } else {
          swtConfigurationPane.add(nativeSWTPanel, BorderLayout.CENTER);
        }
        swtConfigurationPane.revalidate();
        swtConfigurationPane.repaint();
      }
    });
    swtConfigurationPane.add(swtSwingPanel);
    swtSelectionPane.add(swtConfigurationPane, BorderLayout.CENTER);
    centerPane.add(swtSelectionPane, BorderLayout.NORTH);
    JPanel southPane = new JPanel(new BorderLayout());
    JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    viewSourceButton = new JButton("View Source");
    viewSourceButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showSnippetSource();
      }
    });
    viewSourceButton.setEnabled(false);
    buttonPane.add(viewSourceButton);
    launchButton = new JButton(LAUNCH_TEXT);
    launchButton.setEnabled(false);
    launchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        launchSnippet();
      }
    });
    buttonPane.add(launchButton);
    southPane.add(buttonPane, BorderLayout.NORTH);
    final JScrollPane processScrollPane = new JScrollPane();
    processTextArea = new JTextArea() {
      public void append(String str) {
        super.append(str);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            JScrollBar verticalScrollBar = processScrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount());
          }
        });
      }
    };
    processScrollPane.getViewport().setView(processTextArea);
    processTextArea.setEditable(false);
    southPane.add(processScrollPane, BorderLayout.CENTER);
    centerPane.add(southPane, BorderLayout.CENTER);
    splitPane.setRightComponent(centerPane);
    getContentPane().add(splitPane, BorderLayout.CENTER);
    JPanel southComponent = new JPanel(new BorderLayout(0, 0));
    southComponent.add(createStatisticsComponent(), BorderLayout.WEST);
    southComponent.add(new JLabel("Christopher Deckers - chrriis@nextencia.net "), BorderLayout.EAST);
    getContentPane().add(southComponent, BorderLayout.SOUTH);
    setSize(800, 600);
  }

  protected String getClassPath() {
    String classPath = System.getProperty("java.class.path");
    String pathSeparator = System.getProperty("path.separator");
    if(System.getProperty("javawebstart.version") != null) {
      try {
        java.lang.ClassLoader cl = getClass().getClassLoader();
        java.lang.reflect.Field jcpField = cl.getClass().getDeclaredField("_jcp");
        jcpField.setAccessible(true);
        Object jcp = jcpField.get(cl);
        java.lang.reflect.Field fileToUrlsField = jcp.getClass().getDeclaredField("_fileToUrls");
        fileToUrlsField.setAccessible(true);
        java.util.HashMap fileToUrlFieldMap = (java.util.HashMap)fileToUrlsField.get(jcp);
        StringBuffer sb = new StringBuffer();
        for(Iterator it = fileToUrlFieldMap.keySet().iterator(); it.hasNext(); ) {
          if(sb.length() != 0) {
            sb.append(pathSeparator);
            sb.append(((File)it.next()).getAbsolutePath());
          }
        }
        sb.append(classPath);
        return sb.toString();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    return classPath;
  }

  protected JPanel createSWTSwingPanel() {
    GridBagLayout gridBag = new GridBagLayout();
    JPanel contentPane = new JPanel(gridBag);
    GridBagConstraints cons = new GridBagConstraints();
    cons.gridx = 0;
    cons.gridy = 0;
    cons.insets = new Insets(2, 2, 2, 2);
    cons.fill = GridBagConstraints.BOTH;
    lookAndFeelCheckBox = new JCheckBox("Look And Feel: ");
    lookAndFeelCheckBox.setSelected(true);
    gridBag.setConstraints(lookAndFeelCheckBox, cons);
    contentPane.add(lookAndFeelCheckBox);
    cons.gridy++;
    cons.insets = new Insets(2, new JCheckBox().getPreferredSize().width + 2, 2, 2);
    lookAndFeelClassPathLabel = new JLabel("ClassPath: ");
    gridBag.setConstraints(lookAndFeelClassPathLabel, cons);
    contentPane.add(lookAndFeelClassPathLabel);
    cons.insets = new Insets(2, 2, 2, 2);
    cons.gridy++;
    cons.gridwidth = 2;
    realDispatchCheckBox = new JCheckBox("Use Real Dispatching");
    realDispatchCheckBox.setSelected(true);
    gridBag.setConstraints(realDispatchCheckBox, cons);
    contentPane.add(realDispatchCheckBox);
    cons.gridwidth = 1;
    cons.weightx = 1.0;
    cons.gridy = 0;
    cons.gridx++;
    lookAndFeelField = new JTextField("javax.swing.plaf.metal.MetalLookAndFeel");
    gridBag.setConstraints(lookAndFeelField, cons);
    contentPane.add(lookAndFeelField);
    cons.gridy++;
    lookAndFeelClassPathField = new JTextField();
    gridBag.setConstraints(lookAndFeelClassPathField, cons);
    contentPane.add(lookAndFeelClassPathField);
    return contentPane;
  }

  protected JPanel createNativeSWTPanel() {
    GridBagLayout gridBag = new GridBagLayout();
    JPanel contentPane = new JPanel(gridBag);
    GridBagConstraints cons = new GridBagConstraints();
    cons.gridx = 0;
    cons.gridy = 0;
    cons.insets = new Insets(2, 2, 2, 2);
    cons.fill = GridBagConstraints.BOTH;
    JLabel classPathLabel = new JLabel("Class-Path: ");
    gridBag.setConstraints(classPathLabel, cons);
    contentPane.add(classPathLabel);
    cons.gridy++;
    JLabel libraryPathLabel = new JLabel("Library Path: ");
    gridBag.setConstraints(libraryPathLabel, cons);
    contentPane.add(libraryPathLabel);
    cons.weightx = 1.0;
    cons.gridy = 0;
    cons.gridx++;
    classPathField = new JTextField("./swt.jar" + System.getProperty("path.separator") + "./swt-pi.jar");
    gridBag.setConstraints(classPathField, cons);
    contentPane.add(classPathField);
    cons.gridy++;
    libraryPathField = new JTextField(".");
    gridBag.setConstraints(libraryPathField, cons);
    contentPane.add(libraryPathField);
    return contentPane;
  }
  
  protected Snippet selectedSnippet;
  
  protected void setSelectedSnippet(Snippet snippet) {
    selectedSnippet = snippet;
    if(snippet == null) {
      viewSourceButton.setEnabled(false);
      launchButton.setEnabled(false);
    } else {
      viewSourceButton.setEnabled(true);
      launchButton.setEnabled(true);
    }
  }
  
  protected Component createStatisticsComponent() {
    int total = 0;
    int achieved = 0;
    for(int i=0; i<SnippetLauncher.snippetCategories.length; i++) {
      SnippetCategory category = SnippetLauncher.snippetCategories[i];
      Snippet[] snippets = category.getSnippets();
      for(int j=0; j<snippets.length; j++) {
        Snippet snippet = snippets[j];
        switch(snippet.getStatus()) {
        case Snippet.WORKING:
          total += 2;
          achieved += 2;
          break;
        case Snippet.PARTIALLY_WORKING:
          total += 2;
          achieved++;
          break;
        case Snippet.NOT_WORKING:
          total += 2;
          break;
        }
      }
    }
    final int ratio = Math.round((float)achieved * 100 / total);
    JLabel label = new JLabel(ratio + "% supported", JLabel.CENTER) {
      protected void paintComponent(Graphics g) {
        Dimension size = getSize();
        int amount = Math.round(size.width * ratio / 100);
        g.setColor(SUCCESS_COLOR);
        g.fillRect(0, 0, amount, size.height);
        g.setColor(FAILURE_COLOR);
        g.fillRect(amount, 0, size.width - amount - 1, size.height);
        super.paintComponent(g);
      }
    };
    label.setBorder(BorderFactory.createEtchedBorder());
    label.setPreferredSize(new Dimension(150, label.getPreferredSize().height));
    return label;
  }
  
  protected JTree createSnippetTree() {
    final JTree tree = new JTree(new TreeModel() {
      protected Object root = "Snippets";
      protected EventListenerList listenerList = new EventListenerList();
      public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
      }
      public Object getChild(Object parent, int index) {
        if(parent == root) {
          return SnippetLauncher.snippetCategories[index];
        }
        if(parent instanceof SnippetCategory) {
          return ((SnippetCategory)parent).getSnippets()[index];
        }
        return null;
      }
      public int getChildCount(Object parent) {
        if(parent == root) {
          return SnippetLauncher.snippetCategories.length;
        }
        if(parent instanceof SnippetCategory) {
          return ((SnippetCategory)parent).getSnippets().length;
        }
        return 0;
      }
      public int getIndexOfChild(Object parent, Object child) {
        if(parent == root) {
          for(int i=0; i<SnippetLauncher.snippetCategories.length; i++) {
            if(SnippetLauncher.snippetCategories[i] == child) {
              return i;
            }
          }
        } else if(parent instanceof SnippetCategory) {
          Snippet[] snippets = ((SnippetCategory)parent).getSnippets();
          for(int i=0; i<snippets.length; i++) {
            if(snippets[i] == child) {
              return i;
            }
          }
        }
        return -1;
      }
      public Object getRoot() {
        return root;
      }
      public boolean isLeaf(Object node) {
        return node instanceof Snippet;
      }
      public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
      }
      public void valueForPathChanged(TreePath path, Object newValue) {
      }
    });
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if(e.getClickCount() != 2) {
          return;
        }
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if(path == null) {
          return;
        }
        Object o = path.getLastPathComponent();
        if(o == selectedSnippet) {
          if(process != null) {
            return;
          }
          launchSnippet();
        }
      }
    });
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getNewLeadSelectionPath();
        if(path != null) {
          Object o = path.getLastPathComponent();
          if(o instanceof Snippet) {
            setSelectedSnippet((Snippet)o);
            return;
          }
        }
        setSelectedSnippet(null);
      }
    });
    tree.setCellRenderer(new DefaultTreeCellRenderer() {
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultTreeCellRenderer c = (DefaultTreeCellRenderer)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        c.setOpaque(false);
        if(value instanceof Snippet) {
          Snippet snippet = (Snippet)value;
          switch(snippet.getStatus()) {
          case Snippet.UNSUPPORTED: c.setIcon(UNSUPPORTED_ICON); break;
          case Snippet.WORKING: c.setIcon(WORKING_ICON); break;
          case Snippet.PARTIALLY_WORKING: c.setIcon(PARTIALLY_WORKING_ICON); break;
          case Snippet.NOT_WORKING: c.setIcon(NOT_WORKING_ICON); break;
          }
          if(filterText != null && c.getText().toLowerCase(Locale.ENGLISH).contains(filterText)) {
            c.setOpaque(true);
            c.setBackground(HIGHLIGHT_COLOR);
          }
        } else if(value instanceof SnippetCategory) {
          Snippet[] snippets = ((SnippetCategory)value).getSnippets();
          int total = 0;
          int achieved = 0;
          boolean isFound = false;
          for(int i=0; i<snippets.length; i++) {
            Snippet snippet = snippets[i];
            switch(snippet.getStatus()) {
            case Snippet.WORKING:
              total += 2;
              achieved += 2;
              break;
            case Snippet.PARTIALLY_WORKING:
              total += 2;
              achieved++;
              break;
            case Snippet.NOT_WORKING:
              total += 2;
              break;
            }
            if(!isFound && filterText != null && snippet.toString().toLowerCase(Locale.ENGLISH).contains(filterText)) {
              isFound = true;
            }
          }
          int ratio = total == 0? -1: Math.round(((float)achieved) * 14 / total);
          ImageIcon icon = (ImageIcon)ratioToIconMap.get(new Integer(ratio));
          if(icon == null) {
            BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.setColor(Color.black);
            g.drawRect(0, 4, 15, 7);
            if(total > 0) {
              g.setColor(FAILURE_COLOR);
              g.fillRect(1 + ratio, 5, 14 - ratio, 6);
              g.setColor(SUCCESS_COLOR);
              g.fillRect(1, 5, ratio, 6);
            } else {
              g.setColor(UNDETERMINED_COLOR);
              g.fillRect(1, 5, 14, 6);
            }
            ratioToIconMap.put(new Integer(ratio), icon);
            icon = new ImageIcon(image);
          }
          c.setIcon(icon);
          if(isFound) {
            c.setOpaque(true);
            c.setBackground(HIGHLIGHT_COLOR);
          }
        } else {
          c.setIcon(ROOT_ICON);
        }
        return c;
      }
    });
    return tree;
  }

  protected HashMap ratioToIconMap = new HashMap();
  protected static final Color UNDETERMINED_COLOR = Color.LIGHT_GRAY;
  protected static final Color SUCCESS_COLOR = new Color(0, 197, 0);
  protected static final Color FAILURE_COLOR = Color.RED;

  protected Process process;

  protected class StreamGobbler extends Thread {
    protected InputStream is;
    
    public StreamGobbler(InputStream is, String name) {
      super("StreamGobbler: " + name);
      this.is = is;
    }

    public void run() {
      try {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        String lineSeparator = System.getProperty("line.separator");
        while ((line = br.readLine()) != null) {
          processTextArea.append(line + lineSeparator);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  protected void launchSnippet() {
    if(selectedSnippet == null) {
      return;
    }
    if(process != null) {
      process.destroy();
      return;
    }
    launchButton.setText(TERMINATE_TEXT);
    processTextArea.setText("");
    try {
      ArrayList parameterList = new ArrayList();
      if(NATIVE_SWT.equals(swtComboBox.getSelectedItem())) {
        String pathSeparator = System.getProperty("path.separator");
        String classPath = classPathField.getText();
        if(classPath.length() == 0) {
          classPath = "./swt.jar" + pathSeparator + "./swt-pi.jar";
        }
        boolean isPathValid = false;
        StringTokenizer st = new StringTokenizer(classPath, pathSeparator);
        while(st.hasMoreTokens()) {
          File file = new File(st.nextToken());
          if(file.exists() && file.isFile()) {
            isPathValid = true;
            break;
          }
        }
        if(!isPathValid) {
          processTextArea.setText("The classpath is not valid!");
          launchButton.setText(LAUNCH_TEXT);
          return;
        }
        parameterList.add(System.getProperty("java.home") + "/bin/java");
        String libraryPath = libraryPathField.getText();
        if(libraryPath.length() == 0) {
          libraryPath = ".";
        }
        parameterList.add("-Djava.library.path=" + libraryPath);
        parameterList.add("-Dswt.swing.realdispatch=false");
        parameterList.add("-cp");
        parameterList.add(classPath + pathSeparator + System.getProperty("java.class.path"));
        parameterList.add("chrriis.swtswing.SnippetLauncher");
        parameterList.add(String.valueOf(selectedSnippet.getNumber()));
      } else {
        parameterList.add(System.getProperty("java.home") + "/bin/java");
        parameterList.add("-Dswt.swing.realdispatch=" + realDispatchCheckBox.isSelected());
        String classPath = "";
        if(lookAndFeelCheckBox.isSelected()) {
          String laf = lookAndFeelField.getText();
          if(laf.length() > 0) {
            parameterList.add("-Dswt.swing.laf=" + laf);
          }
          classPath = lookAndFeelClassPathField.getText();
        }
        if(classPath.length() != 0) {
          String pathSeparator = System.getProperty("path.separator");
          classPath += pathSeparator;
        }
        parameterList.add("-cp");
        parameterList.add(classPath + System.getProperty("java.class.path"));
        parameterList.add("chrriis.swtswing.SnippetLauncher");
        parameterList.add(String.valueOf(selectedSnippet.getNumber()));
      }
      ProcessBuilder pb = new ProcessBuilder(parameterList);
      pb.directory(new File(System.getProperty("user.dir")));
      process = pb.start();
      new StreamGobbler(process.getErrorStream(), "Error").start();
      new StreamGobbler(process.getInputStream(), "Input").start();
    } catch(Exception e) {
      launchButton.setText(LAUNCH_TEXT);
      e.printStackTrace();
    }
    new Thread() {
      public void run() {
        while(true) {
          try {
            sleep(100);
          } catch(Exception e) {
          }
          try {
            process.exitValue();
            process = null;
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                launchButton.setText(LAUNCH_TEXT);
              }
            });
            break;
          } catch(Exception e) {
          }
        }
      }
    }.start();
  }

  protected void showSnippetSource() {
    if(selectedSnippet == null) {
      return;
    }
    int number = selectedSnippet.getNumber();
    try {
      JFrame sourceFrame = new JFrame("Snippet" + number + ".java");
      sourceFrame.setIconImage(SOURCE_ICON.getImage());
      sourceFrame.setLocationByPlatform(true);
      SourcePane sourcePane = new SourcePane(new InputStreamReader(getClass().getResourceAsStream("/org/eclipse/swt/snippets/Snippet" + number + ".java")));
      sourceFrame.getContentPane().add(new JScrollPane(sourcePane), BorderLayout.CENTER);
      sourceFrame.setSize(800, 600);
      sourceFrame.setVisible(true);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Toolkit.getDefaultToolkit().setDynamicLayout(true);
    new SnippetLauncherUI().setVisible(true);
  }

}
