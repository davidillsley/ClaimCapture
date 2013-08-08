package models.domain

import models.DayMonthYear
import models.MultiLineAddress
import models.yesNo.YesNoWithText

case object SelfEmployment extends Section.Identifier {
  val id = "s8"
}

case object AboutSelfEmployment extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g1"
}

case class AboutSelfEmployment(areYouSelfEmployedNow: String = "",
                               whenDidYouStartThisJob: Option[DayMonthYear] = None,
                               whenDidTheJobFinish: Option[DayMonthYear] = None,
                               haveYouCeasedTrading: Option[String] = None,
                               natureOfYourBusiness: Option[String] = None) extends QuestionGroup(AboutSelfEmployment)

case object SelfEmploymentYourAccounts extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g2"
}

case class SelfEmploymentYourAccounts(whatWasOrIsYourTradingYearFrom: Option[DayMonthYear] = None,
                                      whatWasOrIsYourTradingYearTo: Option[DayMonthYear] = None,
                                      areAccountsPreparedOnCashFlowBasis: String = "",
                                      areIncomeOutgoingsProfitSimilarToTrading: Option[String] = None,
                                      tellUsWhyAndWhenTheChangeHappened: Option[String] = None,
                                      doYouHaveAnAccountant: Option[String] = None,
                                      canWeContactYourAccountant: Option[String] = None) extends QuestionGroup(SelfEmploymentYourAccounts)

case class SelfEmploymentAccountantContactDetails(accountantsName: String = "",
                                                  address: MultiLineAddress = MultiLineAddress(None, None, None),
                                                  postcode: Option[String] = None,
                                                  telephoneNumber: Option[String] = None,
                                                  faxNumber: Option[String] = None) extends QuestionGroup(SelfEmploymentAccountantContactDetails)

case object SelfEmploymentAccountantContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g3"
}

case object SelfEmploymentPensionsAndExpenses extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g4"
}

case class SelfEmploymentPensionsAndExpenses(pensionSchemeMapping: YesNoWithText = YesNoWithText(answer="", text=None),
                                             doYouPayToLookAfterYourChildren: String = "",
                                             didYouPayToLookAfterThePersonYouCaredFor: String = "") extends QuestionGroup(SelfEmploymentPensionsAndExpenses)

case class ChildcareExpensesWhileAtWork(howMuchYouPay: String = "",
                                        nameOfPerson: String = "",
                                        whatRelationIsToYou: String = "",
                                        relationToPartner: Option[String] = None,
                                        whatRelationIsTothePersonYouCareFor: String = "") extends QuestionGroup(ChildcareExpensesWhileAtWork)

case object ChildcareExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g5"
}

case class ChildcareProvidersContactDetails(address: Option[MultiLineAddress] = None,
                                            postcode: Option[String] = None) extends QuestionGroup(ChildcareProvidersContactDetails)

case object ChildcareProvidersContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g6"
}

case class ExpensesWhileAtWork(howMuchYouPay: String = "",
                               nameOfPerson: String = "",
                               whatRelationIsToYou: String = "",
                               whatRelationIsTothePersonYouCareFor: String = "") extends QuestionGroup(ExpensesWhileAtWork)

case object ExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g7"
}

case class CareProvidersContactDetails(address: Option[MultiLineAddress] = None,
                                       postcode: Option[String] = None) extends QuestionGroup(CareProvidersContactDetails)

case object CareProvidersContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g8"
}