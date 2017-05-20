package controllers

import models.{User, UserForm}
import play.api.mvc._
import scala.concurrent.Future
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationController extends Controller {

  def index = Action {
      Ok(views.html.index(""))
  }
  
  def login = Action {
      Ok(views.html.login())
  }

 def addUser() = Action.async { implicit request =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.index(""))),
      data => {
        val newUser = User(0, data.firstName, data.lastName, data.mobile, data.email, data.login, data.pass)
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

}