jboss-as-management List Deployments in Domain Example
======================================================

What is it?
-----------

This example shows you how you can use the JBoss Application Server Management API to
list the deployments that exist in a domain controller


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.1.1-SNAPSHOT or EAP 6.0
 
With the prerequisites out of the way, you're ready to build and deploy.


Run the standalone application
-------------------------

First of all you need to create a user in the ManagementRealm by running
the $JBOSS_HOME/bin/add-user.sh (add-user.bat on Windows). Then start JBoss AS 7.1.1-SNAPSHOT.
by running this script
  
    $JBOSS_HOME/bin/domain.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/domain.bat

Make sure you have an application like the jboss-as-helloworld application deployed to a
server group. To run the application, set the properties in pom.xml accordingly and use
the following Maven goal:

    mvn test

    $ mvn test
    [INFO] Scanning for projects...
    [INFO]
    [INFO] ------------------------------------------------------------------------
    [INFO] Building JBoss AS Quickstarts: This Quickstarts shows you how you can list deployments via the mgmt API 1.0.0
    [INFO] ------------------------------------------------------------------------
    .
    .
    .
    [INFO]
    [INFO] --- exec-maven-plugin:1.2.1:java (default) @ mgmt-domain-list-deployments ---
    Trying to connect to:
    Host:      1.2.3.4
    Username:  admin
    Password:  redhat
    Examining: main-server-group
    02.03.2012 08:58:25 org.xnio.Xnio <clinit>
    INFO: XNIO Version 3.0.3.GA
    02.03.2012 08:58:25 org.xnio.nio.NioXnio <clinit>
    INFO: XNIO NIO Implementation Version 3.0.3.GA
    02.03.2012 08:58:25 org.jboss.remoting3.EndpointImpl <clinit>
    INFO: JBoss Remoting version 3.2.2.GA

    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    &&&&&& Deployment: "jboss-as-helloworld.war"
    &&&&&& enabled: true
    &&&&&& name: "jboss-as-helloworld.war"
    &&&&&& runtime-name: "jboss-as-helloworld.war"
    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 4.312s
    [INFO] Finished at: Fri Mar 02 08:58:26 CET 2012
    [INFO] Final Memory: 8M/81M
    [INFO] ------------------------------------------------------------------------


You will receive a simple listing with all the deployments and their state.