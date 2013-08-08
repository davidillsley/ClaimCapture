package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain._
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s8_self_employment.SelfEmployment.whenSectionVisible
import scala.Some

object G5ChildcareExpensesWhileAtWork extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(ChildcareExpensesWhileAtWork)

  def form(implicit claim: Claim) = Form(
    mapping(
      call(routes.G5ChildcareExpensesWhileAtWork.present()),
      "howMuchYouPay" -> nonEmptyText(maxLength = 8).verifying(validDecimalNumber),
      "whoLooksAfterChildren" -> nonEmptyText(maxLength = sixty),
      "whatRelationIsToYou" -> nonEmptyText(maxLength = sixty),
      "relationToPartner" -> optional(nonEmptyText(maxLength = sixty)),
      "whatRelationIsTothePersonYouCareFor" -> nonEmptyText(maxLength = sixty)
    )(ChildcareExpensesWhileAtWork.apply)(ChildcareExpensesWhileAtWork.unapply)
      .verifying("relationToPartner.required", validateRelationToPartner(claim, _))
  )

  def validateRelationToPartner(implicit claim: Claim, childcareExpensesWhileAtWork: ChildcareExpensesWhileAtWork) = {

    claim.questionGroup(MoreAboutYou) -> claim.questionGroup(PersonYouCareFor) match {
      case (Some(m: MoreAboutYou), Some(p: PersonYouCareFor)) if m.hadPartnerSinceClaimDate == "yes" && p.isPartnerPersonYouCareFor == "no" => childcareExpensesWhileAtWork.relationToPartner.isDefined
      case _ => true
    }
  }

  def present = claiming {
    implicit claim => implicit request =>

      val payToLookAfterChildren = claim.questionGroup(SelfEmploymentPensionsAndExpenses) match {
        case Some(s: SelfEmploymentPensionsAndExpenses) => s.doYouPayToLookAfterYourChildren == `yes`
        case _ => false
      }

      payToLookAfterChildren match {
        case true => whenSectionVisible(Ok(views.html.s8_self_employment.g5_childcareExpensesWhileAtWork(form.fill(ChildcareExpensesWhileAtWork), completedQuestionGroups)))
        case false => claim.delete(ChildcareExpensesWhileAtWork) -> Redirect(routes.G6ChildcareProvidersContactDetails.present())
      }
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("", "relationToPartner.required", FormError("relationToPartner", "error.required"))
          BadRequest(views.html.s8_self_employment.g5_childcareExpensesWhileAtWork(formWithErrorsUpdate, completedQuestionGroups))
        },
        f => claim.update(f) -> Redirect(routes.G6ChildcareProvidersContactDetails.present())
      )
  }
}