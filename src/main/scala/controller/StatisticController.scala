package com.innowise.covid
package controller

import model.StatisticRequest
import service.StatisticService

import akka.http.scaladsl.model.{ContentType, HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.model.StatusCodes.Created
import akka.http.scaladsl.server.{Directives, Route}
import play.api.libs.json.Json

object StatisticController extends Directives {

  private val statisticService = StatisticService()

  def route: Route = path("stat") {
    post {
      entity(as[String]) { jsonStr =>

        val jsValue = Json.parse(jsonStr).validate[StatisticRequest]
        val statisticRequest = jsValue.get

        val statisticFuture = statisticService.getMaxAndMinForPeriodByCountry(
          statisticRequest.countries, statisticRequest.from.toInstant, statisticRequest.to.toInstant
        )

        onSuccess(statisticFuture) { statisticSequence =>
          val responseJson = Json.stringify(Json.toJson(statisticSequence))
          val entity = HttpEntity(ContentType(MediaTypes.`application/json`), responseJson)

          complete(Created, entity)
        }

      }
    }
  }
}
