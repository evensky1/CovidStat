package com.innowise.covid
package model

import play.api.libs.json.{Json, OWrites, Reads}

import java.time.Instant

case class CountryStatistic(country: String,
                            maxNewCases: Int, maxNewCasesDate: Instant,
                            minNewCases: Int, minNewCasesDate: Instant,
                            from: Instant, to: Instant)

object CountryStatistic {
  implicit val writes: OWrites[CountryStatistic] = Json.writes[CountryStatistic]
  implicit val reads: Reads[CountryStatistic] = Json.reads[CountryStatistic]
}