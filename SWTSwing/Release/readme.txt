The SWTSwing project - SWT running on Swing
http://swtswing.sourceforge.net
Christopher Deckers (chrriis@nextencia.com)
Licence terms: EPL 1.0 (see licence.html)


1. What is SWTSwing?

SWTSwing is an SWT port to Swing.
SWT is a graphical library for Java that uses the native widgets offered by the
operating system, at the cost of a thin native layer that is not part of the
Java Runtime Environment.
If you need more information about SWT, go to http://www.eclipse.org/swt.

A port to Swing has many benefits, including true portability (no native
libraries), can act as a bridge between Swing and SWT components, support for
look and feel.

SWTSwing requires the latest official version of the Java Runtime Environment in
order to provide a maximum coverage of SWT features.
The current version is Java 5.0.


2. How to use it?

Remove the SWT jars and native libraries from your application's class path, and
add SWTSwing.jar. A usual way to proceed is to rename "SWTSwing.jar" to
"swt.jar" and replace the original "swt.jar" with the new one.

If everything is OK, your application runs using Swing's widgets.

If you have the possibility, let SWTSwing run your UI code in its UI thread. To
do this, place your UI code in a Runnable object, and invoke
Display.swtExec(runnable)
Alternatively, if your application uses the main thread as the UI thread, you
can use the special Display.main(String[]) method, with the first parameter
being the main class of your application, followed by the normal parameters.
If this is isn't possible, the library works with decreased performance.

You can set the Look And Feel of your application by setting the system property
"swt.swing.laf" to the name of the Look And Feel class. For example, to use the
metal Look And Feel, the system property should be set to
"javax.swing.plaf.metal.MetalLookAndFeel".

If you want to try Eclipse with SWTSwing, the easiest way is to place
SWTSwing.jar in the eclipse folder (let's assume it is C:\eclipse) and run the
following command:
java -cp SWTSwing.jar;startup.jar -Dosgi.install.area=C:\eclipse 
-Dosgi.parentClassloader=app -Dswt.swing.debug=true 
org.eclipse.swt.widgets.Display org.eclipse.core.launcher.Main


3. Any demo?

The Snippet launcher is there to demonstrate the current support of the
snippets: simply launch SWTSwingSnippetLauncher.jar.


4. What is the development status?

Many snippets, some small code samples provided by the SWT team to test SWT
ports) are running flawlessly.

Some more complex examples, like the ones provided by the SWT team, and some
simple applications do work on SWTSwing too, and the goal is now to make more of
these real world test cases to be supported.

The Eclipse Java SDK is able to run with SWTSwing. It has a few problems still,
but a lot is working already.

For more detailed information about the current implementation status, visit
SWTSwing's website.


5. Sources?

The sources are part of the distribution, both for SWTSwing and the
SnippetLauncher.
Some methods do not compile which indicate the API is not complete. Eclipse
allows the compilation of code that contains errors: such code runs as long as
the affected methods are not used by the application using SWTSwing.


6. How to contribute?

If you are interested in helping the project, simply send me an e-mail.
