package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import java.net.InetAddress;

/**
 * <p>
 * This example shows you how you can use the JBoss Application Server Management API to create a
 * Datasource.
 * </p>
 *
 * @author Juergen Hoffmann (jhoffmann@redhat.com)
 */
public class Client
{
    public static void main(String[] args) throws Exception
    {
        //ModelControllerClient client = ModelControllerClient.Factory.create(
        //      InetAddress.getByName("127.0.0.1"), 9999, DemoAuthentication.getCallbackHandler());
        ModelControllerClient client = ModelControllerClient.Factory.create(
                InetAddress.getByName("127.0.0.1"), 9999);
        try
        {
            String dsname = "REPLACE_WITH_PROPERTY";
            ModelNode op = new ModelNode();
            op.get("operation").set("add");

            op.get("address").add("subsystem", "datasources").add("data-source", dsname);

            op.get("jndi-name").set("java:jboss/datasources/" + dsname);
            op.get("driver-name").set("mysql");
            //op.get("driver-name").set("postgresql-8.4-703.jdbc4.jar");
            op.get("pool-name").set("TestDS");
            op.get("connection-url").set("jdbc:mysql://localhost/demo");
            op.get("max-pool-size").set(10);
            op.get("min-pool-size").set(5);

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
