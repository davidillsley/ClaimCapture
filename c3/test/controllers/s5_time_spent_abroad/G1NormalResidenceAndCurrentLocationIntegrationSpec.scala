package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G1NormalResidenceAndCurrentLocationIntegrationSpec extends Specification with Tags {
  "Normal residence and current location" should {
    "present" in new WithBrowser {
      browser.goTo("/timeSpentAbroad/normalResidenceAndCurrentLocation")
      browser.title mustEqual "Normal Residence and Current Location - Time Spent Abroad"

      /* Does not work - Why?
      browser.await().atMost(30, TimeUnit.SECONDS).until("#live").isPresent()
      browser.await().atMost(30, TimeUnit.SECONDS).untilPage().isLoaded()

      browser.findFirst("#live").isDisplayed should beFalse */
    }

    """give 2 errors when missing mandatory data:
       "Do you live in the UK?"
       "Are you in Great Britain now?".""" in new WithBrowser {
      browser.goTo("/timeSpentAbroad/normalResidenceAndCurrentLocation")
      browser.title mustEqual "Normal Residence and Current Location - Time Spent Abroad"

      browser.submit("button[value='next']")
      browser.title mustEqual "Normal Residence and Current Location - Time Spent Abroad"
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    """give 2 errors when missing mandatory including optional to mandatory data:
       "Where do you normally live?"
       "Are you in Great Britain now?".""" in new WithBrowser {
      browser.goTo("/timeSpentAbroad/normalResidenceAndCurrentLocation")
      browser.title mustEqual "Normal Residence and Current Location - Time Spent Abroad"

      browser.click("#liveInUK_answer_no")
      browser.submit("button[value='next']")
      browser.title mustEqual "Normal Residence and Current Location - Time Spent Abroad"
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }
  } section "integration"
}