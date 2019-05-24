package test

import java.io.File

import org.apache.commons.io.FileUtils
import utils.Crawler.Crawler

import scala.collection.JavaConverters._

object test06 {


  def main(args: Array[String]): Unit = {


    val file = FileUtils.readLines(new File("D:\\水稻资源中心资料\\Annotation\\R498/R498.IGDBv1.Allset.annotate.pathway")).asScala

    val name = "R498"

    val g2 = file.map{x=>
      val c =x.split("\t")
      (c.head.take(17) ,c.tail.filter(_.contains("KEGG:")))
    }.filter(_._2.length != 0)


    val map = g2.map { y =>
      val urls = y._2.map { z =>
        val pathway = z.split('+')
        val ko = pathway.head.takeRight(5)
        val ec = pathway.last
        s"http://rest.kegg.jp/link/ko/ko$ko+ec:$ec"
      }
      (y._1, urls)
    }.toMap


    FileUtils.writeStringToFile(new File(s"F:/database/RiceRC/enrichData/$name.kegg.xls"),"")

    var f2 = FileUtils.readLines(new File(s"F:/database/RiceRC/enrichData/$name.kegg.xls")).asScala
    val us = map.flatMap(_._2).toArray.distinct
    var keggExist = f2.map(_.split("\t").head)

    while (us.diff(keggExist).length != 0) {
      try {
        var code = new Crawler("crawle", false)
        code.setThreads(1000)

        us.diff(keggExist).foreach { x =>
          println(us.indexOf(x) + "/" + us.length + "\t" + name)
          val html = code.getResponse(code.addSeedAndReturn(x)).html()
          val k = html.split("\n").map(_.split("\t")).groupBy(_.head)
          val koArray = k.head._2.map(_.last)
          val ecArray = k.last._2.map(_.last)
          val kegg = koArray.intersect(ecArray)
          val line = x + "\t" + kegg.mkString(";") + "\n"
          println(line)
          FileUtils.writeStringToFile(new File(s"F:/database/RiceRC/enrichData/$name.kegg.xls"), line, true)
        }

        code.start(1)
      } catch {
        case e: Exception =>
          f2 = FileUtils.readLines(new File(s"F:/database/RiceRC/enrichData/$name.kegg.xls")).asScala
          keggExist = f2.map(_.split("\t").head)
      }
    }


    val f3 = FileUtils.readLines(new File(s"F:/database/RiceRC/enrichData/$name.kegg.xls")).asScala

    val w = f3.map(_.split("\t")).filter(_.length == 2).map(x=>(x.head,x.last)).toMap

    val row = map.map{x=>
      println(x._2.map(y=> w.getOrElse(y,"")).mkString(";").split(";").map(_.drop(3)).distinct.mkString(";"))
      x._1  + "\t"  + x._2.map(y=> w.getOrElse(y,"")).mkString(";").split(";").map(_.drop(3)).distinct.mkString(";")
    }.filter(_.split("\t").length == 2).toBuffer


    new File(s"F:/database/RiceRC/enrichData/$name.kegg.xls").delete()
    FileUtils.writeLines(new File(s"F:/database/RiceRC/enrichData/${name}_kegg.xls"),row.asJava)




  }
}
