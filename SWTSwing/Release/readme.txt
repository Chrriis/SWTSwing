The SWTSwing project - SWT running on Swing
https://github.com/Chrriis/SWTSwing
Christopher Deckers (chrriis@nextencia.net)
Licence terms: EPL 2.0 (see licence.txt)


1. What is SWTSwing?

SWTSwing is an implementation of SWT (version 3.2 for now) over Swing.

SWT is a graphical library for Java that uses the native widgets offered by the
operating system, at the cost of a thin native layer that is not part of the
Java Runtime Environment.
If you need more information about SWT, go to http://www.eclipse.org/swt.

A port to Swing has many benefits, including true portability (no native
libraries), can act as a bridge between Swing and SWT components, support for
look and feels.

SWTSwing requires the Java Runtime Environment 1.8 minimum.


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

If you want to try Eclipse Java SDK R4_20_maintenance with SWTSwing, the easiest
way is to place SWTSwing.jar in the eclipse folder (let's assume it is
C:\eclipse), remove org.eclipse.e4.ui.swt.win32_1.1.0.v20201119-1132.jar and
org.eclipse.swt.win32.win32.x86_64_3.116.100.v20210602-2209.jar from the plugins
folder and run the following command: java -cp
SWTSwing.jar;plugins\org.eclipse.equinox.launcher_1.6.200.v20210416-2027.jar
-Dosgi.install.area=C:\eclipse -Dosgi.parentClassloader=app
-Dswt.swing.debug=true org.eclipse.swt.widgets.Display
org.eclipse.core.launcher.Main


3. Any demo?

The Snippet launcher is there to demonstrate the current support of the
snippets: simply launch SWTSwingSnippetLauncher.jar.


4. What is the development status?

Many snippets, small to complex code samples and real applications work on
SWTSwing.

For more detailed information about the current implementation status, visit
SWTSwing's website.


5. Sources?

The sources are part of the distribution, both for SWTSwing and the
SnippetLauncher.

The sources are available on GitHub.


6. How to contribute?

If you are interested in helping the project, simply send me an e-mail. Nice
e-mails in general are always welcome!
