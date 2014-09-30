package edu.ucr.cnc.cas.support.duo.authentication

import spock.lang.Specification
import org.jasig.cas.authentication.principal.SimplePrincipal
import org.jasig.cas.authentication.UsernamePasswordCredential
import edu.usf.cims.cas.support.duo.authentication.principal.DuoCredentials
import org.jasig.cas.authentication.AuthenticationBuilder
import edu.ucr.cnc.cas.support.duo.authentication.MutableAuthentication
import edu.ucr.cnc.cas.support.duo.CasConstants


class DuoAuthenticationMetaDataPopulatorTests extends Specification {
 
  def "Populate TwoFactor LOA in TGT"(){
    given:
      def credentials = new DuoCredentials()
      def principal = new SimplePrincipal("testUser")
      def mutableAuth = new AuthenticationBuilder(principal)

    when:
      def populator = new DuoAuthenticationMetaDataPopulator()
      populator.populateAttributes(mutableAuth, credentials)

    then:
      mutableAuth.getAttributes().get(CasConstants.LOA_ATTRIBUTE) == CasConstants.LOA_TF
  }

  def "Only populate the LOA if its a DuoCredential"(){
    given:
      def credentials = new UsernamePasswordCredential()
      def principal = new SimplePrincipal("testUser")
      def mutableAuth = new AuthenticationBuilder(principal)

    when:
      def populator = new DuoAuthenticationMetaDataPopulator()
      populator.populateAttributes(mutableAuth, credentials)

    then:
      mutableAuth.getAttributes().get(CasConstants.LOA_ATTRIBUTE) == null
  }
}
