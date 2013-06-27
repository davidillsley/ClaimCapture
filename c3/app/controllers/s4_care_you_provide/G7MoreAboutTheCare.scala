package controllers.s4_care_you_provide

import models.domain.MoreAboutTheCare
import play.api.data.Form
import controllers.{Routing, s4_care_you_provide}
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.view.CachedClaim
import play.api.mvc.Controller

object G7MoreAboutTheCare extends Controller with Routing with CachedClaim {

  override val route = MoreAboutTheCare.id -> controllers.s4_care_you_provide.routes.G7MoreAboutTheCare.present

  val form = Form(
    mapping(
      "spent35HoursCaring" -> nonEmptyText,
      "spent35HoursCaringBeforeClaim" -> nonEmptyText,
      "careStartDate" -> optional(dayMonthYear verifying validDateOnly),
      "hasSomeonePaidYou" -> nonEmptyText
    )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))

  def present = claiming {
    implicit claim => implicit request =>

      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != MoreAboutTheCare.id)

      val currentForm = claim.questionGroup(MoreAboutTheCare.id) match {
        case Some(h: MoreAboutTheCare) => form.fill(h)
        case _ => form
      }

      Ok(views.html.s4_careYouProvide.g7_moreAboutTheCare(currentForm, completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim => implicit request =>

      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != MoreAboutTheCare.id)

      def actAs(form: Form[MoreAboutTheCare])(implicit moreAboutTheCare: MoreAboutTheCare): Form[MoreAboutTheCare] = {
        if (moreAboutTheCare.spent35HoursCaringBeforeClaim == "yes" && moreAboutTheCare.careStartDate == None) form.fill(moreAboutTheCare).withError("careStartDate", "error.required")
        else form
      }

      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g7_moreAboutTheCare(formWithErrors, completedQuestionGroups)),
        implicit moreAboutTheCare => {
          val formValidations: (Form[MoreAboutTheCare]) => Form[MoreAboutTheCare] = actAs
          val moreAboutTheCareFormValidated = formValidations(form)

          if (moreAboutTheCareFormValidated.hasErrors) BadRequest(views.html.s4_careYouProvide.g7_moreAboutTheCare(moreAboutTheCareFormValidated, completedQuestionGroups))
          else claim.update(moreAboutTheCare) -> Redirect(s4_care_you_provide.routes.G8OneWhoPaysPersonalDetails.present)
        })
  }
}