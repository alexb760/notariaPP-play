package models

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

case class User(id: Long, firstName: String, lastName: String, mobile: Long, email: String, login: String, pass: String)

case class UserFormData(firstName: String, lastName: String, mobile: Long, email: String, login: String, pass: String)
case class UserFormDataLogin(login: String, pass: String)

object UserForm {

  val form = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "mobile" -> longNumber,
      "email" -> email,
      "login" -> nonEmptyText,
      "pass" -> nonEmptyText
    )(UserFormData.apply)(UserFormData.unapply)
  )
}

object LoginForm{
  val formLogin = Form(
      mapping(
          "login" -> nonEmptyText,
          "pass" ->nonEmptyText
        )(UserFormDataLogin.apply)(UserFormDataLogin.unapply)
    )
}

class UserTableDef(tag: Tag) extends Table[User](tag, "user") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def mobile = column[Long]("mobile")
  def email = column[String]("email")
  def login = column[String]("usu_login")
  def pass = column[String]("usu_pass")

  override def * =
    (id, firstName, lastName, mobile, email, login,pass) <>(User.tupled, User.unapply)
}

object Users {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val users = TableQuery[UserTableDef]

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(users.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    dbConfig.db.run(users.result)
  }

  def loginUser(login: String, pass: String): Future[Option[User]] = {
    dbConfig.db.run(users.filter{u =>(u.login === login && u.pass === pass)}.result.headOption)
  }

  def authenticate(login: String, pass: String):  Future[Int] = {
  dbConfig.db.run(users.filter{u =>(u.login === login && u.pass === pass)}.length.result)

  }


}