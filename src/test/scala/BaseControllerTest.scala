package com.innowise.covid

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt

trait BaseControllerTest extends AnyWordSpec with Matchers with ScalatestRouteTest {

}
