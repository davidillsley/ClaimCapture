package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment._
import utils.pageobjects.s2_about_you.{G4ClaimDatePage, G7PropertyAndRentPage, G4ClaimDatePageContext}
import controllers.ClaimScenarioFactory
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s3_your_partner.G4PersonYouCareForPage


class G5ChildcareExpensesWhileAtWorkIntegrationSpec extends Specification with Tags {

  "Self Employment Child Care expenses" should {
    "be presented" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(true)

      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage()
      page runClaimWith (claim, G7PropertyAndRentPage.title, waitForPage = true)

      val nextPage = page goToPage( throwException = false, page = new G5ChildcareExpensesWhileAtWorkPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {

        val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
        val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
        pagePensionAndExpenses goToThePage()
        pagePensionAndExpenses fillPageWith claimPensionAndExpenses
        pagePensionAndExpenses.submitPage(true)


        val claim = new ClaimScenario

        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 5
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {


      val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      val pageClaimDate = new G4ClaimDatePage(browser)
      pageClaimDate goToThePage()
      pageClaimDate fillPageWith claimDate
      val pageMoreAboutYou = pageClaimDate.submitPage(true)
      pageMoreAboutYou fillPageWith claimDate
      pageMoreAboutYou.submitPage(true)


      var claimAboutYourPartner = new ClaimScenario
      claimAboutYourPartner.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "no"
      val pageAboutYourPartner = new G4PersonYouCareForPage(browser)
      pageAboutYourPartner goToThePage()
      pageAboutYourPartner fillPageWith claimAboutYourPartner
      pageAboutYourPartner.submitPage(true)

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(true)


      val claim = ClaimScenarioFactory.s9SelfEmploymentChildCareExpenses
      page goToThePage()
      page fillPageWith claim
      page submitPage(true)
    }


    "navigate to next page on valid submission" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {


      val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      val pageClaimDate = new G4ClaimDatePage(browser)
      pageClaimDate goToThePage()
      pageClaimDate fillPageWith claimDate
      val pageMoreAboutYou = pageClaimDate.submitPage(true)
      pageMoreAboutYou fillPageWith claimDate
      pageMoreAboutYou.submitPage(true)


      var claimAboutYourPartner = new ClaimScenario
      claimAboutYourPartner.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "no"
      val pageAboutYourPartner = new G4PersonYouCareForPage(browser)
      pageAboutYourPartner goToThePage()
      pageAboutYourPartner fillPageWith claimAboutYourPartner
      pageAboutYourPartner.submitPage(true)

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(true)


      val claim = ClaimScenarioFactory.s9SelfEmploymentChildCareExpenses
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G7ExpensesWhileAtWorkPage])
    }
  }
}