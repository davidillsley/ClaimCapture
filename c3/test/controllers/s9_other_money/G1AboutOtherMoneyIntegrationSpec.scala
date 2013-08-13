package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import controllers.{BrowserMatchers, Formulate, ClaimScenarioFactory}
import play.api.test.WithBrowser
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPageContext

class G1AboutOtherMoneyIntegrationSpec extends Specification with Tags {
  "About Other Money" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/other-money/about-other-money")
      titleMustEqual("Details about other money - About Other Money")
    }

    "navigate back to Completion - Self Employment" in new WithBrowser with BrowserMatchers {
      browser.goTo("/other-money/about-other-money")
      browser.click("#backButton")
      titleMustEqual("Completion - Self Employment")
    }

    "contain errors on invalid submission" in {
      "no fields selected" in new WithBrowser with BrowserMatchers {
        browser.goTo("/other-money/about-other-money")
        browser.submit("button[type='submit']")
        titleMustEqual("Details about other money - About Other Money")

        findMustEqualSize("div[class=validation-summary] ol li", 1)
      }

      "text field enabled but not filled in" in new WithBrowser with BrowserMatchers {
        browser.goTo("/other-money/about-other-money")
        browser.click("#yourBenefits_answer_yes")
        browser.submit("button[type='submit']")
        titleMustEqual("Details about other money - About Other Money")

        findMustEqualSize("div[class=validation-summary] ol li", 1)
      }

      "text enabled but neither not filled in" in new WithBrowser with BrowserMatchers {
        Formulate.claimDate(browser)
        Formulate.moreAboutYou(browser)
        browser.goTo("/other-money/about-other-money")
        browser.click("#yourBenefits_answer_yes")
        browser.submit("button[type='submit']")
        titleMustEqual("Details about other money - About Other Money")

        findMustEqualSize("div[class=validation-summary] ol li", 1)
      }
    }

    "navigate to next page on valid submission with all text fields enabled and filled in" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutYou(browser)
      Formulate.aboutOtherMoney(browser)
      titleMustEqual("Money paid to someone else for you - About Other Money")
    }

    "navigate to next page on valid submission with first mandatory field set to no" in new WithBrowser with BrowserMatchers {
      browser.goTo("/other-money/about-other-money")
      browser.click("#yourBenefits_answer_no")
      browser.submit("button[type='submit']")
      titleMustEqual("Money paid to someone else for you - About Other Money")
    }

    "be presented" in new WithBrowser with G1AboutOtherMoneyPageContext {
      page goToThePage()
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with G1AboutOtherMoneyPageContext {
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 1
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
  } section("integration", models.domain.OtherMoney.id)
}