package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

import java.net.InetAddress;

/**
 * Red Hat GmbH - Global Professional Services (GPS)
 * <p/>
 * Author: <a href="mailto:jhoffmann@redhat.com">Juergen Hoffmann</a>
 * Date:   21.02.12
 * Time:   13:32
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
            op.get("operation").set("remove");
            op.get("address").add("deployment", APPLICATION_NAME);

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
