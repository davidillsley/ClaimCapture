package models.view.example

case class SingleStringInputForm(answer: String = "") extends AbstractForm {

  def answerToString(): String = {
        answer
  }

}

