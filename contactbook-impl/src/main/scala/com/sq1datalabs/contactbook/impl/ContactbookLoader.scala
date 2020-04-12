package com.sq1datalabs.contactbook.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.sq1datalabs.contactbook.api.ContactbookService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.softwaremill.macwire._
import com.sq1datalabs.contactbook.impl.repository.ContactBookRepository
import com.squareonedatalabs.ifrm.cognitiveods.impl.ContactbookSerializerRegistry

class ContactbookLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new ContactbookApplication(context) {
      println("++++++++++load method in lagom ApplicationLoader class")
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication = {
    println("++++++++++++indise Load Dev Mode method in ContactBookLoader")
    new ContactbookApplication(context) with LagomDevModeComponents
  }

  override def describeService = Some(readDescriptor[ContactbookService])
}




abstract class ContactbookApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {
  println("+++++++++++++ContactBookApplication abstract class inside contactbookLoader")

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[ContactbookService](wire[ContactbookServiceImpl])
  lazy val contactbookService = serviceClient.implement[ContactbookService]
  lazy val lagomRepository = wire[ContactBookRepository]

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = ContactbookSerializerRegistry

}
