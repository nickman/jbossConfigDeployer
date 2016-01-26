/**
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 */
package com.heliosapm.jboss.deployer.common;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

/**
 * <p>Title: AdminConnection</p>
 * <p>Description: Encapsulates a ModelControllerClient and a number of admin functions</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.common.AdminConnection</code></p>
 */


/*
 * TODO:
 * Deploy/Undeploy Apps, DataSources, JDBC Drivers, RARs
 * 
 */

public class AdminConnection  implements Closeable {
	/** The native client connection */
	final ModelControllerClient mcc;
	
	/**
	 * Creates a new AdminConnection
	 * @param mcc The native client connection
	 */
	public AdminConnection(final ModelControllerClient mcc) {
		if(mcc==null) throw new IllegalArgumentException("The passed client was null");
		this.mcc = mcc;
	}
	
	@Override
	public void close() throws IOException {
		mcc.close();		
	}
	
	/**
	 * Returns the status of the connected server
	 * @return the status of the connected server
	 */
	public String getStatus() {
    try {
      ModelNode op = new ModelNode();
      op.get("operation").set("read-attribute");
      op.get("name").set("server-state");
      ModelNode result = mcc.execute(op);
      if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
          if (result.hasDefined("result")) {
          	return result.get("result").asString();
          }
          throw new RuntimeException("Operation not successful; No result");
      } else if (result.hasDefined("failure-description")) {
          throw new RuntimeException(result.get("failure-description").toString());
      } else {
          throw new RuntimeException("Operation not successful; outcome = " + result.get("outcome"));
      }
	  } catch (Exception ex) {
	  	throw new RuntimeException("Failed to get status", ex);
	  }		
	}
	
	/**
	 * Returns a map of deployments keyed by the deployment names
	 * @return a map of deployments keyed by the deployment names
	 */
	public Map<String, Deployment> getDeployments() {
		return Deployment.deployments(mcc);
	}

}
