package controllers

import java.io.File
import java.nio.file.Files

import dao._
import javax.inject.Inject
import models.Tables.GeneinfoRow
import org.apache.commons.io.FileUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import utils.Utils.windowsPath
import utils.{ExecCommand, Utils}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global

class ToolsController@Inject()(geneinfodao: geneinfoDao) extends Controller {


  def blastBefore = Action { implicit request =>
    Ok(views.html.tools.blast())
  }

  case class blastData(blastType: String, method: String, queryText: String, db: String, evalue: String, wordSize: String, maxTargetSeqs: String)

  val blastForm = Form(
    mapping(
      "blastType" -> text,
      "method" -> text,
      "queryText" -> text,
      "db" -> text,
      "evalue" -> text,
      "wordSize" -> text,
      "maxTargetSeqs" -> text
    )(blastData.apply)(blastData.unapply)
  )

  def blastRun = Action(parse.multipartFormData) { implicit request =>
    val data = blastForm.bindFromRequest.get
    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val seqFile = new File(tmpDir, "seq.fa")
    data.method match {
      case "text" =>
        FileUtils.writeStringToFile(seqFile, data.queryText)
      case "file" =>
        val file = request.body.file("file").get
        file.ref.moveTo(seqFile, replace = true)
    }

    val outXml = new File(tmpDir, "out.xml")
    val outHtml = new File(tmpDir, "out.html")
    val outTable = new File(tmpDir, "table.xls")
    val execCommand = new ExecCommand

    val blastType = data.blastType match {
      case "blastn" => "blastn"
      case "blastp" => "blastp"
      case "blastx" => "blastx"
    }

    val blast2Html = data.blastType match {
      case "blastx" => "blastx2html"
      case _ => "blast2html"
    }

    val database = Utils.path + "/blastData/" + data.db


    val command1 = Utils.path + "/tools/ncbi-blast-2.6.0+/bin/%s%s -query ".format(blastType, Utils.suffix) + seqFile.getAbsolutePath + " -subject " +
      database + " -outfmt 5 -evalue " + data.evalue + " -max_target_seqs " + data.maxTargetSeqs +
      " -word_size " + data.wordSize + " -out " + outXml.getAbsolutePath
    val command2 = "python " + Utils.path + s"/tools/blast2html/$blast2Html.py -i " + outXml.getAbsolutePath + " -o " + outHtml.getAbsolutePath + " --template %s/tools/blast2html/%s.jinja".format(Utils.path, blastType)
    val btt = Utils.path + "/tools/Blast2table -format 10 -xml " + outXml.getAbsolutePath + " -header -top > " + outTable.getAbsoluteFile
    val bttf = new File(tmpDir, "blastToTable.sh")


    FileUtils.writeStringToFile(bttf, btt)
    val command3 = "sh " + bttf.getAbsoluteFile
    execCommand.exect(Array(command1, command2, command3), tmpDir)
    if (execCommand.isSuccess) {
      val html = FileUtils.readFileToString(outHtml)
      val excel = FileUtils.readFileToString(outTable)
      //   val excel = ""
      //Utils.deleteDirectory(tmpDir)

      Ok(Json.obj("html" -> tmpDir.replaceAll("\\\\","/"), "excel" -> excel))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }

  def blastResultBefore(path:String) = Action{
    Ok(views.html.tools.blastResult(path))
  }

  def blastResult(path:String) = Action{
    val html = FileUtils.readFileToString(new File(path + "/out.html"))
    Utils.deleteDirectory(path)
    Ok(Json.obj("html" -> (html + "\n" + Utils.scriptHtml)))
  }

  def searchBefore = Action{implicit request=>
    Ok(views.html.tools.search())
  }

  def getJson(x: Seq[GeneinfoRow]): Seq[JsValue] = {
    x.map { y =>
      Json.obj("geneid" -> y.geneid,"chr" -> y.chr,"start" -> y.start,"end" -> y.end,"strand" -> y.stand,"go" -> y.go,
        "pfam" -> y.pfam,"ipr" -> y.ipr, "ko" -> y.ko ,"pathway" -> y.pathway )
    }
  }

  case class geneIdData(db:String,gene: String)

  val geneIdForm = Form(
    mapping(
      "db" -> text,
      "gene" -> text
    )(geneIdData.apply)(geneIdData.unapply)
  )

  def searchById = Action.async { implicit request =>
    val data = geneIdForm.bindFromRequest.get
    val id = data.gene.split(",").map(_.trim).distinct

    geneinfodao.getByGeneIds(data.db,id).map{x=>
      Ok(Json.toJson(getJson(x)))
    }
  }

  case class regionData(species:String, chr: String, start: String, end: String)

  val regionForm = Form(
    mapping(
      "species" -> text,
      "chr" -> text,
      "start" -> text,
      "end" -> text
    )(regionData.apply)(regionData.unapply)
  )

  def searchByRegion = Action.async { implicit request =>
    val data = regionForm.bindFromRequest.get

    geneinfodao.getByRegion(data.species,data.chr, data.start.toInt, data.end.toInt).map{x=>
      Ok(Json.toJson(getJson(x)))
    }
  }

  def seqFetchBefore = Action{implicit request=>
    Ok(views.html.tools.seqFetch())
  }

  case class regData(species:String,region: String)

  val regForm = Form(
    mapping(
      "species" -> text,
      "region" -> text
    )(regData.apply)(regData.unapply)
  )

  def seqRegion = Action { implicit request =>
    val data = regForm.bindFromRequest.get
    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val outFile = new File(tmpDir, "data.txt")
    val execCommand = new ExecCommand
    val command = if(new File(windowsPath).exists()){
      Utils.path + "/tools/samtools-0.1.19/samtools.exe faidx "+ Utils.path + "/blastData/" + data.species+" " + data.region
    }else{
      "samtools faidx " + Utils.path + "/blastData/" + data.species+" " + data.region
    }
    execCommand.execo(command, outFile)
    if (execCommand.isSuccess) {
      val dataStr = FileUtils.readFileToString(outFile)
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "true", "data" -> dataStr))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }

