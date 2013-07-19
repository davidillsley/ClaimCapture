package controllers.s7_employment

import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.BeenEmployed
import utils.helpers.CarersForm._
import controllers.Mappings._

object G1BeenEmployed extends Controller with CachedClaim {

  val form = Form(
    mapping(
      "beenEmployed" -> (nonEmptyText verifying validYesNo),
      "call" -> ignored(routes.G1BeenEmployed.present())
    )(BeenEmployed.apply)(BeenEmployed.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g1_beenEmployed(form))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors =>BadRequest(views.html.s7_employment.g1_beenEmployed(formWithErrors)),
      beenEmployed => claim.update(beenEmployed) -> Redirect(routes.G2JobDetails.present()))
  }
}