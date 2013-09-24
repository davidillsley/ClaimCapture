package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.yesNo.{OptYesNoWithText, YesNoWithText}
import play.test.WithApplication
import scala.Some

class DeclarationSpec extends Specification with Tags {

  "Declaration" should {

    "generate claimer full name in declaration text when" in {
      "first name and surname are present" in {
        val personalDetails = YourDetails(firstName = "firstName", surname="surname")
        val fullName = xml.Declaration.fullName(Claim().update(personalDetails).asInstanceOf[Claim])

        fullName mustEqual "firstName surname"
      }

      "first name, middle name and surname are present" in {
        val personalDetails = YourDetails(firstName = "firstName", middleName=Some("middleName"), surname="surname")
        val fullName = xml.Declaration.fullName(Claim().update(personalDetails).asInstanceOf[Claim])

        fullName mustEqual "firstName middleName surname"
      }

      "declaration and disclaimer are present" in {
        val personalDetails = YourDetails(firstName = "firstName", middleName=Some("middleName"), surname="surname")
        val consent = Consent(OptYesNoWithText(answer = Some("yes"), text = None),YesNoWithText(answer = "yes", text = None))
        val additionalInfo = AdditionalInfo()
        val claim = Claim() + personalDetails + consent + additionalInfo
        val x = xml.Declaration.xml(claim.asInstanceOf[Claim])
        x.text must contain("declaration.1.pdf")
        x.text must contain("disclaimer.7")
      }
    }
  } section "unit"
}