package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.{Mappings, Routing}
import models.view.CachedClaim
import models.domain.{MoreAboutThePerson, PreviousCarerContactDetails}
import utils.helpers.CarersForm._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import scala.Some

object G5PreviousCarerContactDetails extends Controller with Routing with CachedClaim {

  override val route = PreviousCarerContactDetails.id -> controllers.s4_care_you_provide.routes.G5PreviousCarerContactDetails.present

  val form = Form(
    mapping(
      "address" -> optional(address.verifying(requiredAddress)),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber),
      "mobileNumber" -> optional(text verifying validPhoneNumber)
    )(PreviousCarerContactDetails.apply)(PreviousCarerContactDetails.unapply))

  def present = claiming {
    implicit claim => implicit request =>

      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).filter(q => q.id < PreviousCarerContactDetails.id)

      val claimedAllowanceBefore: Boolean = claim.questionGroup(MoreAboutThePerson.id) match {
        case Some(t: MoreAboutThePerson) => if (t.claimedAllowanceBefore == Mappings.yes) true else false
        case _ => false
      }

      if (claimedAllowanceBefore) {
        val currentForm: Form[PreviousCarerContactDetails] = claim.questionGroup(PreviousCarerContactDetails.id) match {
          case Some(t: PreviousCarerContactDetails) => form.fill(t)
          case _ => form
        }

        Ok(views.html.s4_careYouProvide.g5_previousCarerContactDetails(currentForm, completedQuestionGroups))
      } else claim.delete(PreviousCarerContactDetails.id) -> Redirect(controllers.s4_care_you_provide.routes.G6RepresentativesForThePerson.present)
  }

  def submit = claiming {
    implicit claim => implicit request =>

      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g5_previousCarerContactDetails(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id).filter(q => q.id < PreviousCarerContactDetails.id))),
        previousCarerContactDetails => claim.update(previousCarerContactDetails) -> Redirect(controllers.s4_care_you_provide.routes.G6RepresentativesForThePerson.present))
  }
}