package integration.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G10HasBreaks extends Specification with Tags {
  "Care you provide" should {
    """present "has breaks" """ in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.title() mustEqual "Has Breaks - Care You Provide"
    }

    """re-present "has breaks" upon missing mandatory data""" in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Has Breaks - Care You Provide"
    }

    """present "completed" when no more breaks are required""" in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.click("#answer_no")
      browser.submit("button[value='next']")
      browser.pageSource() must contain("Completed - Care You Provide")
    }

    "go back to contact details" in new WithBrowser {
      pending("Once 'Contact details' are done, this example must be written")
    }
  } section "integration"
}