  def goBefore = Action { implicit request =>
    Ok(views.html.tools.go())
  }

  def keggBefore = Action { implicit request =>
    Ok(views.html.tools.kegg())
  }

  case class keggData(id: String, db:String,  m: String, n: String, c: String, pval: String)

  val keggForm = Form(
    mapping(
      "id" -> text,
      "db" -> text,
      "m" -> text,
      "n" -> text,
      "c" -> text,
      "pval" -> text
    )(keggData.apply)(keggData.unapply)
  )

  def keggResult = Action { implicit request =>
    val data = keggForm.bindFromRequest.get
    val Id = data.id
    val population = Utils.path + "/conf/" + data.db +".gene.xls"
    val association = Utils.path + "/conf/" + data.db +".kegg.xls"

    val geneId = Id.split(",").map(_.trim).distinct.toBuffer
    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val studies = new File(tmpDir, "tmp.txt")
    FileUtils.writeLines(studies, geneId.asJava)
    val study = studies.getAbsolutePath
    val output = new File(tmpDir, "KEGG_enrichment.txt")
    val o = output.getAbsolutePath
    // println(study,population,association,m,n,o,c,pval)
    //QVALUE在unix转译文本后可以使用
    val execCommand = new ExecCommand
    val command = "perl " + Utils.path + "/tools/identify.pl -study=" + study + " -population=" + population +
      " -association=" + association + " -m=" + data.m + " -n=" + data.n + " -o=" + o + " -c=" +
      data.c + " -maxp=" + data.pval
    execCommand.exect(command, tmpDir)
    if (execCommand.isSuccess) {
      val keggInfo = FileUtils.readLines(output).asScala
      val json = keggInfo.filter(_.split("\t").length == 9).map { x =>
        val all = x.split("\t")
        val hyper = "<a target='_blank' href='" + all(8) + "'><input type='button' class='link' value='linked'></a><a style='display: none'>" + all(8) + "</a>"
        Json.obj("term" -> all.head, "database" -> all(1), "id" -> all(2), "input_num" -> all(3), "back_num" -> all(4),
          "p-value" -> all(5), "correct_pval" -> all(6), "input" -> all(7), "hyperlink" -> hyper)
      }.drop(1)
      Utils.deleteDirectory(tmpDir)
      Ok(Json.toJson(json))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }

  case class goData(id: String , db: String, alpha: String, pval: String)

  val goForm = Form(
    mapping(
      "id" -> text,
      "db" -> text,
      "alpha" -> text,
      "pval" -> text
    )(goData.apply)(goData.unapply)
  )

  def goResult = Action { implicit request =>
    val data = goForm.bindFromRequest.get
    val Id = data.id
    val geneId = Id.split(",").map(_.trim).distinct.toBuffer
    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val studies = new File(tmpDir, "tmp.txt")
    FileUtils.writeLines(studies, geneId.asJava)
    val study = studies.getAbsolutePath
    val population = Utils.path + "/conf/" + data.db +".gene.xls"
    val association = Utils.path + "/conf/" + data.db +".go.xls"

    val o = new File(tmpDir, "GO_enrichment.txt")
    val execCommand = new ExecCommand
    val command = "python " + Utils.path + "/tools/goatools-0.5.7/scripts/find_enrichment.py --alpha=" + data.alpha +
      " --pval=" + data.pval + " --output " + o.getAbsolutePath + " " + study + " " + population + " " + association
    execCommand.exect(command, tmpDir)
    if (execCommand.isSuccess) {
      val goInfo = FileUtils.readLines(o).asScala
      val buffer = goInfo.drop(1)
      val json = buffer.map { x =>
        val all = x.split("\t")
        val goLink = "<a target='_blank' href='http://amigo.geneontology.org/amigo/term/" + all(0) + "'>" + all(0) + "</a>"
        Json.obj("id" -> goLink, "enrichment" -> all(1), "description" -> all(2), "ratio_in_study" -> all(3),
          "ratio_in_pop" -> all(4), "p_uncorrected" -> all(5), "p_bonferroni" -> all(6), "p_holm" -> all(7),
          "p_sidak" -> all(8), "p_fdr" -> all(9), "namespace" -> all(10), "genes_in_study" -> all(11))
      }
      Utils.deleteDirectory(tmpDir)
      Ok(Json.toJson(json))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }

  def enrichmentBefore = Action{implicit request=>
    Ok(views.html.tools.enrichment())
  }

  case class enrichData(method:String,dataType:String,gene:String,db:String,pValue:String)

  val enrichForm = Form(
    mapping(
      "method" -> text,
      "dataType" -> text,
      "gene" -> text,
      "db" -> text,
      "pValue" -> text
    )(enrichData.apply)(enrichData.unapply)
  )


  def enrichment = Action(parse.multipartFormData){implicit request=>
    val data = enrichForm.bindFromRequest.get

    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val seqFile = new File(tmpDir, "tmp.txt")
    data.dataType match {
      case "text" =>
        val geneId = data.gene.split(",").map(_.trim).distinct.toBuffer
        FileUtils.writeLines(seqFile, geneId.asJava)
      case "file" =>
        val file = request.body.file("file").get
        file.ref.moveTo(seqFile, replace = true)
    }

    data.method match{
      case "kegg" => kegg(data,tmpDir,seqFile.getAbsolutePath)
      case "go" => go(data,tmpDir,seqFile.getAbsolutePath)
    }
  }

  def kegg(data:enrichData,tmpDir:String,study:String) = {

    val population = Utils.enrichPath +  data.db +"_gene.xls"
    val association = Utils.enrichPath +  data.db +"_kegg.xls"

    val output = new File(tmpDir, "KEGG_enrichment.txt")
    val o = output.getAbsolutePath
    // println(study,population,association,m,n,o,c,pval)
    //QVALUE在unix转译文本后可以使用
    val execCommand = new ExecCommand
    val command = "perl " + Utils.path + "/tools/identify.pl -study=" + study + " -population=" + population +
      " -association=" + association + " -m=b" + " -n=BH" + " -o=" + o + " -c=5" + " -maxp=" + data.pValue
    execCommand.exect(command, tmpDir)
    if (execCommand.isSuccess) {
      val keggInfo = FileUtils.readLines(output).asScala
      val json = keggInfo.filter(_.split("\t").length == 9).map { x =>
        val all = x.split("\t")
        val hyper = "<a target='_blank' href='" + all(8) + "'>linked</a><a style='display: none'>" + all(8) + "</a>"
        Json.obj("term" -> all.head, "database" -> all(1), "id" -> all(2), "input_num" -> all(3), "back_num" -> all(4),
          "p-value" -> all(5), "correct_pval" -> all(6), "input" -> all(7), "hyperlink" -> hyper)
      }.drop(1)
      Utils.deleteDirectory(tmpDir)
      Ok(Json.toJson(json))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }

  def go(data:enrichData,tmpDir:String,study:String) = {

    val population = Utils.enrichPath + data.db +"_gene.xls"
    val association = Utils.enrichPath + data.db +"_go.xls"

    val o = new File(tmpDir, "GO_enrichment.txt")
    val execCommand = new ExecCommand
    val command = "python " + Utils.path + "/tools/goatools-0.5.7/scripts/find_enrichment.py --alpha=0.05 " +
      " --pval=" + data.pValue + " --output " + o.getAbsolutePath + " " + study + " " + population + " " + association
    execCommand.exect(command, tmpDir)
    if (execCommand.isSuccess) {
      val goInfo = FileUtils.readLines(o).asScala
      val buffer = goInfo.drop(1)
      val json = buffer.map { x =>
        val all = x.split("\t")
        val goLink = "<a target='_blank' href='http://amigo.geneontology.org/amigo/term/" + all(0) + "'>" + all(0) + "</a>"
        Json.obj("id" -> goLink, "enrichment" -> all(1), "description" -> all(2), "ratio_in_study" -> all(3),
          "ratio_in_pop" -> all(4), "p_uncorrected" -> all(5), "p_bonferroni" -> all(6), "p_holm" -> all(7),
          "p_sidak" -> all(8), "p_fdr" -> all(9), "namespace" -> all(10), "genes_in_study" -> all(11))
      }
      Utils.deleteDirectory(tmpDir)
      Ok(Json.toJson(json))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }


}
