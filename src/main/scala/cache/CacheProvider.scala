package com.innowise.covid
package cache

import model.CountryDayStatistic

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{Cache, CachingSettings, LfuCacheSettings}

import java.time.Instant
import scala.concurrent.duration.Duration

object CacheProvider {
  implicit val cacheSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "cache-system")

  private val defaultCachingSettings: CachingSettings = CachingSettings(cacheSystem)
  private val lfuCacheSettings: LfuCacheSettings =
    defaultCachingSettings.lfuCacheSettings
      .withInitialCapacity(0)
      .withMaxCapacity(1024)
      .withTimeToLive(Duration.Inf)
      .withTimeToIdle(Duration.Inf)

  private val cachingSettings: CachingSettings =
    defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)

  val cache: Cache[(String, Instant, Instant), Seq[CountryDayStatistic]] = LfuCache(cachingSettings)

}
