package models.domain

import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}


case object Circumstances extends Section.Identifier {
  val id = "c1"
}

case class CircumstancesAboutYou(title: String = "",
                    firstName: String = "",
                    middleName: Option[String] = None,
                    lastName: String = "",
                    nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some(""), Some(""), Some(""), Some(""), Some("")),
                    dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)
                     ) extends QuestionGroup(CircumstancesAboutYou)

object CircumstancesAboutYou extends QuestionGroup.Identifier {
  val id = s"${Circumstances.id}.g1"
}

case class CircumstancesYourContactDetails(address: MultiLineAddress = MultiLineAddress(None, None, None),
                                           postcode: Option[String] = None,
                                           phoneNumber: Option[String] = None,
                                           mobileNumber: Option[String] = None
                                  ) extends QuestionGroup(CircumstancesYourContactDetails)

object CircumstancesYourContactDetails extends QuestionGroup.Identifier {
  val id = s"${Circumstances.id}.g2"
}

case class CircumstancesOtherInfo(change: String = "") extends QuestionGroup(CircumstancesOtherInfo)

object CircumstancesOtherInfo extends QuestionGroup.Identifier {
  val id = s"${Circumstances.id}.g2"
}


case class DetailsOfThePersonYouCareFor(title: String = "",
                                 firstName: String = "",
                                 middleName: Option[String] = None,
                                 lastName: String = "",
                                 nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some(""), Some(""), Some(""), Some(""), Some("")),
                                 dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)
                                  ) extends QuestionGroup(DetailsOfThePersonYouCareFor)

object DetailsOfThePersonYouCareFor extends QuestionGroup.Identifier {
  val id = s"${Circumstances.id}.g3"
}