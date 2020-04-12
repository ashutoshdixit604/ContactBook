package com.squareonedatalabs.ifrm.cognitiveods.impl

import java.util

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.sq1datalabs.contactbook.api.request.ContactDetailRequest
import com.sq1datalabs.contactbook.api.response.ContactDetailResponse

object ContactbookSerializerRegistry extends JsonSerializerRegistry {
  println("+++++++++++++++++ContactbookSerializerRegistry")
  override val serializers = Vector(
    JsonSerializer[ContactDetailRequest],
    JsonSerializer[ContactDetailResponse]
  )
}
