package com.innowise.covid
package service

import client.CovidApiClient
import client.model.DayInfo
import model.CountryDayStatistic

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

import java.time.Instant
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}

class CovidApiService {

  implicit val serviceActorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "api-system")
  implicit val executionContext: ExecutionContextExecutor = serviceActorSystem.executionContext

  private val covidApiClient: CovidApiClient = new CovidApiClient()

  def getDayStatistics(countryName: String, from: Instant, to: Instant): Future[Seq[CountryDayStatistic]] = {

    covidApiClient.fetchByCountryName(countryName, from, to).map(countDayStatisticForDateRange)
  }

  private def countDayStatisticForDateRange(infos: Seq[DayInfo]): Seq[CountryDayStatistic] = {
    val dayStatisticList = ListBuffer[CountryDayStatistic]()

    for (i <- 0 until infos.length - 1) {

      val current = infos.apply(i)
      val next = infos.apply(i + 1)

      dayStatisticList.addOne(
        CountryDayStatistic(current.country, next.cases - current.cases, current.date)
      )
    }

    dayStatisticList.toSeq
  }

}