package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G8AboutExpensesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends ClaimPage(browser, G8AboutExpensesPage.url.replace(":jobID", iteration.toString), G8AboutExpensesPage.title, previousPage, iteration) {
  declareYesNo("#payForAnythingNecessary", "EmploymentDoYouPayforAnythingNecessaryToDoYourJob_" + iteration)
  declareYesNo("#payAnyoneToLookAfterChildren", "EmploymentDoYouPayAnyoneLookAfterYourChild_" + iteration)
  declareYesNo("#payAnyoneToLookAfterPerson", "EmploymentDoYouPayAnyonetoLookAfterPersonYouCareFor_" + iteration)
}

object G8AboutExpensesPage {
  val title = "About expenses to do with your employment - Employment History".toLowerCase

  val url  = "/employment/about-expenses/:jobID"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G8AboutExpensesPage(browser,previousPage,iteration)
}

trait G8AboutExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8AboutExpensesPage (browser,iteration = 1)
}