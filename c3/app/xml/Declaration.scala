package xml

import models.domain._
import play.api.i18n.Messages
import XMLHelper.titleCase

object Declaration {

  def xml(claim: Claim) = {

    val additionalInfo = claim.questionGroup[AdditionalInfo].getOrElse(AdditionalInfo())
    val consent = claim.questionGroup[Consent].getOrElse(Consent())

    <Declaration>
      { (1 to 4).foreach(n => <TextLine>{ Messages(s"declaration.$n") }</TextLine>) }
      <TextLine>We may wish to contact any current or previous employers, or other persons or organisations you have listed on this claim form to obtain information about your claim. You do not have to agree to this but if you do not, it may mean that we are unable to obtain enough information to satisfy ourselves that you meet the conditions of entitlement for your claim.</TextLine>
      <TextLine>Do you agree to us getting information from any current or previous employer you have told us about as part of this claim? {titleCase(consent.informationFromEmployer.answer)}</TextLine>
      {
        consent.informationFromEmployer.answer match {
          case "yes" =>
          case "no" => <TextLine>If you answered No please tell us why</TextLine> <TextLine>{ consent.informationFromEmployer.text.orNull }</TextLine>
        }
      }
      <TextLine>Do you agree to us getting information from any other person or organisation you have told us about as part of this claim? {titleCase(consent.informationFromPerson.answer)}</TextLine>
      {
        consent.informationFromPerson.answer match {
          case "yes" =>
          case "no" => <TextLine>If you answered No please tell us why</TextLine> <TextLine>{ consent.informationFromPerson.text.orNull }</TextLine>
        }
      }
      <TextLine>This is my claim for Carer's Allowance.</TextLine>
      { (1 to 6).foreach(n => <TextLine>{ Messages(s"disclaimer.$n").replace("[[first name, middle name, surname]]", fullName(claim)) }</TextLine>) }
      <TextLine>Do you live in Wales and would like to receive future communications in Welsh? {titleCase(additionalInfo.welshCommunication)}</TextLine>
      <TextLine>If you want to view or print a full, printer-friendly version of the information you have entered on this claim, please use the buttons provided.</TextLine>
      <TextLine>Do not send us the printed version. This is for your personal records only.</TextLine>
    </Declaration>
  }

  def fullName(claim: Claim) = {
    val personalDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())

    personalDetails.middleName match {
      case Some(middleName) => personalDetails.firstName + " " + middleName + " " + personalDetails.surname
      case _ => personalDetails.firstName + " " + personalDetails.surname
    }
  }
}