package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import java.net.InetAddress;

/**
 * <p>
 * This example shows you how you can use the JBoss Application Server Management API to create a
 * JMS Queue.
 * </p>
 *
 * @author Juergen Hoffmann (jhoffmann@redhat.com)
 */
public class Client
{
    public static void main(String[] args) throws Exception
    {
        ModelControllerClient client = ModelControllerClient.Factory.create(
                InetAddress.getByName("localhost"), 9999);

        ModelNode result;

        try
        {
            String topicName = "REPLACE_WITH_PROPERTY";
            ModelNode op = new ModelNode();
            op.get("operation").set("add");

            op.get("address").add("subsystem", "messaging").add("hornetq-server", "default").add("jms-topic", topicName);
            op.get("entries").add("queue/" + topicName);

            result = client.execute(op);

            if (result.hasDefined("outcome")
                    && "success".equals(result.get("outcome").asString()))
            {

                ModelNode readOp = new ModelNode();
                readOp.get("operation").set("read-resource");
                readOp.get("address").add("subsystem", "messaging").add("hornetq-server", "default");
                readOp.get("recursive").set(true);

                result = client.execute(readOp);

                if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString()))
                {
                    System.out.println("SUCCESS!!!");
                    System.out.println("The following JMS Topics are now configured");

                    ModelNode queueList = result.get("result").get("jms-topic");
                    for (ModelNode queue : queueList.asList())
                    {
                        System.out.println("-------> JMS Topic: " + queue.asProperty().getName() + " <------");
                        for (Property prop : queue.asProperty().getValue().asPropertyList())
                        {
                            System.out.println(prop.getName() + "=" + prop.getValue());
                        }
                    }
                }
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
