SWTSwing
========

![image](https://github.com/user-attachments/assets/4f46ed16-c2ee-488b-b65b-87d90ae5d34b)

SWTSwing is a port of the [SWT toolkit](https://www.eclipse.org/swt/) to run on Swing.

Here is an example of Eclipse, running on Swing, with the excellent [FlatLaf](https://www.formdev.com/flatlaf/) look and feel:
![image](https://github.com/user-attachments/assets/0fd358b0-1163-4f4e-a566-94e49952a1d4)

Is it bug-free? No. Will it solve your problem? Probably not. But it is cool.

Running Eclipse on Swing 
========================

- Download [eclipse-SDK-4.20-win32-x86_64.zip](https://archive.eclipse.org/eclipse/downloads/drops4/R-4.20-202106111600/download.php?dropFile=eclipse-SDK-4.20-win32-x86_64.zip)
- Go to the "<eclipse-SDK-4.20>\plugins" folder, move these 2 plugins to the parent folder:
  - org.eclipse.e4.ui.swt.win32_1.1.0.v20201119-1132.jar
  - org.eclipse.swt.win32.win32.x86_64_3.116.100.v20210602-2209.jar
- Copy SWTSwing.jar to <eclipse-SDK-4.20>.
- Run the following command:
<pre>jdk_11\java.exe -Dosgi.parentClassloader=app -cp SWTSwing.jar;plugins\org.eclipse.equinox.launcher_1.6.200.v20210416-2027.jar org.eclipse.swt.widgets.Display org.eclipse.core.launcher.Main</pre>

- If you want to run Eclipse with the FlatLaf look and feel, place the flatlaf.jar in <eclipse-SDK-4.20> and run:
<pre>jdk_11\java.exe -Dswt.swing.laf=com.formdev.flatlaf.FlatLightLaf -Dosgi.parentClassloader=app -cp SWTSwing.jar;flatlaf.jar;plugins\org.eclipse.equinox.launcher_1.6.200.v20210416-2027.jar org.eclipse.swt.widgets.Display org.eclipse.core.launcher.Main</pre>

Contributing 
============

You're encouraged to contribute to SWTSwing. Fork the code from [github.com/Chrriis/SWTSwing](https://github.com/Chrriis/SWTSwing) and submit pull requests.

License
=======

This library is provided under the Eclipse Public License, version 2.0.
