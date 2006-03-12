package chrriis.swtswing;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.widgets.Display;

public class Test {

  static Object[] definitions = new Object[] {
    new Object[] {"Hello World", new String[][] {
      {"1", "Hello World"},
    }}, new Object[] {"Accessibility", new String[][] {
//      {"162", "an accessible listener to provide state information"},
//      {"164", "override the text that is spoken for a native Button"},
    }}, new Object[] {"Browser", new String[][] {
//      {"148", "check if the browser is available or not"},
//      {"128", "bring up a browser (single window)"},
//      {"173", "bring up a browser (support for new windows and pop-up filter)"},
//      {"136", "render HTML from memory"},
//      {"137", "render HTML that includes relative links from memory"},
//      {"161", "modify DOM (execute javascript)"},
//      {"160", "query DOM node value"},
//      {"159", "modify HTML title tag"},
    }}, new Object[] {"BusyIndicator", new String[][] {
//      {"130", "display busy cursor during long running task"},
    }}, new Object[] {"Button", new String[][] {
      {"108", "set the default button"},
      {"169", "make a toggle button have radio behavior"},
//      {"224", "implement radio behavior for setSelection()"},
    }}, new Object[] {"Canvas", new String[][] {
      {"48", "scroll an image (flicker free, no double buffering)"},
//      {"21", "implement tab traversal (behave like a tab group)"},
    }}, new Object[] {"Caret", new String[][] {
      {"74", "create a caret"},
//      {"43", "create a caret (using an image)"},
    }}, new Object[] {"Clipboard", new String[][] {
//      {"94", "copy and paste data with the clipboard"},
//      {"122", "enable/disable menu depending on clipboard content availability"},
    }}, new Object[] {"Combo", new String[][] {
      {"26", "create a combo box (non-editable)"},
//      {"24", "detect return in a combo box (default selection)"},
//      {"147", "prevent CR from going to the default button"},
    }}, new Object[] {"Composite", new String[][] {
      {"9", "scroll a child control automatically"},
//      {"75", "set the tab traversal order of children"},
      {"98", "create and dispose children of a composite"},
      {"115", "force radio behavior on two different composites"},
      {"46", "intercept mouse events (drag a button with the mouse)"},
    }}, new Object[] {"Color and RGB", new String[][] {
//      {"208", "adjust hue, brightness and saturation of a color"},
    }}, new Object[] {"Control", new String[][] {
      {"14", "detect mouse enter, exit and hover events"},
//      {"127", "prevent Tab from traversing out of a control"},
      {"25", "print key state, code and character"},
      {"62", "print mouse state and button (down, move, up)"},
//      {"214", "set a background image"},
    }}, new Object[] {"CoolBar", new String[][] {
//      {"20", "create a cool bar"},
      {"150", "create a cool bar (relayout when resized)"},
//      {"140", "drop-down a chevron menu containing hidden tool items"},
    }}, new Object[] {"CTabFolder, CTabItem", new String[][] {
//      {"82", "prevent an item from closing"},
//      {"165", "min and max buttons, close button and image only on selected tab"},
    }}, new Object[] {"Cursor", new String[][] {
      {"44", "set the hand cursor into a control"},
//      {"92", "create a cursor from a source and a mask"},
//      {"119", "create a color cursor from a source and a mask"},
      {"118", "create a color cursor from an image file"},
    }}, new Object[] {"DirectoryDialog", new String[][] {
//      {"33", "prompt for a directory"},
    }}, new Object[] {"Display", new String[][] {
      {"60", "create two one shot timers (5000 ms, 2000 ms)"},
      {"16", "create one repeating timer (every 500 ms)"},
      {"68", "stop a repeating timer when a button is pressed"},
      {"42", "get the bounds and client area of a display"},
      {"7", "fill a table from a background thread (sync exec)"},
//      {"142", "post mouse events (UI testing tools only)"},
//      {"146", "post key events (UI testing tools only)"},
    }}, new Object[] {"Drag and Drop", new String[][] {
//      {"78", "drag text between two labels"},
//      {"91", "drag leaf items in a tree"},
//      {"79", "define my own data transfer type"},
//      {"84", "define a default operation (in this example, Copy)"},
//      {"83", "determine data types available (win32 only)"},
//      {"158", "determine data types available (motif only)"},
//      {"185", "Dropped data type depends on target item in table"},
//      {"210", "dragging text from a StyledText widget"},
    }}, new Object[] {"ExpandBar", new String[][] {
//      {"223", "create a expand bar"},
    }}, new Object[] {"FileDialog", new String[][] {
      {"72", "prompt for a file name (to save)"},
    }}, new Object[] {"Font", new String[][] {
      {"100", "create a large font for use by a text widget"},
    }}, new Object[] {"FormLayout", new String[][] {
      {"65", "create a simple dialog using form layout"},
      {"69", "center a label and single line text using a form layout"},
      {"71", "create a simple OK/CANCEL dialog using form layout"},
    }}, new Object[] {"GC", new String[][] {
      {"13", "draw a thick line"}, // Works, but for real dispatching or depending on the JRE, a repaint event may happen and clear the frame immediately.
      {"93", "measure a string"},
      {"66", "implement a simple scribble program"},
      {"70", "create an icon (in memory)"},
//      {"95", "capture a widget image with a GC"},
//      {"215", "take a screen shot with a GC"},
      {"168", "draw lines with different cap and join styles"},
//      {"10", "drawing with transformations, paths and alpha blending"},
//      {"207", "reflect, shear and rotate images using matrix transformations"},
    }}, new Object[] {"GridLayout", new String[][] {
      {"172", "align widgets in rows and columns"},
      {"6", "insert widgets into a grid layout"},
      {"175", "exclude an invisible widget from a grid layout"},
    }}, new Object[] {"Image", new String[][] {
      {"112", "display an image in a group"},
      {"139", "rotate and flip an image"},
//      {"141", "display an animated GIF"},
//      {"194", "write an animated GIF"},
    }}, new Object[] {"Label", new String[][] {
      {"34", "create a label (with an image)"},
      {"37", "create a label (a separator)"},
    }}, new Object[] {"Link", new String[][] {
//      {"182", "create a link widget"},
//      {"183", "detect selection events in a link widget"},
    }}, new Object[] {"List", new String[][] {
      {"59", "print selected items in a list"},
    }}, new Object[] {"Menu", new String[][] {
      {"29", "create a bar and pull down menu (accelerators, mnemonics) "},
      {"40", "create a popup menu (set in multiple controls)"},
//      {"89", "create a menu with radio items"},
      {"73", "enable menu items dynamically (when menu shown)"},
      {"97", "fill a menu dynamically (when menu shown)"},
//      {"131", "show a popup menu (wait for it to close)"},
//      {"152", "update a status line when an item is armed"},
//      {"178", "access About, Preferences and Quit menus on carbon"},
    }}, new Object[] {"Monitor", new String[][] {
      {"120", "center a shell on the primary monitor"},
    }}, new Object[] {"OLE and ActiveX", new String[][] {
//      {"123", "get events from IE control"},
//      {"81", "browse the typelibinfo for a program id"},
//      {"157", "embed Word in an applet"},
//      {"186", "reading and writing to a SAFEARRAY"},
//      {"187", "execute a script function that exists on a web page"},
//      {"199", "listen for Excel AppEvents"},
    }}, new Object[] {"OpenGL", new String[][] {
//      {"195", "draw a rotating torus using the LWJGL OpenGL binding"},
//      {"209", "draw a rotating torus using the JOGL OpenGL binding"},
//      {"174", "draw a rectangle using the org.eclipse.opengl OpenGL binding"},
    }}, new Object[] {"Path", new String[][] {
//      {"198", "Create a path from some text"},      
    }}, new Object[] {"Printing", new String[][] {
//      {"132", "print "Hello World!" in black, outlined in red, to default printer"},
//      {"133", "print text to printer, with word wrap and pagination"},
    }}, new Object[] {"Program", new String[][] {
//      {"32", "find the icon of the program that edits .bmp files"},
//      {"105", "invoke an external batch file"},
//      {"30", "invoke the system text editor on autoexec.bat"},
    }}, new Object[] {"ProgressBar", new String[][] {
      {"57", "update a progress bar (from the UI thread)"},
      {"56", "update a progress bar (from another thread)"},
    }}, new Object[] {"RowLayout", new String[][] {
      {"176", "Lay out widgets in a row"},
      {"177", "Lay out widgets in a column"},
    }}, new Object[] {"Sash", new String[][] {
      {"54", "create a sash (allow it to be moved)"},
      {"107", "implement a simple splitter (with a 20 pixel limit)"},
    }}, new Object[] {"SashForm", new String[][] {
      {"109", "create a sash form with three children"},
    }}, new Object[] {"Scale", new String[][] {
      {"45", "create a scale (maximum 40, page increment 5)"},
    }}, new Object[] {"ScrolledComposite", new String[][] {
      {"5", "scroll a control in a scrolled composite"},
      {"166", "create a ScrolledComposite with wrapping content"},
//      {"167", "create two ScrolledComposites that scroll in tandem"},
//      {"188", "Scroll widgets into view when they get focus"},
    }}, new Object[] {"Shell", new String[][] {
      {"50", "create a dialog shell"},
      {"63", "create a dialog shell (prompt for a value)"},
      {"104", "create a splash screen"},
      {"99", "prevent a shell from closing (prompt the user)"},
//      {"4", "prevent escape from closing a dialog"},
      {"27", "open a shell minimized (iconified)"},
      {"28", "open a shell maximized (full screen)"},
//      {"134", "create a non-rectangular window"},
//      {"138", "set icons with different resolutions"},
//      {"180", "emulate transparent shell"},
    }}, new Object[] {"Slider", new String[][] {
      {"17", "print scroll event details"},
    }}, new Object[] {"Spinner", new String[][] {
      {"184", "create and initialize a spinner widget"},
//      {"190", "use floats in a spinner widget"},
    }}, new Object[] {"StyledText", new String[][] {
//      {"163", "change font style, foreground and background colors of StyledText"},
//      {"189", "underline and strike through text"},
//      {"211", "use rise and font with StyleRange"},
//      {"212", "embed images in StyledText"},
//      {"213", "use indent, alignment and justify"},
//      {"217", "embed controls in StyledText"},        
//      {"222", "use bulleted lists in StyledText"},        
//      {"218", "use gradient background in StyledText"},        
    }}, new Object[] {"Swing/AWT", new String[][] {
//      {"135", "embed Swing/AWT in SWT"},
      {"154", "embed a JTable in SWT (no flickering)"},
      {"155", "draw an X using AWT Graphics"},
      {"156", "convert between SWT Image and AWT BufferedImage"},
    }}, new Object[] {"TabFolder, TabItem", new String[][] {
      {"76", "create a tab folder (six pages)"},
    }}, new Object[] {"Table, TableItem, TableColumn", new String[][] {
//      {"151", "add 1000 sorted entries to a virtual table every 500 ms"},
//      {"129", "color cells and rows in table"},
      {"35", "create a table (no columns, no headers)"},
      {"38", "create a table (columns, headers, lines)"},
//      {"144", "create a table with 1,000,000 items (lazy)"},
//      {"201", "create a table with 1,000,000 items (lazy, page size 64)"},
      {"7", "create a table (lazy with a thread)"},
//      {"113", "detect a selection or check event in a table (SWT.CHECK)"},
//      {"3", "find a table cell from mouse down (SWT.FULL_SELECTION)"},
//      {"110", "find a table cell from mouse down (works for any table style)"},
      {"101", "insert a table item (at an index)"},
//      {"106", "insert a table column (at an index)"},
//      {"181", "make columns reorderable by dragging"},
//      {"126", "place arbitrary controls in a table"},
//      {"64", "print selected items in a table"},
//      {"53", "remove selected items"},
//      {"77", "resize columns as table resizes"},
//      {"51", "scroll a table (set the top index)"},
//      {"52", "select an index (select and scroll)"},
//      {"2", "sort a table by column"},
//      {"192", "sort a table by column (virtual table, sort indicator)"},
      {"103", "update table item text"},
    }}, new Object[] {"TableCursor", new String[][] {
//      {"96", "navigate a table cells with arrow keys"},
    }}, new Object[] {"TableEditor", new String[][] {
//      {"88", "edit the text of a table item (in place)"},
//      {"124", "edit a cell in a table (in place, fancy)"},
//      {"149", "place a progress bar in a table"},
    }}, new Object[] {"Text", new String[][] {
      {"117", "add a select all menu item to the control"},
      {"24", "detect CR in a text control (default selection)"},
      {"116", "prevent CR from going to the default button"},
//      {"121", "prompt for a password (set the echo character)"},
      {"55", "resize a text control (show about 10 characters)"},
      {"22", "select all the text in the control"},
      {"11", "set the selection (i-beam)"},
      {"12", "set the selection (start, end)"},
      {"19", "verify input (only allow digits)"},
//      {"179", "verify input in a template (YYYY/MM/DD)"},
//      {"191", "detect when the user scrolls a text control"},
//      {"196", "verify input using a regular expression"},
    }}, new Object[] {"TextLayout, TextStyle", new String[][] {
//      {"145", "draw internationalized styled text on a shell"},
//      {"197", "draw dynamically wrapped text on a shell"},
//      {"203", "justify, align and indent text"},
//      {"204", "change the rise of text relative to the baseline"},
//      {"205", "embed images and widgets in text"},
    }}, new Object[] {"ToolBar, ToolItem", new String[][] {
      {"18", "create a tool bar (text)"},
      {"36", "create a flat tool bar (images)"},
      {"47", "create tool bar (normal, hot and disabled images)"},
      {"49", "create tool bar (wrap on resize)"},
      {"58", "place a combo box in a tool bar"},
      {"67", "place a drop down menu in a tool bar"},
      {"153", "update a status line when the pointer enters a ToolItem"},
    }}, new Object[] {"Tool Tips", new String[][] {
      {"41", "create tool tips for a tab folder, tool bar and control"},
      {"216", "show a tool tip inside a rectangle"},
//      {"125", "create emulated tool tips for items in a table"},
//      {"225", "create a balloon tooltip for a tray item"},
    }}, new Object[] {"Tracker", new String[][] {
//      {"23", "create a tracker (drag on mouse down)"},
//      {"31", "create a tracker (drag when "torn off")"},
    }}, new Object[] {"Tray, TrayItem", new String[][] {
//      {"143", "place a popup menu on the system tray"},
    }}, new Object[] {"Tree", new String[][] {
      {"15", "create a tree"},
      {"8", "create a tree (lazy)"},
      {"114", "detect a selection or check event in a tree (SWT.CHECK)"},          
      {"102", "insert a tree item (at an index)"},
      {"61", "print selected items in a tree"},
//      {"80", "limit selection to items that match a pattern"},
//      {"90", "detect mouse down in a tree item"},
      {"170", "create a tree with columns"},
//      {"193", "make columns reorderable by dragging"},
//      {"202", "virtual tree - lazy creation of sub nodes"},
    }}, new Object[] {"TreeEditor", new String[][] {
//      {"111", "edit the text of a tree item (in place, fancy)"},
    }},
  };

