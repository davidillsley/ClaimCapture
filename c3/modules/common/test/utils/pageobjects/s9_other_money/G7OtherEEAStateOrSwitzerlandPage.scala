package utils.pageobjects.s9_other_money

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * PageObject pattern associated to S8 other money EEA pension and insurance.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class G7OtherEEAStateOrSwitzerlandPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G7OtherEEAStateOrSwitzerlandPage.url, G7OtherEEAStateOrSwitzerlandPage.title, previousPage) {
  declareYesNo("#benefitsFromOtherEEAStateOrSwitzerland","OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA")
  declareYesNo("#workingForOtherEEAStateOrSwitzerland", "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA")
}

object G7OtherEEAStateOrSwitzerlandPage {
  val title = "Other EEA State or Switzerland - About Other Money".toLowerCase

  val url = "/other-money/other-eea-state-or-switzerland"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G7OtherEEAStateOrSwitzerlandPage(browser, previousPage)

}

trait G7OtherEEAStateOrSwitzerlandPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7OtherEEAStateOrSwitzerlandPage buildPageWith browser
}