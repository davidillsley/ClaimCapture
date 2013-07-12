package controllers

import play.api.test.TestBrowser
import java.util.concurrent.TimeUnit

object Formulate {
  val partnerAddress = "Partner Address"

  val partnerPostcode = "RM11 1AA"
    
  def yourDetails(browser: TestBrowser) = {
    browser.goTo("/aboutyou/yourDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.fill("#nationality") `with` "English"
    browser.click("#maritalStatus option[value='s']")
    browser.click("#alwaysLivedUK_yes")
    browser.submit("button[type='submit']")
  }

  def yourDetailsEnablingTimeOutsideUK(browser: TestBrowser) = {
    browser.goTo("/aboutyou/yourDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.fill("#nationality") `with` "English"
    browser.click("#maritalStatus option[value='s']")
    browser.click("#alwaysLivedUK_no")
    browser.submit("button[type='submit']")
  }

  def yourContactDetails(browser: TestBrowser) = {
    browser.goTo("/aboutyou/contactDetails")
    browser.fill("#address_lineOne") `with` "My Address"
    browser.fill("#postcode") `with` "SE1 6EH"
    browser.submit("button[type='submit']")
  }

  def timeOutsideUK(browser: TestBrowser) = {
    browser.goTo("/aboutyou/timeOutsideUK")
    browser.click("#livingInUK_answer_yes")

    browser.click("#livingInUK_arrivalDate_day option[value='1']")
    browser.click("#livingInUK_arrivalDate_month option[value='1']")
    browser.fill("#livingInUK_arrivalDate_year") `with` "2001"

    browser.click("#livingInUK_goBack_answer_no")

    browser.submit("button[value='next']")
  }

  def timeOutsideUKNotLivingInUK(browser: TestBrowser) = {
    browser.goTo("/aboutyou/timeOutsideUK")
    browser.click("#livingInUK_answer_no")
    browser.submit("button[value='next']")
  }

  def claimDate(browser: TestBrowser) = {
    browser.goTo("/aboutyou/claimDate")
    browser.click("#dateOfClaim_day option[value='3']")
    browser.click("#dateOfClaim_month option[value='4']")
    browser.fill("#dateOfClaim_year") `with` "1950"
    browser.submit("button[type='submit']")
  }

  def moreAboutYou(browser: TestBrowser) = {
    browser.goTo("/aboutyou/moreAboutYou")
    browser.click("#hadPartnerSinceClaimDate_yes")
    browser.click("#eitherClaimedBenefitSinceClaimDate_yes")
    browser.click("#beenInEducationSinceClaimDate_yes")
    browser.click("#receiveStatePension_yes")
    browser.submit("button[type='submit']")
  }

  def moreAboutYouNotHadPartnerSinceClaimDate(browser: TestBrowser) = {
    browser.goTo("/aboutyou/moreAboutYou")
    browser.click("#hadPartnerSinceClaimDate_no")
    browser.click("#eitherClaimedBenefitSinceClaimDate_yes")
    browser.click("#beenInEducationSinceClaimDate_yes")
    browser.click("#receiveStatePension_yes")
    browser.submit("button[type='submit']")
  }

  def employment(browser: TestBrowser) = {
    browser.goTo("/aboutyou/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
    browser.submit("button[type='submit']")
  }

  def propertyAndRent(browser: TestBrowser) = {
    browser.goTo("/aboutyou/propertyAndRent")
    browser.click("#ownProperty_yes")
    browser.click("#hasSublet_yes")
    browser.submit("button[type='submit']")
  }

  // Your partner
  def yourPartnerPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/yourPartner/personalDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#middleName") `with` "Dave"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#otherNames") `with` "Roberts"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#liveAtSameAddress_yes]")
    browser.submit("button[type='submit']")
  }

  def yourPartnerPersonalDetailsNotLiveAtSameAddress(browser: TestBrowser) = {
    browser.goTo("/yourPartner/personalDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#middleName") `with` "Dave"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#otherNames") `with` "Roberts"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#liveAtSameAddress_no]")
    browser.submit("button[type='submit']")
  }

  def yourPartnerContactDetails(browser: TestBrowser) = {
    browser.goTo("/yourPartner/contactDetails")
    browser.fill("#address_lineOne") `with` partnerAddress
    browser.fill("#postcode") `with` partnerPostcode
    browser.submit("button[type='submit']")
  }
  
  def moreAboutYourPartnerSeparated(browser: TestBrowser) = {
    browser.goTo("/yourPartner/moreAboutYourPartner")
    browser.click("#startedLivingTogether_afterClaimDate_yes")
    browser.click("#startedLivingTogether_date_day option[value='3']")
    browser.click("#startedLivingTogether_date_month option[value='4']")
    browser.fill("#startedLivingTogether_date_year") `with` "1950"
    browser.click("#separated_fromPartner_yes]")
    browser.click("#separated_date_day option[value='3']")
    browser.click("#separated_date_month option[value='8']")
    browser.fill("#separated_date_year") `with` "2005"
    browser.submit("button[type='submit']")
  }
  
  def moreAboutYourPartnerNotSeparated(browser: TestBrowser) = {
    browser.goTo("/yourPartner/moreAboutYourPartner")
    browser.click("#startedLivingTogether_afterClaimDate_yes")
    browser.click("#startedLivingTogether_date_day option[value='3']")
    browser.click("#startedLivingTogether_date_month option[value='4']")
    browser.fill("#startedLivingTogether_date_year") `with` "1950"
    browser.click("#separated_fromPartner_no]")
    browser.submit("button[type='submit']")
  }

  def personYouCareFor(browser: TestBrowser) = {
    browser.goTo("/yourPartner/personYouCareFor")
    browser.click("#isPartnerPersonYouCareFor_yes]")
    browser.submit("button[type='submit']")
  }
  
  def personYouCareForNotPartner(browser: TestBrowser) = {
    browser.goTo("/yourPartner/personYouCareFor")
    browser.click("#isPartnerPersonYouCareFor_no]")
    browser.submit("button[type='submit']")
  }
  
  def yourPartnerCompleted(browser: TestBrowser) = {
    browser.goTo("/yourPartner/completed")
    browser.submit("button[type='submit']")
  }
    
  // Care You Provide
  def theirPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/theirPersonalDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#liveAtSameAddress_yes]")
    browser.submit("button[type='submit']")
  }

  def theirPersonalDetailsNotLiveAtSameAddress(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/theirPersonalDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#liveAtSameAddress_no]")
    browser.submit("button[type='submit']")
  }

  def theirContactDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/theirContactDetails")
    browser.fill("#address_lineOne") `with` "Their Address"
    browser.fill("#postcode") `with` "RM11 1DA"
    browser.submit("button[type='submit']")
  }

