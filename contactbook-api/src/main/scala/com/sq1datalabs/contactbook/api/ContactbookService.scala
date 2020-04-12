package com.sq1datalabs.contactbook.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.sq1datalabs.contactbook.api.request.ContactDetailRequest
import com.sq1datalabs.contactbook.api.response.ContactDetailResponse


trait ContactbookService extends Service {

  def getContactDetailById(id: String): ServiceCall[NotUsed, ContactDetailResponse]
  def getAllContactDetail(): ServiceCall[NotUsed,Seq[ContactDetailResponse]]
  def addContactDetail():ServiceCall[ContactDetailRequest,Done]
 // def addMultipleContactDetail():ServiceCall[Seq[ContactDetailRequest],Done]
  def updateContactDetail():ServiceCall[ContactDetailRequest,Done]
  def deleteContactDetail(id: String): ServiceCall[NotUsed,Done]

  override final def descriptor: Descriptor = {
    import Service._
    // @formatter:off
    named("contactbook")
      .withCalls(
        restCall(Method.GET, "/api/get/contact/id/:id", getContactDetailById _),
        restCall(Method.GET, "/api/get/all/contact", getAllContactDetail _),
        restCall(Method.POST,"/api/add/contact/",addContactDetail _),
   //     restCall(Method.POST,"/api/add/multiple/contact",addMultipleContactDetail _),
        restCall(Method.PUT,"/api/update/contact/id", updateContactDetail _),
        restCall(Method.DELETE,"/api/delete/contact/id/:id", deleteContactDetail _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}