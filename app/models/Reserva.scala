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

case class Reserva(
 id:Long,   
 servicio:Long,
 turno:String,
 suscriptorId:Long,
 estado:String,
 fechamod:String,
 fechares:String
)

case class ReservaFormData(
 servicio:Long,
 suscriptorId:Long,
 fechares:String
)

object ReservaForm {

  val form = Form(
    mapping(
      "servicio" -> longNumber,
      "suscriptorId" -> longNumber,
      "fechares" ->  nonEmptyText
    )(ReservaFormData.apply)(ReservaFormData.unapply)
  )
}

class ReservaTableDef(tag: Tag) extends Table[Reserva](tag, "not_reserva") {

  def id = column[Long]("res_id", O.PrimaryKey,O.AutoInc)
  def servicio = column[Long]("res_servicio_ser_id")
  def turno = column[String]("res_turno")
  def suscriptorId = column[Long]("res_suscriptorId")
  def estado = column[String]("res_estado")
  def fechamod = column[String]("res_fechamod")
  def fechares = column[String]("res_fechares")

  override def * =
    (id, 
    servicio, 
    turno, 
    suscriptorId, 
    estado,
    fechamod,
    fechares) <> (Reserva.tupled, Reserva.unapply)
    
    // A reified foreign key relation that can be navigated to create a join
    val servi = TableQuery[ServicioTableDef]
    def servis = foreignKey("fk_not_reserva_not_servicio1", servicio, servi)(_.id)
    
    val suscrip = TableQuery[SuscriptorTableDef]
    def suscrips = foreignKey("fk_not_reserva_not_suscriptor1", suscriptorId, suscrip)(_.id)
} 

object Reservas {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val reservas = TableQuery[ReservaTableDef]

  def add(reserva: Reserva): Future[String] = {
    Logger.info("estoy en las reservas")
    dbConfig.db.run(reservas += reserva).map(res => "Reserva agregada con exito").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
  
  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(reservas.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Seq[Reserva]] = {
    dbConfig.db.run(reservas.filter(_.id === id).result)
  }

  def update(id: Long, reserva: Reserva): Future[String] = {
    val reservaUpdate: Reserva = reserva.copy(id)
    dbConfig.db.run(reservas.filter(_.id === id).update(reservaUpdate)).map(res => "Actualizada con exito").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def listAll: Future[Seq[Reserva]] = {
    dbConfig.db.run(reservas.result)
  } 
  
}
