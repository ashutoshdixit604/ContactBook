package com.sq1datalabs.contactbook.api.request

import com.datastax.driver.core.Row
import com.sq1datalabs.contactbook.api.response.ContactDetailResponse
import play.api.libs.json.{Format, Json}

case class ContactDetailRequest(id: String, firstName: String, lastName: String, contactNumber:String)

object ContactDetailRequest{

  def apply(row: Row):ContactDetailRequest = ContactDetailRequest.create(row)

  def create(row : Row)=
    ContactDetailRequest(
      id=row.getString("id"),
      firstName=row.getString("first_nm"),
      lastName=row.getString("last_nm"),
      contactNumber=row.getString("contact_nbr")
    )

  implicit lazy val jsonContactDetailReponse = Json.format[ContactDetailRequest]
  implicit lazy val jsonContactDetailRequestList:Format[ContactDetailRequest]=Json.format
}
