package models

import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import play.Logger
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

case class Servicio(
	id: Long, 
	notariaId: Long, 
	nombre: String, 
	descripcion: Option[String], 
	valor: Long, 
	estado:String, 
	fechaMod: String
)

case class ServicioFormData(
	nombre: String, 
	descripcion: Option[String], 
	valor: Long
)

case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

object ServicioForm {

  val form = Form(
    mapping(
      "nombre" -> nonEmptyText,
      "descripcion" -> optional(text),
      "valor" ->  longNumber
    )(ServicioFormData.apply)(ServicioFormData.unapply)
  )
}

class ServicioTableDef(tag: Tag) extends Table[Servicio](tag, "not_servicio") {

  def id = column[Long]("ser_id", O.PrimaryKey,O.AutoInc)
  def notariaId = column[Long]("ser_notariaid")
  def nombre = column[String]("ser_nombre")
  def descripcion = column[Option[String]]("ser_descripcion")
  def valor = column[Long]("ser_valor")
  def estado = column[String]("ser_estado")
  def fechaMod = column[String]("ser_fechamod")

  override def * =
    (id, 
  	notariaId, 
  	nombre, 
  	descripcion, 
  	valor, 
  	estado, 
  	fechaMod) <> (Servicio.tupled, Servicio.unapply)
    // A reified foreign key relation that can be navigated to create a join
    val notaria = TableQuery[NotariaTableDef]
    def notarias = foreignKey("fk_not_notaria", notariaId, notaria)(_.id)
}

//DAO
object Servicios {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val servicios = TableQuery[ServicioTableDef]
  val notarias = TableQuery[NotariaTableShort]

  def add(servicio: Servicio): Future[String] = {
    dbConfig.db.run(servicios += servicio).map(res => "Servicio agregado con éxito").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
  
  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(servicios.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Seq[Servicio]] = {
    dbConfig.db.run(servicios.filter(_.id === id).result)
  }

  def update(id: Long, servicio: Servicio): Future[String] = {
    val servicioUpdate: Servicio = servicio.copy(id)
    dbConfig.db.run(servicios.filter(_.id === id).update(servicioUpdate)).map(res => "Actualizado con exito").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def listAll: Future[Seq[Servicio]] = {
    dbConfig.db.run(servicios.result)
  }

  def count(filter: String): Future[Int] = {
    dbConfig.db.run(servicios.filter {servicio => servicio.nombre.toLowerCase like filter.toLowerCase}.length.result)
  }

  def listServicioNotaria {
    servicios.flatMap(s => 
      notarias.filter(n => s.notariaId === n.id)
              .map(n => (n.nombre))
    ).result

    (for(s <- servicios;
         n <- notarias if s.notariaId === n.id
     ) yield (n.nombre)
    ).result
  }

  def listServicioXIdNotaria(idNot: Long): Future[Seq[Servicio]] = {
    dbConfig.db.run(servicios.filter(_.notariaId === idNot).result)
  }

  def lstServNota{
    (servicios join notarias on (_.notariaId === _.id))
      .map{case (s, n) => (s.nombre, n.nombre)}.result
  }

  def list(page: Int = 0, pageSize: Int = 5, orderBy: Int = 1, filter: String = "%"): Future[Page[(Servicio, NotariaShort)]] = {
    
    val offset = pageSize * page
    val query =
      (for {
        (servicio, notaria) <- servicios joinLeft notarias on (_.notariaId === _.id)
          if servicio.nombre.toLowerCase like filter.toLowerCase
          } yield (servicio, notaria.map(_.id), notaria.map(_.nombre)))
            .drop(offset)
            .take(pageSize)

    for{
      totalRows <- count(filter)
      list = query.result.map { rows => rows.collect { case (servicio, Some(id), Some(nombre)) => (servicio, NotariaShort(id, nombre)) }}
      result <- dbConfig.db.run(list)
    } yield Page(result, page, offset, totalRows)
  }
  
}