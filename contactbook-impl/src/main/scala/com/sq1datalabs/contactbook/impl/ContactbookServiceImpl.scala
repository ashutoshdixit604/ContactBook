package com.sq1datalabs.contactbook.impl


import com.sq1datalabs.contactbook.api.ContactbookService
import akka.{Done, NotUsed}
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.util.Timeout
import com.sq1datalabs.contactbook.api.request.ContactDetailRequest
import com.sq1datalabs.contactbook.api.response.ContactDetailResponse
import com.sq1datalabs.contactbook.impl.repository.ContactBookRepository

/**
  * Implementation of the ContactbookService.
  */
class ContactbookServiceImpl(contactBookRepository: ContactBookRepository)(
  clusterSharding: ClusterSharding,
  persistentEntityRegistry: PersistentEntityRegistry
)(implicit ec: ExecutionContext)
  extends ContactbookService {

  /**
   * Looks up the entity for the given ID.
   */

  implicit val timeout = Timeout(5.seconds)

  override def getContactDetailById(id: String): ServiceCall[NotUsed, ContactDetailResponse] = ServiceCall{
    request => contactBookRepository.getContactDetailbyIdResult(id)
  }

  override def getAllContactDetail():ServiceCall[NotUsed,Seq[ContactDetailResponse]] = ServiceCall{
    request => contactBookRepository.getAllContactDetails()
  }

  override def addContactDetail(): ServiceCall[ContactDetailRequest, Done] =ServiceCall{
    request => contactBookRepository.insertContactDetail(request)
  }

 /* override def addMultipleContactDetail(): ServiceCall[Seq[ContactDetailRequest], Done] =ServiceCall{

      request =>contactBookRepository.insertAllContactDetail(request)
  }*/

  override def updateContactDetail():ServiceCall[ContactDetailRequest,Done] =ServiceCall{
    request =>  contactBookRepository.updateContactDetail(request)
  }

  override def deleteContactDetail(id: String): ServiceCall[NotUsed,Done]=ServiceCall{
    request => contactBookRepository.deleteContactDetail(id)
  }
}


