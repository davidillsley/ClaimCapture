package controllers.submission

import org.specs2.mutable.Specification
import models.domain._
import models.{Street, MultiLineAddress, DayMonthYear, LivingInUK}
import models.yesNo.{YesNo, YesNoWithText, YesNoWithDate}
import org.specs2.mock.Mockito

class ClaimSubmissionControllerSpec extends Specification with Mockito {
  val controller = new ClaimSubmissionController(mock[Submitter])

  val claim = Claim().update(Benefits("no"))
    .update(Hours("no"))
    .update(LivesInGB("no"))
    .update(Over16("no"))

  "Claim submission" should {
    "be flagged for completing sections too quickly e.g. a bot" in {
      controller.checkTimeToCompleteAllSections(claim, currentTime = 0) should beTrue
    }

    "be completed slow enough to be human" in {
      controller.checkTimeToCompleteAllSections(claim, currentTime = Long.MaxValue) should beFalse
    }

    "returns false given did not answer any honeyPot question groups" in {
      controller.honeyPot(Claim()) should beFalse
    }

    "returns false given TimeOutsideUK answered yes and honeyPot filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "yes", date = Some(DayMonthYear()), text = Some("some text"), goBack = Some(YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given TimeOutsideUK answered no and honeyPot not filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", date = None, text = None, goBack = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given TimeOutsideUK answered no and honeyPot date filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", date = Some(DayMonthYear()))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given TimeOutsideUK answered no and honeyPot text filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", text = Some("some text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given TimeOutsideUK answered no and honeyPot goBack filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", goBack = Some(YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given MoreAboutTheCare answered yes and honeyPot filled" in {
      val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given MoreAboutTheCare answered no and honeyPot not filled" in {
      val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "no", date = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given MoreAboutTheCare answered no and honeyPot filled" in {
      val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "no", date = Some(DayMonthYear()))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given NormalResidenceAndCurrentLocation answered no and honeyPot filled" in {
      val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "no", text = Some("some text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given NormalResidenceAndCurrentLocation answered yes and honeyPot not filled" in {
      val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "yes", text = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given NormalResidenceAndCurrentLocation answered yes and honeyPot filled" in {
      val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "yes", text = Some("some text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given NormalResidenceAndCurrentLocation honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given ChildcareExpenses honeyPot not filled (frequency other and text entered)" in {
      val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given ChildcareExpenses honeyPot filled (frequency not other and text entered)" in {
      val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given PersonYouCareForExpenses honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given PersonYouCareForExpenses honeyPot not filled (frequency other and text entered)" in {
      val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given PersonYouCareForExpenses honeyPot filled (frequency not other and text entered)" in {
      val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given ChildcareExpensesWhileAtWork honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given ChildcareExpensesWhileAtWork honeyPot not filled (frequency other and text entered)" in {
      val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given ChildcareExpensesWhileAtWork honeyPot filled (frequency not other and text entered)" in {
      val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given ExpensesWhileAtWork honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given ExpensesWhileAtWork honeyPot not filled (frequency other and text entered)" in {
      val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given ExpensesWhileAtWork honeyPot filled (frequency not other and text entered)" in {
      val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given PensionSchemes answered no and honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = None))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given PensionSchemes answered yes and honeyPot filled (frequency not other)" in {
      val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "yes", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None))))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given PensionSchemes answered no and honeyPot filled" in {
      val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given PensionSchemes honeyPot filled (frequency not other and text entered)" in {
      val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "yes", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given AboutOtherMoney answered yes and honeyPot filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("yes"), whoPaysYou = Some("some whoPaysYou")))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given AboutOtherMoney answered no and honeyPot not filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), whoPaysYou = None))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given AboutOtherMoney answered no and honeyPot whoPaysYou filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), whoPaysYou = Some("some whoPaysYou")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given AboutOtherMoney answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given AboutOtherMoney answered no and honeyPot howOften filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given StatutorySickPay answered yes and honeyPot filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "yes", howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given StatutorySickPay answered no and honeyPot not filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howMuch = None))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given StatutorySickPay answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given StatutorySickPay answered no and honeyPot howOften filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given StatutorySickPay answered no and honeyPot employersName filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersName = Some("some employersName")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given StatutorySickPay answered no and honeyPot employersAddress filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersAddress = Some(MultiLineAddress(Street(Some("some lineOne"))))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given StatutorySickPay answered no and honeyPot employersPostcode filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersPostcode = Some("some employersPostcode")))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given OtherStatutoryPay answered yes and honeyPot filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "yes", howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given OtherStatutoryPay answered no and honeyPot not filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howMuch = None))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given OtherStatutoryPay answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot howOften filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersName filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersName = Some("some employersName")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersAddress filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersAddress = Some(MultiLineAddress(Street(Some("some lineOne"))))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersPostcode filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersPostcode = Some("some employersPostcode")))
      controller.honeyPot(claim) should beTrue
    }
  }
}