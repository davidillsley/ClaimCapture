package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.{Mappings, Routing}
import models.view.CachedClaim
import models.domain.{MoreAboutThePerson, PreviousCarerPersonalDetails}
import utils.helpers.CarersForm._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import scala.Some

object G4PreviousCarerPersonalDetails extends Controller with Routing with CachedClaim {

  override val route = PreviousCarerPersonalDetails.id -> controllers.s4_care_you_provide.routes.G4PreviousCarerPersonalDetails.present

  val form = Form(
    mapping(
      "firstName" -> optional(text(maxLength = sixty)),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> optional(text(maxLength = sixty)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "dateOfBirth" -> optional(dayMonthYear.verifying(validDateOnly))
    )(PreviousCarerPersonalDetails.apply)(PreviousCarerPersonalDetails.unapply))

  def present = claiming {
    implicit claim => implicit request =>

      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id < PreviousCarerPersonalDetails.id)

      val claimedAllowanceBefore: Boolean = claim.questionGroup(MoreAboutThePerson.id) match {
        case Some(t: MoreAboutThePerson) => if (t.claimedAllowanceBefore == Mappings.yes) true else false
        case _ => false
      }

      if (claimedAllowanceBefore) {
        val currentForm = claim.questionGroup(PreviousCarerPersonalDetails.id) match {
          case Some(h: PreviousCarerPersonalDetails) => form.fill(h)
          case _ => form
        }

        Ok(views.html.s4_careYouProvide.g4_previousCarerPersonalDetails(currentForm, completedQuestionGroups))
      } else claim.delete(PreviousCarerPersonalDetails.id) -> Redirect(controllers.s4_care_you_provide.routes.G5PreviousCarerContactDetails.present)
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g4_previousCarerPersonalDetails(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id).filter(q => q.id < PreviousCarerPersonalDetails.id))),
        currentForm => claim.update(currentForm) -> Redirect(controllers.s4_care_you_provide.routes.G5PreviousCarerContactDetails.present))
  }
}