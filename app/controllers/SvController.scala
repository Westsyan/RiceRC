package controllers

import java.io.File
import javax.inject.Inject

import dao._
import org.apache.commons.io.FileUtils
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import models.Tables._
import utils.Utils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SvController @Inject()(geneinfodao: geneinfoDao, blocksdao: blocksDao, msudao: msuDao) extends Controller {


  def insertMSU = Action {
    val file = FileUtils.readLines(new File("D:\\水稻资源中心资料\\共线性\\MSU/MSUv7.gff")).asScala
    val row = file.map(_.split("\t")).filter(x => x(2) == "gene").map { x =>
      val id = x.last.split(";").head.drop(3)
      MsuRow(0, id, x.head, x(3).toInt, x(4).toInt, x(6))
    }

    Await.result(msudao.insert(row), Duration.Inf)

    Ok(Json.toJson(1))
  }


  def getBlock = Action.async {

    val file = FileUtils.readLines(new File("D:\\水稻资源中心资料/02428.blocks")).asScala

    val bMap = file.map{x=>
      val c = x.split("\t")
      (c.head,c.last)
    }.toMap

    blocksdao.getAll.map { y =>

      y.foreach { z =>
        val g1 = Await.result(geneinfodao.getByRegion(z.tp, z.chr, z.start.toInt, z.end.toInt),Duration.Inf).map{x=>
          (x.geneid,x.chr,x.start,x.end)
        }
        val g2 = Await.result(msudao.getByRegion(z.g2Chr, z.g2Start.toInt, z.g2End.toInt),Duration.Inf).map { x =>
          (x.geneid, x.chr, x.start, x.end)
        }

        val e = g1.filter{q=>
         g2.map(_._1).contains(bMap.getOrElse(q._1,"none"))
        }.map{q=>
          val q2 = g2.filter(_._1 == bMap(q._1)).head
          println(z.id,q._1,q._2,q._3,q._4,q2._1,q2._2,q2._3,q2._4)
        }




      }


      Ok(Json.toJson(1))

    }


  }



  def insertBlock = Action{
    new File("D:\\水稻资源中心资料\\共线性\\bed").listFiles().filter(_.getName.contains("block")).foreach{x=>
      val file = FileUtils.readLines(x).asScala
      val row = file.map(_.split("\t")).filter(y=> y(3).contains("Chr") && y(6).contains("Chr")).map{c=>
        BlocksRow(0,c(1),c(2),c(3),c(4).toInt,c(5).toInt,c(6),c(7).toInt,c(8).toInt)
      }
      Await.result(blocksdao.insert(row),Duration.Inf)
    }

    Ok(Json.toJson(1))
  }




  def test = Action { implicit request =>
    Ok(views.html.block.test())
  }

  def getAllData = Action.async { implicit request =>
    blocksdao.getAll.map { x =>
      val row = x.sortBy(_.bid.drop(2).toInt).map { y =>
        Json.obj("id" -> y.id, "bid" -> y.bid, "tp" -> y.tp, "query" -> y.chr, "q_start" -> y.start, "q_stop" -> y.end,
          "ref" -> y.g2Chr, "r_start" -> y.g2Start, "r_stop" -> y.g2End)
      }
      Ok(Json.toJson(row))
    }
  }

  def circosBefore = Action { implicit request =>
    Ok(views.html.block.circos())
  }

  def getCircos(tp:String) = Action.async { implicit request =>

    blocksdao.getByTp(tp).map { x =>
      val ur = x.groupBy(_.chr).flatMap(_._2.groupBy(_.g2Chr).map(_._2.maxBy(z => z.end - z.start)))

      val nameMap = Map("Chr1" -> "1", "Chr2" -> "2", "Chr3" -> "3", "Chr4" -> "4", "Chr5" -> "5", "Chr6" -> "6",
        "Chr7" -> "7", "Chr8" -> "8", "Chr9" -> "9", "Chr10" -> "10", "Chr11" -> "11", "Chr12" -> "12")
      val row = ur.map { y =>
        Json.obj("bid" -> y.bid, "color" -> "black", "id" -> y.id,
          "source" -> Json.obj("chr" -> nameMap(y.chr), "name" -> ("A." + y.chr), "start" -> y.start, "end" -> (y.start + (y.end - y.start) * 15)),
          "target" -> Json.obj("chr" -> (nameMap(y.g2Chr).toInt + 12), "name" -> ("B." + y.g2Chr), "start" -> y.g2Start, "end" -> (y.g2Start + (y.g2End - y.g2Start) * 15)))
      }

      val query = FileUtils.readLines(new File(Utils.path + s"/length/$tp.query.genome")).asScala
      val ref = FileUtils.readLines(new File(Utils.path + s"/length/$tp.ref.genome")).asScala

      val queryRow = query.zipWithIndex.map{x=>
        val chr = x._1.split("\t").head
        val length = x._1.split("\t").last
        Json.obj("id" -> (x._2+1),"chr" -> ("A." + chr),"length" -> length.toInt,"color" -> "blue")
      }

      val refRow = ref.zipWithIndex.map{x=>
        val chr = x._1.split("\t").head
        val length = x._1.split("\t").last
        Json.obj("id" -> (x._2+13),"chr" -> ("B." + chr),"length" -> length.toInt,"color" -> "red")
      }

      val collection = queryRow ++ refRow


      val table = ur.toArray.sortBy(_.bid.split("_").last.toInt).map { y =>
        Json.obj("id" -> y.id, "bid" -> y.bid, "tp" -> y.tp, "query" -> y.chr, "q_start" -> y.start, "q_stop" -> y.end,
           "q_gap" -> (y.end - y.start), "ref" -> y.g2Chr, "r_start" -> y.g2Start, "r_stop" -> y.g2End,
           "r_gap" -> (y.end - y.start))
      }


      Ok(Json.obj("ur" -> row,"collection" -> collection,"table" -> table))

    }

  }

  def blocksBefore(id:Int) = Action{implicit request=>
    Ok(views.html.block.blocks(id))
  }


  def getDrawData(id: Int) = Action { implicit request =>

    val info = Await.result(blocksdao.getById(id), Duration.Inf)

    val g1 =  Utils.geneMap(info.tp).filter(_.chr == info.chr).filter(_.start >= info.start.toInt).
      filter(_.end <= info.end.toInt).map{x=>
      (x.geneid,x.start,x.end)
    }

    val g2 = Await.result(msudao.getByRegion(info.g2Chr, info.g2Start.toInt, info.g2End.toInt),Duration.Inf).map { x =>
      (x.geneid, x.start, x.end)
    }


    val file = FileUtils.readLines(new File(Utils.path + "/compareToMsu/" + info.tp + ".blocks")).asScala

    val bMap = file.map{x=>
      val c = x.split("\t")
      (c.head,c.last)
    }.toMap



    val rect = g1.filter{q=>
      g2.map(_._1).contains(bMap.getOrElse(q._1,"none"))
    }.map{q=>
      val q2 = g2.filter(_._1 == bMap(q._1)).head
      (q._1,q._2,q._3,q2._1,q2._2,q2._3)
    }




    val otherA = g1.filter(x=> !rect.map(_._1).contains(x._1))
    val otherB = g2.filter(x=> !rect.map(_._4).contains(x._1))

    val relate = rect.zipWithIndex.map{x=>
      Array(x._2 + 1,x._2 + 1)
    }

    val rectA = (rect.map(x=>(x._1,x._2,x._3)) ++ otherA).map(y=>Array(y._1, y._2.toString, y._3.toString)).distinct
    val rectB = (rect.map(x=>(x._4,x._5,x._6)) ++ otherB).map(y=>Array(y._1, y._2.toString, y._3.toString)).distinct

    val row = Json.obj("A" -> Json.obj("name" -> info.chr, "min" -> info.start, "max" -> info.end, "rect" -> rectA),
      "B" -> Json.obj("name" -> info.g2Chr, "min" -> info.g2Start, "max" -> info.g2End, "rect" -> rectB))

    val table = (rect.map(x=> (x._1,x._4)) ++ otherA.map(x=> (x._1,"NA")) ++ otherB.map(x=> ("NA",x._1))).map{x=>
      Json.obj("gene1" -> x._1,"gene2" -> x._2)
    }

    val blocks = Json.obj("bid" -> info.bid, "organism1" -> info.tp,
      "location1" -> (info.chr + " : " + info.start + " - " + info.end),
      "location2" -> (info.g2Chr + " : " + info.g2Start + " - " + info.g2End)
    )


    Ok(Json.obj("data" -> row, "relate" -> relate, "table" -> table, "block" -> blocks))

  }


}
