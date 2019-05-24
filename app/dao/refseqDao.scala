package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class refseqDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def insert(row:Seq[RefseqRow]) : Future[Unit] = {
    db.run(Refseq ++= row).map(_=>())
  }

  def getById(id:Int) : Future[RefseqRow] = {
    db.run(Refseq.filter(_.id === id).result.head)
  }


}
