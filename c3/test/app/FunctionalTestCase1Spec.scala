package app

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, ClaimScenario, Page}

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase1Spec extends FunctionalTestCommon {
  isolated

  "The application " should {
    "Successfully run absolute Test Case 1 " in new WithBrowser with G1BenefitsPageContext {

      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)

      lastPage match {
        case p: XmlPage => {
          validateAndPrintErrors(p, claim) should beTrue
        }
        case p: Page => println(p.source())
      }
    }

//    "Successfully run absolute Test Case 2 " in new WithBrowser with G1BenefitsPageContext {
//
//      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase2.csv")
//      page goToThePage(waitForPage = true, waitDuration = 500)
//      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
//
//      lastPage match {
//        case p: XmlPage => {
//          validateAndPrintErrors(p, claim) should beTrue
//        }
//        case p: Page => println(p.source())
//      }
//    }
//
//    "Successfully run absolute Test Case 3 " in new WithBrowser with G1BenefitsPageContext {
//
//      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase3.csv")
//      page goToThePage(waitForPage = true, waitDuration = 500)
//      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
//
//      lastPage match {
//        case p: XmlPage => {
//          validateAndPrintErrors(p, claim) should beTrue
//        }
//        case p: Page => println(p.source())
//      }
//    }
//
//    "Successfully run absolute Test Case 4 " in new WithBrowser with G1BenefitsPageContext {
//
//      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase4.csv")
//      page goToThePage(waitForPage = true, waitDuration = 500)
//      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
//
//      lastPage match {
//        case p: XmlPage => {
//          validateAndPrintErrors(p, claim) should beTrue
//        }
//        case p: Page => println(p.source())
//      }
//    }
//
//    "Successfully run absolute Test Case 5 " in new WithBrowser with G1BenefitsPageContext {
//
//      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase5.csv")
//      page goToThePage(waitForPage = true, waitDuration = 500)
//      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
//
//      lastPage match {
//        case p: XmlPage => {
//          validateAndPrintErrors(p, claim) should beTrue
//        }
//        case p: Page => println(p.source())
//      }
//    }
//
//    "Successfully run absolute Test Case 6 " in new WithBrowser with G1BenefitsPageContext {
//
//      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase6.csv")
//      page goToThePage(waitForPage = true, waitDuration = 500)
//      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
//
//      lastPage match {
//        case p: XmlPage => {
//          validateAndPrintErrors(p, claim) should beTrue
//        }
//        case p: Page => println(p.source())
//      }
//    }
//
//    "Successfully run absolute Test Case 7 " in new WithBrowser with G1BenefitsPageContext {
//
//      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase7.csv")
//      page goToThePage(waitForPage = true, waitDuration = 500)
//      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
//
//      lastPage match {
//        case p: XmlPage => {
//          validateAndPrintErrors(p, claim) should beTrue
//        }
//        case p: Page => println(p.source())
//      }
//    }
  } section "functional"
}