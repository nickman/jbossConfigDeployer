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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

/**
 * <p>Title: Deployment</p>
 * <p>Description: Represents a deployment on the connected server</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.common.Deployment</code></p>
 */

public class Deployment {
	protected final byte[] hash;
	protected final String hashString;
	protected final boolean enabled;
	protected final String name;
	protected final boolean persistent;
	protected final String runtimeName;
	protected final Map<String, Deployment> subdeployments = new HashMap<String, Deployment>();
	protected final String[] subsystems;

	private static final String[] EMPTY_STR = {};
	private static final Map<String, Deployment> EMPTY_DEP_MAP = Collections.unmodifiableMap(new HashMap<String, Deployment>(0));
	
	/**
	 * Creates a new Deployment
	 */
	public Deployment(final List<Property> properties, final ModelControllerClient mcc) {
		Map<String, ModelNode> map = toMap(properties);
		
		hash = map.get("content").get(0).get("hash").asBytes();
		hashString = DatatypeConverter.printHexBinary(hash);
		enabled = map.get("enabled").asBoolean();
		persistent = map.get("persistent").asBoolean();
		name = map.get("name").asString();
		runtimeName = map.get("runtime-name").asString();
		ModelNode tmp = map.get("subdeployment");
		if(tmp.getType()!=ModelType.UNDEFINED) {
			final ModelNode subDeps = map.get("subdeployment"); 
			for(String deploymentName: subDeps.keys()) {
				if(mcc==null) {
					subdeployments.put(deploymentName, null);
				} else {
          ModelNode op = new ModelNode();
          op.get("operation").set("read-resource");
          op.get("address")
//          	.add("server-group", serverGroup)
          	.add("deployment", deploymentName);
          ModelNode state = null;
          try {
          	state = mcc.execute(op);
          	subdeployments.put(deploymentName, new Deployment(state.get("result").asPropertyList(), mcc));
          } catch (Exception ex) {
          	subdeployments.put(deploymentName, null);
          }
				}
			}
		}
		tmp = map.get("subsystem");
		subsystems = tmp.getType()==ModelType.UNDEFINED ? EMPTY_STR : tmp.keys().toArray(EMPTY_STR);
		
	}
	
	public static Map<String, ModelNode> toMap(final List<Property> properties) {
		final Map<String, ModelNode> map = new HashMap<String, ModelNode>(properties.size());
		for (Property p : properties) {
			map.put(p.getName(), p.getValue());
		}
		return map;
	}
	
	
	public static Map<String, Deployment> deployments(final ModelControllerClient mcc) {
    ModelNode op = new ModelNode();
    op.get("operation").set("read-children-names");
//    op.get("address").add("server-group", serverGroup);
    op.get("child-type").set("deployment");
    ModelNode result = null;
    try {
    	result = mcc.execute(op);
    } catch (Exception ex) {
    	throw new RuntimeException("Failed to list deployments", ex);
    }
    
		if(result==null) return EMPTY_DEP_MAP;		
		if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
			if (result.hasDefined("result")) {
				final List<ModelNode> deploymentList = result.get("result").asList();
				final Map<String, Deployment> map = new HashMap<String, Deployment>(deploymentList.size());
				for (ModelNode deployment : deploymentList) {
          op = new ModelNode();
          op.get("operation").set("read-resource");
          op.get("address")
          	//.add("server-group", serverGroup)
          	.add("deployment", deployment.asString());
          ModelNode state = null;
          try {
          	state = mcc.execute(op);
          	log("DEPLOYMENT JSON:" + state.toJSONString(false));
          } catch (Exception ex) {
          	throw new RuntimeException("Failed to get deployment [" + deployment + "]", ex);
          }
          if (state.hasDefined("outcome") && "success".equals(state.get("outcome").asString())) {
          	List<Property> properties = state.get("result").asPropertyList();
          	Deployment dep = new Deployment(properties, mcc);
          	map.put(dep.name, dep);
          }
				}
				return map;
			}
		}
		return EMPTY_DEP_MAP;
	}
	
//	ModelNode result = mcc.execute(op);
//  if (result.hasDefined("outcome")
//          && "success".equals(result.get("outcome").asString()))
//  {
//      if (result.hasDefined("result"))
//      {
//          System.out.println();
////          System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//          for (ModelNode deployment : result.get("result").asList())
//          {
//              System.out.println("&&&&&& Deployment: " + deployment.toString());
//              op = new ModelNode();
//              op.get("operation").set("read-resource");
//              op.get("address")
//              	//.add("server-group", serverGroup)
//              	.add("deployment", deployment.asString());
//              ModelNode state = mcc.execute(op);
//              if (state.hasDefined("outcome") && "success".equals(state.get("outcome").asString()))
//              {
//              	System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//                  List<Property> properties = state.get("result").asPropertyList();
//                  Deployment d = new Deployment(properties);
                  
	
	
	
	public static void log(final Object fmt, final Object...args) {
		System.out.println(String.format(fmt.toString(), args));
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder()
		.append("Deployment (").append(name).append(") [")		
		.append("\n\thash: ").append(hashString.toLowerCase())
		.append("\n\tenabled: ").append(enabled)
		.append("\n\tpersistent: ").append(persistent)
		.append("\n\truntimeName: ").append(runtimeName)
		.append("\n\tsubSystems: ").append(Arrays.toString(subsystems))
		.append("\n\tsubDeployments: ").append(subdeployments)
		.append("\n]").toString();
	}
	

}
