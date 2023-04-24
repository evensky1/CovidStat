package com.innowise.covid
package model

import play.api.libs.json.{Json, OWrites, Reads}

import java.time.Instant

case class CountryDayStatistic(country: String, newCases: Int, date: Instant)

object CountryDayStatistic {
  implicit val writes: OWrites[CountryDayStatistic] = Json.writes[CountryDayStatistic]
  implicit val reads: Reads[CountryDayStatistic] = Json.reads[CountryDayStatistic]
}
