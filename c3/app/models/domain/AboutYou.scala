package models.domain

import models.{LivingInUK, NationalInsuranceNumber, MultiLineAddress, DayMonthYear}
import play.api.mvc.Call

object AboutYou extends Section.Identifier {
  val id = "s2"
}

case class YourDetails(call: Call = NoRouting,
                       title: String = "",
                       firstName: String = "",
                       middleName: Option[String] = None,
                       surname: String = "",
                       otherSurnames: Option[String] = None,
                       nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                       nationality: String = "",
                       dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                       maritalStatus: String = "",
                       alwaysLivedUK: String = "") extends QuestionGroup(YourDetails) {
  def otherNames = firstName + (middleName match {
    case Some(m: String) => s" $m"
    case _ => ""
  })
}

object YourDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(call: Call,
                          address: MultiLineAddress, postcode: Option[String],
                          phoneNumber: Option[String], mobileNumber: Option[String]) extends QuestionGroup(ContactDetails)

object ContactDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g2"
}

case class TimeOutsideUK(call: Call, livingInUK: LivingInUK, visaReference: Option[String]) extends QuestionGroup(TimeOutsideUK)

object TimeOutsideUK extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g3"
}

case class ClaimDate(call: Call, dateOfClaim: DayMonthYear) extends QuestionGroup(ClaimDate)

object ClaimDate extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g4"
}

case class MoreAboutYou(call: Call,
                        hadPartnerSinceClaimDate: String, eitherClaimedBenefitSinceClaimDate: String,
                        beenInEducationSinceClaimDate: String, receiveStatePension: String) extends QuestionGroup(MoreAboutYou)

object MoreAboutYou extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g5"
}

case class Employment(call: Call,
                      beenSelfEmployedSince1WeekBeforeClaim: String, beenEmployedSince6MonthsBeforeClaim: String) extends QuestionGroup(Employment)

object Employment extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g6"
}

case class PropertyAndRent(call: Call, ownProperty: String, hasSublet: String) extends QuestionGroup(PropertyAndRent)

object PropertyAndRent extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g7"
}