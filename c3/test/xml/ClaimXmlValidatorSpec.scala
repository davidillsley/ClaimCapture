package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import helpers.ClaimBuilder._
import play.api.test.WithApplication
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory
import scala.xml.Elem
import play.api.Logger

class ClaimXmlValidatorSpec extends Specification with Tags {

  private def updateClaim(claim: Claim) = {
    claim.update(aboutYou.yourDetails)
      .update(aboutYou.claimDate)
      .update(aboutYou.contactDetails)
      .update(aboutYou.timeOutsideUK.get)
      .update(aboutYou.moreAboutYou)
      .update(aboutYou.employment)
      .update(aboutYou.propertyAndRent)

      .update(yourPartner.yourPartnerPersonalDetails)
      .update(yourPartner.yourPartnerContactDetails)
      .update(yourPartner.moreAboutYourPartner)
      .update(yourPartner.personYouCareFor.get)
      
      .update(careYouProvide.theirPersonalDetails)
      .update(careYouProvide.theirContactDetails)
      .update(careYouProvide.moreAboutThePerson)
      .update(careYouProvide.representatives)
      .update(careYouProvide.previousCarerContactDetails.get)
      .update(careYouProvide.previousCarerPersonalDetails.get)
      .update(careYouProvide.moreAboutTheCare)
      .update(careYouProvide.oneWhoPays.get)
      .update(careYouProvide.contactDetailsPayingPerson.get)
      .update(careYouProvide.breaksInCare)

      .update(timeSpentAbroad.normalResidence)
      .update(timeSpentAbroad.trips)

      .update(education.yourCourseDetails)
      .update(education.addressOfSchool)

      .update(selfEmployment.aboutSelfEmployment)
      .update(selfEmployment.selfEmploymentYourAccounts)
      .update(selfEmployment.accountantContactDetails)
      .update(selfEmployment.pensionsAndExpenses)
      .update(selfEmployment.childcareExpenses)
      .update(selfEmployment.expensesWhileAtWork)

      .update(otherMoney.aboutOtherMoney)
      .update(otherMoney.moneyPaidToSomeoneElse)
      .update(otherMoney.personWhoGetsThisMoney)
      .update(otherMoney.personContactDetails)
      .update(otherMoney.statutorySickPay)
      .update(otherMoney.otherStatutoryPay)

      .update(payDetails.howWePayYou.get)
      .update(payDetails.bankBuildingSocietyDetails.get)

      .update(employmentJobs)

      .update(consentAndDeclaration.additionalInfo)
      .update(consentAndDeclaration.consent)
      .update(consentAndDeclaration.disclaimer)
      .update(consentAndDeclaration.declaration)
  }
  "Claim Submission" should {

    "validate a good claim" in new WithApplication {
      val claim = updateClaim(Claim())

      val claimXml = DWPCAClaim.xml(claim, "TY6TV9G")

      val fullXml = buildFullClaim(claimXml)

      val validator = XmlValidatorFactory.buildCaValidator()

      validator.validate(fullXml.buildString(stripComments = true)) must beTrue
    }

    "validate a bad claim" in new WithApplication {
      val claim = updateClaim(Claim())

      val claimXml = DWPCAClaim.xml(claim, "878786876786Y6TV9G")

      val fullXml = buildFullClaim(claimXml)

      val validator = XmlValidatorFactory.buildCaValidator()

      validator.validate(fullXml.buildString(stripComments = true)) must beFalse
    }
  } section "externalDependency"

  def buildFullClaim(claimXml:Elem) = {

    <DWPBody xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666"
                   xmlns="http://www.govtalk.gov.uk/dwp/ca/claim"
                   xmlns:gds="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"
                   xmlns:dc="http://purl.org/dc/elements/1.1/"
                   xmlns:dcq="http://purl.org/dc/terms/"
                   xmlns:gms="http://www.govtalk.gov.uk/CM/gms"
                   xmlns:dsig="http://www.w3.org/2000/09/xmldsig#"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.govtalk.gov.uk/dwp/ca/claim file:/Users/jmi/Temp/dwp-ca-claim-v1_10.xsd">
      <DWPEnvelope>
        <DWPCAHeader>
        <TestMessage>5</TestMessage>
        <Keys>
          <Key type="}~e"></Key>
          <Key type="Z}"></Key>
        </Keys>
        <Language>en</Language>
        <DefaultCurrency>GBP</DefaultCurrency>
        <Manifest>
          <Reference>
            <Namespace>http://PtqKCMVh/</Namespace>
            <SchemaVersion></SchemaVersion>
            <TopElementName>FZXic.rwPpxsw5wsX</TopElementName>
          </Reference>
          <Reference>
            <Namespace>http://jwJGvJlj/</Namespace>
            <SchemaVersion></SchemaVersion>
            <TopElementName>vaN1Eh5z61pekYlfOv-vP0sGy</TopElementName>
          </Reference>
        </Manifest>
        <TransactionId>{(claimXml \\ "DWPCAClaim" \ "@id").text}</TransactionId>
      </DWPCAHeader>
      {claimXml}
    </DWPEnvelope>
    </DWPBody>
  }
}