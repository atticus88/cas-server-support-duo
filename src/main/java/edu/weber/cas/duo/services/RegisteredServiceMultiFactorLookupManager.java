package edu.weber.cas.duo.services;

import edu.weber.cas.duo.services.ServiceMultiFactorLookupManager;
import edu.weber.cas.duo.services.MultiFactorAttributeValidator;
import edu.weber.cas.duo.services.DefaultMultiFactorAttributeValidator;

import net.unicon.cas.addons.serviceregistry.RegisteredServiceWithAttributes;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.authentication.principal.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

/**
 * An implementation of {@link ServiceMultiFactorLookupManager} that uses an attribute in the JSON service
 * registry to decide if Multi-factor Authentication (MFA) is required.
 *
 * There are three possible values for the attribute:
 *   * 'ALL' - Require all users accessing this service to use MFA
 *   * 'NONE' - Do not require MFA for users accessing this service
 *   * 'USER_ATTRIBUTE' - Check user attributes to determine if MFA is required
 *
 * For backwards-compatibility, if no value is found, MFA is assumed to NOT be required.
 *
 * @author Eric Pierce
 * @author Michael Kennedy
 * @version 1.2
 *
 */
public class RegisteredServiceMultiFactorLookupManager implements ServiceMultiFactorLookupManager {

    private String mfaRequiredKey = "casMFARequired";
    private String mfaRequiredAttributesKey = "casMFAUserAttributes";
    private MultiFactorAttributeValidator mfaValidator = new DefaultMultiFactorAttributeValidator();

    private static final String REQUIRE_ALL = "ALL";
    private static final String REQUIRE_NONE = "NONE";
    private static final String REQUIRE_ATTRIBUTE = "CHECK_ATTRIBUTE";

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisteredServiceMultiFactorLookupManager.class);

    @Override
    public boolean getMFARequired(RegisteredService registeredService, Principal principal) {
        if (registeredService instanceof RegisteredServiceWithAttributes) {
            RegisteredServiceWithAttributes registeredServiceWithAttributes = (RegisteredServiceWithAttributes)registeredService;
            String result = (String) registeredServiceWithAttributes.getExtraAttributes().get(this.mfaRequiredKey);

            if(result == null){
              LOGGER.debug("No MultiFactor requirement found for service {}", registeredServiceWithAttributes.getServiceId());
              return false;
            }

            LOGGER.debug("Check MultiFactor requirement for service {} returned: {}", registeredServiceWithAttributes.getServiceId(), result);
            if (result.equalsIgnoreCase(REQUIRE_NONE)) {
                return false;
            } else if (result.equalsIgnoreCase(REQUIRE_ALL)) {
                return true;
            } else if (result.equalsIgnoreCase(REQUIRE_ATTRIBUTE)) {
                //Compare the user's attributes to the list from the service registry
                Map mfaAttributeMap = getMfaRequiredAttributeMap(registeredService);
                Map userAttributeMap = principal.getAttributes();
                LOGGER.debug("MultiFactor required for service {} with attributes [{}]. {} has attributes [{}]", registeredServiceWithAttributes.getServiceId(), mfaAttributeMap.toString(), principal.getId(), userAttributeMap.toString());
                return this.mfaValidator.check(mfaAttributeMap, userAttributeMap);
            } else {
                LOGGER.warn("MultiFactor check for service {} returned unhandled results: {}", registeredServiceWithAttributes.getServiceId(), result);
                return false;
            }
        }
        return false;
    }

    private Map getMfaRequiredAttributeMap(RegisteredService registeredService) {
        if (registeredService instanceof RegisteredServiceWithAttributes) {
            RegisteredServiceWithAttributes registeredServiceWithAttributes = (RegisteredServiceWithAttributes)registeredService;
            return (Map) registeredServiceWithAttributes.getExtraAttributes().get(this.mfaRequiredAttributesKey);
        }

        return null;
    }

    public String getMfaRequiredKey() {
        return mfaRequiredKey;
    }

    public void setMfaRequiredKey(String mfaRequiredKey) {
        this.mfaRequiredKey = mfaRequiredKey;
    }

    public String getMfaRequiredAttributesKey() {
        return mfaRequiredAttributesKey;
    }

    public void setMfaRequiredAttributesKey(String mfaRequiredAttributesKey) {
        this.mfaRequiredAttributesKey = mfaRequiredAttributesKey;
    }

    public void setMultiFactorAttributeValidator(MultiFactorAttributeValidator validator){
        this.mfaValidator = validator;
    }

    public MultiFactorAttributeValidator getMultiFactorAttributeValidator() {
        return this.mfaValidator;
    }
}
