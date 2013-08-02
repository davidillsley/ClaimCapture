package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G13CareProviderPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G13CareProviderPage.url, G13CareProviderPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
  def fillPageWith(theClaim: ClaimScenario) {
    fillAddress("#beenEmployed", theClaim.selectDynamic("EmploymentAddressCareProvider_"+iteration))
    fillInput("#postcode", theClaim.selectDynamic("EmploymentPostcodeCareProvider_"+iteration))
  }
}

object G13CareProviderPage {
  val title = "Care provider’s contact Details - Employment"
  val url  = "/employment/careProvider/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G13CareProviderPage(browser,previousPage,iteration)
}

trait G13CareProviderPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G13CareProviderPage buildPageWith(browser,iteration = 1)
}