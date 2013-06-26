package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.{Routing}
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.domain.{ContactDetails, TheirPersonalDetails, TheirContactDetails}

object G2TheirContactDetails extends Controller with Routing with CachedClaim {

  override val route = TheirContactDetails.id -> controllers.s4_care_you_provide.routes.G2TheirContactDetails.present

  val form = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber)
    )(TheirContactDetails.apply)(TheirContactDetails.unapply))


  def present = claiming { implicit claim => implicit request =>

    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

    val liveAtSameAddress = claim.questionGroup(TheirPersonalDetails.id) match {
      case Some(t: TheirPersonalDetails) => t.liveAtSameAddress == yes
      case _ => false
    }

    val theirContactDetailsPrePopulatedForm = if (liveAtSameAddress) {
      claim.questionGroup(ContactDetails.id) match {
        case Some(cd: ContactDetails) => form.fill(TheirContactDetails(address = cd.address, postcode = cd.postcode))
        case _ => form
      }
    } else {
      claim.questionGroup(TheirContactDetails.id) match {
        case Some(t: TheirContactDetails) => form.fill(t)
        case _ => form
      }
    }

    Ok(views.html.s4_careYouProvide.g2_theirContactDetails(theirContactDetailsPrePopulatedForm, completedQuestionGroups))

  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g2_theirContactDetails(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id))),
      theirContactDetails => claim.update(theirContactDetails) -> Redirect(controllers.routes.CareYouProvide.moreAboutThePerson()))
  }


}
