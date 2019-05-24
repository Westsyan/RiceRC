package dao

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import models.Tables._

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class blocksDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def insert(row:Seq[BlocksRow]) : Future[Unit] = {
    db.run(Blocks ++= row).map(_=>())
  }

  def getAll : Future[Seq[BlocksRow]] = {
    db.run(Blocks.result)
  }

  def getByTp(tp:String) : Future[Seq[BlocksRow]] = {
    db.run(Blocks.filter(_.tp === tp).result)
  }

  def getById(id:Int) : Future[BlocksRow] = {
    db.run(Blocks.filter(_.id === id).result.head)
  }


}
