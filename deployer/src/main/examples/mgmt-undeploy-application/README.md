jboss-as-management server undeploy Application Example
=======================================================

What is it?
-----------

This example shows you how you can use the JBoss Application Server Management API to undeploy and remove an application in a standalone server.


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
    [INFO] Building JBoss AS Quickstarts: This Example undeploys the jboss-as-helloworld.war Application via the Management API 1.0
    [INFO] ------------------------------------------------------------------------
    [INFO]
    .
    .
    .
    [INFO]
    [INFO] >>> exec-maven-plugin:1.2.1:java (default) @ mgmt-undeploy-application >>>
    [INFO]
    [INFO] <<< exec-maven-plugin:1.2.1:java (default) @ mgmt-undeploy-application <<<
    [INFO]
    [INFO] --- exec-maven-plugin:1.2.1:java (default) @ mgmt-undeploy-application ---
    21.02.2012 13:43:18 org.xnio.Xnio <clinit>
    INFO: XNIO Version 3.0.0.CR5
    21.02.2012 13:43:18 org.xnio.nio.NioXnio <clinit>
    INFO: XNIO NIO Implementation Version 3.0.0.CR5
    21.02.2012 13:43:18 org.jboss.remoting3.EndpointImpl <clinit>
    INFO: JBoss Remoting version 3.2.0.CR6
    {"outcome" => "success"}
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 2.635s
    [INFO] Finished at: Tue Feb 21 13:43:19 CET 2012
    [INFO] Final Memory: 6M/81M
    [INFO] ------------------------------------------------------------------------


You can also check the JBoss AS7 Standalone Console

    13:43:19,029 INFO  [org.jboss.weld.deployer] (MSC service thread 1-8) JBAS016009: Stopping weld service for deployment jboss-as-helloworld.war
    13:43:19,041 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-8) JBAS015877: Stopped deployment jboss-as-helloworld.war in 31ms
    13:43:19,076 INFO  [org.jboss.as.server] (management-handler-threads - 127) JBAS018558: Undeployed "jboss-as-helloworld.war"
    13:43:19,077 INFO  [org.jboss.as.repository] (management-handler-threads - 127) JBAS014901: Content removed from location /as7/standalone/data/content/08/38a8293c85177951a346326fb2367a0b856e10/content
