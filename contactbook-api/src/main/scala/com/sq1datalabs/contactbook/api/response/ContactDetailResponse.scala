package com.sq1datalabs.contactbook.api.response

import java.util

import com.datastax.driver.core.Row
import play.api.libs.json._

case class ContactDetailResponse(id: String, firstName: String, lastName: String, contactNumber:String)

object ContactDetailResponse{

  def apply(row: Row): ContactDetailResponse = ContactDetailResponse.create(row)

    def apply(seqRow:Seq[Row]): util.ArrayList[ContactDetailResponse] = {
      val contactDetailResponseList=createList(seqRow)
      contactDetailResponseList
    }

    def create(row: Row)=
      ContactDetailResponse(
        id=row.getString("id"),
        firstName=row.getString("first_nm"),
        lastName=row.getString("last_nm"),
        contactNumber=row.getString("contact_nbr")
      )

  def createList(seqRow:Seq[Row]): util.ArrayList[ContactDetailResponse] ={
    val contactDetailResponseList=new util.ArrayList[ContactDetailResponse]
    seqRow.map((row: Row) => {
      contactDetailResponseList.add(create(row))
    })
    contactDetailResponseList
  }

  implicit lazy val jsonContactDetailResponse = Json.format[ContactDetailResponse]
  implicit lazy val jsonContactDetailResponseList:Format[ContactDetailResponse]=Json.format


}