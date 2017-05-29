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

case class Suscriptor(
 id:Long,   
 nombre:String,
 apellido:String,
 nacio:String,
 correo:String,
 tipoDocumento:String,
 documento:String,
 usuario:String
)

case class SuscriptorFormData(
 nombre:String,
 apellido:String,
 nacio:String,
 correo:String,
 tipoDocumento:String,
 documento:String
)

object SuscriptorForm {

  val form = Form(
    mapping(
      "nombre" -> nonEmptyText,
      "apellido" -> nonEmptyText,
      "nacio" ->  nonEmptyText,
      "correo" ->  nonEmptyText,
      "tipoDocumento" ->  nonEmptyText,
      "documento" ->  nonEmptyText
    )(SuscriptorFormData.apply)(SuscriptorFormData.unapply)
  )
}

class SuscriptorTableDef(tag: Tag) extends Table[Suscriptor](tag, "not_suscriptor") {

  def id = column[Long]("sus_id", O.PrimaryKey,O.AutoInc)
  def nombre = column[String]("sus_nombre")
  def apellido = column[String]("sus_apellidos")
  def nacio = column[String]("sus_nacio")
  def correo = column[String]("sus_correo")
  def tipoDocumento = column[String]("sus_tipodocumento")
  def documento = column[String]("sus_documento")
  def usuario = column[String]("sus_usuario")

  override def * =
    (id, 
    nombre, 
    apellido, 
    nacio, 
    correo,
    tipoDocumento,
    documento,
    usuario) <> (Suscriptor.tupled, Suscriptor.unapply)
    
}

object Suscriptores {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val suscript = TableQuery[SuscriptorTableDef]

  def add(suscrip: Suscriptor): Future[String] = {
    Logger.info("estoy en suscriptores")
    dbConfig.db.run(suscript += suscrip).map(res => "Suscriptor agregado con exito").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
  
  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(suscript.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Seq[Suscriptor]] = {
    dbConfig.db.run(suscript.filter(_.id === id).result)
  }

  def update(id: Long, suscrip: Suscriptor): Future[String] = {
    val suscriptUpdate: Suscriptor = suscrip.copy(id)
    dbConfig.db.run(suscript.filter(_.id === id).update(suscriptUpdate)).map(res => "Actualizada con exito").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def listAll: Future[Seq[Suscriptor]] = {
    dbConfig.db.run(suscript.result)
  } 
  
}

