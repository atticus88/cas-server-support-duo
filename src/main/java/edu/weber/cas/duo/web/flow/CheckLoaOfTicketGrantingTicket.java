package edu.weber.cas.duo.web.flow;

import edu.weber.cas.duo.config.CasConstants;
import edu.weber.cas.duo.services.ServiceMultiFactorLookupManager;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CheckLoadOfTicketGrantingTicket is a Spring MVC {@link AbstractAction} that looks at the requirements of
 * the service the user is attempting to login to, as well as the users requirements to use MFA or not and either:
 *
 * 1. Requires a reauthentication using two factors if the original TGT was acquired using a single factor and the
 * service requires two factors.
 * 2. Determines that the LOA of the TGT is sufficient and continues the login webflow.
 *
 * @author Michael Kennedy <michael.kennedy@ucr.edu>
 * @version 1.1
 */
public class CheckLoaOfTicketGrantingTicket extends AbstractAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckLoaOfTicketGrantingTicket.class);

    @NotNull
    private ServiceMultiFactorLookupManager serviceMultiFactorLookupManager;

    @NotNull
    private ServicesManager servicesManager;

    @NotNull
    private TicketRegistry ticketRegistry;

    @Override
    protected Event doExecute(RequestContext context) throws Exception {
        LOGGER.debug("Checking the LOA of a TGT");

        // Get the TGT id from the flow scope and retrieve the actual TGT from the ticket registry
        String ticketGrantingTicketId = (String)context.getFlowScope().get("ticketGrantingTicketId");
        TicketGrantingTicket ticketGrantingTicket;
        ticketGrantingTicket = (TicketGrantingTicket) this.ticketRegistry.getTicket(ticketGrantingTicketId, TicketGrantingTicket.class);

        // If there isn't a matching TGT in the registry let the user continue
        if (ticketGrantingTicket == null) {
            LOGGER.debug("No TGT found for TGT ID [{}]", ticketGrantingTicketId);
            return result("continue");
        }

        // Get the registered service from flow scope
        Service service = (Service)context.getFlowScope().get("service");
        RegisteredService registeredService = this.servicesManager.findServiceBy(service);
        boolean serviceMultiFactorRequired = this.serviceMultiFactorLookupManager.getMFARequired(registeredService, ticketGrantingTicket.getAuthentication().getPrincipal());

        // Get the LOA of the current TGT
        String tgtLOA = (String)ticketGrantingTicket.getAuthentication().getAttributes().get(CasConstants.LOA_ATTRIBUTE);

        //Assume tickets without LOA are single-factor
        if (tgtLOA == null){
          tgtLOA = CasConstants.LOA_SF;
        }

        LOGGER.debug("LOA of TGT {} is set to {}", ticketGrantingTicketId, tgtLOA);

        // Should the user be required to reauthenticate?
        if((serviceMultiFactorRequired) && (!tgtLOA.equals(CasConstants.LOA_TF))) {
            return result("renewForTwoFactor");
        }

        return result("continue");
    }

    public ServiceMultiFactorLookupManager getServiceMultiFactorLookupManager() {
        return serviceMultiFactorLookupManager;
    }

    /**
     * Sets the object that should lookup the multi factor mechanism used
     *
     * @param serviceMultiFactorLookupManager a ServiceMultiFactorLookupManager object
     */
    public void setServiceMultiFactorLookupManager(ServiceMultiFactorLookupManager serviceMultiFactorLookupManager) {
        this.serviceMultiFactorLookupManager = serviceMultiFactorLookupManager;
    }

    public ServicesManager getServicesManager() {
        return servicesManager;
    }

    public void setServicesManager(ServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }

    public TicketRegistry getTicketRegistry() {
        return ticketRegistry;
    }

    public void setTicketRegistry(TicketRegistry ticketRegistry) {
        this.ticketRegistry = ticketRegistry;
    }
}
