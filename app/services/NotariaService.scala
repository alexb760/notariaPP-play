package services

import models.{Notaria, Notarias}
import play.Logger
import scala.concurrent.Future

object NotariaService{
    
  def addNotaria(notaria: Notaria): Future[String] = {
    Logger.info("asd")
    Notarias.add(notaria)
  }

  def deleteNotaria(id: Long): Future[Int] = {
    Notarias.delete(id)
  }

  def getNotaria(id: Long): Future[Seq[Notaria]] = {
    Notarias.get(id)
  }

  def updateNotaria(id: Long, notaria: Notaria): Future[String] = {
    Notarias.update(id, notaria)
  }

  def listAllNotarias: Future[Seq[Notaria]] = {
    Notarias.listAll
  }
    
}