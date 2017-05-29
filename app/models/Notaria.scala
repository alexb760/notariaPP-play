package models

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import play.Logger

case class Notaria(id: Long, nombre: String, direccion: String, telefono: String, telefono2: Option[String], horario: String, correo: String, usuario: String, fechacrea: String)

case class NotariaShort(id: Long, nombre: String)

case class NotariaFormData(nombre: String, direccion: String, telefono: String, telefono2: Option[String], horario: String, correo: String)

object NotariaForm {

  val form = Form(
    mapping(
      "nombre" -> nonEmptyText,
      "direccion" -> nonEmptyText,
      "telefono" -> nonEmptyText,
      "telefono2" -> optional(text),
      "horario" ->  nonEmptyText,
      "correo" -> nonEmptyText
    )(NotariaFormData.apply)(NotariaFormData.unapply)
  )
}

class NotariaTableShort(tag: Tag) extends Table[NotariaShort](tag, "not_notaria") {
  def id = column[Long]("not_id", O.PrimaryKey,O.AutoInc)
  def nombre = column[String]("not_nombre")

  override def * =
    (id, nombre) <> (NotariaShort.tupled, NotariaShort.unapply)
}

class NotariaTableDef(tag: Tag) extends Table[Notaria](tag, "not_notaria") {

  def id = column[Long]("not_id", O.PrimaryKey,O.AutoInc)
  def nombre = column[String]("not_nombre")
  def direccion = column[String]("not_direccion")
  def telefono = column[String]("not_telefono")
  def telefono2 = column[Option[String]]("not_telefono2")
  def horario = column[String]("not_horario")
  def correo = column[String]("not_correo")
  def usuario = column[String]("not_usuario")
  def fechacrea = column[String]("not_fechacrea")

  override def * =
    (id, nombre, direccion, telefono, telefono2,horario,correo, usuario, fechacrea) <> (Notaria.tupled, Notaria.unapply)
} 

object Notarias {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val notarias = TableQuery[NotariaTableDef]

  def add(notaria: Notaria): Future[String] = {
    Logger.info("asdasd")
    dbConfig.db.run(notarias += notaria).map(res => "Notaria agregada con exito").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
  
  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(notarias.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Seq[Notaria]] = {
    dbConfig.db.run(notarias.filter(_.id === id).result)
  }

  def update(id: Long, notaria: Notaria): Future[String] = {
    val notariaUpdate: Notaria = notaria.copy(id)
    dbConfig.db.run(notarias.filter(_.id === id).update(notariaUpdate)).map(res => "Actualizada con exito").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def listAll: Future[Seq[Notaria]] = {
    dbConfig.db.run(notarias.result)
  } 
  
}
  
  