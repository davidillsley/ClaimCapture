package controllers.s9_other_money

import language.reflectiveCalls
import controllers.Mappings._
import models.domain.{MoneyPaidToSomeoneElseForYou, Claim, PersonWhoGetsThisMoney}
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding

object G3PersonWhoGetsThisMoney extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "fullName" -> nonEmptyText(maxLength = sixty),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNinoOnly)),
      "nameOfBenefit" -> nonEmptyText(maxLength = sixty),
      call(routes.G3PersonWhoGetsThisMoney.present())
    )(PersonWhoGetsThisMoney.apply)(PersonWhoGetsThisMoney.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(PersonWhoGetsThisMoney)

  def present = claiming { implicit claim => implicit request =>
    val iAmVisible = claim.questionGroup(MoneyPaidToSomeoneElseForYou) match {
      case Some(t: MoneyPaidToSomeoneElseForYou) => t.moneyAddedToBenefitSinceClaimDate == yes
      case _ => true
    }

    if (iAmVisible)
      Ok(views.html.s9_other_money.g3_personWhoGetsThisMoney(form.fill(PersonWhoGetsThisMoney), completedQuestionGroups))
    else
      Redirect(routes.G4PersonContactDetails.present())
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s9_other_money.g3_personWhoGetsThisMoney(formWithErrors, completedQuestionGroups)),
      f => claim.update(f) -> Redirect(routes.G4PersonContactDetails.present()))
  }
}