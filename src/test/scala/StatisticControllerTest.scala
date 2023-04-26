package com.innowise.covid

import controller.StatisticController
import model.{CountryStatistic, StatisticRequest}

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.testkit.RouteTestTimeout
import org.scalatest.matchers.should.Matchers.shouldBe
import org.scalatest.matchers.should.Matchers.should
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.{JsObject, Json}

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

import java.time.temporal.ChronoUnit
import java.util.Timer

import scala.concurrent.duration

class StatisticControllerTest extends BaseControllerTest {
  "StatisticController" when {
    "Positive POST /api/stat" should {

      val dateFormat = SimpleDateFormat("yyyy-mm-dd")
      val from = dateFormat.parse("2023-03-1")
      val to = dateFormat.parse("2023-03-2")

      val requestEntity = StatisticRequest(from, to, List[String]("Lithuania"))
      val json = Json.stringify(Json.toJson(requestEntity))

      val gmtOffset = from.getTimezoneOffset + 3
      val fromValueInstant = from.toInstant.minus(gmtOffset, ChronoUnit.MINUTES)
      val toValueInstant = to.toInstant.minus(gmtOffset, ChronoUnit.MINUTES)

      val expectedEntity = List(CountryStatistic("Lithuania", 774, toValueInstant, 43, fromValueInstant, fromValueInstant, toValueInstant))

      var startTime: Long = 0
      var firstCompletionTime: Long = 0

      "return 201 CREATED and CountryStatistic array" in {

        startTime = System.currentTimeMillis()
        Post("/api/stat", json) ~> StatisticController.route ~> check {

          firstCompletionTime = System.currentTimeMillis() - startTime
          val responseEntity = Json.parse(responseAs[String]).validate[List[CountryStatistic]].get

          responseEntity shouldBe expectedEntity
          status.intValue() shouldBe 201
        }
      }

      "second request should take much less time and return same values" in {

        startTime = System.currentTimeMillis()
        var secondCompletionTime: Long = 0

        Post("/api/stat", json) ~> StatisticController.route ~> check {

          val responseEntity = Json.parse(responseAs[String]).validate[List[CountryStatistic]].get
          secondCompletionTime = System.currentTimeMillis() - startTime

          responseEntity shouldBe expectedEntity
          status.intValue() shouldBe 201
          secondCompletionTime should be < firstCompletionTime / 50
        }
      }
    }

    "Empty country list on POST /api/stat" should {

      "return 201 CREATED and empty CountryStatistic array" in {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd")
        val from = dateFormat.parse("2023-03-1")
        val to = dateFormat.parse("2023-03-2")

        val requestEntity = StatisticRequest(from, to, List[String]())
        val json = Json.stringify(Json.toJson(requestEntity))

        val expectedEntity = List()

        Post("/api/stat", json) ~> StatisticController.route ~> check {
          val responseEntity = Json.parse(responseAs[String]).validate[List[CountryStatistic]].get

          responseEntity shouldBe expectedEntity
          status.intValue() shouldBe 201
        }
      }

    }

    "Country list with corrupted name in it on POST /api/stat" should {

      "return 201 CREATED and CountryStatistic array" in {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd")
        val from = dateFormat.parse("2023-03-1")
        val to = dateFormat.parse("2023-03-2")

        val requestEntity = StatisticRequest(from, to, List[String]("Unexisting", "Lithuania"))
        val json = Json.stringify(Json.toJson(requestEntity))

        val gmtOffset = from.getTimezoneOffset + 3
        val fromValueInstant = from.toInstant.minus(gmtOffset, ChronoUnit.MINUTES)
        val toValueInstant = to.toInstant.minus(gmtOffset, ChronoUnit.MINUTES)

        val expectedEntity = List(CountryStatistic("Lithuania", 774, toValueInstant, 43, fromValueInstant, fromValueInstant, toValueInstant))

        Post("/api/stat", json) ~> StatisticController.route ~> check {
          val responseEntity = Json.parse(responseAs[String]).validate[List[CountryStatistic]].get

          responseEntity shouldBe expectedEntity
          status.intValue() shouldBe 201
        }
      }
    }

  }
}
