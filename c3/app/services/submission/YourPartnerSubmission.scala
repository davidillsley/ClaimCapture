package services.submission

import models.domain._

object YourPartnerSubmission {
  private def getQuestionGroup[T](claim: Claim, questionGroup: QuestionGroup) = {
    claim.questionGroup(questionGroup).asInstanceOf[Option[T]].get
  }
  private def questionGroup[T](claim: Claim, questionGroup: QuestionGroup) = {
    claim.questionGroup(questionGroup).asInstanceOf[Option[T]]
  }
  
  def buildYourPartner(claim: Claim) = {
    // TODO [SKW] Check which of the below are optional.
    val yourPartnerPersonalDetails = getQuestionGroup[YourPartnerPersonalDetails](claim, YourPartnerPersonalDetails)
    val yourPartnerContactDetails = getQuestionGroup[YourPartnerContactDetails](claim, YourPartnerContactDetails)
    val moreAboutYourPartner = getQuestionGroup[MoreAboutYourPartner](claim, MoreAboutYourPartner)
    val personYouCareFor = questionGroup[PersonYouCareFor](claim, PersonYouCareFor) // New to schema
    YourPartner(yourPartnerPersonalDetails, yourPartnerContactDetails, moreAboutYourPartner, personYouCareFor)
  }

  def buildYourPartner(yourPartner: YourPartner) = {
      <Partner> 
        <NationalityPartner>${yourPartner.yourPartnerPersonalDetails.nationality.getOrElse("")}</NationalityPartner>
        <Surname>${yourPartner.yourPartnerPersonalDetails.surname}</Surname>
        <OtherNames>${yourPartner.yourPartnerPersonalDetails.otherNames.getOrElse("")}</OtherNames>
        <OtherSurnames/>
        <Title>${yourPartner.yourPartnerPersonalDetails.title}</Title>
        <DateOfBirth>${yourPartner.yourPartnerPersonalDetails.dateOfBirth.toXmlString}</DateOfBirth>
        <NationalInsuranceNumber>${yourPartner.yourPartnerPersonalDetails.nationalInsuranceNumber.orNull}</NationalInsuranceNumber>
        <Address>
          <gds:Line>{if(yourPartner.yourPartnerContactDetails.address.isDefined) yourPartner.yourPartnerContactDetails.address.get.lineOne.getOrElse("") else ""}</gds:Line>
          <gds:Line>{if(yourPartner.yourPartnerContactDetails.address.isDefined)yourPartner.yourPartnerContactDetails.address.get.lineTwo.getOrElse("") else ""}</gds:Line>
          <gds:Line>{if(yourPartner.yourPartnerContactDetails.address.isDefined)yourPartner.yourPartnerContactDetails.address.get.lineThree.getOrElse("") else ""}</gds:Line>
          <gds:PostCode>{if(yourPartner.yourPartnerContactDetails.address.isDefined)yourPartner.yourPartnerContactDetails.postcode.getOrElse("") else ""}</gds:PostCode>
        </Address>
        <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>{if(yourPartner.moreAboutYourPartner.dateStartedLivingTogether.isDefined) "yes" else "no"}</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate>{yourPartner.moreAboutYourPartner.dateStartedLivingTogether.getOrElse("")}</JoinedHouseholdDate>
          <SeparatedFromPartner>{yourPartner.moreAboutYourPartner.separatedFromPartner}</SeparatedFromPartner>
          <SeparationDate>{if(yourPartner.moreAboutYourPartner.separatedFromPartner == "yes") yourPartner.moreAboutYourPartner.separationDate.getOrElse("") else ""}</SeparationDate>
        </RelationshipStatus>
      </Partner>
  }
}