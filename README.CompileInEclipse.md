## Compile the code in Eclipse

This guide has been tested on Windows 64-bit (both 7 and 8). For other operating systems it should work in a very similar way.

1. Make sure you have Java 8 64-bit installed

  http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

2. Download the latest version of Eclipse IDE for Java Developers (64-bit)

  https://eclipse.org/downloads/

3. Unzip the Eclipse file anywhere you like on your hard drive and start "eclipse.exe".

4. Inside Eclipse, close the Welcome screen and choose File->Import and then expand the "Git" option and choose "Projects from Git". On the next screen select "Clone URI" and then in the URI field enter "https://github.com/neura-sx/BitSharesHash.git". All other fields leave as they are and then navigate through the wizard accepting the default entries. You should end up having two projects (BitSharesHashAPI & BitSharesHashGUI) cloned to you workspace.

5. The projects should start compiling automatically (when they compile for the first time an internet connection is needed for Maven to be able to download dependencies).

6. When the compilation is successful navigate to sx.neura.bts.gui.Main in the BitSharesHashGUI project, right-click it and run it as a Java application. It can take a minute or two before the log-in screen shows up.

7. If you already have a BitShares wallet on your computer the Java GUI will open your existing wallet. Otherwise it will create a new wallet with the passphrase "southpark". 
