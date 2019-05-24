package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class geneinfoDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def insert(row: Seq[GeneinfoRow]): Future[Unit] = {
    db.run(Geneinfo ++= row).map(_ => ())
  }

  def getAll: Future[Seq[GeneinfoRow]] = {
    db.run(Geneinfo.result)
  }

  def getAllBySpecies(species: String): Future[Seq[GeneinfoRow]] = {
    db.run(Geneinfo.filter(_.species === species).result)
  }

  def getByGeneIds(species: String, ids: Seq[String]): Future[Seq[GeneinfoRow]] = {
    db.run(Geneinfo.filter(_.species === species).filter(_.geneid.inSetBind(ids)).result)
  }

  def getById(id: Int): Future[GeneinfoRow] = {
    db.run(Geneinfo.filter(_.id === id).result.head)
  }

  def getByGeneId(id:String) :Future[GeneinfoRow] = {
    db.run(Geneinfo.filter(_.geneid === id).result.head)
  }

  def getByRegion(species: String, chr: String, start: Int, end: Int): Future[Seq[GeneinfoRow]] = {
    db.run(Geneinfo.filter(_.species === species).filter(_.chr === chr).filter(_.start >= start).filter(_.end <= end).result)
  }

  def getBySpecies(species: String): Future[Seq[Int]] = {
    db.run(Geneinfo.filter(_.species === species).map(_.id).result)
  }

  def getIdByGeneId(geneid: String): Future[Int] = {
    db.run(Geneinfo.filter(_.geneid === geneid).map(_.id).result.head)
  }

  def updateKegg(species: String, geneid: String, kegg: String): Future[Unit] = {
    db.run(Geneinfo.filter(_.species === species).filter(_.geneid === geneid).map(_.ko).update(kegg)).map(_ => ())
  }

  def searchByInput(input: String): Future[Seq[GeneinfoRow]] = {
    val i = "%" + input + "%"
    db.run(Geneinfo.filter(x => x.geneid.like(i) || x.go.like(i) || x.pfam.like(i) || x.ipr.like(i)
      || x.ko.like(i) || x.pathway.like(i)).result)
  }

  def searchByInput1(input: String): Future[Seq[GeneinfoRow]] = {
    val i = "%" + input + "%"
    db.run(Geneinfo.filter(_.geneid.like(i)).
      union(Geneinfo.filter(_.go.like(i))).
      union(Geneinfo.filter(_.pfam.like(i))).
      union(Geneinfo.filter(_.ipr.like(i))).
      union(Geneinfo.filter(_.ko.like(i))).
      union(Geneinfo.filter(_.pathway.like(i))).
      result)
  }

}
