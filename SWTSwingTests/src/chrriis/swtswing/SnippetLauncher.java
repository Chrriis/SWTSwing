/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package chrriis.swtswing;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.internal.swing.Utils;
import org.eclipse.swt.widgets.Display;

public class SnippetLauncher {

	protected static class SnippetCategory {
		
		protected String name;
		protected Snippet[] snippets;
		
		public SnippetCategory(String name, Snippet[] snippets) {
			this.name = name;
			this.snippets = snippets;
		}
		
		public Snippet[] getSnippets() {
			return snippets;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return name + " (" + snippets.length + ")";
		}

	}

	protected static class Snippet {
		
		protected static final int UNSUPPORTED = -1;
		protected static final int WORKING = 0;
		protected static final int NOT_WORKING = 1;
		protected static final int PARTIALLY_WORKING = 2;
		
		protected int number;
		protected String title;
		protected int status;
		protected String comment;
		
		public Snippet(int number, String title) {
			this(number, title, null);
		}

		public Snippet(int number, String title, String comment) {
			this(number, title, WORKING, comment);
		}

		public Snippet(int number, String title, int status, String comment) {
			this.number = number;
			this.title = title;
			this.status = status;
			this.comment = comment;
		}
		
		public int getNumber() {
			return number;
		}
		
		public int getStatus() {
			return status;
		}
		
		public String getTitle() {
			return title;
		}
		
		public String toString() {
			return number + ": " + title;
		}
		
	}

	protected static class NotWorkingSnippet extends Snippet {
		
		public NotWorkingSnippet(int number, String title) {
			this(number, title, null);
		}

		public NotWorkingSnippet(int number, String title, String comment) {
			super(number, title, NOT_WORKING, comment);
		}
		
	}

	protected static class PartiallyWorkingSnippet extends Snippet {
		
		public PartiallyWorkingSnippet(int number, String title) {
			this(number, title, null);
		}
		
		public PartiallyWorkingSnippet(int number, String title, String comment) {
			super(number, title, PARTIALLY_WORKING, comment);
		}
		
	}
	
	protected static class UnsupportedSnippet extends Snippet {
		
		public UnsupportedSnippet(int number, String title) {
			this(number, title, null);
		}
		
		public UnsupportedSnippet(int number, String title, String comment) {
			super(number, title, UNSUPPORTED, comment);
		}
		
	}
	
