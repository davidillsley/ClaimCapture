package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.CachedClaim
import controllers.Routing
import models.domain.{YourPartnerPersonalDetails, NormalResidenceAndCurrentLocation}
import controllers.Mappings._
import models.yesNo.YesNoWithText
import utils.helpers.CarersForm._

object G1NormalResidenceAndCurrentLocation extends Controller with Routing with CachedClaim {
  override val route = NormalResidenceAndCurrentLocation.id -> routes.G1NormalResidenceAndCurrentLocation.present

  val liveMapping =
    "liveInUK" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whereDoYouLive" -> optional(nonEmptyText(maxLength = sixty)))(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnNo _)

  val form = Form(
    mapping(
      liveMapping,
      "inGBNow" -> nonEmptyText.verifying(validYesNo)
    )(NormalResidenceAndCurrentLocation.apply)(NormalResidenceAndCurrentLocation.unapply)
  )

  def present = claiming {
    implicit claim => implicit request =>

      val currentForm: Form[NormalResidenceAndCurrentLocation] = claim.questionGroup(NormalResidenceAndCurrentLocation) match {
        case Some(t: NormalResidenceAndCurrentLocation) => form.fill(t)
        case _ => form
      }
      Ok(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(currentForm))
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors.replaceError("liveInUK", FormError("liveInUK.whereDoYouLive", "error.required"))
          BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(formWithErrorsUpdate))
        },
        normalResidenceAndCurrentLocation => claim.update(normalResidenceAndCurrentLocation) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      )

  }
}