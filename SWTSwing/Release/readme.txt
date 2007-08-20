The SWTSwing project - SWT running on Swing
http://swtswing.sourceforge.net
Christopher Deckers (chrriis@nextencia.net)
Licence terms: EPL 1.0 (see licence.html)


1. What is SWTSwing?

SWTSwing is an implementation of SWT (version 3.2 for now) over Swing.

SWT is a graphical library for Java that uses the native widgets offered by the
operating system, at the cost of a thin native layer that is not part of the
Java Runtime Environment.
If you need more information about SWT, go to http://www.eclipse.org/swt.

A port to Swing has many benefits, including true portability (no native
libraries), can act as a bridge between Swing and SWT components, support for
look and feels.

SWTSwing requires the latest official version of the Java Runtime Environment
(6.0) in order to provide a maximum coverage of the SWT API. It can be used on
on older runtimes (1.4+) with a few less functionalities.


2. How to use it?

Remove the SWT jars and native libraries from your application's class path, and
add SWTSwing.jar. A usual way to proceed is to rename "SWTSwing.jar" to
"swt.jar" and replace the original "swt.jar" (make a backup) with the new one.

For increased performance, let SWTSwing run your UI code in its UI thread. To do
this, place your UI code in a Runnable object, and invoke
org.eclipse.swt.widgets.Display.Display.swtExec(runnable)
Alternatively, if your application uses the main thread as the UI thread, you
can use the special org.eclipse.swt.widgets.Display.main(String[]) method, with
the first parameter being your main class, followed by its parameters.
If this is isn't possible, the library works with decreased performance.

You can set the Look And Feel of your application by setting the system property
"swt.swing.laf" to the name of the Look And Feel class. For example, to use the
metal Look And Feel, the system property should be set to
"javax.swing.plaf.metal.MetalLookAndFeel".

If you want to try Eclipse Java SDK 3.2 with SWTSwing, the easiest way is to
place SWTSwing.jar in the eclipse folder (let's assume it is C:\eclipse) and run
the following command:
java -cp SWTSwing.jar;startup.jar -Dosgi.install.area=C:\eclipse 
-Dosgi.parentClassloader=app -Dswt.swing.debug=true 
org.eclipse.swt.widgets.Display org.eclipse.core.launcher.Main


3. Any demo?

The Snippet launcher is there to demonstrate the current support of the
snippets: simply launch SWTSwingSnippetLauncher.jar.

The SWT control example shows the support of the controls with SWTSwing:
http://swtswing.sf.net/webstart/SWTSwingControlExample.jnlp


4. What is the development status?

Many snippets, small to complex code samples and real applications work on
SWTSwing.

For more detailed information about the current implementation status, visit
SWTSwing's website.


5. Sources?

The sources are part of the distribution, both for SWTSwing and the
SnippetLauncher.

There is of course some access to the various CVS trees, composed of the SWT
standard source tree and the SWTSwing sources. The setup process is documented
on the SWTSwing web site.


6. How to contribute?

If you are interested in helping the project, simply send me an e-mail. Nice
e-mails in general are always welcome!
