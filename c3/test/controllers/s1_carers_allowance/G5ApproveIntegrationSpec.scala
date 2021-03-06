package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G5ApproveIntegrationSpec extends Specification with Tags {

  "Approve" should {
    "be presented" in new WithBrowser {
      browser.goTo("/allowance/approve")
      browser.title mustEqual "Can you get Carer's Allowance?"
    }
  } section "integration"

  "Carer's Allowance" should {
    "be approved" in new WithBrowser with BrowserMatchers {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Hours - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Over 16 - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Lives in GB - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Can you get Carer's Allowance?")
      browser.find("div[class=prompt]").size mustEqual 1
      browser.find(".prompt.error").size mustEqual 0
    }

    "be declined" in new WithBrowser with BrowserMatchers {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Hours - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Over 16 - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Lives in GB - Carer's Allowance")

      browser.click("#q3-no")
      browser.submit("button[type='submit']")
      titleMustEqual("Can you get Carer's Allowance?")
      browser.find("div[class=prompt]").size mustEqual 0
      browser.find(".prompt.error").size mustEqual 1
    }

    "navigate to next section" in new WithBrowser with BrowserMatchers {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Hours - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Over 16 - Carer's Allowance")

      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Lives in GB - Carer's Allowance")

      browser.click("#q3-no")
      browser.submit("button[type='submit']")
      titleMustEqual("Can you get Carer's Allowance?")

      browser.submit("button[type='submit']")
      titleMustEqual("Your Details - About You")
    }
  } section "integration"
}