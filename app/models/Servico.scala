package models

import models.Notaria

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import java.sql.Date
import java.sql.Timestamp

import java.time.LocalDate

case class Servicio(
	id: Long, 
	notariaId: Long, 
	nombre: String, 
	descripcion: String, 
	valor: Long, 
	estado:String, 
	fechaMod: Date
	)

case class ServicioFormData(
	id: Long, 
	notariaId: Long, 
	nombre: String, 
	descripcion: String, 
	valor: Long, 
	estado:String, 
	fechaMod: Date
	)

object ServicioForm {

  val form = Form(
    mapping(
      "id" -> longNumber,
      "notariaId" -> longNumber,
      "nombre" -> nonEmptyText,
      "descripcion" -> nonEmptyText,
      "valor" ->  longNumber,
      "estado" -> nonEmptyText,
      "fechaMod" -> sqlDate
    )(ServicioFormData.apply)(ServicioFormData.unapply)
  )
}

class ServicioTableDef(tag: Tag) extends Table[Servicio](tag, "not_servicio") {

  def id = column[Long]("ser_id", O.PrimaryKey,O.AutoInc)
  def notariaId = column[Long]("ser_notariaid")
  def nombre = column[String]("ser_nombre")
  def descripcion = column[String]("ser_descripcion")
  def valor = column[Long]("ser_valor")
  def estado = column[String]("ser_estado")
  def fechaMod = column[Date]("ser_fechamod")

  override def * =
    (id, 
	notariaId, 
	nombre, 
	descripcion, 
	valor, 
	estado, 
	fechaMod) <>(Servicio.tupled, Servicio.unapply)
    // A reified foreign key relation that can be navigated to create a join
  //def Notaria = foreignKey("fk_table1_not_notaria", notariaId, Notaria)(_.id)
}

//DAO
object Servicios {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val servicios = TableQuery[ServicioTableDef]

  def add(servicio: Servicio): Future[String] = {
    dbConfig.db.run(servicios += servicio).map(res => "Servicio successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
  
  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(servicios.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Servicio]] = {
    dbConfig.db.run(servicios.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Servicio]] = {
    dbConfig.db.run(servicios.result)
  } 
  
}
