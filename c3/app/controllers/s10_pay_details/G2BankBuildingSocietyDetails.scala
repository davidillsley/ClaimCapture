package controllers.s10_pay_details

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._

import controllers.s10_pay_details.PayDetails._


object G2BankBuildingSocietyDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      call(routes.G2BankBuildingSocietyDetails.present()),
      "accountHolderName" -> nonEmptyText(maxLength = sixty),
      "bankFullName" -> nonEmptyText(maxLength = 100),
      "sortCode" -> (sortCode verifying requiredSortCode),
      "accountNumber" -> nonEmptyText(minLength = 6, maxLength = 10),
      "rollOrReferenceNumber" -> text(maxLength = 18)
    )(BankBuildingSocietyDetails.apply)(BankBuildingSocietyDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(BankBuildingSocietyDetails)

  def present = claiming {
    implicit claim => implicit request =>

      whenSectionVisible {

        val iAmVisible = claim.questionGroup(HowWePayYou) match {
          case Some(y: HowWePayYou) => y.likeToBePaid == "01"
          case _ => true
        }

        if (iAmVisible) Ok(views.html.s10_pay_details.g2_bankBuildingSocietyDetails(form.fill(BankBuildingSocietyDetails), completedQuestionGroups))
        else claim.delete(BankBuildingSocietyDetails) -> Redirect(routes.PayDetails.completed())
      }

  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s10_pay_details.g2_bankBuildingSocietyDetails(formWithErrors, claim.completedQuestionGroups(BankBuildingSocietyDetails))),
        howWePayYou => claim.update(howWePayYou) -> Redirect(routes.PayDetails.completed()))
  }
}