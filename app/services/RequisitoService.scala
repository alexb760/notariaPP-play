package service

import models.{Requisito, Requisitos}
import scala.concurrent.Future
import play.Logger

object RequisitoService {

  def getRequisitoXIdServ(idSer: Long): Future[Seq[Requisito]] = {
    Requisitos.getXIdServ(idSer)
  }

}