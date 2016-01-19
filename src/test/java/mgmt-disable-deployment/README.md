jboss-as-management server disable Application Example
======================================================

What is it?
-----------

This example shows you how you can use the JBoss Application Server Management API to disable an application in a standalone server.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.1.0 or EAP 6.0
 
With the prerequisites out of the way, you're ready to build and deploy.


Run the standalone application
-------------------------

First of all you need to enable the "admin" user from $JBOSS_HOME/standalone/configuration/mgmt-users.properties file, and then start JBoss AS 7.1.0. by running this script
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To run the application, use the following Maven goal:

    mvn test

    $ mvn test
    [INFO] Scanning for projects...
    [INFO]
    [INFO] ------------------------------------------------------------------------
    [INFO] Building JBoss AS Quickstarts: This Example deploys and Application via the Management API 1.0
    [INFO] ------------------------------------------------------------------------
    [INFO]
    .
    .
    .
    [INFO]
    [INFO] >>> exec-maven-plugin:1.2.1:java (default) @ mgmt-deploy-application >>>
    [INFO]
    [INFO] <<< exec-maven-plugin:1.2.1:java (default) @ mgmt-deploy-application <<<
    [INFO]
    [INFO] --- exec-maven-plugin:1.2.1:java (default) @ mgmt-deploy-application ---
    21.02.2012 12:20:13 org.xnio.Xnio <clinit>
    INFO: XNIO Version 3.0.0.CR5
    21.02.2012 12:20:13 org.xnio.nio.NioXnio <clinit>
    INFO: XNIO NIO Implementation Version 3.0.0.CR5
    21.02.2012 12:20:13 org.jboss.remoting3.EndpointImpl <clinit>
    INFO: JBoss Remoting version 3.2.0.CR6
    {"outcome" => "success"}
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 5.290s
    [INFO] Finished at: Tue Feb 21 12:20:15 CET 2012
    [INFO] Final Memory: 11M/81M
    [INFO] ------------------------------------------------------------------------


Now you can access the helloworld application from the JBoss AS7 Quickstarts through your Webbrowser. Open up the
Webbrowser of your choice and point it to http://localhost:8080/jboss-as-helloworld

You can also check the JBoss AS7 Standalone Console

    12:20:13,479 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) JBAS015876: Starting deployment of "jboss-as-helloworld.war"
    12:20:13,943 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.iiop-client:main") which may be changed or removed in future versions without notice.
    12:20:13,943 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.as.ejb3:main") which may be changed or removed in future versions without notice.
    12:20:13,943 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.as.jacorb:main") which may be changed or removed in future versions without notice.
    12:20:13,943 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.as.ee:main") which may be changed or removed in future versions without notice.
    12:20:13,944 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("com.sun.jsf-impl:main") which may be changed or removed in future versions without notice.
    12:20:13,944 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.as.web:main") which may be changed or removed in future versions without notice.
    12:20:13,945 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.interceptor:main") which may be changed or removed in future versions without notice.
    12:20:13,945 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.javassist:main") which may be changed or removed in future versions without notice.
    12:20:13,945 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.weld.core:main") which may be changed or removed in future versions without notice.
    12:20:13,946 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.weld.spi:main") which may be changed or removed in future versions without notice.
    12:20:13,946 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.as.weld:main") which may be changed or removed in future versions without notice.
    12:20:13,947 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.ws.spi:main") which may be changed or removed in future versions without notice.
    12:20:13,947 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jboss.as.security:main") which may be changed or removed in future versions without notice.
    12:20:13,947 WARN  [org.jboss.as.dependency.private] (MSC service thread 1-5) JBAS018567: Deployment "deployment.jboss-as-helloworld.war" is using a private module ("org.jacorb:main") which may be changed or removed in future versions without notice.
    12:20:14,149 INFO  [org.jboss.weld.deployer] (MSC service thread 1-6) JBAS016002: Processing weld deployment jboss-as-helloworld.war
    12:20:14,356 INFO  [org.jboss.weld.deployer] (MSC service thread 1-6) JBAS016005: Starting Services for CDI deployment: jboss-as-helloworld.war
    12:20:14,461 INFO  [org.jboss.weld.Version] (MSC service thread 1-6) WELD-000900 2012-01-10 12:38
    12:20:14,512 INFO  [org.jboss.weld.deployer] (MSC service thread 1-2) JBAS016008: Starting weld service for deployment jboss-as-helloworld.war
    12:20:15,389 INFO  [org.jboss.web] (MSC service thread 1-8) JBAS018210: Registering web context: /jboss-as-helloworld
    12:20:15,406 INFO  [org.jboss.as.server] (management-handler-threads - 99) JBAS018559: Deployed "jboss-as-helloworld.war"
