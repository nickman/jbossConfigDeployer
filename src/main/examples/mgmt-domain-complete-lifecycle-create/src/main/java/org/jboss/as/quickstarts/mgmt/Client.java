package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.helpers.domain.*;
import org.jboss.dmr.ModelNode;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    private static DomainClient client = null;

    private static String EXAMPLE_SERVER_GROUP = "example-server-group";

    private static String EXAMPLE_SERVER_NAME = "example-server";

    private static String SLAVE_NODE_NAME = "slave";

    public static void main(String[] args) throws Exception
    {

        String username = "admin";
        String password = "redhat";
        String host = "10.32.69.185";

        if (args.length == 4)
        {
            host = args[0];
            username = args[1];
            password = args[2];
        }

        System.out.println("Trying to connect to:");
        System.out.println("Host:      " + host);
        System.out.println("Username:  " + username);
        System.out.println("Password:  " + password);


        client = DomainClient.Factory.create(InetAddress.getByName(host), 9999, new Authenticator(username, password));
        try
        {
            String profile = getProfile();
            String socketBindingGroup = getSocketBindingGroup();

            System.out.println("Will create server-group=" + EXAMPLE_SERVER_GROUP + " with profile=" + profile + " and Socket Binding Group=" + socketBindingGroup);

            addExampleServerGroup(EXAMPLE_SERVER_GROUP, profile, socketBindingGroup);

            System.out.println("server-group=" + EXAMPLE_SERVER_GROUP + " created");
            System.out.println("Creating server=" + EXAMPLE_SERVER_NAME + " on host=" + SLAVE_NODE_NAME + " with reference to server-group=" + EXAMPLE_SERVER_GROUP);

            addServer(EXAMPLE_SERVER_NAME, SLAVE_NODE_NAME, EXAMPLE_SERVER_GROUP);

            System.out.println("Server server=" + EXAMPLE_SERVER_NAME + " on host=" + SLAVE_NODE_NAME + " with reference to server-group=" + EXAMPLE_SERVER_GROUP + " created.");
            System.out.println("Starting server=" + EXAMPLE_SERVER_NAME);

            startServer(SLAVE_NODE_NAME, EXAMPLE_SERVER_NAME);

            deployApplication();

            System.out.println("SUCCESS!!! Please point your Browser to http://<ip.address.of.slavehost>:8080/jboss-as-helloworld/HelloWorld and you should see a nice Page :-)");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            client.close();
        }
    }

    private static void deployApplication() throws IOException, DuplicateDeploymentNameException, ExecutionException, InterruptedException
    {
        DomainDeploymentManager manager = client.getDeploymentManager();
        Future future = manager.execute(
                manager.newDeploymentPlan()
                        .add(Client.class.getResource("/jboss-as-helloworld.war"))
                        .andDeploy()
                        .toServerGroup(EXAMPLE_SERVER_GROUP)
                        .build()
        );

        while (!future.isDone())
        {
            System.out.println("Waiting for our application to be deployed...");
            Thread.sleep(1000);
        }

        DeploymentPlanResult result = (DeploymentPlanResult) future.get();
        Map<String, ServerGroupDeploymentPlanResult> resultMap = result.getServerGroupResults();
        for (String string : resultMap.keySet())
        {
            System.out.print(string + ": ");
            System.out.println(resultMap.get(string).getServerGroupName() + ": " + resultMap.get(string).getServerResults().toString());
        }


    }

    private static void startServer(String node_name, String server_name) throws IOException, InterruptedException
    {
        ServerStatus status = client.startServer(node_name, server_name);

        while (status == ServerStatus.STARTING)
        {
            System.out.println("Waiting for the server to start up...");
            Map<ServerIdentity, ServerStatus> map = client.getServerStatuses();
            for (ServerIdentity identity : map.keySet())
            {
                if (identity.getServerName().equals(server_name) && identity.getHostName().equals(node_name))
                    status = map.get(identity);
            }
            Thread.sleep(500);
        }
        Thread.sleep(2000);
        System.out.println("Server started");
    }

    private static ModelNode addServer(String server_name, String node_name, String server_group) throws IOException
    {
        ModelNode operation = new ModelNode();
        operation.get("operation").set("add");
        operation.get("address").add("host", node_name).add("server-config", server_name);
        operation.get("group").set(server_group);

        ModelNode result = getResult(operation);
        return result;

    }

    private static ModelNode addExampleServerGroup(String server_group, String profile, String socket_bindings_group) throws IOException
    {
        ModelNode operation = new ModelNode();
        operation.get("operation").set("add");
        operation.get("address").add("server-group", server_group);
        operation.get("profile").set(profile);
        operation.get("socket-binding-group").set(socket_bindings_group);

        ModelNode result = getResult(operation);
        return result;
    }

    private static String getSocketBindingGroup() throws IOException
    {
        ModelNode operation = new ModelNode();
        operation.get("operation").set("read-children-names");
        operation.get("child-type").set("socket-binding-group");

        ModelNode result = getResult(operation);

        return result.asList().get(0).asString();
    }

    private static ModelNode getResult(ModelNode operation) throws IOException
    {
        ModelNode result = client.execute(operation);

        if (result.hasDefined("outcome")
                && "success".equals(result.get("outcome").asString()))
        {
            return result.get("result");
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

    private static String getProfile() throws IOException
    {
        ModelNode operation = new ModelNode();
        operation.get("operation").set("read-children-names");
        operation.get("child-type").set("profile");

        return getResult(operation).asList().get(0).asString();
    }
}