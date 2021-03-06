package controllers.s7_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G4AdditionalInformationSpec extends Specification with Tags {
  "Additional Information" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G4AdditionalInfo.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to all questions""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G4AdditionalInfo.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
                                 .withFormUrlEncodedBody("welshCommunication" -> "yes")

      val result = G4AdditionalInfo.submit(request)
      redirectLocation(result) must beSome("/consentAndDeclaration/submit")
    }
  } section "unit"
}