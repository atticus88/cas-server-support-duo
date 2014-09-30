package edu.usf.cims.cas.support.duo.authentication.handler;

import edu.ucr.cnc.cas.support.duo.DuoConfiguration;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.AbstractAuthenticationHandler;
import org.jasig.cas.authentication.AuthenticationHandler;
import org.jasig.cas.authentication.handler.UncategorizedAuthenticationException;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.Credential;
import edu.usf.cims.cas.support.duo.authentication.principal.DuoCredential;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.authentication.BasicCredentialMetaData;
import java.security.GeneralSecurityException;
import com.duosecurity.DuoWeb;
import javax.validation.constraints.NotNull;
import javax.security.auth.login.FailedLoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DuoAuthenticationHandler
 *
 * Bean that implements AuthenticationHandler that can be added to deployerConfigContext.xml to
 * perform authentications on {@link DuoCredentials}.
 *
 * @author  Eric Pierce <epierce@usf.edu>
 * @author  Michael Kennedy <michael.kennedy@ucr.edu>
 * @version 1.1
 *
 */

public final class DuoAuthenticationHandler implements AuthenticationHandler {
  
    private static final Logger LOGGER = LoggerFactory.getLogger(DuoAuthenticationHandler.class);
   
    @NotNull
    private DuoConfiguration duoConfiguration;

    @NotNull
    private String name = DuoAuthenticationHandler.class.getSimpleName();

    @Override
    public HandlerResult authenticate(final Credential credential) throws GeneralSecurityException {
        final DuoCredential c = (DuoCredential) credential;

	String duoVerifyResponse = DuoWeb.verifyResponse(this.duoConfiguration.getIntegrationKey(), this.duoConfiguration.getSecretKey(), this.duoConfiguration.getApplicationKey(), c.getSignedDuoResponse());
	
	final Principal principal = c.getPrincipal();
	
	if(duoVerifyResponse == null){
        	duoVerifyResponse = "";
        }

        LOGGER.debug("Response from Duo verify: [{}]", duoVerifyResponse);
	if(duoVerifyResponse.equals(c.getPrincipal().getId())){
        	return new HandlerResult(this, new BasicCredentialMetaData(c), principal);
	} else {
		throw new FailedLoginException("Duo Authentication Failed");
	}
    }

    @Override
    public boolean supports(final Credential credential) {
        return credential instanceof DuoCredential;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public DuoConfiguration getDuoConfiguration() {
        return duoConfiguration;
    }

    public void setDuoConfiguration(DuoConfiguration duoConfiguration) {
        this.duoConfiguration = duoConfiguration;
    }
}
