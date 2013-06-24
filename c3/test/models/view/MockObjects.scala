package models.view

import models.domain._

object MockObjects {

  val sectionOne: Section = {
    val forms = List[QuestionGroup](Benefits(), Hours(), LivesInGB(), Over16())
    Section(CarersAllowance.id, forms)
  }

  val claim: Claim = {
    Claim(Map(CarersAllowance.id -> sectionOne))
  }
}