package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim
import scala.Some

class G2JobDetailsSpec extends Specification with Tags {
  "Details about your job" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G2JobDetails.present(request)
      status(result) mustEqual OK
    }

    "miss all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G2JobDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """submit only mandatory data to a "new employment".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody(
        "jobID" -> "1",
        "employerName" -> "Toys r not us",
        "finishedThisJob" -> "yes")

      val result = G2JobDetails.submit(request)
      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == "1") must beLike {
            case Some(j: Job) => j.questionGroups.head.asInstanceOf[JobDetails].employerName shouldEqual "Toys r not us"
          }
        }
      }
    }

    """submit all data to a "new employment".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody(
        "jobID" -> "1",
        "employerName" -> "Toys r not us",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "yes",
        "lastWorkDate.day" -> "1",
        "lastWorkDate.month" -> "1",
        "lastWorkDate.year" -> "2001",
        "p45LeavingDate.day" -> "1",
        "p45LeavingDate.month" -> "1",
        "p45LeavingDate.year" -> "2001",
        "hoursPerWeek" -> "75",
        "jobTitle" -> "Goblin",
        "payrollEmployeeNumber" -> "445566")

      val result = G2JobDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}