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

case class Requisito(id: Long, 
	servicioId: Long, 
	nombre: String, 
	descripcion: Option[String]
)

case class RequisitoFormData(
	nombre: String,
	descripcion: Option[String]
)

class RequisitoTableDef(tag: Tag) extends Table[Requisito](tag, "not_requisitos") {
	def id = column[Long]("req_id", O.PrimaryKey,O.AutoInc)
  	def servicioId = column[Long]("req_servicio_ser_id")
  	def nombre = column[String]("req_nombre")
  	def descripcion = column[Option[String]]("req_descripcion")

  	override def * = (id, servicioId, nombre, descripcion) <> (Requisito.tupled, Requisito.unapply)
}

object RequisitoForm  {
	
	val form = Form(
		mapping(
			"nombre" -> nonEmptyText,
			"descripcion" -> optional(text)
		)(RequisitoFormData.apply)(RequisitoFormData.unapply)
	)
}

object Requisitos {
	val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

	val requisitos = TableQuery[RequisitoTableDef]

	def getXIdServ(id: Long): Future[Seq[Requisito]] = {
    	dbConfig.db.run(requisitos.filter(_.servicioId === id).result)
  	}

}