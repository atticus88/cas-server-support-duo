package edu.weber.cas.duo.authentication

import edu.weber.cas.duo.authentication.principal.DuoCredentials
import edu.weber.cas.duo.config.CasConstants

import spock.lang.Specification
import org.jasig.cas.authentication.principal.SimplePrincipal
import org.jasig.cas.authentication.UsernamePasswordCredential
import org.jasig.cas.authentication.AuthenticationBuilder

class UsernamePasswordAuthenticationMetaDataPopulatorTests extends Specification {

  def "Populate Single Factor LOA in TGT"(){
    given:
      def credentials = new UsernamePasswordCredential()
      def principal = new SimplePrincipal("testUser")
      def mutableAuth = new AuthenticationBuilder(principal)

    when:
      def populator = new UsernamePasswordAuthenticationMetaDataPopulator()
      def finalAuth = populator.populateAttributes(mutableAuth, credentials)

    then:
      mutableAuth.getAttributes().get(CasConstants.LOA_ATTRIBUTE) == CasConstants.LOA_SF
  }

  def "Only populate Single Factor LOA if its a UsernamePasswordCredentials"(){
    given:
      def credentials = new DuoCredentials()
      def principal = new SimplePrincipal("testUser")
      def mutableAuth = new AuthenticationBuilder(principal)

    when:
      def populator = new UsernamePasswordAuthenticationMetaDataPopulator()
      def finalAuth = populator.populateAttributes(mutableAuth, credentials)

    then:
      mutableAuth.getAttributes().get(CasConstants.LOA_ATTRIBUTE) == null
  }
}
