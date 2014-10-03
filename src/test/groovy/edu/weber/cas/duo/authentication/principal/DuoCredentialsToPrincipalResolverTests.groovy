package edu.weber.cas.duo.authentication.principal

import edu.weber.cas.duo.authentication.principal.DuoCredentials
import edu.weber.cas.duo.authentication.principal.DuoCredentialsToPrincipalResolver

import spock.lang.Specification
import com.duosecurity.*
import org.jasig.cas.authentication.principal.Principal
import org.jasig.cas.authentication.Authentication
import org.jasig.cas.authentication.UsernamePasswordCredential

class DuoCredentialsToPrincipalResolverTests extends Specification {

  /**
  * Mock objects that will be used in all tests
  */
  def principal = Mock(Principal)
  def authentication = Mock(Authentication)

  def "Get principal from DuoCredentials"(){
    given:
      authentication.principal >> principal
      principal.id  >> "testUser"
      
      def duoCredentials = new DuoCredentials()
      duoCredentials.setFirstAuthentication(authentication)
      def c2p = new DuoCredentialsToPrincipalResolver()

    when:
      def principal = c2p.resolve(duoCredentials)

    then:
      principal.getId() == "testUser"
  }

  def "Get principal with attributes"(){
    given:
      authentication.principal >> principal
      principal.id  >> "testUser"
      principal.attributes >> [foo: "bar", yes: "no"]
     
      def duoCredentials = new DuoCredentials()
      duoCredentials.setFirstAuthentication(authentication)
      def c2p = new DuoCredentialsToPrincipalResolver()

    when:
      def principal = c2p.resolve(duoCredentials)

    then:
      principal.getId() == "testUser"
      principal.getAttributes() == [foo: "bar", yes: "no"]
  }

  def "Support DuoCredentials"(){
    given:
      def duoCredentials = new DuoCredentials()
      def c2p = new DuoCredentialsToPrincipalResolver()

    when:
      def result = c2p.supports(duoCredentials)

    then:
      result == true
  }

  def "Support ONLY DuoCredentials"(){
    given:
      def upCredentials = new UsernamePasswordCredential()
      def c2p = new DuoCredentialsToPrincipalResolver()

    when:
      def result = c2p.supports(upCredentials)

    then:
      result == false
  }
}
