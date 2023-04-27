package com.innowise.covid
package service

import cache.CacheProvider
import client.CovidApiClient
import client.exception.NotFoundException
import client.model.DayInfo
import model.{CountryDayStatistic, CountryStatistic, StatisticRequest}

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorAttributes, ActorMaterializer, Supervision}

import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.concurrent.Future

class StatisticService {

  implicit val reactiveStreamActor: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "stream-system")
  implicit val materializer: ActorMaterializer.type = ActorMaterializer
  private val covidApiService = CovidApiService()

  def getMaxAndMinForPeriodByCountry(countries: List[String], from: Instant, to: Instant): Future[Seq[CountryStatistic]] = {

    val decider: Supervision.Decider = {
      case _: NotFoundException => Supervision.Resume
      case _ => Supervision.Stop
    }

    Source(countries)
      .mapAsyncUnordered(4)(countryName => {
        CacheProvider.cache
          .getOrLoad((countryName, from, to), _ => covidApiService.getDayStatisticSeq(countryName, from, to))

      })
      .withAttributes(ActorAttributes.supervisionStrategy(decider))
      .map(dayStats => {

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
