package test

import java.io.File

import org.apache.commons.io.FileUtils
import play.api.libs.json.Json

import scala.collection.JavaConverters._

object test02 {


  def main(args: Array[String]): Unit = {


    val query = FileUtils.readLines(new File("F:\\database\\RiceRC\\length/02428.query.genome")).asScala
    val ref = FileUtils.readLines(new File("F:\\database\\RiceRC\\length/02428.ref.genome")).asScala

    val queryRow = query.zipWithIndex.map{x=>
      val chr = x._1.split("\t").head
      val length = x._1.split("\t").last
      Json.obj("id" -> x._2,"chr" -> ("A." + chr),"length" -> length,"color" -> "blue")
    }

    val refRow = ref.zipWithIndex.map{x=>
      val chr = x._1.split("\t").head
      val length = x._1.split("\t").last
      Json.obj("id" -> (x._2+12),"chr" -> ("B." + chr),"length" -> length,"color" -> "red")
    }

    (queryRow ++ refRow).foreach(println)



  }
}
