## Compile the code in Eclipse

This guide has been tested on Windows 64-bit (both 7 and 8). For other operating systems it should work in a very similar way, except step 6 - for the time being for operating systems other than Windows you will need to have the official BitShares client installed before you can use the Java GUI.

1. Make sure you have Java 8 64-bit installed

  http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

2. Download the latest version of Eclipse IDE for Java Developers (64-bit)

  https://eclipse.org/downloads/

3. Unzip the Eclipse file anywhere you like on your hard drive and start "eclipse.exe".

4. Inside Eclipse, close the Welcome screen and choose File->Import and then expand the "Git" option and choose "Projects from Git". On the next screen select "Clone URI" and then in the URI field enter "https://github.com/neura-sx/BitSharesHash.git". All other fields leave as they are and then navigate through the wizard accepting the default entries. You should end up having two projects (BitSharesHashAPI & BitSharesHashGUI) cloned to you workspace.

5. The projects should start compiling automatically (when they compile for the first time an internet connection is needed for Maven to be able to download dependencies).

6. For Windows: if you have the official BitShares client already installed - skip this step. Otherwise make sure you have Visual C++ Redistributable 2013 installed

  http://www.microsoft.com/en-us/download/details.aspx?id=40784. 
  
  Bear in mind that the Java GUI will attempt to start an exe file (bitshares_client.exe) included in the source code which is the official CLI client - if you do not trust this file feel free to replace it with a your own copy downloaded from http://bitshares.org or remove this file and start your own BitShares CLI manually as in case for Mac and Linux.

  For Mac and Linux: you need have the official BitShares client installed and then start the BitShares CLI manually with    the following settings: --server --rpcuser test --rpcpassword test --httpport 9989

7. When the compilation is successful navigate to sx.neura.bts.gui.Main in the BitSharesHashGUI project, right-click it and run it as a Java application. It may take a minute or two before the log-in screen shows up.

8. If you already have a BitShares wallet on your computer the Java GUI will open your existing wallet. Otherwise it will create a new wallet with the passphrase "southpark". 
