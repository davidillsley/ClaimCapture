package models.domain

import models.DayMonthYear
import models.yesNo.YesNoWith2Text

case object SelfEmployment extends Section.Identifier {
  val id = "s8"
}

case object AboutSelfEmployment extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g1"
}

case class AboutSelfEmployment(areYouSelfEmployedNow: String = "",
                               whenDidYouStartThisJob: DayMonthYear = DayMonthYear(None, None, None),
                               whenDidTheJobFinish: Option[DayMonthYear] = None,
                               haveYouCeasedTrading: Option[String] = None,
                               natureOfYourBusiness: Option[String] = None) extends QuestionGroup(AboutSelfEmployment)

case object SelfEmploymentYourAccounts extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g2"
}

case class SelfEmploymentYourAccounts(whatWasOrIsYourTradingYearFrom: Option[DayMonthYear] = None,
                                      whatWasOrIsYourTradingYearTo: Option[DayMonthYear] = None,
                                      areIncomeOutgoingsProfitSimilarToTrading: Option[String] = None,
                                      tellUsWhyAndWhenTheChangeHappened: Option[String] = None) extends QuestionGroup(SelfEmploymentYourAccounts)

case object SelfEmploymentPensionsAndExpenses extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g4"
}

case class SelfEmploymentPensionsAndExpenses(pensionSchemeMapping: YesNoWith2Text = YesNoWith2Text(),
                                             doYouPayToLookAfterYourChildren: String = "",
                                             didYouPayToLookAfterThePersonYouCaredFor: String = "") extends QuestionGroup(SelfEmploymentPensionsAndExpenses)

case class ChildcareExpensesWhileAtWork(nameOfPerson: String = "",
                                        howMuchYouPay: String = "",
                                        howOftenPayChildCare: String = "",
                                        whatRelationIsToYou: String = "",
                                        relationToPartner: Option[String] = None,
                                        whatRelationIsTothePersonYouCareFor: String = "") extends QuestionGroup(ChildcareExpensesWhileAtWork)

case object ChildcareExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g5"
}

case class ExpensesWhileAtWork(nameOfPerson: String = "",
                               howMuchYouPay: String = "",
                               howOftenPayExpenses: String = "",
                               whatRelationIsToYou: String = "",
                               relationToPartner: Option[String] = None,
                               whatRelationIsTothePersonYouCareFor: String = "") extends QuestionGroup(ExpensesWhileAtWork)

case object ExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g7"
}