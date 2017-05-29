package services

import models.{Suscriptor, Suscriptores}
import scala.concurrent.Future
import play.Logger;

object SuscriptorService {

  def addSuscriptor(suscriptor: Suscriptor): Future[String] = {
    Suscriptores.add(suscriptor)
  }

  def deleteSuscriptor(id: Long): Future[Int] = {
    Suscriptores.delete(id)
  }

  def getSuscriptor(id: Long): Future[Seq[Suscriptor]] = {
    Suscriptores.get(id)
  }

  def updateSuscriptor(id: Long, suscriptor: Suscriptor): Future[String] = {
    Suscriptores.update(id, suscriptor)
  }

}