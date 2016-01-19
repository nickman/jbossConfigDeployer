package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

import java.net.InetAddress;

/**
 * Red Hat GmbH - Global Professional Services (GPS)
 * <p/>
 * Author: <a href="mailto:jhoffmann@redhat.com">Juergen Hoffmann</a>
 * Date:   21.02.12
 * Time:   10:20
 */
public class Client
{

    public static final String APPLICATION_NAME = "jboss-as-helloworld.war";

    public static void main(String[] args) throws Exception
    {
        ModelControllerClient client = ModelControllerClient.Factory.create(
                InetAddress.getByName("10.32.69.185"), 9999, new Authenticator("admin", "redhat"));

        ModelNode result;

        try
        {
            ModelNode op = new ModelNode();
            op.get("operation").set("add");
            op.get("address").add("deployment", APPLICATION_NAME);
            // This is the URL for the deployment. Through pom.xml we are getting
            // the basedir presented, and then we just add the missing path
            op.get("content").add("url", "file://" + args[0] + "/application/" + APPLICATION_NAME);
            //op.get("content").add("archive", true);
            op.get("enabled").set(true);

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
