package utils.pageobjects.s9_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}


final class G3PersonWhoGetsThisMoneyPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3PersonWhoGetsThisMoneyPage.url, G3PersonWhoGetsThisMoneyPage.title, previousPage) {

    declareInput("#fullName", "OtherMoneyOtherPersonFullName")
    declareNino("#nationalInsuranceNumber", "OtherMoneyOtherPersonNINO")
    declareInput("#nameOfBenefit", "OtherMoneyOtherPersonBenefit")

}

object G3PersonWhoGetsThisMoneyPage {
  val title = "Person Who Gets This Money - Other Money"
  val url  = "/otherMoney/personWhoGetsThisMoney"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3PersonWhoGetsThisMoneyPage(browser,previousPage)
}

trait G3PersonWhoGetsThisMoneyPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3PersonWhoGetsThisMoneyPage buildPageWith browser
}
