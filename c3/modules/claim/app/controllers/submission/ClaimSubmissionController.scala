package controllers.submission

import play.api.mvc._
import play.api.Logger
import com.google.inject._
import models.view.CachedClaim
import services.UnavailableTransactionIdException

@Singleton
class ClaimSubmissionController @Inject()(submitter: Submitter) extends Controller with CachedClaim {

  def submit = executeOnForm {
    implicit claim => implicit request =>
      if (claim.isBot) {
        NotFound(views.html.errors.onHandlerNotFound(request)) // Send bot to 404 page.
      }
      else {
        try {
          Async {
            submitter.submit(claim, request)
          }
        }
        catch {
          case e: UnavailableTransactionIdException => {
            Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
            Redirect(s"/error?key=${CachedClaim.key}")
          }
          case e: java.lang.Exception => {
            Logger.error(s"InternalServerError ! ${e.getMessage}")
            Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
            Redirect(s"/error?key=${CachedClaim.key}")
          }
        }
      }
  }
}