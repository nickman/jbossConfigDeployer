package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import java.net.InetAddress;
import java.util.List;

/**
 * <p>
 * This example shows you how you can use the JBoss Application Server Management API to get the
 * Status of a standalone server."
 * </p>
 * <p> The Model description via the CLI will look like this
 * <pre>
 * [standalone@localhost:9999 /] :read-attribute(name=server-state)
 * {
 *   "outcome" => "success",
 *   "result" => "running"
 * }
 * </pre>
 * </p>
 *
 * @author Serge Pagop (spagop@redhat.com)
 */
public class Client
{
    public static void main(String[] args) throws Exception
    {

        String username = "";
        String password = "";
        String host = "";
        String serverGroup = "";

        if (args.length == 4)
        {
            host = args[0];
            username = args[1];
            password = args[2];
            serverGroup = args[3];
        }

        System.out.println("Trying to connect to:");
        System.out.println("Host:      " + host);
        System.out.println("Username:  " + username);
        System.out.println("Password:  " + password);
        System.out.println("Examining: " + serverGroup);

        ModelControllerClient client = ModelControllerClient.Factory.create(
                InetAddress.getByName(host), 9999, new Authenticator(username, password));
        try
        {
            ModelNode op = new ModelNode();
            op.get("operation").set("read-children-names");
            op.get("address").add("server-group", serverGroup);
            op.get("child-type").set("deployment");
            ModelNode result = client.execute(op);
            if (result.hasDefined("outcome")
                    && "success".equals(result.get("outcome").asString()))
            {
                if (result.hasDefined("result"))
                {
                    System.out.println();
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    for (ModelNode deployment : result.get("result").asList())
                    {
                        System.out.println("&&&&&& Deployment: " + deployment.toString());
                        op = new ModelNode();
                        op.get("operation").set("read-resource");
                        op.get("address").add("server-group", serverGroup).add("deployment", deployment.asString());
                        ModelNode state = client.execute(op);
                        if (state.hasDefined("outcome") && "success".equals(state.get("outcome").asString()))
                        {
                            List<Property> properties = state.get("result").asPropertyList();
                            for (Property property : properties)
                            {
                                System.out.println("&&&&&& " + property.getName() + ": " + property.getValue());
                            }
                        }
                        else if (state.hasDefined("failure-description"))
                        {
                            throw new RuntimeException(state.get("failure-description")
                                    .toString());
                        }
                        else
                        {
                            throw new RuntimeException(
                                    "Operation not successful; outcome = "
                                            + state.get("outcome"));
                        }
                    }
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    System.out.println();
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
}