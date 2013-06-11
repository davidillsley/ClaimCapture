package integration

import play.api.test.WithBrowser
import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

class NavigationSpec extends Specification with Tags {

  "Browser" should {
    "not cache pages" in new WithBrowser {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("input[type='submit']")

      browser.click("#q3-yes")
      browser.submit("input[type='submit']")

      browser.click("#q3-yes")
      browser.submit("input[type='submit']")

      browser.title() mustEqual "Over 16 - Carer's Allowance"

      browser.webDriver.navigate().back()
      browser.webDriver.navigate().back()
      browser.webDriver.navigate().back()

      browser.title() mustEqual "Benefits - Carer's Allowance"

      browser.click("#q3-no")
      browser.submit("input[type='submit']")

      val completed = browser.find("div[class=completed] ul li")
      completed.size() mustEqual 1
      completed.getText must contain("Q1")
    }
  } section "integration"
}