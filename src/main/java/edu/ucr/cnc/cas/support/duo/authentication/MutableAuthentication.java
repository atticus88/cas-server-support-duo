package edu.ucr.cnc.cas.support.duo.authentication;

import java.util.Date;
import java.util.Map;
import java.util.List;
import org.jasig.cas.authentication.CredentialMetaData;
import org.jasig.cas.authentication.HandlerResult;
import java.util.HashMap;
import edu.ucr.cnc.cas.support.duo.authentication.AbstractAuthentication;
import org.jasig.cas.authentication.principal.Principal;

/**
 * Mutable implementation of Authentication interface.
 * <p>
 * Instanciators of the MutableAuthentication class must take care that the map
 * they provide is serializable (i.e. HashMap).
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.3
 */
public final class MutableAuthentication extends AbstractAuthentication {

    /** Unique Id for serialization. */
    private static final long serialVersionUID = -4415875344376642246L;

    /** The date/time this authentication object became valid. */
    private final Date authenticatedDate;

    public MutableAuthentication(final Principal principal) {
        this(principal, new Date());
    }
    
    public List<CredentialMetaData> getCredentials() {
	return null;
    }

    public Map<String,HandlerResult> getSuccesses() {
	return null;
    }

    public Map<String,Class<? extends Exception>> getFailures() {
	//return super.getFailures();
	return null;
    }

    public MutableAuthentication(final Principal principal, final Date date) {
        super(principal, new HashMap<String, Object>());
        this.authenticatedDate = date;
    }

    public Date getAuthenticatedDate() {
        return this.authenticatedDate;
    }
}
