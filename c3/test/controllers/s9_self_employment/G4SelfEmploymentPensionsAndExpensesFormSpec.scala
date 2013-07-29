package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}


class G4SelfEmploymentPensionsAndExpensesFormSpec extends Specification with Tags {

  "About Self Employment - Pensions and Expenses Form" should {

    "map data into case class" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("doYouPayToPensionScheme" -> "yes",
          "howMuchDidYouPay" -> "11",
          "doYouPayToLookAfterYourChildren" -> "yes",
          "isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes",
          "isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.doYouPayToPensionScheme must equalTo("yes")
        }
      )
    }

    "reject if doYouPayToPensionScheme is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "howMuchDidYouPay" -> "11",
          "doYouPayToLookAfterYourChildren" -> "yes",
          "isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes",
          "isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if howMuchDidYouPay is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "doYouPayToPensionScheme" -> "yes",
          "doYouPayToLookAfterYourChildren" -> "yes",
          "isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes",
          "isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if doYouPayToLookAfterYourChildren is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "doYouPayToPensionScheme" -> "yes",
          "howMuchDidYouPay" -> "11",
          "isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "didYouPayToLookAfterThePersonYouCaredFor" -> "yes",
          "isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if didYouPayToLookAfterThePersonYouCaredFor is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "doYouPayToPensionScheme" -> "yes",
          "howMuchDidYouPay" -> "11",
          "isItTheSameExpenseWhileAtWorkForChildren" -> "yes",
          "doYouPayToLookAfterYourChildren" -> "yes",
          "isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

  }

}