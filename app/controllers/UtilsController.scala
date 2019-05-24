package controllers

import java.io.File
import java.nio.file.Files

import play.api.mvc.{Action, Controller, RangeResult}
import utils.Utils

case class PageData(limit: Int, offset: Int, order: String, search: Option[String], sort: Option[String])


class UtilsController extends Controller {

  def getImageByPhotoId(id: Int, types: String) = Action { implicit request =>
    val ifModifiedSinceStr = request.headers.get(IF_MODIFIED_SINCE)

    val path = ""

    val file = new File(path)

    val lastModifiedStr = file.lastModified().toString
    val MimeType = "image/png"
    val byteArray = Files.readAllBytes(file.toPath)
    if (ifModifiedSinceStr.isDefined && ifModifiedSinceStr.get == lastModifiedStr) {
      NotModified
    } else {
      Ok(byteArray).as(MimeType).withHeaders(LAST_MODIFIED -> file.lastModified().toString)
    }
  }

  def downloadExample(example: String) = Action { implicit request =>
    val filename = "\"" + example + "\""
    Ok.sendFile(new File(Utils.path + "/example/" + example)).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> ("attachment; filename=" + filename),
      CONTENT_TYPE -> "application/x-download"
    )
  }


  //断点续传
  def linkIgvData(path: String) = Action { implicit request =>
    RangeResult.ofFile(new File(Utils.path + "/igvData/" + path), request.headers.get(RANGE), Some("application/octet-stream"))
  }

  def download(file: String) = Action { implicit request =>
    val name = file.split("/").last
    val filename = "\"" + name + "\""
    Ok.sendFile(new File(Utils.path + "/download/" + file)).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> ("attachment; filename=" + filename),
      CONTENT_TYPE -> "application/x-download"
    )
    /*  Ok.sendFile(new File(url)).withHeaders(
//      ACCEPT_RANGES -> "bytes",
    //缓存
    CACHE_CONTROL -> "no-cache",
      //打开类型,inline:直接网页打开，attachment:下载
 //   CONTENT_DISPOSITION -> ("inline; filename=" + filename),

    CONTENT_RANGE -> length,
    CONTENT_TYPE -> "application/octet-stream"
    //断点续传
 //   CONTENT_ENCODING -> "gzip",
 //   VARY  -> ACCEPT_ENCODING
  )*/
  }

}

