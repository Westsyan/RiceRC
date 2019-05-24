package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class msuDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile]  {

  import profile.api._

  def insert(row: Seq[MsuRow]) : Future[Unit] = {
    db.run(Msu ++= row).map(_=>())
  }

  def getByGeneId(geneid:String) : Future[MsuRow] = {
    db.run(Msu.filter(_.geneid === geneid).result.head)
  }

  def getAll : Future[Seq[MsuRow]] = {
    db.run(Msu.result)
  }

  def getByRegion(chr: String, start: Int, end: Int): Future[Seq[MsuRow]] = {
    db.run(Msu.filter(_.chr === chr).filter(_.start >= start).filter(_.end <= end).result)
  }


}
