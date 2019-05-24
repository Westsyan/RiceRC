package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test07 {


  def main(args: Array[String]): Unit = {


    new File("D:\\水稻资源中心资料\\共线性\\bed").listFiles().foreach{x=>

      val f = FileUtils.readLines(x).asScala
      val s =  f.tail.map(_.split("\t")).filter(x=> x(4).toInt>5000).map(_.mkString("\t"))
      val name = x.getName.split("_").head
      val s1 = s.map { x =>
        val c = x.split("\t")
        val c2 = c(9).split(":")
        Array(0,name,c(3),c.head,c(1),c(2),c2.head,c2(1).split("-").head,c2(1).split("-").last).mkString("\t")
      }

      FileUtils.writeLines(new File("D:\\水稻资源中心资料\\共线性\\bed/" + name + ".block" ),s1.asJava)


    }

  }


  def xx = {
    val f = FileUtils.readLines(new File("D:/水稻资源中心资料/共线性/bed/")).asScala

    val s =  f.tail.map(_.split("\t")).filter(x=> x(4).toInt>5000).map(_.mkString("\t"))

    val s1 = s.map { x =>
      val c = x.split("\t")
      val c2 = c(9).split(":")
      Array(0,"02428",c(3),c.head,c(1),c(2),c2.head,c2(1).split("-").head,c2(1).split("-").last).mkString("\t")
    }

    FileUtils.writeLines(new File("D:\\水稻资源中心资料\\02428_5K.block"),s1.asJava)

  }
}
