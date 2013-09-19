package controllers.s4_care_you_provide

import org.specs2.mutable.{ Tags, Specification }

class G2TheirContactDetailsFormSpec extends Specification with Tags {
  val validPostcode: String = "SE1 6EH"
  val validPhoneNumber = "020-76542059"

  "Their Contact Details Form" should {

    "map data into case class" in {
      G2TheirContactDetails.form.bind(
        Map("address.street.lineOne" -> "lineOne",
          "address.town.lineTwo" -> "lineTwo",
          "address.town.lineThree" -> "lineThree",
          "postcode" -> validPostcode,
          "phoneNumber" -> validPhoneNumber)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          theirContactDetails => {
            theirContactDetails.address.lineOne must equalTo(Some("lineOne"))
            theirContactDetails.address.lineTwo must equalTo(Some("lineTwo"))
            theirContactDetails.address.lineThree must equalTo(Some("lineThree"))
            theirContactDetails.postcode must equalTo(Some(validPostcode))
            theirContactDetails.phoneNumber must equalTo(Some(validPhoneNumber))
          })
    }

    "have a mandatory address" in {
      G2TheirContactDetails.form.bind(
        Map("address.street.lineOne" -> "", "address.town.lineTwo" -> "", "address.town.lineThree" -> "", "postcode" -> "", "phoneNumber" -> "")).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject an invalid postcode" in {
      G2TheirContactDetails.form.bind(
        Map("address.street.lineOne" -> "lineOne", "address.town.lineTwo" -> "", "address.town.lineThree" -> "", "postcode" -> "INVALID")).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
          theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject letters in a phone number" in {
      G2TheirContactDetails.form.bind(
        Map("address.street.lineOne" -> "lineOne",
          "address.town.lineTwo" -> "lineTwo",
          "address.town.lineThree" -> "lineThree",
          "postcode" -> validPostcode,
          "phoneNumber" -> "INVALID")).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
          theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }
  } section ("unit", models.domain.CareYouProvide.id)
}