package service

import models.{User, Users, UserFormDataLogin}
import scala.concurrent.Future

object UserService {

  def addUser(user: User): Future[String] = {
    Users.add(user)
  }

  def deleteUser(id: Long): Future[Int] = {
    Users.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    Users.get(id)
  }

  def listAllUsers: Future[Seq[User]] = {
    Users.listAll
  }

  def login(login: String, pass: String): Future[Option[User]] = {
    Users.loginUser(login, pass)
  }
}