package service

import models.{Servicio, Servicios}
import scala.concurrent.Future

object ServicioService {

  def addUser(servicio: Servicio): Future[String] = {
    Servicios.add(servicio)
  }

  def deleteUser(id: Long): Future[Int] = {
    Servicios.delete(id)
  }

  def getUser(id: Long): Future[Option[Servicio]] = {
    Servicios.get(id)
  }

  def listAllServicios: Future[Seq[Servicio]] = {
    Servicios.listAll
  }
}