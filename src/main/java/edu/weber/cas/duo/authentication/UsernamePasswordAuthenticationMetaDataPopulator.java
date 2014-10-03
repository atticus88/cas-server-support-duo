package edu.weber.cas.duo.authentication;

import edu.weber.cas.duo.config.CasConstants;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationMetaDataPopulator;
import org.jasig.cas.authentication.AuthenticationBuilder;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UsernamePasswordAuthenticationMetaDataPopulator is used to insert an attribute into a CAS TGT indicating that a
 * single credential was authenticated.
 *
 * @author Michael Kennedy <michael.kennedy@ucr.edu>
 * @version 1.0
 */
public class UsernamePasswordAuthenticationMetaDataPopulator implements AuthenticationMetaDataPopulator{

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernamePasswordAuthenticationMetaDataPopulator.class);

    /**
     * Returns an {@link Authentication} object with the added LOA_SF attributed appended.
     *
     * @param authentication
     * @param credentials
     * @return
     */
    @Override
    public void populateAttributes(AuthenticationBuilder authentication, Credential credentials) {

        // Only do anything if the credential being provided is of type UsernamePasswordCredentials
        if (credentials instanceof UsernamePasswordCredential) {

            authentication.getAttributes().put(CasConstants.LOA_ATTRIBUTE, CasConstants.LOA_SF);

            LOGGER.debug("Adding LOA of {} to Authentication object.", CasConstants.LOA_SF);

            //return mutableAuthentication;
        }

        //return authentication;
    }
}
