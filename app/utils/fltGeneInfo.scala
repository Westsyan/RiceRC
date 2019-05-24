package utils

import java.io.File

import org.apache.commons.io.FileUtils
import play.api.Logger

import scala.collection.JavaConverters._

object fltGeneInfo {

  val path = "D:/水稻资源中心资料/Annotation/"

  def main(args: Array[String]): Unit = {

    new File(path).listFiles().foreach { x =>

      val name = x.getName

     getFileter(name)
      Logger.info(name + " gff过滤完成")
      addGO(name)
      Logger.info(name + " GO 添加完成")
      addPfam(name)
      Logger.info(name + " PFAM 添加完成")
      addIpr(name)
      Logger.info(name + " IPR 添加完成")
      addKegg(name)
      Logger.info(name + " Kegg 添加完成")
      addPathway(name)
      Logger.info(name + " Pathway 添加完成")
    }
  }

  def addPathway(name:String) = {
    val file = FileUtils.readLines(new File(path + name + "/" + name + ".IGDBv1.Allset.annotate.pathway")).asScala

    val map = file.map{x=>
      val c = x.split("\t")
      val tail = if(c.tail.mkString(";")==""){
        "-"
      }else{
        c.tail.mkString(";")
      }
      (c.head.dropRight(7), tail)
    }.toMap

    val f = FileUtils.readLines(new File(path + name + "/b4.flt")).asScala

    val row = f.map { x =>
      val column = x.split("\t")
      val geneid = column.head

      var pathway = map.getOrElse(geneid, "-")
      if (pathway == "") {
        pathway = "-"
      }

      geneid + "\t" + column.tail.mkString("\t") + "\t" + pathway
    }

    FileUtils.writeLines(new File(path + name + "/" + name + ".flt"), row.asJava)

  }

  def addKegg(name: String) = {

/*    val file = FileUtils.readLines(new File(path + name + "/" + name + ".IGDBv1.Allset.annotate.kegg_pathway")).asScala

    val map = file.map { x =>
      val c = x.split("\t")
      val tail = if(c.tail.mkString(";")==""){
        "-"
      }else{
        c.tail.mkString(";")
      }
      (c.head.dropRight(7), tail)
    }.toMap*/

    val kegg = FileUtils.readLines(new File("F:\\database\\RiceRC\\enrichData/" + name + "_kegg.xls")).asScala
    val kmap = kegg.map{x=>
      val k = x.split("\t")
      (k.head,k.last)
    }.toMap



    val f = FileUtils.readLines(new File(path + name + "/b3.flt")).asScala

    val row = f.map { x =>
      val column = x.split("\t")
      val geneid = column.head

      var ko = kmap.getOrElse(geneid, "-")
      if (ko == "") {
        ko = "-"
      }

      geneid + "\t" + column.tail.mkString("\t") + "\t" + ko
    }

    FileUtils.writeLines(new File(path + name + "/b4.flt"), row.asJava)
  }

  def addIpr(name: String) = {
    val file = FileUtils.readLines(new File(path + name + "/" + name + ".IGDBv1.Allset.interpro")).asScala

    val pfamMap = file.map { x =>
      val c = x.split("\t")
      val tail = if(c.tail.mkString(";")==""){
        "-"
      }else{
        c.tail.mkString(";")
      }
      (c.head.dropRight(7), tail)
    }.toMap


    val f = FileUtils.readLines(new File(path + name + "/b2.flt")).asScala

    val row = f.map { x =>
      val column = x.split("\t")
      val geneid = column.head

      geneid + "\t" + column.tail.mkString("\t") + "\t" + pfamMap.getOrElse(geneid, "-")
    }

    FileUtils.writeLines(new File(path + name + "/b3.flt"), row.asJava)
  }

  def addPfam(name: String) = {
    val file = FileUtils.readLines(new File(path + name + "/" + name + ".IGDBv1.Allset.annotate.pfam")).asScala

    val pfamMap = file.map { x =>
      val c = x.split("\t")
      val tail = if(c.tail.mkString(";")==""){
        "-"
      }else{
        c.tail.mkString(";")
      }
      (c.head.dropRight(7), tail)
    }.toMap


    val f = FileUtils.readLines(new File(path + name + "/b1.flt")).asScala

    val row = f.map { x =>
      val column = x.split("\t")
      val geneid = column.head

      geneid + "\t" + column.tail.mkString("\t") + "\t" + pfamMap.getOrElse(geneid, "-")
    }

    FileUtils.writeLines(new File(path + name + "/b2.flt"), row.asJava)
  }

  def addGO(name: String) = {

    val goFile = FileUtils.readLines(new File(path + name + "/" + name + ".IGDBv1.Allset.annotate.go")).asScala

    val goMap = goFile.map { x =>
      val c = x.split("\t")
      val tail = if(c.tail.mkString(";")==""){
        "-"
      }else{
        c.tail.mkString(";")
      }
      (c.head.dropRight(7), tail)
    }.toMap


    val f = FileUtils.readLines(new File(path + name + "/" + name + ".gff.flt")).asScala

    val row = f.map { x =>
      val column = x.split("\t")
      val gene = column.last.split(";").head
      val geneid = gene.slice(3, gene.length - 3)
      val chr = column.head
      val start = column(3)
      val end = column(4)
      val stand = column(6)

      geneid + "\t" + chr + "\t" + start + "\t" + end + "\t" + stand + "\t" + goMap.getOrElse(geneid, "-")
    }

    FileUtils.writeLines(new File(path + name + "/b1.flt"), row.asJava)


  }

  def getFileter(name: String) = {
    val f = FileUtils.readLines(new File(path + name + "/" + name + ".IGDBv1.Allset.gff")).asScala

    val row = f.map(_.split("\t")).filter(x => x(2) == "gene").map(_.mkString("\t"))

    FileUtils.writeLines(new File(path + name + "/" + name + ".gff.flt"), row.asJava)

  }
}
