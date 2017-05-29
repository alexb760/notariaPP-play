package controllers

import models._ //{User, UserForm, LoginForm, UserFormDataLogin}
import models.{User, UserForm, LoginForm, UserFormDataLogin}
import play.api._
import play.api.data._
import play.api.mvc._
import play.api.data.Forms._

import scala.concurrent.Future
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global

//import play.api.libs.functional.syntax.functionalCanBuildApplicative
//import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.JsPath
import play.api.libs.json.Json.toJson
import play.api.libs.json.Reads
import play.api.libs.json.Reads._
import play.api.libs.json.Reads.StringReads
import play.api.libs.json.Reads.functorReads


class ApplicationController extends Controller {

  def index = Action {
      Ok(views.html.index(""))
  }

  /*
  def login(login: String, pass: String) = Action.async { implicit request =>
    LoginForm.formLogin.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.login())),
      dataLogin => {
        //val loginUser = UserFormDataLogin(dataLogin.login, dataLogin.pass)
        UserService.login(login, pass).map( res =>
          Redirect(routes.ApplicationController.index))
      }
      )
  }
*/


lazy val loginForm = Form(
    tuple(
      "login" -> text,
      "password" -> text) verifying ("Invalid user or password", result => result match {
        case (login, password) => {
          //println("user=" + login + " password=" + password);
          println("+++++++: login");
          val userList = Users.authenticate(login, password)
          userList.onSuccess { case s => println(s"Valor--> $s") }
          userList == 1
        }
        case _ => false
      }))

  def login = Action { implicit request =>
    Ok(views.html.index(""))
  }



  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.ApplicationController.index()).withSession("login" -> user._1))
  }

/**

 def addUser() = Action.async { implicit request =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.index(""))),
      data => {
        val newUser = Users(0, data.firstName, data.lastName, data.mobile, data.email, data.login, data.pass)
        UserService.addUser(newUser).map(res =>
          Redirect(routes.ApplicationController.index())
        )
      })
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    UserService.deleteUser(id) map { res =>
      Redirect(routes.ApplicationController.index())
    }
  }
*/
}