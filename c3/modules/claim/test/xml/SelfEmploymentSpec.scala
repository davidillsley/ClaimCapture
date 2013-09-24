package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.{PensionPaymentFrequency, DayMonthYear}
import scala.Some
import app.XMLValues._
import app.PensionPaymentFrequency._

class SelfEmploymentSpec extends Specification with Tags {

  "SelfEmployment" should {

    val startDate = DayMonthYear(Some(1), Some(1), Some(2000))
    val endDate = DayMonthYear(Some(1), Some(1), Some(2005))
    val software = "software"
    val amount = "15.5"

    "generate xml when data is present" in {
      val aboutSelfEmployment = AboutSelfEmployment(areYouSelfEmployedNow = yes,
        whenDidYouStartThisJob = startDate,
        whenDidTheJobFinish = Some(endDate),
        haveYouCeasedTrading = Some(no),
        natureOfYourBusiness = Some(software)
      )

      val claim = Claim().update(models.domain.Employment(beenSelfEmployedSince1WeekBeforeClaim = yes))
        .update(aboutSelfEmployment).asInstanceOf[Claim]

      val selfEmploymentXml = xml.SelfEmployment.xml(claim)

      (selfEmploymentXml \\ "SelfEmployedNow").text mustEqual yes
      val recentJobDetailsXml = selfEmploymentXml \\ "CurrentJobDetails"
      (recentJobDetailsXml \\ "DateStarted").text mustEqual startDate.`yyyy-MM-dd`
      (recentJobDetailsXml \\ "NatureOfBusiness").text mustEqual software
    }

    "generate xml when data is missing" in {
      val claim = Claim().update(models.domain.Employment(beenSelfEmployedSince1WeekBeforeClaim = no)).asInstanceOf[Claim]
      val selfEmploymentXml = xml.SelfEmployment.xml(claim)
      selfEmploymentXml.text must beEmpty
    }

    "generate <PensionScheme> if claimer has paid for pension scheme" in {
      val pensionScheme = SelfEmploymentPensionsAndExpenses(doYouPayToPensionScheme = "yes",
      howMuchDidYouPay = Some(amount),
      howOften = Some(PensionPaymentFrequency(Weekly)),
      doYouPayToLookAfterYourChildren= "yes",
      didYouPayToLookAfterThePersonYouCaredFor = "yes")

      val claim = Claim().update(pensionScheme).asInstanceOf[Claim]

      val pensionSchemeXml = xml.SelfEmployment.pensionScheme(claim)

      (pensionSchemeXml \\ "Payment" \\ "Amount").text shouldEqual amount
      (pensionSchemeXml \\ "Frequency").text shouldEqual "02"
    }

    "skip <PensionScheme> if claimer has NO pension scheme" in {
      val pensionSchemeXml = xml.SelfEmployment.pensionScheme(Claim())

      pensionSchemeXml.text must beEmpty
    }

    "generate <ChildCareExpenses> if claimer pays anyone to care for children" in {

      val pensionScheme = SelfEmploymentPensionsAndExpenses(doYouPayToLookAfterYourChildren = yes)
      val childcareExpenses = ChildcareExpensesWhileAtWork(howMuchYouPay = amount, nameOfPerson = "Andy", whatRelationIsToYou = "grandSon", whatRelationIsTothePersonYouCareFor = "relation")
      val claim = Claim().update(pensionScheme).update(childcareExpenses).asInstanceOf[Claim]

      val childcareXml = xml.SelfEmployment.childCareExpenses(claim)
      (childcareXml \\ "CarerName").text shouldEqual childcareExpenses.nameOfPerson
      (childcareXml \\ "WeeklyPayment" \\ "Amount").text shouldEqual NotAsked
      (childcareXml \\ "RelationshipCarerToClaimant").text shouldEqual childcareExpenses.whatRelationIsToYou
      (childcareXml \\ "ChildDetails" \\ "RelationToChild").text shouldEqual childcareExpenses.whatRelationIsTothePersonYouCareFor
    }

    "skip <ChildCareExpenses> if claimer has NO childcare expenses" in {
      val pensionScheme = SelfEmploymentPensionsAndExpenses(doYouPayToLookAfterYourChildren = no)
      val claim = Claim().update(pensionScheme).asInstanceOf[Claim]
      val childcareXml = xml.SelfEmployment.childCareExpenses(claim)
      childcareXml.text must beEmpty
    }

    "generate <CareExpenses> if claimer has care expenses" in {
      val pensionScheme = SelfEmploymentPensionsAndExpenses(didYouPayToLookAfterThePersonYouCaredFor = yes)
      val grandSon = "grandSon"
      val expensesWhileAtWork: ExpensesWhileAtWork = ExpensesWhileAtWork(howMuchYouPay = amount, nameOfPerson = "NameOfPerson", whatRelationIsToYou = grandSon, whatRelationIsTothePersonYouCareFor = grandSon)
      val claim = Claim().update(pensionScheme).update(expensesWhileAtWork).asInstanceOf[Claim]

      val careExpensesXml = xml.SelfEmployment.careExpenses(claim)

      (careExpensesXml \\ "CarerName").text shouldEqual expensesWhileAtWork.nameOfPerson
      (careExpensesXml \\ "WeeklyPayment" \\ "Amount").text shouldEqual NotAsked
      (careExpensesXml \\ "RelationshipCarerToClaimant").text shouldEqual grandSon
      (careExpensesXml \\ "RelationshipCarerToCaree").text shouldEqual grandSon
    }

    "skip <CareExpenses> if claimer has NO care expenses" in {
      val pensionScheme = SelfEmploymentPensionsAndExpenses(didYouPayToLookAfterThePersonYouCaredFor = no)
      val claim = Claim().update(pensionScheme).asInstanceOf[Claim]

      val careExpensesXml = xml.SelfEmployment.careExpenses(claim)

      careExpensesXml.text must beEmpty
    }

  } section "unit"

}