package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test {


  def main(args: Array[String]): Unit = {


    new File("D:/水稻资源中心资料/Annotation/").listFiles().foreach{x=>
      val name = x.getName


      val gff = FileUtils.readLines(new File(s"D:/水稻资源中心资料/Annotation/$name/$name.IGDBv1.Allset.gff")).asScala

    val row = gff.map(_.split("\t")).filter(x => x(2) == "gene" || x(2) == "exon" || x(2) == "CDS" ).map{x=>
      if(x(2) == "gene"){
       x.init.mkString("\t") + "\t" +  x.last.split('.').head+";"
      }else{
        x.init.mkString("\t") + "\t" + x.last.split(";").last.split('.').head+";"
      }
    }

    FileUtils.writeLines(new File(s"D:/水稻资源中心资料/Annotation/$name/$name.gff"),row.asJava)
    }



  }

}
