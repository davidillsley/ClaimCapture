package controllers.circs.s3_consent_and_declaration

import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s2_additional_info._
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.circumstances.s3_consent_and_declaration.{G1DeclarationPageContext, G1DeclarationPage}
import utils.pageobjects.circumstances.s1_about_you.{G4CompletedPage, G3DetailsOfThePersonYouCareForPageContext}
import utils.pageobjects.TestData

class G1DeclarationIntegrationSpec extends Specification with Tags {

  "Declaration" should {
    val obtainInfoAgreement = "no"
    val obtainInfoWhy = "Cause I want"
    val confirm = "yes"

    "be presented" in new WithBrowser with G1DeclarationPageContext {
      page goToThePage()
    }

    "navigate to previous page" in new WithBrowser with G1OtherChangeInfoPageContext {
      page goToThePage()

      val claim = CircumstancesScenarioFactory.declaration
      val otherChangeInfoPage = page runClaimWith (claim, G1DeclarationPage.title)

      otherChangeInfoPage must beAnInstanceOf[G1DeclarationPage]

      val prevPage = otherChangeInfoPage.goBack()

      prevPage must beAnInstanceOf[G1OtherChangeInfoPage]
    }

    "navigate to next page" in new WithBrowser with G1DeclarationPageContext {
      pending("of finishing the circs submission result page")
      val claim = CircumstancesScenarioFactory.otherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }
    "contain errors on invalid submission" in {
      "missing obtainInfoAgreement field" in new WithBrowser with G1DeclarationPageContext {
        val claim = new TestData
        //claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
        claim.CircumstancesDeclarationWhy = obtainInfoWhy
        claim.CircumstancesDeclarationConfirmation = confirm

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Do you agree to us obtaining information from any other persons or organisations you may have listed on this form? - This is required")
      }

      "given obtainInfoAgreement is set to 'no' missing obtainInfoWhy field" in new WithBrowser with G1DeclarationPageContext {
        val claim = new TestData
        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
        claim.CircumstancesDeclarationConfirmation = confirm

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Please tell us why not - This is required")
      }

      "missing confirm field" in new WithBrowser with G1DeclarationPageContext {
        val claim = new TestData
        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
        claim.CircumstancesDeclarationWhy = obtainInfoWhy
        //claim.CircumstancesDeclarationConfirmation = confirm

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Please tick this box to confirm that you understand and make the declarations above. - This is required")
      }
    }
  } section("integration", models.domain.CircumstancesConsentAndDeclaration.id)

}