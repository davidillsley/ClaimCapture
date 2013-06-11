package controllers

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.claim.{YourDetails, Section, Claim}
import play.api.test.Helpers._

class AboutYouSpec extends Specification {
  "About you" should {
    "accept all initial mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
                      .withFormUrlEncodedBody(
                        "firstName" -> "Scooby",
                        "title" -> "Mr",
                        "surname" -> "Doo",
                        "nationality" -> "US",
                        "dateOfBirth" -> "Dunno",
                        "maritalStatus" -> "Single")

      val result = AboutYou.yourDetailsSubmit(request)
      redirectLocation(result) must beSome("/aboutyou/contactDetails")

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.claim.AboutYou.id).get

      section.form(YourDetails.id) must beLike {
        case Some(f: YourDetails) => f.firstName mustEqual "Scooby"
      }
    }

    "highlight missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
                      .withFormUrlEncodedBody("firstName" -> "Scooby")

      val result = AboutYou.yourDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST

      val claim = Cache.getAs[Claim](claimKey).get
      claim.section(models.claim.AboutYou.id) must beNone
    }
  }
}