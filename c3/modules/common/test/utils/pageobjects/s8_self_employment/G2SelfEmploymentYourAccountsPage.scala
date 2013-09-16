package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G2SelfEmploymentYourAccountsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G2SelfEmploymentYourAccountsPage.url, G2SelfEmploymentYourAccountsPage.title, previousPage) {
  declareDate("#whatWasOrIsYourTradingYearFrom", "SelfEmployedWhatWasIsYourTradingYearfrom")
  declareDate("#whatWasOrIsYourTradingYearTo", "SelfEmployedWhatWasIsYourTradingYearIs")
  declareYesNo("#areIncomeOutgoingsProfitSimilarToTrading", "SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent")
  declareInput("#tellUsWhyAndWhenTheChangeHappened", "SelfEmployedTellUsWhyandWhentheChangeHappened")
}

object G2SelfEmploymentYourAccountsPage {
  val title = "Your accounts - About self-employment".toLowerCase
  val url = "/self-employment/your-accounts"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2SelfEmploymentYourAccountsPage(browser, previousPage)
}

trait G2SelfEmploymentYourAccountsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2SelfEmploymentYourAccountsPage buildPageWith browser
}