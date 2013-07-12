package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import utils.helpers.CarersForm._
import models.view.CachedClaim
import controllers.Routing
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{AbroadForMoreThan52Weeks, Claim}
import models.yesNo.YesNo
import TimeSpentAbroad.trips

object G3AbroadForMoreThan52Weeks extends Controller with Routing with CachedClaim {
  override val route = AbroadForMoreThan52Weeks.id -> routes.G3AbroadForMoreThan52Weeks.present

  val form = Form(
    mapping(
      "anyTrips" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AbroadForMoreThan52Weeks)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g3_abroad_for_more_than_52_weeks(form, trips, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    def next(abroadForMoreThan52Weeks: YesNo) = abroadForMoreThan52Weeks.answer match {
      case `yes` if trips.fiftyTwoWeeksTrips.size < 10 => Redirect(routes.G4Trip.fiftyTwoWeeks())
      case `yes` => Redirect(routes.G3AbroadForMoreThan52Weeks.present())
      case _ => Redirect(routes.G5otherEEAStateOrSwitzerland.present())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g3_abroad_for_more_than_52_weeks(formWithErrors, trips, completedQuestionGroups)),
      abroadForMoreThan52Weeks => claim.update(AbroadForMoreThan52Weeks).update(trips) -> next(abroadForMoreThan52Weeks))
  }
}