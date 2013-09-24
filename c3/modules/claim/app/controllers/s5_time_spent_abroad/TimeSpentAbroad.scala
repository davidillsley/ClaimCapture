package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import models.view.Navigable

object TimeSpentAbroad extends Controller with CachedClaim with Navigable {
  def trips(implicit claim: DigitalForm) = claim.questionGroup(Trips) match {
    case Some(ts: Trips) => ts
    case _ => Trips()
  }

  def completed = executeOnForm {implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
    else track(TimeSpentAbroad) { implicit claim => Ok(views.html.s5_time_spent_abroad.g6_completed())}
  }

  def completedSubmit = executeOnForm {implicit claim => implicit request =>
    if (completedQuestionGroups.distinct.size == 4) Redirect("/education/your-course-details")
    else Redirect(controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present())
  }

  private def completedQuestionGroups(implicit claim: DigitalForm): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.TimeSpentAbroad)
  }
}