package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G10ChildcareExpensesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G10ChildcareExpensesPage.url, G10ChildcareExpensesPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
  def fillPageWith(theClaim: ClaimScenario) {
    fillInput("#howMuchCostChildcare", theClaim.selectDynamic("EmploymentChildcareExpensesHowMuchYouPayfor_"+iteration))
    fillInput("#whoLooksAfterChildren", theClaim.selectDynamic("EmploymentNameOfthePersonWhoLooksAfterYourChild_"+iteration))
    fillInput("#relationToYou", theClaim.selectDynamic("EmploymentChildcareExpensesWhatRelationIsthePersontoYou_"+iteration))
    fillInput("#relationToPartner", theClaim.selectDynamic("EmploymentChildcareExpensesWhatRelationIsthePersontoYourPartner_"+iteration))
    /* missing EmploymentChildcareExpensesWhatRelationIsthePersonToThePersonYouCareFor */


  }
}

object G10ChildcareExpensesPage {
  val title = "Childcare expenses while you are at work - Employment"
  val url  = "/employment/childcareExpenses/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G10ChildcareExpensesPage(browser,previousPage,iteration)
}

trait G10ChildcareExpensesPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G10ChildcareExpensesPage buildPageWith(browser,iteration = 1)
}