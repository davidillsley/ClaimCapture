package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Claim, AboutOtherMoney}
import controllers.Mappings._
import models.domain.MoreAboutYou
import models.yesNo.YesNo
import utils.helpers.CarersForm._
import play.api.data.FormError
import play.api.i18n.Messages

object G1AboutOtherMoney extends Controller with CachedClaim {

  val yourBenefitsMapping =
    "yourBenefits" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply)
    
    val anyPaymentsSinceClaimDateMapping =
    "anyPaymentsSinceClaimDate" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply)

  val form = Form(
    mapping(
      yourBenefitsMapping,
      // TODO add the following fields to the XML
      anyPaymentsSinceClaimDateMapping
      
    )(AboutOtherMoney.apply)(AboutOtherMoney.unapply))

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.hadPartnerSinceClaimDate == yes
    case _ => false
  }

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_other_money.g1_aboutOtherMoney(form.fill(AboutOtherMoney), hadPartnerSinceClaimDate))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val yourBenefitsAnswerErrorMessage = Messages("yourBenefits.answer.label",
                                                      if (hadPartnerSinceClaimDate) "or your Partner/Spouse" else "",
                                                      claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))

        val formWithErrorsUpdate = formWithErrors
          .replaceError("yourBenefits.answer", FormError(yourBenefitsAnswerErrorMessage, "error.required"))

        BadRequest(views.html.s9_other_money.g1_aboutOtherMoney(formWithErrorsUpdate, hadPartnerSinceClaimDate))
      },
      f => claim.update(f) -> Redirect(routes.G5StatutorySickPay.present()))
  }
}