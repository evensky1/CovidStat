package com.innowise.covid
package client.model

import play.api.libs.json.{JsPath, Json, OWrites, Reads}

import java.time.Instant


case class DayInfo(country: String, countryCode: Option[String],
                   province: Option[String], city: Option[String], cityCode: Option[String],
                   lat: String, lon: String, cases: Int,
                   status: String, date: Instant)

object DayInfo {

  implicit val writes: OWrites[DayInfo] = OWrites { info =>
    Json.obj(
      "Country" -> info.country,
      "CountryCode" -> info.countryCode,
      "Province" -> info.province,
      "City" -> info.city,
      "CityCode" -> info.cityCode,
      "Lat" -> info.lat,
      "Lon" -> info.lon,
      "Cases" -> info.cases,
      "Status" -> info.status,
      "Date" -> info.date,
    )
  }

  implicit val reads: Reads[DayInfo] =
    for {
      country <- ( JsPath \ "Country").read[String]
      countryCode <- ( JsPath \ "CountryCode").readNullable[String]
      province <- ( JsPath \ "Province").readNullable[String]
      city <- ( JsPath \ "City").readNullable[String]
      cityCode <- ( JsPath \ "CityCode").readNullable[String]
      lat <- ( JsPath \ "Lat").read[String]
      lon <- ( JsPath \ "Lon").read[String]
      cases <- ( JsPath \ "Cases").read[Int]
      status <- ( JsPath \ "Status").read[String]
      date <- ( JsPath \ "Date").read[Instant]
    } yield {
      DayInfo(country, countryCode, province, city, cityCode, lat, lon, cases, status, date)
    }
}
