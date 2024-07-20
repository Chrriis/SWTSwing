/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package chrriis.swtswing.source;

import java.awt.Font;
import java.io.Reader;

import javax.swing.JEditorPane;
import javax.swing.JViewport;

public class SourcePane extends JEditorPane {

	public SourcePane(Reader reader) throws Exception {
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setEditorKit(RegExJavaTypes.getEditorKit());
		read(new TabFilterReader(reader), null);
		setEditable(false);
	}

	/**
	 * Get the viewport size so that text does not wrap but horizontal scrollbar
	 * appears instead.
	 */
	public boolean getScrollableTracksViewportWidth() {
		if (getParent() instanceof JViewport) {
			return getParent().getWidth() > getUI().getPreferredSize(this).width;
		}
		return false;
	}

}
