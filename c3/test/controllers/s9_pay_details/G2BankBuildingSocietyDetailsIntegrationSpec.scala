package controllers.s9_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2BankBuildingSocietyDetailsIntegrationSpec extends Specification with Tags {
  "Bank building society details" should {
    "be presented" in new WithBrowser {
      browser.goTo("/payDetails/bankBuildingSocietyDetails")
      browser.title mustEqual "Bank Building Society Details - Pay Details"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/payDetails/bankBuildingSocietyDetails")
      browser.title mustEqual "Bank Building Society Details - Pay Details"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 5
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.bankBuildingSocietyDetails(browser)
      browser.title mustEqual "Completion - Pay Details"
    }

    "navigate back to How We Pay You - Pay Details" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      titleMustEqual("Bank Building Society Details - Pay Details")

      browser.goTo("/payDetails/bankBuildingSocietyDetails")
      browser.click("#backButton")
      browser.title mustEqual "How We Pay You - Pay Details"
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.howWePayYou(browser)
      Formulate.bankBuildingSocietyDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
  } section "integration"
}