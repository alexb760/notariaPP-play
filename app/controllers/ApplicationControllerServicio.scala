package controllers

import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import models.{Servicio, ServicioForm}
import models.Notaria
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import service.ServicioService
import service.UserService

class ApplicationControllerServicio extends Controller {

  def index(idNot: Long) = Action.async { implicit request =>
    ServicioService.listServicioXidNot(idNot) map { servicios =>
      Ok(views.html.servicioIndex(idNot, ServicioForm.form, servicios))
    }
  }

  def addServicio(idNot: Long) = Action.async { implicit request =>
    var sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    var date: String = sdf.format(new java.util.Date())

    ServicioForm.form.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.servicioIndex(idNot, errorForm, Seq.empty[Servicio]))),
      data => {
        val newServicio = Servicio(0, idNot, data.nombre, data.descripcion, data.valor, 
                                    "A", date)
        ServicioService.addServicio(newServicio).map(res =>
          Redirect(routes.ApplicationControllerServicio.index(idNot))
        )
      })
  }

  def deleteServicio(id: Long, idNot: Long) = Action.async { implicit request =>
    ServicioService.deleteServicio(id) map { res =>
      Redirect(routes.ApplicationControllerServicio.index(idNot))
    }
  } 

  def list(page: Int, orderBy: Int, filter: String) = Action.async {implicit request => 
    val servicios = models.Servicios.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%"))
    servicios.map(cs => Ok(views.html.servicioList(cs, orderBy, filter)))
  }

  def editServicio(id: Long, idNot: Long) = Action.async { implicit request =>
    Logger.info("entro edit")
    ServicioService.getServicio(id) map { servicios =>
      Ok(views.html.servicioEdit(id, idNot, ServicioForm.form, servicios))
    }
  }

  def updateServicio(id: Long, idNot: Long) = Action.async { implicit request =>
    var sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    var date: String = sdf.format(new java.util.Date())

    ServicioForm.form.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.servicioEdit(id, idNot, errorForm, Seq.empty[Servicio]))),
      data => {
        val newServicio = Servicio(0, idNot, data.nombre, data.descripcion, data.valor, 
                                    "A", date)
        ServicioService.updateServicio(id, newServicio).map(res =>
          Redirect(routes.ApplicationControllerServicio.index(idNot))
        )
      })
  }

}
