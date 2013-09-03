package app

import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, ClaimScenario, Page}

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 03/09/2013
 */
class FunctionalTestCase8Spec extends FunctionalTestCommon {
  isolated

  "The application " should {

    "Successfully run absolute Test Case 8 " in new WithBrowser with G1BenefitsPageContext {

      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase8.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)

      lastPage match {
        case p: XmlPage => {
          validateAndPrintErrors(p, claim) should beTrue
        }
        case p: Page => println(p.source())
      }
    }
  } section "functional"
}