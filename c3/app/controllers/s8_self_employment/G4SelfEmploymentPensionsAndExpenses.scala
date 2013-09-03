package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentPensionsAndExpenses, Claim}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.yesNo.YesNoWith2Text
import controllers.s8_self_employment.SelfEmployment._
import play.api.data.FormError
import utils.helpers.PastPresentLabelHelper.didYouDoYouIfSelfEmployed

object G4SelfEmploymentPensionsAndExpenses extends Controller with SelfEmploymentRouting with CachedClaim {
  val pensionSchemeMapping =
      "doYouPayToPensionScheme" -> mapping(
        "answer" -> nonEmptyText.verifying(validYesNo),
        "howMuchDidYouPay" -> optional(nonEmptyText verifying validDecimalNumber),
        "howOften" -> optional(nonEmptyText)
      )(YesNoWith2Text.apply)(YesNoWith2Text.unapply)
        .verifying("howMuchDidYouPay", YesNoWith2Text.validateText1OnYes _)
        .verifying("howOften", YesNoWith2Text.validateText2OnYes _)

  def form(implicit claim: Claim) = Form(
    mapping(
      pensionSchemeMapping,
      "doYouPayToLookAfterYourChildren" -> nonEmptyText.verifying(validYesNo),
      "didYouPayToLookAfterThePersonYouCaredFor" -> nonEmptyText.verifying(validYesNo)
    )(SelfEmploymentPensionsAndExpenses.apply)(SelfEmploymentPensionsAndExpenses.unapply))

  def present = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s8_self_employment.g4_selfEmploymentPensionsAndExpenses(form.fill(SelfEmploymentPensionsAndExpenses), completedQuestionGroups(SelfEmploymentPensionsAndExpenses))))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val pastPresent = didYouDoYouIfSelfEmployed
        val formWithErrorsUpdate = formWithErrors
          .replaceError("doYouPayToPensionScheme.answer", "error.required", FormError("doYouPayToPensionScheme.answer", "error.required", Seq(pastPresent)))
          .replaceError("doYouPayToLookAfterYourChildren", "error.required", FormError("doYouPayToLookAfterYourChildren", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("didYouPayToLookAfterThePersonYouCaredFor", "error.required", FormError("didYouPayToLookAfterThePersonYouCaredFor", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("doYouPayToPensionScheme", "howMuchDidYouPay", FormError("doYouPayToPensionScheme.howMuchDidYouPay", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("doYouPayToPensionScheme", "howOften", FormError("doYouPayToPensionScheme.howOften", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("doYouPayToPensionScheme.howMuchDidYouPay", "decimal.invalid", FormError("doYouPayToPensionScheme.howMuchDidYouPay", "decimal.invalid", Seq(pastPresent.toLowerCase)))
        BadRequest(views.html.s8_self_employment.g4_selfEmploymentPensionsAndExpenses(formWithErrorsUpdate, completedQuestionGroups(SelfEmploymentPensionsAndExpenses)))
      },
      f => claim.update(f) -> Redirect(routes.G5ChildcareExpensesWhileAtWork.present()))
  }
}