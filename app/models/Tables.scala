package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._
  import org.joda.time.DateTime
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Blocks.schema ++ Geneinfo.schema ++ Msu.schema ++ Refseq.schema ++ Sample.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Blocks
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param tp Database column tp SqlType(VARCHAR), Length(255,true)
   *  @param bid Database column bid SqlType(VARCHAR), Length(255,true)
   *  @param chr Database column chr SqlType(VARCHAR), Length(255,true)
   *  @param start Database column start SqlType(BIGINT)
   *  @param end Database column end SqlType(BIGINT)
   *  @param g2Chr Database column g2_chr SqlType(VARCHAR), Length(255,true)
   *  @param g2Start Database column g2_start SqlType(BIGINT)
   *  @param g2End Database column g2_end SqlType(BIGINT) */
  final case class BlocksRow(id: Int, tp: String, bid: String, chr: String, start: Long, end: Long, g2Chr: String, g2Start: Long, g2End: Long)
  /** GetResult implicit for fetching BlocksRow objects using plain SQL queries */
  implicit def GetResultBlocksRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[BlocksRow] = GR{
    prs => import prs._
    BlocksRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Long], <<[Long], <<[String], <<[Long], <<[Long]))
  }
  /** Table description of table blocks. Objects of this class serve as prototypes for rows in queries. */
  class Blocks(_tableTag: Tag) extends profile.api.Table[BlocksRow](_tableTag, Some("ricerc"), "blocks") {
    def * = (id, tp, bid, chr, start, end, g2Chr, g2Start, g2End) <> (BlocksRow.tupled, BlocksRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(tp), Rep.Some(bid), Rep.Some(chr), Rep.Some(start), Rep.Some(end), Rep.Some(g2Chr), Rep.Some(g2Start), Rep.Some(g2End)).shaped.<>({r=>import r._; _1.map(_=> BlocksRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column tp SqlType(VARCHAR), Length(255,true) */
    val tp: Rep[String] = column[String]("tp", O.Length(255,varying=true))
    /** Database column bid SqlType(VARCHAR), Length(255,true) */
    val bid: Rep[String] = column[String]("bid", O.Length(255,varying=true))
    /** Database column chr SqlType(VARCHAR), Length(255,true) */
    val chr: Rep[String] = column[String]("chr", O.Length(255,varying=true))
    /** Database column start SqlType(BIGINT) */
    val start: Rep[Long] = column[Long]("start")
    /** Database column end SqlType(BIGINT) */
    val end: Rep[Long] = column[Long]("end")
    /** Database column g2_chr SqlType(VARCHAR), Length(255,true) */
    val g2Chr: Rep[String] = column[String]("g2_chr", O.Length(255,varying=true))
    /** Database column g2_start SqlType(BIGINT) */
    val g2Start: Rep[Long] = column[Long]("g2_start")
    /** Database column g2_end SqlType(BIGINT) */
    val g2End: Rep[Long] = column[Long]("g2_end")

    /** Primary key of Blocks (database name blocks_PK) */
    val pk = primaryKey("blocks_PK", (id, tp, bid))
  }
  /** Collection-like TableQuery object for table Blocks */
  lazy val Blocks = new TableQuery(tag => new Blocks(tag))

  /** Entity class storing rows of table Geneinfo
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param species Database column species SqlType(VARCHAR), Length(255,true)
   *  @param geneid Database column geneid SqlType(VARCHAR), Length(255,true)
   *  @param chr Database column chr SqlType(TEXT)
   *  @param start Database column start SqlType(INT)
   *  @param end Database column end SqlType(INT)
   *  @param stand Database column stand SqlType(TEXT)
   *  @param go Database column go SqlType(TEXT)
   *  @param pfam Database column pfam SqlType(TEXT)
   *  @param ipr Database column ipr SqlType(TEXT)
   *  @param ko Database column ko SqlType(TEXT)
   *  @param pathway Database column pathway SqlType(TEXT) */
  final case class GeneinfoRow(id: Int, species: String, geneid: String, chr: String, start: Int, end: Int, stand: String, go: String, pfam: String, ipr: String, ko: String, pathway: String)
  /** GetResult implicit for fetching GeneinfoRow objects using plain SQL queries */
  implicit def GetResultGeneinfoRow(implicit e0: GR[Int], e1: GR[String]): GR[GeneinfoRow] = GR{
    prs => import prs._
    GeneinfoRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Int], <<[Int], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table geneinfo. Objects of this class serve as prototypes for rows in queries. */
  class Geneinfo(_tableTag: Tag) extends profile.api.Table[GeneinfoRow](_tableTag, Some("ricerc"), "geneinfo") {
    def * = (id, species, geneid, chr, start, end, stand, go, pfam, ipr, ko, pathway) <> (GeneinfoRow.tupled, GeneinfoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(species), Rep.Some(geneid), Rep.Some(chr), Rep.Some(start), Rep.Some(end), Rep.Some(stand), Rep.Some(go), Rep.Some(pfam), Rep.Some(ipr), Rep.Some(ko), Rep.Some(pathway)).shaped.<>({r=>import r._; _1.map(_=> GeneinfoRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column species SqlType(VARCHAR), Length(255,true) */
    val species: Rep[String] = column[String]("species", O.Length(255,varying=true))
    /** Database column geneid SqlType(VARCHAR), Length(255,true) */
    val geneid: Rep[String] = column[String]("geneid", O.Length(255,varying=true))
    /** Database column chr SqlType(TEXT) */
    val chr: Rep[String] = column[String]("chr")
    /** Database column start SqlType(INT) */
    val start: Rep[Int] = column[Int]("start")
    /** Database column end SqlType(INT) */
    val end: Rep[Int] = column[Int]("end")
    /** Database column stand SqlType(TEXT) */
    val stand: Rep[String] = column[String]("stand")
    /** Database column go SqlType(TEXT) */
    val go: Rep[String] = column[String]("go")
    /** Database column pfam SqlType(TEXT) */
    val pfam: Rep[String] = column[String]("pfam")
    /** Database column ipr SqlType(TEXT) */
    val ipr: Rep[String] = column[String]("ipr")
    /** Database column ko SqlType(TEXT) */
    val ko: Rep[String] = column[String]("ko")
    /** Database column pathway SqlType(TEXT) */
    val pathway: Rep[String] = column[String]("pathway")

    /** Primary key of Geneinfo (database name geneinfo_PK) */
    val pk = primaryKey("geneinfo_PK", (id, geneid))
  }
  /** Collection-like TableQuery object for table Geneinfo */
  lazy val Geneinfo = new TableQuery(tag => new Geneinfo(tag))

  /** Entity class storing rows of table Msu
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param geneid Database column geneid SqlType(VARCHAR), Length(255,true)
   *  @param chr Database column chr SqlType(TEXT)
   *  @param start Database column start SqlType(INT)
   *  @param end Database column end SqlType(INT)
   *  @param stand Database column stand SqlType(TEXT) */
  final case class MsuRow(id: Int, geneid: String, chr: String, start: Int, end: Int, stand: String)
  /** GetResult implicit for fetching MsuRow objects using plain SQL queries */
  implicit def GetResultMsuRow(implicit e0: GR[Int], e1: GR[String]): GR[MsuRow] = GR{
    prs => import prs._
    MsuRow.tupled((<<[Int], <<[String], <<[String], <<[Int], <<[Int], <<[String]))
  }
  /** Table description of table msu. Objects of this class serve as prototypes for rows in queries. */
  class Msu(_tableTag: Tag) extends profile.api.Table[MsuRow](_tableTag, Some("ricerc"), "msu") {
    def * = (id, geneid, chr, start, end, stand) <> (MsuRow.tupled, MsuRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(geneid), Rep.Some(chr), Rep.Some(start), Rep.Some(end), Rep.Some(stand)).shaped.<>({r=>import r._; _1.map(_=> MsuRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column geneid SqlType(VARCHAR), Length(255,true) */
    val geneid: Rep[String] = column[String]("geneid", O.Length(255,varying=true))
    /** Database column chr SqlType(TEXT) */
    val chr: Rep[String] = column[String]("chr")
    /** Database column start SqlType(INT) */
    val start: Rep[Int] = column[Int]("start")
    /** Database column end SqlType(INT) */
    val end: Rep[Int] = column[Int]("end")
    /** Database column stand SqlType(TEXT) */
    val stand: Rep[String] = column[String]("stand")

    /** Primary key of Msu (database name msu_PK) */
    val pk = primaryKey("msu_PK", (id, geneid))
  }
  /** Collection-like TableQuery object for table Msu */
  lazy val Msu = new TableQuery(tag => new Msu(tag))

  /** Entity class storing rows of table Refseq
   *  @param id Database column id SqlType(INT), PrimaryKey
   *  @param cds Database column cds SqlType(TEXT)
   *  @param pep Database column pep SqlType(TEXT)
   *  @param trans Database column trans SqlType(TEXT) */
  final case class RefseqRow(id: Int, cds: String, pep: String, trans: String)
  /** GetResult implicit for fetching RefseqRow objects using plain SQL queries */
  implicit def GetResultRefseqRow(implicit e0: GR[Int], e1: GR[String]): GR[RefseqRow] = GR{
    prs => import prs._
    RefseqRow.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table refseq. Objects of this class serve as prototypes for rows in queries. */
  class Refseq(_tableTag: Tag) extends profile.api.Table[RefseqRow](_tableTag, Some("ricerc"), "refseq") {
    def * = (id, cds, pep, trans) <> (RefseqRow.tupled, RefseqRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(cds), Rep.Some(pep), Rep.Some(trans)).shaped.<>({r=>import r._; _1.map(_=> RefseqRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column cds SqlType(TEXT) */
    val cds: Rep[String] = column[String]("cds")
    /** Database column pep SqlType(TEXT) */
    val pep: Rep[String] = column[String]("pep")
    /** Database column trans SqlType(TEXT) */
    val trans: Rep[String] = column[String]("trans")
  }
  /** Collection-like TableQuery object for table Refseq */
  lazy val Refseq = new TableQuery(tag => new Refseq(tag))

  /** Entity class storing rows of table Sample
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param sample Database column sample SqlType(VARCHAR), Length(255,true)
   *  @param nums Database column nums SqlType(INT)
   *  @param detail Database column detail SqlType(TEXT) */
  final case class SampleRow(id: Int, sample: String, nums: Int, detail: String)
  /** GetResult implicit for fetching SampleRow objects using plain SQL queries */
  implicit def GetResultSampleRow(implicit e0: GR[Int], e1: GR[String]): GR[SampleRow] = GR{
    prs => import prs._
    SampleRow.tupled((<<[Int], <<[String], <<[Int], <<[String]))
  }
  /** Table description of table sample. Objects of this class serve as prototypes for rows in queries. */
  class Sample(_tableTag: Tag) extends profile.api.Table[SampleRow](_tableTag, Some("ricerc"), "sample") {
    def * = (id, sample, nums, detail) <> (SampleRow.tupled, SampleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(sample), Rep.Some(nums), Rep.Some(detail)).shaped.<>({r=>import r._; _1.map(_=> SampleRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column sample SqlType(VARCHAR), Length(255,true) */
    val sample: Rep[String] = column[String]("sample", O.Length(255,varying=true))
    /** Database column nums SqlType(INT) */
    val nums: Rep[Int] = column[Int]("nums")
    /** Database column detail SqlType(TEXT) */
    val detail: Rep[String] = column[String]("detail")

    /** Primary key of Sample (database name sample_PK) */
    val pk = primaryKey("sample_PK", (id, sample))
  }
  /** Collection-like TableQuery object for table Sample */
  lazy val Sample = new TableQuery(tag => new Sample(tag))
}
