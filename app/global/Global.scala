package global

import java.util.concurrent.{Executors, TimeUnit}

import _root_.utils.Utils
import play._

class Global extends GlobalSettings {

  override def onStart(app: Application): Unit = {

    val jarPath = this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val p = jarPath.split("/").map(_.trim).dropRight(3).mkString("/")

    val runnable = new Runnable {
      override def run() = {
        val time = System.currentTimeMillis()
        val clean = Utils.searchTimeMap.filter(x => (time - x._2) / 1000.0 > 3600)
        clean.keys.foreach { x =>
          Utils.searchMap.remove(x)
        }
        Utils.searchTimeMap = clean
        Logger.info("清理完成")
      }
    }

    val service = Executors.newSingleThreadScheduledExecutor()
    // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
    service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.HOURS)


    Logger.info("系统路径：" + p)
    Logger.info("Application has started")
  }

  override def onStop(app: Application): Unit = {
    Logger.info("Application shutdown...")
  }
}

