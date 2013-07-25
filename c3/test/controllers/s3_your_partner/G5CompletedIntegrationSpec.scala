package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import utils.pageobjects.s3_your_partner.G5CompletedPageContext

class G5CompletedIntegrationSpec extends Specification with Tags {

  "Your Partner" should {
    "be presented" in new WithBrowser with G5CompletedPageContext {
      page goToThePage()
      //browser.goTo("/yourPartner/completed")
      //titleMustEqual("Completion - Your Partner")
    }

    """be submitted to "care you provide" page.""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/yourPartner/completed")
      browser.submit("button[type='submit']")
      titleMustEqual("Their Personal Details - Care You Provide")
    }
    
    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      Formulate.moreAboutYourPartnerSeparated(browser)
      Formulate.personYouCareFor(browser)
      titleMustEqual("Completion - Your Partner")(Duration(5, TimeUnit.MINUTES))
      browser.find("div[class=completed] ul li").size() mustEqual 4
    }
  } section "integration"
}