	protected static SnippetCategory[] snippetCategories = new SnippetCategory[] {
		new SnippetCategory("Hello World", new Snippet[] {
			new Snippet(1, "Hello World"),
		}), new SnippetCategory("Accessibility", new Snippet[] {
/**/      new NotWorkingSnippet(162, "using an accessible listener to provide state information"),
/**/      new NotWorkingSnippet(164, "provide text that will be spoken for an image button"),
/**/      new NotWorkingSnippet(291, "give accessible names to a tree and its tree items"),
/**/      new NotWorkingSnippet(334, "respond to text-based questions from an AT"),
/**/      new NotWorkingSnippet(340, "tell a screen reader about updates to a non-focused descriptive area"),
/**/      new NotWorkingSnippet(350, "use accessible relations to provide additional information to an AT"),
/**/      new NotWorkingSnippet(357, "provide a way for an AT to set text attributes in a StyledText"),
/**/      new NotWorkingSnippet(363, "declare a message area to be a \"live region\""),
		}), new SnippetCategory("Browser", new Snippet[] {
/**/      new UnsupportedSnippet(148, "check if the browser is available or not"),
/**/      new UnsupportedSnippet(128, "bring up a browser (single window)"),
/**/      new UnsupportedSnippet(173, "bring up a browser with pop-up blocker"),
/**/      new UnsupportedSnippet(270, "bring up a browser with pop-up window support"),
			new Snippet(136, "render HTML from memory"),
			new Snippet(137, "render HTML that includes relative links from memory"),
/**/      new UnsupportedSnippet(327, "render HTML from memory in response to a link click"),
/**/      new UnsupportedSnippet(303, "listen for DOM mousedown events with JavaScript (SWT 3.4 and earlier)"),
/**/      new UnsupportedSnippet(362, "listen for DOM mousedown events with JavaScript (SWT 3.5 and newer)"),
/**/      new UnsupportedSnippet(161, "modify DOM (execute JavaScript)"),
/**/      new UnsupportedSnippet(160, "query DOM node value (SWT 3.4 and earlier)"),
/**/      new UnsupportedSnippet(308, "query DOM node value (SWT 3.5 and newer)"),
/**/      new UnsupportedSnippet(159, "modify HTML title tag"),
/**/      new UnsupportedSnippet(317, "provide credentials for a basic authentication challenge"),
/**/      new UnsupportedSnippet(307, "call Java from JavaScript"),
/**/      new UnsupportedSnippet(326, "close a Browser such that it can be cancelled by an onbeforeunload handler"),
/**/      new UnsupportedSnippet(330, "send custom headers and post data with HTTP requests"),
/**/      new UnsupportedSnippet(351, "WebKit in a browser"),
/**/      new UnsupportedSnippet(372, "Resize the shell to the fit the html content after it's loaded"),
/**/      new UnsupportedSnippet(260, "Mozilla in a browser"),
/**/      new UnsupportedSnippet(267, "toggle a Mozilla Browser between Design mode and View mode"),
/**/      new UnsupportedSnippet(277, "implement a custom download handler for a Mozilla Browser"),
/**/      new UnsupportedSnippet(306, "show an HTML element-specific context menu in a Mozilla Browser"),
/**/      new UnsupportedSnippet(321, "examine request and response headers in a Mozilla Browser"),
/**/      new UnsupportedSnippet(323, "view DOM tree and edit node values in a Mozilla Browser"),
/**/      new UnsupportedSnippet(329, "zoom HTML page content in a Mozilla Browser"),
/**/      new UnsupportedSnippet(161, "modify DOM (execute javascript)"),
/**/      new UnsupportedSnippet(160, "query DOM node value"),
/**/      new UnsupportedSnippet(159, "modify HTML title tag"),
		}), new SnippetCategory("BusyIndicator", new Snippet[] {
			new Snippet(130, "display busy cursor during long running task"),
		}), new SnippetCategory("Button", new Snippet[] {
			new Snippet(108, "set the default button"),
			new Snippet(169, "make a toggle button have radio behavior"),
			new Snippet(224, "implement radio behavior for setSelection()"),
/**/      new UnsupportedSnippet(206, "a button with text and image"),
/**/      new UnsupportedSnippet(293, "create a tri-state button"),
/**/      new UnsupportedSnippet(315, "create a tri-state button (toggle three states)"),
/**/      new UnsupportedSnippet(294, "create a non-rectangular button"),
/**/      new UnsupportedSnippet(345, "create buttons with wrapped titles"),
		}), new SnippetCategory("Canvas", new Snippet[] {
/**/      new UnsupportedSnippet(245, "paint a circle in a canvas"),
			new Snippet(48, "scroll an image (flicker free, no double buffering)"),
			new Snippet(21, "implement tab traversal (behave like a tab group)"),
/**/      new UnsupportedSnippet(275, "update a portion of a Canvas frequently"),
/**/      new UnsupportedSnippet(290, "ignore 2nd mouse up event after double-click"),
		}), new SnippetCategory("Caret", new Snippet[] {
			new Snippet(74, "create a caret"),
/**/      new NotWorkingSnippet(43, "create a caret (using an image)"),
		}), new SnippetCategory("CCombo", new Snippet[] {
/**/      new NotWorkingSnippet(39, "create a ccombo"),
		}), new SnippetCategory("Clipboard", new Snippet[] {
			new Snippet(94, "copy and paste data with the clipboard"),
/**/      new UnsupportedSnippet(282, "copy and paste image with the clipboard using ImageTransfer"),
			new Snippet(122, "enable/disable menu depending on clipboard content availability"),
		}), new SnippetCategory("Color and RGB", new Snippet[] {
			new Snippet(208, "adjust hue, brightness and saturation of a color"),
		}), new SnippetCategory("Combo", new Snippet[] {
			new Snippet(26, "create a combo box (non-editable)"),
			new Snippet(24, "detect return in a combo box (default selection)"),
			new Snippet(147, "prevent CR from going to the default button"),
/**/      new NotWorkingSnippet(269, "set the caret position within a combo's text"),
/**/      new NotWorkingSnippet(289, "add a new number item to a combo box"),
		}), new SnippetCategory("Composite", new Snippet[] {
			new Snippet(9, "scroll a child control automatically"),
/**/      new PartiallyWorkingSnippet(75, "set the tab traversal order of children"),
			new Snippet(98, "create and dispose children of a composite"),
			new Snippet(115, "force radio behavior on two different composites"),
			new Snippet(46, "intercept mouse events (drag a button with the mouse)"),
/**/      new NotWorkingSnippet(237, "inherit a background color or image"),
		}), new SnippetCategory("Control", new Snippet[] {
			new Snippet(14, "detect mouse enter, exit and hover events"),
			new Snippet(127, "prevent Tab from traversing out of a control"),
/**/      new NotWorkingSnippet(247, "allow a multi-line text to process the default button"),
/**/      new NotWorkingSnippet(338, "perform custom traversals"),
			new Snippet(25, "print key state, code and character"),
			new Snippet(62, "print mouse state and button (down, move, up)"),
/**/      new NotWorkingSnippet(324, "print mouse events (all)"),
			new Snippet(214, "set a background image"),
/**/      new NotWorkingSnippet(365, "set transparent background"),
		}), new SnippetCategory("CoolBar", new Snippet[] {
/**/      new NotWorkingSnippet(20, "create a cool bar"),
			new Snippet(150, "create a cool bar (relayout when resized)"),
/**/      new NotWorkingSnippet(140, "drop-down a chevron menu containing hidden tool items"),
		}), new SnippetCategory("CTabFolder, CTabItem", new Snippet[] {
			new Snippet(82, "prevent an item from closing"),
			new Snippet(165, "min and max buttons, close button and image only on selected tab"),
/**/      new NotWorkingSnippet(371, "demonstration of a multi line CTabFolder"),
		}), new SnippetCategory("Cursor", new Snippet[] {
			new Snippet(44, "set the hand cursor into a control"),
/**/      new NotWorkingSnippet(92, "create a cursor from a source and a mask"),
/**/      new PartiallyWorkingSnippet(119, "create a color cursor from a source and a mask"),
			new Snippet(118, "create a color cursor from an image file"),
/**/      new PartiallyWorkingSnippet(242, "hide the Cursor over a control"),
		}), new SnippetCategory("DateTime", new Snippet[] {
/**/      new NotWorkingSnippet(250, "create a DateTime calendar and a DateTime time"),
/**/      new NotWorkingSnippet(251, "create a DateTime calendar and a DateTime time in a dialog"),
/**/      new NotWorkingSnippet(370, "DateTime with localization support"),
		}), new SnippetCategory("DirectoryDialog", new Snippet[] {
			new Snippet(33, "prompt for a directory"),
		}), new SnippetCategory("Display", new Snippet[] {
			new Snippet(60, "create two one shot timers (5000 ms, 2000 ms)"),
			new Snippet(16, "create one repeating timer (every 500 ms)"),
			new Snippet(68, "stop a repeating timer when a button is pressed"),
			new Snippet(42, "get the bounds and client area of a display"),
			new Snippet(7, "fill a table from a background thread (sync exec)"),
			new Snippet(142, "post mouse events (UI testing tools only)"),
			new Snippet(146, "post key events (UI testing tools only)"),
/**/      new NotWorkingSnippet(304, "post key events to simulate CTRL+END (UI testing tools only)"),
/**/      new NotWorkingSnippet(268, "post mouse wheel events (UI testing tools only)"),
/**/      new NotWorkingSnippet(235, "detect a system settings change"),
/**/      new NotWorkingSnippet(276, "map from control-relative to display-relative coordinates"),
/**/      new NotWorkingSnippet(347, "use application menu bar, if platform supports it"),
/**/      new NotWorkingSnippet(348, "more complex example using application menu bar to provide better platform behavior"),
		}), new SnippetCategory("Drag and Drop", new Snippet[] {
			new Snippet(78, "drag text between two labels"),
/**/      new NotWorkingSnippet(91, "drag leaf items in a tree"),
			new Snippet(79, "define my own data transfer type"),
/**/      new NotWorkingSnippet(171, "define a second data transfer type that subclasses the first"),
/**/      new PartiallyWorkingSnippet(84, "define a default operation (in this example, Copy)"),
/**/      new UnsupportedSnippet(83, "determine data types available (win32 only)"),
/**/      new NotWorkingSnippet(185, "make a dropped data type depend on a target item in table"),
/**/      new NotWorkingSnippet(210, "drag text between two StyledText widgets"),
/**/      new NotWorkingSnippet(257, "drag text within a StyledText widget (with drag image)"),
/**/      new NotWorkingSnippet(259, "detect drag in a custom control"),
/**/      new NotWorkingSnippet(284, "drag and drop a URL"),
/**/      new NotWorkingSnippet(300, "drag and drop text between SWT and Swing"),
/**/      new NotWorkingSnippet(319, "drag and drop an object with a custom data transfer type from SWT to AWT/Swing"),
		}), new SnippetCategory("ExpandBar", new Snippet[] {
			new Snippet(223, "create a expand bar"),
/**/      new NotWorkingSnippet(343, "change an ExpandItem's height in response to a change in a contained Control"),
		}), new SnippetCategory("FileDialog", new Snippet[] {
			new Snippet(72, "prompt for a file name (to save)"),
		}), new SnippetCategory("Font", new Snippet[] {
			new Snippet(100, "create a large font for use by a text widget"),
		}), new SnippetCategory("FormLayout", new Snippet[] {
			new Snippet(65, "create a simple dialog using form layout"),
			new Snippet(69, "center a label and single line text using a form layout"),
			new Snippet(71, "create a simple OK/CANCEL dialog using form layout"),
/**/      new NotWorkingSnippet(295, "create a simple text prompt dialog using form layout"),
/**/      new NotWorkingSnippet(313, "use a form layout to hide/show a composite"),
		}), new SnippetCategory("GC", new Snippet[] {
			new Snippet(70, "create an icon (in memory)"),
			new Snippet(95, "capture a widget image with a GC"),
/**/      new NotWorkingSnippet(280, "draw a multi-gradient (without advanced graphics)"),
/**/      new NotWorkingSnippet(281, "draw a multi-gradient"),
			new Snippet(13, "draw a thick line"),
			new Snippet(168, "draw lines with different cap and join styles"),
			new Snippet(10, "drawing with transformations, paths and alpha blending"),
			new Snippet(66, "implement a simple scribble program"),
			new Snippet(93, "measure a string"),
			new Snippet(207, "reflect, shear and rotate images using matrix transformations"),
			new Snippet(215, "take a screen shot with a GC"),
/**/      new NotWorkingSnippet(252, "draw 2 polylines with different line attributes"),
		}), new SnippetCategory("Gesture, Touch support", new Snippet[] {
/**/      new NotWorkingSnippet(352, "create a shell and listen for TouchEvents"),
/**/      new NotWorkingSnippet(353, "Gesture example"),
		}), new SnippetCategory("GridLayout", new Snippet[] {
			new Snippet(172, "align widgets in rows and columns"),
			new Snippet(6, "insert widgets into a grid layout"),
/**/      new NotWorkingSnippet(266, "span columns and center within columns"),
			new Snippet(175, "exclude an invisible widget from a grid layout"),
/**/      new NotWorkingSnippet(335, "show a wrappable Label that fills available width without expanding it"),
/**/      new NotWorkingSnippet(342, "grow/shrink a wrappable Text's height to show its content as it changes"),
		}), new SnippetCategory("Image", new Snippet[] {
			new Snippet(112, "display an image in a group"),
/**/      new NotWorkingSnippet(279, "draw a reflection of an image"),
/**/      new NotWorkingSnippet(355, "draw an image scaled to half size and double size"),
/**/      new NotWorkingSnippet(367, "draw an image at various zoom/dpi levels"),
		}), new SnippetCategory("ImageData", new Snippet[] {
			new Snippet(141, "display an animated GIF"),
			new Snippet(139, "rotate and flip an image"),
/**/      new NotWorkingSnippet(194, "write an animated GIF"),
/**/      new NotWorkingSnippet(246, "write a PNG"),
		}), new SnippetCategory("Label", new Snippet[] {
			new Snippet(34, "create a label (with an image)"),
			new Snippet(37, "create a label (a separator)"),
		}), new SnippetCategory("Link", new Snippet[] {
			new Snippet(182, "create a link widget"),
			new Snippet(183, "detect selection events in a link widget"),
		}), new SnippetCategory("List", new Snippet[] {
			new Snippet(59, "print selected items in a list"),
		}), new SnippetCategory("Menu", new Snippet[] {
			new Snippet(29, "create a bar and pull down menu (accelerators, mnemonics) "),
			new Snippet(40, "create a popup menu (set in multiple controls)"),
/**/      new NotWorkingSnippet(238, "create a popup menu with a submenu"),
			new Snippet(89, "create a menu with radio items"),
			new Snippet(73, "enable menu items dynamically (when menu shown)"),
			new Snippet(97, "fill a menu dynamically (when menu shown)"),
			new Snippet(131, "show a popup menu (wait for it to close)"),
			new Snippet(152, "update a status line when an item is armed"),
/**/      new NotWorkingSnippet(286, "use a menu item's armListener to update a status line"),
		}), new SnippetCategory("Monitor", new Snippet[] {
			new Snippet(120, "center a shell on the primary monitor"),
		}), new SnippetCategory("OLE and ActiveX", new Snippet[] {
			new UnsupportedSnippet(123, "get events from IE control"),
			new UnsupportedSnippet(81, "browse the typelibinfo for a program id"), // Need a param, cf swt real SnippetLauncher
			new UnsupportedSnippet(157, "embed Word in an applet"),
			new UnsupportedSnippet(186, "reading and writing to a SAFEARRAY"),
			new UnsupportedSnippet(187, "execute a script function that exists on a web page"),
			new UnsupportedSnippet(199, "listen for Excel AppEvents"),
			new UnsupportedSnippet(261, "open an Excel Sheet"),
			new UnsupportedSnippet(262, "open a Word document"),
			new UnsupportedSnippet(263, "open a PowerPoint slide"),
			new UnsupportedSnippet(264, "open a Media Player"),
			new UnsupportedSnippet(265, "open a Windows Media Player"),
			new UnsupportedSnippet(305, "show selected cell position in Excel"),
		}), new SnippetCategory("OpenGL", new Snippet[] {
			new UnsupportedSnippet(195, "draw a rotating torus using the LWJGL OpenGL binding"),
			new UnsupportedSnippet(209, "draw a rotating torus using the JOGL OpenGL binding"),
			new UnsupportedSnippet(174, "draw a rectangle using the org.eclipse.opengl OpenGL binding"),
			new UnsupportedSnippet(341, "capture a LWJGL drawing to an SWT Image"),
		}), new SnippetCategory("Path, PathItem", new Snippet[] {
			new Snippet(198, "Create a path from some text"),
/**/      new NotWorkingSnippet(285, "create a circular shell from a path"),
/**/      new NotWorkingSnippet(200, "fill a shape with a predefined pattern"),
		}), new SnippetCategory("Printing", new Snippet[] {
			new Snippet(132, "print \"Hello World!\" in black, outlined in red, to default printer"),
			new Snippet(133, "print text to printer, with word wrap and pagination"),
/**/      new NotWorkingSnippet(292, "print a control to the printer"),
/**/      new NotWorkingSnippet(318, "print in landscape mode"),
		}), new SnippetCategory("Program", new Snippet[] {
			new UnsupportedSnippet(32, "find the icon of the program that edits .bmp files"),
			new Snippet(105, "invoke an external batch file"),
			new UnsupportedSnippet(30, "invoke the system text editor on autoexec.bat"),
		}), new SnippetCategory("ProgressBar", new Snippet[] {
			new Snippet(57, "update a progress bar (from the UI thread)"),
			new Snippet(56, "update a progress bar (from another thread)"),
		}), new SnippetCategory("RowLayout", new Snippet[] {
			new Snippet(176, "Lay out widgets in a row"),
			new Snippet(177, "Lay out widgets in a column"),
		}), new SnippetCategory("Sash", new Snippet[] {
			new Snippet(54, "create a sash (allow it to be moved)"),
			new Snippet(107, "implement a simple splitter (with a 20 pixel limit)"),
		}), new SnippetCategory("SashForm", new Snippet[] {
			new Snippet(109, "create a sash form with three children"),
		}), new SnippetCategory("Scale", new Snippet[] {
			new Snippet(45, "create a scale (maximum 40, page increment 5)"),
		}), new SnippetCategory("ScrolledComposite", new Snippet[] {
			new Snippet(5, "scroll a control in a scrolled composite"),
			new Snippet(166, "create a ScrolledComposite with wrapping content"),
			new Snippet(167, "create two ScrolledComposites that scroll in tandem"),
			new Snippet(188, "scroll a widget into view on focus in"),
/**/      new NotWorkingSnippet(296, "use a ScrolledComposite to scroll a Tree vertically"),
/**/      new NotWorkingSnippet(322, "use a ScrolledComposite to scroll a disabled control"),
		}), new SnippetCategory("Shell", new Snippet[] {
			new Snippet(50, "create a dialog shell"),
			new Snippet(63, "create a dialog shell (prompt for a value)"),
/**/      new NotWorkingSnippet(295, "create a dialog shell (prompt for a string)"),
/**/      new NotWorkingSnippet(233, "create a dialog shell and position it"),
			new Snippet(104, "create a splash screen"),
			new Snippet(99, "prevent a shell from closing (prompt the user)"),
/**/      new NotWorkingSnippet(248, "allow escape to close a shell"),
			new Snippet(4, "prevent escape from closing a dialog"),
			new Snippet(27, "open a shell minimized (iconified)"),
			new Snippet(28, "open a shell maximized"),
/**/      new NotWorkingSnippet(134, "create a non-rectangular window"),
/**/      new NotWorkingSnippet(285, "create a circular shell from a path"),
			new Snippet(138, "set icons with different resolutions"),
/**/      new UnsupportedSnippet(180, "emulate transparent shell"),
/**/      new NotWorkingSnippet(219, "create a non-rectangular shell from a transparent image"),
/**/      new NotWorkingSnippet(314, "use shell modified state to prompt for save"),
		}), new SnippetCategory("Slider", new Snippet[] {
			new Snippet(17, "print scroll event details"),
		}), new SnippetCategory("Spinner", new Snippet[] {
			new Snippet(184, "create and initialize a spinner widget"),
			new Snippet(190, "use floats in a spinner widget"),
/**/      new NotWorkingSnippet(310, "validate input in a spinner widget"),
		}), new SnippetCategory("StackLayout", new Snippet[] {
/**/      new NotWorkingSnippet(249, "use a stack layout to switch between composites"),
		}), new SnippetCategory("StyledText", new Snippet[] {
			new Snippet(163, "change font style, foreground and background colors of StyledText"),
/**/      new NotWorkingSnippet(244, "draw a box around text"),
			new Snippet(189, "underline and strike through text"),
			new Snippet(211, "use rise and font with StyleRange"),
			new Snippet(212, "embed images in StyledText"),
/**/      new PartiallyWorkingSnippet(213, "use indent, alignment and justify"),
			new Snippet(217, "embed controls in StyledText"),
/**/      new PartiallyWorkingSnippet(222, "use bulleted lists in StyledText"),
			new Snippet(218, "use gradient background in StyledText"),
/**/      new NotWorkingSnippet(325, "variable tab stops in StyledText"),
/**/      new NotWorkingSnippet(328, "per line variable tab stops in StyledText"),
/**/      new NotWorkingSnippet(331, "different types of indent and combining wrap indent and bulleted lists"),
/**/      new NotWorkingSnippet(332, "using BidiSegmentEvent#segmentsChars to implement custom bidi segments"),
/**/      new NotWorkingSnippet(356, "embed an inline link in StyledText"),
/**/      new NotWorkingSnippet(368, "line spacing provider in StyledText"),
/**/      new NotWorkingSnippet(369, "dynamically customize line spacing provider in StyledText"),
/**/      new NotWorkingSnippet(377, "ligatures support added in StyledText"),
		}), new SnippetCategory("Swing/AWT", new Snippet[] {
			new Snippet(135, "embed Swing/AWT in SWT"),
			new Snippet(154, "embed a JTable in SWT (no flickering)"),
			new Snippet(155, "draw an X using AWT Graphics"),
			new Snippet(156, "convert between SWT Image and AWT BufferedImage"),
/**/      new NotWorkingSnippet(337, "launch SWT from AWT and keep both active"),
/**/      new NotWorkingSnippet(319, "drag and drop an object with a custom data transfer type from SWT to AWT/Swing"),
/**/      new NotWorkingSnippet(361, "use Java2D to modify an image being displayed in an SWT GUI"),
		}), new SnippetCategory("TabFolder, TabItem", new Snippet[] {
			new Snippet(76, "create a tab folder (six pages)"),
		}), new SnippetCategory("Table, TableItem, TableColumn", new Snippet[] {
			new Snippet(151, "add 1000 sorted entries to a virtual table every 500 ms"),
			new Snippet(129, "color cells and rows in table"),
			new Snippet(35, "create a table (no columns, no headers)"),
			new Snippet(38, "create a table (columns, headers, lines)"),
/**/      new NotWorkingSnippet(297, "create a table with column header images"),
			new Snippet(144, "create a table with 1,000,000 items (lazy)"),
			new Snippet(201, "create a table with 1,000,000 items (lazy, page size 64)"),
			new Snippet(7, "create a table (lazy with a thread)"),
/**/      new NotWorkingSnippet(301, "create a table with no scroll bars"),
			new Snippet(113, "detect a selection or check event in a table (SWT.CHECK)"),
			new Snippet(3, "find a table cell from mouse down (SWT.FULL_SELECTION)"),
			new Snippet(110, "find a table cell from mouse down (works for any table style)"),
			new Snippet(101, "insert a table item (at an index)"),
			new Snippet(106, "insert a table column (at an index)"),
			new Snippet(181, "make columns reorderable by dragging"),
/**/      new PartiallyWorkingSnippet(126, "place arbitrary controls in a table"),
			new Snippet(64, "print selected items in a table"),
			new Snippet(53, "remove selected items"),
/**/      new NotWorkingSnippet(77, "resize columns as table resizes"),
			new Snippet(51, "scroll a table (set the top index)"),
			new Snippet(52, "select an index (select and scroll)"),
			new Snippet(2, "sort a table by column"),
			new Snippet(192, "sort a table by column (virtual table, sort indicator)"),
			new Snippet(103, "update table item text"),
/**/      new NotWorkingSnippet(349, "show a table with images of various sizes"),
/**/      new NotWorkingSnippet(230, "draw images on right side of table item"),
/**/      new NotWorkingSnippet(228, "show results as a bar chart in table"),
/**/      new NotWorkingSnippet(229, "draw a custom gradient selection for table"),
/**/      new NotWorkingSnippet(231, "draw multiple lines in a table item"),
/**/      new NotWorkingSnippet(234, "fixed first column horizontal scroll remaining columns"),
/**/      new NotWorkingSnippet(236, "draw different foreground colors for text in a TableItem"),
/**/      new NotWorkingSnippet(239, "make text span multiple columns"),
/**/      new NotWorkingSnippet(253, "compute the number of visible rows in a table"),
/**/      new NotWorkingSnippet(271, "specify custom content dimensions in a table with no columns"),
/**/      new NotWorkingSnippet(272, "specify custom content widths when a column is packed"),
/**/      new NotWorkingSnippet(273, "modify the clipping of custom background paints"),
/**/      new NotWorkingSnippet(311, "show a menu in a table header"),
		}), new SnippetCategory("TableCursor", new Snippet[] {
/**/      new NotWorkingSnippet(96, "navigate a table cells with arrow keys"),
		}), new SnippetCategory("TableEditor", new Snippet[] {
/**/      new PartiallyWorkingSnippet(88, "edit the text of a table item (in place)"),
/**/      new PartiallyWorkingSnippet(124, "edit a cell in a table (in place, fancy)"),
/**/      new PartiallyWorkingSnippet(149, "place a progress bar in a table"),
		}), new SnippetCategory("Text", new Snippet[] {
			new Snippet(117, "add a select all menu item to the control"),
			new Snippet(24, "detect CR in a text control (default selection)"),
/**/      new NotWorkingSnippet(241, "override Tab behavior to traverse out of a text control"),
			new Snippet(116, "prevent CR from going to the default button"),
			new Snippet(121, "prompt for a password (set the echo character)"),
			new Snippet(55, "resize a text control (show about 10 characters)"),
			new Snippet(22, "select all the text in the control"),
			new Snippet(11, "set the selection (i-beam)"),
			new Snippet(12, "set the selection (start, end)"),
			new Snippet(19, "verify input (only allow digits)"),
			new Snippet(179, "verify input in a template (YYYY/MM/DD)"),
			new Snippet(191, "detect when the user scrolls a text control"),
			new Snippet(196, "verify input using a regular expression"),
/**/      new NotWorkingSnippet(243, "type in one text control, output to another"),
/**/      new NotWorkingSnippet(258, "create a search text control"),
/**/      new NotWorkingSnippet(309, "display different styles of the search Text control"),
/**/      new NotWorkingSnippet(320, "implement content assist"),
/**/      new NotWorkingSnippet(346, "set contents of a password field using a char array instead of a String"),
		}), new SnippetCategory("TextLayout, TextStyle", new Snippet[] {
			new Snippet(145, "draw internationalized styled text on a shell"),
			new Snippet(197, "draw dynamically wrapped text on a shell"),
/**/      new PartiallyWorkingSnippet(203, "justify, align and indent text"),
			new Snippet(204, "change the rise of text relative to the baseline"),
			new Snippet(205, "embed images and widgets in text"),
/**/      new NotWorkingSnippet(256, "underline and strike through text"),
		}), new SnippetCategory("ToolBar, ToolItem", new Snippet[] {
			new Snippet(18, "create a tool bar (text)"),
			new Snippet(36, "create a flat tool bar (images)"),
			new Snippet(47, "create tool bar (normal, hot and disabled images)"),
			new Snippet(49, "create tool bar (wrap on resize)"),
			new Snippet(58, "place a combo box in a tool bar"),
			new Snippet(67, "place a drop down menu in a tool bar"),
			new Snippet(153, "update a status line when the pointer enters a ToolItem"),
/**/      new NotWorkingSnippet(288, "create a tool bar containing animated gifs"),
		}), new SnippetCategory("Tool Tips", new Snippet[] {
			new Snippet(41, "create tool tips for a tab folder, tool bar and control"),
			new Snippet(125, "create emulated tool tips for items in a table"),
			new Snippet(225, "create a balloon tooltip for a tray item"),
/**/      new NotWorkingSnippet(278, "show a tool tip iff it's not fully visible"),
			new Snippet(216, "show a tool tip inside a rectangle"),
		}), new SnippetCategory("Tracker", new Snippet[] {
			new Snippet(23, "create a tracker (drag on mouse down)"),
			new Snippet(31, "create a tracker (drag when \"torn off\")"),
		}), new SnippetCategory("Tray, TrayItem", new Snippet[] {
/**/      new PartiallyWorkingSnippet(143, "place a popup menu on the system tray"),
		}), new SnippetCategory("Tree, TreeItem, TreeColumn", new Snippet[] {
			new Snippet(15, "create a tree"),
			new Snippet(8, "create a tree (lazy)"),
			new Snippet(202, "virtual tree - lazy creation of sub nodes"),
/**/      new NotWorkingSnippet(302, "create a tree with no scroll bars"),
			new Snippet(114, "detect a selection or check event in a tree (SWT.CHECK)"),
/**/      new NotWorkingSnippet(274, "implement standard tree check box behavior (SWT.CHECK)"),
			new Snippet(102, "insert a tree item (at an index)"),
			new Snippet(61, "print selected items in a tree"),
/**/      new PartiallyWorkingSnippet(80, "limit selection to items that match a pattern"),
			new Snippet(90, "detect mouse down in a tree item"),
			new Snippet(170, "create a tree with columns"),
			new Snippet(193, "make columns reorderable by dragging"),
/**/      new NotWorkingSnippet(220, "images on right side of TreeItem"),
/**/      new NotWorkingSnippet(232, "show results as a bar chart in tree"),
/**/      new NotWorkingSnippet(226, "draw a custom gradient selection for tree"),
/**/      new NotWorkingSnippet(227, "draw multiple lines in a tree item"),
/**/      new NotWorkingSnippet(221, "scroll tree when mouse at top or bottom"),
/**/      new NotWorkingSnippet(287, "search for a string in a tree (recursively)"),
/**/      new NotWorkingSnippet(254, "compute the number of visible rows in a tree"),
/**/      new NotWorkingSnippet(312, "show a menu in a tree header"),
/**/      new NotWorkingSnippet(358, "determine which TreeItems are visible in a Tree's viewport"),
		}), new SnippetCategory("TreeCursor", new Snippet[] {
/**/      new NotWorkingSnippet(360, "navigate a Tree's cells with the arrow keys"),
		}), new SnippetCategory("TreeEditor", new Snippet[] {
/**/      new NotWorkingSnippet(111, "edit the text of a tree item (in place, fancy)"),
		})
	};

