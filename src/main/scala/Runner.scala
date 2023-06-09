package com.innowise.covid

import controller.StatisticController

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.path
import akka.stream.ActorMaterializer

import scala.collection.mutable.ListBuffer
import scala.io.StdIn
import akka.event.slf4j.Slf4jLogger
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor

@main
def main(): Unit = {

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "runner-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val route = cors() {
    StatisticController.route
  }

  val bindingFuture = Http().newServerAt("localhost", 8081).bind(route)

  println("Server runs on port 8081")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}