package com.innowise.covid
package service

import client.CovidApiClient
import client.model.DayInfo
import model.{CountryStatistic, CountryDayStatistic, StatisticRequest}

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import java.time.Instant
import scala.concurrent.Future

class StatisticService {

  implicit val fetchActor: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val materializer: ActorMaterializer.type = ActorMaterializer
  private val covidApiService = CovidApiService()

  def getMaxAndMinForPeriodByCountry(countries: List[String], from: Instant, to: Instant): Future[Seq[CountryStatistic]] = {

    Source(countries).mapAsyncUnordered(4)(countryName => {
      covidApiService.getDayStatistics(countryName, from, to)
    }).map(dayStats => {

      println(dayStats)

      val maxCasesDay = dayStats.maxBy(_.newCases)
      val minCasesDay = dayStats.minBy(_.newCases)

      CountryStatistic(
        dayStats.head.country, 
        maxCasesDay.newCases, maxCasesDay.date, 
        minCasesDay.newCases, minCasesDay.date, 
        from, to
      )
    }).runWith(Sink.seq)
  }
}
