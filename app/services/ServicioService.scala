package service

import models.{Servicio, Servicios}
import models.NotariaShort
import scala.concurrent.Future
import play.Logger

object ServicioService {

  def addServicio(servicio: Servicio): Future[String] = {
    Servicios.add(servicio)
  }

  def deleteServicio(id: Long): Future[Int] = {
    Servicios.delete(id)
  }

  def getServicio(id: Long): Future[Seq[Servicio]] = {
    Servicios.get(id)
  }

  def updateServicio(id: Long, servicio: Servicio): Future[String] = {
    Servicios.update(id, servicio)
  }

  def listAllServicios: Future[Seq[Servicio]] = {
    //Logger.info("" + Servicios.listAll)
    Servicios.listAll
  }

  def listServicioXidNot(idNot: Long): Future[Seq[Servicio]] = {
    Servicios.listServicioXIdNotaria(idNot)
  }

  def lstServXNotaria(idNot: Long): Future[Seq[(Servicio, NotariaShort)]] = {
    Servicios.lstServXNotaria(idNot)
  }
}