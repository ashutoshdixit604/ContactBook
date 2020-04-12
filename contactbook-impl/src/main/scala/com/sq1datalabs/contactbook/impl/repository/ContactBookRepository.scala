package com.sq1datalabs.contactbook.impl.repository

import java.util

import akka.Done
import akka.actor.Status.{Failure, Success}
import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.api.transport.BadRequest
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.sq1datalabs.contactbook.api.request.ContactDetailRequest
import com.sq1datalabs.contactbook.api.response.ContactDetailResponse
import com.sq1datalabs.contactbook.impl.repository.ContactBookRepository._
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

class ContactBookRepository(session: CassandraSession)(implicit ec: ExecutionContext) {

  private val logger = LoggerFactory.getLogger(classOf[ContactBookRepository])


  private def fetchContactDetailByIdStatement() = {
    session.prepare(fetchContactDetailByIdPreparedStatement)
  }

  private def fetchContactDetailStatement() = {
    session.prepare(fetchContactDetailPreparedStatement)
  }

  private def insertContactDetailStatement()={
    session.prepare(inserContactDetailPreparedStatement)
  }

  private def updateContactDetailStatement()={
    session.prepare(updateContactDetailPreparedStatement)
  }

  private def deleteContactDetailStatement()={
    session.prepare(deleteContactDetailPreparedStatement)
  }


  def getContactDetailbyIdResult(id: String) = {
    fetchContactDetailByIdStatement().flatMap(stmt =>
      session.selectOne(stmt.bind(id)))
      .map(row =>

      row match {
        case Some(res) => ContactDetailResponse(res)
        case None => throw BadRequest("Record Not Found ")
      }
    )
  }

  def getAllContactDetails() = {
    fetchContactDetailStatement().flatMap (stmt =>
      session.selectAll(stmt.bind()).map((rows: Seq[Row]) =>
        rows.map((row: Row) => {
          ContactDetailResponse(row)
        }))
    )
  }

  def insertContactDetail(request:ContactDetailRequest)= {

    insertContactDetailStatement().flatMap(stmt =>
      session.executeWrite(stmt.bind(
        request.id,
        request.firstName,
        request.lastName,
        request.contactNumber))
    )
  }

  def insertAllContactDetail(request:Seq[ContactDetailRequest])= {

    for {
      req <- request
    }
      yield {
        insertContactDetailStatement().flatMap(stmt =>
          session.executeWrite(stmt.bind(
            req.id,
            req.firstName,
            req.lastName,
            req.contactNumber))
        )
      }
  }


  def updateContactDetail(request:ContactDetailRequest)={
    updateContactDetailStatement().flatMap(stmt =>
      session.executeWrite(stmt.bind(
        request.firstName,
        request.lastName,
        request.contactNumber,
        request.id
      ))
    )
  }

  def deleteContactDetail(id:String)={
    deleteContactDetailStatement().flatMap(stmt =>
      session.executeWrite(stmt.bind(id))
    )
  }
}

object ContactBookRepository {
  lazy val fetchContactDetailByIdPreparedStatement =
    s"""
       |SELECT
       |id,
       |first_nm,
       |last_nm,
       |contact_nbr
       |FROM CONTACT_DETAILS
       |WHERE id= ?
  """.stripMargin

  lazy val fetchContactDetailPreparedStatement =
    s"""
       |SELECT
       |id, first_nm, last_nm, contact_nbr
       |FROM CONTACT_DETAILS
  """.stripMargin

  lazy val inserContactDetailPreparedStatement =
    s"""
      |INSERT INTO CONTACT_DETAILS
      |(ID,
      |FIRST_NM,
      |LAST_NM,
      |CONTACT_NBR)
      |VALUES(${List.fill(4)("?").mkString(",")})""".stripMargin

  lazy val updateContactDetailPreparedStatement =
    s"""
      |UPDATE CONTACT_DETAILS
      |SET
      |first_nm=?,
      |last_nm=?,
      |contact_nbr=?
      |WHERE id=?
      """.stripMargin

  lazy val deleteContactDetailPreparedStatement =
    s"""
       |DELETE FROM CONTACT_DETAILS
       |WHERE id=?
      """.stripMargin

}