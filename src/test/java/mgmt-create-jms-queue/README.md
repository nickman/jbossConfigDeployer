jboss-as-management server create JMS Queue Example
====================================================

What is it?
-----------

This example shows you how you can use the JBoss Application Server Management API to create a JMS Queue in a standalone server.


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

Note: you can also run the sample standalone application "Client" from your Eclipse IDE 
After executing the maven goal or from eclipse, the result can look like this:

[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building JBoss AS Quickstarts: Create a JMS Queue through the Mgmt API 1.0
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.4.3:resources (default-resources) @ mgmt-create-jms-queue ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /Users/buddy/spagop-docs/mgmt-create-jms-queue/src/main/resources
[INFO]
[INFO] --- maven-compiler-plugin:2.3.1:compile (default-compile) @ mgmt-create-jms-queue ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-resources-plugin:2.4.3:testResources (default-testResources) @ mgmt-create-jms-queue ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /Users/buddy/spagop-docs/mgmt-create-jms-queue/src/test/resources
[INFO]
[INFO] --- maven-compiler-plugin:2.3.1:testCompile (default-testCompile) @ mgmt-create-jms-queue ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-surefire-plugin:2.7.2:test (default-test) @ mgmt-create-jms-queue ---
[INFO] Surefire report directory: /Users/buddy/spagop-docs/mgmt-create-jms-queue/target/surefire-reports

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
There are no tests to run.

Results :

Tests run: 0, Failures: 0, Errors: 0, Skipped: 0

[INFO]
[INFO] >>> exec-maven-plugin:1.2.1:java (default) @ mgmt-create-jms-queue >>>
[INFO]
[INFO] <<< exec-maven-plugin:1.2.1:java (default) @ mgmt-create-jms-queue <<<
[INFO]
[INFO] --- exec-maven-plugin:1.2.1:java (default) @ mgmt-create-jms-queue ---
05.02.2012 09:53:05 org.xnio.Xnio <clinit>
INFO: XNIO Version 3.0.0.CR5
05.02.2012 09:53:05 org.xnio.nio.NioXnio <clinit>
INFO: XNIO NIO Implementation Version 3.0.0.CR5
05.02.2012 09:53:05 org.jboss.remoting3.EndpointImpl <clinit>
INFO: JBoss Remoting version 3.2.0.CR6
SUCCESS!!!
The following JMS Queues are now configured
-------> JMS Queue: REPLACE_WITH_PROPERTY <------
durable=true
entries=["queue/REPLACE_WITH_PROPERTY"]
selector=undefined
-------> JMS Queue: testQueue <------
durable=true
entries=["queue/test"]
selector=undefined
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 2.622s
[INFO] Finished at: Sun Feb 05 09:53:06 CET 2012
[INFO] Final Memory: 6M/81M
[INFO] ------------------------------------------------------------------------

You can also start JBoss AS 7 and test the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.
