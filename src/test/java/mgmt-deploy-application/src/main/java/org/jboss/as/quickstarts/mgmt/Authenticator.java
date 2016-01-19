package org.jboss.as.quickstarts.mgmt;

import javax.security.auth.callback.*;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import java.io.IOException;

/**
 * Red Hat GmbH - Global Professional Services (GPS)
 * <p/>
 * Author: <a href="mailto:jhoffmann@redhat.com">Juergen Hoffmann</a>
 * Date:   24.02.12
 * Time:   12:02
 */
public class Authenticator implements CallbackHandler
{
    String username = null;
    char[] password = null;

    public Authenticator(String username, String password)
    {
        this.username = username;
        this.password = password.toCharArray();
    }

    @Override
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
            }
            else if (current instanceof RealmChoiceCallback)
            {
                throw new UnsupportedCallbackException(current, "Realm choice not currently supported.");
            }
            else if (current instanceof NameCallback)
            {
                NameCallback ncb = (NameCallback) current;
                ncb.setName(username);
            }
            else if (current instanceof PasswordCallback)
            {
                PasswordCallback pcb = (PasswordCallback) current;
                pcb.setPassword(password);
            }
            else
            {
                throw new UnsupportedCallbackException(current);
            }

        }

    }
}
