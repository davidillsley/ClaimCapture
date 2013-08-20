package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import play.api.test.WithBrowser
import org.fluentlenium.core.Fluent

class NavigationIntegrationSpec extends Specification with Tags {
  "About you" should {
    """navigate to
        "your details" then
        "contact details" and back to
        "your details".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with DataFiller {
      goTo("/about-you/your-details").title shouldEqual "Your details - About you - the carer"
      enter > `/about-you/your-details`

      next.title shouldEqual "Your contact details - About you - the carer"
      back.title shouldEqual "Your details - About you - the carer"
    }

    """navigate to
        "your details" then
        "contact details" then
        "time outside UK" then back to
        "contact details" and back to
        "your details".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with DataFiller {
      goTo("/about-you/your-details").title shouldEqual "Your details - About you - the carer"
      enter > `/about-you/your-details` click "#alwaysLivedUK_no"

      next.title shouldEqual "Your contact details - About you - the carer"
      enter > `/about-you/contact-details`

      next.title shouldEqual "About your time outside the UK - About you - the carer"
      back.title shouldEqual "Your contact details - About you - the carer"
      back.title shouldEqual "Your details - About you - the carer"
    }

    """navigate to
        "your details" then
        "contact details" then
        "claim date" (i.e. skipping "time outside UK") then back to
        "contact details" and back to
        "your details" then "time outside UK" going to
        "contact details" then
        "time outside UK" and finally back to
        "contact details".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with DataFiller {
      goTo("/about-you/your-details").title shouldEqual "Your details - About you - the carer"
      enter > `/about-you/your-details`

      next.title shouldEqual "Your contact details - About you - the carer"
      enter > `/about-you/contact-details`

      next.title shouldEqual "Your Claim Date - About you - the carer"
      back.title shouldEqual "Your contact details - About you - the carer"
      back.title shouldEqual "Your details - About you - the carer"

      enter > `/about-you/your-details` click "#alwaysLivedUK_no"

      next.title shouldEqual "Your contact details - About you - the carer"
      enter > `/about-you/contact-details`

      next.title shouldEqual "About your time outside the UK - About you - the carer"
      back.title shouldEqual "Your contact details - About you - the carer"
    }
  } section "integration"
}

trait DataFiller {
  this: WithBrowserHelper =>

  def `/about-you/your-details`: Fluent = {
    click("#title option[value='mr']")
    fill("#firstName") `with` "Scooby"
    fill("#surname") `with` "Doo"
    fill("#nationalInsuranceNumber_ni1") `with` "AB"
    fill("#nationalInsuranceNumber_ni2") `with` "12"
    fill("#nationalInsuranceNumber_ni3") `with` "34"
    fill("#nationalInsuranceNumber_ni4") `with` "56"
    fill("#nationalInsuranceNumber_ni5") `with` "C"
    click("#dateOfBirth_day option[value='3']")
    click("#dateOfBirth_month option[value='4']")
    fill("#dateOfBirth_year") `with` "1950"
    fill("#nationality") `with` "British"
    click("#maritalStatus option[value='s']")
    click("#alwaysLivedUK_yes")
  }

  def `/about-you/contact-details`: Fluent = {
    fill("#address_lineOne") `with` "My Address"
    fill("#postcode") `with` "SE1 6EH"
    fluent
  }
}