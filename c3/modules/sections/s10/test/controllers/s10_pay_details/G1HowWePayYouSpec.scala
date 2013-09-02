package controllers.s10_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim

class G1HowWePayYouSpec extends Specification with Tags {
  "How we pay you" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = G1HowWePayYou.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "How would you like to be paid?" and "How often do you want to get paid?".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = G1HowWePayYou.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept customer gets paid by bank account or building society""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
                                 .withFormUrlEncodedBody("likeToPay" -> "01",
                                                         "paymentFrequency"->"fourweekly")

      val result = G1HowWePayYou.submit(request)
      redirectLocation(result) must beSome("/bank-building-society-details")
    }
  } section("unit", models.domain.PayDetails.id)
}