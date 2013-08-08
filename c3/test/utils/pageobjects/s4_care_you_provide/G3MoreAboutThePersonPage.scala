package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s4_care_you_provide g3_more_about_the_person.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G3MoreAboutThePersonPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3MoreAboutThePersonPage.url, G3MoreAboutThePersonPage.title, previousPage)  {
  
    declareSelect("#relationship","AboutTheCareYouProvideWhatTheirRelationshipToYou")
    declareYesNo("#armedForcesPayment", "AboutTheCareYouProvideDoesPersonGetArmedForcesIndependencePayment")
    declareYesNo("#claimedAllowanceBefore", "AboutTheCareYouProvideHasAnyoneelseClaimedCarerAllowance")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3MoreAboutThePersonPage {
  val title = "Relationship and other claims - About the care you provide"
  val url  = "/careYouProvide/relationshipAndOtherClaims"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3MoreAboutThePersonPage(browser,previousPage)
}

/** The context for Specs tests */
trait G3MoreAboutThePersonPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3MoreAboutThePersonPage buildPageWith browser
}
