package org.jboss.as.quickstarts.mgmt;

import org.jboss.as.controller.client.helpers.domain.*;
import org.jboss.as.controller.client.helpers.domain.impl.DomainClientImpl;

import java.io.File;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Red Hat GmbH - Global Professional Services (GPS)
 * <p/>
 * Author: <a href="mailto:jhoffmann@redhat.com">Juergen Hoffmann</a>
 * Date:   28.02.12
 * Time:   21:47
 */
public class HelperClient
{
    public static final String APPLICATION_NAME = "jboss-as-helloworld.war";

    public static void main(String[] args) throws Exception
    {

        DomainClient client = new DomainClientImpl(InetAddress.getByName("10.32.69.185"), 9999, new Authenticator("admin", "redhat"));
        try
        {
            DomainDeploymentManager manager = client.getDeploymentManager();
            DeploymentPlanBuilder builder = manager.newDeploymentPlan();
            builder = builder.add(new File(args[0] + "/application/" + APPLICATION_NAME));
            Future future = manager.execute(builder.build());
            while (!future.isDone())
            {
                System.out.println("Waiting for deployment to finish");
                Thread.sleep(500);
            }

            DeploymentPlanResult deploymentPlanResult = (DeploymentPlanResult) future.get();

            if (deploymentPlanResult.isValid())
            {
                List<DeploymentAction> actions = deploymentPlanResult.getDeploymentPlan().getDeploymentActions();
                for (int i = 0; i < actions.size(); i++)
                {
                    DeploymentAction deploymentAction = actions.get(i);
                    System.out.println("Deployed: " + deploymentAction.getDeploymentUnitUniqueName());
                }
            }
            else
            {
                System.out.println(deploymentPlanResult.getInvalidDeploymentPlanException().toString());
            }
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
}
