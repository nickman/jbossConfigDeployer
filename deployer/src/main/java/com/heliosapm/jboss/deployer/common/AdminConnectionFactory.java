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

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.threads.AsyncFuture;

/**
 * <p>Title: AdminConnectionFactory</p>
 * <p>Description: Caching ModelControllerClient factory</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.common.AdminConnectionFactory</code></p>
 */

public class AdminConnectionFactory {
	
	/** A cache of connected ModelControllerClient keyed by host:port */
	protected static final Map<String, ModelControllerClient> clients = new ConcurrentHashMap<String, ModelControllerClient>();
	
	/** The default host */
	public static final String DEFAULT_HOST = "127.0.0.1";
	/** The default port */
	public static final int DEFAULT_PORT = 9999;
	
	public static void main(String args[]) {
		log("Connect Test");
		//ModelControllerClient mcc = getConnection("leopard", 9998, "admin", "jere!1029", null);
		ModelControllerClient mcc = getConnection();
		log("Connected !");
		log("Cached: %s", clients.size());
		AdminConnection ac = new AdminConnection(mcc);
		log("Status: [%s]", ac.getStatus());
		Map<String, Deployment> map = ac.getDeployments();
		for(Deployment d : map.values()) {
			log(d);
		}
		try {
			ac.close();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		log("Cached: %s", clients.size());
	}

	/**
	 * Acquires a ModelControllerClient for the passed host at the passed port
	 * @param host The host name to connect to
	 * @param port The port to connect to
	 * @param user The optional user name
	 * @param password The optional password
	 * @param realm The optional realm to connect to
	 * @return the ModelControllerClient
	 */
	public static ModelControllerClient getConnection(final String host, final int port, final String user, final String password, final String realm) {
		final String key = host + ":" + port;
		ModelControllerClient mcc = clients.get(key);
		if(mcc==null) {
			synchronized(clients) {
				mcc = clients.get(key);
				if(mcc==null) {
					try {
						if(user==null) {
							mcc = new CacheAwareModelControllerClient(ModelControllerClient.Factory.create(host, port), key);
						} else {
							mcc = new CacheAwareModelControllerClient(ModelControllerClient.Factory.create(host, port, new CallbackHandler(){
								@Override
								public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
			            for (Callback current : callbacks) {
			                if (current instanceof RealmCallback) {
			                    RealmCallback rcb = (RealmCallback) current;
			                    String defaultText = realm==null ? rcb.getDefaultText() : realm;
			                    rcb.setText(defaultText); 
			                } else if (current instanceof RealmChoiceCallback) {
			                    throw new UnsupportedCallbackException(current, "Realm choice not currently supported.");
			                } else if (current instanceof NameCallback) {
			                    NameCallback ncb = (NameCallback) current;
			                    ncb.setName(user);
			                } else if (current instanceof PasswordCallback) {
			                    PasswordCallback pcb = (PasswordCallback) current;
			                    pcb.setPassword(password.toCharArray());
			                } else {
			                    throw new UnsupportedCallbackException(current);
			                }
			            }									
								}
							}), key);
						}
					} catch (Exception ex) {
						throw new RuntimeException("Failed to get ModelControllerClient for [" + key + "]", ex);
					}
				}
				clients.put(key, mcc);
			}
		}
		return mcc;
	}
	
	/**
	 * Acquires a ModelControllerClient for the passed host at the passed port without credentials
	 * @param host The host name to connect to
	 * @param port The port to connect to
	 * @return the ModelControllerClient
	 */
	public static ModelControllerClient getConnection(final String host, final int port) {
		return getConnection(host, port, null, null, null);
	}
	
	/**
	 * Acquires a ModelControllerClient for the default host and port without credentials
	 * @return the ModelControllerClient
	 */
	public static ModelControllerClient getConnection() {
		return getConnection(DEFAULT_HOST, DEFAULT_PORT, null, null, null);
	}
	

	private static class CacheAwareModelControllerClient implements ModelControllerClient {
		final ModelControllerClient client;
		final String key;

		/**
		 * Creates a new CacheAwareModelControllerClient
		 * @param client The delegate client
		 * @param key The cache key
		 */
		private CacheAwareModelControllerClient(final ModelControllerClient client, final String key) {
			super();
			this.client = client;
			this.key = key;
		}

		/**
		 * {@inheritDoc}
		 * @see java.io.Closeable#close()
		 */
		@Override
		public void close() throws IOException {
			clients.remove(key);
			client.close();
		}

		/**
		 * {@inheritDoc}
		 * @see org.jboss.as.controller.client.ModelControllerClient#execute(org.jboss.dmr.ModelNode)
		 */
		@Override
		public ModelNode execute(ModelNode operation) throws IOException {
			return client.execute(operation);
		}

		/**
		 * {@inheritDoc}
		 * @see org.jboss.as.controller.client.ModelControllerClient#execute(org.jboss.as.controller.client.Operation)
		 */
		@Override
		public ModelNode execute(Operation operation) throws IOException {
			return client.execute(operation);
		}

		/**
		 * {@inheritDoc}
		 * @see org.jboss.as.controller.client.ModelControllerClient#execute(org.jboss.dmr.ModelNode, org.jboss.as.controller.client.OperationMessageHandler)
		 */
		@Override
		public ModelNode execute(ModelNode operation, OperationMessageHandler messageHandler) throws IOException {
			return client.execute(operation, messageHandler);
		}

		/**
		 * {@inheritDoc}
		 * @see org.jboss.as.controller.client.ModelControllerClient#execute(org.jboss.as.controller.client.Operation, org.jboss.as.controller.client.OperationMessageHandler)
		 */
		@Override
		public ModelNode execute(Operation operation, OperationMessageHandler messageHandler) throws IOException {
			return client.execute(operation, messageHandler);
		}

		/**
		 * {@inheritDoc}
		 * @see org.jboss.as.controller.client.ModelControllerClient#executeAsync(org.jboss.dmr.ModelNode, org.jboss.as.controller.client.OperationMessageHandler)
		 */
		@Override
		public AsyncFuture<ModelNode> executeAsync(ModelNode operation, OperationMessageHandler messageHandler) {
			return client.executeAsync(operation, messageHandler);
		}

		/**
		 * {@inheritDoc}
		 * @see org.jboss.as.controller.client.ModelControllerClient#executeAsync(org.jboss.as.controller.client.Operation, org.jboss.as.controller.client.OperationMessageHandler)
		 */
		@Override
		public AsyncFuture<ModelNode> executeAsync(Operation operation, OperationMessageHandler messageHandler) {
			return client.executeAsync(operation, messageHandler);
		}
		
		
	}
	
	
	public static void log(final Object fmt, final Object...args) {
		System.out.println(String.format(fmt.toString(), args));
	}
	
}
