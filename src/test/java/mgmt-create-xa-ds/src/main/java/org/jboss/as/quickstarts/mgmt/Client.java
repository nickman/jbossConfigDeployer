package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

import java.net.InetAddress;

/**
 * Red Hat GmbH - Global Professional Services (GPS)
 * <p/>
 * Author: <a href="mailto:jhoffmann@redhat.com">Juergen Hoffmann</a>
 * Date:   13.02.12
 * Time:   09:51
 */
public class Client
{
    public static void main(String[] args) throws Exception
    {
        ModelControllerClient client = ModelControllerClient.Factory.create(
                InetAddress.getByName("127.0.0.1"), 9999);

        try
        {
            String dsname = "REPLACE_WITH_PROPERTY";
            ModelNode op = new ModelNode();
            op.get("operation").set("add");

            op.get("address").add("subsystem", "datasources").add("xa-data-source", dsname);

            op.get("jndi-name").set("java:jboss/datasources/" + "XA" + dsname);
            op.get("xa-datasource-class").set("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
            op.get("driver-name").set("mysql");
            op.get("use-java-context").set("true");
            op.get("pool-name").set("TestXADS");
            op.get("xa-resource-timeout").set(3600);
            op.get("jta").set(true);

            op.get("max-pool-size").set(10);
            op.get("min-pool-size").set(5);

            ModelNode result = client.execute(op);

            System.out.println(result.toString());

            op = new ModelNode();
            op.get("operation").set("add");
            op.get("address").add("subsystem", "datasources").add("xa-data-source", dsname).add("xa-datasource-properties", "URL");
            op.get("value").set("jdbc:mysql://localhost/demo");

            result = client.execute(op);

            System.out.println(result.toString());

            op = new ModelNode();
            op.get("operation").set("add");
            op.get("address").add("subsystem", "datasources").add("xa-data-source", dsname).add("xa-datasource-properties", "User");
            op.get("value").set("demo");

            result = client.execute(op);

            System.out.println(result.toString());

            op = new ModelNode();
            op.get("operation").set("add");
            op.get("address").add("subsystem", "datasources").add("xa-data-source", dsname).add("xa-datasource-properties", "Password");
            op.get("value").set("demo");

            result = client.execute(op);

            System.out.println(result.toString());
        }
        finally
        {
            client.close();
        }
    }
}
