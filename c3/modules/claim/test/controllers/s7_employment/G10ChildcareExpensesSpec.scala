package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.view.CachedClaim
import app.StatutoryPaymentFrequency._

class G10ChildcareExpensesSpec extends Specification with Tags {
  "Childcare expenses while you are at work - Controller" should {
    val jobID = "Dummy job ID"
    val whoLooksAfterChildren = "myself"
    val howMuchYouPay = "123445"
    val howOften_frequency = Other
    val howOften_frequency_other = "Every day and twice on Sundays"
    val whatRelationIsToYou = "son"
    val whatRelationIsToThePersonYouCareFor = "parent"


    "present" in new WithApplication with Claiming {
      val aboutExpenses = mock[AboutExpenses]
      aboutExpenses.identifier returns AboutExpenses
      aboutExpenses.jobID returns jobID
      aboutExpenses.payAnyoneToLookAfterChildren returns "yes"

      val job = mock[Job]
      job.questionGroups returns aboutExpenses :: Nil

      val jobs = mock[Jobs]
      jobs.identifier returns Jobs
      jobs.jobs returns job :: Nil
      jobs.questionGroup(jobID, AboutExpenses) returns Some(aboutExpenses)

      val claim = Claim()().update(jobs)
      Cache.set(claimKey, claim)

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = G10ChildcareExpenses.present(jobID)(request)
      status(result) mustEqual OK
    }

    "require all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID)

      val result = G10ChildcareExpenses.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "accept all mandatory data." in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("jobID" -> jobID,
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchCostChildcare" -> howMuchYouPay,
        "howOftenPayChildCare.frequency" -> howOften_frequency,
        "howOftenPayChildCare.frequency.other" -> howOften_frequency_other,
        "relationToYou" -> whatRelationIsToYou,
        "relationToPersonYouCare" -> whatRelationIsToThePersonYouCareFor)

      val result = G10ChildcareExpenses.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "be added to a (current) job" in new WithApplication with Claiming {
      G2JobDetails.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> jobID,
        "employerName" -> "Toys r not us",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "yes"))

      val result = G10ChildcareExpenses.submit(FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("jobID" -> jobID,
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchCostChildcare" -> howMuchYouPay,
        "howOftenPayChildCare.frequency" -> howOften_frequency,
        "howOftenPayChildCare.frequency.other" -> howOften_frequency_other,
        "relationToYou" -> whatRelationIsToYou,
        "relationToPersonYouCare" -> whatRelationIsToThePersonYouCareFor))

      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == jobID) must beLike {
            case Some(j: Job) => j.questionGroups.size shouldEqual 2
          }
        }
      }
    }
  } section("unit", models.domain.Employed.id)
}