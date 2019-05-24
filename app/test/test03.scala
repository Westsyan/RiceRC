package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test03 {


  def main(args: Array[String]): Unit = {
    new File("F:\\database\\RiceRC\\compareToMsu\\tmp").listFiles().foreach{x=>
      val f = FileUtils.readLines(x).asScala
      val row = f.map(_.split("\t")).filter(_.last != ".").map{y=>
        y.head.split('.').head + "\t" + y.last.split('.').head
      }

      FileUtils.writeLines(new File("F:\\database\\RiceRC\\compareToMsu/" + x.getName.split('.').head + ".blocks"),row.asJava)

    }
  }
}
