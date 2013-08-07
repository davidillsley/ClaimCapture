package controllers.s2_about_you

import language.reflectiveCalls
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._

object G4ClaimDate extends Controller with AboutYouRouting with CachedClaim {
  val form = Form(
    mapping(
      "dateOfClaim" -> dayMonthYear.verifying(validDate)
    )(ClaimDate.apply)(ClaimDate.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s2_about_you.g4_claimDate(form.fill(ClaimDate), completedQuestionGroups(ClaimDate)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g4_claimDate(formWithErrors, completedQuestionGroups(ClaimDate))),
      claimDate => claim.update(claimDate) -> Redirect(routes.G5MoreAboutYou.present()))
  }
}