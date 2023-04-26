ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-http_2.13" % "10.5.0",
  "com.typesafe.akka" % "akka-http-caching_2.13" % "10.5.0",
  "com.typesafe.akka" % "akka-actor-typed_2.13" % "2.8.0",
  "com.typesafe.akka" % "akka-stream_2.13" % "2.8.0",
  "com.typesafe.akka" % "akka-slf4j_2.13" % "2.8.0",
  "ch.megard" % "akka-http-cors_2.13" % "1.2.0",
  "ch.qos.logback" % "logback-classic" % "1.4.6",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC7",

  "com.typesafe.akka" % "akka-testkit_2.13" % "2.8.0" % Test,
  "com.typesafe.akka" % "akka-stream-testkit_2.13" % "2.8.0" % Test,
  "com.typesafe.akka" % "akka-http-testkit_2.13" % "10.5.0" % Test,

  "org.scalatest" %% "scalatest" % "3.2.15"
)

lazy val root = (project in file("."))
  .settings(
    name := "CovidStat",
    idePackagePrefix := Some("com.innowise.covid")
  )
