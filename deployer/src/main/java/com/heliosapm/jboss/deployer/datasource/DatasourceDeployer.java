/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heliosapm.jboss.deployer.datasource;

import java.net.InetAddress;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

/**
 * <p>Title: DatasourceDeployer</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.datasource.DatasourceDeployer</code></p>
 */

public class DatasourceDeployer {

	// http://wildscribe.github.io/JBoss%20EAP/6.0.0/subsystem/datasources/data-source/ExampleDS/index.html
	
    public static void main(String[] args) throws Exception
    {
        //ModelControllerClient client = ModelControllerClient.Factory.create(
        //      InetAddress.getByName("127.0.0.1"), 9999, DemoAuthentication.getCallbackHandler());
        ModelControllerClient client = ModelControllerClient.Factory.create(
                InetAddress.getByName("127.0.0.1"), 9990);
        try
        {
            String dsname = "oracle-bar";
            ModelNode op = new ModelNode();
            op.get("operation").set("add");

            op.get("address").add("subsystem", "datasources").add("data-source", dsname);

            op.get("jndi-name").set("java:jboss/datasources/" + dsname);
            op.get("driver-name").set("oracle");
            op.get("driver-name").set("ojdbc7-12.1.0.2.jar");
            op.get("pool-name").set("Oracle12DS");
            op.get("connection-url").set("jdbc:oracle:thin:@//localhost:1521/XE");
            op.get("max-pool-size").set(10);
            op.get("min-pool-size").set(5);
            op.get("allocation-retry").set(2);
            op.get("allocation-retry-wait-millis").set(90);
            op.get("allow-multiple-users").set(false);
            op.get("background-validation").set(true);
            
            // =================================================
            
            op.get("background-validation-millis").set(1000);
            op.get("blocking-timeout-wait-millis").set(500);
            op.get("check-valid-connection-sql").set("SELECT SYSDATE FROM DUAL");
            op.get("connectable").set(true);
            op.get("datasource-class").set("oracle.jdbc.pool.OracleDataSource");
            op.get("driver-class").set("oracle.jdbc.OracleDriver");
            op.get("driver-name").set("ojdbc7-12.1.0.2.jar");
            op.get("enabled").set(true);
            op.get("exception-sorter-class-name").set("org.jboss.jca.adapters.jdbc.extensions.oracle.OracleExceptionSorter");
//            op.get("exception-sorter-properties").set(undefined);
            op.get("flush-strategy").set("FailingConnectionOnly");
            op.get("idle-timeout-minutes").set(10);
            op.get("jta").set(true);
//            op.get("new-connection-sql").set(undefined);
            op.get("user-name").set("tqreactor");
            op.get("password").set("tq");
            op.get("pool-prefill").set(true);
            op.get("pool-use-strict-min").set(true);
            op.get("prepared-statements-cache-size").set(120);
            op.get("query-timeout").set(60);
//            op.get("reauth-plugin-class-name").set(undefined);
//            op.get("reauth-plugin-properties").set(undefined);
//            op.get("security-domain").set(undefined);
            op.get("set-tx-query-timeout").set(true);
            op.get("share-prepared-statements").set(true);
            op.get("spy").set(true);
            op.get("stale-connection-checker-class-name").set("org.jboss.jca.adapters.jdbc.extensions.oracle.OracleStaleConnectionChecker");
//            op.get("stale-connection-checker-properties").set(undefined);
            op.get("statistics-enabled").set(true);
            op.get("track-statements").set("NOWARN");
            op.get("transaction-isolation").set("TRANSACTION_SERIALIZABLE");
            op.get("url-delimiter").set(",");
//            op.get("url-selector-strategy-class-name").set(undefined);  //  A class that implements org.jboss.jca.adapters.jdbc.URLSelectorStrategy
            op.get("use-ccm").set(true);
            op.get("use-fast-fail").set(true);
            op.get("use-java-context").set(true);
            op.get("use-try-lock").set(2);
            
            op.get("valid-connection-checker-class-name").set("org.jboss.jca.adapters.jdbc.extensions.oracle.OracleValidConnectionChecker");
//            op.get("valid-connection-checker-properties").set(undefined);
            op.get("validate-on-match").set(true);
            
            
            

            ModelNode result = client.execute(op);

            if (result.hasDefined("outcome")
                    && "success".equals(result.get("outcome").asString()))
            {

                ModelNode readOp = new ModelNode();
                readOp.get("operation").set("read-resource");
                readOp.get("address").add("subsystem", "datasources");
                readOp.get("recursive").set(true);

                ModelNode localResult = client.execute(readOp);

                if (localResult.hasDefined("outcome") && "success".equals(localResult.get("outcome").asString()))
                {
                    System.out.println("SUCCESS!!!");
                    System.out.println("The following Datasources are now configured");

                    ModelNode dsList = localResult.get("result").get("data-source");
                    for (ModelNode ds : dsList.asList())
                    {
                        System.out.println("-------> Datasource: " + ds.asProperty().getName() + " <------");
                        for (Property prop : ds.asProperty().getValue().asPropertyList())
                        {
                            System.out.println(prop.getName() + "=" + prop.getValue());
                        }
                    }
                }

                op = new ModelNode();
                op.get("operation").set("enable");
                op.get("address").add("subsystem", "datasources").add("data-source", dsname);
                result = client.execute(
                        new ModelNode()
                                .get("operation").set("enable")
                                .get("address")
                                .add("subsystem", "datasources")
                                .add("data-source", dsname)
                );

                op = new ModelNode();
                op.get("operation").set("write-attribute");
                op.get("address").add("subsystem", "datasources").add("data-source", dsname);
                op.get("name").set("max-pool-size");
                op.get("value").set("20");
                result = client.execute(op);

                op = new ModelNode();
                op.get("operation").set("write-attribute");
                op.get("address").add("subsystem", "datasources").add("data-source", dsname);
                op.get("name").set("min-pool-size");
                op.get("value").set("10");
                result = client.execute(op);


            }
            else if (result.hasDefined("failure-description"))
            {
                throw new RuntimeException(result.get("failure-description")
                        .toString());
            }
            else
            {
                throw new RuntimeException(
                        "Operation not successful; outcome = "
                                + result.get("outcome"));
            }
        }
        finally
        {
            client.close();
        }
    }

    private static void debug(String identifier, ModelNode node)
    {
        System.out.println(identifier + " - ModelNodeType: " + node.getType());
    }

}
