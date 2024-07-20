/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

public class Compatibility {


	/**
	 * When a class does not exist on a certain version of Java, it would generate a
	 * NoClassDefFoundError simply when the surrounding class gets loaded. The fix is to
	 * surround the access by an intermediate class. Using this particular ProtectedCode
	 * class is not mandatory but allows a better tracking of such compatibility issues.
	 * Here is an example:<br>
	 * <pre>
	 * if(Compatibility.IS_JAVA_5_OR_GREATER) {
	 *   new Compatibility.ProtectedCode() {{
	 *     // Code accessing classes that only exist in Java 5.0+
	 *   }};
	 * }
	 * </pre>
	 * Note that in the example above, the code is invoked in the constructor of this
	 * intermediate class.
	 */
	public static abstract class ProtectedCode {
	}
	
}
