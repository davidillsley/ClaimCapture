package models.domain

import models.{PensionPaymentFrequency, DayMonthYear}
import controllers.Mappings._
import models.PensionPaymentFrequency

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

  def validateHowMuchSelfEmployed(input: SelfEmploymentPensionsAndExpenses): Boolean = input.doYouPayToPensionScheme match {
    case `yes` => input.howMuchDidYouPay.isDefined
    case `no` => true
  }

  def validateHowOftenSelfEmployed(input: SelfEmploymentPensionsAndExpenses): Boolean = input.doYouPayToPensionScheme match {
    case `yes` => input.howOften.isDefined
    case `no` => true
  }
}

case class SelfEmploymentPensionsAndExpenses(doYouPayToPensionScheme: String = "",
                                             howMuchDidYouPay: Option[String] = None,
                                             howOften: Option[PensionPaymentFrequency] = None,
                                             doYouPayToLookAfterYourChildren: String = "",
                                             didYouPayToLookAfterThePersonYouCaredFor: String = "") extends QuestionGroup(SelfEmploymentPensionsAndExpenses)

case class ChildcareExpensesWhileAtWork(nameOfPerson: String = "",
                                        howMuchYouPay: String = "",
                                        howOftenPayChildCare: PensionPaymentFrequency = models.PensionPaymentFrequency(""),
                                        whatRelationIsToYou: String = "",
                                        relationToPartner: Option[String] = None,
                                        whatRelationIsTothePersonYouCareFor: String = "") extends QuestionGroup(ChildcareExpensesWhileAtWork)

case object ChildcareExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g5"
}

case class ExpensesWhileAtWork(nameOfPerson: String = "",
                               howMuchYouPay: String = "",
                               howOftenPayExpenses: PensionPaymentFrequency = models.PensionPaymentFrequency(""),
                               whatRelationIsToYou: String = "",
                               relationToPartner: Option[String] = None,
                               whatRelationIsTothePersonYouCareFor: String = "") extends QuestionGroup(ExpensesWhileAtWork)

case object ExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g7"
}