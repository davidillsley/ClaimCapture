package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.domain.{Claim, ContactDetails, TheirPersonalDetails, TheirContactDetails}
import models.domain.YourPartnerContactDetails

object G2TheirContactDetails extends Controller with Routing with CachedClaim {

  override val route = TheirContactDetails.id -> routes.G2TheirContactDetails.present

  val form = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber)
    )(TheirContactDetails.apply)(TheirContactDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(TheirContactDetails)

  def present = claiming { implicit claim => implicit request =>
    val liveAtSameAddress = claim.questionGroup(TheirPersonalDetails) match {
      case Some(t: TheirPersonalDetails) => t.liveAtSameAddress == yes
      case _ => false
    }

    val theirContactDetailsPrePopulatedForm = if (liveAtSameAddress) {
      claim.questionGroup(ContactDetails) match {
        case Some(cd: ContactDetails) => form.fill(TheirContactDetails(address = cd.address, postcode = cd.postcode))
        case _ => form
      }
    } else {
      claim.questionGroup(TheirContactDetails) match {
        case Some(t: TheirContactDetails) => form.fill(t)
        case _ => claim.questionGroup(YourPartnerContactDetails) match {// Get YourPartner address
            case Some(cd: YourPartnerContactDetails) => form.fill(TheirContactDetails(address = cd.address.get, postcode =  cd.postcode))
            case _ => form
          }
      }
    }

    Ok(views.html.s4_care_you_provide.g2_theirContactDetails(theirContactDetailsPrePopulatedForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g2_theirContactDetails(formWithErrors, completedQuestionGroups)),
      theirContactDetails => claim.update(theirContactDetails) -> Redirect(routes.G3MoreAboutThePerson.present()))
  }
}