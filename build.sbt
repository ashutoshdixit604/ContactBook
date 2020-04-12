import com.lightbend.lagom.sbt.LagomImport.lagomScaladslServer
import com.typesafe.config.ConfigFactory

organization in ThisBuild := "com.sq1datalabs"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test
val jsonExtensions = "ai.x" %% "play-json-extensions" % "0.10.0"
val joda = "joda-time" % "joda-time" % "2.9.9"
val playJsonJoda = "com.typesafe.play" %% "play-json-joda" % "2.6.0-RC1"


lazy val `contactbook` = (project in file("."))
  .aggregate(`contactbook-api`, `contactbook-impl`)

lazy val `common` = (project in file("common"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer,
      lagomScaladslPersistenceCassandra
    )
  )



lazy val `contactbook-api` = (project in file("contactbook-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer,
      lagomScaladslPersistenceCassandra
    )
  ).dependsOn(`common`)

lazy val `contactbook-impl` = (project in file("contactbook-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      joda,
      guice
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`contactbook-api`)

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false


lagomUnmanagedServices in ThisBuild := Map(
  "cas_native" -> ConfigFactory.load().getString("cassandra.url")
)