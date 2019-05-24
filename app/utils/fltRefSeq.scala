package utils

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object fltRefSeq {


  def main(args: Array[String]): Unit = {

    new File("D:/水稻资源中心资料/Annotation/").listFiles().foreach{x=>
      val name = x.getName
      fltCds(name)
      fltPep(name)
      fltTrans(name)
      println(name)
    }


  }



  def fltCds(name:String) = {
    val cds = FileUtils.readLines(new File(s"D:/水稻资源中心资料/Annotation/$name/$name.IGDBv1.Allset.cds.long.fasta")).asScala

    val row =  cds.map{x=>
      if(x.contains(">")){
        x.split('.').head
      }else{
        x
      }
    }

    FileUtils.writeLines(new File(s"D:/水稻资源中心资料/Annotation/$name/cds.flt.fa"),row.asJava)
  }

  def fltPep(name:String) = {
    val pep = FileUtils.readLines(new File(s"D:/水稻资源中心资料/Annotation/$name/$name.IGDBv1.Allset.pros.long.fasta")).asScala

    val row =  pep.map{x=>
      if(x.contains(">")){
        x.split('.').head
      }else{
        x
      }
    }

    FileUtils.writeLines(new File(s"D:/水稻资源中心资料/Annotation/$name/pep.flt.fa"),row.asJava)

  }

  def fltTrans(name : String) = {
    val trans = FileUtils.readLines(new File(s"D:/水稻资源中心资料/Annotation/$name/$name.IGDBv1.Allset.trans.long.fasta")).asScala

    val row =  trans.map{x=>
      if(x.contains(">")){
        x.split('.').head
      }else{
        x
      }
    }

    FileUtils.writeLines(new File(s"D:/水稻资源中心资料/Annotation/$name/trans.flt.fa"),row.asJava)
  }
}
