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
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;

/**
 * <p>Title: Authentication</p>
 * <p>Description: Container for authentication credentials</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.jboss.deployer.common.Authentication</code></p>
 */

public class Authentication {
//  private static final Authentication INSTANCE = new Authentication();

  private final CallbackHandler CALLBACK_HANDLER = new DemoCallbackHandler();
  private final Authenticator AUTHENTICATOR = new DemoAuthenticator();

  // After the demo has connected the physical connection may be re-established numerous times.
  // for this reason we cache the entered values to allow for re-use without pestering the end
  // user.
  private boolean promptShown = false;
  private String userName = null;
  private char[] password = null;

  Authentication(final String userName, final char[] password) {
  	this.userName = userName;
  	this.password = password;
  }

  public CallbackHandler getCallbackHandler()
  {
      return CALLBACK_HANDLER;
  }

  public Authenticator getAuthenticator()
  {
      return AUTHENTICATOR;
  }

  void prompt(final String realm)
  {
      if (promptShown == false)
      {
          promptShown = true;
          System.out.println("Authenticating against security realm: " + realm);
      }
  }

  String obtainUsername(final String prompt)
  {
      if (userName == null)
      {
          userName = System.console().readLine(prompt);
      }
      return userName;
  }

  char[] obtainPassword(final String prompt)
  {
      if (password == null)
      {
          password = System.console().readPassword(prompt);
      }

      return password;
  }

  class DemoCallbackHandler implements CallbackHandler
  {

      public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
      {

          // Special case for anonymous authentication to avoid prompting user for their name.
          if (callbacks.length == 1 && callbacks[0] instanceof NameCallback)
          {
              ((NameCallback) callbacks[0]).setName("anonymous demo user");
              return;
          }

          for (Callback current : callbacks)
          {
              if (current instanceof RealmCallback)
              {
                  RealmCallback rcb = (RealmCallback) current;
                  String defaultText = rcb.getDefaultText();
                  rcb.setText(defaultText); // For now just use the realm suggested.

                  prompt(defaultText);
              }
              else if (current instanceof RealmChoiceCallback)
              {
                  throw new UnsupportedCallbackException(current, "Realm choice not currently supported.");
              }
              else if (current instanceof NameCallback)
              {
                  NameCallback ncb = (NameCallback) current;
                  String userName = obtainUsername("Username:");

                  ncb.setName(userName);
              }
              else if (current instanceof PasswordCallback)
              {
                  PasswordCallback pcb = (PasswordCallback) current;
                  char[] password = obtainPassword("Password:");

                  pcb.setPassword(password);
              }
              else
              {
                  throw new UnsupportedCallbackException(current);
              }

          }
      }

  }

  class DemoAuthenticator extends Authenticator
  {
      @Override
      protected PasswordAuthentication getPasswordAuthentication()
      {
          prompt(getRequestingPrompt());
          String userName = obtainUsername("Username:");
          char[] password = obtainPassword("Password:");

          return new PasswordAuthentication(userName, password);
      }
  }

}