	protected static int snippetNumber = 2;

	protected static boolean isRealDispatch = true;

	public static void main(final String[] args) {
		boolean isRealDispatch = SnippetLauncher.isRealDispatch;
		String realDispatchProperty = System.getProperty("swt.swing.realdispatch");
		if(realDispatchProperty != null) {
			isRealDispatch = "true".equals(realDispatchProperty);
		}
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
		int number = -1;
		boolean isDevelopment = System.getProperty("swt.swing.snippets") != null && snippetNumber != -1;
		if(isDevelopment) {
			number = snippetNumber;
		}
		if(number == -1 && args.length < 1) {
			printUsage();
		} else {
			if(args.length > 0) {
				try {
					number = Integer.parseInt(args[0]);
				} catch(Exception e) {
					printUsage();
					return;
				}
				String[] newArgs = new String[args.length - 1];
				System.arraycopy(args, 1, newArgs, 0, newArgs.length);
				args = newArgs;
			}
			boolean isFound = isDevelopment;
			for(int i=0; i<snippetCategories.length; i++) {
				Snippet[] snippets = snippetCategories[i].getSnippets();
				for(int j=0; j<snippets.length; j++) {
					if(snippets[j].getNumber() == number) {
						isFound = true;
						break;
					}
				}
			}
			if(!isFound) {
				printUsage();
				return;
			}
			try {
				Class.forName("org.eclipse.swt.snippets.Snippet" + number).getMethod("main", new Class[] {String[].class}).invoke(null, new Object[] {args});
			} catch(InvocationTargetException e) {
				Utils.throwUncheckedException(e.getCause());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void printUsage() {
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("SWT snippet launcher, for SWTSwing - http://www.nextencia.net/projects/swtswing");
		System.out.println("by Christopher Deckers (chrriis@nextencia.net)");
		System.out.println("Usage: provide a number as a parameter identifying a snippet from the list.");
		System.out.println("Note: The \"swt.swing.laf\" property sets the L&F");
		System.out.println("      e.g.: -Dswt.swing.laf=javax.swing.plaf.metal.MetalLookAndFeel");
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		for(int i=0; i<snippetCategories.length; i++) {
			SnippetCategory category = snippetCategories[i];
			boolean isFound = false;
			Snippet[] snippets = category.getSnippets();
			for(int j=0; j<snippets.length; j++) {
				Snippet snippet = snippets[j];
				if(snippet.getStatus() == Snippet.WORKING) {
					if(!isFound) {
						System.out.println("* " + category.getName());
						isFound = true;
					}
					System.out.println("  " + snippet.getNumber() + ": " + snippet.getTitle());
				}
			}
		}
		System.out.println("-------------------------------------------------------------------------------");
	}

} 
