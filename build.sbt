ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.5.0",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
  "com.typesafe.akka" %% "akka-stream" % "2.8.0",
  "com.typesafe.akka" %% "akka-slf4j" % "2.8.0",
  "ch.qos.logback" % "logback-classic" % "1.4.6",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC7"
)

lazy val root = (project in file("."))
  .settings(
    name := "CovidStat",
    idePackagePrefix := Some("com.innowise.covid")
  )
