package controllers.s9_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentPensionsAndExpenses, Claim, ChildcareProvidersContactDetails}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s9_self_employment.SelfEmployment.whenSectionVisible

object G6ChildcareProvidersContactDetails extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(ChildcareProvidersContactDetails)
  val formCall = routes.G6ChildcareProvidersContactDetails.present()

  val form = Form(
    mapping(
      call(formCall),
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode)
    )(ChildcareProvidersContactDetails.apply)(ChildcareProvidersContactDetails.unapply)
  )

  def present = claiming { implicit claim => implicit request =>

    val payToLookAfterChildren = claim.questionGroup(SelfEmploymentPensionsAndExpenses) match {
      case Some(s: SelfEmploymentPensionsAndExpenses) => s.doYouPayToLookAfterYourChildren == `yes`
      case _ => false
    }

    payToLookAfterChildren  match {
      case true => whenSectionVisible(Ok(views.html.s9_self_employment.g6_childcareProvidersContactDetails(form.fill(ChildcareProvidersContactDetails), completedQuestionGroups)))
      case false => claim.delete(ChildcareProvidersContactDetails) -> Redirect(routes.G7ExpensesWhileAtWork.present())
    }


  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s9_self_employment.g6_childcareProvidersContactDetails(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G7ExpensesWhileAtWork.present())
      )
  }
}
