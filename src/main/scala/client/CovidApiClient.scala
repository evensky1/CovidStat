package com.innowise.covid
package client

import model.DayInfo

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import play.api.libs.json.{JsValue, Json}

import java.time.Instant
import scala.concurrent.{ExecutionContext, Future}

class CovidApiClient {
  implicit val fetchActor: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "fetch-system")
  implicit val executionContext: ExecutionContext = fetchActor.executionContext

  def fetchByCountryName(countryName: String, from: Instant, to: Instant): Future[Seq[DayInfo]] = {
    val request: HttpRequest = createHttpRequest(countryName, from.toString, to.toString)

    Http().singleRequest(request).flatMap { res =>
      Unmarshal(res).to[String].map { data =>

        Json.parse(data)
          .validate[Seq[DayInfo]]
          .get
      }
    }
  }

  private def createHttpRequest(countryName: String, from: String, to: String) = {

    val requestUri = Uri.from(
      scheme = "https",
      host = "api.covid19api.com",
      path = s"/total/country/$countryName/status/confirmed")
      .withQuery(Query("from" -> from, "to" -> to))

    HttpRequest(
      method = HttpMethods.GET,
      uri = requestUri
    )
  }
}
