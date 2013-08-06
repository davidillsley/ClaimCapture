package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain._
import models.domain.Section
import models.domain.Claim

class G3Over16Spec extends Specification with Tags {
  """Can you get Carer's Allowance""" should {
    "present the Are you aged 16 or over form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claimWithBenefit = Claim().update(Benefits(answer = true))
      val claimWithHours = claimWithBenefit.update(Hours(answer = true))
      Cache.set(claimKey, claimWithHours)

      val result = G2Hours.present(request)

      status(result) mustEqual OK

      val sectionIdentifier = Section.sectionIdentifier(Over16)
      val completedQuestionGroups = claimWithHours.completedQuestionGroups(sectionIdentifier)

      completedQuestionGroups(0) mustEqual Benefits(answer = true)
      completedQuestionGroups(1) mustEqual Hours(answer = true)
    }

    "acknowledge that carer is aged 16 or over" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      G3Over16.submit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Over16) must beLike { case Some(o: Over16) => o.answer must beTrue }
    }
  } section "unit"
}