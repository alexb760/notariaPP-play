package services

import models.{Reserva, Reservas}
import scala.concurrent.Future
import play.Logger;

object ReservaService {

  def addReserva(reserva: Reserva): Future[String] = {
    Reservas.add(reserva)
  }

  def deleteReserva(id: Long): Future[Int] = {
    Reservas.delete(id)
  }

  def getReserva(id: Long): Future[Seq[Reserva]] = {
    Reservas.get(id)
  }

  def updateReserva(id: Long, reserva: Reserva): Future[String] = {
    Reservas.update(id, reserva)
  }

}