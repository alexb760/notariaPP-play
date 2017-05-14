package services

import model.{Notaria, Notarias}
import scala.concurrent.Future

object NotariaService{
    
  def addNotaria(notaria: Notaria): Future[String] = {
    Notarias.add(notaria)
  }

  def deleteNotaria(id: Long): Future[Int] = {
    Notarias.delete(id)
  }

  def getNotaria(id: Long): Future[Option[Notaria]] = {
    Notarias.get(id)
  }

  def listAllNotarias: Future[Seq[Notaria]] = {
    Notarias.listAll
  }
    
}