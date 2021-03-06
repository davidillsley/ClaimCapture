package models.domain

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

class BreaksInCareSpec extends Specification with Mockito {
  "Breaks in care" should {
    "give zero breaks upon deleting from no existing breaks in care" in {
      val breaksInCare = BreaksInCare()

      val updatedBreaksInCare = breaksInCare delete "non existing break ID"
      updatedBreaksInCare.breaks.size mustEqual 0
    }

    "give zero breaks upon deleting the only break" in {
      val break = mock[Break]
      break.id returns "breakID"
      val breaksInCare = BreaksInCare(List(break))
      breaksInCare.breaks.size mustEqual 1

      val updatedBreaksInCare = breaksInCare delete break.id
      updatedBreaksInCare.breaks.size mustEqual 0
    }

    "give 2 breaks upon deleting 2nd out of 3 breaks" in {
      val break1 = mock[Break]
      break1.id returns "break1ID"

      val break2 = mock[Break]
      break2.id returns "break2ID"

      val break3 = mock[Break]
      break3.id returns "break3ID"

      val breaksInCare = BreaksInCare(List(break1, break2, break3))
      breaksInCare.breaks.size mustEqual 3

      val updatedBreaksInCare = breaksInCare delete break2.id
      updatedBreaksInCare.breaks.size mustEqual 2
    }
  }
}