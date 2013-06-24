package models.domain

case class Section(id: String, questionGroups: List[QuestionGroup]) {
  def questionGroup(questionGroupID: String): Option[QuestionGroup] = {
    questionGroups.find(qg => qg.id == questionGroupID)
  }
}