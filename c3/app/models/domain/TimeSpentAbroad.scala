package models.domain

import models.DayMonthYear
import models.yesNo.YesNoWithText
import play.api.mvc.Call

object TimeSpentAbroad extends Section.Identifier {
  val id = "s5"
}

case class NormalResidenceAndCurrentLocation(call: Call,
                                             whereDoYouLive: YesNoWithText, inGBNow: String) extends QuestionGroup(NormalResidenceAndCurrentLocation)

object NormalResidenceAndCurrentLocation extends QuestionGroup.Identifier {
  val id = s"${TimeSpentAbroad.id}.g1"
}

case class AbroadForMoreThan4Weeks(call: Call, anyTrips: String) extends QuestionGroup(AbroadForMoreThan4Weeks)

object AbroadForMoreThan4Weeks extends QuestionGroup.Identifier {
  val id = s"${TimeSpentAbroad.id}.g2"
}

case class AbroadForMoreThan52Weeks(call: Call, anyTrips: String) extends QuestionGroup(AbroadForMoreThan52Weeks)

object AbroadForMoreThan52Weeks extends QuestionGroup.Identifier  {
  val id = s"${TimeSpentAbroad.id}.g3"
}

case class Trips(fourWeeksTrips: List[FourWeeksTrip] = Nil, fiftyTwoWeeksTrips: List[FiftyTwoWeeksTrip] = Nil) extends QuestionGroup(Trips) with NoRouting {
  def update(trip: FourWeeksTrip) = {
    val updated = fourWeeksTrips map { t => if (t.id == trip.id) trip else t }
    if (updated.contains(trip)) Trips(updated, fiftyTwoWeeksTrips) else Trips(fourWeeksTrips :+ trip, fiftyTwoWeeksTrips)
  }

  def update(trip: FiftyTwoWeeksTrip) = {
    val updated = fiftyTwoWeeksTrips map { t => if (t.id == trip.id) trip else t }
    if (updated.contains(trip)) Trips(fourWeeksTrips, updated) else Trips(fourWeeksTrips, fiftyTwoWeeksTrips :+ trip)
  }

  def delete(tripID: String) = Trips(fourWeeksTrips.filterNot(_.id == tripID), fiftyTwoWeeksTrips.filterNot(_.id == tripID))
}

object Trips extends QuestionGroup.Identifier {
  val id = s"${TimeSpentAbroad.id}.g4"
}

case class Trip(id: String, start: DayMonthYear, end: DayMonthYear, where: String, why: Option[String] = None) extends FourWeeksTrip with FiftyTwoWeeksTrip {
  def as[T >: Trip]: T = asInstanceOf[T]
}

sealed trait TripPeriod {
  val id: String

  val start: DayMonthYear

  val end: DayMonthYear

  val where: String

  val why: Option[String]
}

trait FourWeeksTrip extends TripPeriod {
  this: Trip =>
}

trait FiftyTwoWeeksTrip extends TripPeriod {
  this: Trip =>
}

case class OtherEEAStateOrSwitzerland(call: Call,
                                      benefitsFromOtherEEAStateOrSwitzerland: YesNoWithText, workingForOtherEEAStateOrSwitzerland: String) extends QuestionGroup(OtherEEAStateOrSwitzerland)

object OtherEEAStateOrSwitzerland extends QuestionGroup.Identifier {
  val id = s"${TimeSpentAbroad.id}.g5"
}