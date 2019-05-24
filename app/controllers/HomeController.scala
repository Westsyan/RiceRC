package controllers

import java.io.File
import javax.inject.Inject

import dao.{geneinfoDao, sampleDao}
import play.api.Logger
import play.api.mvc.{Action, Controller}
import utils.Utils

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class HomeController @Inject()(geneinfodao: geneinfoDao) extends Controller {


  def home = Action {

    if (Utils.geneMap.size != 32) {
      val run = Future {
        geneinfodao.getAll.map { x =>
          x.groupBy(_.species).map { y =>
            Utils.geneMap.put(y._1, y._2)
          }
          Logger.info("GeneMap 添加成功！")
        }
      }
    }




    Ok(views.html.home.index())
  }


  def downloadBefore = Action { implicit request =>
    Ok(views.html.download.download())
  }

  def download(name: String) = Action { implicit request =>
    val file = new File(Utils.path + "/download/" + name)
    Ok.sendFile(file).withHeaders(
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> ("attachment; filename=" + file.getName),
      CONTENT_TYPE -> "application/x-download"
    )
  }

}
