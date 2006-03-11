package chrriis.swtswing;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.widgets.Display;

public class Test {

  static Object[] definitions = new Object[] {
    new Object[] {"Button", new String[][] {
      {"108", "set the default button"},
    }}, new Object[] {"Canvas", new String[][] {
      {"48", "scroll an image (flicker free, no double buffering)"},
//      {"21", "implement tab traversal (behave like a tab group)"},
    }}, new Object[] {"Caret", new String[][] {
      {"74", "create a caret"},
//      {"43", "create a caret (using an image)"},
    }}, new Object[] {"Clipboard", new String[][] {
//      {"94", "copy and paste data with the clipboard"},
    }}, new Object[] {"Combo", new String[][] {
      {"26", "create a combo box (non-editable)"},
    }}, new Object[] {"Composite", new String[][] {
      {"9", "scroll a child control automatically"},
//      {"75", "set the tab traversal order of children"},
//      {"98", "create and dispose children of a composite"},
      {"115", "force radio behavior on two different composites"},
      {"46", "intercept mouse events (drag a button with the mouse)"},
    }}, new Object[] {"Control", new String[][] {
      {"14", "detect mouse enter, exit and hover events"},
      {"25", "print key state, code and character"},
      {"62", "print mouse state and button (down, move, up)"},
    }}, new Object[] {"CoolBar", new String[][] {
//      {"20", "create a cool bar"},
    }}, new Object[] {"CTabFolder", new String[][] {
//      {"82", "prevent an item from closing"},
    }}, new Object[] {"Cursor", new String[][] {
      {"44", "set the hand cursor into a control"},
//      {"92", "create a cursor from a source and a mask"},
//      {"119", "create a color cursor from a source and a mask"}, <-- does not work in original!!
      {"118", "create a color cursor from an image file"},
    }}, new Object[] {"DirectoryDialog", new String[][] {
//      {"33", "prompt for a directory"},
    }}, new Object[] {"Display", new String[][] {
      {"60", "create two one shot timers (5000 ms, 2000 ms)"},
      {"16", "create one repeating timer (every 500 ms)"},
      {"68", "stop a repeating timer when a button is pressed"},
      {"42", "get the bounds and client area of a display"},
    }}, new Object[] {"Drag and Drop", new String[][] {
//      {"78", "drag text between two labels"},
//      {"91", "drag leaf items in a tree"},
//      {"79", "define my own data transfer type"}, <-- Not in the snippet package. No main class...
//      {"84", "define a default operation (in this example, Copy)"},
    }}, new Object[] {"FileDialog", new String[][] {
      {"72", "prompt for a file name (to save)"},
    }}, new Object[] {"Font", new String[][] {
      {"100", "create a large font for use by a text widget"},
    }}, new Object[] {"FormLayout", new String[][] {
      {"65", "create a simple dialog using form layout"},
      {"69", "display a label and single line text using a form layout"},
      {"71", "create a simple OK/CANCEL dialog using form layout"},
    }}, new Object[] {"GC", new String[][] {
      {"13", "draw a thick line"},
      {"93", "measure a string"},
      {"66", "implement a simple scribble program"},
      {"70", "create an icon (in memory)"},
//      {"95", "capture a widget image with a GC"},
    }}, new Object[] {"GridLayout", new String[][] {
      {"6", "insert widgets into a grid layout"},
    }}, new Object[] {"Image", new String[][] {
      {"112", "display an image in a group"},
    }}, new Object[] {"Label", new String[][] {
      {"34", "create a label (with an image)"},
      {"37", "create a label (a separator)"},
    }}, new Object[] {"List", new String[][] {
      {"59", "print selected items in a list"},
    }}, new Object[] {"Menu", new String[][] {
      {"29", "create a bar and pull down menu (accelerators, mnemonics)"},
      {"40", "create a popup menu (set in multiple controls)"},
      {"73", "enable menu items dynamically (when menu shown)"},
      {"97", "fill a menu dynamically (when menu shown)"},
      {"89", "add radio items to a menu"},
    }}, new Object[] {"Program", new String[][] {
//      Is it platform specific??
    }}, new Object[] {"ProgressBar", new String[][] {
      {"57", "update a progress bar (from the UI thread)"},
      {"56", "update a progress bar (another thread)"},
    }}, new Object[] {"Sash", new String[][] {
      {"54", "create a sash (allow it to be moved)"},
      {"107", "implement a simple splitter (with a 20 pixel limit)"},
    }}, new Object[] {"SashForm", new String[][] {
      {"109", "create a sash form with three children"},
    }}, new Object[] {"Scale", new String[][] {
      {"45", "create a scale (maximum 40, page increment 5)"},
    }}, new Object[] {"ScrolledComposite", new String[][] {
      {"5", "scroll a control in a scrolled composite"},
    }}, new Object[] {"Shell", new String[][] {
      {"50", "create a dialog shell"},
      {"63", "create a dialog shell (prompt for a value)"},
      {"104", "create a splash screen"},
      {"99", "prevent a shell from closing (prompt the user)"},
//      {"4", "prevent escape from closing a dialog"},
      {"27", "open a shell minimized (iconified)"},
      {"28", "open a shell maximized (full screen)"},
    }}, new Object[] {"Slider", new String[][] {
      {"17", "print scroll event details"},
    }}, new Object[] {"TabFolder", new String[][] {
      {"76", "create a tab folder (six pages)"},
    }}, new Object[] {"Table", new String[][] {
      {"35", "create a table (no columns, no headers)"},
      {"38", "create a table (columns, headers, lines)"},
      {"7", "create a table (lazy)"},
//      {"113", "detect a selection or check event in a table (SWT.CHECK)"},
//      {"3", "find a table cell from mouse down (SWT.FULL_SELECTION)"},
//      {"110", "find a table cell from mouse down (works for any table style)"},
      {"101", "insert a table item (at an index)"},
//      {"106", "insert a table column (at an index)"},
//      {"64", "print selected items in a table"},
//      {"53", "remove selected items"},
//      {"77", "resize columns as table resizes"},
//      {"51", "scroll a table (set the top index)"},
//      {"52", "select an index (select and scroll)"},
//      {"2", "sort a table by column"},
      {"103", "update a table's contents dynamically"},
    }}, new Object[] {"TableCursor", new String[][] {
//      {"96", "navigate a table cells with arrow keys"},
    }}, new Object[] {"TableEditor", new String[][] {
//      {"88", "edit a cell in a table (in place)"},
    }}, new Object[] {"Text", new String[][] {
      {"11", "set the selection (i-beam)"},
      {"12", "set the selection (start, end)"},
      {"19", "verify input (only allow digits)"},
      {"22", "select all the text in the control"},
      {"24", "detect CR in a text control (default selelection)"},
      {"55", "resize a text control (show about 10 characters)"},
      {"116", "stop CR from going to the default button"},
      {"117", "add a select all menu item to the control"},
    }}, new Object[] {"ToolBar", new String[][] {
      {"18", "create a tool bar (text)"},
      {"36", "create a flat tool bar (images)"},
      {"47", "create tool bar (normal, hot and disabled images)"},
      {"49", "create tool bar (wrap on resize)"},
      {"58", "place a combo box in a tool bar"},
      {"67", "place a drop down menu in a tool bar"},
    }}, new Object[] {"Tool Tips", new String[][] {
      {"41", "create tool tips for a tab folder, tool bar and control"},
    }}, new Object[] {"Tracker", new String[][] {
//      {"23", "create a tracker (drag on mouse down)"},
//      {"31", "create a tracker (drag when torn off)"},
    }}, new Object[] {"Tree", new String[][] {
//      {"15", "create a tree"},
//      {"8", "create a tree (lazy)"},
      {"114", "detect a selection or check event in a tree (SWT.CHECK)"},
      {"102", "insert a tree item (at an index)"},
      {"61", "print selected items in a tree"},
//      {"80", "limit selection to items that match a pattern"},
//      {"90", "detect mouse down in a tree item"},
    }}, new Object[] {"TreeEditor", new String[][] {
//      {"111", "edit the text of a tree item (in place, fancy)"},
    }},
  };

  static String snippetNumber = "72";

  static boolean isRealDispatch = true;

  public static void main(final String[] args) {
    if(isRealDispatch) {
      Display.swtExec(new Runnable() {
        public void run() {
          snippetMain(args);
        }
      });
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
    if(number == null && args.length != 1) {
      printUsage();
    } else {
      if(args.length == 1) {
        number = args[0];
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
          Class.forName("org.eclipse.swt.snippets.Snippet" + number).getMethod("main", new Class[] {String[].class}).invoke(null, new Object[] {null});
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
