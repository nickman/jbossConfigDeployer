package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

import java.net.InetAddress;

/**
 * Red Hat GmbH - Global Professional Services (GPS)
 * <p/>
 * Author: <a href="mailto:jhoffmann@redhat.com">Juergen Hoffmann</a>
 * Date:   21.02.12
 * Time:   14:12
 */
public class Client
{
    private static String APPLICATION_NAME = null;

    public static void main(String[] args) throws Exception
    {
        ModelControllerClient client = ModelControllerClient.Factory.create(
                InetAddress.getByName("localhost"), 9999);

        APPLICATION_NAME = (args.length > 0) ? args[0] : "jboss-as-helloworld.war";

        ModelNode result;

        try
        {
            ModelNode op = new ModelNode();
            op.get("operation").set("undeploy");
            op.get("address").add("deployment", APPLICATION_NAME);

            result = client.execute(op);

            /**
             * due to https://issues.jboss.org/browse/AS7-3844 it is not possible at the moment
             * to disable a deployment and set the enabled state to STOPPED
             *
             * Therefor we have to use a workaround and undeploy the application first and redeploy
             * it afterwards with enabled==false
             */
            op = new ModelNode();
            op.get("operation").set("deploy");
            op.get("address").add("deployment", APPLICATION_NAME);
            op.get("enabled").set(false);

            result = client.execute(op);

            if (result.hasDefined("outcome")
                    && "success".equals(result.get("outcome").asString()))
            {
                System.out.println(result);
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
