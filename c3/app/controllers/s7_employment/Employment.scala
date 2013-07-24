package controllers.s7_employment

import language.implicitConversions
import language.reflectiveCalls
import reflect.ClassTag
import play.api.mvc.{Call, AnyContent, Request, Controller}
import models.view.CachedClaim
import models.domain._
import models.domain.Claim
import play.api.data.Form

object Employment extends Controller with CachedClaim {
  implicit def jobFormFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fillWithJobID(qi: QuestionGroup.Identifier, jobID: String)(implicit claim: Claim): Form[Q] = {
      claim.questionGroup(Jobs).getOrElse(Jobs()).asInstanceOf[Jobs].jobs.find(_.jobID == jobID).getOrElse(Job("", List())).find(_.identifier.id == qi.id) match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }

  def jobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier, jobID: String)(implicit claim: Claim, request: Request[AnyContent]): List[(QuestionGroup, Call)] = {
    claim.questionGroup(Jobs) match {
      case Some(js: Jobs) => js.find(_.jobID == jobID) match {
        case Some(j: Job) => j.questionGroups.takeWhile(_.identifier.index < questionGroupIdentifier.index).map(qg => qg -> route(qg, jobID))
        case _ => Nil
      }
      case _ => Nil
    }
  }

  def completed = claiming { implicit claim => implicit request =>
    Ok("")
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect("")
  }

  private def route(qg: QuestionGroup, jobID: String) = qg.identifier match {
    case JobDetails => routes.G2JobDetails.present()
    case EmployerContactDetails => routes.G3EmployerContactDetails.present(jobID)
    case _ => routes.G1BeenEmployed.present()
  }
}