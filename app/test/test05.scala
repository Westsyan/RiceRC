package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test05 {


  def main(args: Array[String]): Unit = {


    new File("D:\\水稻资源中心资料\\Annotation").listFiles().foreach{x=>
      val file = FileUtils.readLines(new File(x.getAbsolutePath,"b1.flt")).asScala
      val go = file.map{y=>
        val c = y.split("\t")
        (c.head,c.last)
      }
      val gene = go.map(_._1)
      val gos = go.filter(_._2 != "-").map(y=> y._1 + "\t" + y._2)

      FileUtils.writeLines(new File("F:\\database\\RiceRC\\enrichData/" + x.getName + "_gene.xls"),gene.asJava)
      FileUtils.writeLines(new File("F:\\database\\RiceRC\\enrichData/" + x.getName + "_go.xls"),gos.asJava)
    }

  }
}
