# VeraDemo - Blab-a-Gag

## About

Blab-a-Gag is a fairly simple forum type application which allows:
 - users to post a one-liner joke
 - users to follow the jokes of other users or not (heckle or ignore)
 - users to comment on other users messages (heckle)
 
### URLs

`/reset` will reset the data in the database with a load of:
 - users
 - jokes
 - heckles
  
`/feed` shows the jokes/heckles that are relevant to the current user.

`/blabbers` shows a list of all other users and allows the current user to heckle or ignore.

`/profile` allows the current user to modify their profile.

`/login` allows you to log in to your account

`/register` allows you to create a new user account
   
## Configure

Build and installation requires [Maven](https://maven.apache.org), [MySQL](https://www.mysql.com/) and [Tomcat](https://tomcat.apache.org/).

The simplest way to aquire these on MacOS is via [Homebrew](http://brew.sh/). Install Homebrew then:

    brew install maven mysql tomcat

This also requires a Java SE 1.8 installation.

If you are Windows, do NOT use one of the Tomcat for Windows installers if you want to debug in VS Code! Instead, download and unzip one of the binary distributions somewhere on your system. For example, I used the current [Tomcat 9 Core](https://apache.claz.org/tomcat/tomcat-9/v9.0.40/bin/apache-tomcat-9.0.40.zip) version.


### Database

Install [MySQL 8.0 Community](https://dev.mysql.com/downloads/) for your platform. Make sure you use Native (legacy) password authentication! 

Set up a database in MySQL called `blab` with a user of `blab` and password `z2^E6J4$;u;d`.
 
 
## Run

`mvn package` will build the web application and output a war file to `target/verademo.war`

Deploy the resulting war file to Tomcat.

Open `http://localhost:8080/verademo/reset` in your browser and follow the instructions to prep the database

Login with a username/password as defined in `Utils.java`, or navigate to the `http://localhost:8080/verademo/register` endpoint to create a new account.


## Debugging in VS Code

Install the [Tomcat for Java](https://marketplace.visualstudio.com/items?itemName=adashen.vscode-tomcat) extension. 

Set up a server profile based on the Tomcat version you downloaded in **Configure**. Select the `target/verademo.war` you built and select **Debug on Tomcat Server**. The tomcat server should start with the `.war` file loaded and you can debug.

Note that I *think* you should be able to right-click on the server profile in VS Code, select **Customize JVM Options** and then enter values like the following to be able to run the Tomcat server and web application with the IAST Agent attached:

```
# Set some IAST Agent debug properties
-DIASTAGENT_LOGGING_STDERR_ENABLED=true
-DIASTAGENT_LOGGING_STDERR_LEVEL=info
# Run Tomcat with the IAST Agent attached
-agentpath:C:\iast\agent\agent_win64.dll
```

The environment variables appear to be used on startup, but the `-agentpath` property is ignored.

However, I have not been able to get this to work. Instead, I had to create the file `%CATALINA_HOME%\bin\setenv.bat` (or `$CATALINA_HOME%/bin/setenv.sh`), with the following, which seems to work ok:

```
set CATALINA_OPTS=%CATALINA_OPTS% -agentpath:C:\iast\agent\agent_win64.dll
```

Happy IAST'ing!
