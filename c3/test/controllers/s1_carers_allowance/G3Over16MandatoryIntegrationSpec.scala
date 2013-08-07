package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s1_carers_allowance.G3Over16MandatoryPageContext
import utils.pageobjects.s1_carers_allowance.G3Over16MandatoryPage
import utils.pageobjects.s1_carers_allowance.G1BenefitsMandatoryPageContext
import utils.pageobjects.s1_carers_allowance.G4LivesInGBMandatoryPage

class G3Over16MandatoryIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with G3Over16MandatoryPageContext {
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G3Over16MandatoryPageContext {
        val claim = new ClaimScenario
        claim.CanYouGetCarersAllowanceAreYouAged16OrOver = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G3Over16MandatoryPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G3Over16MandatoryPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "yes"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G4LivesInGBMandatoryPage]
    }

    "contain the completed forms" in new WithBrowser with G1BenefitsMandatoryPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "no"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      page goToThePage()
      page fillPageWith claim
      val s1g2 = page submitPage() 
      
      s1g2 fillPageWith claim
      val s1g3 = s1g2 submitPage()

      s1g3 match {
        case p: G3Over16MandatoryPage => {
          p numberSectionsCompleted() mustEqual 2
          val completed = p.findTarget("div[class=completed] ul li")
          completed(0) must contain("Q1")
          completed(0) must contain("No")
          completed(1) must contain("Q2")
          completed(1) must contain("Yes")
        }
        case _ => ko("Next Page is not of the right type.")
      }
    }
  } section "integration"
}