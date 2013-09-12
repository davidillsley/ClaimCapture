package controllers.s7_employment

import java.util.UUID._
import play.api.mvc.Request
import models.domain.DigitalForm

object JobID {
  def apply(form: play.api.data.Form[_])(implicit claim: DigitalForm, request: Request[_]): String = {
    val regex = """^(?:.*?)/employment/(?:.*?)(?:/(.*?))?$""".r

    form("jobID").value.getOrElse(regex.findFirstMatchIn(request.path).map {
      _ group 1 match {
        case s if s != null && s.length > 0 => s
        case _ => randomUUID.toString
      }
    }.getOrElse(randomUUID.toString))
  }
}