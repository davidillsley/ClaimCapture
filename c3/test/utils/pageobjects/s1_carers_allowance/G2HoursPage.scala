package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects._
import utils.pageobjects.Page

/**
 * PageObject pattern associated to S1 carers allowance G2 Hours page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
final class G2HoursPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2HoursPage.url, G2HoursPage.title,previousPage) with WebSearchActions {

  /* temporary, until tested class is refactored and use new common components. */
  private val separator = "-"

  def clickChangeBenefitsDetails() = browser.click("div[class=completed] a")

  def isInHoursPage: Boolean = titleMatch()

  def isQ1Yes: Boolean = isSpecifiedSectionCompleted(0, "Q1", "Yes")

  def isQ1No: Boolean = isSpecifiedSectionCompleted(0, "Q1", "No")

  /**
   * Sub-class reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#q3", theClaim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring, separator)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G2HoursPage {
  val title = "Hours - Carer's Allowance"
  val url = "/allowance/hours"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2HoursPage(browser, previousPage)
}

/** The context for Specs tests */
trait G2HoursPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page =  G2HoursPage buildPageWith browser
}