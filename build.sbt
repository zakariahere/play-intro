name := """playfinal2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.18"

libraryDependencies ++= Seq(guice, "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "org.mongodb.scala" %% "mongo-scala-driver" % "5.13.0",
  "org.typelevel" %% "cats-core" % "2.13.0")

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
