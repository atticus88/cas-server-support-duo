package edu.usf.cims.cas.support.duo.authentication.principal;

import com.duosecurity.DuoWeb;
import org.jasig.cas.authentication.Credential;
import edu.ucr.cnc.cas.support.duo.DuoConfiguration;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import javax.validation.constraints.NotNull;

/**
 * DuoCredentialsToPrincipalResolver is responsible for converting a {@link DuoCredentials} to
 * a CAS {@link Principal}. This is used by the authentication process after an authentication succeeds.
 *
 * This creates a new {@link SimplePrincipal} and copies all of the attribute data from the {@link Principal} used 
 * during the initial authentication.
 *
 * @author Eric Pierce <epierce@usf.edu>
 * @author Michael Kennedy <michael.kennedy@ucr.edu>
 * @version 1.1
 */
public class DuoCredentialsToPrincipalResolver implements PrincipalResolver {

    SimplePrincipal simplePrincipal;

    /**
     * Returns a {@link Principal} based on a {@link DuoCredentials} credential.
     *
     * @param credentials a DuoCredentials object
     * @return a {@link Principal} based on a {@link DuoCredentials} credential
     */
    @Override
    public Principal resolve(Credential credentials) {
        final DuoCredentials duoCredentials = (DuoCredentials)credentials;

        if (duoCredentials.getPrincipal().getAttributes() != null) {
          simplePrincipal = new SimplePrincipal(duoCredentials.getPrincipal().getId(),
                duoCredentials.getPrincipal().getAttributes());
        } else {
          simplePrincipal = new SimplePrincipal(duoCredentials.getPrincipal().getId());
        }

        return simplePrincipal;
    }

    /**
     * Determines whether a particular credential is supported by this {@link CredentialsToPrincipalResolver}. This
     * only supports {@link DuoCredentials}.
     *
     * @param credentials any {@link Credentials} object
     * @return a boolean indicating whether it is supported
     */
    @Override
    public boolean supports(Credential credentials) {
        return (credentials.getClass() == DuoCredentials.class);
    }
}