  static String snippetNumber = "125";

  static boolean isRealDispatch = false;

  public static void main(final String[] args) {
    if(isRealDispatch) {
      try {
        Display.swtExec(new Runnable() {
          public void run() {
            snippetMain(args);
          }
        });
      } catch(Error e) {
        System.err.println("Fail to use real dispatching. Now using fallback method.");
        snippetMain(args);
      }
    } else {
      snippetMain(args);
    }
  }

  public static void snippetMain(String[] args) {
    String number = null;
    boolean isDevelopment = System.getProperty("swt.swing.snippets") != null && !snippetNumber.equals("");
    if(isDevelopment) {
      number = snippetNumber;
    }
    if(number == null && args.length < 1) {
      printUsage();
    } else {
      if(args.length > 1) {
        number = args[0];
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
        args = newArgs;
      }
      boolean isFound = isDevelopment;
      for(int i=0; i<definitions.length && !isFound; i++) {
        Object[] section = (Object[])definitions[i];
        String[][] pairs = (String[][])section[1];
        for(int j=0; j<pairs.length && !isFound; j++) {
          if(pairs[j][0].equals(number)) {
            isFound = true;
          }
        }
      }
      if(isFound) {
        try {
          Class.forName("org.eclipse.swt.snippets.Snippet" + number).getMethod("main", new Class[] {String[].class}).invoke(null, new Object[] {args});
        } catch(InvocationTargetException e) {
          e.getCause().printStackTrace();
        } catch(Exception e) {
          e.printStackTrace();
        }
      } else {
        printUsage();
      }
    }
//    System.exit(0);
  }
  
  public static void printUsage() {
    System.out.println("Test by Christopher Deckers: chrriis@brainlex.com (chrriis.brainlex.com/swtswing)");
    System.out.println("Usage: provide a number (as a parameter) identifying a snippet:");
    for(int i=0; i<definitions.length; i++) {
      Object[] section = (Object[])definitions[i];
      if(((String[][])section[1]).length > 0) {
        System.out.println("* " + section[0]);
        String[][] pairs = (String[][])section[1];
        for(int j=0; j<pairs.length; j++) {
          System.out.println("  " + pairs[j][0] + ": " + pairs[j][1]);
        }
      }
    }
  }

} 
