package com.innowise.covid
package model

import play.api.libs.json.{Json, OWrites, Reads}

import java.util.Date

case class StatisticRequest(from: Date, to: Date, countries: List[String])

object StatisticRequest {
  implicit val writes: OWrites[StatisticRequest] = Json.writes[StatisticRequest]
  implicit val reads: Reads[StatisticRequest] = Json.reads[StatisticRequest]
}