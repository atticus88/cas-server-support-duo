package edu.ucr.cnc.cas.support.duo.authentication;

import edu.usf.cims.cas.support.duo.authentication.principal.DuoCredentials;
import edu.ucr.cnc.cas.support.duo.authentication.MutableAuthentication;
import edu.ucr.cnc.cas.support.duo.CasConstants;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationBuilder;
import org.jasig.cas.authentication.AuthenticationMetaDataPopulator;
//import org.jasig.cas.authentication.MutableAuthentication;
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
            Principal simplePrincipal = new SimplePrincipal(authentication.getPrincipal().getId(), authentication.getPrincipal().getAttributes());
            MutableAuthentication mutableAuthentication = new MutableAuthentication(simplePrincipal, authentication.getAuthenticationDate());

            // Add the LOA_TF attribute to the Authentication object
            //.getAttributes().putAll(authentication.getAttributes());
            authentication.getAttributes().put(CasConstants.LOA_ATTRIBUTE, CasConstants.LOA_TF);

            LOGGER.debug("adding LOA of {} to Authentication object for {}", CasConstants.LOA_TF, simplePrincipal.getId());

            //return mutableAuthentication;
        }

        //return authentication;
    }
}
