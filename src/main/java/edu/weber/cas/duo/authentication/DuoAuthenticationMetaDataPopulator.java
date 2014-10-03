package edu.weber.cas.duo.authentication;

import edu.weber.cas.duo.authentication.principal.DuoCredentials;
import edu.weber.cas.duo.config.CasConstants;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationBuilder;
import org.jasig.cas.authentication.AuthenticationMetaDataPopulator;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DuoAuthenticationMetaDataPopulator implements the {@link AuthenticationMetaDataPopulator} interface and is responsible
 * for adding additional an additional attribute to CAS TGT that the authentication was done using a second factor, in this
 * case a Duo authentication.
 *
 * @author Michael Kennedy <michael.kennedy@ucr.edu>
 * @version 1.0
 */
public class DuoAuthenticationMetaDataPopulator implements AuthenticationMetaDataPopulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DuoAuthenticationMetaDataPopulator.class);

    /**
     * Returns an {@link Authentication} object with the added LOA_TF attributed appended.
     *
     * @param authentication
     * @param credentials
     * @return
     */

    
    @Override
    public void populateAttributes(AuthenticationBuilder authentication, Credential credentials) {

        // Only do anything if the credentials being passed is of type DuoCredentials
        if (credentials instanceof DuoCredentials) {
            authentication.getAttributes().put(CasConstants.LOA_ATTRIBUTE, CasConstants.LOA_TF);
            LOGGER.debug("adding LOA of {} to Authentication object.", CasConstants.LOA_TF);
        }
    }
}
