package controllers.s4_care_you_provide

import models.domain.{Claim, MoreAboutTheCare}
import play.api.data.Form
import controllers.Routing
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.view.CachedClaim
import play.api.mvc.Controller

object G7MoreAboutTheCare extends Controller with Routing with CachedClaim {

  override val route = MoreAboutTheCare.id -> routes.G7MoreAboutTheCare.present

  val form = Form(
    mapping(
      "spent35HoursCaring" -> nonEmptyText.verifying(validYesNo),
      "spent35HoursCaringBeforeClaim" -> nonEmptyText.verifying(validYesNo),
      "careStartDate" -> optional(dayMonthYear verifying validDate),
      "hasSomeonePaidYou" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutTheCare)

  def present = claiming { implicit claim => implicit request =>
    val currentForm = claim.questionGroup(MoreAboutTheCare) match {
      case Some(m: MoreAboutTheCare) => form.fill(m)
      case _ => form
    }

    Ok(views.html.s4_care_you_provide.g7_moreAboutTheCare(currentForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    def actAs(form: Form[MoreAboutTheCare])(implicit moreAboutTheCare: MoreAboutTheCare): Form[MoreAboutTheCare] = {
      if (moreAboutTheCare.spent35HoursCaringBeforeClaim == "yes" && moreAboutTheCare.careStartDate == None) form.fill(moreAboutTheCare).withError("careStartDate", "error.required")
      else form
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g7_moreAboutTheCare(formWithErrors, completedQuestionGroups)),
      implicit moreAboutTheCare => {
        val formValidations: (Form[MoreAboutTheCare]) => Form[MoreAboutTheCare] = actAs
        val moreAboutTheCareFormValidated = formValidations(form)

        if (moreAboutTheCareFormValidated.hasErrors) BadRequest(views.html.s4_care_you_provide.g7_moreAboutTheCare(moreAboutTheCareFormValidated, completedQuestionGroups))
        else claim.update(moreAboutTheCare) -> Redirect(routes.G8OneWhoPaysPersonalDetails.present())
      })
  }
}