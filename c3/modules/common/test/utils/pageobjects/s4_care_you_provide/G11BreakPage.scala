package utils.pageobjects.s4_care_you_provide

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G11BreakPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends ClaimPage(browser, G11BreakPage.url, G11BreakPage.title, previousPage, iteration) {
  declareDate("#start", "AboutTheCareYouProvideBreakStartDate_" + iteration)
  declareDate("#end", "AboutTheCareYouProvideBreakEndDate_" + iteration)
  declareTime("#start", "AboutTheCareYouProvideBreakStartTime_" + iteration)
  declareTime("#end", "AboutTheCareYouProvideBreakEndTime_" + iteration)
  declareSelect("#whereYou_location", "AboutTheCareYouProvideWhereWereYouDuringTheBreak_" + iteration)
  declareInput("#whereYou_location_other", "AboutTheCareYouProvideWhereWereYouDuringTheBreakOther_" + iteration)
  declareSelect("#wherePerson_location", "AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_" + iteration)
  declareInput("#wherePerson_location_other", "AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreakOther_" + iteration)
  declareYesNo("#medicalDuringBreak", "AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_" + iteration)

  /**
   * Called by submitPage of Page. A new G10 will be built with an incremented iteration number.
   * @return Incremented iteration number.
   */
  protected override def getNewIterationNumber = iteration + 1
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G11BreakPage {
  val title = "Break - About the care you provide".toLowerCase

  val url  = "/care-you-provide/break"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G11BreakPage(browser,previousPage,iteration)
}

/** The context for Specs tests */
trait G11BreakPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G11BreakPage (browser = browser, iteration = 1)
}