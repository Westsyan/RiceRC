package controllers


import javax.inject.Inject

import dao.geneinfoDao
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{Action, Controller}
import utils.Utils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SearchController@Inject()(geneinfodao:geneinfoDao) extends Controller {


  def searchBefore(input: String) = Action { implicit request =>
    Ok(views.html.search.search(input))
  }


  def getKeyWord(input: String, key: String) = {
    val index = input.toUpperCase.indexOf(key.toUpperCase)
    val value = input.slice(index, index + key.length)
    input.split("<span style='color:red;'>").map(_.split("</span>").
      map(_.replaceAll("(?i)" + key, "<span style='color:red;'>" + value + "</span>")).
      mkString("</span>")).mkString("<span style='color:red;'>")
  }

  def getKeyWordHead(input: String, key: String) = {
    val index = input.toUpperCase.indexOf(key.toUpperCase)
    val value = input.slice(index, index + key.length)
    input.replaceAll("(?i)" + key, "<span style='color:red;'>" + value + "</span>")
  }


  def searchResult(input: String) = {

    val keys = input.trim.split(" ")

    val option = Array("Gene ID", "GO", "Pfam", "Interpro", "Kegg", "Pathway")

    val x = Await.result(geneinfodao.searchByInput(keys.head.trim), Duration.Inf)
    val r = x.map{y=>
      y.id+y.geneid+y.go+y.ipr+y.pfam+y.pathway+y.ko
    }


    val flt = x.map(y => ((y.id, y.geneid), Array(y.geneid, y.go, y.pfam, y.ipr, y.ko, y.pathway)))

    val rows = getSearchResult(flt, option, input)

    Utils.searchMap.put(input.trim.toLowerCase, rows)
  }

  val pageForm = Form(
    mapping(
      "limit" -> number,
      "offset" -> number,
      "order" -> text,
      "search" -> optional(text),
      "sort" -> optional(text)
    )(PageData.apply)(PageData.unapply)
  )

  def getResult(tp: String, input: String) = Action { implicit request =>
    val page = pageForm.bindFromRequest.get

    val i = input.trim.toLowerCase

    if (!Utils.searchMap.contains(i)) {
      searchResult(i)
    }
    Utils.searchTimeMap.put(i, System.currentTimeMillis())

    val x = tp match {
      case "All" => Utils.searchMap(i)
      case x :String => Utils.searchMap(i).filter(_.value("geneid").toString.contains(x))
    }

    // val orderX = Utils.dealDataByPage(x, page)
    val total = x.size
    val tmpX = x.slice(page.offset, page.offset + page.limit)

    Ok(Json.obj("rows" -> tmpX, "total" -> total))
  }

  /**
    *
    * @param flts   第一次搜索结果： Int:结果ID，String:结果geneid，Array[String]:需要展示的内容形成的数组
    * @param option ： 需要展示的内容的字段名
    * @param input  ： 根据空格分隔的关键词数组
    * @return ： 返回一个JSON对象，直接前端调用
    */
  def getSearchResult(flts: Seq[((Int, String), Array[String])], option: Array[String], input: String): Seq[JsObject] = {

    var results = Array[((Int, String), Int)]()

    val keys = input.trim.split(" ")
    var flt = flts

    if (keys.length > 1) {

      //过滤
      keys.tail.foreach { y =>
        flt = flt.filter(z => z._2.count(_.toUpperCase.contains(y.toUpperCase)) != 0)
      }

      //多次过滤结果，得到基因ID和对应的字段名的下标
      results = keys.flatMap { k =>
        flt.map { y =>
          (y._1, y._2.indexOf(y._2.filter(_.toUpperCase.contains(k.toUpperCase)).head))
        }
      }.distinct

    } else {
      results = flt.map { y =>
        (y._1, y._2.indexOf(y._2.filter(_.toUpperCase.contains(keys.head.toUpperCase)).head))
      }.distinct.toArray

    }

    val fltMap = flt.toMap


    //整合
    val rows = results.groupBy(_._1).map { y =>
      val re = y._2.distinct.map { z =>
        //得到结果json
        var resu = fltMap(y._1)(z._2)

        resu = getKeyWordHead(resu, keys.head)

        if (keys.length != 1) {
          keys.tail.foreach { k =>
            resu = getKeyWord(resu, k)
          }
        }

        Json.obj("option" -> option(z._2), "result" -> resu)
      }


      (y._1._2, Json.obj("id" -> y._1._1, "geneid" -> y._1._2, "species" -> y._1._2.drop(2).dropRight(11),  "result" -> re))
    }.toSeq.sortBy(_._1).map(_._2)


    rows
  }


}
