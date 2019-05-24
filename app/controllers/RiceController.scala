package controllers

import java.io.File
import javax.inject.Inject

import dao._
import models.Tables._
import org.apache.commons.io.FileUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.Utils

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

class RiceController @Inject()(geneinfodao: geneinfoDao, sampledao: sampleDao,refseqdao:refseqDao) extends Controller {


  def insert = Action {
    val path = "D:/水稻资源中心资料/Annotation/"
    new File(path).listFiles().foreach { x =>
      val name = x.getName
      val file = FileUtils.readLines(new File(path + name + "/" + name + ".flt")).asScala
      val row = file.map { x =>
        val column = x.split("\t")
        GeneinfoRow(0, name, column.head, column(1), column(2).toInt, column(3).toInt, column(4), column(5), column(6), column(7), column(8), column.last)
      }
      println(name)
      Await.result(geneinfodao.insert(row), Duration.Inf)
    }
    Ok(Json.toJson("1"))
  }


  def insertSeq = Action{

    val path = "D:/水稻资源中心资料/Annotation/"
    new File(path).listFiles().foreach{x=>
      val name = x.getName
      val cds = FileUtils.readLines(new File(x.getAbsolutePath + "/cds.flt.fa")).asScala.grouped(2).map{y=>
        (y.head.tail.trim,y.last.toUpperCase)
      }.toMap

      val pep = FileUtils.readLines(new File(x.getAbsolutePath + "/pep.flt.fa")).asScala.grouped(2).map{y=>
        (y.head.tail.trim,y.last.toUpperCase)
      }.toMap

      val trans = FileUtils.readLines(new File(x.getAbsolutePath + "/trans.flt.fa")).asScala.grouped(2).map{y=>
        (y.head.tail.trim,y.last.toUpperCase)
      }.toMap

      val geneInfo = Await.result(geneinfodao.getAllBySpecies(name),Duration.Inf)
      val row = geneInfo.map{y=>
        println(y.id,y.geneid)
        if(cds.getOrElse(y.geneid,"-") == "-"){
          RefseqRow(y.id,"","","")
        }else{
          RefseqRow(y.id,cds(y.geneid),pep(y.geneid),trans(y.geneid))
        }
      }

      Await.result(refseqdao.insert(row),Duration.Inf)
      println(name)
    }

    Ok(Json.toJson("1"))
  }

  def checkSeq = Action{

    new File("D:/水稻资源中心资料/Annotation").listFiles().foreach{x=>
      val name = x.getName
      val gene = Await.result(geneinfodao.getAllBySpecies(name),Duration.Inf).map(x=>(x.geneid,x.id)).toMap
      val cds = FileUtils.readLines(new File(x.getAbsolutePath,"cds.flt.fa")).asScala

      cds.grouped(2).foreach{y=>
        if(gene.getOrElse(y.head.tail,"-") == "-"){
          println(name,y.head)
        }
      }
    }


    Ok(Json.toJson("1"))
  }

  def updateKegg = Action{

    new File("D:\\水稻资源中心资料\\Annotation").listFiles().foreach{x=>
      val name = x.getName
      val keggFile = FileUtils.readLines(new File("F:\\database\\RiceRC\\enrichData/" + name + "_kegg.xls")).asScala
      keggFile.foreach { k =>
        val ks = k.split("\t")
        val geneId = ks.head
        val ko = ks.last
        println(name + "\t" + geneId)
        Await.result(geneinfodao.updateKegg(name,geneId,ko),Duration.Inf)
      }
    }


    Ok(Json.toJson("1"))
  }


  def getLength = Action {
    val path = "D:/水稻资源中心资料/Annotation/"
    new File(path).listFiles().foreach { x =>
      val name = x.getName
      val l = Await.result(geneinfodao.getBySpecies(name), Duration.Inf).length
      println(name + "\t" + l)

    }

    Ok(Json.toJson("1"))
  }


  def browse = Action {
    Ok(views.html.rice.browse())
  }

  def getAllSample = Action.async {
    sampledao.getAllSample.map { x =>
      val row = x.map { y =>
        Json.obj("sample" -> y.sample, "nums" -> y.nums, "detail" -> y.detail)
      }
      Ok(Json.toJson(row))
    }
  }


  def riceSpecie(species: String) = Action {
    Ok(views.html.rice.riceSpecies(species))
  }

  val pageForm = Form(
    mapping(
      "limit" -> number,
      "offset" -> number,
      "order" -> text,
      "search" -> optional(text),
      "sort" -> optional(text)
    )(PageData.apply)(PageData.unapply)
  )

  def getAllRice(species: String) = Action {implicit request=>

    val page = pageForm.bindFromRequest.get
    val x = Utils.geneMap(species)
    val orderX = Utils.dealDataByPage(x, page)
    val total = orderX.size
    val tmpX = orderX.slice(page.offset, page.offset + page.limit)
    val row = tmpX.map { y =>
      Json.obj("id" -> y.id,"geneid" -> y.geneid, "chr" -> y.chr, "start" -> y.start, "end" -> y.end, "strand" -> y.stand, "go" -> y.go,
        "pfam" -> y.pfam, "ipr" -> y.ipr, "ko" -> y.ko,"pathway" -> y.pathway)
    }
    Ok(Json.obj("rows" -> row, "total" -> total))
  }

  def moreInfo(id:Int) = Action{
    val gene = Await.result(geneinfodao.getById(id),Duration.Inf)
    val ref = Await.result(refseqdao.getById(id),Duration.Inf)
    Ok(views.html.rice.moreInfo(gene,ref))
  }

}