  def moreAboutTheCare(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutTheCare")
    browser.click("#spent35HoursCaring_yes")

    browser.click("#beforeClaimCaring_answer_yes")
    browser.await().atMost(30, TimeUnit.SECONDS).until("#beforeClaimCaring_date_year").areDisplayed
    browser.click("#beforeClaimCaring_date_day option[value='3']")
    browser.click("#beforeClaimCaring_date_month option[value='4']")
    browser.fill("#beforeClaimCaring_date_year") `with` "1950"

    browser.click("#hasSomeonePaidYou_yes")

    browser.submit("button[type='submit']")
  }

  def moreAboutTheCareWithNotPaying(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutTheCare")
    browser.click("#spent35HoursCaring_yes")
    browser.click("#spent35HoursCaringBeforeClaim_yes")
    browser.click("#hasSomeonePaidYou_no")
    browser.click("#careStartDate_day option[value='3']")
    browser.click("#careStartDate_month option[value='4']")
    browser.fill("#careStartDate_year") `with` "1950"
    browser.submit("button[type='submit']")
  }
  
  def moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutTheCare")
    browser.click("#spent35HoursCaring_yes")
    browser.click("#spent35HoursCaringBeforeClaim_no")
    browser.click("#hasSomeonePaidYou_yes")
    browser.submit("button[type='submit']")
  }

  def moreAboutThePersonWithClaimedAllowanceBefore(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutThePerson")
    browser.click("#relationship option[value='father']")
    browser.click("#armedForcesPayment_yes")
    browser.click("#claimedAllowanceBefore_yes")
    browser.submit("button[type='submit']")
  }

  def moreAboutThePersonWithNotClaimedAllowanceBefore(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutThePerson")
    browser.click("#relationship option[value='father']")
    browser.click("#armedForcesPayment_no")
    browser.click("#claimedAllowanceBefore_no")
    browser.submit("button[type='submit']")
  }

  def previousCarerPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/previousCarerPersonalDetails")
    browser.fill("#firstName") `with` "John"
    browser.fill("#middleName") `with` "Joe"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.submit("button[type='submit']")
  }

  def previousCarerContactDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/previousCarerContactDetails")
    browser.fill("#address_lineOne") `with` "My Address"
    browser.fill("#postcode") `with` "SE1 6EH"
    browser.submit("button[type='submit']")
  }
  
  def representativesForThePerson(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/representativesForPerson")
    browser.click("#you_actForPerson_yes")
    browser.click("#someoneElse_actForPerson_yes")
    browser.click("#you_actAs option[value='guardian']")
    browser.click("#someoneElse_actAs option[value='judicial']")
    browser.fill("#someoneElse_fullName") `with` "John"
    browser.submit("button[type='submit']")
  }
  
  def representativesForThePersonNegativeAnswers(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/representativesForPerson")
    browser.click("#actForPerson_no")
    browser.click("#someoneElseActForPerson_no")
    browser.submit("button[type='submit']")
  }

  def howWePayYou(browser: TestBrowser) = {
    browser.goTo("/payDetails/howWePayYou")
    browser.click("#likeToPay_01")
    browser.click("#paymentFrequency option[value='fourWeekly']")
    browser.submit("button[type='submit']")
  }

  def bankBuildingSocietyDetails(browser: TestBrowser) = {
    browser.goTo("/payDetails/bankBuildingSocietyDetails")
    browser.fill("#accountHolderName") `with` "holder name"
    browser.fill("#bankFullName") `with` "bank name"
    browser.fill("#sortCode_sort1") `with` "10"
    browser.fill("#sortCode_sort2") `with` "11"
    browser.fill("#sortCode_sort3") `with` "12"
    browser.fill("#accountNumber") `with` "account"
    browser.fill("#rollOrReferenceNumber") `with` "1234567"
    browser.submit("button[type='submit']")
  }

  def consent(browser: TestBrowser) = {
    browser.goTo("/consentAndDeclaration/consent")
    browser.click("#informationFromEmployer_yes")
    browser.fill("#why") `with` "some reason"
    browser.click("#informationFromPerson_yes")
    browser.submit("button[type='submit']")
  }

  def disclaimer(browser: TestBrowser) = {
    browser.goTo("/consentAndDeclaration/disclaimer")
    browser.click("#read")
    browser.submit("button[type='submit']")
  }

  def declaration(browser: TestBrowser) = {
    browser.goTo("/consentAndDeclaration/declaration")
    browser.click("#read")
    browser.submit("button[type='submit']")
  }

  def additionalInfo(browser: TestBrowser) = {
    browser.goTo("/consentAndDeclaration/additionalInfo")
    browser.click("#welshCommunication_yes")
    browser.submit("button[type='submit']")
  }
}