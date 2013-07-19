package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.Mappings.validYesNo
import models.domain.Claim

object G6Employment extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "call" -> ignored(routes.G6Employment.present()),
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText.verifying(validYesNo),
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText.verifying(validYesNo)
    )(Employment.apply)(Employment.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Employment)

  def present = claiming { implicit claim => implicit request =>
    val employmentForm: Form[Employment] = claim.questionGroup(Employment) match {
      case Some(e: Employment) => form.fill(e)
      case _ => form
    }

    claim.questionGroup(ClaimDate) match {
      case Some(n) => Ok(views.html.s2_about_you.g6_employment(employmentForm, completedQuestionGroups))
      case _ => Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g6_employment(formWithErrors, completedQuestionGroups)),
      employment => claim.update(employment) -> Redirect(routes.G7PropertyAndRent.present()))
  }
}
