package controllers.s10_pay_details

import language.reflectiveCalls
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain._
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s10_pay_details.PayDetails._
import app.AccountStatus

object G2BankBuildingSocietyDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "accountHolderName" -> nonEmptyText(maxLength = sixty),
    "whoseNameIsTheAccountIn" -> nonEmptyText,
    "bankFullName" -> nonEmptyText(maxLength = 100),
    "sortCode" -> (sortCode verifying requiredSortCode),
    "accountNumber" -> nonEmptyText(minLength = 6, maxLength = 10),
    "rollOrReferenceNumber" -> text(maxLength = 18)
  )(BankBuildingSocietyDetails.apply)(BankBuildingSocietyDetails.unapply))

  def present = executeOnForm {implicit claim => implicit request =>
    presentConditionally(bankBuildingSocietyDetails)
  }

  def bankBuildingSocietyDetails(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    val iAmVisible = claim.questionGroup(HowWePayYou) match {
      case Some(y: HowWePayYou) => y.likeToBePaid == AccountStatus.BankBuildingAccount.name
      case _ => true
    }

    if (iAmVisible) track(BankBuildingSocietyDetails) { implicit claim => Ok(views.html.s10_pay_details.g2_bankBuildingSocietyDetails(form.fill(BankBuildingSocietyDetails))) }
    else claim.delete(BankBuildingSocietyDetails) -> Redirect(routes.PayDetails.completed())
  }

  def submit = executeOnForm {implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s10_pay_details.g2_bankBuildingSocietyDetails(formWithErrors)),
      howWePayYou => claim.update(howWePayYou) -> Redirect(routes.PayDetails.completed()))
  }
}