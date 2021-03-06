package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{Trip, FourWeeksTrip, Trips}
import utils.helpers.CarersForm._
import play.api.i18n.Messages
import models.DayMonthYear

object G4Trip extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "tripID" -> nonEmptyText,
      "start" -> (dayMonthYear verifying validDate),
      "end" -> (dayMonthYear verifying validDate),
      "where" -> nonEmptyText,
      "why" -> optional(text)
    )(Trip.apply)(Trip.unapply))

  def fourWeeks = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g4_trip(form))
  }

  def fourWeeksSubmit = claiming { implicit claim => implicit request =>
    val trips = claim.questionGroup(Trips) match {
      case Some(ts: Trips) => ts
      case _ => Trips()
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g4_trip(formWithErrors)),
      trip => {
        val updatedTrips = if (trips.fourWeeksTrips.size >= 5) trips else trips.update(trip.as[FourWeeksTrip])
        claim.update(updatedTrips) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      })
  }

  def fiftyTwoWeeks = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g4_trip(form))
  }

  def fiftyTwoWeeksSubmit = claiming { implicit claim => implicit request =>
    Ok("")
  }

  def trip(id: String) = claiming { implicit claim => implicit request =>
    claim.questionGroup(Trips) match {
      case Some(ts: Trips) => ts.fourWeeksTrips.find(_.id == id) match {
        case Some(t: Trip) => Ok(views.html.s5_time_spent_abroad.g4_trip(form.fill(t)))
        case _ => Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      }

      /*
      52 WEEKS
      case Some(ts: Trips) => ts.fourWeeksTrips.find(_.id == id) match {
        case Some(t: Trip) => Ok(views.html.s5_time_spent_abroad.g4_trip(form.fill(t)))
        case _ => Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      }*/

      case _ => Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
    }
  }

  def delete(id: String) = claiming { implicit claim => implicit request =>
    import play.api.libs.json.Json
    import language.postfixOps

    claim.questionGroup(Trips) match {
      case Some(ts: Trips) => {
        val updatedTrips = ts.delete(id)

        if (updatedTrips.fourWeeksTrips.isEmpty) claim.update(updatedTrips) -> Ok(Json.obj("anyTrips" -> Messages("4Weeks.label", (DayMonthYear.today - 3 years).`dd/MM/yyyy`)))
        else claim.update(updatedTrips) -> Ok(Json.obj("anyTrips" -> Messages("4Weeks.more.label", (DayMonthYear.today - 3 years).`dd/MM/yyyy`)))
      }

      case _ => BadRequest(s"""Failed to delete trip with ID "$id" as claim currently has no trips""")
    }
  }
}