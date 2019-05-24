package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test01 {


  def main(args: Array[String]): Unit = {

    new File("F:\\database\\RiceRC\\length").listFiles().foreach{x=>
      val f = FileUtils.readLines(x).asScala
      val filter =  f.filter(_.contains("Chr")).sortBy(_.split("\t").head.drop(3).toInt)

      FileUtils.writeLines(new File("F:\\database\\RiceRC/tmp/" + x.getName.split("_").head +
        "." + x.getName.split('.').takeRight(2).mkString(".")),filter.asJava)

    }

  }


}
