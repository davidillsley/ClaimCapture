package models.domain

import models.DayMonthYear
import models.yesNo.YesNoWithText

object TimeSpentAbroad extends Section.Identifier {
  val id = "s5"
}

case class NormalResidenceAndCurrentLocation(whereDoYouLive: YesNoWithText = YesNoWithText("", Some("")),
                                             inGBNow: String = "") extends QuestionGroup(NormalResidenceAndCurrentLocation)

object NormalResidenceAndCurrentLocation extends QuestionGroup.Identifier {
  val id = s"${TimeSpentAbroad.id}.g1"
}

case class AbroadForMoreThan4Weeks(anyTrips: String = "") extends QuestionGroup(AbroadForMoreThan4Weeks)

object AbroadForMoreThan4Weeks extends QuestionGroup.Identifier {
  val id = s"${TimeSpentAbroad.id}.g2"
}

case class AbroadForMoreThan52Weeks(anyTrips: String = "") extends QuestionGroup(AbroadForMoreThan52Weeks)

object AbroadForMoreThan52Weeks extends QuestionGroup.Identifier  {
  val id = s"${TimeSpentAbroad.id}.g3"
}

case class Trips(fourWeeksTrips: List[FourWeeksTrip] = Nil, fiftyTwoWeeksTrips: List[FiftyTwoWeeksTrip] = Nil) extends QuestionGroup(Trips) {
  def update(trip: FourWeeksTrip): Trips = {
    val updated = fourWeeksTrips map { t => if (t.id == trip.id) trip else t }
    if (updated.contains(trip)) Trips(updated, fiftyTwoWeeksTrips) else Trips(fourWeeksTrips :+ trip, fiftyTwoWeeksTrips)
  }

  def +(trip: FourWeeksTrip): Trips = update(trip)

  def update(trip: FiftyTwoWeeksTrip): Trips = {
    val updated = fiftyTwoWeeksTrips map { t => if (t.id == trip.id) trip else t }
    if (updated.contains(trip)) Trips(fourWeeksTrips, updated) else Trips(fourWeeksTrips, fiftyTwoWeeksTrips :+ trip)
  }

  def +(trip: FiftyTwoWeeksTrip): Trips = update(trip)

  def delete(tripID: String): Trips = Trips(fourWeeksTrips.filterNot(_.id == tripID), fiftyTwoWeeksTrips.filterNot(_.id == tripID))

  def -(tripID: String): Trips = delete(tripID)
}

object Trips extends QuestionGroup.Identifier {
  val id = s"${TimeSpentAbroad.id}.g4"
}

case class Trip(id: String, start: DayMonthYear, end: DayMonthYear, where: String, why: String) extends FourWeeksTrip with FiftyTwoWeeksTrip {
  def as[T >: Trip]: T = asInstanceOf[T]
}

sealed trait TripPeriod {
  val id: String

  val start: DayMonthYear

  val end: DayMonthYear

  val where: String

  val why: String
}

trait FourWeeksTrip extends TripPeriod {
  this: Trip =>
}

trait FiftyTwoWeeksTrip extends TripPeriod {
  this: Trip =>
}