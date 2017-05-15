package controllers

import models.{Servicio, ServicioForm}
import service.ServicioService
import play.api.mvc._
import scala.concurrent.Future
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationControllerServicio extends Controller {

  def index = Action.async { implicit request =>
    ServicioService.listAllServicios map { servicios =>
      Ok(views.html.servicioIndex(ServicioForm.form, servicios))
    }
  }

  def addServicio() = Action.async { implicit request =>
    ServicioForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.servicioIndex(errorForm, Seq.empty[Servicio]))),
      data => {
        val newServicio = Servicio(0, data.notariaId, data.nombre, data.descripcion, data.valor, data.estado, data.fechaMod)
        ServicioService.addUser(newServicio).map(res =>
          Redirect(routes.ApplicationControllerServicio.index())
        )
      })
  }

  def deleteServicio(id: Long) = Action.async { implicit request =>
    ServicioService.deleteUser(id) map { res =>
      Redirect(routes.ApplicationControllerServicio.index())
    }
  }

}
